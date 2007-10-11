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
package org.jboss.test.aop.annotatedAdviceParams;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests that have been temporarily removed from {@link ReturnTestCase} (task
 * JBBUILD-422)
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ReturnJDK50TestCase extends AOPTestWithSetup
{
   private ReturnPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ReturnJDK50TestCase");
      suite.addTestSuite(ReturnTestCase.class);
      return suite;
   }
   
   public ReturnJDK50TestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      ReturnAspectGenerics.clear();
      this.pojo = new ReturnPOJO();
   }
   
   public void testGenerics1() throws Exception
   {
      pojo.methodGenerics(false);
      assertTrue(ReturnAspectGenerics.around3);
      assertTrue(ReturnAspectGenerics.after1);
      assertTrue(ReturnAspectGenerics.after2);
      assertTrue(ReturnAspectGenerics.after3);
      assertTrue(ReturnAspectGenerics.after4);
      assertTrue(ReturnAspectGenerics.after6);
      assertTrue(ReturnAspectGenerics.after9);
      assertTrue(ReturnAspectGenerics.after10);
      assertTrue(ReturnAspectGenerics.finally1);
      assertTrue(ReturnAspectGenerics.finally2);
      assertTrue(ReturnAspectGenerics.finally3);
      assertTrue(ReturnAspectGenerics.finally4);
      assertTrue(ReturnAspectGenerics.finally6);
      assertTrue(ReturnAspectGenerics.finally11);
   }
   
   public void testGenerics2() throws Exception
   {
      boolean thrown = false;
      try
      {
         pojo.methodGenerics(true);
      }
      catch (Throwable t)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      assertTrue(ReturnAspectGenerics.around3);
      assertTrue(ReturnAspectGenerics.around6);
      assertFalse(ReturnAspectGenerics.after1);
      assertFalse(ReturnAspectGenerics.after2);
      assertFalse(ReturnAspectGenerics.after3);
      assertFalse(ReturnAspectGenerics.after4);
      assertFalse(ReturnAspectGenerics.after6);
      assertFalse(ReturnAspectGenerics.after9);
      assertFalse(ReturnAspectGenerics.after10);
      assertTrue(ReturnAspectGenerics.finally1);
      assertTrue(ReturnAspectGenerics.finally2);
      assertTrue(ReturnAspectGenerics.finally3);
      assertTrue(ReturnAspectGenerics.finally4);
      assertTrue(ReturnAspectGenerics.finally6);
      assertTrue(ReturnAspectGenerics.finally11);
   }
}