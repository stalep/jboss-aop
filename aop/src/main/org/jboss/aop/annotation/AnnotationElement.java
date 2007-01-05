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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

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
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotation(method, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotation(method, annotation);
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
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotation(con, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotation(con, annotation);
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
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotation(field, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotation(field, annotation);
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
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotation(clazz, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotation(clazz, annotation);
      }
   }

   public static boolean isVisibleAnnotationPresent(Class clazz, Class annotation)
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.isVisibleAnnotationPresent(clazz, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.isVisibleAnnotationPresent(clazz, annotation);
      }
   }

   public static boolean isVisibleAnnotationPresent(Method m, Class annotation)
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.isVisibleAnnotationPresent(m, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.isVisibleAnnotationPresent(m, annotation);
      }
   }

   public static boolean isVisibleAnnotationPresent(Field f, Class annotation)
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.isVisibleAnnotationPresent(f, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.isVisibleAnnotationPresent(f, annotation);
      }
   }

   public static boolean isVisibleAnnotationPresent(Constructor con, Class annotation)
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.isVisibleAnnotationPresent(con, annotation);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.isVisibleAnnotationPresent(con, annotation);
      }
   }

   public static Object[] getVisibleAnnotations(Class clazz) throws Exception
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotations(clazz);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotations(clazz);
      }
   }

   public static Object[] getVisibleAnnotations(Method m) throws Exception
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotations(m);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotations(m);
      }
   }
   
   public static Object[] getVisibleAnnotations(Field f) throws Exception
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotations(f);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotations(f);
      }
   }
   
   public static Object[] getVisibleAnnotations(Constructor c) throws Exception
   {
      if (System.getSecurityManager() == null)
      {
         return AnnotationElementAction.NON_PRIVILEGED.getVisibleAnnotations(c);
      }
      else
      {
         return AnnotationElementAction.PRIVILEGED.getVisibleAnnotations(c);
      }
   }
   
   
   private interface AnnotationElementAction
   {
      Object getVisibleAnnotation(Method method, Class annotation);

      Object getVisibleAnnotation(Constructor con, Class annotation);

      Object getVisibleAnnotation(Field field, Class annotation);

      Object getVisibleAnnotation(Class clazz, Class annotation);

      boolean isVisibleAnnotationPresent(Class clazz, Class annotation);

      boolean isVisibleAnnotationPresent(Method m, Class annotation);

      boolean isVisibleAnnotationPresent(Field f, Class annotation);

      boolean isVisibleAnnotationPresent(Constructor con, Class annotation);

      Object[] getVisibleAnnotations(Class clazz) throws Exception;

      Object[] getVisibleAnnotations(Method m) throws Exception;
      
      Object[] getVisibleAnnotations(Field f) throws Exception;
      
      Object[] getVisibleAnnotations(Constructor c) throws Exception;
      
      AnnotationElementAction NON_PRIVILEGED = new AnnotationElementAction()
      {
         public Object getVisibleAnnotation(Method method, Class annotation)
         {
            return method.getAnnotation(annotation);
         }

         public Object getVisibleAnnotation(Constructor con, Class annotation)
         {
            return con.getAnnotation(annotation);
         }

         public Object getVisibleAnnotation(Field field, Class annotation)
         {
            return field.getAnnotation(annotation);
         }

         public Object getVisibleAnnotation(Class clazz, Class annotation)
         {
            return clazz.getAnnotation(annotation);
         }

         public boolean isVisibleAnnotationPresent(Class clazz, Class annotation)
         {
            return clazz.isAnnotationPresent(annotation);
         }

         public boolean isVisibleAnnotationPresent(Method m, Class annotation)
         {
            return m.isAnnotationPresent(annotation);
         }

         public boolean isVisibleAnnotationPresent(Field f, Class annotation)
         {
            return f.isAnnotationPresent(annotation);
         }

         public boolean isVisibleAnnotationPresent(Constructor con, Class annotation)
         {
            return con.isAnnotationPresent(annotation);
         }

         public Object[] getVisibleAnnotations(Class clazz) throws Exception
         {
            return clazz.getAnnotations();
         }

         public Object[] getVisibleAnnotations(Method m) throws Exception
         {
            return m.getAnnotations();
         }
         
         public Object[] getVisibleAnnotations(Field f) throws Exception
         {
            return f.getAnnotations();
         }
         
         public Object[] getVisibleAnnotations(Constructor c) throws Exception
         {
            return c.getAnnotations();
         }
      };
      

      AnnotationElementAction PRIVILEGED = new AnnotationElementAction()
      {
         public Object getVisibleAnnotation(final Method method, final Class annotation)
         {
            return AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return method.getAnnotation(annotation);
               }
            });
         }

         public Object getVisibleAnnotation(final Constructor con, final Class annotation)
         {
            return AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return con.getAnnotation(annotation);
               }
            });
         }

         public Object getVisibleAnnotation(final Field field, final Class annotation)
         {
            return AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return field.getAnnotation(annotation);
               }
            });
         }

         public Object getVisibleAnnotation(final Class clazz, final Class annotation)
         {
            return AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return clazz.getAnnotation(annotation);
               }
            });
         }

         public boolean isVisibleAnnotationPresent(final Class clazz, final Class annotation)
         {
            Boolean present = (Boolean)AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return clazz.isAnnotationPresent(annotation) ? Boolean.TRUE : Boolean.FALSE;
               }
            });
            
            return present.booleanValue();
         }

         public boolean isVisibleAnnotationPresent(final Method m, final Class annotation)
         {
            Boolean present = (Boolean)AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return m.isAnnotationPresent(annotation) ? Boolean.TRUE : Boolean.FALSE;
               }
            });
            
            return present.booleanValue();
         }

         public boolean isVisibleAnnotationPresent(final Field f, final Class annotation)
         {
            Boolean present = (Boolean)AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return f.isAnnotationPresent(annotation) ? Boolean.TRUE : Boolean.FALSE;
               }
            });
            return present;
         }

         public boolean isVisibleAnnotationPresent(final Constructor con, final Class annotation)
         {
            Boolean present = (Boolean)AccessController.doPrivileged(new PrivilegedAction(){
               public Object run()
               {
                  return con.isAnnotationPresent(annotation) ? Boolean.TRUE : Boolean.FALSE;
               }
            });
            return present;
         }

         public Object[] getVisibleAnnotations(final Class clazz) throws Exception 
         {
            try
            {
               return (Object[])AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     return clazz.getAnnotations();
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw e.getException();
            }
         }

         public Object[] getVisibleAnnotations(final Method m) throws Exception 
         {
            try
            {
               return (Object[])AccessController.doPrivileged(new PrivilegedExceptionAction(){
                  public Object run() throws Exception
                  {
                     return m.getAnnotations();
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw e.getException();
            }
         }
         
         public Object[] getVisibleAnnotations(final Field f) throws Exception
         {
            try
            {
               return (Object[])AccessController.doPrivileged(new PrivilegedExceptionAction(){
                  public Object run() throws Exception
                  {
                     return f.getAnnotations();
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw e.getException();
            }
         }
         
         public Object[] getVisibleAnnotations(final Constructor c) throws Exception
         {
            try
            {
               return (Object[])AccessController.doPrivileged(new PrivilegedExceptionAction(){
                  public Object run() throws Exception
                  {
                     return c.getAnnotations();
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw e.getException();
            }
         }
      };
   }
}
