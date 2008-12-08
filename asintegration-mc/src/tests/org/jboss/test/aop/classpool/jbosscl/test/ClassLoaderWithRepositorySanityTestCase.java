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

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderDomain;

/**
 * Tests the behaviour of the new classloaders
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderWithRepositorySanityTestCase extends JBossClClassPoolTest
{

   public ClassLoaderWithRepositorySanityTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ClassLoaderWithRepositorySanityTestCase.class);
   }

   public void testGlobalScope() throws Exception
   {
      ClassLoader clA = null;
      ClassLoader clB = null;
      try
      {
         clA = createClassLoader("A", true, JAR_A_1);
         assertModule(clA);
         clB = createClassLoader("B", true, JAR_B_1);
         assertModule(clB);
         try
         {
            Class<?> aFromA = clA.loadClass(CLASS_A);
            assertNotNull(aFromA);
            Class<?> bFromA = clA.loadClass(CLASS_B);
            assertNotNull(bFromA);
            Class<?> aFromB = clB.loadClass(CLASS_A);
            Class<?> bFromB = clB.loadClass(CLASS_B);
            assertSame(aFromA, aFromB);
            assertSame(bFromA, bFromB);
            assertSame(clA, aFromA.getClassLoader());
            assertSame(clB, bFromB.getClassLoader());
         }
         finally
         {
            unregisterClassLoader(clB);
         }
         Class<?> aFromA = clA.loadClass(CLASS_A);
         assertNotNull(aFromA);

         assertCannotLoadClass(clA, CLASS_B);
      }
      finally
      {
         unregisterClassLoader(clA);
      }
      assertCannotLoadClass(getDefaultDomain(), CLASS_A);
   }

   public void testChildDomain() throws Exception
   {
      ClassLoader clA = null;
      ClassLoader clB = null;
      ClassLoaderDomain childDomain = null;
      ClassLoader clC = null;
      try
      {
         clA = createClassLoader("A", true, JAR_A_1);
         assertModule(clA);
         clB = createClassLoader("B", true, JAR_B_2);
         assertModule(clB);
         clC = createChildDomainParentFirstClassLoader("C", "CHILD", true, JAR_C_1);
         assertModule(clC);
         
         childDomain = getChildDomainForLoader(clC);
         assertNotNull(childDomain);
         assertSame(getSystem().getDefaultDomain(), childDomain.getParent());

         Class<?> aFromA = clA.loadClass(CLASS_A);
         assertNotNull(aFromA);
         Class<?> bFromB = clB.loadClass(CLASS_B);
         assertNotNull(bFromB);
         Class<?> cFromC = clC.loadClass(CLASS_C);
         assertNotNull(cFromC);
         Class<?> aFromC = clC.loadClass(CLASS_A);
         assertNotNull(aFromC);
         Class<?> bFromC = clC.loadClass(CLASS_B);
         assertNotNull(bFromC);
         
         assertSame(aFromA, aFromC);
         assertSame(bFromB, bFromC);
         assertSame(clA, aFromA.getClassLoader());
         assertSame(clB, bFromB.getClassLoader());
         assertSame(clC, cFromC.getClassLoader());
         
         assertCannotLoadClass(clA, CLASS_C);
         assertCannotLoadClass(clB, CLASS_C);
      }
      finally
      {
         unregisterClassLoader(clA);
         unregisterClassLoader(clB);
         unregisterClassLoader(clC);
         unregisterDomain(childDomain);
      }
   }
   
   public void testSiblingDomains() throws Exception
   {
      ClassLoader clA = null;
      ClassLoader clB = null;
      ClassLoaderDomain domainA = null;
      ClassLoaderDomain domainB = null;
      try
      {
         clA = createChildDomainParentFirstClassLoader("A", "ChildA", true, JAR_A_1);
         assertModule(clA);
         clB = createChildDomainParentLastClassLoader("B", "ChildB", true, JAR_B_1);
         assertModule(clB);

         domainA = getChildDomainForLoader(clA);
         assertNotNull(domainA);
         assertSame(getSystem().getDefaultDomain(), domainA.getParent());
         domainB = getChildDomainForLoader(clB);
         assertNotNull(domainB);
         assertSame(getSystem().getDefaultDomain(), domainB.getParent());
         assertNotSame(domainA, domainB);

         Class<?> clazzA = clA.loadClass(CLASS_A);
         assertSame(clA, clazzA.getClassLoader());
         Class<?> clazzB = clB.loadClass(CLASS_B);
         assertSame(clB, clazzB.getClassLoader());
         assertCannotLoadClass(clA, CLASS_B);
         assertCannotLoadClass(clB, CLASS_A);
      }
      finally
      {
         unregisterClassLoader(clA);
         unregisterClassLoader(clB);
         unregisterDomain(clA);
         unregisterDomain(clB);
      }
   }
   
   public void testChildWithNewClassesInParent() throws Exception
   {
      ClassLoader clGlobal = null;
      ClassLoader clScoped = null;
      try
      {
         clScoped = createChildDomainParentFirstClassLoader("SCOPED", "SCOPED", true, JAR_B_1);
         assertModule(clScoped);
         assertCannotLoadClass(clScoped, CLASS_A);
         
         clGlobal = createClassLoader("GLOBAL", true, JAR_A_1);
         assertModule(clScoped);
         assertModule(clGlobal);

         Class<?> aFromChild = clScoped.loadClass(CLASS_A);
         assertNotNull(aFromChild);
         Class<?> aFromParent = clGlobal.loadClass(CLASS_A);
         assertNotNull(aFromParent);
         assertSame(aFromChild, aFromParent);
         assertSame(clGlobal, aFromParent.getClassLoader());
      }
      finally
      {
         unregisterClassLoader(clGlobal);
         unregisterClassLoader(clScoped);
         unregisterDomain(clScoped);
      }
   }
   
   public void testChildOverrideWithParentDelegation() throws Exception
   {
      ClassLoader clGlobal = null;
      ClassLoader clScoped = null;
      try
      {
         clGlobal = createClassLoader("GLOBAL", true, JAR_A_1);
         clScoped = createChildDomainParentFirstClassLoader("SCOPED", "SCOPED", true, JAR_B_1);
         Class<?> aFromParent = clGlobal.loadClass(CLASS_A);
         assertNotNull(aFromParent);
         Class<?> aFromChild = clScoped.loadClass(CLASS_A);
         assertNotNull(aFromChild);
         assertSame(aFromParent, aFromChild);
         assertSame(clGlobal, aFromParent.getClassLoader());
      }
      finally
      {
         unregisterClassLoader(clGlobal);
         unregisterClassLoader(clScoped);
         unregisterDomain(clScoped);
      }
   }
   
   public void testChildOverrideWithNoParentDelegation() throws Exception
   {
      ClassLoader clGlobal = null;
      ClassLoader clScoped = null;
      try
      {
         clGlobal = createClassLoader("GLOBAL", true, JAR_A_1);
         clScoped = createChildDomainParentLastClassLoader("CHILD", "CHILD", true, JAR_A_1);
         Class<?> aFromParent = clGlobal.loadClass(CLASS_A);
         assertNotNull(aFromParent);
         Class<?> aFromChild = clScoped.loadClass(CLASS_A);
         assertNotNull(aFromChild);
         assertNotSame(aFromParent, aFromChild);
         assertSame(clGlobal, aFromParent.getClassLoader());
         assertSame(clScoped, aFromChild.getClassLoader());
      }
      finally
      {
         unregisterClassLoader(clGlobal);
         unregisterClassLoader(clScoped);
         unregisterDomain(clScoped);
      }
   }
   
   public void testURLChildOfGlobalUcl() throws Exception
   {
      ClassLoader clGlobal = null;
      ClassLoader clChildA = null;
      ClassLoader clChildB = null;
      try
      {
         clGlobal = createClassLoader("GLOBAL", true, JAR_A_1);
         clChildA = createChildURLClassLoader(clGlobal, JAR_B_1);
         
         Class<?> aFromA = clChildA.loadClass(CLASS_A);
         assertSame(clGlobal, aFromA.getClassLoader());
         Class<?> bFromA = clChildA.loadClass(CLASS_B);
         assertSame(clChildA, bFromA.getClassLoader());
         
         clChildB = createChildURLClassLoader(clGlobal, JAR_A_2);
         Class<?> aFromB = clChildB.loadClass(CLASS_A);
         assertSame(clGlobal, aFromB.getClassLoader());
      }
      finally
      {
         unregisterClassLoader(clGlobal);
         unregisterClassLoader(clChildA);
         unregisterClassLoader(clChildB);
      }
   }
   
   public void testUndeploySibling() throws Exception
   {
      ClassLoader clA = null;
      ClassLoader clB = null;
      try
      {
         try
         {
            clA = createClassLoader("A", true, JAR_A_1);
            assertCannotLoadClass(clA, CLASS_B);
            
            clB = createClassLoader("B", true, JAR_B_1);
            Class<?> bFromA = clA.loadClass(CLASS_B);
            assertSame(clB, bFromA.getClassLoader());
         }
         finally
         {
            unregisterClassLoader(clB);
         }
         assertCannotLoadClass(clA, CLASS_B);
      }
      finally
      {
         unregisterClassLoader(clA);
      }
   }

   
   public void testUndeployParentDomainClassLoader() throws Exception
   {
      ClassLoader globalA = null;
      ClassLoader globalB = null;
      ClassLoader child = null;
      try
      {
         try
         {
            globalA = createClassLoader("A", true, JAR_A_1);
            assertCannotLoadClass(globalA, CLASS_B);
            
            child = createChildDomainParentLastClassLoader("C", "C", true, JAR_C_1);
            assertCannotLoadClass(child, CLASS_B);
            
            globalB = createClassLoader("B", true, JAR_B_1);
            Class<?> bFromChild = child.loadClass(CLASS_B);
            Class<?> bFromA = globalA.loadClass(CLASS_B);
            assertSame(globalB, bFromA.getClassLoader());
            assertSame(bFromA, bFromChild);
         }
         finally
         {
            unregisterClassLoader(globalB);
         }
         assertCannotLoadClass(child, CLASS_B);
      }
      finally
      {
         unregisterClassLoader(globalA);
         unregisterClassLoader(child);
         unregisterDomain(getChildDomainForLoader(child));
      }
   }
   
   public void testClassLoaderlWithParentClassLoader() throws Exception
   {
      ClassLoader parent = createChildURLClassLoader(null, JAR_B_1);
      ClassLoader global = null;
      try
      {
         global = createChildDomainParentFirstClassLoader("A", "A", true, parent, JAR_A_1);
         Class<?> aFromGlobal = global.loadClass(CLASS_A);
         assertSame(global, aFromGlobal.getClassLoader());
         Class<?> bFromGlobal = global.loadClass(CLASS_B);
         assertSame(parent, bFromGlobal.getClassLoader());
         Class<?> bFromParent = parent.loadClass(CLASS_B);
         assertSame(bFromGlobal, bFromParent);
      }
      finally
      {
         unregisterClassLoader(global);
         unregisterClassLoader(parent);
         unregisterDomain(global);
      }
   }

   
   public void testClassLoaderWithParentClassLoaderAndSameClassInDomain() throws Exception
   {
      ClassLoader parent = createChildURLClassLoader(null, JAR_B_1);
      ClassLoader globalA = null;
      ClassLoader globalB = null;
      try
      {
         final String domain = "CHILD";
         globalA = createChildDomainParentFirstClassLoader("A", domain, true, parent, JAR_A_1);
         Class<?> aFromGlobal = globalA.loadClass(CLASS_A);
         assertSame(globalA, aFromGlobal.getClassLoader());

         globalB = createChildDomainParentFirstClassLoader("B", domain, true, parent, JAR_B_1);
         Class<?> bFromGlobalA = globalA.loadClass(CLASS_B);
         assertSame(globalB, bFromGlobalA.getClassLoader());
         Class<?> bFromParent = parent.loadClass(CLASS_B);
         assertSame(parent, bFromParent.getClassLoader());
         assertNotSame(bFromGlobalA, bFromParent);
      }
      finally
      {
         unregisterClassLoader(globalA);
         unregisterClassLoader(globalB);
         unregisterClassLoader(parent);
         unregisterDomain(globalA);
      }
   }


}
