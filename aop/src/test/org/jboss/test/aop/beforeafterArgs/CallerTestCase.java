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
 * Tests the use of @Caller parameters.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class CallerTestCase extends AOPTestWithSetup
{
   private TargetCallerPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("CallerTestCase");
      suite.addTestSuite(CallerTestCase.class);
      return suite;
   }
   
   public CallerTestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      CallerAspect.clear();
      this.pojo = new TargetCallerPOJO();
   }
   
   public void test1() throws Exception
   {
      pojo.method3();
      assertAllAdvices(pojo);
   }
   
   public void test2() throws Exception
   {
      pojo.method4();
      assertAllAdvices(pojo);
   }
   
   public void test3() throws Exception
   {
      pojo.method5();
      assertAllAdvices(pojo);
   }
   
   public void test4() throws Exception
   {
      TargetCallerPOJO.method6();
      assertAllAdvices(null);
   }
   
   public void test5() throws Exception
   {
      TargetCallerPOJO.method7();
      assertAllAdvices(null);
   }
   
   public void test6() throws Exception
   {
      TargetCallerPOJO.method8();
      assertAllAdvices(null);
   }
   
   public void assertAllAdvices(Object caller)
   {
      assertTrue(CallerAspect.before1);
      assertTrue(CallerAspect.before2);
      assertTrue(CallerAspect.before3);
      assertTrue(CallerAspect.around1);
      assertTrue(CallerAspect.around2);
      assertTrue(CallerAspect.around4);
      assertTrue(CallerAspect.after1);
      assertTrue(CallerAspect.after2);
      assertSame(caller, CallerAspect.before2Caller);
      assertSame(CallerAspect.before2Caller, CallerAspect.before3Caller);
      assertSame(CallerAspect.before3Caller, CallerAspect.around2Caller);
      assertSame(CallerAspect.around2Caller, CallerAspect.around4Caller);
      assertSame(CallerAspect.around4Caller, CallerAspect.after2Caller);
   }
}