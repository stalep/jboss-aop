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
package org.jboss.aop.annotation.factory.duplicate.javassist;

import java.lang.reflect.InvocationHandler;
import java.util.Map;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 57050 $
 */
public class AnnotationProxy extends org.jboss.aop.annotation.factory.duplicate.AnnotationProxy implements InvocationHandler
{
   public AnnotationProxy(Class annotationType, Map valueMap)
   {
      super(annotationType, valueMap);
   }

   public static Object createProxy(javassist.bytecode.annotation.Annotation info) throws Exception
   {
      Class annotation = Thread.currentThread().getContextClassLoader().loadClass(info.getTypeName());
      return createProxy(info, annotation);
   }

   public static Object createProxy(javassist.bytecode.annotation.Annotation info, Class annotation) throws Exception
   {
      Map map = ProxyMapCreator.createProxyMap(annotation, info);
      DefaultValueAnnotationValidator reader = new DefaultValueAnnotationValidator();
      reader.validate(map, annotation);
      AnnotationProxy proxyHandler = new AnnotationProxy(annotation, map);
      return java.lang.reflect.Proxy.newProxyInstance(annotation.getClassLoader(), new Class[]{annotation}, proxyHandler);
   }
   
}
