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

import org.jboss.classloader.spi.ClassLoaderDomain;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import junit.framework.Test;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassPoolWithRepositoryTestCase extends JBossClClassPoolTest
{
   final static String STRING = String.class.getName();
   
   public ClassPoolWithRepositoryTestCase(String name)
   {
      super(name);
   }

   
   public static Test suite()
   {
      return suite(ClassPoolWithRepositoryTestCase.class, new Class[0]);
   }
   
   public void testGlobalScope() throws Exception
   {
      ClassPool poolA = null;
      ClassPool poolB = null;
      try
      {
         poolA = createClassPool("A", true, JAR_A_1);
         poolB = createClassPool("B", true, JAR_B_1);
         try
         {
            CtClass aFromA = poolA.get(CLASS_A);
            assertNotNull(aFromA);
            CtClass bFromA = poolA.get(CLASS_B);
            assertNotNull(bFromA);
            CtClass aFromB = poolB.get(CLASS_A);
            CtClass bFromB = poolB.get(CLASS_B);
            assertSame(aFromA, aFromB);
            assertSame(poolA, aFromA.getClassPool());
            assertSame(poolB, bFromB.getClassPool());

            checkCanLoadString(poolA, poolB);
         }
         finally
         {
            unregisterClassPool(poolB);
         }
         CtClass aFromA = poolA.get(CLASS_A);
         assertNotNull(aFromA);

         assertCannotLoadCtClass(poolA, CLASS_B);
      }
      finally
      {
         unregisterClassPool(poolA);
      }
   }
   
   public void testChildDomain() throws Exception
   {
      ClassPool poolA = null;
      ClassPool poolB = null;
      ClassLoaderDomain childDomain = null;
      ClassPool poolC = null;
      try
      {
         poolA = createClassPool("A", true, JAR_A_1);
         poolB = createClassPool("B", true, JAR_B_1);
         poolC = createChildDomainParentFirstClassPool("C", "CHILD", true, JAR_C_1);
         
         childDomain = getChildDomainForPool(poolC);
         assertNotNull(childDomain);
         assertSame(getSystem().getDefaultDomain(), childDomain.getParent());

         CtClass aFromA = poolA.get(CLASS_A);
         assertNotNull(aFromA);
         CtClass bFromB = poolB.get(CLASS_B);
         assertNotNull(bFromB);
         CtClass cFromC = poolC.get(CLASS_C);
         assertNotNull(cFromC);
         CtClass aFromC = poolC.get(CLASS_A);
         assertNotNull(aFromC);
         CtClass bFromC = poolC.get(CLASS_B);
         assertNotNull(bFromC);
         
         assertSame(aFromA, aFromC);
         assertSame(bFromB, bFromC);
         assertSame(poolA, aFromA.getClassPool());
         assertSame(poolB, bFromB.getClassPool());
         assertSame(poolC, cFromC.getClassPool());
         
         assertCannotLoadCtClass(poolA, CLASS_C);
         assertCannotLoadCtClass(poolB, CLASS_C);

         checkCanLoadString(poolA, poolB);
         checkCanLoadString(poolB, poolC);
      }
      finally
      {
         unregisterClassPool(poolA);
         unregisterClassPool(poolB);
         unregisterClassPool(poolC);
         unregisterDomain(childDomain.getName());
      }
   }
   
   public void testSiblingDomains() throws Exception
   {
      ClassPool poolA = null;
      ClassPool poolB = null;
      ClassLoaderDomain domainA = null;
      ClassLoaderDomain domainB = null;
      try
      {
         poolA = createChildDomainParentFirstClassPool("A", "ChildA", true, JAR_A_1);
         poolB = createChildDomainParentLastClassPool("B", "ChildB", true, JAR_B_1);

         domainA = getChildDomainForPool(poolA);
         assertNotNull(domainA);
         assertSame(getSystem().getDefaultDomain(), domainA.getParent());
         domainB = getChildDomainForPool(poolB);
         assertNotNull(domainB);
         assertSame(getSystem().getDefaultDomain(), domainB.getParent());
         assertNotSame(domainA, domainB);

         CtClass clazzA = poolA.get(CLASS_A);
         assertSame(poolA, clazzA.getClassPool());
         CtClass clazzB = poolB.get(CLASS_B);
         assertSame(poolB, clazzB.getClassPool());
         assertCannotLoadCtClass(poolA, CLASS_B);
         assertCannotLoadCtClass(poolB, CLASS_A);

         checkCanLoadString(poolA, poolB);
      }
      finally
      {
         unregisterClassPool(poolA);
         unregisterClassPool(poolB);
         unregisterDomain(poolA);
         unregisterDomain(poolB);
      }
   }
   
   public void testChildWithNewClassesInParent() throws Exception
   {
      ClassPool globalPool = null;
      ClassPool scopedPool = null;
      try
      {
         scopedPool = createChildDomainParentFirstClassPool("SCOPED", "SCOPED", true, JAR_B_1);
         assertCannotLoadCtClass(scopedPool, CLASS_A);
         
         globalPool = createClassPool("GLOBAL", true, JAR_A_1);

         CtClass aFromChild = scopedPool.get(CLASS_A);
         assertNotNull(aFromChild);
         CtClass aFromParent = globalPool.get(CLASS_A);
         assertNotNull(aFromParent);
         assertSame(aFromChild, aFromParent);
         assertSame(globalPool, aFromParent.getClassPool());
      }
      finally
      {
         unregisterClassPool(globalPool);
         unregisterClassPool(scopedPool);
         unregisterDomain(scopedPool);
      }
   }
   
   public void testChildOverrideWithParentDelegation() throws Exception
   {
      ClassPool globalPool = null;
      ClassPool scopedPool = null;
      try
      {
         globalPool = createClassPool("GLOBAL", true, JAR_A_1);
         scopedPool = createChildDomainParentFirstClassPool("SCOPED", "SCOPED", true, JAR_B_1);
         CtClass aFromParent = globalPool.get(CLASS_A);
         assertNotNull(aFromParent);
         CtClass aFromChild = scopedPool.get(CLASS_A);
         assertNotNull(aFromChild);
         assertSame(aFromParent, aFromChild);
         assertSame(globalPool, aFromParent.getClassPool());
      }
      finally
      {
         unregisterClassPool(globalPool);
         unregisterClassPool(scopedPool);
         unregisterDomain(scopedPool);
      }
   }
   
   public void testURLChildOfGlobalUcl() throws Exception
   {
      ClassPool global = null;
      ClassPool childA = null;
      ClassPool childB = null;
      try
      {
         global = createClassPool("GLOBAL", true, JAR_A_1);
         childA = createChildURLClassPool(global, JAR_B_1);
         
         CtClass aFromA = childA.get(CLASS_A);
         assertSame(global, aFromA.getClassPool());
         CtClass bFromA = childA.get(CLASS_B);
         assertSame(childA, bFromA.getClassPool());
         
         childB = createChildURLClassPool(global, JAR_A_2);
         CtClass aFromB = childB.get(CLASS_A);
         assertSame(global, aFromB.getClassPool());
      }
      finally
      {
         unregisterClassPool(global);
         unregisterClassPool(childA);
         unregisterClassPool(childB);
      }
   }
   
   public void testChildOverrideWithNoParentDelegation() throws Exception
   {
      ClassPool globalPool = null;
      ClassPool scopedPool = null;
      try
      {
         globalPool = createClassPool("GLOBAL", true, JAR_A_1);
         scopedPool = createChildDomainParentLastClassPool("CHILD", "CHILD", true, JAR_A_1);
         CtClass aFromParent = globalPool.get(CLASS_A);
         assertNotNull(aFromParent);
         CtClass aFromChild = scopedPool.get(CLASS_A);
         assertNotNull(aFromChild);
         assertNotSame(aFromParent, aFromChild);
         assertSame(globalPool, aFromParent.getClassPool());
         assertSame(scopedPool, aFromChild.getClassPool());
      }
      finally
      {
         unregisterClassPool(globalPool);
         unregisterClassPool(scopedPool);
         unregisterDomain(scopedPool);
      }
   }


   public void testUndeploySibling() throws Exception
   {
      ClassPool poolA = null;
      ClassPool poolB = null;
      try
      {
         try
         {
            poolA = createClassPool("A", true, JAR_A_1);
            assertCannotLoadCtClass(poolA, CLASS_B);
            
            poolB = createClassPool("B", true, JAR_B_1);
            CtClass bFromA = poolA.get(CLASS_B);
            assertSame(poolB, bFromA.getClassPool());
         }
         finally
         {
            unregisterClassPool(poolB);
         }
         assertCannotLoadCtClass(poolA, CLASS_B);
      }
      finally
      {
         unregisterClassPool(poolA);
      }
   }

   
   public void testUndeployParentDomainClassLoader() throws Exception
   {
      ClassPool globalA = null;
      ClassPool globalB = null;
      ClassPool child = null;
      try
      {
         try
         {
            globalA = createClassPool("A", true, JAR_A_1);
            assertCannotLoadCtClass(globalA, CLASS_B);
            
            child = createChildDomainParentLastClassPool("C", "C", true, JAR_C_1);
            assertCannotLoadCtClass(child, CLASS_B);
            
            globalB = createClassPool("B", true, JAR_B_1);
            CtClass bFromChild = child.get(CLASS_B);
            CtClass bFromA = globalA.get(CLASS_B);
            assertSame(globalB, bFromA.getClassPool());
            assertSame(bFromA, bFromChild);
         }
         finally
         {
            unregisterClassPool(globalB);
         }
         assertCannotLoadCtClass(child, CLASS_B);
      }
      finally
      {
         unregisterClassPool(globalA);
         unregisterClassPool(child);
         unregisterDomain(child);
      }
   }
   
   
   public void testSeveralLevelsOfDomain() throws Exception
   {
      ClassPool parent = null;
      ClassPool cl1B = null;
      ClassPool cl1C = null;
      ClassPool cl2B = null;
      ClassPool cl2C = null;
      ClassPool cl11A = null;
      ClassPool cl11B = null;
      ClassPool cl11C = null;
      ClassPool cl12A = null;
      ClassPool cl12B = null;
      ClassPool cl12C = null;
      
      try
      {
         parent = createClassPool("A", true, JAR_A_1);
         CtClass aFromParent = parent.get(CLASS_A);
         assertSame(parent, aFromParent.getClassPool());
         
         final String domain1 = "1";
         cl1B = createChildDomainParentFirstClassPool("1B", domain1, true, JAR_B_1);
         cl1C =  createChildDomainParentFirstClassPool("1C", domain1, true, JAR_C_1);
         CtClass aFrom1B = cl1B.get(CLASS_A);
         CtClass bFrom1B = cl1B.get(CLASS_B);
         CtClass cFrom1B = cl1B.get(CLASS_C);
         CtClass aFrom1C = cl1C.get(CLASS_A);
         CtClass bFrom1C = cl1C.get(CLASS_B);
         CtClass cFrom1C = cl1C.get(CLASS_C);
         assertSame(aFromParent, aFrom1B);
         assertSame(aFromParent, aFrom1C);
         assertSame(bFrom1B, bFrom1C);
         assertSame(cFrom1B, cFrom1C);
         assertSame(cl1B, bFrom1B.getClassPool());
         assertSame(cl1C, cFrom1B.getClassPool());
         
         
         final String domain2 = "2";
         cl2B = createChildDomainParentFirstClassPool("2B", domain2, true, JAR_B_1);
         cl2C = createChildDomainParentFirstClassPool("2C", domain2, true, JAR_C_1);
         CtClass aFrom2B = cl2B.get(CLASS_A);
         CtClass bFrom2B = cl2B.get(CLASS_B);
         CtClass cFrom2B = cl2B.get(CLASS_C);
         CtClass aFrom2C = cl2C.get(CLASS_A);
         CtClass bFrom2C = cl2C.get(CLASS_B);
         CtClass cFrom2C = cl2C.get(CLASS_C);
         assertSame(aFromParent, aFrom2B);
         assertSame(aFromParent, aFrom2C);
         assertSame(bFrom2B, bFrom2C);
         assertSame(cFrom2B, cFrom2C);
         assertSame(cl2B, bFrom2B.getClassPool());
         assertSame(cl2C, cFrom2B.getClassPool());
         assertNotSame(bFrom1B, bFrom2B);
         assertNotSame(bFrom2C, bFrom1C);
         assertNotSame(cFrom2C, cFrom1C);
         
         final String domain11 = "11";
         cl11A = createChildDomainParentFirstClassPool("11A", domain11, domain1, true, JAR_A_2);
         cl11B = createChildDomainParentFirstClassPool("11B", domain11, domain1, true, JAR_B_2);
         cl11C = createChildDomainParentFirstClassPool("11C", domain11, domain1, true, JAR_C_2);
         final String domain12 = "12";
         cl12A = createChildDomainParentLastClassPool("12A", domain12, domain1, true, JAR_A_2);
         cl12B = createChildDomainParentLastClassPool("12B", domain12, domain1, true, JAR_B_2);
         cl12C = createChildDomainParentLastClassPool("12C", domain12, domain1, true, JAR_C_2);
         CtClass aFrom11A = cl11A.get(CLASS_A);
         CtClass aFrom11B = cl11B.get(CLASS_A);
         CtClass aFrom11C = cl11C.get(CLASS_A);
         assertSame(aFromParent, aFrom11A);
         assertSame(aFromParent, aFrom11B);
         assertSame(aFromParent, aFrom11C);
         CtClass aFrom12A = cl12A.get(CLASS_A);
         CtClass aFrom12B = cl12B.get(CLASS_A);
         CtClass aFrom12C = cl12C.get(CLASS_A);
         assertNotSame(aFromParent, aFrom12A);
         assertSame(aFrom12A, aFrom12B);
         assertSame(aFrom12A, aFrom12C);
         assertSame(cl12A, aFrom12A.getClassPool());
         
         CtClass bFrom11A = cl11A.get(CLASS_B);
         CtClass bFrom11B = cl11B.get(CLASS_B);
         CtClass bFrom11C = cl11C.get(CLASS_B);
         CtClass cFrom11A = cl11A.get(CLASS_C);
         CtClass cFrom11B = cl11B.get(CLASS_C);
         CtClass cFrom11C = cl11C.get(CLASS_C);
         assertSame(bFrom11A, bFrom11B);
         assertSame(bFrom11A, bFrom11C);
         assertSame(cl1B, bFrom11B.getClassPool());
         assertSame(cFrom11A, cFrom11B);
         assertSame(cFrom11A, cFrom11C);
         assertSame(cl1C, cFrom11C.getClassPool());
         
         CtClass bFrom12A = cl12A.get(CLASS_B);
         CtClass bFrom12B = cl12B.get(CLASS_B);
         CtClass bFrom12C = cl12C.get(CLASS_B);
         CtClass cFrom12A = cl12A.get(CLASS_C);
         CtClass cFrom12B = cl12B.get(CLASS_C);
         CtClass cFrom12C = cl12C.get(CLASS_C);
         assertSame(bFrom12A, bFrom12B);
         assertSame(bFrom12A, bFrom12C);
         assertSame(cl12B, bFrom12B.getClassPool());
         assertSame(cFrom12A, cFrom12B);
         assertSame(cFrom12A, cFrom12C);
         assertSame(cl12C, cFrom12C.getClassPool());
         assertNotSame(bFrom11B, bFrom12B);
         assertNotSame(cFrom11C, cFrom12C);
      }
      finally
      {
         unregisterClassPool(parent);
         unregisterClassPool(cl1B);
         unregisterClassPool(cl1C);
         unregisterClassPool(cl2B);
         unregisterClassPool(cl2C);
         unregisterClassPool(cl11A);
         unregisterClassPool(cl11B);
         unregisterClassPool(cl11C);
         unregisterClassPool(cl12A);
         unregisterClassPool(cl12B);
         unregisterClassPool(cl12C);
         
         unregisterDomain(cl12A);
         unregisterDomain(cl11A);
         unregisterDomain(cl2B);
         unregisterDomain(cl1B);
      }
   }

   
   public void testUclLoaderOrdering() throws Exception
   {
      ClassPool globalA = null;
      ClassPool globalB = null;
      ClassPool globalC = null;
      try
      {
         globalA = createClassPool("A", true, JAR_A_1);
         globalB = createClassPool("B", true, JAR_A_1);
         globalC = createClassPool("C", true, JAR_A_1);
         
         CtClass aFromA = globalA.get(CLASS_A);
         CtClass aFromB = globalB.get(CLASS_A);
         CtClass aFromC = globalC.get(CLASS_A);
         assertSame(aFromA, aFromB);
         assertSame(aFromA, aFromC);
         assertSame(globalA, aFromA.getClassPool());
      }
      finally
      {
         unregisterClassPool(globalA);
         unregisterClassPool(globalB);
         unregisterClassPool(globalC);
      }
   }

   public void testSimpleGeneratingClass() throws Exception
   {
      ClassPool global = null;
      try
      {
         global = createClassPool("A", true, JAR_A_1);
         final String NEW_CLASS = CLASS_A + "XYZ";
         CtClass newCtClass = global.makeClass(NEW_CLASS);
         Class<?> newClass = newCtClass.toClass();
         Class<?> foundClass = global.getClassLoader().loadClass(NEW_CLASS);
         assertSame(newClass, foundClass);
      }
      finally
      {
         unregisterClassPool(global);
      }
   }
   
   public void testSimpleGeneratingClassAndFindInSameDomain() throws Exception
   {
      ClassPool globalA = null;
      ClassPool globalB = null;
      try
      {
         //Created class must be in one of the packages the loader handles
         //http://www.jboss.com/index.html?module=bb&op=viewtopic&t=147105
         final String NEW_CLASS = PACKAGE_A + ".NewClass";

         globalA = createClassPool("A", true, JAR_A_1);
         globalB = createClassPool("B", true, JAR_B_1);
         CtClass newCtClass = globalA.makeClass(NEW_CLASS);
         Class<?> newClass = newCtClass.toClass();
         Class<?> foundClassA = globalA.getClassLoader().loadClass(NEW_CLASS);
         Class<?> foundClassB = globalB.getClassLoader().loadClass(NEW_CLASS);
         assertSame(newClass, foundClassA);
         assertSame(newClass, foundClassB);
      }
      finally
      {
         unregisterClassPool(globalA);
         unregisterClassPool(globalB);
      }
   }
   
   public void testGeneratingCrossDomainClassHierarchy() throws Exception
   {
      ClassPool global = null;
      ClassPool child1 = null;
      ClassPool child2 = null;
      try
      {
         //Created class must be in one of the packages the loader handles
         //http://www.jboss.com/index.html?module=bb&op=viewtopic&t=147105
         final String PARENT = PACKAGE_A + ".Parent";
         final String CHILD = PACKAGE_C + ".Child";
         
         global = createClassPool("A", true, JAR_A_1);
         child1 = createChildDomainParentLastClassPool("C1", "C1", true, JAR_B_1);
         child2 = createChildDomainParentLastClassPool("C2", "C2", "C1", true, JAR_C_1);
         
         assertCannotLoadCtClass(global, PARENT);
         assertCannotLoadCtClass(child1, PARENT);
         assertCannotLoadCtClass(child2, PARENT);
         assertCannotLoadCtClass(child2, CHILD);
         
         CtClass parentClass = global.makeClass(PARENT);
         
         CtClass childClass = child2.makeClass(CHILD);
         childClass.setSuperclass(parentClass);
         
         CtClass parentFromGlobal = global.get(PARENT);
         assertSame(global, parentFromGlobal.getClassPool());
         assertSame(parentClass, parentFromGlobal);
         
         CtClass childFromChild2 = child2.get(CHILD);
         assertSame(child2, childFromChild2.getClassPool());
         assertSame(childClass, childFromChild2);
         
         assertCannotLoadCtClass(global, CHILD);
         assertCannotLoadCtClass(child1, CHILD);
         
         CtClass parentFromChildA = childClass.getSuperclass();
         assertSame(parentClass, parentFromChildA);
       
         CtClass parentFromChildB = child2.get(PARENT);
         assertSame(parentClass, parentFromChildB);
         
         Class<?> parentClazz = parentClass.toClass();
         assertSame(global.getClassLoader(), parentClazz.getClassLoader());
         
         Class<?> childClazz = childClass.toClass();
         assertSame(child2.getClassLoader(), childClazz.getClassLoader());
         
         Class<?> parentClazzFromParent = global.getClassLoader().loadClass(PARENT);
         assertSame(parentClazz, parentClazzFromParent);
         
         Class<?> parentClazzFromChild = child2.getClassLoader().loadClass(PARENT);
         assertSame(parentClazz, parentClazzFromChild);
         
         Class<?> childClazzFromChild = child2.getClassLoader().loadClass(CHILD);
         assertSame(childClazz, childClazzFromChild);
      }
      finally
      {
         unregisterClassPool(global);
         unregisterClassPool(child1);
         unregisterClassPool(child2);
         unregisterDomain(child1);
         unregisterDomain(child2);
      }
   }
   
   
   public void testGeneratingClassInDelegatingPool() throws Exception
   {
      ClassPool globalA = null;
      ClassPool globalB = null;
      ClassPool child = null;
      try
      {
         //Created class must be in one of the packages the loader handles
         //http://www.jboss.com/index.html?module=bb&op=viewtopic&t=147105
         final String A_CLASS = PACKAGE_A + ".AClazz";
         final String B_CLASS = PACKAGE_B + ".BClazz";

         globalA = createClassPool("A", true, JAR_A_1);
         globalB = createClassPool("B", true, JAR_B_1);
         child = createChildURLClassPool(globalA, JAR_C_1);
         
         
         assertCannotLoadCtClass(globalA, A_CLASS);
         assertCannotLoadCtClass(globalB, A_CLASS);
         assertCannotLoadCtClass(child, A_CLASS);
         assertCannotLoadCtClass(globalA, B_CLASS);
         assertCannotLoadCtClass(globalB, B_CLASS);
         assertCannotLoadCtClass(child, B_CLASS);
         
         CtClass a = globalA.makeClass(A_CLASS);
         CtClass b = globalB.makeClass(B_CLASS);
         
         CtClass aFromA = globalA.get(A_CLASS);
         assertSame(a, aFromA);
         assertSame(globalA, aFromA.getClassPool());
         CtClass aFromB = globalB.get(A_CLASS);
         assertSame(a, aFromB);
         CtClass bFromA = globalA.get(B_CLASS);
         assertSame(b, bFromA);
         assertSame(globalB, bFromA.getClassPool());
         CtClass bFromB = globalB.get(B_CLASS);
         assertSame(b, bFromB);
         CtClass aFromChild = child.get(A_CLASS);
         assertSame(a, aFromChild);
         CtClass bFromChild = child.get(B_CLASS);
         assertSame(b, bFromChild);
         
         Class<?> clazzA = a.toClass();
         assertSame(globalA.getClassLoader(), clazzA.getClassLoader());
         
         Class<?> clazzB = b.toClass();
         assertSame(globalB.getClassLoader(), clazzB.getClassLoader());
         
         Class<?> clazzAFromA = globalA.getClassLoader().loadClass(A_CLASS);
         assertSame(clazzA, clazzAFromA);
         Class<?> clazzAFromB = globalB.getClassLoader().loadClass(A_CLASS);
         assertSame(clazzA, clazzAFromB);
         Class<?> clazzAFromChild = child.getClassLoader().loadClass(A_CLASS);
         assertSame(clazzA, clazzAFromChild);
         
         Class<?> clazzBFromA = globalA.getClassLoader().loadClass(B_CLASS);
         assertSame(clazzB, clazzBFromA);
         Class<?> clazzBFromB = globalB.getClassLoader().loadClass(B_CLASS);
         assertSame(clazzB, clazzBFromB);
         Class<?> clazzBFromChild = child.getClassLoader().loadClass(B_CLASS);
         assertSame(clazzB, clazzBFromChild);
      }
      finally
      {
         unregisterClassPool(globalA);
         unregisterClassPool(globalB);
         unregisterClassPool(child);
      }
   }
   

/* 
   The folllowing two tests are probably not very realistic http://www.jboss.com/index.html?module=bb&op=viewtopic&p=4195022#4195022
   public void testClassLoaderlWithParentClassLoader() throws Exception
   {
      ClassPool parent = createChildURLClassPool(null, JAR_B_1);
      ClassPool global = null;
      try
      {
         global = createChildDomainParentFirstClassPool("A", "A", true, parent, JAR_A_1);
         CtClass aFromGlobal = global.get(CLASS_A);
         assertSame(global, aFromGlobal.getClassPool());
         CtClass bFromGlobal = global.get(CLASS_B);
         assertSame(parent, bFromGlobal.getClassPool());
         CtClass bFromParent = parent.get(CLASS_B);
         assertSame(bFromGlobal, bFromParent);
      }
      finally
      {
         unregisterClassPool(global);
         unregisterClassPool(parent);
         unregisterDomain(global);
      }
   }

   If we ever need to implement this, this test needs to have the latest additions to ClassLoaderWithRepositorySanityTestCase
   public void testClassLoaderWithParentClassLoaderAndSameClassInDomain() throws Exception
   {
      ClassPool parent = createChildURLClassPool(null, JAR_B_1);
      ClassPool globalA = null;
      ClassPool globalB = null;
      try
      {
         final String domain = "CHILD";
         globalA = createChildDomainParentFirstClassPool("A", domain, true, parent, JAR_A_1);
         CtClass aFromGlobal = globalA.get(CLASS_A);
         assertSame(globalA, aFromGlobal.getClassPool());

         globalB = createChildDomainParentFirstClassPool("B", domain, true, parent, JAR_B_1);
         CtClass bFromGlobalA = globalA.get(CLASS_B);
         assertSame(globalB, bFromGlobalA.getClassPool());
         CtClass bFromParent = parent.get(CLASS_B);
         assertSame(parent, bFromParent.getClassPool());
         assertNotSame(bFromGlobalA, bFromParent);
      }
      finally
      {
         unregisterClassPool(globalA);
         unregisterClassPool(globalB);
         unregisterClassPool(parent);
         unregisterDomain(globalA);
      }
   }
*/
   private void checkCanLoadString(ClassPool poolA, ClassPool poolB) throws NotFoundException
   {
      CtClass strA = poolA.getCtClass(STRING);
      CtClass strB = poolB.getCtClass(STRING);
      assertSame(strA, strB);
      assertSame(ClassPool.getDefault(), strA.getClassPool());
   }
}
