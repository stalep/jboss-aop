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
 * Tests the use of @Args parameters.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ArgsTestCase extends AOPTestWithSetup
{
   private ArgsPOJO pojo;

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ArgsTestCase");
      suite.addTestSuite(ArgsTestCase.class);
      return suite;
   }

   public ArgsTestCase(String name)
   {
      super(name);
   }

   public void setUp() throws Exception
   {
      super.setUp();
      this.pojo = new ArgsPOJO();
      ArgsAspect.clear();
   }
   
   public void test1()
   {
      Object text = pojo.field1;
      assertTrue(ArgsAspect.before1);
      assertTrue(ArgsAspect.before2);
      assertTrue(ArgsAspect.before3);
      assertSame(ArgsAspect.before2Args, ArgsAspect.before3Args);
      assertEquals(0, ArgsAspect.before2Args.length);
      
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after3);
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.around3);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test2()
   {
      pojo.field1 = "test2";
      assertTrue(ArgsAspect.before1);
      assertTrue(ArgsAspect.before2);
      assertTrue(ArgsAspect.before3);
      assertSame(ArgsAspect.before2Args, ArgsAspect.before3Args);
      assertEquals(1, ArgsAspect.before2Args.length);
      assertEquals("test2", ArgsAspect.before2Args[0]);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after3);
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.around3);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test3()
   {
      pojo.field2 = "test3";
      assertTrue(ArgsAspect.before6);
      assertTrue(ArgsAspect.after6);
      assertNotNull(ArgsAspect.before6Args);
      assertSame(ArgsAspect.before6Args, ArgsAspect.after6Args);
      assertEquals(1, ArgsAspect.before6Args.length);
      assertEquals("after6", ArgsAspect.before6Args[0]);
      
      assertFalse(ArgsAspect.before1);
      assertFalse(ArgsAspect.before2);
      assertFalse(ArgsAspect.before3);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after3);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.around3);
      assertFalse(ArgsAspect.throwing);
      
      assertEquals("before6", pojo.field2);
   }
   
   public void test4()
   {
      pojo.field3 = 10;
      assertTrue(ArgsAspect.around3);
      assertTrue(ArgsAspect.after1);
      assertTrue(ArgsAspect.after2);
      assertTrue(ArgsAspect.after3);
      assertNotNull(ArgsAspect.around3Args);
      assertEquals(1, ArgsAspect.around3Args.length);
      assertEquals(10, ArgsAspect.around3Args[0]);
      assertSame(ArgsAspect.after2Args, ArgsAspect.after3Args);
      assertEquals(1, ArgsAspect.after2Args.length);
      assertEquals(10, ArgsAspect.after2Args[0]);
      
      
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.before1);
      assertFalse(ArgsAspect.before2);
      assertFalse(ArgsAspect.before3);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test5()
   {
      boolean result = ArgsPOJO.field4 && true;
      
      assertTrue(ArgsAspect.before1);
      assertTrue(ArgsAspect.before2);
      assertTrue(ArgsAspect.before3);
      assertTrue(ArgsAspect.around3);
      assertTrue(ArgsAspect.after1);
      assertTrue(ArgsAspect.after2);
      assertTrue(ArgsAspect.after3);
      assertNotNull(ArgsAspect.before2Args);
      assertSame(ArgsAspect.before2Args, ArgsAspect.before3Args);
      assertSame(ArgsAspect.before2Args, ArgsAspect.after2Args);
      assertSame(ArgsAspect.before2Args, ArgsAspect.after3Args);
      assertEquals(0, ArgsAspect.before2Args.length);
      
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test6()
   {
      ArgsPOJO.field4 = false;
      
      assertTrue(ArgsAspect.before1);
      assertTrue(ArgsAspect.before2);
      assertTrue(ArgsAspect.before3);
      assertTrue(ArgsAspect.around3);
      assertTrue(ArgsAspect.after1);
      assertTrue(ArgsAspect.after2);
      assertTrue(ArgsAspect.after3);
      assertNotNull(ArgsAspect.before2Args);
      assertSame(ArgsAspect.before2Args, ArgsAspect.before3Args);
      assertSame(ArgsAspect.before2Args, ArgsAspect.around3Args);
      assertSame(ArgsAspect.around3Args, ArgsAspect.after2Args);
      assertSame(ArgsAspect.before2Args, ArgsAspect.after3Args);
      assertEquals(1, ArgsAspect.before2Args.length);
      assertEquals(false, ((Boolean) ArgsAspect.before2Args[0]).booleanValue());
      
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test7()
   {
      pojo.method1("test6", 6, true, null);
      
      assertTrue(ArgsAspect.before3);
      assertTrue(ArgsAspect.after3);
      assertNotNull(ArgsAspect.before3Args);
      assertSame(ArgsAspect.before3Args, ArgsAspect.after3Args);
      assertEquals(4, ArgsAspect.before3Args.length);
      assertEquals("test6", ArgsAspect.before3Args[0]);
      assertEquals(6, ((Integer) ArgsAspect.before3Args[1]).intValue());
      assertEquals(true, ((Boolean) ArgsAspect.before3Args[2]).booleanValue());
      assertNull(ArgsAspect.before3Args[3]);
      System.out.println("AROUND ARGS: " + ArgsAspect.around3Args);
      assertFalse(ArgsAspect.before1);
      assertFalse(ArgsAspect.before2);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.around3);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test8()
   {
      pojo.method2("test7", 0, false, null);
      assertTrue(ArgsAspect.before7);
      assertTrue(ArgsAspect.after7);
      assertTrue(ArgsAspect.around1);
      
      assertNotNull(ArgsAspect.before7Args);
      assertSame(ArgsAspect.before7Args, ArgsAspect.after7Args);
      assertSame(ArgsAspect.before7Args, ArgsAspect.around1Args);
      assertEquals(4, ArgsAspect.before7Args.length);
      assertEquals("after7", ArgsAspect.before7Args[0]);
      assertEquals(14, ((Integer) ArgsAspect.before7Args[1]).intValue());
      assertEquals(false, ((Boolean) ArgsAspect.before7Args[2]).booleanValue());
      
      assertNotNull(ArgsAspect.before7Args[3]);
      assertEquals(1, ((ArgsPOJO[]) ArgsAspect.before7Args[3]).length);
      assertNull(((ArgsPOJO[]) ArgsAspect.before7Args[3])[0]);
      
      assertFalse(ArgsAspect.before1);
      assertFalse(ArgsAspect.before2);
      assertFalse(ArgsAspect.before3);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after3);
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.around3);
      assertFalse(ArgsAspect.throwing);
   }
   
   public void test9()
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method3((short) 0, 100);
      }
      catch(POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
      
      assertTrue(ArgsAspect.throwing);
      assertNotNull(ArgsAspect.throwingArgs);
      assertEquals(2, ArgsAspect.throwingArgs.length);
      assertEquals(0, ((Short) ArgsAspect.throwingArgs[0]).shortValue());
      assertEquals(100, ((Long) ArgsAspect.throwingArgs[1]).longValue());
      
      assertFalse(ArgsAspect.before1);
      assertFalse(ArgsAspect.before2);
      assertFalse(ArgsAspect.before3);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.before8);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after3);
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around2);
      assertFalse(ArgsAspect.around3);
   }
   
   public void test10()
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method4();
      } catch (POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);

      assertTrue(ArgsAspect.before8);
      assertTrue(ArgsAspect.around2);
      assertTrue(ArgsAspect.throwing);
      
      assertNotNull(ArgsAspect.before8Args);
      assertSame(ArgsAspect.before8Args, ArgsAspect.around2Args);
      assertSame(ArgsAspect.around2Args, ArgsAspect.throwingArgs);
      
      assertEquals(2, ArgsAspect.before8Args.length);
      assertEquals((short) -8, ((Short) ArgsAspect.before8Args[0]).shortValue());
      assertEquals((long) 8, ((Long) ArgsAspect.before8Args[1]).longValue());
      
      assertFalse(ArgsAspect.before1);
      assertFalse(ArgsAspect.before2);
      assertFalse(ArgsAspect.before3);
      assertFalse(ArgsAspect.before6);
      assertFalse(ArgsAspect.before7);
      assertFalse(ArgsAspect.after1);
      assertFalse(ArgsAspect.after2);
      assertFalse(ArgsAspect.after3);
      assertFalse(ArgsAspect.after6);
      assertFalse(ArgsAspect.after7);
      // after 8 is not called due to the exceptio thrown
      assertFalse(ArgsAspect.after8);
      assertFalse(ArgsAspect.around1);
      assertFalse(ArgsAspect.around3);
   }
}