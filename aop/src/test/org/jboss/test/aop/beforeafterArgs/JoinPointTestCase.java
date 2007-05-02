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

import org.jboss.aop.FieldInfo;
import org.jboss.aop.MethodInfo;
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
      assertNull(JoinPointAspect.beforeJoinPointInfo);
      assertEquals("after1", JoinPointAspect.afterAdvice);
      assertNotNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertTrue(JoinPointAspect.afterJoinPointInfo instanceof FieldInfo);
      FieldInfo fieldInfo = (FieldInfo) JoinPointAspect.afterJoinPointInfo;
      assertEquals("number", fieldInfo.getAdvisedField().getName());
      assertFalse(fieldInfo.isRead());
   }
   
   public void test2()
   {
      pojo.text = "test2";
      assertEquals("before2", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertEquals("finally1", JoinPointAspect.finallyAdvice);
      assertNotNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertSame(JoinPointAspect.beforeJoinPointInfo,
            JoinPointAspect.finallyJoinPointInfo);
      assertTrue(JoinPointAspect.beforeJoinPointInfo instanceof FieldInfo);
      FieldInfo fieldInfo = (FieldInfo) JoinPointAspect.beforeJoinPointInfo;
      assertEquals("text", fieldInfo.getAdvisedField().getName());
      assertFalse(fieldInfo.isRead());
   }
   
   public void test3()
   {
      String text = pojo.text;
      assertNull(JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPointInfo);
      assertEquals("after5", JoinPointAspect.afterAdvice);
      assertNotNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertTrue(JoinPointAspect.afterJoinPointInfo instanceof FieldInfo);
      FieldInfo fieldInfo = (FieldInfo) JoinPointAspect.afterJoinPointInfo;
      assertEquals("text", fieldInfo.getAdvisedField().getName());
      assertTrue(fieldInfo.isRead());
   }
   
   public void test4()
   {
      pojo.method1();
      assertEquals("before3", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPointInfo);
      assertEquals("after3", JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertTrue(JoinPointAspect.beforeJoinPointInfo instanceof MethodInfo);
      assertEquals("method1", ((MethodInfo) JoinPointAspect.beforeJoinPointInfo).
            getAdvisedMethod().getName());
   }
   
   public void test5() throws POJOException
   {
      pojo.method2(false);
      assertEquals("before4", JoinPointAspect.beforeAdvice);
      assertNotNull(JoinPointAspect.beforeJoinPointInfo);
      assertEquals("after4", JoinPointAspect.afterAdvice);
      assertNotNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertSame(JoinPointAspect.beforeJoinPointInfo, JoinPointAspect.afterJoinPointInfo);
      assertTrue(JoinPointAspect.beforeJoinPointInfo instanceof MethodInfo);
      assertEquals("method2", ((MethodInfo) JoinPointAspect.beforeJoinPointInfo).
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
      assertNotNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertEquals("throwing1", JoinPointAspect.throwingAdvice);
      assertNotNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertSame(JoinPointAspect.beforeJoinPointInfo,
            JoinPointAspect.throwingJoinPointInfo);
      assertTrue(JoinPointAspect.beforeJoinPointInfo instanceof MethodInfo);
      assertEquals("method2", ((MethodInfo) JoinPointAspect.beforeJoinPointInfo).
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
      assertNotNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertEquals("finally2", JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertTrue(JoinPointAspect.beforeJoinPointInfo instanceof MethodInfo);
      assertEquals("method3", ((MethodInfo) JoinPointAspect.beforeJoinPointInfo).
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
      assertNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertEquals("throwing3", JoinPointAspect.throwingAdvice);
      assertNotNull(JoinPointAspect.throwingJoinPointInfo);
      assertEquals("finally3", JoinPointAspect.finallyAdvice);
      assertNotNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertSame(JoinPointAspect.throwingJoinPointInfo,
            JoinPointAspect.finallyJoinPointInfo);
      assertTrue(JoinPointAspect.throwingJoinPointInfo instanceof MethodInfo);
      assertEquals("method4", ((MethodInfo) JoinPointAspect.throwingJoinPointInfo).
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
      assertNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertEquals("throwing4", JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
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
      assertNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertEquals("throwing5", JoinPointAspect.throwingAdvice);
      assertNotNull(JoinPointAspect.throwingJoinPointInfo);
      assertNull(JoinPointAspect.finallyAdvice);
      assertNull(JoinPointAspect.finallyJoinPointInfo);
      
      assertTrue(JoinPointAspect.throwingJoinPointInfo instanceof MethodInfo);
      assertEquals("method6", ((MethodInfo) JoinPointAspect.throwingJoinPointInfo).
            getAdvisedMethod().getName());

   }
}