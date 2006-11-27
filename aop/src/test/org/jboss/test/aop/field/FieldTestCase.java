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
package org.jboss.test.aop.field;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.aop.Advised;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassAdvisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.joinpoint.FieldJoinpoint;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 *
 * @author <a href="mailto:stalep@conduct.no">Stale W. Pedersen</a>
 * @version $Revision
 */
public class FieldTestCase extends AOPTestWithSetup
{
   
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("FieldTestCase");
      suite.addTestSuite(FieldTestCase.class);
      return suite;
   }

   public FieldTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
   }
   
   public void testField()
   {
      System.out.println("*** testField");
      SubPOJO spojo = new SubPOJO(4);
      assertEquals("Field is not set correctly", spojo.getPOJOField(), (spojo.getSubPOJOField()/2));  
   }

   public void testField2()
   {
      System.out.println("*** testField2");
      SubSubPOJO spojo = new SubSubPOJO(4);
      assertEquals("Field is not set correctly", spojo.getSubSubPOJOField()/2, (spojo.getSubPOJOField()));  
   }
   
   public void testFieldInheritance()
   {
      System.out.println("*** testFieldInheritance");
      SubSubPOJO pojo = new SubSubPOJO(4);

      TraceInterceptor.intercepted = false;
      pojo.mine = 5;
      assertTrue(TraceInterceptor.intercepted);


      TraceInterceptor.intercepted = false;
      pojo.pojoInherited = 5;
      assertTrue(TraceInterceptor.intercepted);

      TraceInterceptor.intercepted = false;
      pojo.subpojoInherited = 5;
      assertTrue(TraceInterceptor.intercepted);
   }

   public void testPerJoinpoint() throws Exception
   {
      SubSubPOJO pojo1 = new SubSubPOJO(5);
      SubSubPOJO pojo2 = new SubSubPOJO(5);

      FieldPerJoinpointInterceptor.last = null;
      pojo1.mine = 10;
      assertNotNull(FieldPerJoinpointInterceptor.last);
      FieldPerJoinpointInterceptor mineWrite1 = FieldPerJoinpointInterceptor.last;

      FieldPerJoinpointInterceptor.last = null;
      int x = pojo1.mine;
      assertNotNull(FieldPerJoinpointInterceptor.last);
      FieldPerJoinpointInterceptor mineRead1 = FieldPerJoinpointInterceptor.last;
      
      assertSame(mineRead1, mineWrite1);
      
      FieldPerJoinpointInterceptor.last = null;
      pojo2.mine = 10;
      assertNotNull(FieldPerJoinpointInterceptor.last);
      FieldPerJoinpointInterceptor mineWrite2 = FieldPerJoinpointInterceptor.last;

      assertNotSame(mineRead1, mineWrite2);
      
      FieldPerJoinpointInterceptor.last = null;
      pojo1.pojoInherited = 10;
      assertNotNull(FieldPerJoinpointInterceptor.last);
      FieldPerJoinpointInterceptor inheritedWrite1 = FieldPerJoinpointInterceptor.last;
      
      assertNotSame(inheritedWrite1, mineWrite1);

      Field mine = pojo1.getClass().getField("mine"); 
      Field pojoInherited = pojo1.getClass().getSuperclass().getSuperclass().getField("pojoInherited");
      
      AspectDefinition def = AspectManager.instance().getAspectDefinition("org.jboss.test.aop.field.FieldPerJoinpointInterceptor");
      assertNotNull(def);
      InstanceAdvisor ia1 = ((Advised)pojo1)._getInstanceAdvisor();
      InstanceAdvisor ia2 = ((Advised)pojo2)._getInstanceAdvisor();
      FieldPerJoinpointInterceptor ia1Mine = (FieldPerJoinpointInterceptor)ia1.getPerInstanceJoinpointAspect(new FieldJoinpoint(mine), def);
      assertNotNull(ia1Mine);
      FieldPerJoinpointInterceptor ia2Mine = (FieldPerJoinpointInterceptor)ia2.getPerInstanceJoinpointAspect(new FieldJoinpoint(mine), def);
      assertNotNull(ia2Mine);
      FieldPerJoinpointInterceptor ia1Inherited = (FieldPerJoinpointInterceptor)ia1.getPerInstanceJoinpointAspect(new FieldJoinpoint(pojoInherited), def); 
      assertNotNull(ia1Inherited);
      
      assertSame(mineRead1, ia1Mine);
      assertSame(mineWrite2, ia2Mine);
      assertSame(inheritedWrite1, ia1Inherited);
   }
}
