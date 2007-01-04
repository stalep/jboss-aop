/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.aop.beforeafterArgs;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests the use of @Target parameters.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class TargetTestCase extends AOPTestWithSetup
{
   private TargetPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("TargetTestCase");
      suite.addTestSuite(TargetTestCase.class);
      return suite;
   }
   
   public TargetTestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      TargetAspect.clear();
      this.pojo = new TargetPOJO();
   }

   public void test1()
   {
      new TargetPOJO(1);
      assertStaticAdvices();
   }

   
   public void test2()
   {
      pojo.field1 = 0;
      assertAllAdvices();
      assertTarget(pojo);
   }
   
   public void test3()
   {
      int test = pojo.field1;
      assertAllAdvices();
      assertTarget(pojo);
   }
   
   public void test4()
   {
      TargetPOJO.field2 = 5;
      assertAllAdvices();
      assertTarget(null);
   }
   
   public void test5()
   {
      int test = TargetPOJO.field2;
      assertAllAdvices();
      assertTarget(null);
   }
   
   public void test6()
   {
      pojo.method1();
      assertAllAdvices();
      assertTarget(pojo);
   }
   
   public void test7()
   {
      TargetPOJO.method2();
      assertAllAdvices();
      assertTarget(null);
   }
   
   public void test8()
   {
      pojo.method3();
      assertStaticAdvices();
   }
   
   public void test9()
   {
      pojo.method4();
      assertAllAdvices();
      assertTarget();
   }
   
   public void test10()
   {
      pojo.method5();
      assertAllAdvices();
      assertTarget(null);
   }
   
   public void test11()
   {
      TargetPOJO.method6();
      assertStaticAdvices();
   }
   
   public void test12()
   {
      TargetPOJO.method7();
      assertAllAdvices();
      assertTarget();
   }
   
   public void test13()
   {
      TargetPOJO.method8();
      assertAllAdvices();
      assertTarget(null);
   }
   
   private void assertAllAdvices()
   {
      assertTrue(TargetAspect.before1);
      assertTrue(TargetAspect.before2);
      assertTrue(TargetAspect.around1);
      assertTrue(TargetAspect.around2);
      assertTrue(TargetAspect.after1);
      assertTrue(TargetAspect.after2);
   }
   
   private void assertTarget()
   {
      assertNotNull(TargetAspect.before2Target);
      assertSame(TargetAspect.before2Target, TargetAspect.around2Target);
      assertSame(TargetAspect.around2Target, TargetAspect.after2Target);
   }
   
   private void assertTarget(Object target)
   {
      assertSame(target, TargetAspect.before2Target);
      assertSame(TargetAspect.before2Target, TargetAspect.around2Target);
      assertSame(TargetAspect.around2Target, TargetAspect.after2Target);
   }
   
   private void assertStaticAdvices()
   {
      assertTrue(TargetAspect.before1);
      assertFalse(TargetAspect.before2);
      assertTrue(TargetAspect.around1);
      assertFalse(TargetAspect.around2);
      assertTrue(TargetAspect.after1);
      assertFalse(TargetAspect.after2);
      assertNull(TargetAspect.before2Target);
      assertNull(TargetAspect.around2Target);
      assertNull(TargetAspect.after2Target);
   }
}