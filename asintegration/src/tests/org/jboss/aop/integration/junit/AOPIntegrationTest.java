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
package org.jboss.aop.integration.junit;

import java.util.List;

import junit.framework.Test;

import org.jboss.classloader.plugins.filter.PatternClassFilter;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.test.support.IsolatedClassLoaderTestHelper;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * AOPIntegrationTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 42515 $
 */
public abstract class AOPIntegrationTest extends AbstractTestCaseWithSetup
{
   /** The classloader helper */
   private static IsolatedClassLoaderTestHelper helper;
   
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
      ClassFilter aopFilter = new PatternClassFilter(new String[] { "org\\.jboss\\.aop\\..+"}, new String[] { "org/jboss/aop/.+" });
      Class<?> newClass = helper.initializeClassLoader(clazz, aopFilter, importAll, packages);
      return AbstractTestCaseWithSetup.suite(newClass);
   }

   /**
    * Get the test delegate
    * 
    * @param clazz the test class
    * @return the delegate
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz)
   {
      return new AOPIntegrationTestDelegate(clazz, helper.getSystem());
   }

   /**
    * Create a new AOPTest.
    * 
    * @param name the test name
    */
   public AOPIntegrationTest(String name)
   {
      super(name);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }

   /**
    * Get the delegate
    * 
    * @return the delegate
    */
   protected AOPIntegrationTestDelegate getMCDelegate()
   {
      return (AOPIntegrationTestDelegate) getDelegate();
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
   protected static ClassLoader createClassLoader(String name, boolean importAll, String... packages) throws Exception
   {
      return helper.createClassLoader(name, importAll, packages);
   }
   
   /**
    * Create a classloader
    * 
    * It exports everything
    *
    * @param policy the policy
    * @return the classloader
    * @throws Exception for any error
    */
   protected static ClassLoader createClassLoader(MockClassLoaderPolicy policy) throws Exception
   {
      return helper.createClassLoader(policy);
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
    * Unregister a domain
    * 
    * @param name the domain name
    */
   public void unregisterDomain(String name)
   {
      helper.unregisterDomain(name);
   }
}
