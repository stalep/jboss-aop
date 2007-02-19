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
package org.jboss.test.aop.proxy;

import java.io.Externalizable;
import java.io.InputStream;
import java.io.Serializable;
import java.rmi.MarshalledObject;

import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassInstanceAdvisor;
import org.jboss.aop.InstanceDomain;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AdviceFactory;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.proxy.ClassProxyFactory;
import org.jboss.aop.proxy.Proxy;
import org.jboss.aop.proxy.ProxyFactory;
import org.jboss.aop.proxy.ProxyMixin;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.aop.proxy.container.ClassProxyContainer;
import org.jboss.aop.proxy.container.ContainerProxyCacheKey;
import org.jboss.aop.proxy.container.ContainerProxyFactory;
import org.jboss.aop.proxy.container.Delegate;
import org.jboss.test.aop.AOPTestWithSetup;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ProxyTestCase extends AOPTestWithSetup
{

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("ProxyTester");
      suite.addTestSuite(ProxyTestCase.class);
      return suite;
   }

   public ProxyTestCase(String name)
   {
      super(name);
   }

   public void testProxy() throws Exception
   {
      Class[] mixIntfs = {MixinInterface.class};
      ProxyMixin mixin = new ProxyMixin(new Mixin(), mixIntfs);
      ProxyMixin[] mixins = {mixin};
      Class[] intfs = {SomeInterface.class};
      ClassInstanceAdvisor advisor = new ClassInstanceAdvisor();
      advisor.insertInterceptor(new EchoInterceptor());

      /*
      CtClass clazz = ProxyFactory.createProxyCtClass(Thread.currentThread().getContextClassLoader(), mixins, intfs);
      FileOutputStream fo = new FileOutputStream(clazz.getName() + ".class");
      DataOutputStream dos = new DataOutputStream(fo);
      clazz.toBytecode(dos);
      dos.close();

      System.out.println("**************");
      */

      Proxy proxy = ProxyFactory.createInterfaceProxy(Thread.currentThread().getContextClassLoader(), intfs, mixins, advisor);

      MixinInterface mi = (MixinInterface) proxy;
      assertEquals(mi.hello("mixin"), "mixin");
      SomeInterface si = (SomeInterface) proxy;
      assertEquals(si.helloWorld(), "echoed");

      MarshalledObject mo = new MarshalledObject(proxy);
      proxy = (Proxy) mo.get();
      mi = (MixinInterface) proxy;
      assertEquals(mi.hello("mixin"), "mixin");
      si = (SomeInterface) proxy;
      assertEquals(si.helloWorld(), "echoed");
   }

   public void testClassProxy() throws Exception
   {
      Class[] mixIntfs = {MixinInterface.class};
      ProxyMixin mixin = new ProxyMixin(new Mixin(), mixIntfs);
      ProxyMixin[] mixins = {mixin};
      ClassInstanceAdvisor advisor = new ClassInstanceAdvisor();
      advisor.insertInterceptor(new EchoInterceptor());
      POJO proxy = (POJO) ClassProxyFactory.newInstance(POJO.class, mixins, advisor);

      /*
      CtClass clazz = ClassProxyFactory.createProxyCtClass(mixins, POJO.class);
      FileOutputStream fo = new FileOutputStream(clazz.getName() + ".class");
      DataOutputStream dos = new DataOutputStream(fo);
      clazz.toBytecode(dos);
      dos.close();
      */


      MixinInterface mi = (MixinInterface) proxy;
      assertEquals(mi.hello("mixin"), "mixin");

      MarshalledObject mo = new MarshalledObject(proxy);
      proxy = (POJO) mo.get();
      mi = (MixinInterface) proxy;
      assertEquals(mi.hello("mixin"), "mixin");
      assertEquals(proxy.helloWorld(), "echoed");
   }

   public void testContainerProxyCacheKey() throws Exception
   {
      ContainerProxyCacheKey key1 = new ContainerProxyCacheKey(this.getClass(), new Class[] {Serializable.class, InputStream.class, Externalizable.class}, null);
      ContainerProxyCacheKey key2 = new ContainerProxyCacheKey(this.getClass(), new Class[] {Serializable.class, Externalizable.class}, null);
      ContainerProxyCacheKey key3 = new ContainerProxyCacheKey(this.getClass(), new Class[] {Externalizable.class, InputStream.class, Serializable.class}, null);
      
      assertFalse(key1.equals(key2));
      assertTrue(key1.equals(key3));
      
      assertFalse(key1.hashCode() == key2.hashCode());
      assertTrue(key1.hashCode() == key3.hashCode());
   }
   
   public void testContainerProxy() throws Exception
   {
      InstanceDomain domain = new InstanceDomain(AspectManager.instance(), "blah", false);

      
      InterfaceIntroduction intro = new InterfaceIntroduction("intro", "*", null);
      String[] intfs = {MixinInterface.class.getName()};
      InterfaceIntroduction.Mixin mixin = new InterfaceIntroduction.Mixin(Mixin.class.getName(), intfs, null, false);
      intro.getMixins().add(mixin);
      domain.addInterfaceIntroduction(intro);

      
      AspectDefinition def = new AspectDefinition("aspect", Scope.PER_VM, new GenericAspectFactory(EchoInterceptor.class.getName(), null));
      domain.addAspectDefinition(def);
      AdviceFactory advice = new AdviceFactory(def, "invoke");
      domain.addInterceptorFactory(advice.getName(), advice);
      //PointcutExpression pointcut = new PointcutExpression("pointcut", "execution(java.lang.String $instanceof{" + POJO.class.getName() + "}->helloWorld(..))");
      {
      PointcutExpression pointcut = new PointcutExpression("pointcut", "execution(java.lang.String " + POJO.class.getName() + "->helloWorld(..))");
      domain.addPointcut(pointcut);
      InterceptorFactory[] interceptors = {advice};
      AdviceBinding binding = new AdviceBinding("pojo-binding", pointcut, null, null, interceptors);
      domain.addBinding(binding);
      }

      {
      PointcutExpression pointcut = new PointcutExpression("mixin-pointcut", "execution(java.lang.String $instanceof{" + MixinInterface.class.getName() + "}->intercepted(..))");
      domain.addPointcut(pointcut);
      InterceptorFactory[] interceptors = {advice};
      AdviceBinding binding = new AdviceBinding("mixin-binding", pointcut, null, null, interceptors);
      domain.addBinding(binding);
      }


      /*
      CtClass clazz = ContainerProxyFactory.createProxyCtClass(ContainerProxyFactory.getIntroductions(POJO.class, container), POJO.class);
      FileOutputStream fo = new FileOutputStream(clazz.getName() + ".class");
      DataOutputStream dos = new DataOutputStream(fo);
      clazz.toBytecode(dos);
      dos.close();
      */

      Class proxyClass = ContainerProxyFactory.getProxyClass(POJO.class, domain);
      ClassProxyContainer container = new ClassProxyContainer("test", domain);
      domain.setAdvisor(container);
      container.setClass(proxyClass);
      container.initializeClassContainer();
      POJO proxy = (POJO) proxyClass.newInstance();
      AspectManaged cp = (AspectManaged)proxy;
      cp.setAdvisor(container);
      Delegate delegate = (Delegate)cp;
      delegate.setDelegate(new POJO());

      MixinInterface mi = (MixinInterface) proxy;
      System.out.println("--- mixin");
      assertEquals(mi.hello("mixin"), "mixin");
      System.out.println("--- hw");
      assertEquals("echoed", proxy.helloWorld());
      System.out.println("--- icptd");
      assertEquals("echoed", mi.intercepted("error"));

   }
   
   public void testContainerProxyWithFinalMethods() throws Exception
   {
      InstanceDomain domain = new InstanceDomain(AspectManager.instance(), "test", false);
      AspectDefinition def = new AspectDefinition("aspect", Scope.PER_VM, new GenericAspectFactory(EchoInterceptor.class.getName(), null));
      domain.addAspectDefinition(def);
      AdviceFactory advice = new AdviceFactory(def, "invoke");
      domain.addInterceptorFactory(advice.getName(), advice);
      {
      PointcutExpression pointcut = new PointcutExpression("pointcut", "execution(* " + POJOWithFinalMethods.class.getName() + "->*(..))");
      domain.addPointcut(pointcut);
      InterceptorFactory[] interceptors = {advice};
      AdviceBinding binding = new AdviceBinding("pojo-binding", pointcut, null, null, interceptors);
      domain.addBinding(binding);
      }

      Class proxyClass = ContainerProxyFactory.getProxyClass(POJOWithFinalMethods.class, domain);
      ClassProxyContainer container = new ClassProxyContainer("test", domain);
      domain.setAdvisor(container);
      container.setClass(proxyClass);
      container.initializeClassContainer();
      POJOWithFinalMethods proxy = (POJOWithFinalMethods) proxyClass.newInstance();
      AspectManaged cp = (AspectManaged)proxy;
      cp.setAdvisor(container);
      Delegate delegate = (Delegate)cp;
      delegate.setDelegate(new POJOWithFinalMethods());
      
      EchoInterceptor.intercepted = false;
      proxy.method();
      assertTrue(EchoInterceptor.intercepted);
      
      EchoInterceptor.intercepted = false;
      proxy.finalMethod();
      assertFalse(EchoInterceptor.intercepted);
   }
}
