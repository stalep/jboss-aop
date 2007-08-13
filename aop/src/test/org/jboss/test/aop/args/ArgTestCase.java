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
package org.jboss.test.aop.args;

import org.jboss.test.aop.AOPTestWithSetup;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


/**
 * Tests the use of @Arg parameters.
 * 
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ArgTestCase extends AOPTestWithSetup
{
   POJO pojo;

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
      pojo = new POJO();
      ArgAspect.clear();
   }
   
//   public void testBench()
//   {
//      {
//         long start = System.currentTimeMillis();
//         for (int i = 0; i < 1000000; i++)
//         {
//            pojo.bunchArgs(1, 2.2, 3.3F, "four", 5);
//         }
//         long end = System.currentTimeMillis() - start;
//         System.out.println("bunchArgs: " + end);
//      }
//
//      {
//         long start = System.currentTimeMillis();
//         for (int i = 0; i < 1000000; i++)
//         {
//            pojo.bunchWrapped(1, 2.2, 3.3F, "four", 5);
//         }
//         long end = System.currentTimeMillis() - start;
//         System.out.println("bunchWrapped: " + end);
//      }
//
//      {
//         long start = System.currentTimeMillis();
//         for (int i = 0; i < 1000000; i++)
//         {
//            pojo.bunchArgsWithInvocation(1, 2.2, 3.3F, "four", 5);
//         }
//         long end = System.currentTimeMillis() - start;
//         System.out.println("bunchArgsWithInvocation: " + end);
//      }
//      assertTrue(ArgAspect.argsWithInvocation);
//      assertTrue(ArgAspect.args);
//      assertTrue(ArgAspect.wrapped);
//   }

   public void testEcho()
   {
      pojo.echo("hello");
      //assertTrue(ArgAspect.echoCalled);
   }

//   public void testBunch()
//   {
//      pojo.bunch(1, 2.2, 3.3F, "four", 5);
//      assertTrue(ArgAspect.bunchCalled);
//      assertTrue(ArgAspect.bunch2Called);
//      assertTrue(ArgAspect.arg1Called);
//      assertTrue(ArgAspect.arg2Called);
//      assertTrue(ArgAspect.arg3Called);
//      assertTrue(ArgAspect.arg4Called);
//      assertTrue(ArgAspect.arg15Called);
//      assertTrue(ArgAspect.arg24Called);
//      assertTrue(ArgAspect.emptyArgCalled);
//   }
}