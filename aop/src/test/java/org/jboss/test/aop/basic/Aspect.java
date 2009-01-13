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

import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.FieldInvocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class Aspect
{
   public Object interceptConstructor(ConstructorInvocation invocation) throws Throwable
   {
      Object rtn = invocation.invokeNext();
      POJOAspectTester pojo = (POJOAspectTester) rtn;
      pojo.marker = "interceptConstructor";
      return rtn;
   }

   public Object interceptField(FieldInvocation invocation) throws Throwable
   {
      Object rtn = invocation.invokeNext();
      POJOAspectTester pojo = (POJOAspectTester) invocation.getTargetObject();
      pojo.marker = "interceptField";
      return rtn;
   }

   public Object interceptMethod(MethodInvocation invocation) throws Throwable
   {
      Object rtn = invocation.invokeNext();
      POJOAspectTester pojo = (POJOAspectTester) invocation.getTargetObject();
      pojo.marker = "interceptMethod";
      return rtn;
   }
}
