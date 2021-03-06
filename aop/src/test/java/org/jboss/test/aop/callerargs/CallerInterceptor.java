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
package org.jboss.test.aop.callerargs;

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.ConstructorCalledByConstructorInvocation;
import org.jboss.aop.joinpoint.ConstructorCalledByMethodInvocation;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodCalledByConstructorInvocation;
import org.jboss.aop.joinpoint.MethodCalledByMethodInvocation;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class CallerInterceptor implements Interceptor
{
   public static boolean intercepted;
   public static int argsLength;
   public static Object[] args;

   public String getName()
   {
      return "CallerInterceptor";
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      System.out.println("caller intercepted");
      intercepted = true;
      Object[] invArgs = getArguments(invocation);
      if (invArgs == null)
      {
         argsLength = 0;
      }
      else
      {
         argsLength = invArgs.length;
      }
      args = invArgs;
      
      return invocation.invokeNext();
   }

   private Object[] getArguments(Invocation invocation)
   {
      if (invocation instanceof ConstructorCalledByConstructorInvocation)
      {
         return ((ConstructorCalledByConstructorInvocation)invocation).getArguments();
      }
      else if (invocation instanceof ConstructorCalledByMethodInvocation)
      {
         return ((ConstructorCalledByMethodInvocation)invocation).getArguments();
      } 
      else if (invocation instanceof MethodCalledByConstructorInvocation)
      {
         return ((MethodCalledByConstructorInvocation)invocation).getArguments();
      }
      else if (invocation instanceof MethodCalledByMethodInvocation)
      {
         return ((MethodCalledByMethodInvocation)invocation).getArguments();
      }
      return null; 
   }
}
