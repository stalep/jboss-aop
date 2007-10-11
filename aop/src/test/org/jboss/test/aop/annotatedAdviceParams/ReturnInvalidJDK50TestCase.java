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

import org.jboss.aop.advice.NoMatchingAdviceException;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests that have been temporarily removed from {@link ReturnInvalidTestCase} (task
 * JBAOP-458)
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ReturnInvalidJDK50TestCase extends AOPTestWithSetup
{
   private ReturnInvalidPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ReturnTestCase");
      suite.addTestSuite(ReturnInvalidJDK50TestCase.class);
      return suite;
   }
   
   public ReturnInvalidJDK50TestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      ReturnAspectGenerics.clear();
      this.pojo = new ReturnInvalidPOJO();
   }
   
   public void testGenerics1()
   {
      boolean thrown = false;
      try
      {
         pojo.methodGenericsAround1();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAround2();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAround4();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAround5();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAround7();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAround8();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAfter5();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         pojo.methodGenericsAfter7();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         pojo.methodGenericsAfter8();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         pojo.methodGenericsFinally5();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         pojo.methodGenericsFinally7();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         pojo.methodGenericsFinally8();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         pojo.methodGenericsFinally9();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         pojo.methodGenericsFinally10();
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
   }
   
   public void testGenerics2()
   {
      boolean thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAround1);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAround2);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAround4);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAround5);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAround7);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAround8);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAfter5);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);

      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAfter7);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsAfter8);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsFinally5);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsFinally7);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsFinally8);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsFinally9);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
      
      thrown = false;
      try
      {
         assertNull(pojo.fieldGenericsFinally10);
      }
      catch (NoMatchingAdviceException e)
      {
         thrown = true;
      }
      assertTrue(thrown);
   }
}