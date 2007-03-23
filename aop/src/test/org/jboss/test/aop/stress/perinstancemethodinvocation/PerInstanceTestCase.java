/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.aop.stress.perinstancemethodinvocation;

import org.jboss.test.aop.stress.Scenario;
import org.jboss.test.aop.stress.ScenarioTest;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class PerInstanceTestCase extends ScenarioTest
{
   public static void main(String[] args)
   {
      junit.textui.TestRunner.run(PerInstanceTestCase.class);
   }

   public PerInstanceTestCase(String name)
   {
      // FIXME PerInstanceTestCase constructor
      super(name);
   }

   public void testPerInstanceInterceptor() throws Exception
   {
      POJO pojo = new POJO();
      PerInstanceInterceptor.called = 0;
      pojo.method1();
      assertEquals(1, PerInstanceInterceptor.called);
      getRunner().executeScenario(new PerInstanceInterceptorScenario());
   }
   
   private class PerInstanceInterceptorScenario implements Scenario
   {
      POJO pojo = new POJO();
      public void execute(int thread, int loop) throws Exception
      {
         pojo.method1();
      }
   }

   public void testPerInstanceAspect() throws Exception
   {
      POJO pojo = new POJO();
      PerInstanceAspect.called = 0;
      pojo.method2();
      assertEquals(1, PerInstanceAspect.called);
      getRunner().executeScenario(new PerInstanceAspectScenario());
   }
   
   private class PerInstanceAspectScenario implements Scenario
   {
      POJO pojo = new POJO();
      public void execute(int thread, int loop) throws Exception
      {
         pojo.method2();
      }
   }

   public void testPerInstanceInterceptorRepeatInstantiation() throws Exception
   {
      POJO pojo = new POJO();
      PerInstanceInterceptor.called = 0;
      pojo.method1();
      pojo.method1();
      assertEquals(2, PerInstanceInterceptor.called);
      getRunner().executeScenario(new PerInstanceInterceptorRepeatInstantiationScenario());
   }
   
   private class PerInstanceInterceptorRepeatInstantiationScenario implements Scenario
   {
      public void execute(int thread, int loop) throws Exception
      {
         POJO pojo = new POJO();
         pojo.method1();
      }
   }

   public void testPerInstanceAspectRepeatInstantiation() throws Exception
   {
      POJO pojo = new POJO();
      PerInstanceAspect.called = 0;
      pojo.method2();
      assertEquals(1, PerInstanceAspect.called);
      getRunner().executeScenario(new PerInstanceAspectRepeatInstantiationScenario());
   }
   
   private class PerInstanceAspectRepeatInstantiationScenario implements Scenario
   {
      public void execute(int thread, int loop) throws Exception
      {
         POJO pojo = new POJO();
         pojo.method2();
      }
   }
}
