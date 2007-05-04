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

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Tests the selection of advice methods when these are overloaded.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
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
      this.pojo = new OverloadedAdvicePOJO();
   }

   public void testBefore()
   {
      // clear all relevant aspect fields
      OverloadedBeforeAspect.clear();
      // execute the join point
      pojo.text = "test2";
      // check aspect fields
      assertEquals("FieldInfo,String", OverloadedBeforeAspect.before1);
      assertEquals("FieldInfo,Object", OverloadedBeforeAspect.before2);
      assertEquals("JoinPointInfo,String", OverloadedBeforeAspect.before3);
      assertEquals("JoinPointInfo,Object", OverloadedBeforeAspect.before4);
      assertEquals("Object,String", OverloadedBeforeAspect.before5);
      assertEquals("Object,Object", OverloadedBeforeAspect.before6);
      assertEquals("FieldInfo,Object[]", OverloadedBeforeAspect.before7);
      assertEquals("JoinPointInfo,Object[]", OverloadedBeforeAspect.before8);
      assertEquals("Object,Object[]", OverloadedBeforeAspect.before9);
      assertEquals("FieldInfo", OverloadedBeforeAspect.before10);
      assertEquals("JoinPointInfo", OverloadedBeforeAspect.before11);
      assertEquals("Object", OverloadedBeforeAspect.before12);
      assertEquals("String", OverloadedBeforeAspect.before13);
      assertEquals("Object", OverloadedBeforeAspect.before14);
      assertEquals("Object[]", OverloadedBeforeAspect.before15);
      assertEquals("", OverloadedBeforeAspect.before16);
      assertEquals("FieldInfo,String", OverloadedBeforeAspect.before17);
      OverloadedBeforeAspect.clear();
      String myText = pojo.text;
      assertEquals("FieldInfo", OverloadedBeforeAspect.before17);
      
   }
   
   public void testAround()
   {
      // clear all relevant aspect fields
      OverloadedAroundAspect.clear();
      // execute the join point
      pojo.method1(10, 15);
      // check aspect fields
      assertEquals("defaultSignature", OverloadedAroundAspect.around1);
      assertEquals("MethodInvocation,int,long", OverloadedAroundAspect.around2);
      assertEquals("Invocation,int,long", OverloadedAroundAspect.around3);
      assertEquals("Object,int,long", OverloadedAroundAspect.around4);
      assertTrue(OverloadedAroundAspect.around5.startsWith("MethodInvocation,"));
      assertTrue(OverloadedAroundAspect.around5.equals("MethodInvocation,int") ||
            OverloadedAroundAspect.around5.equals("MethodInvocation,long"));
      assertTrue(OverloadedAroundAspect.around6.startsWith("Invocation,"));
      assertTrue(OverloadedAroundAspect.around6.equals("Invocation,int") ||
            OverloadedAroundAspect.around6.equals("Invocation,long"));
      assertTrue(OverloadedAroundAspect.around7.startsWith("Object,"));
      assertTrue(OverloadedAroundAspect.around7.equals("Object,int") ||
            OverloadedAroundAspect.around7.equals("Object,long"));
      assertEquals("MethodInvocation,Object[]", OverloadedAroundAspect.around8);
      assertEquals("Invocation,Object[]", OverloadedAroundAspect.around9);
      assertEquals("Object,Object[]", OverloadedAroundAspect.around10);
      assertEquals("MethodInvocation", OverloadedAroundAspect.around11);
      assertEquals("Object", OverloadedAroundAspect.around12);
      assertEquals("int,long", OverloadedAroundAspect.around13);
      assertTrue(OverloadedAroundAspect.around14.equals("int") ||
            OverloadedAroundAspect.around14.equals("long"));
      assertEquals("Object[]", OverloadedAroundAspect.around15);
      assertEquals("", OverloadedAroundAspect.around16);
   }
   
   public void testAfter()
   {
      // clear all relevant aspect fields
      OverloadedAfterAspect.clear();
      // execute the join point
      pojo.method2(0, null);
      // check aspect fields
      assertEquals("Object,MethodInfo,SuperClass,float,SubValue",
            OverloadedAfterAspect.after1);
      assertEquals("Object,MethodInfo,SuperClass,float,SuperValue",
            OverloadedAfterAspect.after2);
      assertEquals("Object,MethodInfo,SuperClass,float,Object",
            OverloadedAfterAspect.after3);
      assertEquals("Object,MethodInfo,Object,float,SubValue",
            OverloadedAfterAspect.after4);
      assertEquals("Object,MethodInfo,Object,float,SuperValue",
            OverloadedAfterAspect.after5);
      assertEquals("Object,MethodInfo,Object,float,Object",
            OverloadedAfterAspect.after6);
      assertEquals("Object,JoinPointInfo,SuperClass,float,SubValue",
            OverloadedAfterAspect.after7);
      assertEquals("SuperClass,MethodInfo,SuperClass,SubValue",
            OverloadedAfterAspect.after8);
      assertEquals("Object,MethodInfo,SuperClass,float",
            OverloadedAfterAspect.after9);
      assertEquals("Object,MethodInfo,SuperClass,SuperValue",
            OverloadedAfterAspect.after10);
      assertEquals("SuperClass,MethodInfo,SuperClass,Object",
            OverloadedAfterAspect.after11);
      assertEquals("SuperClass,MethodInfo,Object,SubValue",
            OverloadedAfterAspect.after12);
      assertEquals("Object,MethodInfo,Object,float",
            OverloadedAfterAspect.after13);
      assertEquals("SubClass,MethodInfo,Object,SuperValue",
            OverloadedAfterAspect.after14);
      assertEquals("SubClass,MethodInfo,Object,Object",
            OverloadedAfterAspect.after15);
      assertEquals("SuperClass,JoinPointInfo,SuperClass,SubValue",
            OverloadedAfterAspect.after16);
      assertEquals("SubClass,JoinPointInfo,SuperClass,float",
            OverloadedAfterAspect.after17);
      assertEquals("SubClass,JoinPointInfo,SuperClass,SuperValue",
            OverloadedAfterAspect.after18);
      assertEquals("SuperClass,JoinPointInfo,SuperClass,Object",
            OverloadedAfterAspect.after19);
      assertEquals("SubClass,Object,SuperClass,SubValue",
            OverloadedAfterAspect.after20);
      assertEquals("Object,Object,SuperClass,float", OverloadedAfterAspect.after21);
      assertEquals("SubClass,Object,SuperClass,SuperValue",
            OverloadedAfterAspect.after22);
      assertEquals("Object,Object,SuperClass,Object", OverloadedAfterAspect.after23);
      assertEquals("Object,MethodInfo,SuperClass,Object[]",
            OverloadedAfterAspect.after24);
      assertEquals("Object,MethodInfo,Object,Object[]",
            OverloadedAfterAspect.after25);
      assertEquals("Object,JoinPointInfo,SuperClass,Object[]",
            OverloadedAfterAspect.after26);
      assertEquals("Object,JoinPointInfo,Object,Object[]",
            OverloadedAfterAspect.after27);
      assertEquals("Object,Object,SuperClass,Object[]",
            OverloadedAfterAspect.after28);
      assertEquals("Object,Object,Object,Object[]", OverloadedAfterAspect.after29);
      assertEquals("Object,MethodInfo,SuperClass", OverloadedAfterAspect.after30);
      assertEquals("Object,MethodInfo,Object", OverloadedAfterAspect.after31);
      assertEquals("Object,JoinPointInfo,SuperClass", OverloadedAfterAspect.after32);
      assertEquals("Object,MethodInfo,float,SubValue",
            OverloadedAfterAspect.after33);
      assertEquals("Object,MethodInfo,float,SuperValue",
            OverloadedAfterAspect.after34);
      assertEquals("Object,MethodInfo,float,Object", OverloadedAfterAspect.after35);
      assertEquals("Object,JoinPointInfo,float,SubValue",
            OverloadedAfterAspect.after36);
      assertEquals("SuperClass,MethodInfo,SubValue", OverloadedAfterAspect.after37);
      assertEquals("Object,MethodInfo,float", OverloadedAfterAspect.after38);
      assertEquals("Object,MethodInfo,SuperValue", OverloadedAfterAspect.after39);
      assertEquals("SuperClass,JoinPointInfo,SubValue",
            OverloadedAfterAspect.after40);
      assertEquals("SubClass,JoinPointInfo,float", OverloadedAfterAspect.after41);
      assertEquals("SubClass,JoinPointInfo,SuperValue",
            OverloadedAfterAspect.after42);
      assertEquals("SubClass,Object,SubValue", OverloadedAfterAspect.after43);
      assertEquals("Object,Object,float", OverloadedAfterAspect.after44);
      assertEquals("SubClass,Object,SuperValue", OverloadedAfterAspect.after45);
      assertEquals("SubClass,MethodInfo,Object[]", OverloadedAfterAspect.after46);
      assertEquals("SubClass,JoinPointInfo,Object[]", OverloadedAfterAspect.after47);
      assertEquals("Object,MethodInfo", OverloadedAfterAspect.after48);
      assertEquals("Object,JoinPointInfo", OverloadedAfterAspect.after49);
      assertEquals("Object,SuperClass,float,SubValue",
            OverloadedAfterAspect.after50);
      assertEquals("Object,SuperClass,float,SuperValue",
            OverloadedAfterAspect.after51);
      assertEquals("Object,SuperClass,float,Object", OverloadedAfterAspect.after52);
      assertEquals("SuperClass,SuperClass,float", OverloadedAfterAspect.after53);
      assertEquals("Object,SuperClass,SubValue", OverloadedAfterAspect.after54);
      assertEquals("Object,SuperClass,SuperValue", OverloadedAfterAspect.after55);
      assertEquals("SuperClass,SuperClass,Object", OverloadedAfterAspect.after56);
      assertEquals("Object,SuperClass,Object[]", OverloadedAfterAspect.after57);
      assertEquals("Object,Object,Object[]", OverloadedAfterAspect.after58);
      assertEquals("Object,SuperClass", OverloadedAfterAspect.after59);
      assertEquals("Object,Object", OverloadedAfterAspect.after60);
      assertEquals("void,float,SubValue", OverloadedAfterAspect.after61);
      assertEquals("Object,float", OverloadedAfterAspect.after62);
      assertEquals("void,SubValue", OverloadedAfterAspect.after63);
      assertEquals("void,SuperValue", OverloadedAfterAspect.after64);
      assertEquals("void,Object[]", OverloadedAfterAspect.after65);
      assertEquals("void", OverloadedAfterAspect.after66);
   }
   
   public void testAfterThrowing()
   {
      // clear all relevant aspect fields
      OverloadedThrowingAspect.clear();
      // execute the join point
      try
      {
         new OverloadedAdvicePOJO(null, null);
      } catch (POJOException pe) {}
      
      // check aspect fields
      
      assertEquals("JoinPointInfo,Throwable,SubInterface,Implementor",
            OverloadedThrowingAspect.throwing1);
      
      assertTrue(OverloadedThrowingAspect.throwing2.startsWith(
            "JoinPointInfo,Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing2.equals(
            "JoinPointInfo,Throwable,Interface,Implementor") ||
            OverloadedThrowingAspect.throwing2.equals(
            "JoinPointInfo,Throwable,SubInterface,SubInterface") ||
            OverloadedThrowingAspect.throwing2.equals(
            "JoinPointInfo,Throwable,SubInterface,Object"));
      
      assertTrue(OverloadedThrowingAspect.throwing3.startsWith(
            "JoinPointInfo,Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing3.equals(  
            "JoinPointInfo,Throwable,SuperInterface,Implementor") ||
            OverloadedThrowingAspect.throwing3.equals(
            "JoinPointInfo,Throwable,SubInterface,Interface") ||
            OverloadedThrowingAspect.throwing3.equals(
            "JoinPointInfo,Throwable,Interface,SubInterface"));
      
      assertTrue(OverloadedThrowingAspect.throwing4.startsWith(
            "JoinPointInfo,Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing4.equals(
            "JoinPointInfo,Throwable,SuperInterface,SubInterface") ||
            OverloadedThrowingAspect.throwing4.equals(
            "JoinPointInfo,Throwable,SuperInterface,Object") ||
            OverloadedThrowingAspect.throwing4.equals(
            "JoinPointInfo,Throwable,Interface,Interface") ||
            OverloadedThrowingAspect.throwing4.equals(
            "JoinPointInfo,Throwable,SubInterface,SuperInterface"));
      
      assertTrue(OverloadedThrowingAspect.throwing5.startsWith(
            "JoinPointInfo,Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing5.equals(
            "JoinPointInfo,Throwable,SuperInterface,Interface") ||
            OverloadedThrowingAspect.throwing5.equals(
            "JoinPointInfo,Throwable,Interface,SuperInterface"));
      
      assertEquals("JoinPointInfo,Throwable,SuperInterface,SuperInterface",
            OverloadedThrowingAspect.throwing6);
      
      assertEquals("JoinPointInfo,Object,SubInterface,Implementor",
            OverloadedThrowingAspect.throwing7);
      
      assertTrue(OverloadedThrowingAspect.throwing8.startsWith(
            "JoinPointInfo,Object,"));
      assertTrue(OverloadedThrowingAspect.throwing8.equals(
            "JoinPointInfo,Object,Interface,Implementor") ||
            OverloadedThrowingAspect.throwing8.equals(
            "JoinPointInfo,Object,SubInterface,SubInterface") ||
            OverloadedThrowingAspect.throwing8.equals(
            "JoinPointInfo,Object,SubInterface,Object"));

      assertTrue(OverloadedThrowingAspect.throwing9.startsWith(
            "JoinPointInfo,Object,"));
      assertTrue(OverloadedThrowingAspect.throwing9.equals(  
            "JoinPointInfo,Object,SuperInterface,Implementor") ||
            OverloadedThrowingAspect.throwing9.equals(
            "JoinPointInfo,Object,SubInterface,Interface") ||
            OverloadedThrowingAspect.throwing9.equals(
            "JoinPointInfo,Object,Interface,SubInterface"));

      assertTrue(OverloadedThrowingAspect.throwing10.startsWith(
            "JoinPointInfo,Object,"));
      assertTrue(OverloadedThrowingAspect.throwing10.equals(
            "JoinPointInfo,Object,SuperInterface,SubInterface") ||
            OverloadedThrowingAspect.throwing10.equals(
            "JoinPointInfo,Object,SuperInterface,Object") ||
            OverloadedThrowingAspect.throwing10.equals(
            "JoinPointInfo,Object,Interface,Interface") ||
            OverloadedThrowingAspect.throwing10.equals(
            "JoinPointInfo,Object,SubInterface,SuperInterface"));
      
      assertTrue(OverloadedThrowingAspect.throwing11.startsWith(
            "JoinPointInfo,Object,"));
      assertTrue(OverloadedThrowingAspect.throwing11.equals(
            "JoinPointInfo,Object,SuperInterface,Interface") ||
            OverloadedThrowingAspect.throwing11.equals(
            "JoinPointInfo,Object,Interface,SuperInterface"));

      assertEquals("JoinPointInfo,Object,SuperInterface,SuperInterface",
               OverloadedThrowingAspect.throwing12);
      
      assertTrue(OverloadedThrowingAspect.throwing13.startsWith(
            "JoinPointInfo,Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing13.equals(
            "JoinPointInfo,Throwable,SubInterface") ||
            OverloadedThrowingAspect.throwing13.equals(
            "JoinPointInfo,Throwable,Implementor"));
      
      assertTrue(OverloadedThrowingAspect.throwing14.startsWith(
            "JoinPointInfo,Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing14.equals(
            "JoinPointInfo,Throwable,Interface") ||
            OverloadedThrowingAspect.throwing14.equals(
            "JoinPointInfo,Throwable,Object"));
      
      assertEquals("JoinPointInfo,Throwable,SuperInterface",
            OverloadedThrowingAspect.throwing15);
      
      assertEquals("JoinPointInfo,Throwable,Object[]",
            OverloadedThrowingAspect.throwing16);
      
      assertEquals("JoinPointInfo,Object,Object[]",
            OverloadedThrowingAspect.throwing17);
      
      assertEquals("Throwable,SubInterface,Implementor",
            OverloadedThrowingAspect.throwing18);
      
      assertTrue(OverloadedThrowingAspect.throwing19.startsWith("Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing19.equals(
            "Throwable,Interface,Implementor") ||
            OverloadedThrowingAspect.throwing19.equals(
            "Throwable,SubInterface,SubInterface") ||
            OverloadedThrowingAspect.throwing19.equals(
            "Throwable,SubInterface,Object"));
      
      assertTrue(OverloadedThrowingAspect.throwing20.startsWith("Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing20.equals(  
            "Throwable,SuperInterface,Implementor") ||
            OverloadedThrowingAspect.throwing20.equals(
            "Throwable,SubInterface,Interface") ||
            OverloadedThrowingAspect.throwing20.equals(
            "Throwable,Interface,SubInterface"));
      
      assertTrue(OverloadedThrowingAspect.throwing21.startsWith("Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing21.equals(
            "Throwable,SuperInterface,SubInterface") ||
            OverloadedThrowingAspect.throwing21.equals(
            "Throwable,SuperInterface,Object") ||
            OverloadedThrowingAspect.throwing21.equals(
            "Throwable,Interface,Interface") ||
            OverloadedThrowingAspect.throwing21.equals(
            "Throwable,SubInterface,SuperInterface"));

      assertTrue(OverloadedThrowingAspect.throwing22.startsWith("Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing22.equals(
            "Throwable,SuperInterface,Interface") ||
            OverloadedThrowingAspect.throwing22.equals(
            "Throwable,Interface,SuperInterface"));

      assertEquals("Throwable,SuperInterface,SuperInterface",
            OverloadedThrowingAspect.throwing23);

      assertEquals("Object,SubInterface,Implementor",
            OverloadedThrowingAspect.throwing24);
      
      assertTrue(OverloadedThrowingAspect.throwing25.startsWith("Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing25.equals("Throwable,SubInterface")
            || OverloadedThrowingAspect.throwing25.equals("Throwable,Implementor"));

      assertTrue(OverloadedThrowingAspect.throwing26.startsWith("Throwable,"));
      assertTrue(OverloadedThrowingAspect.throwing26.equals("Throwable,Interface") ||
         OverloadedThrowingAspect.throwing26.equals("Throwable,Object"));

      assertEquals("Throwable,SuperInterface", OverloadedThrowingAspect.throwing27);
      
      assertTrue(OverloadedThrowingAspect.throwing28.startsWith("Object,"));
      assertTrue(OverloadedThrowingAspect.throwing28.equals("Object,SubInterface")
            || OverloadedThrowingAspect.throwing28.equals("Object,Implementor"));

      assertTrue(OverloadedThrowingAspect.throwing29.startsWith("Object,"));
      assertTrue(OverloadedThrowingAspect.throwing29.equals("Object,Interface") ||
         OverloadedThrowingAspect.throwing29.equals("Object,Object"));

      assertEquals("Object,SuperInterface", OverloadedThrowingAspect.throwing30);
      
      assertEquals("Throwable,Object[]", OverloadedThrowingAspect.throwing31);
      
      assertEquals("Object,Object[]", OverloadedThrowingAspect.throwing32);
      
      assertEquals("Throwable", OverloadedThrowingAspect.throwing33);
      
      assertEquals("Object", OverloadedThrowingAspect.throwing34);
   }
   
   public void testAfterThrowing2()
   {
      // clear all relevant aspect fields
      OverloadedThrowingAspect.clear();
      // execute the join point
      try
      {
         new OverloadedAdvicePOJO(null, null, 0);
      } catch (POJOException pe) {}
      
      // check aspect fields
      
      assertEquals("Throwable,SuperInterface", OverloadedThrowingAspect.throwing35);
      assertEquals("Throwable,Object", OverloadedThrowingAspect.throwing36);
      assertNotNull(OverloadedThrowingAspect.throwing37); // it can choose any advice
      assertEquals("Throwable,Object", OverloadedThrowingAspect.throwing38);
   }
   
   public void testFinally() throws Exception
   {
      // clear all relevant aspect fields
      OverloadedFinallyAspect.clear();
      
      // execute the join point
      assertEquals("finally69", pojo.method5(0, 1));
      
      // check aspect fields
      assertEquals("void,MethodInfo,String,Throwable,int,long",
            OverloadedFinallyAspect.finally1);
      assertEquals("void,MethodInfo,String,Serializable,int,long",
            OverloadedFinallyAspect.finally2);
      assertEquals("void,MethodInfo,CharSequence,Throwable,int,long",
            OverloadedFinallyAspect.finally3);
      assertEquals("String,MethodInfo,String,Throwable,int",
            OverloadedFinallyAspect.finally4);
      assertEquals("void,MethodInfo,String,Throwable,long",
            OverloadedFinallyAspect.finally5);
      assertEquals("String,MethodInfo,String,Serializable,long",
            OverloadedFinallyAspect.finally6);
      assertEquals("void,MethodInfo,String,Serializable,int",
            OverloadedFinallyAspect.finally7);
      assertEquals("Object,MethodInfo,CharSequence,Throwable,int",
            OverloadedFinallyAspect.finally8);
      assertEquals("void,MethodInfo,CharSequence,Throwable,long",
            OverloadedFinallyAspect.finally9);
      assertEquals("void,MethodInfo,String,Throwable,Object[]",
            OverloadedFinallyAspect.finally10);
      assertEquals("void,MethodInfo,String,Throwable,Object",
            OverloadedFinallyAspect.finally11);
      assertEquals("void,MethodInfo,String,Serializable,Object[]",
            OverloadedFinallyAspect.finally12);
      assertEquals("void,MethodInfo,String,Serializable,Object",
            OverloadedFinallyAspect.finally13);
      assertEquals("void,MethodInfo,CharSequence,Throwable,Object[]",
            OverloadedFinallyAspect.finally14);
      assertEquals("void,MethodInfo,CharSequence,Throwable,Object",
            OverloadedFinallyAspect.finally15);
      assertEquals("void,MethodInfo,String,Throwable",
            OverloadedFinallyAspect.finally16);
      assertEquals("void,MethodInfo,String,Serializable",
            OverloadedFinallyAspect.finally17);
      assertEquals("void,MethodInfo,CharSequence,Throwable",
            OverloadedFinallyAspect.finally18);
      assertEquals("void,MethodInfo,Throwable,int,long",
            OverloadedFinallyAspect.finally19);
      assertEquals("void,MethodInfo,Serializable,int,long",
            OverloadedFinallyAspect.finally20);
      assertEquals("Object,MethodInfo,Throwable,long",
            OverloadedFinallyAspect.finally21);
      assertEquals("void,MethodInfo,Throwable,int",
            OverloadedFinallyAspect.finally22);
      assertEquals("String,MethodInfo,Serializable,int",
            OverloadedFinallyAspect.finally23);
      assertEquals("Object,MethodInfo,Serializable,long",
            OverloadedFinallyAspect.finally24);
      assertEquals("void,MethodInfo,Throwable,Object[]",
            OverloadedFinallyAspect.finally25);
      assertEquals("void,MethodInfo,Throwable,Object",
            OverloadedFinallyAspect.finally26);
      assertEquals("void,MethodInfo,Serializable,Object[]",
            OverloadedFinallyAspect.finally27);
      assertEquals("void,MethodInfo,Serializable,Object",
            OverloadedFinallyAspect.finally28);
      assertEquals("void,MethodInfo,Throwable", OverloadedFinallyAspect.finally29);
      assertEquals("void,MethodInfo,Serializable",
            OverloadedFinallyAspect.finally30);
      assertEquals("void,MethodInfo,int,long", OverloadedFinallyAspect.finally31);
      assertEquals("String,MethodInfo,long", OverloadedFinallyAspect.finally32);
      assertEquals("Object,MethodInfo,int", OverloadedFinallyAspect.finally33);
      assertEquals("void,MethodInfo,Object[]", OverloadedFinallyAspect.finally34);
      assertEquals("void,MethodInfo,Object", OverloadedFinallyAspect.finally35);
      assertEquals("void,MethodInfo", OverloadedFinallyAspect.finally36);
      assertEquals("void,String,Throwable,int,long",
            OverloadedFinallyAspect.finally37);
      assertEquals("void,String,Serializable,int,long",
            OverloadedFinallyAspect.finally38);
      assertEquals("void,CharSequence,Throwable,int,long",
            OverloadedFinallyAspect.finally39);
      assertEquals("String,String,Throwable,int", OverloadedFinallyAspect.finally40);
      assertEquals("void,String,Throwable,long", OverloadedFinallyAspect.finally41);
      assertEquals("String,String,Serializable,long",
            OverloadedFinallyAspect.finally42);
      assertEquals("void,String,Serializable,int",
            OverloadedFinallyAspect.finally43);
      assertEquals("Object,CharSequence,Throwable,int",
            OverloadedFinallyAspect.finally44);
      assertEquals("void,CharSequence,Throwable,long",
            OverloadedFinallyAspect.finally45);
      assertEquals("void,String,Throwable,Object[]",
            OverloadedFinallyAspect.finally46);
      assertEquals("void,String,Throwable,Object",
            OverloadedFinallyAspect.finally47);
      assertEquals("void,String,Serializable,Object[]",
            OverloadedFinallyAspect.finally48);
      assertEquals("void,String,Serializable,Object",
            OverloadedFinallyAspect.finally49);
      assertEquals("void,CharSequence,Throwable,Object[]",
            OverloadedFinallyAspect.finally50);
      assertEquals("void,CharSequence,Throwable,Object",
            OverloadedFinallyAspect.finally51);
      assertEquals("void,String,Throwable", OverloadedFinallyAspect.finally52);
      assertEquals("void,String,Serializable", OverloadedFinallyAspect.finally53);
      assertEquals("void,CharSequence,Throwable", OverloadedFinallyAspect.finally54);
      assertEquals("void,Throwable,int,long", OverloadedFinallyAspect.finally55);
      assertEquals("void,Serializable,int,long", OverloadedFinallyAspect.finally56);
      assertEquals("Object,Throwable,long", OverloadedFinallyAspect.finally57);
      assertEquals("void,Throwable,int", OverloadedFinallyAspect.finally58);
      assertEquals("String,Serializable,int", OverloadedFinallyAspect.finally59);
      assertEquals("Object,Serializable,long", OverloadedFinallyAspect.finally60);
      assertEquals("void,Throwable,Object[]", OverloadedFinallyAspect.finally61);
      assertEquals("void,Throwable,Object", OverloadedFinallyAspect.finally62);
      assertEquals("void,Serializable,Object[]", OverloadedFinallyAspect.finally63);
      assertEquals("void,Serializable,Object", OverloadedFinallyAspect.finally64);
      assertEquals("void,Throwable", OverloadedFinallyAspect.finally65);
      assertEquals("void,Serializable", OverloadedFinallyAspect.finally66);
      assertEquals("void,int,long", OverloadedFinallyAspect.finally67);
      assertEquals("String,long", OverloadedFinallyAspect.finally68);
      assertEquals("Object,int", OverloadedFinallyAspect.finally69);
      assertEquals("void,Object[]", OverloadedFinallyAspect.finally70);
      assertEquals("void,Object", OverloadedFinallyAspect.finally71);
      assertEquals("void", OverloadedFinallyAspect.finally72);
   }
   
   public void testBeforeCall()
   {
      // clear all relevant aspect fields
      OverloadedBeforeCallAspect.clear();
      // execute the join point
      (new OverloadedAdvicePOJOCaller()).callMethod3(pojo);
      // check aspect fields
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedBeforeCallAspect.before1);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,SuperClass,int",
            OverloadedBeforeCallAspect.before2);
      assertEquals("MethodByMethodInfo,Object,OverloadedAdvicePOJOCaller,int",
            OverloadedBeforeCallAspect.before3);
      assertEquals("MethodByMethodInfo,Object,SuperClass,int",
            OverloadedBeforeCallAspect.before4);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedBeforeCallAspect.before5);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,SuperClass,Object[]",
            OverloadedBeforeCallAspect.before6);
      assertEquals("MethodByMethodInfo,Object,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedBeforeCallAspect.before7);
      assertEquals("MethodByMethodInfo,Object,SuperClass,Object[]",
            OverloadedBeforeCallAspect.before8);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller",
            OverloadedBeforeCallAspect.before9);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,SuperClass",
            OverloadedBeforeCallAspect.before10);
      assertEquals("MethodByMethodInfo,Object,OverloadedAdvicePOJOCaller",
            OverloadedBeforeCallAspect.before11);
      assertEquals("MethodByMethodInfo,Object,SuperClass",
            OverloadedBeforeCallAspect.before12);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,int",
            OverloadedBeforeCallAspect.before13);
      assertEquals("MethodByMethodInfo,Object,int",
            OverloadedBeforeCallAspect.before14);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,Object[]",
            OverloadedBeforeCallAspect.before15);
      assertEquals("MethodByMethodInfo,Object,Object[]",
            OverloadedBeforeCallAspect.before16);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,int",
            OverloadedBeforeCallAspect.before17);
      assertEquals("MethodByMethodInfo,SuperClass,int",
            OverloadedBeforeCallAspect.before18);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedBeforeCallAspect.before19);
      assertEquals("MethodByMethodInfo,SuperClass,Object[]",
            OverloadedBeforeCallAspect.before20);
      assertEquals("MethodByMethodInfo,int", OverloadedBeforeCallAspect.before21);
      assertEquals("MethodByMethodInfo,Object[]",
            OverloadedBeforeCallAspect.before22);
      assertEquals("MethodByMethodInfo", OverloadedBeforeCallAspect.before23);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedBeforeCallAspect.before24);
      assertEquals("OverloadedAdvicePOJO,SuperClass,int",
            OverloadedBeforeCallAspect.before25);
      assertEquals("Object,OverloadedAdvicePOJOCaller,int",
            OverloadedBeforeCallAspect.before26);
      assertEquals("Object,SuperClass,int", OverloadedBeforeCallAspect.before27);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedBeforeCallAspect.before28);
      assertEquals("OverloadedAdvicePOJO,SuperClass,Object[]",
            OverloadedBeforeCallAspect.before29);
      assertEquals("Object,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedBeforeCallAspect.before30);
      assertEquals("Object,SuperClass,Object[]",
            OverloadedBeforeCallAspect.before31);
      assertEquals("OverloadedAdvicePOJO,int", OverloadedBeforeCallAspect.before32);
      assertEquals("Object,int", OverloadedBeforeCallAspect.before33);
      assertEquals("OverloadedAdvicePOJO,Object[]",
            OverloadedBeforeCallAspect.before34);
      assertEquals("Object,Object[]", OverloadedBeforeCallAspect.before35);
      assertEquals("OverloadedAdvicePOJO", OverloadedBeforeCallAspect.before36);
      assertEquals("Object", OverloadedBeforeCallAspect.before37);
      assertEquals("OverloadedAdvicePOJOCaller,int",
            OverloadedBeforeCallAspect.before38);
      assertEquals("SuperClass,int", OverloadedBeforeCallAspect.before39);
      assertEquals("OverloadedAdvicePOJOCaller,Object[]",
            OverloadedBeforeCallAspect.before40);
      assertEquals("SuperClass,Object[]", OverloadedBeforeCallAspect.before41);
      assertEquals("OverloadedAdvicePOJOCaller",
            OverloadedBeforeCallAspect.before42);
      assertEquals("SuperClass", OverloadedBeforeCallAspect.before43);
      assertEquals("int", OverloadedBeforeCallAspect.before44);
      assertEquals("Object[]", OverloadedBeforeCallAspect.before45);
   }
   
   public void testAroundCall()
   {
      // clear all relevant aspect fields
      OverloadedAroundCallAspect.clear();
      // execute the join point
      (new OverloadedAdvicePOJOCaller()).callMethod3(pojo);
      // check aspect fields
      assertEquals("defaultSignature", OverloadedAroundCallAspect.around1);
      assertEquals(
            "MethodCalledByMethodInvocation,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedAroundCallAspect.around2);
      assertEquals(
            "MethodCalledByMethodInvocation,OverloadedAdvicePOJO,SuperClass,int",
            OverloadedAroundCallAspect.around3);
      assertEquals(
            "MethodCalledByMethodInvocation,Object,OverloadedAdvicePOJOCaller,int",
            OverloadedAroundCallAspect.around4);
      assertEquals("MethodCalledByMethodInvocation,Object,SuperClass,int",
            OverloadedAroundCallAspect.around5);
      assertEquals(
            "MethodCalledByMethodInvocation,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedAroundCallAspect.around6);
      assertEquals(
            "MethodCalledByMethodInvocation,OverloadedAdvicePOJO,SuperClass,Object[]",
            OverloadedAroundCallAspect.around7);
      assertEquals(
            "MethodCalledByMethodInvocation,Object,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedAroundCallAspect.around8);
      assertEquals("MethodCalledByMethodInvocation,Object,SuperClass,Object[]",
            OverloadedAroundCallAspect.around9);
      assertEquals(
            "MethodCalledByMethodInvocation,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller",
            OverloadedAroundCallAspect.around10);
      assertEquals("MethodCalledByMethodInvocation,OverloadedAdvicePOJO,SuperClass",
            OverloadedAroundCallAspect.around11);
      assertEquals(
            "MethodCalledByMethodInvocation,Object,OverloadedAdvicePOJOCaller",
            OverloadedAroundCallAspect.around12);
      assertEquals("MethodCalledByMethodInvocation,Object,SuperClass",
            OverloadedAroundCallAspect.around13);
      assertEquals("MethodCalledByMethodInvocation,OverloadedAdvicePOJO,int",
            OverloadedAroundCallAspect.around14);
      assertEquals("MethodCalledByMethodInvocation,Object,int",
            OverloadedAroundCallAspect.around15);
      assertEquals("MethodCalledByMethodInvocation,OverloadedAdvicePOJO,Object[]",
            OverloadedAroundCallAspect.around16);
      assertEquals("MethodCalledByMethodInvocation,Object,Object[]",
            OverloadedAroundCallAspect.around17);
      assertEquals("MethodCalledByMethodInvocation,OverloadedAdvicePOJOCaller,int",
            OverloadedAroundCallAspect.around18);
      assertEquals("MethodCalledByMethodInvocation,SuperClass,int",
            OverloadedAroundCallAspect.around19);
      assertEquals(
            "MethodCalledByMethodInvocation,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedAroundCallAspect.around20);
      assertEquals("MethodCalledByMethodInvocation,SuperClass,Object[]",
            OverloadedAroundCallAspect.around21);
      assertEquals("MethodCalledByMethodInvocation,int",
            OverloadedAroundCallAspect.around22);
      assertEquals("MethodCalledByMethodInvocation,Object[]",
            OverloadedAroundCallAspect.around23);
      assertEquals("MethodCalledByMethodInvocation",
            OverloadedAroundCallAspect.around24);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedAroundCallAspect.around25);
      assertEquals("OverloadedAdvicePOJO,SuperClass,int",
            OverloadedAroundCallAspect.around26);
      assertEquals("Object,OverloadedAdvicePOJOCaller,int",
            OverloadedAroundCallAspect.around27);
      assertEquals("Object,SuperClass,int", OverloadedAroundCallAspect.around28);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedAroundCallAspect.around29);
      assertEquals("OverloadedAdvicePOJO,SuperClass,Object[]",
            OverloadedAroundCallAspect.around30);
      assertEquals("Object,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedAroundCallAspect.around31);
      assertEquals("Object,SuperClass,Object[]",
            OverloadedAroundCallAspect.around32);
      assertEquals("OverloadedAdvicePOJO,int", OverloadedAroundCallAspect.around33);
      assertEquals("Object,int", OverloadedAroundCallAspect.around34);
      assertEquals("OverloadedAdvicePOJO,Object[]",
            OverloadedAroundCallAspect.around35);
      assertEquals("Object,Object[]", OverloadedAroundCallAspect.around36);
      assertEquals("OverloadedAdvicePOJO", OverloadedAroundCallAspect.around37);
      assertEquals("Object", OverloadedAroundCallAspect.around38);
      assertEquals("OverloadedAdvicePOJOCaller,int",
            OverloadedAroundCallAspect.around39);
      assertEquals("SuperClass,int", OverloadedAroundCallAspect.around40);
      assertEquals("OverloadedAdvicePOJOCaller,Object[]",
            OverloadedAroundCallAspect.around41);
      assertEquals("SuperClass,Object[]", OverloadedAroundCallAspect.around42);
      assertEquals("OverloadedAdvicePOJOCaller",
            OverloadedAroundCallAspect.around43);
      assertEquals("SuperClass", OverloadedAroundCallAspect.around44);
      assertEquals("int", OverloadedAroundCallAspect.around45);
      assertEquals("Object[]", OverloadedAroundCallAspect.around46);
   }
   
   public void testAfterCall()
   {
      // clear all relevant aspect fields
      OverloadedAfterCallAspect.clear();
      // execute the join point
      (new OverloadedAdvicePOJOCaller()).callMethod3(pojo);
      // check aspect fields
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,int",
            OverloadedAfterCallAspect.after1);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Object[]",
            OverloadedAfterCallAspect.after2);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long",
            OverloadedAfterCallAspect.after3);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedAfterCallAspect.after4);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller",
            OverloadedAfterCallAspect.after5);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,long,int",
            OverloadedAfterCallAspect.after6);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,long,Object[]",
            OverloadedAfterCallAspect.after7);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,long",
            OverloadedAfterCallAspect.after8);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,int",
            OverloadedAfterCallAspect.after9);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO",
            OverloadedAfterCallAspect.after10);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,long,int",
            OverloadedAfterCallAspect.after11);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,long,Object[]",
            OverloadedAfterCallAspect.after12);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,long",
            OverloadedAfterCallAspect.after13);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,int",
            OverloadedAfterCallAspect.after14);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller",
            OverloadedAfterCallAspect.after15);
      assertEquals("MethodByMethodInfo,long,int", OverloadedAfterCallAspect.after16);
      assertEquals("MethodByMethodInfo,long,Object[]",
            OverloadedAfterCallAspect.after17);
      assertEquals("MethodByMethodInfo,long", OverloadedAfterCallAspect.after18);
      assertEquals("MethodByMethodInfo,int", OverloadedAfterCallAspect.after19);
      assertEquals("MethodByMethodInfo", OverloadedAfterCallAspect.after20);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,int",
            OverloadedAfterCallAspect.after21);
      assertEquals("OverloadedAdvicePOJO,SuperClass,long,int",
            OverloadedAfterCallAspect.after22);
      assertEquals("Object,OverloadedAdvicePOJOCaller,long,int",
            OverloadedAfterCallAspect.after23);
      assertEquals("Object,SuperClass,long,int", OverloadedAfterCallAspect.after24);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Object[]",
            OverloadedAfterCallAspect.after25);
      assertEquals("OverloadedAdvicePOJO,SuperClass,long,Object[]",
            OverloadedAfterCallAspect.after26);
      assertEquals("Object,OverloadedAdvicePOJOCaller,long,Object[]",
            OverloadedAfterCallAspect.after27);
      assertEquals("Object,SuperClass,long,Object[]",
            OverloadedAfterCallAspect.after28);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long",
            OverloadedAfterCallAspect.after29);
      assertEquals("OverloadedAdvicePOJO,SuperClass,long",
            OverloadedAfterCallAspect.after30);
      assertEquals("Object,OverloadedAdvicePOJOCaller,long",
            OverloadedAfterCallAspect.after31);
      assertEquals("Object,SuperClass,long", OverloadedAfterCallAspect.after32);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedAfterCallAspect.after33);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller",
            OverloadedAfterCallAspect.after34);
      assertEquals("OverloadedAdvicePOJO,long,int",
            OverloadedAfterCallAspect.after35);
      assertEquals("Object,long,int", OverloadedAfterCallAspect.after36);
      assertEquals("OverloadedAdvicePOJO,long,Object[]",
            OverloadedAfterCallAspect.after37);
      assertEquals("Object,long,Object[]", OverloadedAfterCallAspect.after38);
      assertEquals("OverloadedAdvicePOJO,long", OverloadedAfterCallAspect.after39);
      assertEquals("Object,long", OverloadedAfterCallAspect.after40);
      assertEquals("OverloadedAdvicePOJO,int", OverloadedAfterCallAspect.after41);
      assertEquals("OverloadedAdvicePOJO", OverloadedAfterCallAspect.after42);
      assertEquals("OverloadedAdvicePOJOCaller,long,int",
            OverloadedAfterCallAspect.after43);
      assertEquals("SuperClass,long,int", OverloadedAfterCallAspect.after44);
      assertEquals("OverloadedAdvicePOJOCaller,long,Object[]",
            OverloadedAfterCallAspect.after45);
      assertEquals("SuperClass,long,Object[]", OverloadedAfterCallAspect.after46);
      assertEquals("OverloadedAdvicePOJOCaller,long",
            OverloadedAfterCallAspect.after47);
      assertEquals("SuperClass,long", OverloadedAfterCallAspect.after48);
      assertEquals("OverloadedAdvicePOJOCaller,int",
            OverloadedAfterCallAspect.after49);
      assertEquals("OverloadedAdvicePOJOCaller", OverloadedAfterCallAspect.after50);
   }
   
   public void testAfterCallThrowing()
   {
      // clear all relevant aspect fields
      OverloadedThrowingCallAspect.clear();
      // execute the join point
      (new OverloadedAdvicePOJOCaller()).callMethod4(pojo);
      // check aspect fields
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing1);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing2);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedThrowingCallAspect.throwing3);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing4);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing5);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,Throwable",
            OverloadedThrowingCallAspect.throwing6);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJOCaller,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing7);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing8);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedThrowingCallAspect.throwing9);
      assertEquals("MethodByMethodInfo,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing10);
      assertEquals("MethodByMethodInfo,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing11);
      assertEquals("MethodByMethodInfo,Throwable",
            OverloadedThrowingCallAspect.throwing12);
      assertEquals(
            "OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing13);
      assertEquals("OverloadedAdvicePOJO,SuperClass,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing14);
      assertEquals("Object,OverloadedAdvicePOJOCaller,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing15);
      assertEquals("Object,SuperClass,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing16);
      assertEquals(
            "OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing17);
      assertEquals("OverloadedAdvicePOJO,SuperClass,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing18);
      assertEquals("Object,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing19);
      assertEquals("Object,SuperClass,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing20);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedThrowingCallAspect.throwing21);
      assertEquals("OverloadedAdvicePOJO,SuperClass,Throwable",
            OverloadedThrowingCallAspect.throwing22);
      assertEquals("Object,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedThrowingCallAspect.throwing23);
      assertEquals("Object,SuperClass,Throwable",
            OverloadedThrowingCallAspect.throwing24);
      assertEquals("OverloadedAdvicePOJO,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing25);
      assertEquals("Object,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing26);
      assertEquals("OverloadedAdvicePOJO,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing27);
      assertEquals("Object,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing28);
      assertEquals("OverloadedAdvicePOJO,Throwable",
            OverloadedThrowingCallAspect.throwing29);
      assertEquals("Object,Throwable", OverloadedThrowingCallAspect.throwing30);
      assertEquals("OverloadedAdvicePOJOCaller,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing31);
      assertEquals("SuperClass,Throwable,boolean",
            OverloadedThrowingCallAspect.throwing32);
      assertEquals("OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing33);
      assertEquals("SuperClass,Throwable,Object[]",
            OverloadedThrowingCallAspect.throwing34);
      assertEquals("OverloadedAdvicePOJOCaller,Throwable",
            OverloadedThrowingCallAspect.throwing35);
      assertEquals("SuperClass,Throwable", OverloadedThrowingCallAspect.throwing36);
      assertEquals("Throwable,boolean", OverloadedThrowingCallAspect.throwing37);
   }
   
   public void testFinallyCall()
   {
      // clear all relevant aspect fields
      OverloadedFinallyCallAspect.clear();
      
      // execute the join point
      (new OverloadedAdvicePOJOCaller()).callMethod3(pojo);
      
      // check aspect fields
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Throwable,int",
            OverloadedFinallyCallAspect.finally1);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,SuperClass,long,Throwable,int",
            OverloadedFinallyCallAspect.finally2);
      assertEquals(
            "MethodByMethodInfo,Object,OverloadedAdvicePOJOCaller,long,Throwable,int",
            OverloadedFinallyCallAspect.finally3);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally4);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,SuperClass,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally5);
      assertEquals(
            "MethodByMethodInfo,Object,OverloadedAdvicePOJOCaller,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally6);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Throwable",
            OverloadedFinallyCallAspect.finally7);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,SuperClass,long,Throwable",
            OverloadedFinallyCallAspect.finally8);
      assertEquals(
            "MethodByMethodInfo,Object,OverloadedAdvicePOJOCaller,long,Throwable",
            OverloadedFinallyCallAspect.finally9);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,int",
            OverloadedFinallyCallAspect.finally10);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally11);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedFinallyCallAspect.finally12);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedFinallyCallAspect.finally13);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedFinallyCallAspect.finally14);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller",
            OverloadedFinallyCallAspect.finally15);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,long,Throwable,int",
            OverloadedFinallyCallAspect.finally16);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally17);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,long,Throwable",
            OverloadedFinallyCallAspect.finally18);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,Throwable,int",
            OverloadedFinallyCallAspect.finally19);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally20);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,Throwable",
            OverloadedFinallyCallAspect.finally21);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,int",
            OverloadedFinallyCallAspect.finally22);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO,Object[]",
            OverloadedFinallyCallAspect.finally23);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJO",
            OverloadedFinallyCallAspect.finally24);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJOCaller,long,Throwable,int",
            OverloadedFinallyCallAspect.finally25);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJOCaller,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally26);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,long,Throwable",
            OverloadedFinallyCallAspect.finally27);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,Throwable,int",
            OverloadedFinallyCallAspect.finally28);
      assertEquals(
            "MethodByMethodInfo,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally29);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedFinallyCallAspect.finally30);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,int",
            OverloadedFinallyCallAspect.finally31);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedFinallyCallAspect.finally32);
      assertEquals("MethodByMethodInfo,OverloadedAdvicePOJOCaller",
            OverloadedFinallyCallAspect.finally33);
      assertEquals("MethodByMethodInfo,long,Throwable,int",
            OverloadedFinallyCallAspect.finally34);
      assertEquals("MethodByMethodInfo", OverloadedFinallyCallAspect.finally35);
      assertEquals(
            "OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Throwable,int",
            OverloadedFinallyCallAspect.finally36);
      assertEquals(
            "OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally37);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,long,Throwable",
            OverloadedFinallyCallAspect.finally38);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,int",
            OverloadedFinallyCallAspect.finally39);
      assertEquals(
            "OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally40);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Throwable",
            OverloadedFinallyCallAspect.finally41);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,int",
            OverloadedFinallyCallAspect.finally42);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller,Object[]",
            OverloadedFinallyCallAspect.finally43);
      assertEquals("OverloadedAdvicePOJO,OverloadedAdvicePOJOCaller",
            OverloadedFinallyCallAspect.finally44);
      assertEquals("OverloadedAdvicePOJO,long,Throwable,int",
            OverloadedFinallyCallAspect.finally45);
      assertEquals("OverloadedAdvicePOJO,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally46);
      assertEquals("OverloadedAdvicePOJO,long,Throwable",
            OverloadedFinallyCallAspect.finally47);
      assertEquals("OverloadedAdvicePOJO,Throwable,int",
            OverloadedFinallyCallAspect.finally48);
      assertEquals("OverloadedAdvicePOJO,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally49);
      assertEquals("OverloadedAdvicePOJO,Throwable",
            OverloadedFinallyCallAspect.finally50);
      assertEquals("OverloadedAdvicePOJO,int",
            OverloadedFinallyCallAspect.finally51);
      assertEquals("OverloadedAdvicePOJO,Object[]",
            OverloadedFinallyCallAspect.finally52);
      assertEquals("OverloadedAdvicePOJO", OverloadedFinallyCallAspect.finally53);
      assertEquals("OverloadedAdvicePOJOCaller,long,Throwable,int",
            OverloadedFinallyCallAspect.finally54);
      assertEquals("OverloadedAdvicePOJOCaller,long,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally55);
      assertEquals("OverloadedAdvicePOJOCaller,long,Throwable",
            OverloadedFinallyCallAspect.finally56);
      assertEquals("OverloadedAdvicePOJOCaller,Throwable,int",
            OverloadedFinallyCallAspect.finally57);
      assertEquals("OverloadedAdvicePOJOCaller,Throwable,Object[]",
            OverloadedFinallyCallAspect.finally58);
      assertEquals("OverloadedAdvicePOJOCaller,Throwable",
            OverloadedFinallyCallAspect.finally59);
      assertEquals("OverloadedAdvicePOJOCaller,int",
            OverloadedFinallyCallAspect.finally60);
      assertEquals("OverloadedAdvicePOJOCaller,Object[]",
            OverloadedFinallyCallAspect.finally61);
      assertEquals("OverloadedAdvicePOJOCaller",
            OverloadedFinallyCallAspect.finally62);
   }
}