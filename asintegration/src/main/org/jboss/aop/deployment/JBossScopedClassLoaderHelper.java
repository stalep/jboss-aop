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
package org.jboss.aop.deployment;

import org.jboss.aop.AspectManager;
import org.jboss.aop.Domain;
import org.jboss.aop.classpool.AOPClassLoaderScopingPolicy;
import org.jboss.aop.classpool.AOPScopedClassLoaderHelper;
import org.jboss.mx.loading.HeirarchicalLoaderRepository3;
import org.jboss.mx.loading.LoaderRepository;
import org.jboss.mx.loading.RepositoryClassLoader;

/**
 * 
 * TODO JBAOP-107 Need a different version for the JBoss5 classloader
 * @deprecated use {@link AOPClassLoaderScopingPolicy}
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @autor adrian@jboss.org
 * @version $Revision$
 */
@Deprecated
public class JBossScopedClassLoaderHelper implements AOPScopedClassLoaderHelper
{
   public ClassLoader ifScopedDeploymentGetScopedParentUclForCL(ClassLoader loader)
   {
      ClassLoader parent = loader;
      //The web classloader will be a child of the unified classloader - find out if that is scoped
      while (parent != null)
      {
         if (isScopedClassLoader(parent))
         {
            return parent;
         }
         if (parent instanceof RepositoryClassLoader)
         {
            //We were a repository classloader, but not scoped - ignore the parents like a sulky teenager
            return null;
         }
         parent = parent.getParent();
      }
      return null;
   }
   
   public static boolean isScopedClassLoader(ClassLoader loader)
   {
      boolean scoped = false;
      if (loader instanceof RepositoryClassLoader)
      {
         LoaderRepository repository = ((RepositoryClassLoader)loader).getLoaderRepository();
         if (repository instanceof HeirarchicalLoaderRepository3)
         {
            scoped = true;
            HeirarchicalLoaderRepository3 hlr = (HeirarchicalLoaderRepository3)repository;
            boolean parentFirst = hlr.getUseParentFirst();
         }
      }
      return scoped;
   }
   

   public ClassLoader getTopLevelJBossClassLoader()
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      RepositoryClassLoader topRcl = null;
      while (loader != null)
      {
         if (loader instanceof RepositoryClassLoader)
         {
            topRcl = (RepositoryClassLoader)loader;
         }
         loader = loader.getParent();
      }
      return topRcl;
   }

   public Domain getScopedClassLoaderDomain(ClassLoader cl, AspectManager parent)
   {
      boolean parentDelegation = true;
      if (cl instanceof RepositoryClassLoader)
      {
         HeirarchicalLoaderRepository3 repository = (HeirarchicalLoaderRepository3)((RepositoryClassLoader)cl).getLoaderRepository();
         parentDelegation = repository.getUseParentFirst();
      }
      String name = String.valueOf(System.identityHashCode(cl));
      return new ScopedClassLoaderDomain(cl, name, parentDelegation, parent, false);
   }

   public Object getLoaderRepository(ClassLoader loader)
   {
      ClassLoader cl = ifScopedDeploymentGetScopedParentUclForCL(loader);
      if (cl != null)
      {
         return ((RepositoryClassLoader)cl).getLoaderRepository();
      }
      return null;
   }
}
