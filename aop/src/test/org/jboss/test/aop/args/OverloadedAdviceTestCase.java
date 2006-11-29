package org.jboss.test.aop.args;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests the selection of advice methods when these are overloaded.
 * 
 * @author Flavia Rainone
 */
public class OverloadedAdviceTestCase extends AOPTestWithSetup
{
   private OverloadedAdvicePOJO pojo;
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("OverloadedAdviceTestCase");
      suite.addTestSuite(OverloadedAdviceTestCase.class);
      return suite;
   }
   
   public OverloadedAdviceTestCase(String name)
   {
      super(name);
   }
   
   public void setUp() throws Exception
   {
      super.setUp();
      OverloadedAdviceAspect.clear();
      this.pojo = new OverloadedAdvicePOJO();
   }
   
   public void test1()
   {
      pojo.method(10, 15);
      assertEquals("defaultSignature", OverloadedAdviceAspect.around1);
      assertEquals("MethodInvocation,int,long", OverloadedAdviceAspect.around2);
      assertEquals("Invocation,int,long", OverloadedAdviceAspect.around3);
      assertEquals("Object,int,long", OverloadedAdviceAspect.around4);
      assertTrue(OverloadedAdviceAspect.around5.startsWith("MethodInvocation,"));
      assertTrue(OverloadedAdviceAspect.around5.equals("MethodInvocation,int") ||
            OverloadedAdviceAspect.around5.equals("MethodInvocation,long"));
      assertTrue(OverloadedAdviceAspect.around6.startsWith("Invocation,"));
      assertTrue(OverloadedAdviceAspect.around6.equals("Invocation,int") ||
            OverloadedAdviceAspect.around6.equals("Invocation,long"));
      assertTrue(OverloadedAdviceAspect.around7.startsWith("Object,"));
      assertTrue(OverloadedAdviceAspect.around7.equals("Object,int") ||
            OverloadedAdviceAspect.around7.equals("Object,long"));
      assertEquals("MethodInvocation", OverloadedAdviceAspect.around8);
      assertEquals("Object", OverloadedAdviceAspect.around9);
      assertEquals("int,long", OverloadedAdviceAspect.around10);
      assertTrue(OverloadedAdviceAspect.around11.equals("int") ||
            OverloadedAdviceAspect.around11.equals("long"));
      assertEquals("", OverloadedAdviceAspect.around12);
   }
   
   public void test2()
   {
      pojo.text = "test2";
      assertEquals("FieldInfo,String", OverloadedAdviceAspect.before1);
      assertEquals("FieldInfo,Object", OverloadedAdviceAspect.before2);
      assertEquals("JoinPointInfo,String", OverloadedAdviceAspect.before3);
      assertEquals("JoinPointInfo,Object", OverloadedAdviceAspect.before4);
      assertEquals("Object,String", OverloadedAdviceAspect.before5);
      assertEquals("Object,Object", OverloadedAdviceAspect.before6);
      assertEquals("FieldInfo", OverloadedAdviceAspect.before7);
      assertEquals("JoinPointInfo", OverloadedAdviceAspect.before8);
      assertEquals("Object", OverloadedAdviceAspect.before9);
      assertEquals("String", OverloadedAdviceAspect.before10);
      assertEquals("Object", OverloadedAdviceAspect.before11);
      assertEquals("", OverloadedAdviceAspect.before12);
   }
}