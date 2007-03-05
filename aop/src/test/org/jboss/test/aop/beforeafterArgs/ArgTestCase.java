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
 * Tests the use of @Arg parameters (this class complements <code>
 * org.jboss.test.aop.args.ArgTestCase</code>, by testing advices that are allowed
 * only with generated advisors).
 * 
 * @Args and <code>invocation.setArguments()</code> are used only as a complement, so
 * we can test @Arg functionality combined with access of arguments array.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 * 
 * @see ArgsTestCase
 * @see org.jboss.test.aop.args.ArgTestCase
 * @see org.jboss.test.aop.args.ArgumentsTestCase
 */
public class ArgTestCase extends AOPTestWithSetup
{
   private ArgsPOJO pojo;

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ArgTestCase");
      suite.addTestSuite(ArgTestCase.class);
      return suite;
   }

   public ArgTestCase(String name)
   {
      super(name);
   }

   public void setUp() throws Exception
   {
      super.setUp();
      this.pojo = new ArgsPOJO();
      ArgAspect.clear();
   }

   public void test1()
   {
      assertEquals(52, this.pojo.bunch1(5, (double) 1.3, (float) 0, "test1", 1));
      
      assertTrue(ArgAspect.before1);
      assertTrue(ArgAspect.before2);
      assertTrue(ArgAspect.before3);
      assertTrue(ArgAspect.before4);
      assertTrue(ArgAspect.before5);
      assertTrue(ArgAspect.around4);
      assertTrue(ArgAspect.around5);
      assertTrue(ArgAspect.after1);
      assertTrue(ArgAspect.after4);
      
      assertEquals(5, ArgAspect.before1X);
      assertEquals(5, ArgAspect.before2X);
      assertEquals(1, ArgAspect.before3Q);
      assertEquals(25, ArgAspect.before5X);
      assertEquals(-17, ArgAspect.before5Q);
      assertEquals(17, ArgAspect.around5X);
      assertEquals(34, ArgAspect.around5Q);
      assertEquals(17, ArgAspect.after1X);
      assertEquals(17, ArgAspect.after4X);
      assertEquals(34, ArgAspect.after4Q);  
   }
   
   public void test2()
   {
      assertEquals(52, this.pojo.bunch1(5, (double) 1.3, (float) 0, "test1", 1));
      
      assertTrue(ArgAspect.before1);
      assertTrue(ArgAspect.before2);
      assertTrue(ArgAspect.before3);
      assertTrue(ArgAspect.before4);
      assertTrue(ArgAspect.before5);
      assertTrue(ArgAspect.around4);
      assertTrue(ArgAspect.around5);
      assertTrue(ArgAspect.after1);
      assertTrue(ArgAspect.after4);
      
      assertEquals(5, ArgAspect.before1X);
      assertEquals(5, ArgAspect.before2X);
      assertEquals(1, ArgAspect.before3Q);
      assertEquals(25, ArgAspect.before5X);
      assertEquals(-17, ArgAspect.before5Q);
      assertEquals(17, ArgAspect.around5X);
      assertEquals(34, ArgAspect.around5Q);
      assertEquals(17, ArgAspect.after1X);
      assertEquals(17, ArgAspect.after4X);
      assertEquals(34, ArgAspect.after4Q);  
   }
   
   public void test3()
   {
      assertEquals(108, this.pojo.bunch2(1, (double) 2.0, (float) 3, 4));
      
      assertTrue(ArgAspect.before1);
      assertTrue(ArgAspect.before2);
      assertTrue(ArgAspect.before4);
      assertTrue(ArgAspect.before5);
      assertTrue(ArgAspect.around6);
      assertTrue(ArgAspect.after4);
      assertTrue(ArgAspect.after6);
      
      assertEquals(1, ArgAspect.before1X);
      assertEquals(1, ArgAspect.before2X);
      assertEquals(5, ArgAspect.before5X);
      assertEquals(4, ArgAspect.before5Q);
      assertEquals(6, ArgAspect.after4X);
      assertEquals(48, ArgAspect.after4Q);  
      assertSame(ArgAspect.around6Args, ArgAspect.after6Args);
      
      assertFalse(ArgAspect.before3);
      assertFalse(ArgAspect.after1);
   }
   
   public void test4()
   {
      assertEquals(18, this.pojo.bunch3(1, (double) 2.0, (float) 3, 4));
      
      assertTrue(ArgAspect.before1);
      assertTrue(ArgAspect.before2);
      assertTrue(ArgAspect.before4);
      assertTrue(ArgAspect.before5);
      assertTrue(ArgAspect.after4);
      assertTrue(ArgAspect.after6);
      
      assertEquals(1, ArgAspect.before1X);
      assertEquals(1, ArgAspect.before2X);
      assertEquals(5, ArgAspect.before5X);
      assertEquals(4, ArgAspect.before5Q);
      assertEquals(5, ArgAspect.after4X);
      assertEquals(4, ArgAspect.after4Q);  
      assertNotNull(ArgAspect.after6Args);
      assertEquals(5, ((Integer) ArgAspect.after6Args[0]).intValue());
      assertEquals(2.0, ((Double) ArgAspect.after6Args[1]).doubleValue());
      assertEquals(3.0, ((Float) ArgAspect.after6Args[2]).floatValue());
      assertEquals(4, ((Integer) ArgAspect.after6Args[3]).intValue());
      
      assertFalse(ArgAspect.before3);
      assertFalse(ArgAspect.around6);
      assertFalse(ArgAspect.after1);
   }
   
   public void test5()
   {
      assertEquals(132, this.pojo.bunch4(10, (double) 9.0, (float) 8.0, 7));
      
      assertTrue(ArgAspect.before1);
      assertTrue(ArgAspect.before2);
      assertTrue(ArgAspect.before4);
      assertTrue(ArgAspect.before5);
      assertTrue(ArgAspect.around6);
      
      assertEquals(10, ArgAspect.before1X);
      assertEquals(10, ArgAspect.before2X);
      assertEquals(50, ArgAspect.before5X);
      assertEquals(7, ArgAspect.before5Q);
      
      assertFalse(ArgAspect.before3);
      assertFalse(ArgAspect.after1);
      assertFalse(ArgAspect.after4);
      assertFalse(ArgAspect.after6);
   }
   
   public void test6()
   {
      assertEquals(110, this.pojo.bunch5(10, (double) 9.0, (float) 8.0, 7));
      
      assertTrue(ArgAspect.before1);
      assertTrue(ArgAspect.before2);
      assertTrue(ArgAspect.before4);
      assertTrue(ArgAspect.before5);
      
      
      assertEquals(10, ArgAspect.before1X);
      assertEquals(10, ArgAspect.before2X);
      assertEquals(50, ArgAspect.before5X);
      assertEquals(7, ArgAspect.before5Q);
      
      assertFalse(ArgAspect.before3);
      assertFalse(ArgAspect.around6);
      assertFalse(ArgAspect.after1);
      assertFalse(ArgAspect.after4);
      assertFalse(ArgAspect.after6);
   }
   
   public void test7()
   {
      assertEquals(156, this.pojo.bunch6(51, (double) 5.3, (float) 61, 131));
      
      assertTrue(ArgAspect.around6);
      assertTrue(ArgAspect.after4);
      assertTrue(ArgAspect.after6);
      
      assertEquals(6, ArgAspect.after4X);
      assertEquals(48, ArgAspect.after4Q);  
      assertSame(ArgAspect.around6Args, ArgAspect.after6Args);
      
      assertFalse(ArgAspect.before1);
      assertFalse(ArgAspect.before2);
      assertFalse(ArgAspect.before4);
      assertFalse(ArgAspect.before5);
      assertFalse(ArgAspect.before3);
      assertFalse(ArgAspect.after1);
   }
   
   public void test8()
   {
      assertEquals(278, this.pojo.bunch7(51, (double) 5.3, (float) 61, 131));
      
      assertTrue(ArgAspect.after4);
      assertTrue(ArgAspect.after6);
      
      assertEquals(51, ArgAspect.after4X);
      assertEquals(131, ArgAspect.after4Q);  
      assertNotNull(ArgAspect.after6Args);
      assertEquals(51, ((Integer) ArgAspect.after6Args[0]).intValue());
      assertEquals(5.3, ((Double) ArgAspect.after6Args[1]).doubleValue());
      assertEquals(61, ((Float) ArgAspect.after6Args[2]).floatValue());
      assertEquals(131, ((Integer) ArgAspect.after6Args[3]).intValue());
      
      assertFalse(ArgAspect.before1);
      assertFalse(ArgAspect.before2);
      assertFalse(ArgAspect.before4);
      assertFalse(ArgAspect.before5);
      assertFalse(ArgAspect.before3);
      assertFalse(ArgAspect.around6);
      assertFalse(ArgAspect.after1);
   }
   
   public void test9() throws POJOException
   {
      this.pojo.method5(new Implementor());
      
      assertTrue(ArgAspect.beforeInterface1);
      assertTrue(ArgAspect.beforeInterface2);
      assertTrue(ArgAspect.beforeInterface3);
      
      assertTrue(ArgAspect.aroundInterface1);
      assertTrue(ArgAspect.aroundInterface2);
      assertTrue(ArgAspect.aroundInterface3);
      
      assertTrue(ArgAspect.afterInterface1);
      assertTrue(ArgAspect.afterInterface2);
      assertTrue(ArgAspect.afterInterface3);
      
      assertFalse(ArgAspect.throwingInterface1);
      assertFalse(ArgAspect.throwingInterface2);
      assertFalse(ArgAspect.throwingInterface3);
   }
   
   public void test10()
   {
      boolean thrown = false;
      try
      {
         this.pojo.method6(null);
      }
      catch(POJOException e)
      {
         thrown = true;
      }
      
      assertTrue(thrown); // verify precondition for this test
      
      assertTrue(ArgAspect.beforeInterface1);
      assertTrue(ArgAspect.beforeInterface2);
      assertTrue(ArgAspect.beforeInterface3);
      
      assertTrue(ArgAspect.aroundInterface1);
      assertTrue(ArgAspect.aroundInterface2);
      assertTrue(ArgAspect.aroundInterface3);
      
      assertFalse(ArgAspect.afterInterface1);
      assertFalse(ArgAspect.afterInterface2);
      assertFalse(ArgAspect.afterInterface3);
      
      assertTrue(ArgAspect.throwingInterface1);
      assertTrue(ArgAspect.throwingInterface2);
      assertTrue(ArgAspect.throwingInterface3);
   }
}