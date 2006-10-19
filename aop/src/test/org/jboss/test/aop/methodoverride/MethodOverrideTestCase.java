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
package org.jboss.test.aop.methodoverride;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.aop.proxy.ClassProxyFactory;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 *
 * @author <a href="mailto:stalep@conduct.no">Stale W. Pedersen</a>
 * @version $Revision
 */
public class MethodOverrideTestCase extends AOPTestWithSetup
{
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("MethodOverrideTestCase");
      suite.addTestSuite(MethodOverrideTestCase.class);
      return suite;
   }

   public MethodOverrideTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
   }
   
   public void testMethod()
   {
      try {
      ClassProxyFactory.newInstance(POJO.class);
      assertTrue("ClassProxy failed to instrument class", true);
      }
      catch(Exception e)
      {
         System.out.println("ERROR: "+e.getMessage());
         e.printStackTrace();
         assertTrue("ClassProxy failed to instrument class", false);
         
      }
     
   }
   
   public void testGenericMethod()
   {
      try {
         SuperPOJO superPojo = (SuperPOJO) ClassProxyFactory.newInstance(SubPOJO.class);
//         java.util.AbstractList aList = new java.util.ArrayList();
//         superPojo.foo(aList);
//         SubPOJO subPojo = (SubPOJO) ClassProxyFactory.newInstance(SubPOJO.class);
//         java.util.ArrayList aList = new java.util.ArrayList();
//         subPojo.foo(aList);
         assertTrue("ClassProxy failed to instrument generic class", true);
      }
      catch(Exception e)
      {
         System.out.println("ERROR: "+e.getMessage());
         e.printStackTrace();
         assertTrue("ClassProxy failed to instrument generic class", false);
         
      }
   }
   
   public void testMethodOverride14()
   {
      try {
         ClassProxyFactory.newInstance(SubPOJOJDK14.class);
         assertTrue("ClassProxy failed to instrument jdk14 class", true);
         }
         catch(Exception e)
         {
            System.out.println("ERROR: "+e.getMessage());
            e.printStackTrace();
            assertTrue("ClassProxy failed to instrument jdk14 class", false);
            
         }
   }

}