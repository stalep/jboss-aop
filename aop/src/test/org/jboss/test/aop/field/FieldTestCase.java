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
package org.jboss.test.aop.field;

import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 *
 * @author <a href="mailto:stalep@conduct.no">Stale W. Pedersen</a>
 * @version $Revision
 */
public class FieldTestCase extends AOPTestWithSetup
{
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("FieldTestCase");
      suite.addTestSuite(FieldTestCase.class);
      return suite;
   }

   public FieldTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
   }
   
   public void testField()
   {
      System.out.println("*** testField");
      SubPOJO spojo = new SubPOJO(4);
      assertEquals("Field is not set correctly", spojo.getPOJOField(), (spojo.getSubPOJOField()/2));  
   }

   public void testField2()
   {
      System.out.println("*** testField2");
      SubSubPOJO spojo = new SubSubPOJO(4);
      assertEquals("Field is not set correctly", spojo.getSubSubPOJOField()/2, (spojo.getSubPOJOField()));  
   }
   
   public void testFieldInheritance()
   {
      System.out.println("*** testFieldInheritance");
      SubSubPOJO pojo = new SubSubPOJO(4);

      TraceInterceptor.intercepted = false;
      pojo.mine = 5;
      assertTrue(TraceInterceptor.intercepted);


      TraceInterceptor.intercepted = false;
      pojo.pojoInherited = 5;
      assertTrue(TraceInterceptor.intercepted);

      TraceInterceptor.intercepted = false;
      pojo.subpojoInherited = 5;
      assertTrue(TraceInterceptor.intercepted);
   }
   
}
