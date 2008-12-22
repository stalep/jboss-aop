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

import org.jboss.test.aop.classpool.jbosscl.support.BundleInfoBuilder;

/**
 * Reproduces ClassLoaderWithUsesPackageSanityTestCase 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassPoolWithUsesPackageTestCase extends JBossClClassPoolTest
{

   public ClassPoolWithUsesPackageTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ClassPoolWithUsesPackageTestCase.class);
   }

   public void testUsesImport() throws Exception
   {
      ClassPool clA1 = null;
      
      try
      {
         BundleInfoBuilder builderA1 = BundleInfoBuilder.getBuilder().
            createModule("a1").
            createPackage(PACKAGE_A);
         clA1 = createClassPool("A1", builderA1, JAR_A_1);
         CtClass classA = assertLoadCtClass(CLASS_A, clA1);

         ClassPool clA2 = null;
         try
         {
            BundleInfoBuilder builderA2 = BundleInfoBuilder.getBuilder().
            createModule("a2").
            createUsesPackage(PACKAGE_A);

            clA2 = createClassPool("A2", builderA2, JAR_A_1);
            CtClass classA1 = assertLoadCtClass(CLASS_A, clA1);
            assertSame(classA, classA1);
            classA1 = assertLoadCtClass(CLASS_A, clA2, clA1);
            assertSame(classA, classA1);
         }
         finally
         {
            unregisterClassPool(clA2);
         }
         CtClass classA1 = assertLoadCtClass(CLASS_A, clA1);
         assertSame(classA, classA1);
      }
      finally
      {
         unregisterClassPool(clA1);
      }
   }
   
   public void testUsesNoImport() throws Exception
   {
      ClassPool clA1 = null;
      
      try
      {
         BundleInfoBuilder builderA1 = BundleInfoBuilder.getBuilder().
            createModule("a1").
            createUsesPackage(PACKAGE_A);
         clA1 = createClassPool("A1", builderA1, JAR_A_1);
         assertLoadCtClass(CLASS_A, clA1);
      }
      finally
      {
         unregisterClassPool(clA1);
      }
   }

}
