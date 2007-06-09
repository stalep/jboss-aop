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

import org.jboss.aop.joinpoint.FieldAccess;
import org.jboss.aop.joinpoint.MethodExecution;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests the use of @JoinPoint parameters.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class JoinPointTestCase extends AOPTestWithSetup
{
   private JoinPointPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("JoinPointTestCase");
      suite.addTestSuite(JoinPointTestCase.class);
      return suite;
   }
   
   public JoinPointTestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      JoinPointAspect.clear();
      this.pojo = new JoinPointPOJO();
   }
   
   public void test1()
   {
      pojo.number = 0;
      assertEquals("before1", JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPoint);
      assertEquals("after1", JoinPointAspect.afterAdvice);
      assertNotNull(JoinPointAspect.afterJoinPoint);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertTrue(JoinPointAspect.afterJoinPoint instanceof FieldAccess);
      FieldAccess fieldInfo = (FieldAccess) JoinPointAspect.afterJoinPoint;
      assertEquals("number", fieldInfo.getAdvisedField().getName());
      assertFalse(fieldInfo.isRead());
   }
   
   public void test2()
   {
      pojo.text = "test2";
      assertEquals("before2", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPoint);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertEquals("finally1", JoinPointAspect.finallyAdvice);
      assertNotNull(JoinPointAspect.finallyJoinPoint);
      
      assertSame(JoinPointAspect.beforeJoinPoint,
            JoinPointAspect.finallyJoinPoint);
      assertTrue(JoinPointAspect.beforeJoinPoint instanceof FieldAccess);
      FieldAccess fieldInfo = (FieldAccess) JoinPointAspect.beforeJoinPoint;
      assertEquals("text", fieldInfo.getAdvisedField().getName());
      assertFalse(fieldInfo.isRead());
   }
   
   public void test3()
   {
      String text = pojo.text;
      assertNull(JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPoint);
      assertEquals("after5", JoinPointAspect.afterAdvice);
      assertNotNull(JoinPointAspect.afterJoinPoint);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertTrue(JoinPointAspect.afterJoinPoint instanceof FieldAccess);
      FieldAccess fieldInfo = (FieldAccess) JoinPointAspect.afterJoinPoint;
      assertEquals("text", fieldInfo.getAdvisedField().getName());
      assertTrue(fieldInfo.isRead());
   }
   
   public void test4()
   {
      pojo.method1();
      assertEquals("before3", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPoint);
      assertEquals("after3", JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertTrue(JoinPointAspect.beforeJoinPoint instanceof MethodExecution);
      assertEquals("method1", ((MethodExecution) JoinPointAspect.beforeJoinPoint).
            getAdvisedMethod().getName());
   }
   
   public void test5() throws POJOException
   {
      pojo.method2(false);
      assertEquals("before4", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPoint);
      assertEquals("after4", JoinPointAspect.afterAdvice);
      assertNotNull(JoinPointAspect.afterJoinPoint);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertSame(JoinPointAspect.beforeJoinPoint, JoinPointAspect.afterJoinPoint);
      assertTrue(JoinPointAspect.beforeJoinPoint instanceof MethodExecution);
      assertEquals("method2", ((MethodExecution) JoinPointAspect.beforeJoinPoint).
            getAdvisedMethod().getName());
   }
   
   public void test6() throws POJOException
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method2(true);
      }
      catch (POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
      
      assertEquals("before4", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPoint);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertEquals("throwing1", JoinPointAspect.throwingAdvice);
      assertNotNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertSame(JoinPointAspect.beforeJoinPoint,
            JoinPointAspect.throwingJoinPoint);
      assertTrue(JoinPointAspect.beforeJoinPoint instanceof MethodExecution);
      assertEquals("method2", ((MethodExecution) JoinPointAspect.beforeJoinPoint).
            getAdvisedMethod().getName());
   }
   
   public void test7() throws POJOException
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method3();
      }
      catch (POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
      
      assertEquals("before4", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPoint);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertEquals("finally2", JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertTrue(JoinPointAspect.beforeJoinPoint instanceof MethodExecution);
      assertEquals("method3", ((MethodExecution) JoinPointAspect.beforeJoinPoint).
            getAdvisedMethod().getName());
   }
   
   public void test8() throws POJOException
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method4();
      }
      catch (POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
      
      assertNull(JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPoint);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertEquals("throwing3", JoinPointAspect.throwingAdvice);
      assertNotNull(JoinPointAspect.throwingJoinPoint);
      assertEquals("finally3", JoinPointAspect.finallyAdvice);
      assertNotNull(JoinPointAspect.finallyJoinPoint);
      
      assertSame(JoinPointAspect.throwingJoinPoint,
            JoinPointAspect.finallyJoinPoint);
      assertTrue(JoinPointAspect.throwingJoinPoint instanceof MethodExecution);
      assertEquals("method4", ((MethodExecution) JoinPointAspect.throwingJoinPoint).
            getAdvisedMethod().getName());
   }
   
   public void test9() throws POJOException
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method5();
      }
      catch (POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
      
      assertNull(JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPoint);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertEquals("throwing4", JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
   }
   
   public void test10() throws POJOException
   {
      boolean exceptionThrown = false;
      try
      {
         pojo.method6();
      }
      catch (POJOException e)
      {
         exceptionThrown = true;
      }
      assertTrue(exceptionThrown);
      
      assertNull(JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPoint);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPoint);
      assertEquals("throwing5", JoinPointAspect.throwingAdvice);
      assertNotNull(JoinPointAspect.throwingJoinPoint);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPoint);
      
      assertTrue(JoinPointAspect.throwingJoinPoint instanceof MethodExecution);
      assertEquals("method6", ((MethodExecution) JoinPointAspect.throwingJoinPoint).
            getAdvisedMethod().getName());

   }
}