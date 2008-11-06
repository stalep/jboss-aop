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
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javassist.ClassPool;
import junit.framework.Test;

import org.jboss.aop.AspectManager;
import org.jboss.aop.asintegration.jboss5.DomainRegistry;
import org.jboss.aop.asintegration.jboss5.VFSClassLoaderDomainRegistry;
import org.jboss.aop.classpool.jbosscl.JBossClClassPoolFactory;
import org.jboss.classloader.plugins.filter.PatternClassFilter;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.test.support.IsolatedClassLoaderTestHelper;
import org.jboss.classloader.test.support.MockClassLoaderHelper;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * Base class for testing the new JBoss classloaders
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBossClClassPoolTest extends AbstractTestCaseWithSetup
{
   //These might not be needed with the new loaders
//   public final static URL JAR_A_1 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-jmx-test-classpool-a1.jar");
//   public final static URL JAR_A_2 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-jmx-test-classpool-a2.jar");
//   public final static URL JAR_B_1 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-jmx-test-classpool-b1.jar");
//   public final static URL JAR_B_2 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-jmx-test-classpool-b2.jar");
//   public final static URL JAR_C_1 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-jmx-test-classpool-c1.jar");
//   public final static URL JAR_C_2 = getURLRelativeToProjectRoot("target/jboss-aop-asintegration-jmx-test-classpool-c2.jar");

   public static final String PACKAGE_ROOT = "org.jboss.test.aop.classpool.jbosscl.support";
   public static final String PACKAGE_A = PACKAGE_ROOT + ".a";
   public static final String PACKAGE_B = PACKAGE_ROOT + ".b";
   public static final String PACKAGE_C = PACKAGE_ROOT + ".c";

   public final static String CLASS_A = PACKAGE_A + ".A";
   public final static String CLASS_B = PACKAGE_B + ".B";
   public final static String CLASS_C = PACKAGE_C + ".C";
   
   //I don't think this is needed with the way the classloading system works, it maintains the hard references?
   //Keep a strong reference to the classloaders so that they are not garbage collected
   //final static Set<ClassLoader> registeredClassLoaders = new HashSet<ClassLoader>();
   
   private Map<ClassLoader, ClassLoaderDomain> scopedChildDomainsByLoader = new WeakHashMap<ClassLoader, ClassLoaderDomain>();

   /** The classloader helper */
   protected static IsolatedClassLoaderTestHelper helper;
   protected static DomainRegistry domainRegistry;
   
   static
   {
      domainRegistry = new VFSClassLoaderDomainRegistry();
      AspectManager.setClassPoolFactory(new JBossClClassPoolFactory(domainRegistry));
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
      helper = new IsolatedClassLoaderTestHelper();
      ClassFilter aopFilter = new PatternClassFilter(new String[] { "org\\.jboss\\.aop\\..+", "org\\.jboss\\.metadata\\..+"}, new String[] { "org/jboss/aop/.+", "org/jboss/metadata/.+"}, null);
      Class<?> newClass = helper.initializeClassLoader(clazz, aopFilter, importAll, packages);
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


   protected ClassLoader createChildDomainParentFirstClassLoader(String name, String domainName, boolean importAll, String... packages) throws Exception
   {
      return createChildDomainClassLoader(name, domainName, true, importAll, packages);
   }
   
   protected ClassLoader createChildDomainParentLastClassLoader(String name, String domainName, boolean importAll, String... packages) throws Exception
   {
      return createChildDomainClassLoader(name, domainName, false, importAll, packages);
   }
   
   protected ClassLoader createChildDomainClassLoader(String name, String domainName, boolean parentFirst, boolean importAll, String... packages) throws Exception
   {
      ClassLoaderDomain domain = helper.getSystem().getDomain(domainName);
      if (domain == null)
      {
         if (parentFirst)
         {
            domain = createScopedClassLoaderDomainParentFirst(domainName);
         }
         else
         {
            domain = createScopedClassLoaderDomainParentLast(domainName);
         }
      }
      MockClassLoaderPolicy policy = MockClassLoaderHelper.createMockClassLoaderPolicy("A");
      policy.setPathsAndPackageNames(packages);
      ClassLoader classLoader = createClassLoader(domain, policy);
      scopedChildDomainsByLoader.put(classLoader, domain);
      return classLoader;
   }
   
   /**
    * Here since we cannot access this via the classloading api
    */
   protected ClassLoaderDomain getChildDomainForLoader(ClassLoader loader)
   {
      return scopedChildDomainsByLoader.get(loader);
   }
   
   public void unregisterDomain(ClassLoaderDomain domain)
   {
      if (domain != null)
      {
         helper.unregisterDomain(domain.getName());
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
   
   /////////////////////////////////////////////////////////
   // These are lifted from AOPIntegrationTest
   /**
    * Create a classloader
    * 
    * It exports everything
    *
    * @param name the name
    * @param importAll whether to import all
    * @param packages the packages
    * @return the classloader
    * @throws Exception for any error
    */
   protected static ClassLoader createClassLoader(String name, boolean importAll, String... packages) throws Exception
   {
      return helper.createClassLoader(name, importAll, packages);
   }
   
   /**
    * Create a classloader
    * 
    * It exports everything
    *
    * @param name the name
    * @param importAll whether to import all
    * @param packages the packages
    * @return the classloader
    * @throws Exception for any error
    */
   protected static ClassLoader createClassLoaderInDomain(MockClassLoaderPolicy policy) throws Exception
   {
      return helper.createClassLoader(policy);
   }
   
   /**
    * Create a scoped classloader domain using the test domain as parent
    * using the parent first policy
    * 
    * @param name the name
    * @return the domain
    */
   public ClassLoaderDomain createScopedClassLoaderDomainParentFirst(String name)
   {
      return helper.createScopedClassLoaderDomainParentFirst(name);
   }

   /**
    * Create a scoped classloader domain using the test domain as parent
    * using the parent last policy
    * 
    * @param name the name
    * @return the domain
    */
   public ClassLoaderDomain createScopedClassLoaderDomainParentLast(String name)
   {
      return helper.createScopedClassLoaderDomainParentLast(name);
   }

   /**
    * Create a classloader
    *
    * @param domain the domain
    * @param policy the policy
    * @return the classloader
    * @throws Exception for any error
    */
   public ClassLoader createClassLoader(ClassLoaderDomain domain, MockClassLoaderPolicy policy) throws Exception
   {
      return helper.createClassLoader(domain, policy);
   }

   /**
    * Unregister a classloader
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected static void unregisterClassLoader(ClassLoader classLoader) throws Exception
   {
      helper.unregisterClassLoader(classLoader);
   }

   /**
    * Create the default delegate loader
    * 
    * @return the loaders
    */
   public List<? extends DelegateLoader> createDefaultDelegates()
   {
      return helper.createDefaultDelegates();
   }

   /**
    * Create delegate loaders from policies
    * 
    * @param policies the policies
    * @return the loaders
    */
   public List<? extends DelegateLoader> createDelegates(ClassLoaderPolicy... policies)
   {
      return helper.createDelegates(policies);
   }


   /**
    * Unregister a domain
    * 
    * @param name the domain name
    */
   public void unregisterDomain(String name)
   {
      helper.unregisterDomain(name);
   }
   
}
