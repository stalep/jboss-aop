package org.jboss.test.aop.args;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.aop.FieldInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests the use of @JoinPoint parameters.
 * 
 * @author Flavia Rainone
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
      
      assertNull(JoinPointAspect.beforeAdvice);
      assertNull(JoinPointAspect.beforeJoinPointInfo);
      assertNull(JoinPointAspect.afterAdvice);
      assertNull(JoinPointAspect.afterJoinPointInfo);
      assertNull(JoinPointAspect.throwingAdvice);
      assertNull(JoinPointAspect.throwingJoinPointInfo);
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
      assertTrue(JoinPointAspect.throwingJoinPointInfo instanceof MethodInfo);
      assertEquals("method6", ((MethodInfo) JoinPointAspect.throwingJoinPointInfo).
            getAdvisedMethod().getName());

   }
}