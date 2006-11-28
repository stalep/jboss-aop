package org.jboss.test.aop.args;

import org.jboss.test.aop.AOPTestWithSetup;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Tests the use of @org.jboss.aop.advice.annotation.Invocation parameters.
 * 
 * @author Flavia Rainone
 */
public class InvocationTestCase extends AOPTestWithSetup
{
   private InvocationPOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("InvocationTestCase");
      suite.addTestSuite(InvocationTestCase.class);
      return suite;
   }
   
   public InvocationTestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      InvocationAspect.clear();
      this.pojo = new InvocationPOJO();
   }
   
   public void test1()
   {
      assertEquals("aroundMethodExecution1", pojo.method1());
      assertEquals("aroundMethodExecution1", InvocationAspect.advice);
      assertNull(InvocationAspect.invokeReturn);
   }
   
   public void test2()
   {
      assertEquals(2, pojo.method2());
      assertNull(InvocationAspect.advice);
      assertNull(InvocationAspect.invokeReturn);
   }
   
   public void test3()
   {
      assertEquals(15000, pojo.callerMethod(20));
      assertEquals("aroundMethodCalledByMethod", InvocationAspect.advice);
      assertNotNull(InvocationAspect.invokeReturn);
      assertEquals(40, ((Integer) InvocationAspect.invokeReturn).intValue());
   }
   
   public void test4()
   {
      pojo.number = 35;
      assertEquals("aroundFieldWrite", InvocationAspect.advice);
      assertNull(InvocationAspect.invokeReturn);
      
      
      assertEquals(500, pojo.number);
      assertEquals("aroundFieldRead", InvocationAspect.advice);
      assertNotNull(InvocationAspect.invokeReturn);
      assertEquals(35, ((Integer) InvocationAspect.invokeReturn).intValue());
   }
}