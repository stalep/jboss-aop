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
package org.jboss.aspects.remoting;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.jboss.aop.Dispatcher;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.util.MethodHashing;
import org.jboss.aop.util.PayloadKey;
import org.jboss.remoting.InvokerLocator;

/**
 * comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public class PojiProxy implements java.io.Serializable, InvocationHandler
{
   private static final long serialVersionUID = 8054816523858555978L;

   private Object oid;
   private InvokerLocator uri;
   private Interceptor[] interceptors;

   public PojiProxy(Object oid, InvokerLocator uri, Interceptor[] interceptors)
   {
      this.oid = oid;
      this.uri = uri;
      this.interceptors = interceptors;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      long hash = MethodHashing.calculateHash(method);
      MethodInvocation sri = new MethodInvocation(interceptors, hash, method, method, null);
      sri.setArguments(args);
      sri.getMetaData().addMetaData(Dispatcher.DISPATCHER, Dispatcher.OID, oid, PayloadKey.AS_IS);
      sri.getMetaData().addMetaData(InvokeRemoteInterceptor.REMOTING, InvokeRemoteInterceptor.INVOKER_LOCATOR, uri, PayloadKey.AS_IS);
      sri.getMetaData().addMetaData(InvokeRemoteInterceptor.REMOTING, InvokeRemoteInterceptor.SUBSYSTEM, "AOP", PayloadKey.AS_IS);
      return sri.invokeNext();
   }
}
