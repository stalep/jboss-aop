package org.jboss.test.aop.args;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests both the use of @Return parameters, and the use of return values in advices.
 * 
 * @author Flavia Rainone
 */
public class ReturnTestCase extends AOPTestWithSetup
{
   private ReturnPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ReturnTestCase");
      suite.addTestSuite(ReturnTestCase.class);
      return suite;
   }
   
   public ReturnTestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      ReturnAspect.clear();
      this.pojo = new ReturnPOJO();
   }
   
   public void test1()
   {
      pojo.method1();
      assertNull(ReturnAspect.aroundAdvice);
      assertNull(ReturnAspect.aroundReturn);
      assertEquals("after1", ReturnAspect.afterAdvice);
      assertNull(ReturnAspect.afterReturn);
   }
   
   public void test2()
   {
      assertEquals("method2", pojo.method2());
      assertNull(ReturnAspect.aroundAdvice);
      assertNull(ReturnAspect.aroundReturn);
      assertEquals("after2", ReturnAspect.afterAdvice);
      assertEquals("method2", ReturnAspect.afterReturn);
   }
   
   public void test3()
   {
      assertEquals("after3", pojo.method3());
      assertEquals("around3", ReturnAspect.aroundAdvice);
      assertEquals("method3", ReturnAspect.aroundReturn);
      assertEquals("after3", ReturnAspect.afterAdvice);
      assertEquals("around3", ReturnAspect.afterReturn);
   }
   
   public void test4()
   {
      assertEquals("after4", pojo.method4());
      assertEquals("around4", ReturnAspect.aroundAdvice);
      assertEquals("method4", ReturnAspect.aroundReturn);
      assertEquals("after4", ReturnAspect.afterAdvice);
      assertEquals("around4", ReturnAspect.afterReturn);
   }
   
   public void test5()
   {
      assertEquals("after5", pojo.method5());
      assertEquals("around5", ReturnAspect.aroundAdvice);
      assertNull(ReturnAspect.aroundReturn);
      assertEquals("after5", ReturnAspect.afterAdvice);
      assertNull(ReturnAspect.afterReturn);
   }
   
   public void test6()
   {
      assertEquals("method6", pojo.method6());
      assertNull(ReturnAspect.aroundAdvice);
      assertNull(ReturnAspect.aroundReturn);
      assertEquals("after6", ReturnAspect.afterAdvice);
      assertNull(ReturnAspect.afterReturn);
   }
   
   public void test7()
   {
      assertEquals("after7", pojo.method7());
      assertEquals("around7", ReturnAspect.aroundAdvice);
      assertEquals("method7", ReturnAspect.aroundReturn);
      assertEquals("after7", ReturnAspect.afterAdvice);
      assertEquals("around7", ReturnAspect.afterReturn);
   }
   
   public void test8()
   {
      SubValue value = pojo.method8();
      assertNotNull(value);
      assertEquals(800, value.i);
      assertEquals("around8", ReturnAspect.aroundAdvice);
      assertNull(ReturnAspect.aroundReturn);
      assertEquals("after8", ReturnAspect.afterAdvice);
      assertNull(ReturnAspect.afterReturn);
   }
   
   public void test9()
   {
      SubValue value = pojo.method9();
      assertNotNull(value);
      assertEquals(9, value.i);
      assertNull(ReturnAspect.aroundAdvice);
      assertNull(ReturnAspect.aroundReturn);
      assertNull(ReturnAspect.afterAdvice);
      assertNull(ReturnAspect.afterReturn);
   }
   
   public void test10()
   {
      SuperValue value = pojo.method10();
      assertNotNull(value);
      assertEquals(100, value.i);
      assertEquals("around10", ReturnAspect.aroundAdvice);
      assertNotNull(ReturnAspect.aroundReturn);
      assertEquals(10, ((SuperValue) ReturnAspect.aroundReturn).i);
      assertNull(ReturnAspect.afterAdvice);
      assertNull(ReturnAspect.afterReturn);
   }

   public void test11()
   {
      SuperValue value = pojo.method11();
      assertNotNull(value);
      assertEquals(1100, value.i);
      assertEquals("around11", ReturnAspect.aroundAdvice);
      assertNotNull(ReturnAspect.aroundReturn);
      assertEquals(11, ((SuperValue) ReturnAspect.aroundReturn).i);
      assertEquals("after11", ReturnAspect.afterAdvice);
      assertNotNull(ReturnAspect.afterReturn);
      assertEquals(110, ((SuperValue) ReturnAspect.afterReturn).i);
   }
   
   public void test12()
   {
      try
      {
         pojo.method12();
      }
      catch(POJOException e){}
   }
}