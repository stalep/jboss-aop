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
package org.jboss.test.aop.classpool.jbosscl.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javassist.ClassPool;
import junit.framework.Test;

import org.jboss.aop.AspectManager;
import org.jboss.aop.asintegration.jboss5.VFSClassLoaderDomainRegistry;
import org.jboss.aop.classpool.jbosscl.JBossClClassPoolFactory;
import org.jboss.classloader.plugins.filter.PatternClassFilter;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.test.support.IsolatedClassLoaderTestHelper;
import org.jboss.classloading.spi.dependency.ClassLoading;
import org.jboss.classloading.spi.dependency.Module;
import org.jboss.classloading.spi.dependency.policy.ClassLoaderPolicyModule;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.aop.classpool.jbosscl.support.MockModuleFactory;
import org.jboss.virtual.VFS;

/**
 * Base class for testing the new JBoss classloaders
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBossClClassPoolTest extends AbstractTestCaseWithSetup
{
   //These might not be needed with the new loaders
   public final static URL JAR_A_1 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-mc-test-classpool-a1.jar");
   public final static URL JAR_A_2 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-mc-test-classpool-a2.jar");
   public final static URL JAR_B_1 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-mc-test-classpool-b1.jar");
   public final static URL JAR_B_2 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-mc-test-classpool-b2.jar");
   public final static URL JAR_C_1 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-mc-test-classpool-c1.jar");
   public final static URL JAR_C_2 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-mc-test-classpool-c2.jar");

   public static final String PACKAGE_ROOT = "org.jboss.test.aop.classpool.jbosscl.support";
   public static final String PACKAGE_A = PACKAGE_ROOT + ".a";
   public static final String PACKAGE_B = PACKAGE_ROOT + ".b";
   public static final String PACKAGE_C = PACKAGE_ROOT + ".c";

   public final static String CLASS_A = PACKAGE_A + ".A";
   public final static String CLASS_B = PACKAGE_B + ".B";
   public final static String CLASS_C = PACKAGE_C + ".C";
   
   //Keep strong references to the Modules since the domainRegistry only has weak references
   private Set<Module> modules = new HashSet<Module>();
   
   //Keep a strong reference to the URL classloaders so that they are not garbage collected
   final static Set<URLClassLoader> registeredURLClassLoaders = new HashSet<URLClassLoader>();

   private Map<ClassLoader, ClassLoaderDomain> scopedChildDomainsByLoader = new WeakHashMap<ClassLoader, ClassLoaderDomain>();

   /** The classloader helper */
   //protected static IsolatedClassLoaderTestHelper helper;
   protected static final ClassLoaderSystem system = ClassLoaderSystem.getInstance();
   protected static VFSClassLoaderDomainRegistry domainRegistry;
   
   ClassLoading classLoading = new ClassLoading();

   static
   {
      domainRegistry = new VFSClassLoaderDomainRegistry();
      AspectManager.setClassPoolFactory(new JBossClClassPoolFactory(domainRegistry));
      VFS.init();
   }

   
   public static Test suite(Class<?> clazz)
   {
      return suite(clazz, new Class[0]);
   }
   
   public static Test suite(Class<?> clazz, Class<?>... packages)
   {
      return suite(clazz, true, packages);
   }
   
   public static Test suite(Class<?> clazz, boolean importAll, Class<?>... packages)
   {
      IsolatedClassLoaderTestHelper helper = new IsolatedClassLoaderTestHelper();

      String[] classPatterns = new String[] {
            "org\\.jboss\\.aop\\..+", 
            "org\\.jboss\\.classloading\\..+", 
            "org\\.jboss\\.classloader\\..+", 
            "org\\.jboss\\.virtual\\..+", 
            "org\\.jboss\\.test\\.aop\\.classpool\\.jbosscl\\..+\\..+", 
            "org\\.jboss\\.metadata\\..+"};
      String[] resourcePatterns = new String[] {
            "org/jboss/aop/.+", 
            "org/jboss/classloading/.+", 
            "org/jboss/classloader/.+", 
            "org/jboss/virtual/.+", 
            "org/jboss/test/aop/classpool/jbosscl/.+\\..+", 
            "org/jboss/metadata/.+"};
      ClassFilter aopFilter = new PatternClassFilter(classPatterns, resourcePatterns, null);
      Class<?> newClass = helper.initializeClassLoader(clazz, aopFilter, importAll, packages);
      System.out.println(clazz.getClassLoader());
      System.out.println(newClass.getClassLoader());
      return AbstractTestCaseWithSetup.suite(newClass);
   }

   
   public JBossClClassPoolTest(String name)
   {
      super(name);
   }

   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new JBossClClassPoolTestDelegate(clazz);
   }
   
   protected static URL getURLRelativeToProjectRoot(String relativePath)
   {
      try
      {
         URL url = JBossClClassPoolTest.class.getProtectionDomain().getCodeSource().getLocation();
         String location = url.toString();
         int index = location.lastIndexOf("/asintegration-mc/") + "/asintegration-mc/".length();
         location = location.substring(0, index);
         
         location = location + relativePath;
         return new URL(location);
      }
      catch (MalformedURLException e)
      {
         // AutoGenerated
         throw new RuntimeException(e);
      }
   }
   
   protected ClassLoaderSystem getSystem()
   {
      return system;
   }
   
   protected ClassLoaderDomain getDefaultDomain()
   {
      return system.getDefaultDomain();
   }

   protected ClassLoader createClassLoader(String name, boolean importAll, URL... urls) throws Exception
   {
      ClassLoaderPolicyModule module = MockModuleFactory.createModule(name, importAll, urls);
      return createClassLoader(module);
   }
   
   protected ClassLoader createClassLoader(String name, boolean importAll, ClassLoader parent, URL... urls) throws Exception
   {
      ClassLoaderPolicyModule  module = MockModuleFactory.createModule(name, importAll, urls);
      return createClassLoader(module, parent);
   }
   
   protected ClassLoader createClassLoader(ClassLoaderPolicyModule module)
   {
      return createClassLoader(module, null);
   }
   
   protected ClassLoader createClassLoader(ClassLoaderPolicyModule module, ClassLoader parent)
   {
      classLoading.addModule(module);
      ClassLoader loader = parent == null ? 
            module.registerClassLoaderPolicy(system) : module.registerClassLoaderPolicy(system, parent);
      registerModule(loader, module);
      return loader;
   }
   
   protected ClassLoader createChildDomainParentFirstClassLoader(String name, String domainName, boolean importAll, URL... urls) throws Exception
   {
      return createChildDomainParentFirstClassLoader(name, domainName, importAll, null, urls);
   }
   
   protected ClassLoader createChildDomainParentFirstClassLoader(String name, String domainName, boolean importAll, ClassLoader parent, URL... urls) throws Exception
   {
      return createChildDomainClassLoader(name, domainName, null, true, importAll, parent, urls);
   }
   
   protected ClassLoader createChildDomainParentFirstClassLoader(String name, String domainName, String parentDomainName, boolean importAll, URL... urls) throws Exception
   {
      return createChildDomainClassLoader(name, domainName, parentDomainName, true, importAll, null, urls);
   }
   
   protected ClassLoader createChildDomainParentLastClassLoader(String name, String domainName, boolean importAll, URL... urls) throws Exception
   {
      return createChildDomainParentLastClassLoader(name, domainName, importAll, null, urls);
   }
   
   protected ClassLoader createChildDomainParentLastClassLoader(String name, String domainName, boolean importAll, ClassLoader parent, URL... urls) throws Exception
   {
      return createChildDomainClassLoader(name, domainName, null, false, importAll, parent, urls);
   }
   
   protected ClassLoader createChildDomainParentLastClassLoader(String name, String domainName, String parentDomainName, boolean importAll, URL... urls) throws Exception
   {
      return createChildDomainClassLoader(name, domainName, parentDomainName, false, importAll, null, urls);
   }
   
   protected ClassLoader createChildDomainClassLoader(String name, String domainName, String parentDomainName, boolean parentFirst, boolean importAll, ClassLoader parent, URL... urls) throws Exception
   {
      ClassLoaderPolicyModule module = MockModuleFactory.createModule(name, importAll, domainName, parentDomainName, parentFirst, urls);
      
      ClassLoader classLoader = createClassLoader(module, parent);
      
      ClassLoaderDomain domain = system.getDomain(domainName);
      scopedChildDomainsByLoader.put(classLoader, domain);
      registerModule(classLoader, module);
      return classLoader;
   }
   
   protected ClassLoader createChildURLClassLoader(ClassLoader parent, URL url)
   {
      URLClassLoader cl = new URLClassLoader(new URL[] {url}, parent);
      registeredURLClassLoaders.add(cl);
      return cl;
   }
   
   /**
    * Here since we cannot access this via the classloading api
    */
   protected ClassLoaderDomain getChildDomainForLoader(ClassLoader loader)
   {
      return scopedChildDomainsByLoader.get(loader);
   }
   
   protected ClassLoaderDomain getChildDomainForPool(ClassPool pool)
   {
      return getChildDomainForLoader(pool.getClassLoader());
   }
   
   protected void unregisterDomain(ClassLoaderDomain domain)
   {
      if (domain != null)
      {
         ClassLoaderDomain registeredDomain = system.getDomain(domain.getName());
         if (registeredDomain == null)
            throw new IllegalStateException("Domain is not registered: " + domain.getName());
         if (registeredDomain != domain)
            throw new IllegalStateException(domain + " is not the same as " + registeredDomain);
         system.unregisterDomain(domain);
      }
   }
   
   protected void unregisterDomain(ClassLoader loader)
   {
      if (loader != null)
      {
         ClassLoaderDomain domain = getChildDomainForLoader(loader);
         unregisterDomain(domain);
      }
   }

   protected void unregisterDomain(ClassPool pool)
   {
      if (pool != null)
      {
         ClassLoaderDomain domain = getChildDomainForPool(pool);
         unregisterDomain(domain);
      }
   }

   protected void assertCannotLoadClass(ClassLoader cl, String className)
   {
      try
      {
         cl.loadClass(className);
         fail("Should not have been able to load " + className);
      }
      catch(Exception expected)
      {
      }
   }
   
   protected void assertCannotLoadCtClass(ClassPool pool, String className)
   {
      try
      {
         pool.get(className);
      }
      catch(Exception e)
      {
      }
   }
   
   protected void assertCannotLoadClass(ClassLoaderDomain domain, String className)
   {
      try
      {
         Class<?> clazz = domain.loadClass(className);
         if (clazz == null)
         {
            return;
         }
         fail("Should not have been able to load " + className);
      }
      catch(Exception expected)
      {
      }
   }

   protected ClassPool createClassPool(String name, boolean importAll, URL... urls) throws Exception
   {
      ClassLoader loader = createClassLoader(name, importAll, urls);
      return AspectManager.instance().registerClassLoader(loader);
   }
   
   protected ClassPool createChildDomainParentFirstClassPool(String name, String domainName, boolean importAll, URL... urls) throws Exception
   {
      ClassLoader loader = createChildDomainParentFirstClassLoader(name, domainName, importAll, urls);
      return AspectManager.instance().registerClassLoader(loader);
   }
   
   protected ClassPool createChildDomainParentFirstClassPool(String name, String domainName, boolean importAll, ClassPool parent, URL... urls) throws Exception
   {
      ClassLoader loader = createChildDomainParentFirstClassLoader(name, domainName, importAll, parent.getClassLoader(), urls);
      return AspectManager.instance().registerClassLoader(loader);
   }
   
   protected ClassPool createChildDomainParentLastClassPool(String name, String domainName, boolean importAll, URL... urls) throws Exception
   {
      ClassLoader loader = createChildDomainParentLastClassLoader(name, domainName, importAll, urls);
      return AspectManager.instance().registerClassLoader(loader);
   }

   protected ClassPool createChildURLClassPool(ClassPool parent, URL url)
   {
      ClassLoader parentLoader = null;
      if (parent != null)
      {
         parentLoader = parent.getClassLoader();
      }
      ClassLoader loader = createChildURLClassLoader(parentLoader, url);
      return AspectManager.instance().registerClassLoader(loader);
   }
   
   protected void registerModule(ClassLoader loader, ClassLoaderPolicyModule module)
   {
      if (system != domainRegistry.getSystem())
      {
         domainRegistry.setSystem(system);
      }
      //Add the hard reference to the Module since the registry's reference is weak
      //In the real workd this would be maintained by the deployers
      modules.add(module);
      //TODO I have just hacked the domain and the parentUnitLoader here so this might cause problems
      //with the tests if we try to do weaving. However, it should be fine while just testing pools
      //and loaders
      domainRegistry.initMapsForLoader(loader, module, null, null);
   }
   
   protected void unregisterModule(ClassLoader loader)
   {
      //Remove the hard reference to the Module
      modules.remove(domainRegistry.getModule(loader));
      
      domainRegistry.cleanupLoader(loader);
   }
   
   protected void assertModule(ClassLoader loader)
   {
      ClassLoaderDomain domainForLoader = scopedChildDomainsByLoader.get(loader);
      if (domainForLoader == null)
      {
         //domainForLoader = helper.getDomain();
         domainForLoader = system.getDefaultDomain();
      }
      assertNotNull(domainForLoader);
      
      ClassLoaderDomain domainForModule = domainRegistry.getClassLoaderDomainForLoader(loader);
      assertNotNull(domainForModule);
      assertSame(domainForLoader, domainForModule);
      
      Module module = domainRegistry.getModule(loader);
      assertNotNull(module);
      assertEquals(domainForModule.getName(), module.getDomainName());
      assertEquals(domainForModule.getParentDomainName(), module.getParentDomainName());
   }

   protected void unregisterClassPool(ClassPool pool) throws Exception
   {
      if (pool != null)
      {
         ClassLoader loader = pool.getClassLoader();
         AspectManager.instance().unregisterClassLoader(loader);
         if (loader != null)
         {
            unregisterClassLoader(loader);
         }
      }
   }

   protected void unregisterClassLoader(ClassLoader classLoader) throws Exception
   {
      if (classLoader != null)
      {
         if (registeredURLClassLoaders.remove(classLoader) == false)
         {
            unregisterModule(classLoader);
            system.unregisterClassLoader(classLoader);
         }
      }
   }

   /**
    * Unregister a domain
    * 
    * @param name the domain name
    */
   protected void unregisterDomain(String name)
   {
      if (name != null)
      {
         ClassLoaderDomain registeredDomain = system.getDomain(name);
         unregisterDomain(registeredDomain);
      }
   }
   
   /**
    * The test classes should not be on the launcher classpath
    */
   public void testClassesNotOnClasspath()
   {
      assertCannotLoadClass(this.getClass().getClassLoader(), CLASS_A);
      assertCannotLoadClass(this.getClass().getClassLoader(), CLASS_B);
      assertCannotLoadClass(this.getClass().getClassLoader(), CLASS_C);
   }
}
