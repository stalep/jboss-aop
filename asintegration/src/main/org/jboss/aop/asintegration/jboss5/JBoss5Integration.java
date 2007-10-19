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
package org.jboss.aop.asintegration.jboss5;

import java.io.File;

import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolFactory;
import javassist.scopedpool.ScopedClassPoolRepository;

import org.jboss.aop.AspectManager;
import org.jboss.aop.classpool.AOPClassLoaderScopingPolicy;
import org.jboss.aop.asintegration.JBossIntegration;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.mx.loading.RepositoryClassLoader;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBoss5Integration implements JBossIntegration, ScopedClassPoolFactory
{
   /** The delegate classpool factory */
   private ScopedClassPoolFactory delegateClassPoolFactory;
   private AOPClassLoaderScopingPolicy policy;
   private AspectManager manager = AspectManager.getTopLevelAspectManager();
   DefaultTranslator translator = new DefaultTranslator(manager);

   NewClassLoaderDomainRegistry registry;
   
   public NewClassLoaderDomainRegistry getRegistry()
   {
      return registry;
   }

   public void setRegistry(NewClassLoaderDomainRegistry registry)
   {
      this.registry = registry;
   }
   
   public void start()
   {
      ClassLoaderSystem.getInstance().setTranslator(translator);
   }

   public void stop()
   {
      ClassLoaderSystem.getInstance().setTranslator(null);
   }
   
   public boolean isValidClassLoader(ClassLoader loader)
   {
      if (!(loader instanceof RepositoryClassLoader)) return false;
      return ((RepositoryClassLoader) loader).getLoaderRepository() != null;
   }

   public void setScopingPolicy(AOPClassLoaderScopingPolicy policy)
   {
      this.policy = policy;
   }
   
   public AOPClassLoaderScopingPolicy getScopingPolicy()
   {
      return policy;
   }
   
   public AOPClassLoaderScopingPolicy createAOPClassLoaderScopingPolicy()
   {
      return policy;
   }

   public ScopedClassPoolFactory createScopedClassPoolFactory(File tmpDir) throws Exception
   {
      delegateClassPoolFactory = new JBoss5ClassPoolFactory();
      return this;
   }
   
   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      return delegateClassPoolFactory.create(cl, src, repository);
   }

   public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository)
   {
      return delegateClassPoolFactory.create(src, repository);
   }
   
   public void attachDeprecatedTranslator()
   {
      translator.setTranslate(true);
   }

   public void detachDeprecatedTranslator()
   {
      translator.setTranslate(false);
   }
}
