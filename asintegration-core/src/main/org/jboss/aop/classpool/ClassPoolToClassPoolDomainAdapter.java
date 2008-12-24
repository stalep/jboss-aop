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

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassPoolToClassPoolDomainAdapter extends AbstractClassPoolDomain implements ClassPoolDomainInternal
{
   protected ClassPool pool;
   
   public ClassPoolToClassPoolDomainAdapter()
   {
      this.pool = initialiseParentClassLoader();
      if (pool == null)
      {
         throw new IllegalStateException("Null parent classpool");
      }
   }
   
   public ClassPool initialiseParentClassLoader()
   {
      return ClassPool.getDefault();
   }
   
   public void addClassPool(DelegatingClassPool pool)
   {
      throw new IllegalStateException("Cannot add pools to the domain adaptor");
   }

   
   public synchronized CtClass getCachedOrCreateInternal(DelegatingClassPool initiatingPool, String classname, String resourceName, boolean create)
   {
      if (pool instanceof BaseClassPool)
      {
         return getCachedOrCreateFromPool((BaseClassPool)pool, classname, create);
      }
      else
      {
         return getCachedOrCreateFromPool(pool, classname, create);
      }
   }

   public CtClass getCachedOrCreateInternalFromParent(DelegatingClassPool initiatingPool, String classname, String resourceName, boolean create)
   {
      return null;
   }

   public String getDomainName()
   {
      return null;
   }

   public boolean isParentFirst()
   {
      return !pool.childFirstLookup;
   }

   public void removeClassPool(DelegatingClassPool pool)
   {
      throw new IllegalStateException("Cannot remove pools from the domain adaptor");
   }

   public void setParentFirst(boolean parentFirst)
   {
      throw new IllegalStateException("Cannot change the parent first setting in the domain adaptor");
   }

   public String toString()
   {
      return "ClassPoolToDomainAdapter[" + System.identityHashCode(this) + " " + pool.toString() + "]";
   }

   public CtClass getCachedOrCreate(DelegatingClassPool initiatingPool, String classname, boolean create)
   {
      throw new IllegalStateException("Should never be called");
   }

}
