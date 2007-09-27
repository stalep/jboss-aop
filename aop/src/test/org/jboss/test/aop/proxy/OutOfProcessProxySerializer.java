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
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AdviceFactory;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.proxy.container.AOPProxyFactory;
import org.jboss.aop.proxy.container.AOPProxyFactoryParameters;
import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class OutOfProcessProxySerializer extends Assert
{
   final static int WRONG_ARGS = 1;
   final static int NO_SUCH_FILE = 2;
   final static int GENERAL_ERROR = 3;
   
   public static void main (String[] args)
   {
      if (args.length != 1)
      {
         System.exit(WRONG_ARGS);
      }
      
      File file = new File(args[0]);
      if (!file.exists())
      {
         System.exit(NO_SUCH_FILE);
      }
      
      try
      {
         
//         InstanceDomain domain = new InstanceDomain(AspectManager.instance(), "blah", false);
//
//         
//         InterfaceIntroduction intro = new InterfaceIntroduction("intro", "*", null);
//         String[] intfs = {MixinInterface.class.getName()};
//         InterfaceIntroduction.Mixin mixin = new InterfaceIntroduction.Mixin(Mixin.class.getName(), intfs, null, false);
//         intro.getMixins().add(mixin);
//         domain.addInterfaceIntroduction(intro);
//
//         
//         AspectDefinition def = new AspectDefinition("aspect", Scope.PER_VM, new GenericAspectFactory(EchoInterceptor.class.getName(), null));
//         domain.addAspectDefinition(def);
//         AdviceFactory advice = new AdviceFactory(def, "invoke");
//         domain.addInterceptorFactory(advice.getName(), advice);
//         {
//         PointcutExpression pointcut = new PointcutExpression("pointcut", "execution(java.lang.String " + POJO.class.getName() + "->helloWorld(..))");
//         domain.addPointcut(pointcut);
//         InterceptorFactory[] interceptors = {advice};
//         AdviceBinding binding = new AdviceBinding("pojo-binding", pointcut, null, null, interceptors);
//         domain.addBinding(binding);
//         }
//
//         {
//         PointcutExpression pointcut = new PointcutExpression("mixin-pointcut", "execution(java.lang.String $instanceof{" + MixinInterface.class.getName() + "}->intercepted(..))");
//         domain.addPointcut(pointcut);
//         InterceptorFactory[] interceptors = {advice};
//         AdviceBinding binding = new AdviceBinding("mixin-binding", pointcut, null, null, interceptors);
//         domain.addBinding(binding);
//         }
//
//         
//         AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
//         AOPProxyFactory factory = new GeneratedAOPProxyFactory();
//         params.set
//         factory.createAdvisedProxy(params);
//         
//         Class proxyClass = ContainerProxyFactory.getProxyClass(POJO.class, domain);
//         ClassProxyContainer container = new ClassProxyContainer("test", domain);
//         domain.setAdvisor(container);
//         container.setClass(proxyClass);
//         container.initializeClassContainer();
//         POJO proxy = (POJO) proxyClass.newInstance();
//         AspectManaged cp = (AspectManaged)proxy;
//         cp.setAdvisor(container);
//         Delegate delegate = (Delegate)cp;
//         delegate.setDelegate(new POJO());
//
//         MixinInterface mi = (MixinInterface) proxy;
//         System.out.println("--- mixin");
//         assertEquals(mi.hello("mixin"), "mixin");
//         System.out.println("--- hw");
//         assertEquals("echoed", proxy.helloWorld());
//         System.out.println("--- icptd");
//         assertEquals("echoed", mi.intercepted("error"));
//         
         
         AspectManager manager = AspectManager.instance();
//         AspectDefinition def = new AspectDefinition("perinstanceaspect", Scope.PER_INSTANCE, new GenericAspectFactory(TestInterceptor.class.getName(), null));
         AspectDefinition def = new AspectDefinition("perinstanceaspect", Scope.PER_VM, new GenericAspectFactory(TestInterceptor.class.getName(), null));
         AdviceFactory advice = new AdviceFactory(def, "invoke");
         PointcutExpression pointcut = new PointcutExpression("perinstancepointcut", "execution(* $instanceof{" + SomeInterface.class.getName() + "}->*(..))");
         InterceptorFactory[] interceptors = {advice};
         AdviceBinding binding = new AdviceBinding("perinstancebinding", pointcut, null, null, interceptors);

         manager.addAspectDefinition(def);
         manager.addInterceptorFactory(advice.getName(), advice);
         manager.addPointcut(pointcut);
         manager.addBinding(binding);
            
         AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
         params.setInterfaces(new Class[] {SomeInterface.class});
         params.setTarget(new SerializablePOJO());
         
         AOPProxyFactory factory = new GeneratedAOPProxyFactory();
         SomeInterface si = (SomeInterface)factory.createAdvisedProxy(params);
         
         si.helloWorld();
         
         assertTrue(TestInterceptor.invoked);
         
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
}
