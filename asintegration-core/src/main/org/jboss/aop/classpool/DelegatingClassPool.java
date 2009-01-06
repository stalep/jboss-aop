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
   private final ClassPoolDomainInternal domain;
   
   private boolean closed;
   
   public DelegatingClassPool(ClassPoolDomain domain, ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository, boolean isTemp)
   {
      this(domain, cl, parent, repository);
   }

   protected DelegatingClassPool(ClassPoolDomain domain, ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository)
   {
      super(cl, parent, repository);
      if (domain == null)
      {
         throw new IllegalArgumentException("Domain was null");
      }
      if (domain instanceof ClassPoolDomainInternal == false)
      {
         throw new IllegalArgumentException("Domain must implement ClassPoolDomainInternal");
      }
      this.domain = (ClassPoolDomainInternal)domain;
      domain.addClassPool(this);
   }

   ClassPoolDomain getDomain()
   {
      return domain;
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

   public CtClass loadLocally(String classname, String resourceName, boolean create)
   {
      CtClass clazz = null;
      if (isLocalResource(resourceName))
      {
         clazz = getCachedLocally(classname);
         if (clazz == null && create)
         {
            return createCtClass(classname, true);
         }
      }
      return clazz;
   }
   
   /**
    * Overrides ClassPool.get0() so that we can look up classes without caching them in the initiating pool.
    * The DelgatingClassPool + DomainClassPool handle the caching in the correct pool + handles the 
    * parentFirst functionality
    */
   @Override
   protected synchronized CtClass get0(String classname, boolean useCache) throws NotFoundException
   {
      return domain.getCachedOrCreate(this, classname, true);
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
      return domain.getCachedOrCreate(this, classname, false);
      //return getCached(true, classname);
   }
   
   @Override
   public CtClass getCachedLocally(String classname)
   {
      return super.getCachedLocally(classname);
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
