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
package org.jboss.test.aop.dynamic;


import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.aop.Advised;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassContainer;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.aop.proxy.container.AOPProxyFactoryParameters;
import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;
import org.jboss.aop.util.MethodHashing;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class SimpleDynamicTester extends AOPTestWithSetup
{
   public static Test suite()
   {
      TestSuite suite = new TestSuite("DynamicTester");
      suite.addTestSuite(SimpleDynamicTester.class);
      return suite;
   }
   // Constants ----------------------------------------------------
   // Attributes ---------------------------------------------------

   // Static -------------------------------------------------------

   // Constructors -------------------------------------------------
   public SimpleDynamicTester(String name)
   {
      super(name);
   }
   
   public void testDynamic() throws Exception
   {
      reset();
      @SuppressWarnings("unused")
      POJO tmp = new POJO();
      assertInstanceOf(tmp, Advised.class);
      assertInterceptors(false, false);
      
      addBinding("one", "all(org.jboss.test.aop.dynamic.POJO)", Interceptor1.class);
      callPOJO(true, false);
      
      addBinding("two", "all(org.jboss.test.aop.dynamic.POJO)", Interceptor2.class);
      callPOJO(true, true);
      
      AspectManager.instance().removeBinding("one");
      callPOJO(false, true);
      
      addBinding("one", "all(org.jboss.test.aop.dynamic.POJO)", Interceptor1.class);
      callPOJO(true, true);
      
      AspectManager.instance().removeBinding("two");
      callPOJO(true, false);
      
      AspectManager.instance().removeBinding("one");
      callPOJO(false, false);
   }
   
   public void testDynamicPerInstance() throws Exception
   {
      reset();
      @SuppressWarnings("unused")
      POJOPerInstance tmp = new POJOPerInstance();
      assertInstanceOf(tmp, Advised.class);
      assertInterceptors(false, false);
      
      addBinding("one", "all(org.jboss.test.aop.dynamic.POJOPerInstance)", PerInstanceInterceptor1.class);
      callPOJOPerInstance(true, false);
      
      addBinding("two", "all(org.jboss.test.aop.dynamic.POJOPerInstance)", PerInstanceInterceptor2.class);
      callPOJOPerInstance(true, true);

      AspectManager.instance().removeBinding("one");
      callPOJOPerInstance(false, true);
      
      addBinding("one", "all(org.jboss.test.aop.dynamic.POJOPerInstance)", PerInstanceInterceptor1.class);
      callPOJOPerInstance(true, true);
      
      AspectManager.instance().removeBinding("two");
      callPOJOPerInstance(true, false);
      
      AspectManager.instance().removeBinding("one");
      callPOJOPerInstance(false, false);
   }
   
   public void testClassProxyContainer() throws Exception
   {
      AspectManager manager = AspectManager.instance();
      //Add a binding before creating the proxy
      addBinding("one", "all(org.jboss.test.aop.dynamic.POJOProxy)", Interceptor1.class);

      POJOProxy pojo = new POJOProxy();
      AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
      params.setTarget(pojo);
         
      GeneratedAOPProxyFactory factory = new GeneratedAOPProxyFactory();
      POJOProxy proxy = (POJOProxy)factory.createAdvisedProxy(params);

      reset();
      proxy.method();
      assertInterceptors(true, false);
      
      addBinding("two", "all(org.jboss.test.aop.dynamic.POJOProxy)", Interceptor2.class);
      reset();
      proxy.method();
      assertInterceptors(true, true);

      manager.removeBinding("one");
      reset();
      proxy.method();
      assertInterceptors(false, true);
      
      addBinding("one", "all(org.jboss.test.aop.dynamic.POJOProxy)", Interceptor1.class);
      reset();
      proxy.method();
      assertInterceptors(true, true);
      
      manager.removeBinding("two");
      reset();
      proxy.method();
      assertInterceptors(true, false);
      
      manager.removeBinding("one");
      reset();
      proxy.method();
      assertInterceptors(false, false);
   }

   private void callPOJO(boolean int1, boolean int2)
   {
      reset();
      POJO pojo = new POJO();
      assertInterceptors(int1, int2);
      
      reset();
      pojo.field = 1;
      assertInterceptors(int1, int2);
      
      reset();
      assertEquals(1, pojo.field);
      assertInterceptors(int1, int2);
      
      reset();
      pojo.method();
      assertInterceptors(int1, int2);
   }
   
   private void callPOJOPerInstance(boolean int1, boolean int2)
   {
      reset();
      POJOPerInstance pojo = new POJOPerInstance();
      assertPerInstanceInterceptors(false, false);
      
      reset();
      pojo.method();
      assertPerInstanceInterceptors(int1, int2);
      
      reset();
      pojo.field = 1;
      assertPerInstanceInterceptors(int1, int2);
      
      reset();
      assertEquals(1, pojo.field);
      assertPerInstanceInterceptors(int1, int2);
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

   private void assertInterceptors(boolean int1, boolean int2)
   {
      assertEquals(int1, Interceptor1.intercepted);
      assertEquals(int2, Interceptor2.intercepted);
   }
   
   private void assertPerInstanceInterceptors(boolean int1, boolean int2)
   {
      assertEquals(int1, PerInstanceInterceptor1.intercepted);
      assertEquals(int2, PerInstanceInterceptor2.intercepted);
   }
   
   private void reset()
   {
      Interceptor1.intercepted = false;
      Interceptor2.intercepted = false;
      PerInstanceInterceptor1.intercepted = false;
      PerInstanceInterceptor2.intercepted = false;
   }
   
   private void addBinding(String name, String pointcut, Class<?> interceptor) throws ParseException
   {
      AspectManager manager = AspectManager.instance();
      AdviceBinding binding = new AdviceBinding(pointcut, null);
      binding.setName(name);
      InterceptorFactory factory = manager.getInterceptorFactory(interceptor.getName());
      binding.addInterceptorFactory(factory);
      manager.addBinding(binding);
   }
}
