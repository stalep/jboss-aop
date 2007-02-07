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
 * org.jboss.test.aop.args.ArgTestCAse</code>, by testing advices that are allowed
 * only with generated advisors).
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
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
      assertEquals(52, this.pojo.bunch(5, (double) 1.3, (float) 0, "test1", 1));
      
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
      assertEquals(1, ArgAspect.before3Y);
      assertEquals(25, ArgAspect.before5X);
      assertEquals(-17, ArgAspect.before5Y);
      assertEquals(17, ArgAspect.around5X);
      assertEquals(34, ArgAspect.around5Y);
      assertEquals(17, ArgAspect.after1X);
      assertEquals(17, ArgAspect.after4X);
      assertEquals(34, ArgAspect.after4Y);  
   }
}