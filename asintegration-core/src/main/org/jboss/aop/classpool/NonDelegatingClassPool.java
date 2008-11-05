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
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * ClassPool for class loaders not backed by a repository/classloading domain
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class NonDelegatingClassPool extends BaseClassPool
{
   public NonDelegatingClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, boolean parentFirst)
   {
      super(cl, src, repository, AOPClassPool.SEARCH_LOCAL_ONLY_STRATEGY);
      super.childFirstLookup = !parentFirst;
   }

//   /**
//    * Overrides ClassPool.get0() so that we can look up classes without caching them in the initiating pool.
//    * The DelgatingClassPool + ClassPoolDomain handle the caching in the correct pool + handles the 
//    * parentFirst functionality
//    */
//   @Override
//   protected synchronized CtClass get0(String classname, boolean useCache) throws NotFoundException
//   {
//      CtClass clazz = null;
//      if (useCache) 
//      {
//         clazz = getCached(classname);
//         if (!childFirstLookup && clazz != null)
//         {
//            return clazz;
//         }
//      }
//   
//      boolean attemptCreate = false;
//      if (clazz == null)
//      {
//         attemptCreate = true;
//      }
//      else if (clazz != null && clazz.getClassPool() != this && childFirstLookup && isLocalResource(classname))
//      {
//         attemptCreate = true;
//      }
//      
//      if (attemptCreate)
//      {
//         CtClass clazz2 = createCtClass(classname, useCache);
//         if (clazz2 != null)
//         {
//            clazz = clazz2;
//         }
//      }
//      return clazz;
//   }

   @Override
   public CtClass createCtClass(String classname, boolean useCache)
   {
      CtClass clazz = null;
      if (!childFirstLookup)
      {
         clazz = createParentCtClass(classname, useCache);
      }
      if (clazz == null)
      {
//         String resName = getResourceName(classname);
         if (isLocalResource(classname))
         {
            clazz = super.createCtClass(classname, useCache);
         }
      }
      if (childFirstLookup && clazz == null)
      {
         clazz = createParentCtClass(classname, useCache);
      }
      
      return clazz;
   }
}
