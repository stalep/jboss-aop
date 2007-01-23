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
package org.jboss.test.aop.pointcut;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.PointcutExpressionParser;
import org.jboss.aop.pointcut.ast.PointcutExpressionParserVisitor;
import org.jboss.test.aop.AOPTestWithSetup;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests an annotated introduction
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 45977 $
 */
public class PointcutTester extends AOPTestWithSetup
{
   public static Test suite()
   {
      TestSuite suite = new TestSuite("PointcutTester");
      suite.addTestSuite(PointcutTester.class);
      return suite;
   }

   public PointcutTester(String name)
   {
      super(name);
   }

   public void testPassingPointcuts() throws Exception
   {
      Executor e = new Executor();
      e.parseGoodPointcut("execution(public * *->*())");
      e.parseGoodPointcut("execution(* *->*())");
      e.parseGoodPointcut("execution(* $instanceof{a}->$implements{a}())");
      e.parseGoodPointcut("execution(* $instanceof{a.b}->$implements{a.b}())");
      e.parseGoodPointcut("execution(* $instanceof{@a.b}->$implements{@a.b}())");
      e.parseGoodPointcut("execution(* $instanceof{a}->$implementing{a}())");
      e.parseGoodPointcut("execution(* $instanceof{a.b}->$implementing{a.b}())");
      e.parseGoodPointcut("execution(* $instanceof{@a.b}->$implementing{@a.b}())");
      checkFailures(e.failures);
   }
   
   public void testBadPointcuts() throws Exception
   {
      Executor e = new Executor();
      e.parseBadPointcut("execution(*->*()");
      checkFailures(e.failures);
   }
   
   private void checkFailures(ArrayList failures)
   {
      StringBuffer buf = new StringBuffer();
      if (failures.size() > 0)
      {
         buf.append("======= Did not pass validation ===========\n\n");
         for (Iterator it = failures.iterator() ; it.hasNext() ; )
         {
            buf.append((String)it.next());
            buf.append("\n");
         }
         
         fail(buf.toString());
      }
   }
   
   private static class Executor
   {
      ArrayList failures = new ArrayList();

      private void parseGoodPointcut(String pointcut) throws Exception
      {
         parsePointcut(pointcut, false);
      }

      private void parseBadPointcut(String pointcut) throws Exception
      {
         parsePointcut(pointcut, true);
      }

      private void parsePointcut(String pointcut, boolean expectFailure) throws Exception
      {
         StringReader reader = new StringReader(pointcut);
         PointcutExpressionParser t = new PointcutExpressionParser(reader);
   
         try
         {
            ASTStart n = t.Start();
            PointcutExpressionParserVisitor v = new EmptyPointcutVisitor();
            n.jjtAccept(v, null);
            if (expectFailure)
            {
               failures.add("- Should not have passed: " + pointcut);
            }
         }
         catch (Exception e)
         {
            if (!expectFailure)
            {
               failures.add("+ Should have passed: " + pointcut);
            }
         }
      }
   }
}

