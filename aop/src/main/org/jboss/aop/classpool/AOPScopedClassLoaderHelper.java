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
package org.jboss.aop.classpool;

import org.jboss.aop.AspectManager;
import org.jboss.aop.Domain;

/**
 * Helper to determine if we are running within a scoped classloader. 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public interface AOPScopedClassLoaderHelper
{
   /**
    * Checks if passed in ClassLoader is a scoped JBoss classloader
    * @param loader The loader to check
    * @return The nearest parent (or self) in the hierarchy that is scoped, or null if not scoped
    */
   ClassLoader ifScopedDeploymentGetScopedParentUclForCL(ClassLoader loader);

   /**
    * Returns the top-level JBoss classloader 
    */
   ClassLoader getTopLevelJBossClassLoader();
   
   /**
    * Creates a new domain for the passed in classloader with the passed in AspectManager as the parent
    * @param loader A scoped classloader
    * @param parent The AspectManager to create the domain under
    * @return The created domain for the scoped classloader
    */
   Domain getScopedClassLoaderDomain(ClassLoader cl, AspectManager parent);
   
   /**
    * Gets the loader repository for the passed in scoped JBossClassLoader
    * @param loader The classloader to get the loader repository for
    * @return The LoaderRepository for the scopedc ClassLoader, or null if ClassLoader is not scoped
    */
   Object getLoaderRepository(ClassLoader loader);
}
