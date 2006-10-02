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
package org.jboss.aop.proxy;

import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.util.reference.MethodPersistentReference;

import java.lang.reflect.Method;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ProxyMethodInvocation extends MethodInvocation
{
   static final long serialVersionUID = 2451434088790560691L;

   private transient MethodMapped proxy;

   public ProxyMethodInvocation(MethodMapped mapped, MethodInfo info, Interceptor[] interceptors)
   {
      super(info, interceptors);
      this.proxy = mapped;
   }

   public ProxyMethodInvocation(MethodMapped mapped, Interceptor[] interceptors)
   {
      super(interceptors);
      this.proxy = mapped;
   }

   public ProxyMethodInvocation()
   {

   }

   public Method getMethod()
   {
      MethodPersistentReference ref = (MethodPersistentReference)proxy.getMethodMap().get(new Long(methodHash));
      return (Method)ref.get();
   }

   public Method getActualMethod()
   {
      return getMethod();
   }
}
