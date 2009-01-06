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

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;

import org.jboss.aop.util.ClassLoaderUtils;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class BasicClassPoolDomain extends AbstractClassPoolDomain implements ClassPoolDomainInternal
{
   private String domainName;
   
   private ClassPoolDomainInternal parent;
   
   private List<DelegatingClassPool> delegatingPools = new ArrayList<DelegatingClassPool>();
   
   private boolean parentFirst;

   public BasicClassPoolDomain(String domainName, ClassPoolDomain parent)
   {
      if (parent != null && parent instanceof ClassPoolDomainInternal == false)
      {
         throw new IllegalArgumentException("Parent must implement ClassPoolDomainInternal");
      }
      this.parent = (ClassPoolDomainInternal)parent;
      this.domainName = domainName;

      if (parent == null)
      {
         this.parent = createParentClassPoolToClassPoolDomainAdaptor();
         if (this.parent == null)
         {
            throw new IllegalStateException("No ClassPoolToClassPool");
         }
      }
   }

   protected ClassPoolToClassPoolDomainAdapter createParentClassPoolToClassPoolDomainAdaptor()
   {
      return new ClassPoolToClassPoolDomainAdapter();
   }
   
   public String getDomainName()
   {
      return domainName;
   }
 
   public boolean isParentFirst()
   {
      return parentFirst;
   }

   public void setParentFirst(boolean parentFirst)
   {
      this.parentFirst = parentFirst;
   }

   public synchronized void addClassPool(DelegatingClassPool pool)
   {
      if (!delegatingPools.contains(pool))
      {
         delegatingPools.add(pool);
      }
   }
   
   public synchronized void removeClassPool(DelegatingClassPool pool)
   {
      delegatingPools.remove(pool);
   }
   
   public synchronized CtClass getCachedOrCreate(DelegatingClassPool initiatingPool, String classname, boolean create)
   {
      String resourceName = ClassLoaderUtils.getResourceName(classname);
      
      CtClass clazz = getCachedOrCreateInternal(initiatingPool, classname, resourceName, create);
      
      if (clazz == null)
      {
         clazz = getCachedOrCreateFromPoolParent(initiatingPool, classname, create);
      }
      return clazz;
   }
   
   public CtClass getCachedOrCreateInternal(DelegatingClassPool initiatingPool, String classname, String resourceName, boolean create)
   {
      CtClass clazz = null;
      if (isParentBefore())
      {
         clazz = getCachedOrCreateInternalFromParent(initiatingPool, classname, resourceName, create);
      }
      if (clazz == null)
      {
         String packageName = ClassLoaderUtils.getPackageName(classname);
         for (DelegatingClassPool pool : getPoolsForPackage(packageName))
         {
            clazz = pool.loadLocally(classname, resourceName, create);
            if (clazz != null)
            {
               break;
            }
         }
      }
      if (clazz == null && isParentAfter())
      {
         clazz = getCachedOrCreateInternalFromParent(initiatingPool, classname, resourceName, create);
      }
      return clazz;
   }

   public CtClass getCachedOrCreateInternalFromParent(DelegatingClassPool initiatingPool, String classname, String resourceName, boolean create)
   {
      return parent.getCachedOrCreateInternal(initiatingPool, classname, resourceName, create);
   }
   
   public String toString()
   {
      return super.toString() + "[" + domainName + "]";
   }

   public List<DelegatingClassPool> getClassPools()
   {
      return delegatingPools; 
   }
   
   protected boolean isParentBefore()
   {
      return parentFirst && parent!= null;
   }
   
   protected boolean isParentAfter()
   {
      return parent != null && !parentFirst;
   }
   
   protected ClassPoolDomainInternal getParentDomain()
   {
      return parent;
   }
   
   protected List<DelegatingClassPool> getPoolsForPackage(String packageName)
   {
      return delegatingPools;
   }
}
