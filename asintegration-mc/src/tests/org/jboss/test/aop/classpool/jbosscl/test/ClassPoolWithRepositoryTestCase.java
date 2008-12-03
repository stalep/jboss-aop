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
import junit.framework.Test;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassPoolWithRepositoryTestCase extends JBossClClassPoolTest
{

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
      }
      finally
      {
         unregisterClassPool(poolA);
         unregisterClassPool(poolB);
         unregisterClassPool(poolC);
         unregisterDomain(childDomain.getName());
      }
   }
   

 
}
