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
package org.jboss.aop.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bridge/portability class for resolving annotations in JDK 1.4 and JDK1.5
 * Should be usable in JDK 1.4 and also should support finding invisible annotations.
 * This would be needed for aspect bindings
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AnnotationElement extends PortableAnnotationElement
{
   /**
    * Get a visible annotation for a particle Method.  If this is JDK 1.5
    * then this is a wrapper for Method.getAnnotation
    *
    * @param method
    * @param annotation
    * @return
    */
   public static Object getVisibleAnnotation(Method method, Class annotation)
   {
      return method.getAnnotation(annotation);
   }

   /**
    * If constructor has visible annotation return it.  If this is JDK 1.5
    * this is a wrapper for Constructor.getAnnotation()
    *
    * @param con
    * @param annotation
    * @return
    */
   public static Object getVisibleAnnotation(Constructor con, Class annotation)
   {
      return con.getAnnotation(annotation);
   }

   /**
    * If field has a visible annotation return it.  If this is JDK 1.5 this is a wrapper
    * for Field.getAnnotation.
    *
    * @param field
    * @param annotation
    * @return
    */
   public static Object getVisibleAnnotation(Field field, Class annotation)
   {
      return field.getAnnotation(annotation);
   }

   /**
    * If class has a visible annotation, return it.  If this is JDK 1.5 this is a wrapper
    * for Class.getAnnotation
    *
    * @param clazz
    * @param annotation
    * @return
    */
   public static Object getVisibleAnnotation(Class clazz, Class annotation)
   {
      return clazz.getAnnotation(annotation);
   }

   public static boolean isVisibleAnnotationPresent(Class clazz, Class annotation)
   {
      return clazz.isAnnotationPresent(annotation);
   }

   public static boolean isVisibleAnnotationPresent(Method m, Class annotation)
   {
      return m.isAnnotationPresent(annotation);
   }

   public static boolean isVisibleAnnotationPresent(Field f, Class annotation)
   {
      return f.isAnnotationPresent(annotation);
   }

   public static boolean isVisibleAnnotationPresent(Constructor con, Class annotation)
   {
      return con.isAnnotationPresent(annotation);
   }

   public static Object[] getVisibleAnnotations(Class clazz) throws Exception
   {
      return clazz.getAnnotations();
   }

   public static Object[] getVisibleAnnotations(Method m) throws Exception
   {
      return m.getAnnotations();
   }
   
   public static Object[] getVisibleAnnotations(Field f) throws Exception
   {
      return f.getAnnotations();
   }
   
   public static Object[] getVisibleAnnotations(Constructor c) throws Exception
   {
      return c.getAnnotations();
   }
}
