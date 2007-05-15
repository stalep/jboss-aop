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
package org.jboss.aspects.asynch;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jboss.aop.ClassInstanceAdvisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.instrument.Untransformable;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.proxy.Proxy;
import org.jboss.aop.util.MethodHashing;
import org.jboss.util.id.GUID;

/**
 * An invocation handler for the Future interface using dynamic proxies. It is an alternative to
 * having generated proxies for use with EJB 3, avoiding client relying on javassist in the 
 * EJB 3 client proxies
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class FutureInvocationHandler extends Proxy implements InvocationHandler, Untransformable, Serializable
{
   private static final long serialVersionUID = -2343948303742422382L;
   
   private Map methodMap = new HashMap();

   public FutureInvocationHandler()
   {
      // FIXME FutureInvocationHandler constructor
      super();
   }

   public static Object createFutureProxy(GUID guid, ClassLoader loader, Class[] interfaces) throws Exception
   {
      FutureInvocationHandler ih = new FutureInvocationHandler();
      ih.instanceAdvisor = new ClassInstanceAdvisor();
      ih.mixins = null;
      ih.interfaces = interfaces;
      ih.guid = guid;
      return java.lang.reflect.Proxy.newProxyInstance(loader, interfaces, ih);
   }

   
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      if (method.getName().equals("_getInstanceAdvisor"))
      {
         return _getInstanceAdvisor();
      }
      else if (method.getName().equals("_setInstanceAdvisor") && 
            method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(InstanceAdvisor.class))
      {
         _setInstanceAdvisor((InstanceAdvisor)args[0]);
         return null;
      }
      
      Interceptor[] interceptors = instanceAdvisor.getInterceptors();
      long hash = MethodHashing.calculateHash(method);
      MethodInvocation invocation = new MethodInvocation(interceptors, hash, method, method, null);
      invocation.setInstanceResolver(instanceAdvisor.getMetaData()); 
      invocation.setArguments(args); 
      return invocation.invokeNext(); 
   }
   

   /**
    * Override Proxy implementation so we get default behaviour.
    * Reason is to avoid client dependencies on javassist in EJB 3 asynchronous proxies
    */
   public Object writeReplace() throws ObjectStreamException
   {
      return this;
   }

   public Map getMethodMap()
   {
      //I don't think we need to populate this for now
      return methodMap;
   }
   
   

}
