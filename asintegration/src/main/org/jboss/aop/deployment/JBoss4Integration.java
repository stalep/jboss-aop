/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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

import java.io.File;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ReflectionException;

import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolFactory;
import javassist.scopedpool.ScopedClassPoolRepository;

import org.jboss.aop.AspectManager;
import org.jboss.aop.classpool.AOPClassLoaderScopingPolicy;
import org.jboss.aop.deployers.temp.RepositoryClassLoaderScopingPolicy;
import org.jboss.mx.loading.RepositoryClassLoader;
import org.jboss.mx.util.MBeanServerLocator;

/**
 * JBoss4Integration.<p>
 * 
 * This class and its associated classes are
 * for the old JBoss4 integration with the LoaderRepository<p>
 * 
 * <ul>Related Classes:
 * <li> {@link JBossClassPool}
 * <li> {@link JBossClassPoolFactory}
 * <li> {@link ScopedRepositoryClassLoaderHelper}
 * <li> {@link LoaderRepositoryUrlUtil}
 * <li> {@link ScopedRepositoryClassLoaderDomain}
 * <li> {@link ScopedJBossClassPool}
 * </ul>
 * 
 * @deprecated TODO JBAOP-107 need to write a JBoss5 version 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
@Deprecated
public class JBoss4Integration implements JBossIntegration, ScopedClassPoolFactory
{
   /** The delegate classpool factory */
   private ScopedClassPoolFactory delegateClassPoolFactory;
   
   public boolean isValidClassLoader(ClassLoader loader)
   {
      if (!(loader instanceof RepositoryClassLoader)) return false;
      return ((RepositoryClassLoader) loader).getLoaderRepository() != null;
   }

   public AOPClassLoaderScopingPolicy createAOPClassLoaderScopingPolicy()
   {
      return new RepositoryClassLoaderScopingPolicy();
   }

   public ScopedClassPoolFactory createScopedClassPoolFactory(File tmpDir) throws Exception
   {
      delegateClassPoolFactory = new JBossClassPoolFactory(tmpDir);
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
      AspectManager mgr = AspectManager.instance();
      MBeanServer server = MBeanServerLocator.locateJBoss();
      try
      {
         server.setAttribute(AspectManagerService.DEFAULT_LOADER_REPOSITORY, new Attribute("Translator", mgr));
      }
      catch (InstanceNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (AttributeNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (InvalidAttributeValueException e)
      {
         throw new RuntimeException(e);
      }
      catch (MBeanException e)
      {
         throw new RuntimeException(e);
      }
      catch (ReflectionException e)
      {
         throw new RuntimeException(e);
      }
   }

   public void detachDeprecatedTranslator()
   {
      MBeanServer server = MBeanServerLocator.locateJBoss();
      try
      {
         server.setAttribute(AspectManagerService.DEFAULT_LOADER_REPOSITORY, new Attribute("Translator", null));
      }
      catch (InstanceNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (AttributeNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (InvalidAttributeValueException e)
      {
         throw new RuntimeException(e);
      }
      catch (MBeanException e)
      {
         throw new RuntimeException(e);
      }
      catch (ReflectionException e)
      {
         throw new RuntimeException(e);
      }
   }
}
