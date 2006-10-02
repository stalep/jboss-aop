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
package org.jboss.test.aop.jdk15.annotated;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jboss.aop.joinpoint.FieldReadInvocation;
import org.jboss.aop.joinpoint.FieldWriteInvocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
@org.jboss.aop.InterceptorDef (scope=org.jboss.aop.advice.Scope.PER_VM)
@org.jboss.aop.Bind (pointcut="execution(* org.jboss.test.aop.jdk15.annotated.VariaPOJO*->methodWithInterceptor())")
public class CountingInterceptor implements org.jboss.aop.advice.Interceptor
{

   public String getName()
   {
      return "CountingInterceptor";
   }

   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      System.out.println("CountingInterceptor interception: " + invocation.getClass().getName());
      count++;
      System.out.println("CountingInterceptor.count:" + count);
      return invocation.invokeNext();
   }

   public static int count;
}

