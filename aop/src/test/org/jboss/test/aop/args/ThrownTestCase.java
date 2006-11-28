package org.jboss.test.aop.args;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests parameter annotation @Thrown. 
 * 
 * @author Flavia Rainone
 *
 */
public class ThrownTestCase extends AOPTestWithSetup
{
   private ThrownPOJO pojo;

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ThrownTestCase");
      suite.addTestSuite(ThrownTestCase.class);
      return suite;
   }

   public ThrownTestCase(String name)
   {
      super(name);
   }

   public void setUp() throws Exception
   {
      super.setUp();
      ThrownAspect.clear();
      this.pojo = new ThrownPOJO();
   }

   public void test1()
   {
      boolean thrown = false;
      try
      {
         pojo.method1(11);
      }
      catch(POJOException pojoException)
      {
         thrown = true;
         assertEquals(11, pojoException.number);
         assertNull(ThrownAspect.advice);
         assertNull(ThrownAspect.thrown);
         assertEquals(0, ThrownAspect.thrownNumber);
      }
      assertTrue(thrown);
   }

   public void test2()
   {
      boolean thrown = false;
      try
      {
         pojo.method2(23);
      }
      catch(POJOException pojoException)
      {
         thrown = true;
         assertEquals(23, pojoException.number);
         assertEquals("throwing2", ThrownAspect.advice);
         assertEquals(pojoException, ThrownAspect.thrown);
         assertEquals(23, ThrownAspect.thrownNumber);
      }
      assertTrue(thrown);
   }
   
   public void test3()
   {
      boolean thrown = false;
      try
      {
         pojo.method3(37);
      }
      catch(POJOException pojoException)
      {
         thrown = true;
         assertEquals(37, pojoException.number);
         assertNull(ThrownAspect.advice);
         assertNull(ThrownAspect.thrown);
         assertEquals(0, ThrownAspect.thrownNumber);
         /*assertEquals("throwing3", ThrownAspect.advice);
         assertEquals(pojoException, ThrownAspect.thrown);
         assertEquals(37, ThrownAspect.thrownNumber);*/
      }
      assertTrue(thrown);
   }
   
   public void test4()
   {
      boolean thrown = false;
      try
      {
         pojo.method4(43);
      }
      catch(POJOException pojoException)
      {
         thrown = true;
         assertEquals(43, pojoException.number);
         assertNull(ThrownAspect.advice);
         assertNull(ThrownAspect.thrown);
         assertEquals(0, ThrownAspect.thrownNumber);
         /*assertEquals("throwing4", ThrownAspect.advice);
         assertEquals(pojoException, ThrownAspect.thrown);
         assertEquals(43, ThrownAspect.thrownNumber);*/
      }
      assertTrue(thrown);
   }
   
   public void test5()
   {
      boolean thrown = false;
      try
      {
         pojo.method5(59);
      }
      catch(POJOException pojoException)
      {
         thrown = true;
         assertEquals(59, pojoException.number);
         assertNull(ThrownAspect.advice);
         assertNull(ThrownAspect.thrown);
         assertEquals(0, ThrownAspect.thrownNumber);
      }
      assertTrue(thrown);
   }
   
   public void test6()
   {
      boolean thrown = false;
      try
      {
         pojo.method6();
      }
      catch(POJOException pojoException)
      {
         thrown = true;
         assertEquals(6, pojoException.number);
         assertNull(ThrownAspect.advice);
         assertNull(ThrownAspect.thrown);
         assertEquals(0, ThrownAspect.thrownNumber);
      }
      assertTrue(thrown);
   }
}