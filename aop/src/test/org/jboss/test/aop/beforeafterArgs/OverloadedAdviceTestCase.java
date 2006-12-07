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

   public void test1()
   {
      OverloadedBeforeAspect.clear();
      pojo.text = "test2";
      assertEquals("FieldInfo,String", OverloadedBeforeAspect.before1);
      assertEquals("FieldInfo,Object", OverloadedBeforeAspect.before2);
      assertEquals("JoinPointInfo,String", OverloadedBeforeAspect.before3);
      assertEquals("JoinPointInfo,Object", OverloadedBeforeAspect.before4);
      assertEquals("Object,String", OverloadedBeforeAspect.before5);
      assertEquals("Object,Object", OverloadedBeforeAspect.before6);
      assertEquals("FieldInfo", OverloadedBeforeAspect.before7);
      assertEquals("JoinPointInfo", OverloadedBeforeAspect.before8);
      assertEquals("Object", OverloadedBeforeAspect.before9);
      assertEquals("String", OverloadedBeforeAspect.before10);
      assertEquals("Object", OverloadedBeforeAspect.before11);
      assertEquals("", OverloadedBeforeAspect.before12);
      assertEquals("FieldInfo,String", OverloadedBeforeAspect.before13);
      String myText = pojo.text;
      assertEquals("FieldInfo", OverloadedBeforeAspect.before13);
      
   }
   
   public void test2()
   {
      OverloadedAroundAspect.clear();
      pojo.method1(10, 15);
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
      assertEquals("MethodInvocation", OverloadedAroundAspect.around8);
      assertEquals("Object", OverloadedAroundAspect.around9);
      assertEquals("int,long", OverloadedAroundAspect.around10);
      assertTrue(OverloadedAroundAspect.around11.equals("int") ||
            OverloadedAroundAspect.around11.equals("long"));
      assertEquals("", OverloadedAroundAspect.around12);
   }
   
   public void test3()
   {
      OverloadedAfterAspect.clear();
      pojo.method2(0, null);
      assertEquals("Object,MethodInfo,SuperClass,float,SubValue", OverloadedAfterAspect.after1);
      assertEquals("Object,MethodInfo,SuperClass,float,SuperValue", OverloadedAfterAspect.after2);
      assertEquals("Object,MethodInfo,SuperClass,float,Object", OverloadedAfterAspect.after3);
      assertEquals("Object,MethodInfo,Object,float,SubValue", OverloadedAfterAspect.after4);
      assertEquals("Object,MethodInfo,Object,float,SuperValue", OverloadedAfterAspect.after5);
      assertEquals("Object,MethodInfo,Object,float,Object", OverloadedAfterAspect.after6);
      assertEquals("Object,JoinPointInfo,SuperClass,float,SubValue", OverloadedAfterAspect.after7);
      assertEquals("SuperClass,MethodInfo,SuperClass,SubValue", OverloadedAfterAspect.after8);
      assertEquals("Object,MethodInfo,SuperClass,float", OverloadedAfterAspect.after9);
      assertEquals("Object,MethodInfo,SuperClass,SuperValue", OverloadedAfterAspect.after10);
      assertEquals("SuperClass,MethodInfo,SuperClass,Object", OverloadedAfterAspect.after11);
      assertEquals("SuperClass,MethodInfo,Object,SubValue", OverloadedAfterAspect.after12);
      assertEquals("Object,MethodInfo,Object,float", OverloadedAfterAspect.after13);
      assertEquals("SubClass,MethodInfo,Object,SuperValue", OverloadedAfterAspect.after14);
      assertEquals("SubClass,MethodInfo,Object,Object", OverloadedAfterAspect.after15);
      assertEquals("SuperClass,JoinPointInfo,SuperClass,SubValue", OverloadedAfterAspect.after16);
      assertEquals("SubClass,JoinPointInfo,SuperClass,float", OverloadedAfterAspect.after17);
      assertEquals("SubClass,JoinPointInfo,SuperClass,SuperValue", OverloadedAfterAspect.after18);
      assertEquals("SuperClass,JoinPointInfo,SuperClass,Object", OverloadedAfterAspect.after19);
      assertEquals("SubClass,Object,SuperClass,SubValue", OverloadedAfterAspect.after20);
      assertEquals("Object,Object,SuperClass,float", OverloadedAfterAspect.after21);
      assertEquals("SubClass,Object,SuperClass,SuperValue", OverloadedAfterAspect.after22);
      assertEquals("Object,Object,SuperClass,Object", OverloadedAfterAspect.after23);
      assertEquals("Object,MethodInfo,SuperClass", OverloadedAfterAspect.after24);
      assertEquals("Object,MethodInfo,Object", OverloadedAfterAspect.after25);
      assertEquals("Object,JoinPointInfo,SuperClass", OverloadedAfterAspect.after26);
      assertEquals("Object,MethodInfo,float,SubValue", OverloadedAfterAspect.after27);
      assertEquals("Object,MethodInfo,float,SuperValue", OverloadedAfterAspect.after28);
      assertEquals("Object,MethodInfo,float,Object", OverloadedAfterAspect.after29);
      assertEquals("Object,JoinPointInfo,float,SubValue", OverloadedAfterAspect.after30);
      assertEquals("SuperClass,MethodInfo,SubValue", OverloadedAfterAspect.after31);
      assertEquals("Object,MethodInfo,float", OverloadedAfterAspect.after32);
      assertEquals("Object,MethodInfo,SuperValue", OverloadedAfterAspect.after33);
      assertEquals("SuperClass,JoinPointInfo,SubValue", OverloadedAfterAspect.after34);
      assertEquals("SubClass,JoinPointInfo,float", OverloadedAfterAspect.after35);
      assertEquals("SubClass,JoinPointInfo,SuperValue", OverloadedAfterAspect.after36);
      assertEquals("SubClass,Object,SubValue", OverloadedAfterAspect.after37);
      assertEquals("Object,Object,float", OverloadedAfterAspect.after38);
      assertEquals("SubClass,Object,SuperValue", OverloadedAfterAspect.after39);
      assertEquals("Object,MethodInfo", OverloadedAfterAspect.after40);
      assertEquals("Object,JoinPointInfo", OverloadedAfterAspect.after41);
      assertEquals("Object,SuperClass,float,SubValue", OverloadedAfterAspect.after42);
      assertEquals("Object,SuperClass,float,SuperValue", OverloadedAfterAspect.after43);
      assertEquals("Object,SuperClass,float,Object", OverloadedAfterAspect.after44);
      assertEquals("SuperClass,SuperClass,float", OverloadedAfterAspect.after45);
      assertEquals("Object,SuperClass,SubValue", OverloadedAfterAspect.after46);
      assertEquals("Object,SuperClass,SuperValue", OverloadedAfterAspect.after47);
      assertEquals("SuperClass,SuperClass,Object", OverloadedAfterAspect.after48);
      assertEquals("Object,SuperClass", OverloadedAfterAspect.after49);
      assertEquals("Object,Object", OverloadedAfterAspect.after50);
      assertEquals("void,float,SubValue", OverloadedAfterAspect.after51);
      assertEquals("Object,float", OverloadedAfterAspect.after52);
      assertEquals("void,SubValue", OverloadedAfterAspect.after53);
      assertEquals("void,SuperValue", OverloadedAfterAspect.after54);
      assertEquals("void", OverloadedAfterAspect.after55);
   }
   
   public void test4()
   {
      try
      {
         pojo.method3();
      } catch (POJOException pe) {}
      assertEquals("JoinPointInfo,Throwable,SubInterface,Implementor",
            OverloadedThrowingAspect.throwing1);
   }
}