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

import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolFactory;
import javassist.scopedpool.ScopedClassPoolRepository;

import org.jboss.aop.asintegration.jboss5.DomainRegistry;
import org.jboss.aop.classpool.AbstractJBossClassPoolFactory;
import org.jboss.aop.classpool.ClassPoolDomain;
import org.jboss.aop.classpool.ClassPoolDomainRegistry;
import org.jboss.aop.classpool.NonDelegatingClassPool;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloading.spi.RealClassLoader;
import org.jboss.classloading.spi.dependency.Module;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBossClClassPoolFactory extends AbstractJBossClassPoolFactory implements ScopedClassPoolFactory
{
   private DomainRegistry registry;
   
   public JBossClClassPoolFactory(DomainRegistry registry)
   {
      this.registry = registry;
   }
   
   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      ClassPool parent = getCreateParentClassPools(cl, src, repository);

      if (cl instanceof RealClassLoader)
      {
         Module module = registry.getModule(cl);
         ClassPoolDomain domain = getDomain(module, cl);  
         return new JBossClDelegatingClassPool(domain, cl, parent, repository, module);
      }
      
      return new NonDelegatingClassPool(cl, parent, repository, true);
   }

   private ClassPoolDomain getDomain(Module module, ClassLoader cl)
   {
      ClassLoaderDomain domain = null;
      ClassLoaderSystem sys = registry.getSystem();
      if (module != null && module.getDeterminedParentDomainName() != null)
      {
         //It is scoped
         domain = sys.getDomain(module.getDeterminedDomainName());
      }
      
      if (domain == null)
      {
         domain = sys.getDefaultDomain();
      }
      
      ClassPoolDomain poolDomain = ClassPoolDomainRegistry.getInstance().getDomain(domain);
      if (poolDomain == null)
      {
         String parentDomainName = domain.getParentDomainName();
         ClassPoolDomain parentPoolDomain = null;
         if (parentDomainName != null)
         {
            ClassLoaderDomain parentDomain = sys.getDomain(parentDomainName);
            if (parentDomain == null)
            {
               throw new RuntimeException("No domain found called: " + parentDomainName);
            }
            parentPoolDomain = ClassPoolDomainRegistry.getInstance().getDomain(parentDomain);
         }
         poolDomain = new JBossClClassPoolDomain(domain.getName(), parentPoolDomain, domain.getParentPolicy(), registry);
         
         ClassPoolDomainRegistry.getInstance().addClassPoolDomain(domain, poolDomain);
      }
      return poolDomain;
   }
   
   @Override
   protected ClassPool getCreateParentClassPools(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      ClassPool parent = super.getCreateParentClassPools(cl, src, repository);
      if (parent == ClassPool.getDefault())
      {
         //In AS BaseClassLoader seems to normally have a null parent
         return null;
      }
      return parent;
   }
}
