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

import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

import org.jboss.aop.annotation.PortableAnnotationElement;
import org.jboss.aop.util.ReflectToJavassist;

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
   private final static Object[] EMPTY_OBJECT_ARRAY = new Object[0];
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
      try
      {
         CtMethod ctMethod = ReflectToJavassist.methodToJavassist(method);
         if (ctMethod == null)
         {
            return null;
         }
         MethodInfo mi = ctMethod.getMethodInfo2();

         AnnotationsAttribute visible = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);
         if (visible == null) return null;
         return create(visible, annotation);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
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
      try
      {
         CtConstructor ctMethod = ReflectToJavassist.constructorToJavassist(con);
         if (ctMethod == null)
         {
            return null;
         }
         MethodInfo mi = ctMethod.getMethodInfo2();

         AnnotationsAttribute visible = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);
         if (visible == null) return null;
         return create(visible, annotation);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
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
      try
      {
         CtField ctField = ReflectToJavassist.fieldToJavassist(field);
         FieldInfo mi = ctField.getFieldInfo2();

         AnnotationsAttribute visible = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);
         if (visible == null) return null;
         return create(visible, annotation);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
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
      try
      {
         ClassFile cf = getClassFile(clazz);

         AnnotationsAttribute visible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
         if (visible == null) return null;
         return create(visible, annotation);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
   }

   public static boolean isVisibleAnnotationPresent(Field field, Class annotation) throws Exception
   {
      CtField ctMethod = ReflectToJavassist.fieldToJavassist(field);
      return isVisibleAnnotationPresent(ctMethod, annotation.getName());
   }

   public static boolean isVisibleAnnotationPresent(Class clazz, Class annotation) throws Exception
   {
      ClassFile cf = getClassFile(clazz);

      AnnotationsAttribute visible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
      if (visible == null) return false;
      return visible.getAnnotation(annotation.getName()) != null;
   }

   public static boolean isVisibleAnnotationPresent(Constructor con, Class annotation) throws Exception
   {
      CtConstructor ctMethod = ReflectToJavassist.constructorToJavassist(con);
      return isVisibleAnnotationPresent(ctMethod, annotation.getName());


   }

   public static boolean isVisibleAnnotationPresent(Method method, Class annotation) throws Exception
   {
      CtMethod ctMethod = ReflectToJavassist.methodToJavassist(method);
      if (ctMethod == null) return false;
      MethodInfo mi = ctMethod.getMethodInfo2();


      AnnotationsAttribute visible = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);
      if (visible == null) return false;
      return visible.getAnnotation(annotation.getName()) != null;
   }

   public static Object[] getVisibleAnnotations(Class clazz) throws Exception
   {
      try
      {
         ClassFile cf = getClassFile(clazz);

         AnnotationsAttribute visible = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
         if (visible == null) return EMPTY_OBJECT_ARRAY;
         return getVisibleAnnotations(visible);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
   }

   public static Object[] getVisibleAnnotations(Method m) throws Exception
   {
      CtMethod ctMethod = ReflectToJavassist.methodToJavassist(m);
      if (ctMethod == null) return EMPTY_OBJECT_ARRAY;
      MethodInfo mi = ctMethod.getMethodInfo2();

      AnnotationsAttribute visible = (AnnotationsAttribute) mi.getAttribute(AnnotationsAttribute.visibleTag);
      if (visible == null) return EMPTY_OBJECT_ARRAY;
      return getVisibleAnnotations(visible);
   }
   
   public static Object[] getVisibleAnnotations(Field f) throws Exception
   {
      CtField ctField = ReflectToJavassist.fieldToJavassist(f);
      if (ctField == null) return EMPTY_OBJECT_ARRAY;
      FieldInfo fi = ctField.getFieldInfo2();

      AnnotationsAttribute visible = (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.visibleTag);
      if (visible == null) return EMPTY_OBJECT_ARRAY;
      return getVisibleAnnotations(visible);
   }
   
   public static Object[] getVisibleAnnotations(Constructor c) throws Exception
   {
      CtConstructor ctConstructor = ReflectToJavassist.constructorToJavassist(c);
      if (ctConstructor == null) return EMPTY_OBJECT_ARRAY;
      MethodInfo ci = ctConstructor.getMethodInfo2();

      AnnotationsAttribute visible = (AnnotationsAttribute) ci.getAttribute(AnnotationsAttribute.visibleTag);
      if (visible == null) return EMPTY_OBJECT_ARRAY;
      return getVisibleAnnotations(visible);
   }
   
   private static Object[] getVisibleAnnotations(AnnotationsAttribute visible) throws Exception
   {
      Annotation[] annotations = visible.getAnnotations();
      Object[] returnedAnnotations = new Object[annotations.length];
      for (int i = 0 ; i < annotations.length ; i++)
      {
         String name = annotations[i].getTypeName();
         Class annotation = Thread.currentThread().getContextClassLoader().loadClass(name);
         returnedAnnotations[i] = create(visible, annotation);
      }
      return returnedAnnotations;
   }
}
