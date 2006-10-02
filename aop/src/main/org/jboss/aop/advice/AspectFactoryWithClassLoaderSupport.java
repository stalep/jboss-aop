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
package org.jboss.aop.advice;

import java.lang.ref.WeakReference;

import org.jboss.aop.AspectManager;

/**
 * Helper class to be able to set the classloader needed for loading up the aspects in the aspectfactories 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public abstract class AspectFactoryWithClassLoaderSupport implements AspectFactory, AspectFactoryWithClassLoader
{
   private WeakReference loader;
   
   /** If a scoped classloader with no parent delegation redefines the class, we need to make sure that that class is pushed on the stack */
   private ThreadLocal scopedClassLoader = new ThreadLocal();
   
   protected AspectFactoryWithClassLoaderSupport()
   {
   }

   public void setClassLoader(ClassLoader cl)
   {
      this.loader = new WeakReference(cl);
   }
   
   protected ClassLoader getLoader()
   {
      ClassLoader scopedClassLoader = peekScopedClassLoader();
      if (scopedClassLoader != null)
      {
         return scopedClassLoader;
      }
      if (loader != null)
      {
         return (ClassLoader)loader.get();
      }
      return null;
   }
   
   public void pushScopedClassLoader(ClassLoader scopedCl)
   {
      scopedClassLoader.set(scopedCl);
   }
   
   public void popScopedClassLoader()
   {
      scopedClassLoader.set(null);
   }
   
   public ClassLoader peekScopedClassLoader()
   {
      return (ClassLoader)scopedClassLoader.get();
   }

   protected Class loadClass(String name) throws ClassNotFoundException
   {
      ClassLoader cl = getLoader();
      if (cl == null)
      {
         ClassLoader tcl = Thread.currentThread().getContextClassLoader(); 
         if (AspectManager.verbose) System.out.println("Using context classloader " + tcl + " to load aspect " + name);
         return tcl.loadClass(name);
      }
      else
      {
         if (AspectManager.verbose) System.out.println("Using scoped classloader " + cl + " to load aspect " + name);
         return cl.loadClass(name);
      }
   }

}
