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
package org.jboss.test.aop.jdk15.annotated;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AdviceFactory;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AnnotatedTestCase extends AOPTestWithSetup
{

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("AnnotatedTester");
      suite.addTestSuite(AnnotatedTestCase.class);
      return suite;
   }

   public AnnotatedTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      //This should go in the startService() method of the injboss testsuite
//	   testBinding();
//	   testCompostition();
//	   testMixin();
//	   testIntroduction();
//	   testInterceptorDef();
//	   testTypedef();
//	   testCFlow();
//	   testPrepare();
//	   testPrepareAtClassLevel(); 
//	   testDynamicCFlow();
//	   testAnnotationIntroduction();
//       testAspectFactory();
   }
   
   public void testBinding() throws Exception
   {
      System.out.println("***** testBinding() ****");
      AspectPerVM vm = null;
      AspectPerClass perClass = null;
      AspectPerClass perClass2 = null;
      
      try
      {
         System.out.println("---- POJO ---");
         POJO pojo = new POJO();
         pojo.field++;
         pojo.someMethod();
         System.out.println("---- POJO2 ---");
         POJO2 pojo2 = new POJO2();
         pojo2.field++;
         pojo2.someMethod();

         System.out.println("-- get stats --");
         vm = (AspectPerVM) AspectManager.instance().getPerVMAspect("org.jboss.test.aop.jdk15.annotated.AspectPerVM");
         System.out.println("perVM stats: " + vm.constructorCalled + " " + vm.methodCalled + " " + vm.fieldRead + " " + vm.fieldWrite);
         assertEquals(2, vm.constructorCalled);
         assertEquals(2, vm.methodCalled);
         assertEquals(2, vm.fieldRead);
         assertEquals(2, vm.fieldWrite);

         Advisor advisor = ((Advised) pojo)._getAdvisor();
         perClass = (AspectPerClass) advisor.getPerClassAspect("org.jboss.test.aop.jdk15.annotated.AspectPerClass");
         System.out.println("POJO perClass stats: " + perClass.constructorCalled + " " + perClass.methodCalled + " " + perClass.fieldRead + " " + perClass.fieldWrite);
         assertEquals(1, perClass.constructorCalled);
         assertEquals(1, perClass.methodCalled);
         assertEquals(1, perClass.fieldRead);
         assertEquals(1, perClass.fieldWrite);

         advisor = ((Advised) pojo2)._getAdvisor();
         perClass2 = (AspectPerClass) advisor.getPerClassAspect("org.jboss.test.aop.jdk15.annotated.AspectPerClass");
         System.out.println("POJO2 perClass stats: " + perClass.constructorCalled + " " + perClass.methodCalled + " " + perClass.fieldRead + " " + perClass.fieldWrite);
         assertEquals(1, perClass2.constructorCalled);
         assertEquals(1, perClass2.methodCalled);
         assertEquals(1, perClass2.fieldRead);
         assertEquals(1, perClass2.fieldWrite);

         InstanceAdvisor ia = ((Advised) pojo)._getInstanceAdvisor();
         AspectPerInstance perInstance = (AspectPerInstance) ia.getPerInstanceAspect("org.jboss.test.aop.jdk15.annotated.AspectPerInstance");
         System.out.println("pojo perInstance stats: " + perInstance.methodCalled + " " + perInstance.fieldRead + " " + perInstance.fieldWrite);
         assertEquals(1, perInstance.methodCalled);
         assertEquals(1, perInstance.fieldRead);
         assertEquals(1, perInstance.fieldWrite);

         ia = ((Advised) pojo2)._getInstanceAdvisor();
         perInstance = (AspectPerInstance) ia.getPerInstanceAspect("org.jboss.test.aop.jdk15.annotated.AspectPerInstance");
         System.out.println("pojo2 perInstance stats: " + perInstance.methodCalled + " " + perInstance.fieldRead + " " + perInstance.fieldWrite);
         assertEquals(1, perInstance.methodCalled);
         assertEquals(1, perInstance.fieldRead);
         assertEquals(1, perInstance.fieldWrite);
      } 
      finally
      {
         if (vm != null) vm.reset();
         if (perClass != null) perClass.reset();
         if (perClass2 != null) perClass2.reset();
      }
   }
   
   public void testCompostition() throws Exception
   {
      AspectPerVM vm = null;
      try
      {
         System.out.println("***** testCompostition() ****");
         System.out.println("---- AnotherPOJO ---");
         AnotherPOJO apojo = new AnotherPOJO();
         apojo.field++;
         apojo.someMethod();

         vm = (AspectPerVM) AspectManager.instance().getPerVMAspect("org.jboss.test.aop.jdk15.annotated.AspectPerVM");
         assertEquals(4, vm.anotherPOJOAccess);
      } 
      finally
      {
         if (vm != null) vm.reset();
      }
   }
   
   public void testMixin() throws Exception
   {
      System.out.println("***** testMixin() ****");
      ExternalizableMixin.write = false;
      ExternalizableMixin.read = false;     
      NoInterfacesPOJO pojo = new NoInterfacesPOJO();

      pojo.stuff = "hello world";
      java.rmi.MarshalledObject mo = new java.rmi.MarshalledObject(pojo);
      pojo = (NoInterfacesPOJO)mo.get();
      System.out.println("deserialized pojo2.stuff2: " + pojo.stuff);
      assertTrue("writeExternal was not called", ExternalizableMixin.write);
      assertTrue("readExternal was not called", ExternalizableMixin.read);

      ExternalizableMixin.write = false;
      ExternalizableMixin.read = false;
      NoInterfacesPOJO2 pojo2 = new NoInterfacesPOJO2();

      pojo2.stuff = "whatever";
      java.rmi.MarshalledObject mo2 = new java.rmi.MarshalledObject(pojo2);
      pojo2 = (NoInterfacesPOJO2)mo2.get();
      System.out.println("deserialized pojo2.stuff2: " + pojo2.stuff);
      assertTrue("writeExternal was not called for pojo2", ExternalizableMixin.write);
      assertTrue("readExternal was not called for pojo2", ExternalizableMixin.read);
   
   }

   public void testIntroduction() throws Exception
   {
      System.out.println("***** testIntroduction() ****");
      NoInterfacesPOJO pojo = new NoInterfacesPOJO();
      
      try
      {
         EmptyInterface eif = (EmptyInterface)pojo;
      }
      catch(Exception e)
      {
         throw new RuntimeException("pojo does not implement EmptyInterface");
      }

      NoInterfacesPOJO2 pojo2 = new NoInterfacesPOJO2();
      
      
      try
      {
         EmptyInterface eif = (EmptyInterface)pojo2;
      }
      catch(Exception e)
      {
         throw new RuntimeException("pojo2 does not implement EmptyInterface");
      }
      
   }

   public void testInterceptorDef()throws Exception
   {
      System.out.println("***** testInterceptorDef() ****");
      
      CountingInterceptor.count = 0;
      VariaPOJO pojo = new VariaPOJO();
      pojo.methodWithInterceptor();
      System.out.println("Count: " + CountingInterceptor.count);
      assertEquals("execution of POJO.methodWithInterceptor() was not intercepted", 1, CountingInterceptor.count);

      CountingInterceptor.count = 0;
      pojo.methodWithInterceptorFactory();
      System.out.println("Count: " + CountingInterceptor.count);
      assertEquals("execution of POJO.methodWithInterceptorFactory() was not intercepted", 1, CountingInterceptor.count);
   }

   public void testTypedef()throws Exception
   {
      System.out.println("***** testTypedef() ****");
      
      VariaPOJO pojo = new VariaPOJO();
      pojo.methodWithTypedef();
      System.out.println("Intercepted: " + TypedefAspect.intercepted);
      assertTrue("execution of POJO.methodWithTypedef() was not intercepted", TypedefAspect.intercepted);
   }

   public void testCFlow()throws Exception
   {
      System.out.println("***** testCFlow() ****");
      
      CFlowAspect.cflowAccess = 0;
      
      VariaPOJO pojo = new VariaPOJO();
      pojo.cflowMethod1();
      assertEquals("Wrong number of interceptions 1) for cflow Advice", 1, CFlowAspect.cflowAccess);
      
      CFlowAspect.cflowAccess = 0;
      pojo.cflowMethod2();
      System.out.println("ints: " + CFlowAspect.cflowAccess);
      assertEquals("Wrong number of interceptions 2) for cflow Advice", 1, CFlowAspect.cflowAccess );
      
   }
   
   public void testPrepare()throws Exception
   {
      System.out.println("***** testPrepare() ****");
      PreparePOJO pojo = new PreparePOJO();
      pojo.someMethod();
      
      Advised advised = (Advised)pojo;
      Advisor advisor = advised._getAdvisor();      
   }
   
   public void testPrepareAtClassLevel() throws Exception
   {
      System.out.println("***** testPrepareAtClassLevel() ****");
      PreparedPOJO pojo = new PreparedPOJO();
      pojo.someMethod();
      
      Advised advised = (Advised)pojo;
      Advisor advisor = advised._getAdvisor();
   }

   public void testDynamicCFlow()throws Exception
   {
      System.out.println("***** testDynamicCFlow() ****");
      
      CFlowAspect.cflowAccess = 0;
      
      VariaPOJO pojo = new VariaPOJO();
      pojo.dynamicCFlowMethod();
      assertEquals("Wrong number of interceptions for dynamic cflow Advice", 0, CFlowAspect.cflowAccess);
      
      SimpleDynamicCFlow.execute = true;
      pojo.dynamicCFlowMethod();
      assertEquals("Wrong number of interceptions for dynamic cflow Advice", 1, CFlowAspect.cflowAccess);
      
      SimpleDynamicCFlow.execute = false;
      pojo.dynamicCFlowMethod();
      assertEquals("Wrong number of interceptions for dynamic cflow Advice (2)", 1, CFlowAspect.cflowAccess);
   }
   
   public void testAnnotationIntroduction() throws Exception
   {
      System.out.println("***** testAnnotationIntroduction() ****");
      IntroducedAnnotationPOJO pojo = new IntroducedAnnotationPOJO();
      assertNull("IntroducedAnnotationPOJO should not have had a constructor annotation", IntroducedAnnotationInterceptor.lastMyAnnotation);
      
      pojo.annotationIntroductionMethod();
      MyAnnotation annotation = IntroducedAnnotationInterceptor.lastMyAnnotation;
      assertNotNull("IntroducedAnnotationPOJO.annotationIntroductionMethod() should have had a method annotation", annotation);
      assertEquals("Wrong value for MyAnnotation.string()", "hello", annotation.string());
      assertEquals("Wrong value for MyAnnotation.integer()", 5, annotation.integer());
      assertEquals("Wrong value for MyAnnotation.bool()", true, annotation.bool());
      pojo.noAnnotationIntroductionMethod();
      assertNull("IntroducedAnnotationPOJO.noAnnotationIntroductionMethod() should not have had a method annotation", IntroducedAnnotationInterceptor.lastMyAnnotation);
   }

   public void testPrecedence() throws Exception
   {
      System.out.println("***** testPrecedence() ****");
      VariaPOJO pojo = new VariaPOJO();
      
      pojo.precedenceMethod();
      String[] expected = {"PrecedenceInterceptor1", "PrecedenceAspect1.advice1", "PrecedenceAspect1.advice2", "PrecedenceInterceptor2"};
      java.util.ArrayList intercepted = Interceptions.intercepted;
      assertEquals("Wrong number of interceptions", expected.length ,intercepted.size());
      
      for (int i = 0 ; i < expected.length ; i++)
      {
         assertEquals("Wrong interception at index " + i, expected[i], (String)intercepted.get(i));
      }
   }
   
   /**
    * Tests the annotation of an aspect factory as @Aspect.
    */
   public void testAspectFactory() throws Exception
   {
      AdviceBinding binding = new AdviceBinding(
            "execution(void *PreparedPOJO->someMethod(..))", null);
      AspectDefinition aspectDefinition = AspectManager.instance()
            .getAspectDefinition(AnnotatedAspectFactory.class.getName());
      assertNotNull(aspectDefinition);
      binding.addInterceptorFactory(new AdviceFactory(aspectDefinition,
            "advice"));
      AspectManager.instance().addBinding(binding);

      PreparedPOJO pojo = new PreparedPOJO();
      pojo.someMethod();
      assertTrue(AnnotatedAspectFactory.isAspectCreated());
      assertTrue(AnnotatedAspectFactory.getAspectCreated().isAdvised());
      AspectManager.instance().removeBinding(binding.getName());
   }
}