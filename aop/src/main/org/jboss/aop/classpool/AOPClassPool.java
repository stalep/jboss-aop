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
package org.jboss.aop.classpool;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.aop.AspectManager;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AOPClassPool extends ScopedClassPool
{
   /** Classnames of classes that will be created - we do not want to look for these in other pools */
   protected ConcurrentHashMap<String, String> generatedClasses = new ConcurrentHashMap<String, String>();
   
   protected ConcurrentHashMap<String, Boolean> localResources = new ConcurrentHashMap<String, Boolean>();
   
   /** Classnames of classes that have been loaded, but were not woven */
   protected ConcurrentHashMap<String, Boolean> loadedButNotWovenClasses = new ConcurrentHashMap<String, Boolean>();

   static 
   {
      ClassPool.doPruning = false;
      ClassPool.releaseUnmodifiedClassFile = false;
   }   
   
   public AOPClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      this(cl, src, repository, false);
   }

   protected AOPClassPool(ClassPool src, ScopedClassPoolRepository repository)
   {
      this(null, src, repository, true);
   }

   private AOPClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, boolean isTemp)
   {
      super(cl, src, repository, isTemp);
   }

   public void setClassLoader(ClassLoader cl)
   {
      classLoader = new WeakReference<ClassLoader>(cl);
   }
   
   public void registerGeneratedClass(String className)
   {
      generatedClasses.put(className, className);
   }
      
   public void close()
   {
      super.close();
      AOPClassPoolRepository.getInstance().perfomUnregisterClassLoader(getClassLoader());
   }

   public CtClass getCached(String classname)
   {
      CtClass clazz = getCachedLocally(classname);
      if (clazz == null)
      {
         boolean isLocal = false; 

         ClassLoader cl = getClassLoader0();

         if (cl != null)
         {
            isLocal = isLocalResource(classname); 
         }
         
         if (!isLocal)
         {
            Object o = generatedClasses.get(classname);
            if (o == null)
            {
               Map registeredCLs = AspectManager.getRegisteredCLs();
               synchronized (registeredCLs)
               {
                  Iterator it = registeredCLs.values().iterator();
                  while (it.hasNext())
                  {
                     AOPClassPool pool = (AOPClassPool) it.next();
                     if (pool.isUnloadedClassLoader())
                     {
                        AspectManager.instance().unregisterClassLoader(pool.getClassLoader());
                        continue;
                     }
                     
                     //Do not check classpools for scoped classloaders
                     if (!pool.includeInGlobalSearch())
                     {
                        continue;
                     }
      
                     clazz = pool.getCachedLocally(classname);
                     if (clazz != null)
                     {
                        return clazz;
                     }
                  }
               }
            }
         }
      }
      // *NOTE* NEED TO TEST WHEN SUPERCLASS IS IN ANOTHER UCL!!!!!!
      return clazz;
   }
   
   protected boolean includeInGlobalSearch()
   {
      return true;
   }
   
   protected String getResourceName(String classname)
   {
      final int lastIndex = classname.lastIndexOf('$');
      if (lastIndex < 0)
      {
         return classname.replaceAll("[\\.]", "/") + ".class";
      }
      else
      {
         return classname.substring(0, lastIndex).replaceAll("[\\.]", "/") + classname.substring(lastIndex) + ".class";
      }
   }
   
   protected boolean isLocalResource(String resourceName)
   {
      String classResourceName = getResourceName(resourceName);
      Boolean isLocal = (Boolean)localResources.get(classResourceName);
      if (isLocal != null)
      {
         return isLocal.booleanValue();
      }
      boolean localResource = getClassLoader().getResource(classResourceName) != null;
      localResources.put(classResourceName, localResource ? Boolean.TRUE : Boolean.FALSE);
      return localResource;
   }
   
   public synchronized CtClass getLocally(String classname)
           throws NotFoundException
   {
      softcache.remove(classname);
      CtClass clazz = (CtClass) classes.get(classname);
      if (clazz == null)
      {
         clazz = createCtClass(classname, true);
         if (clazz == null) throw new NotFoundException(classname);
         lockInCache(clazz);//Avoid use of the softclasscache
      }

      return clazz;
   }

   public void setClassLoadedButNotWoven(String classname)
   {
      loadedButNotWovenClasses.put(classname, Boolean.TRUE);
   }
   
   public boolean isClassLoadedButNotWoven(String classname)
   {
      return loadedButNotWovenClasses.get(classname) == Boolean.TRUE;
   }

   public static AOPClassPool createAOPClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      return (AOPClassPool)AspectManager.getClassPoolFactory().create(cl, src, repository);
   }

   public static AOPClassPool createAOPClassPool(ClassPool src, ScopedClassPoolRepository repository)
   {
      return (AOPClassPool)AspectManager.getClassPoolFactory().create(src, repository);
   }
   
   public String toString()
   {
      ClassLoader cl = null;
      try
      {
         cl = getClassLoader();
      }
      catch(IllegalStateException ignore)
      {
      }
      return super.toString() + " - dcl " + cl;
   }
}
