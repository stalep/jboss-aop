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

import javassist.ClassPool;
import javassist.CtClass;
import junit.framework.Test;

import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.aop.classpool.jbosscl.support.BundleInfoBuilder;

/**
 * Reproduces the behavior found in ClassLoaderWithModuleDependencySanityTestCase
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassPoolWithModuleDependencyTestCase extends JBossClClassPoolTest
{
   final static String STRING = String.class.getName();
   
   public ClassPoolWithModuleDependencyTestCase(String name)
   {
      super(name);
   }

   
   public static Test suite()
   {
      return suite(ClassPoolWithModuleDependencyTestCase.class, new Class[0]);
   }
   
   public void testImportNoVersionCheck() throws Exception
   {
      ClassPool clA = null;
      try
      {
         BundleInfoBuilder builderA = BundleInfoBuilder.getBuilder().
            createModule("ModuleA").
            createPackage(PACKAGE_A);
         clA = createClassPool("A", builderA, JAR_A_1);
         
         CtClass aFromA = assertLoadCtClass(CLASS_A, clA);
         assertCannotLoadCtClass(clA, CLASS_B);
         
         ClassPool clB = null;
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createModule("ModuleB").
               createRequireModule("ModuleA");
            clB = createClassPool("B", builderB, JAR_B_1);

            CtClass aFromA1 = assertLoadCtClass(CLASS_A, clA, clA);
            assertSame(aFromA, aFromA1);
            assertCannotLoadCtClass(clA, CLASS_B);
            assertLoadCtClass(CLASS_B, clB, clB);
            CtClass aFromB = assertLoadCtClass(CLASS_A, clB, clA);
            assertSame(aFromA, aFromB);
         }
         finally
         {
            unregisterClassPool(clB);
         }
         CtClass aFromA1 = assertLoadCtClass(CLASS_A, clA);
         assertSame(aFromA, aFromA1);
         assertCannotLoadCtClass(clA, CLASS_B);
         //TODO assertNoClassLoader(b);
      }
      finally
      {
         unregisterClassPool(clA);
      }
   }
   
   public void testImportVersionCheck() throws Exception
   {
      ClassPool clA = null;
      try
      {
         BundleInfoBuilder builderA = BundleInfoBuilder.getBuilder().
            createModule("ModuleA", "1.0.0").
            createPackage(PACKAGE_A);
         clA = createClassPool("A", builderA, JAR_A_1);
         CtClass classA = assertLoadCtClass(CLASS_A, clA);
         assertCannotLoadCtClass(clA, CLASS_B);

         ClassPool clB = null;
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createRequireModule("ModuleA", new VersionRange("1.0.0", "2.0.0")).
               createPackage(PACKAGE_B);
            clB = createClassPool("B", builderB, JAR_B_1);
            CtClass classA1 = assertLoadCtClass(CLASS_A, clA);
            assertSame(classA, classA1);
            assertCannotLoadCtClass(clA, CLASS_B);
            assertLoadCtClass(CLASS_B, clB);
            classA1 = assertLoadCtClass(CLASS_A, clB, clA);
            assertSame(classA, classA1);
         }
         finally
         {
            unregisterClassPool(clB);
         }
         CtClass classA1 = assertLoadCtClass(CLASS_A, clA);
         assertSame(classA, classA1);
         assertCannotLoadCtClass(clA, CLASS_B);
      }
      finally
      {
         unregisterClassPool(clA);
      }
   }   

   public void testImportVersionCheckFailed() throws Exception
   {
      ClassPool clA = null;
      try
      {
         BundleInfoBuilder builderA = BundleInfoBuilder.getBuilder().
            createModule("ModuleA", "3.0.0").
            createPackage(PACKAGE_A);
         clA = createClassPool("A", builderA, JAR_A_1);
         CtClass classA = assertLoadCtClass(CLASS_A, clA);
         assertCannotLoadCtClass(clA, CLASS_B);
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createRequireModule("ModuleA", new VersionRange("1.0.0", "2.0.0")).
               createPackage(PACKAGE_B);
            try
            {
               createClassPool("B", builderB, JAR_B_1);
               fail("Should not have been able to create loader");
            }
            catch(NoSuchClassLoaderException expected)
            {
            }
            CtClass classA1 = assertLoadCtClass(CLASS_A, clA);
            assertSame(classA, classA1);
            assertCannotLoadCtClass(clA, CLASS_B);
         }
         finally
         {
            unregisterClassLoader("B");
         }
         CtClass classA1 = assertLoadCtClass(CLASS_A, clA);
         assertSame(classA, classA1);
         assertCannotLoadCtClass(clA, CLASS_B);
      }
      finally
      {
         unregisterClassPool(clA);
      }
   }
   
   //These are my extra tests
   public void testSeveralModulesWithSamePackages() throws Exception
   {
      ClassPool clAModuleX = null;
      ClassPool clAModuleA = null;
      ClassPool clAModuleY = null;

      try
      {
         BundleInfoBuilder builderAX = BundleInfoBuilder.getBuilder().
            createModule("ModuleX").
            createPackage(PACKAGE_A);
         clAModuleX = createClassPool("X", builderAX, JAR_A_1);
      
         BundleInfoBuilder builderAA = BundleInfoBuilder.getBuilder().
            createModule("ModuleA").
            createPackage(PACKAGE_A);
         clAModuleA = createClassPool("A", builderAA, JAR_A_1);
         
         BundleInfoBuilder builderAY = BundleInfoBuilder.getBuilder().
            createModule("ModuleY").
            createPackage(PACKAGE_A);
         clAModuleY = createClassPool("Y", builderAY, JAR_A_1);
      
         CtClass classAX = assertLoadCtClass(CLASS_A, clAModuleX);
         CtClass classAA = assertLoadCtClass(CLASS_A, clAModuleA);
         CtClass classAY = assertLoadCtClass(CLASS_A, clAModuleY);
         assertCannotLoadCtClass(clAModuleX, CLASS_B);
         assertCannotLoadCtClass(clAModuleA, CLASS_B);
         assertCannotLoadCtClass(clAModuleY, CLASS_B);
         
         ClassPool clB = null;
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createRequireModule("ModuleA").
               createPackage(PACKAGE_B);
            clB = createClassPool("B", builderB, JAR_B_1);
            CtClass classAX1 = assertLoadCtClass(CLASS_A, clAModuleX);
            assertSame(classAX, classAX1);
            CtClass classAA1 = assertLoadCtClass(CLASS_A, clAModuleA);
            assertSame(classAA, classAA1);
            CtClass classAY1 = assertLoadCtClass(CLASS_A, clAModuleY);
            assertSame(classAY, classAY1);
            assertCannotLoadCtClass(clAModuleX, CLASS_B);
            assertCannotLoadCtClass(clAModuleA, CLASS_B);
            assertCannotLoadCtClass(clAModuleY, CLASS_B);
            
            assertLoadCtClass(CLASS_B, clB, clB);
            CtClass aFromB = assertLoadCtClass(CLASS_A, clB, clAModuleA);
            assertSame(aFromB, classAA);
         }
         finally
         {
            unregisterClassPool(clB);
         }
         CtClass classAX1 = assertLoadCtClass(CLASS_A, clAModuleX);
         assertSame(classAX, classAX1);
         CtClass classAA1 = assertLoadCtClass(CLASS_A, clAModuleA);
         assertSame(classAA, classAA1);
         CtClass classAY1 = assertLoadCtClass(CLASS_A, clAModuleY);
         assertSame(classAY, classAY1);
         assertCannotLoadCtClass(clAModuleX, CLASS_B);
         assertCannotLoadCtClass(clAModuleA, CLASS_B);
         assertCannotLoadCtClass(clAModuleY, CLASS_B);
      }
      finally
      {
         unregisterClassPool(clAModuleY);
         unregisterClassPool(clAModuleA);
         unregisterClassPool(clAModuleX);
      }
   }
   
   public void testSeveralModulesWithSameNamesDifferentVersions() throws Exception
   {
      ClassPool clAModuleA1 = null;
      ClassPool clAModuleA2 = null;
      ClassPool clAModuleA3 = null;

      try
      {
         BundleInfoBuilder builderA1 = BundleInfoBuilder.getBuilder().
            createModule("ModuleA", "1.0.0").
            createPackage(PACKAGE_A);
         clAModuleA1 = createClassPool("X", builderA1, JAR_A_1);
      
         BundleInfoBuilder builderA2 = BundleInfoBuilder.getBuilder().
            createModule("ModuleA", "2.0.0").
            createPackage(PACKAGE_A);
         clAModuleA2 = createClassPool("A", builderA2, JAR_A_1);
         
         BundleInfoBuilder builderA3 = BundleInfoBuilder.getBuilder().
            createModule("ModuleA", "3.0.0").
            createPackage(PACKAGE_A);
         clAModuleA3 = createClassPool("Y", builderA3, JAR_A_1);
      
         CtClass classAX = assertLoadCtClass(CLASS_A, clAModuleA1);
         CtClass classAA = assertLoadCtClass(CLASS_A, clAModuleA2);
         CtClass classAY = assertLoadCtClass(CLASS_A, clAModuleA3);
         assertCannotLoadCtClass(clAModuleA1, CLASS_B);
         assertCannotLoadCtClass(clAModuleA2, CLASS_B);
         assertCannotLoadCtClass(clAModuleA3, CLASS_B);
         
         ClassPool clB = null;
         try
         {
            BundleInfoBuilder builderB = BundleInfoBuilder.getBuilder().
               createRequireModule("ModuleA", new VersionRange("2.0.0", true, "3.0.0", false)).
               createPackage(PACKAGE_B);
            clB = createClassPool("B", builderB, JAR_B_1);
            CtClass classAX1 = assertLoadCtClass(CLASS_A, clAModuleA1);
            assertSame(classAX, classAX1);
            CtClass classAA1 = assertLoadCtClass(CLASS_A, clAModuleA2);
            assertSame(classAA, classAA1);
            CtClass classAY1 = assertLoadCtClass(CLASS_A, clAModuleA3);
            assertSame(classAY, classAY1);
            assertCannotLoadCtClass(clAModuleA1, CLASS_B);
            assertCannotLoadCtClass(clAModuleA2, CLASS_B);
            assertCannotLoadCtClass(clAModuleA3, CLASS_B);
            
            assertLoadCtClass(CLASS_B, clB, clB);
            CtClass aFromB = assertLoadCtClass(CLASS_A, clB, clAModuleA2);
            assertSame(aFromB, classAA);
         }
         finally
         {
            unregisterClassPool(clB);
         }
         CtClass classAX1 = assertLoadCtClass(CLASS_A, clAModuleA1);
         assertSame(classAX, classAX1);
         CtClass classAA1 = assertLoadCtClass(CLASS_A, clAModuleA2);
         assertSame(classAA, classAA1);
         CtClass classAY1 = assertLoadCtClass(CLASS_A, clAModuleA3);
         assertSame(classAY, classAY1);
         assertCannotLoadCtClass(clAModuleA1, CLASS_B);
         assertCannotLoadCtClass(clAModuleA2, CLASS_B);
         assertCannotLoadCtClass(clAModuleA3, CLASS_B);
      }
      finally
      {
         unregisterClassPool(clAModuleA3);
         unregisterClassPool(clAModuleA2);
         unregisterClassPool(clAModuleA1);
      }
      
   }

}
