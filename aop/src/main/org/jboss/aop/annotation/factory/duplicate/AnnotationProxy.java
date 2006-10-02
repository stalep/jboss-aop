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
package org.jboss.aop.annotation.factory.duplicate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 46363 $
 */
public class AnnotationProxy implements InvocationHandler
{
   Map map;
   Class annotationType;

   public AnnotationProxy(Class annotationType, Map valueMap)
   {
      this.annotationType = annotationType;
      map = valueMap;
   }

   public Object invoke(Object proxy, Method method, Object[] args)
           throws Throwable
   {
      if (method.getName().equals("equals"))
      {
         return doEquals(proxy, args[0]);
      }
      else if (method.getName().equals("hashCode"))
      {
         return doHashCode();
      }
      else if (method.getName().equals("toString"))
      {
         return map.toString();
      }
      else if (method.getName().equals("annotationType"))
      {
         return annotationType;
      }
      
      /*
      Object obj = map.get(method.getName());
      if (!method.getReturnType().equals(obj.getClass()))
      {
         System.err.println("***** " + method.toString() + " has bad return type: " + obj.getClass().getName());
      }
      return obj;
      */
      return map.get(method.getName());
   }

   public Object getValue(String name)
   {
      return map.get(name);
   }
   
   private Object doEquals(Object proxy, Object obj)
   {
      if (obj == proxy) return Boolean.TRUE;
      if (obj == null) return Boolean.FALSE;

      Class[] intfs = proxy.getClass().getInterfaces();
      if (!intfs[0].isAssignableFrom(obj.getClass()))
      {
         return Boolean.FALSE;
      }
      try
      {
         Proxy.getInvocationHandler(obj);
      }
      catch (Exception ex)
      {
         return Boolean.FALSE;
      }
      return Boolean.TRUE;
   }

   private Object doHashCode()
   {
      return new Integer(map.hashCode());
   }

   public static Object createProxy(Map map, Class annotation) throws Exception
   {
      AnnotationProxy proxyHandler = new AnnotationProxy(annotation, map);
      return java.lang.reflect.Proxy.newProxyInstance(annotation.getClassLoader(), new Class[]{annotation}, proxyHandler);
   }
}
