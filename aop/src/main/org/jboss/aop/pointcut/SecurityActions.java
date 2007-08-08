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
package org.jboss.aop.pointcut;

import java.lang.reflect.AccessibleObject;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
class SecurityActions
{
   static Class loadClass(String name)
   {
      if (System.getSecurityManager() == null)
      {
         return LoadClassAction.NON_PRIVILEGED.loadClass(name);
      }
      else
      {
         return LoadClassAction.PRIVILEGED.loadClass(name);
      }
   }

   interface LoadClassAction
   {
      Class loadClass(String name);
      
      LoadClassAction PRIVILEGED = new LoadClassAction()
      {
         public Class loadClass(final String name)
         {
            try
            {
               return AccessController.doPrivileged(new PrivilegedExceptionAction<Class>()
               {
                  public Class run() throws Exception
                  {
                     return Thread.currentThread().getContextClassLoader().loadClass(name);
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw new RuntimeException("Unable to load class " + name, e.getException());
            }
         }
      };

      LoadClassAction NON_PRIVILEGED = new LoadClassAction()
      {
         public Class loadClass(String name)
         {
            try
            {
               return Thread.currentThread().getContextClassLoader().loadClass(name);
            }
            catch (ClassNotFoundException e)
            {
               throw new RuntimeException("Unable to load class " + name, e);
            }
         }
      };
   }
   static void setAccessible(AccessibleObject accessibleObject)
   {
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
}
