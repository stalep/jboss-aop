/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
package org.jboss.aop.pointcut;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import org.jboss.aop.Advisor;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.ast.ClassExpression;

/**
 * Strategy to allow for different handling for the pointcut matchers for different types of advisor
 * This one uses the "default" strategy, used for proxy advisors. The main problem for these is that
 * methods from interface introductions will have the class of the interface rather than the class of 
 * the advised class.
 * 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class ProxyMatcherStrategy extends MatcherStrategy
{
   public ProxyMatcherStrategy()
   {
   }
   
   protected boolean checkIntroductions(Class clazz, ClassExpression instanceOf, Advisor advisor)
   {
      try
      {
         if (advisor != null)
         {
            ArrayList intros = advisor.getInterfaceIntroductions();
            if (intros.size() > 0)
            {
               for (Iterator itIntro = intros.iterator() ; itIntro.hasNext() ; )
               {
                  InterfaceIntroduction intro = (InterfaceIntroduction)itIntro.next();
                  String[] introductions = intro.getInterfaces();
                  if (introductions != null)
                  {
                     for (int i = 0 ; i < introductions.length ; i++)
                     {
                        Class iface = Thread.currentThread().getContextClassLoader().loadClass(introductions[i]);
                        if (subtypeOf(iface, instanceOf, advisor)) return true;
                     }
                  }
                  ArrayList mixins = intro.getMixins();
                  if (mixins.size() > 0)
                  {
                     for (Iterator itMixin = mixins.iterator() ; itMixin.hasNext() ; )
                     {
                        InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin)itMixin.next();
                        String[] mixinInterfaces = mixin.getInterfaces();
                        if (mixinInterfaces != null)
                        {
                           for (int i = 0 ; i < mixinInterfaces.length ; i++)
                           {
                              Class iface = Thread.currentThread().getContextClassLoader().loadClass(mixinInterfaces[i]);
                              if (subtypeOf(iface, instanceOf, advisor)) return true;                              
                           }
                        }
                     }
                  }
               }
            }
         }
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      
      return false;
   }

   /**
    * Interface Introduced methods on the proxy will have the wrong declaring class for the matcher,
    * use the advisor class if it is an interface
    */
   public Class getDeclaringClass(Advisor advisor, Method m)
   {
      final Class methodClass = m.getDeclaringClass();
      final Class advisorClass = advisor.getClazz();
      
      if (advisorClass != null && methodClass.isInterface())
      {
         return advisorClass;
      }
      return methodClass;
   }
   
   
}
