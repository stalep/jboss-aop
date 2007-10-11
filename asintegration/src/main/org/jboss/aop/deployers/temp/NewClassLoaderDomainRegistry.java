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
package org.jboss.aop.deployers.temp;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

import org.jboss.aop.Domain;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.deployers.plugins.classloading.Module;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
class NewClassLoaderDomainRegistry
{
   final static ClassLoaderDomain domain = new ClassLoaderDomain("NOT_USED_PLACEHOLDER");
   
   /** classloader domains by their classloaders */
   private Map<ClassLoader, WeakReference<ClassLoaderDomain>> classLoaderDomainsByLoader = new WeakHashMap<ClassLoader, WeakReference<ClassLoaderDomain>>();

   /** aopDomains by classloader domain */
   private Map<ClassLoaderDomain, ScopedNewClassLoaderDomain> aopDomainsByClassLoaderDomain = new WeakHashMap<ClassLoaderDomain, ScopedNewClassLoaderDomain>();

   synchronized void initMapsForLoader(ClassLoader loader, Module module, ScopedNewClassLoaderDomain domain)
   {
      ClassLoaderSystem system = ClassLoaderSystem.getInstance();
      
      String domainName = module.getDomainName();
      ClassLoaderDomain clDomain = system.getDomain(domainName);
      classLoaderDomainsByLoader.put(loader, new WeakReference<ClassLoaderDomain>(clDomain));
      
      if (domain != null)
      {
         aopDomainsByClassLoaderDomain.put(clDomain, domain);
      }
   }

   synchronized Domain getRegisteredDomain(ClassLoader cl)
   {
      ClassLoaderDomain clDomain = getClassLoaderDomainForLoader(cl);
      if (clDomain != null)
      {
         return aopDomainsByClassLoaderDomain.get(clDomain);
      }
      return null;
   }
   
   synchronized ClassLoaderDomain getClassLoaderDomainForLoader(ClassLoader cl)
   {
      WeakReference<ClassLoaderDomain> clDomainRef = classLoaderDomainsByLoader.get(cl);
      if (clDomainRef != null)
      {
         return clDomainRef.get();
      }
      
      ClassLoader parent = cl.getParent();
      if (parent != null)
      {
         ClassLoaderDomain domain = getClassLoaderDomainForLoader(parent);
         if (domain != null)
         {
            classLoaderDomainsByLoader.put(parent, new WeakReference<ClassLoaderDomain>(domain));
            return domain;
         }
      }
      return null;
   }
}
