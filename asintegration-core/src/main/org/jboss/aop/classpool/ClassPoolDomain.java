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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.jboss.aop.AspectManager;


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
   
   private ClassPoolDomainStrategy classPoolDomainStrategy;
   
   private boolean parentFirst;

   public ClassPoolDomain(String domainName, ClassPoolDomain parent)
   {
      this.parent = parent;
      this.domainName = domainName;

      if (parent == null)
      {
         classPoolDomainStrategy = new RootClassPoolDomainImpl();
      }
      else 
      {
         classPoolDomainStrategy = new ChildClassPoolDomainImpl();
      }
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
   
   synchronized CtClass getCached(DelegatingClassPool initiating, String classname)
   {
      if (parentFirst)
      {
         CtClass clazz = classPoolDomainStrategy.getParentCached(classname);
         if (clazz != null)
         {
            return clazz;
         }
      }
      for(DelegatingClassPool pool : delegatingPools)
      {
         if (pool == initiating)
         {
            continue;
         }
         if (pool.isUnloadedClassLoader())
         {
            AspectManager.instance().unregisterClassLoader(pool.getClassLoader());
            continue;
         }
         CtClass clazz = pool.getCached(false, classname);
         if (clazz != null)
         {
            return clazz;
         }
      }

      if (!parentFirst)
      {
         return classPoolDomainStrategy.getParentCached(classname); 
      }

      return null;
   }
   
   synchronized CtClass createCtClass(DelegatingClassPool initiating, String classname, boolean useCache)
   {
      if (parentFirst)
      {
         CtClass clazz = createParentCtClass(classname, useCache);
         if (clazz != null)
         {
            return clazz;
         }
      }
      for(DelegatingClassPool pool : delegatingPools)
      {
         if (pool == initiating)
         {
            continue;
         }
         if (pool.isUnloadedClassLoader())
         {
            AspectManager.instance().unregisterClassLoader(pool.getClassLoader());
            continue;
         }
         CtClass clazz = pool.createCtClass(false, classname, useCache);
         if (clazz != null)
         {
            return clazz;
         }
      }

      if (!parentFirst)
      {
         return createParentCtClass(classname, useCache); 
      }
      return null;
   }
   
   synchronized CtClass createParentCtClass(String classname, boolean useCache)
   {
      return classPoolDomainStrategy.createParentCtClass(classname, useCache);
   }
   
   synchronized URL findParentResource(String classname)
   {
      return classPoolDomainStrategy.findParentResource(classname);
   }
   
   synchronized URL findResource(String classname)
   {
      if (parentFirst)
      {
         URL url = classPoolDomainStrategy.findParentResource(classname);
         if (url != null)
         {
            return url;
         }
      }
      for (DelegatingClassPool pool : delegatingPools)
      {
         if (pool.isUnloadedClassLoader())
         {
            AspectManager.instance().unregisterClassLoader(pool.getClassLoader());
            continue;
         }
         if (pool.isLocalClassLoaderClass(classname))
         {
            URL url = pool.find(classname);
            if (url != null)
            {
               return url;
            }
         }
      }
      if (!parentFirst)
      {
         return classPoolDomainStrategy.findParentResource(classname);
      }
      return null;
   }
   
   public String toString()
   {
      return super.toString() + "[" + domainName + "]";
   }
   
   private interface ClassPoolDomainStrategy
   {
      URL findParentResource(String classname);
      CtClass createParentCtClass(String classname, boolean useCache);
      CtClass getParentCached(String classname);
   }
   
   private class RootClassPoolDomainImpl implements ClassPoolDomainStrategy
   {
      ClassPool defaultPool = ClassPool.getDefault();
      
      public URL findParentResource(String classname)
      {
         return defaultPool.find(classname);
      }
      
      public CtClass createParentCtClass(String classname, boolean useCache)
      {
         try
         {
            return defaultPool.get(classname);
         }
         catch(NotFoundException ignore)
         {
         }
         return null;
      }
      
      public CtClass getParentCached(String classname)
      {
         try
         {
            return defaultPool.get(classname);
         }
         catch (NotFoundException ignore)
         {
         }
         return null;
      }
   }
   
   private class ChildClassPoolDomainImpl implements ClassPoolDomainStrategy
   {
      public URL findParentResource(String classname)
      {
         return parent.findResource(classname);
      }
      
      public CtClass createParentCtClass(String classname, boolean useCache)
      {
         return parent.createCtClass(null, classname, useCache);
      }
      
      public CtClass getParentCached(String classname)
      {
         return parent.getCached(null, classname);
      }
   }
}
