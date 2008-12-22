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

import org.jboss.test.aop.classpool.jbosscl.support.BundleInfoBuilder;

/**
 * Tests the behaviour of the new classloaders so that we can get the same in the new classpools
 * Reproduces org.jboss.test.classloading.vfs.metadata.test.ReExportPackageUnitTestCase using our test framework, 
 * ClassPoolWithReExportPackageTestCase replicates this with the javassist classpools
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderWithReExportModuleSanityTestCase extends JBossClClassPoolTest
{

   public ClassLoaderWithReExportModuleSanityTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ClassLoaderWithReExportModuleSanityTestCase.class);
   }

   public void testReExport() throws Exception
   {
      ClassLoader clA = null;
      try
      {
         BundleInfoBuilder builderA = BundleInfoBuilder.getBuilder().
            createModule("a").
            createPackage(PACKAGE_A);
         clA = createClassLoader("A", builderA, JAR_A_1);
         Class<?> classA = assertLoadClass(CLASS_A, clA);
         assertCannotLoadClass(clA, CLASS_B);
         assertCannotLoadClass(clA, CLASS_C);
         
         ClassLoader clB = null;
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createModule("b").
               createPackage(PACKAGE_B).
               createReExportModule("a");
            clB = createClassLoader("B", builderB, JAR_B_1);
            Class<?> classA1 = assertLoadClass(CLASS_A, clA);
            assertSame(classA, classA1);
            assertCannotLoadClass(clA, CLASS_B);
            assertCannotLoadClass(clA, CLASS_C);
            assertLoadClass(CLASS_A, clB, clA);
            Class<?> classB = assertLoadClass(CLASS_B, clB);
            assertCannotLoadClass(clB, CLASS_C);
            
            ClassLoader clC = null;
            try
            {
               BundleInfoBuilder builderC = BundleInfoBuilder.getBuilder().
                  createRequireModule("b");
               clC = createClassLoader("C", builderC, JAR_C_1);
               
               classA1 = assertLoadClass(CLASS_A, clA);
               assertSame(classA, classA1);
               assertCannotLoadClass(clA, CLASS_B);
               assertCannotLoadClass(clA, CLASS_C);
               classA1 = assertLoadClass(CLASS_A, clB, clA);
               assertSame(classA, classA1);
               Class<?> classB1 = assertLoadClass(CLASS_B, clB);
               assertSame(classB, classB1);
               assertCannotLoadClass(clB, CLASS_C);
               classA1 = assertLoadClass(CLASS_A, clC, clA);
               assertSame(classA, classA1);
               classB1 = assertLoadClass(CLASS_B, clC, clB);
               assertSame(classB, classB1);
               assertLoadClass(CLASS_C, clC);
            }
            finally
            {
               unregisterClassLoader(clC);
            }
            classA1 = assertLoadClass(CLASS_A, clA);
            assertSame(classA, classA1);
            assertCannotLoadClass(clA, CLASS_B);
            assertCannotLoadClass(clA, CLASS_C);
            classA1 = assertLoadClass(CLASS_A, clB, clA);
            assertSame(classA, classA1);
            Class<?> classB1 = assertLoadClass(CLASS_B, clB);
            assertSame(classB, classB1);
            assertCannotLoadClass(clB, CLASS_C);
         }
         finally
         {
            unregisterClassLoader(clB);
         }
         Class<?> classA1 = assertLoadClass(CLASS_A, clA);
         assertSame(classA, classA1);
         assertCannotLoadClass(clA, CLASS_B);
         assertCannotLoadClass(clA, CLASS_C);
      }
      finally
      {
         unregisterClassLoader(clA);
      }
   }
   
   public void testNoReExport() throws Exception
   {
      ClassLoader clA = null;
      try
      {
         BundleInfoBuilder builderA = BundleInfoBuilder.getBuilder().
            createModule("a").
            createPackage(PACKAGE_A);
         clA = createClassLoader("A", builderA, JAR_A_1);
         Class<?> classA = assertLoadClass(CLASS_A, clA);
         assertCannotLoadClass(clA, CLASS_B);
         assertCannotLoadClass(clA, CLASS_C);
         
         ClassLoader clB = null;
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createModule("b").
               createPackage(PACKAGE_B).
               createReExportModule("a");
            clB = createClassLoader("B", builderB, JAR_B_1);
            Class<?> classA1 = assertLoadClass(CLASS_A, clA);
            assertSame(classA, classA1);
            assertCannotLoadClass(clA, CLASS_B);
            assertCannotLoadClass(clA, CLASS_C);
            classA1 = assertLoadClass(CLASS_A, clB, clA);
            assertSame(classA, classA1);
            Class<?> classB = assertLoadClass(CLASS_B, clB);
            assertCannotLoadClass(clB, CLASS_C);
            
            ClassLoader clC = null;
            try
            {
               BundleInfoBuilder builderC = BundleInfoBuilder.getBuilder().
                  createRequirePackage(PACKAGE_B);
               clC = createClassLoader("C", builderC, JAR_C_1);
               
               classA1 = assertLoadClass(CLASS_A, clA);
               assertSame(classA, classA1);
               assertCannotLoadClass(clA, CLASS_B);
               assertCannotLoadClass(clA, CLASS_C);
               classA1 = assertLoadClass(CLASS_A, clB, clA);
               assertSame(classA, classA1);
               Class<?> classB1 = assertLoadClass(CLASS_B, clB);
               assertSame(classB, classB1);
               assertCannotLoadClass(clB, CLASS_C);
               assertCannotLoadClass(clC, CLASS_A);
               classB1 = assertLoadClass(CLASS_B, clC, clB);
               assertSame(classB, classB1);
               assertLoadClass(CLASS_C, clC);
            }
            finally
            {
               unregisterClassLoader(clC);
            }
            classA1 = assertLoadClass(CLASS_A, clA);
            assertSame(classA, classA1);
            assertCannotLoadClass(clA, CLASS_B);
            assertCannotLoadClass(clA, CLASS_C);
            classA1 = assertLoadClass(CLASS_A, clB, clA);
            assertSame(classA, classA1);
            Class<?> classB1 = assertLoadClass(CLASS_B, clB);
            assertSame(classB, classB1);
            assertCannotLoadClass(clB, CLASS_C);
         }
         finally
         {
            unregisterClassLoader(clB);
         }
         Class<?> classA1 = assertLoadClass(CLASS_A, clA);
         assertSame(classA, classA1);
         assertCannotLoadClass(clA, CLASS_B);
         assertCannotLoadClass(clA, CLASS_C);
      }
      finally
      {
         unregisterClassLoader(clA);
      }
   }
}
