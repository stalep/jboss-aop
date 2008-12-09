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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.jboss.aop.util.ClassLoaderUtils;


/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassPoolDomain
{
   private String domainName;
   
   private ClassPoolDomain parent;
   
   private List<DelegatingClassPool> delegatingPools = new ArrayList<DelegatingClassPool>();
   
   private boolean parentFirst;

   public ClassPoolDomain(String domainName, ClassPoolDomain parent)
   {
      this.parent = parent;
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
   
   protected ClassPoolDomain()
   {
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

   synchronized void addClassPool(DelegatingClassPool pool)
   {
      if (!delegatingPools.contains(pool))
      {
         delegatingPools.add(pool);
      }
   }
   
   synchronized void removeClassPool(DelegatingClassPool pool)
   {
      delegatingPools.remove(pool);
   }
   
   synchronized CtClass getCachedOrCreate(DelegatingClassPool initiatingPool, String classname, boolean create)
   {
      CtClass clazz = getCachedOrCreateInternal(classname, create);
      
      if (clazz == null)
      {
         clazz = getCachedOrCreateFromPoolParent(initiatingPool, classname, create);
      }
      return clazz;
   }
   
   
   private CtClass getCachedOrCreateFromPoolParent(BaseClassPool initiatingPool, String classname, boolean create)
   {
      if (initiatingPool == null)
      {
         return null;
      }
      ClassPool parentPool = initiatingPool.getParent();
      if (parentPool == null)
      {
         return null;
      }
       
      if (parentPool instanceof BaseClassPool)
      {
         return getCachedOrCreate((BaseClassPool)parentPool, classname, create);
      }
      else
      {
         return getCachedOrCreate(parentPool, classname, create);
      }
   }
   
   
   protected CtClass getCachedOrCreateInternal(String classname, boolean create)
   {
      CtClass clazz = null;
      if (parentFirst && parent!= null)
      {
         clazz = parent.getCachedOrCreateInternal(classname, create);
      }
      if (clazz == null)
      {
         String resourceName = delegatingPools.size() > 0 ? ClassLoaderUtils.getResourceName(classname) : null;
         for (DelegatingClassPool pool : delegatingPools)
         {
            if (pool.isLocalResource(resourceName))
            {
               clazz = pool.getCachedLocally(classname);
               if (clazz == null && create)
               {
                  clazz = pool.createCtClass(classname, true);
               }
            }
         }
      }
      if (clazz == null && parent != null && !parentFirst)
      {
         clazz = parent.getCachedOrCreateInternal(classname, create);
      }
      return clazz;
   }

   protected CtClass getCachedOrCreate(BaseClassPool parentPool, String classname, boolean create)
   {
      if (parentPool == null)
      {
         return null;
      }
      
      CtClass clazz = null;
      if (!parentPool.childFirstLookup)
      {
         clazz = getCachedOrCreateFromPoolParent(parentPool, classname, create); 
      }
      
      //We can use the exposed methods directly to avoid the overhead of NotFoundException
      clazz = parentPool.getCached(classname);
      if (clazz == null && create)
      {
         clazz = parentPool.createCtClass(classname, true);
      }

      if (clazz == null && !parentPool.childFirstLookup)
      {
         clazz = getCachedOrCreateFromPoolParent(parentPool, classname, create); 
      }
      return clazz;
   }
   
   protected CtClass getCachedOrCreate(ClassPool parentPool, String classname, boolean create)
   {
      try
      {
         //This will check the parents
         return parentPool.get(classname);
      }
      catch(NotFoundException e)
      {
         return null;
      }
   }
      
   public String toString()
   {
      return super.toString() + "[" + domainName + "]";
   }
}
