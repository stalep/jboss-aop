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
package org.jboss.aop.classpool.jbosscl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import org.jboss.aop.AspectManager;
import org.jboss.aop.classpool.BaseClassPoolDomain;
import org.jboss.aop.classpool.ClassPoolDomain;
import org.jboss.aop.classpool.DelegatingClassPool;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.dependency.Module;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBossClClassPoolDomain extends BaseClassPoolDomain
{
   Map<String, Set<DelegatingClassPool>> poolsByPackage = new HashMap<String, Set<DelegatingClassPool>>();
   final static List<DelegatingClassPool> EMPTY_LIST = Collections.unmodifiableList(Collections.EMPTY_LIST);
   
   AspectManager manager = AspectManager.instance();
   
   public JBossClClassPoolDomain(String domainName, ClassPoolDomain parent, ParentPolicy parentPolicy)
   {
      super(domainName, 
            new JBossClParentDelegationStrategy(
                  parent, 
                  parentPolicy, 
                  JBossClClassPoolToClassPoolDomainAdaptorFactory.getInstance())
      );
   }

   synchronized void setupPoolsByPackage(DelegatingClassPool pool)
   {
      if (pool instanceof JBossClDelegatingClassPool == false)
      {
         throw new IllegalStateException("Not an instance of JBossClDelegatingClassPool: " + pool.getClass().getName());
      }
      
      Module module = getModuleForPool(pool);
      
      for (String pkg : module.getPackageNames())
      {
         Set<DelegatingClassPool> pools = poolsByPackage.get(pkg);
         if (pools == null)
         {
            pools = new LinkedHashSet<DelegatingClassPool>();
            poolsByPackage.put(pkg, pools);
         }
         pools.add(pool);
      }
   }
   
   @Override
   public synchronized void removeClassPool(DelegatingClassPool pool)
   {
      super.removeClassPool(pool);
   
      Module module = getModuleForPool(pool);
      
      for (String pkg : module.getPackageNames())
      {
         Set<DelegatingClassPool> pools = poolsByPackage.get(pkg);
         if (pools != null)
         {
            pools.remove(pool);
         }
      }
   }
 
   @Override
   public CtClass getCachedOrCreateInternal(DelegatingClassPool initiatingPool, String classname, String resourceName, boolean create)
   {
      Module module = getModuleForPool(initiatingPool);
      if (module != null && module.isImportAll())
      {
         //Use the old "big ball of mud" model
         return super.getCachedOrCreateInternal(initiatingPool, classname, resourceName, create);
      }
      
      //Attempt OSGi style loading
      CtClass clazz = null;
      if (isParentBefore(classname))
      {
         clazz = getCachedOrCreateInternalFromParent(null, classname, resourceName, create);
      }
      
      //Check imports first
      if (module != null)
      {
         List<? extends DelegateLoader> delegates = module.getDelegates();
         if (delegates != null)
         {
            for (DelegateLoader delegate : delegates)
            {
               //TODO This is a hack, need a proper API in jboss-cl
               System.err.println("Commented out loader from delegate in JBossClClassPoolDomain");
               ClassLoader loader = delegate.getBaseClassLoader("a BaseClassLoader", "");
               
               //TODO Should be a nicer way to do this
               ClassPool pool = manager.findClassPool(loader);
               try
               {
                  clazz = pool.get(classname);
               }
               catch(NotFoundException e)
               {
               }
            }
         }
      }
      
      //Try to check the initiating pool itself
      if (clazz == null && initiatingPool != null)
      {
         clazz = initiatingPool.loadLocally(classname, resourceName, create);
      }
      
      if (clazz == null && isParentAfter(classname))
      {
         clazz = getCachedOrCreateInternalFromParent(null, classname, resourceName, create);
      }
      return clazz;
   }
   
   private Module getModuleForPool(DelegatingClassPool pool)
   {
      if (pool == null)
      {
         return null;
      }
      return ((JBossClDelegatingClassPool)pool).getModule();
   }
   
   @Override
   protected List<DelegatingClassPool> getPoolsForPackage(String packageName)
   {
      Set<DelegatingClassPool> poolSet = poolsByPackage.get(packageName);
      if (poolSet == null)
      {
         return EMPTY_LIST;
      }
      return new ArrayList<DelegatingClassPool>(poolSet);
   }

}
