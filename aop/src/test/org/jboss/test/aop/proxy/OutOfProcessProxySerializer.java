/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/ 
package org.jboss.test.aop.proxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.advice.ScopedInterceptorFactory;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.proxy.container.AOPProxyFactory;
import org.jboss.aop.proxy.container.AOPProxyFactoryMixin;
import org.jboss.aop.proxy.container.AOPProxyFactoryParameters;
import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class OutOfProcessProxySerializer extends Assert
{
   final static int FEW_ARGS = 1;
   final static int MANY_ARGS = 2;
   final static int NO_SUCH_FILE = 3;
   final static int GENERAL_ERROR = 4;
 
   public static void main (String[] args)
   {
      if (args.length == 0)
      {
         System.exit(FEW_ARGS);
      }
      else if (args.length > 1)
      {
         System.exit(MANY_ARGS);
      }
      
      File file = new File(args[0]);
      if (!file.exists())
      {
         System.exit(NO_SUCH_FILE);
      }
      
      try
      {
         createAndSerializeProxy(file);
      }
      catch (Throwable t)
      {
         PrintStream out = null;
         try
         {
            out = new PrintStream(new FileOutputStream(file));
            t.printStackTrace(out);
         }
         catch(Exception e)
         {
         }
         finally
         {
            try
            {
               out.close();
            }
            catch(Exception e)
            {
            }
         }
         System.exit(GENERAL_ERROR);
      }
   }
   
   public static void createAndSerializeProxy(File file) throws Exception
   {
      AspectManager manager = AspectManager.instance();
      AspectDefinition def = new AspectDefinition("icptr", Scope.PER_VM, new GenericAspectFactory(TestInterceptor.class.getName(), null));
      InterceptorFactory advice = new ScopedInterceptorFactory(def);
      PointcutExpression pointcut = new PointcutExpression("pc1", "execution(* $instanceof{" + SomeInterface.class.getName() + "}->helloWorld(..))");
      InterceptorFactory[] interceptors = {advice};
      AdviceBinding binding = new AdviceBinding("binding1", pointcut, null, null, interceptors);

      manager.addAspectDefinition(def);
      manager.addInterceptorFactory(advice.getName(), advice);
      manager.addPointcut(pointcut);
      manager.addBinding(binding);

      System.out.println("===>  ENABLE ADDITION OF ASPECTS IN OUTOFPROCESSPROXYSERIALIZER");
//      AspectDefinition def2 = new AspectDefinition("aspect", Scope.PER_VM, new GenericAspectFactory(TestAspect.class.getName(), null));
//      AdviceFactory advice2 = new AdviceFactory(def2, "advice");
//      PointcutExpression pointcut2 = new PointcutExpression("pc2", "execution(* $instanceof{" + SomeInterface.class.getName() + "}->otherWorld(..))");
//      InterceptorFactory[] interceptors2 = {advice2};
//      AdviceBinding binding2 = new AdviceBinding("binding2", pointcut2, null, null, interceptors2);
//
//      manager.addAspectDefinition(def2);
//      manager.addInterceptorFactory(advice2.getName(), advice2);
//      manager.addPointcut(pointcut2);
//      manager.addBinding(binding2);
         
      AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
      params.setInterfaces(new Class[] {SomeInterface.class});
      params.setMixins(new AOPProxyFactoryMixin[] {
            new AOPProxyFactoryMixin(OtherMixin.class, new Class[] {OtherMixinInterface.class, OtherMixinInterface2.class}, "20")
      });
      
      params.setTarget(new SerializablePOJO());
      AOPProxyFactory factory = new GeneratedAOPProxyFactory();
      SomeInterface si = (SomeInterface)factory.createAdvisedProxy(params);
      
      TestInterceptor.invoked = false;
      TestAspect.invoked = false;
      si.helloWorld();
      assertTrue(TestInterceptor.invoked);
      assertFalse(TestAspect.invoked);
      
      System.out.println("===> ENABLE CHECKS OF ASPECTS IN OUTOFPROCESSPROXYSERIALIZER");
//      TestInterceptor.invoked = false;
//      TestAspect.invoked = false;
//      si.otherWorld();
//      assertFalse(TestInterceptor.invoked);
//      assertTrue(TestAspect.invoked);
      
      ObjectOutputStream out = null;
      try
      {
         out = new ObjectOutputStream(new FileOutputStream(file));
         out.writeObject(si);
      }
      finally
      {
         try
         {
            out.close();
         }
         catch(Exception e)
         {
         }
      }
   }
}
