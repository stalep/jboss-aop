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
