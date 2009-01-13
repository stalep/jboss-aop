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
package org.jboss.test.aop.basic;

import org.jboss.logging.Logger;

import java.lang.reflect.Method;

/**
 * @author ifedorenko
 */
public class SimpleBeanInterceptor implements org.jboss.aop.advice.Interceptor
{

   static Logger log = Logger.getLogger(SimpleBeanInterceptor.class);
   public static final String RETURN_VALUE = "aspect";

   public String getName()
   {
      return "SimpleInterceptor";
   }

   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      log.info("in SimpleBeanInterceptor");
      org.jboss.aop.joinpoint.MethodInvocation methodInvocation = (org.jboss.aop.joinpoint.MethodInvocation) invocation;
      final Method m = methodInvocation.getMethod();
      if ("getTest".equals(m.getName())
              && "org.jboss.test.aop.simpleejb.SimpleBean".equals(m.getDeclaringClass().getName()))
      {
         return RETURN_VALUE;
      }
      else
      {
         return invocation.invokeNext();
      }
   }
}
