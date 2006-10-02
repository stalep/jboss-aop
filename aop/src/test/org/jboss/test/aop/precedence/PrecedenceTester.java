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
package org.jboss.test.aop.precedence;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class PrecedenceTester extends AOPTestWithSetup
{
   //Don't list per_instance aspects for constructor
   final static String[] PRECEDENCE_ALL_CONSTRUCTOR = {
         "FirstInterceptor",
			"FirstInterceptor2",
			"SimpleInterceptor2",
			"SimpleInterceptor3",
			"TestAspect.advice",
			"TestAspect2.advice",
			"LastAspect.advice",
			"LastAspect2.advice"};

   final static String[] PRECEDENCE_ALL = {
         "FirstInterceptor",
			"FirstInterceptor2",
			"SimpleInterceptor",
			"SimpleInterceptor2",
			"SimpleInterceptor3",
			"TestAspect.advice",
			"TestAspect2.advice",
			"TestAspect3.advice",
			"LastAspect.advice",
			"LastAspect2.advice"};

   final static String[] PRECEDENCE_TWO = {
         "FirstInterceptor",
			"FirstInterceptor2",
			"SimpleInterceptor",
			"TestAspect.advice",
			"TestAspect.advice2",
			"TestAspect.advice3",
			"TestAspect2.advice",
			"LastAspect.advice",
			"LastAspect2.advice"};

   final static String[] PRECEDENCE_THREE = {
         "FirstInterceptor",
			"FirstInterceptor2",
			"TestAspect.advice",
			"TestAspect.advice2",
			"TestAspect.advice3",
			"LastAspect.advice",
			"LastAspect2.advice"};

   public static Test suite()
   {
      TestSuite suite = new TestSuite("PrecedenceTester");
      suite.addTestSuite(PrecedenceTester.class);
      return suite;
   }

   public PrecedenceTester(String name)
   {
      super(name);
   }

   public void testPrecedence() throws Exception
   {
      System.out.println("*** Invoke constructor");
      Interceptions.reset();
      POJO pojo = new POJO();
      checkInterceptions(PRECEDENCE_ALL_CONSTRUCTOR);
      
      System.out.println("*** Invoke field read");
      Interceptions.reset();
      int i = pojo.var;
      checkInterceptions(PRECEDENCE_ALL);
      
      System.out.println("*** Invoke field write");
      Interceptions.reset();
      pojo.var = i + 1;
      checkInterceptions(PRECEDENCE_ALL);
      
      System.out.println("*** Invoke oneMethod");
      Interceptions.reset();
      pojo.oneMethod();
      checkInterceptions(PRECEDENCE_ALL);
      
      Interceptions.reset();
      pojo.twoMethod();
      checkInterceptions(PRECEDENCE_TWO);

      Interceptions.reset();
      pojo.threeMethod();
      checkInterceptions(PRECEDENCE_THREE);
   }

   private void checkInterceptions(String[] expected)
   {
      ArrayList intercepted = Interceptions.intercepted;
      assertEquals("Wrong number of interceptions", expected.length ,intercepted.size());
      
      for (int i = 0 ; i < expected.length ; i++)
      {
         assertEquals("Wrong interception at index " + i, expected[i], (String)intercepted.get(i));
      }
   }
   
}
