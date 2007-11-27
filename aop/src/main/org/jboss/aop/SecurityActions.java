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
package org.jboss.aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javassist.CtClass;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
class SecurityActions
{
   interface SetAccessibleAction
   {
      void setAccessible(AccessibleObject accessibleObject);
      
      SetAccessibleAction PRIVILEGED = new SetAccessibleAction()
      {
         public void setAccessible(final AccessibleObject accessibleObject)
         {
            try
            {
               AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     accessibleObject.setAccessible(true);
                     return null;
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw new RuntimeException("Error setting " + accessibleObject + " as accessible ", e.getException());
            }
         }
      };

      SetAccessibleAction NON_PRIVILEGED = new SetAccessibleAction()
      {
         public void setAccessible(AccessibleObject accessibleObject)
         {
            accessibleObject.setAccessible(true);
         }
      };
   }

   static void setAccessible(AccessibleObject accessibleObject)
   {
      if (System.getSecurityManager() == null)
      {
         SetAccessibleAction.NON_PRIVILEGED.setAccessible(accessibleObject);
      }
      else
      {
         SetAccessibleAction.PRIVILEGED.setAccessible(accessibleObject);
      }
   }

   interface CtClassDebugWriteFileAction
   {
      void debugWriteFile(CtClass ctClass);
      
      CtClassDebugWriteFileAction PRIVILEGED = new CtClassDebugWriteFileAction()
      {
         public void debugWriteFile(final CtClass ctClass)
         {
            try
            {
               AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     ctClass.debugWriteFile();
                     return null;
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               //Not really a problem if we're not able to write the debug class file
            }
         }
      };

      CtClassDebugWriteFileAction NON_PRIVILEGED = new CtClassDebugWriteFileAction()
      {
         public void debugWriteFile(CtClass ctClass)
         {
            ctClass.debugWriteFile();
         }
      };
   }

   static void debugWriteFile(CtClass ctClass)
   {
      if (System.getSecurityManager() == null)
      {
         CtClassDebugWriteFileAction.NON_PRIVILEGED.debugWriteFile(ctClass);
      }
      else
      {
         CtClassDebugWriteFileAction.PRIVILEGED.debugWriteFile(ctClass);
      }
   }

   public static class GetContextClassLoaderAction implements PrivilegedAction<ClassLoader>
   {
      public static GetContextClassLoaderAction INSTANCE = new GetContextClassLoaderAction();
      
      public ClassLoader run()
      {
         return Thread.currentThread().getContextClassLoader();
      }
   }

   static ClassLoader getContextClassLoader()
   {
      if (System.getSecurityManager() == null)
         return Thread.currentThread().getContextClassLoader();
      else
         return AccessController.doPrivileged(GetContextClassLoaderAction.INSTANCE);
   }
   
   interface GetDeclaredConstructorsAction 
   {
      Constructor[] getDeclaredConstructors(Class clazz);
      
      GetDeclaredConstructorsAction NON_PRIVILEGED = new GetDeclaredConstructorsAction() {

         public Constructor[] getDeclaredConstructors(Class clazz)
         {
            return clazz.getDeclaredConstructors();
         }};

         GetDeclaredConstructorsAction PRIVILEGED = new GetDeclaredConstructorsAction() {

            public Constructor[] getDeclaredConstructors(final Class clazz)
            {
               return AccessController.doPrivileged(new PrivilegedAction<Constructor[]>() {

                  public Constructor[] run()
                  {
                     return clazz.getDeclaredConstructors();
                  }});
            }};
   }
   
   static Constructor[] getDeclaredConstructors(Class clazz)
   {
      if (System.getSecurityManager() == null)
      {
         return GetDeclaredConstructorsAction.NON_PRIVILEGED.getDeclaredConstructors(clazz);
      }
      else
      {
         return GetDeclaredConstructorsAction.PRIVILEGED.getDeclaredConstructors(clazz);
      }
   }

   
   interface GetClassLoaderAction 
   {
      ClassLoader getClassLoader(Class clazz);
      
      GetClassLoaderAction NON_PRIVILEGED = new GetClassLoaderAction() {

         public ClassLoader getClassLoader(Class clazz)
         {
            return clazz.getClassLoader();
         }};

     GetClassLoaderAction PRIVILEGED = new GetClassLoaderAction() {

         public ClassLoader getClassLoader(final Class clazz)
         {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

               public ClassLoader run()
               {
                  return clazz.getClassLoader();
               }});
         }};
   }
   
   static ClassLoader getClassLoader(Class clazz)
   {
      if (System.getSecurityManager() == null)
      {
         return GetClassLoaderAction.NON_PRIVILEGED.getClassLoader(clazz);
      }
      else
      {
         return GetClassLoaderAction.PRIVILEGED.getClassLoader(clazz);
      }
   }
}
