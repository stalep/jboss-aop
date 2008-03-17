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
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.annotation.factory.AnnotationCreator;
import org.jboss.aop.util.UnmodifiableEmptyCollections;

import javassist.CtMember;

/**
 * Repository for annotations that is used by the ClassAdvisor to override annotations.
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AnnotationRepository
{
//   private static final String CLASS_ANNOTATION = "CLASS";
   
   /** Read/Write lock to be used when lazy creating the collections */
   protected Object lazyCollectionLock = new Object();

   volatile Map<Object, Map<String, Object>> annotations = UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP;
   volatile Map<String, Object> classAnnotations = UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP; 
   volatile Map<Member, List<String>> disabledAnnotations = UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP;
   volatile List<String> disabledClassAnnotations = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
   
   public Map<Object, Map<String, Object>> getAnnotations()
   {
	   return annotations;
   }
   
   public Map<String, Object> getClassAnnotations()
   {
	   return classAnnotations;
   }

   public void addClassAnnotation(String annotation, String value)
   {
      initClassAnnotationsMap();
      classAnnotations.put(annotation, value);
   }

   public void addClassAnnotation(Class<?> annotation, Object value)
   {
      initClassAnnotationsMap();
      classAnnotations.put(annotation.getName(), value);
   }

   public Object resolveClassAnnotation(Class<? extends Annotation> annotation)
   {
      return resolveTypedClassAnnotation(annotation);
   }
   
   public <T extends Annotation> T resolveTypedClassAnnotation(Class<T> annotation)
   {
      Object value = classAnnotations.get(annotation.getName());
      boolean reinsert = value instanceof String;
      T ann = extractAnnotation(value, annotation);
      if (reinsert)
      {
         classAnnotations.put(annotation.getName(), ann);
      }
      return ann;
   }

   /**
    * Overridden by EJB3
    */
   public Object resolveAnnotation(Member m, Class<? extends Annotation> annotation)
   {
      return resolveTypedAnnotation(m, annotation);
   }
   
   public <T extends Annotation> T resolveTypedAnnotation(Member m, Class<T> annotation)
   {
      Object value = resolveAnnotation(m, annotation.getName());
      boolean reinsert = value instanceof String;
      T ann = extractAnnotation(value, annotation);
      if (reinsert)
      {
         addAnnotation(m, annotation, value);
      }
      return ann;
   }

   protected <T extends Annotation> T  extractAnnotation(Object value, Class<T> annotation)
   {
      if (value == null) return null;
      if (value instanceof String)
      {
         String expr = (String) value;
         try
         {
            return (T)AnnotationCreator.createAnnotation(expr, annotation);
         }
         catch (Exception e)
         {
            throw new RuntimeException("Bad annotation expression " + expr, e);
         }
      }
      return (T)value;
   }

   protected Object resolveAnnotation(Member m, String annotation)
   {
      Map<String, Object> map = annotations.get(m);
      if (map != null)
      {
         return map.get(annotation);
      }
      return null;
   }
   
   public void disableAnnotation(Member m, String annotation)
   {
      List<String> annotationList = disabledAnnotations.get(m);
      if (annotationList == null)
      {
         annotationList = new ArrayList<String>();
         initDisabledAnnotationsMap();
         disabledAnnotations.put(m,annotationList);
      }
      annotationList.add(annotation);
   }
   
   public void disableAnnotation(String annotation)
   {
      initDisabledClassAnnotationsList();
      disabledClassAnnotations.add(annotation);
   }
   
   public void enableAnnotation(String annotation)
   {
      disabledClassAnnotations.remove(annotation);
   }
   
   @SuppressWarnings("unchecked")
   public boolean isDisabled(Member m, Class annotation)
   {
      return isDisabled(m,annotation.getName());
   }
      
   public boolean isTypedDisabled(Member m, Class<? extends Annotation> annotation)
   {
      return isDisabled(m,annotation.getName());
   }
   
   public boolean isDisabled(Member m, String annotation)
   {
      List<String> overrideList = disabledAnnotations.get(m);
      if (overrideList != null && overrideList.size() > 0)
      {
         for (String override : overrideList)
         {
            if (override.equals(annotation))
            {
               return true;
            }
         }
      }
      return false;
   }
   
   /**
    * Required by EJB3
    */
   @SuppressWarnings("unchecked")
   public boolean isDisabled(Class annotation)
   {
      return isTypedDisabled(annotation);
   }
   
   public boolean isTypedDisabled(Class<? extends Annotation> annotation)
   {
      return isDisabled(annotation.getName());
   }
   
   public boolean isDisabled(String annotation)
   {
      if (disabledClassAnnotations.size() > 0)
      {
         for (String ann : disabledClassAnnotations)
         {
            if (ann.equals(annotation))
            {
               return true;
            }
         }
      }
      return false;
   }

   public void addAnnotation(Member m, Class<? extends Annotation> annotation, Object value)
   {
      Map<String, Object> map = annotations.get(m);
      if (map == null)
      {
         map = new HashMap<String, Object>();
         initAnnotationsMap();
         annotations.put(m, map);
      }
      map.put(annotation.getName(), value);
   }

   public void addAnnotation(Member m, String annotation, Object value)
   {
      Map<String, Object> map = annotations.get(m);
      if (map == null)
      {
         map = new HashMap<String, Object>();
         initAnnotationsMap();
         annotations.put(m, map);
      }
      map.put(annotation, value);
   }

   public boolean hasClassAnnotation(String annotation)
   {
      return classAnnotations.containsKey(annotation);
   }

   public boolean hasClassAnnotation(Class<? extends Annotation> annotation)
   {
      return classAnnotations.containsKey(annotation.getName());
   }

   public boolean hasAnnotation(Member m, Class<? extends Annotation> annotation)
   {
      return resolveAnnotation(m, annotation.getName()) != null;
   }

   public boolean hasAnnotation(Member m, String annotation)
   {
      return resolveAnnotation(m, annotation) != null;
   }

   public boolean hasAnnotation(CtMember m, String annotation)
   {
      Map<String, Object> map = annotations.get(m);
      if (map != null) return map.containsKey(annotation);
      return false;
   }

   public void addAnnotation(CtMember m, String annotation)
   {
      Map<String, Object> map = annotations.get(m);
      if (map == null)
      {
         map = new HashMap<String, Object>();
         initAnnotationsMap();
         annotations.put(m, map);
      }
      map.put(annotation, annotation);
   }

   protected void initAnnotationsMap()
   {
      if (annotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
      {
         synchronized(lazyCollectionLock)
         {
            if (annotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
            {
               annotations = new ConcurrentHashMap<Object, Map<String, Object>>();;
            }
         }
      }
   }

   protected void initClassAnnotationsMap()
   {
      if (classAnnotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
      {
         synchronized(lazyCollectionLock)
         {
            if (classAnnotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
            {
               classAnnotations = new ConcurrentHashMap<String, Object>();;
            }
         }
      }
   }

   protected void initDisabledAnnotationsMap()
   {
      if (disabledAnnotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
      {
         synchronized(lazyCollectionLock)
         {
            if (disabledAnnotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
            {
               disabledAnnotations = new ConcurrentHashMap<Member, List<String>>();;
            }
         }
      }
   }

   protected void initDisabledClassAnnotationsList()
   {
      if (disabledClassAnnotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
      {
         synchronized(lazyCollectionLock)
         {
            if (disabledClassAnnotations == UnmodifiableEmptyCollections.EMPTY_CONCURRENT_HASHMAP)
            {
               disabledClassAnnotations = new ArrayList<String>();;
            }
         }
      }
   }
}
