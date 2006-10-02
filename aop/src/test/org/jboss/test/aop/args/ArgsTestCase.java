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
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ArgsTestCase extends AOPTestWithSetup
{

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("AnnotatedTester");
      suite.addTestSuite(ArgsTestCase.class);
      return suite;
   }

   public ArgsTestCase(String name)
   {
      super(name);
   }

   public void testBench()
   {
      POJO pojo = new POJO();

      {
         long start = System.currentTimeMillis();
         for (int i = 0; i < 1000000; i++)
         {
            pojo.bunchArgs(1, 2.2, 3.3F, "four", 5);
         }
         long end = System.currentTimeMillis() - start;
         System.out.println("bunchArgs: " + end);
      }

      {
         long start = System.currentTimeMillis();
         for (int i = 0; i < 1000000; i++)
         {
            pojo.bunchWrapped(1, 2.2, 3.3F, "four", 5);
         }
         long end = System.currentTimeMillis() - start;
         System.out.println("bunchWrapped: " + end);
      }

      {
         long start = System.currentTimeMillis();
         for (int i = 0; i < 1000000; i++)
         {
            pojo.bunchArgsWithInvocation(1, 2.2, 3.3F, "four", 5);
         }
         long end = System.currentTimeMillis() - start;
         System.out.println("bunchArgsWithInvocation: " + end);
      }

      assertTrue(Aspect.argsWithInvocation);
      assertTrue(Aspect.args);
      assertTrue(Aspect.wrapped);

   }

   public void testEcho()
   {
      POJO pojo = new POJO();

      Aspect.echoCalled = false;
      pojo.echo("hello");
      assertTrue(Aspect.echoCalled);
   }

   public void testBunch()
   {
      POJO pojo = new POJO();

      Aspect.bunchCalled = false;
      Aspect.arg1Called = false;
      Aspect.arg2Called = false;
      Aspect.arg3Called = false;
      Aspect.arg4Called = false;
      Aspect.arg15Called = false;
      Aspect.arg24Called = false;
      pojo.bunch(1, 2.2, 3.3F, "four", 5);
      assertTrue(Aspect.bunchCalled);
      assertTrue(Aspect.arg1Called);
      assertTrue(Aspect.arg2Called);
      assertTrue(Aspect.arg3Called);
      assertTrue(Aspect.arg4Called);
      assertTrue(Aspect.arg15Called);
      assertTrue(Aspect.arg24Called);
   }

}
