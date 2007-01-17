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
package org.jboss.test.aop.container;

import java.lang.reflect.Method;

import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassContainer;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.util.MethodHashing;
import org.jboss.test.aop.AOPTestWithSetup;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
*
* @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
* @version $Revision: 45977 $
*/
public class ContainerTestCase extends AOPTestWithSetup
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ContainerTestCase");
      suite.addTestSuite(ContainerTestCase.class);
      return suite;
   }

   public ContainerTestCase(String name)
   {
      super(name);
   }

   public void testContainerWithNoChainOverriding() throws Throwable
   {
      invokeContainer(false);
   }

   public void testContainerWithChainOverriding() throws Throwable
   {
      invokeContainer(true);
   }

   public void invokeContainer(boolean overriding) throws Throwable
   {
         AspectManager manager = AspectManager.instance();
         ClassContainer container = (overriding ) ? new ContainerWithChainOverriding("X", manager) : new ClassContainer("X", manager);
         container.setClass(Child.class);
         container.initializeClassContainer();
         Child child = new Child();
         
         MethodInvocation invocation = getMethodInvocation(container, "childMethod", child);
         TestInterceptor.invoked = false;
         container.dynamicInvoke(child, invocation);
         assertTrue(TestInterceptor.invoked);
         
         invocation = getMethodInvocation(container, "parentMethod", child);
         TestInterceptor.invoked = false;
         container.dynamicInvoke(child, invocation);
         assertEquals(overriding, TestInterceptor.invoked);
   }
   
   private MethodInvocation getMethodInvocation(ClassContainer advisor, String methodName, Object target) throws Exception
   {
      Method method = target.getClass().getMethod(methodName, new Class[0]);
      long hash = MethodHashing.calculateHash(method);
      MethodInfo info = advisor.getMethodInfo(hash);
      Interceptor[] interceptors = info.getInterceptors();
      MethodInvocation invocation = new MethodInvocation(interceptors, hash, method, method, advisor);
      invocation.setTargetObject(target);
      invocation.setArguments(new Object[0]);
      return invocation;
   }
   
   static class ContainerWithChainOverriding extends ClassContainer
   {
      public ContainerWithChainOverriding(String name, AspectManager manager)
      {
         super(name, manager);
         super.setChainOverridingForInheritedMethods(true);
      }
   }
}

