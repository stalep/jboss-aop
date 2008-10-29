/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.scopedpool.ScopedClassPoolRepository;

import org.jboss.logging.Logger;

/**
 * ClassPool for classloaders backed by a repository/classloading domain
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DelegatingClassPool extends BaseClassPool 
{
   private final static Logger logger = Logger.getLogger(DelegatingClassPool.class);
   private final ClassPoolDomain domain;
   
   private boolean closed;
   
   private IsLocalResourcePlugin isLocalResourcePlugin;
   
   public DelegatingClassPool(ClassPoolDomain domain, ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository)
   {
      super(cl, parent, repository);
      this.domain = domain;
      domain.addClassPool(this);
      isLocalResourcePlugin = IsLocalResourcePluginFactoryRegistry.getPluginFactory(cl).create(this);
   }

   public CtClass get(String classname) throws NotFoundException 
   {
      System.out.println("==> Initiating lookup of " + classname + " in " + this);
      if (logger.isTraceEnabled())
      {
         logger.trace("Initiating lookup of " + classname + " in " + this);
      }
      return super.get(classname);
   }

   

   
   @Override
   public boolean isUnloadedClassLoader()
   {
      return closed;
   }

   @Override
   public void close()
   {
      closed = true;
      super.close();
      domain.removeClassPool(this);
   }

   @Override
   public CtClass getCached(String classname)
   {
      return getCached(true, classname);
   }
   
   
   CtClass getCached(boolean isInitiatingPool, String classname)
   {
      if (isInitiatingPool && domain.isParentFirst())
      {
         CtClass clazz = domain.getCached(this, classname);
         if (clazz != null)
         {
            return clazz;
         }
      }
      CtClass clazz = getCachedLocally(classname);
      if (clazz != null)
      {
         System.out.println("==> Found cached class " + classname + " in " + this);
         if (logger.isTraceEnabled())
         {
            logger.trace("Found cached class " + classname + " in " + this);
         }
         return clazz;
      }
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
            if (o == null && isInitiatingPool)
            {
               return domain.getCached(this, classname);
            }
         }
      }
      // *NOTE* NEED TO TEST WHEN SUPERCLASS IS IN ANOTHER UCL!!!!!!
      return null;
   }
   

   @Override
   public CtClass createCtClass(String classname, boolean useCache)
   {
      return createCtClass(true, classname, useCache);
   }
   
   CtClass createCtClass(boolean isInitiatingPool, String classname, boolean useCache)
   {
      CtClass clazz = null;
      if (isLocalResource(classname))
      {
         
         boolean create = true;
         if (domain.isParentFirst())
         {
            if (domain.findParentResource(classname) != null)
            {
               create = false;
            }
         }
         
         if (create)
         {
            clazz = super.createCtClass(classname, useCache);
            if (clazz != null && useCache)
            {
               if (useCache)
               {
                  System.out.println("==> Caching class " + classname + " in " + this);
                  if (logger.isTraceEnabled())
                  {
                     logger.trace("Caching class " + classname + " in " + this);
                  }
                  cacheCtClass(clazz.getName(), clazz, false);
               }
            }
         }
      }
      if (clazz == null && isInitiatingPool)
      {
         return domain.createCtClass(this, classname, useCache);
      }
      return clazz;
   }

   @Override
   public boolean isLocalClassLoaderResource(String classResourceName)
   {
      return isLocalResourcePlugin.isMyResource(classResourceName);
   }

   public boolean isLocalClassLoaderClass(String classname)
   {
      return isLocalResourcePlugin.isMyResource(getResourceName(classname));
   }

   @Override
   public String toString()
   {
      return super.toString() + " domain: " + domain;
   }
}
