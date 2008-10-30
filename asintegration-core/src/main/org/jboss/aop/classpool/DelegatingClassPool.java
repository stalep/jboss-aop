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
 * Base class for classpools backed by a domain
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DelegatingClassPool extends BaseClassPool
{
   private final static Logger logger = Logger.getLogger(DelegatingClassPool.class);
   private final ClassPoolDomain domain;
   
   private boolean isTemp;
   
   private boolean closed;
   
   public DelegatingClassPool(ClassPoolDomain domain, ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository, boolean isTemp)
   {
      super(cl, parent, repository);
      this.domain = domain;
      domain.addClassPool(this);
   }

   protected DelegatingClassPool(ClassPoolDomain domain, ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository)
   {
      super(cl, parent, repository);
      this.domain = domain;
      domain.addClassPool(this);
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

   
   /**
    * Overrides ClassPool.get0() so that we can look up classes without caching them in the initiating pool.
    * The DelgatingClassPool + DomainClassPool handle the caching in the correct pool + handles the 
    * parentFirst functionality
    */
   @Override
   protected synchronized CtClass get0(String classname, boolean useCache) throws NotFoundException
   {
      CtClass clazz = null;
      if (useCache) 
      {
         clazz = getCached(classname);
         if (clazz != null)
         {
            return clazz;
         }
      }
   
      return createCtClass(classname, useCache);
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
               clazz = domain.getCached(this, classname);
               if (clazz != null)
               {
                  return clazz;
               }
            }
         }
      }
      
      return getCachedFromParent(classname);
   }

   private CtClass getCachedFromParent(String classname)
   {
      if (parent != null)
      {
         if (parent instanceof AOPClassPool)
         {
            return ((AOPClassPool)parent).getCached(classname);
         }
         else
         {
            try
            {
               return parent.get(classname);
            }
            catch (NotFoundException e)
            {
            }
         }
      }
      
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
         clazz = domain.createCtClass(this, classname, useCache);
      }
      
      if (clazz == null)
      {
         clazz = createParentCtClass(classname, useCache);
      }
      return clazz;
   }

   //Lifted from AOPClassPool, also exists in JBossClassPool
   @Override
   protected boolean isLocalResource(String resourceName)
   {
      return super.isLocalResource(resourceName);
   }

   @Override
   public String toString()
   {
      return super.toString() + " domain: " + domain;
   }
}
