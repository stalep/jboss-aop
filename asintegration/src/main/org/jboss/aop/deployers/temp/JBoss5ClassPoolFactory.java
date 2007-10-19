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
package org.jboss.aop.deployers.temp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.jboss.aop.AspectManager;
import org.jboss.aop.classpool.AOPClassLoaderScopingPolicy;
import org.jboss.aop.classpool.AOPClassPool;
import org.jboss.aop.classpool.AbstractJBossClassPoolFactory;
import org.jboss.aop.deployment.JBossClassPool;
import org.jboss.aop.deployment.JBossClassPoolFactory;
import org.jboss.aop.deployment.ScopedJBossClassPool;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.base.BaseClassLoader;
import org.jboss.deployers.plugins.classloading.Module;
import org.jboss.mx.loading.RepositoryClassLoader;
import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolFactory;
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * Comment
 *
 * @deprecated TODO JBAOP-107 Need a different version for the JBoss5 classloader 
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author adrian@jboss.org
 * @version $Revision: 64815 $
 **/
@Deprecated
public class JBoss5ClassPoolFactory extends AbstractJBossClassPoolFactory implements ScopedClassPoolFactory
{
   static ThreadLocal<Boolean> reentry = new ThreadLocal<Boolean>();    
   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      
      ClassPool parent = getCreateParentClassPools(cl, src, repository);
      if (cl instanceof BaseClassLoader)
      {
         Map props = ExtraClassPoolFactoryParameters.peekThreadProperties();
         Module module = (Module)props.get(Module.class);
            
         if (module != null && module.getParentDomain() != null)
         {
            //It is scoped
            ClassLoaderSystem sys = ClassLoaderSystem.getInstance();
            ClassLoaderDomain domain = sys.getDomain(module.getDomainName());
            boolean parentFirst = module.getMetadata().isJ2seClassLoadingCompliance();
            
            return new ScopedJBoss5ClassPool(cl, parent, repository, getTempURL(module), parentFirst, domain);
         }
         return new JBoss5ClassPool(cl, parent, repository, getTempURL(module));
      }
      return new AOPClassPool(cl, parent, repository);
   }
   
   private URL getTempURL(Module module)
   {
      try
      {
         URL tempUrl = module.getDynamicClassRoot();
         return new URL(tempUrl,  "/classes");
      }
      catch (MalformedURLException e)
      {
         throw new RuntimeException(e);
      } 
   }
}
