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

import junit.framework.Assert;

import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Return;
import org.jboss.aop.joinpoint.Invocation;

/**
 * Aspect used both on @Return parameter tests, and on advice return type tests.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ReturnAspect
{
   public static String aroundAdvice = null;
   public static Object aroundReturn = null;
   public static String afterAdvice = null;
   public static Object afterReturn = null;
   
   public static void clear()
   {
      aroundAdvice = null;
      aroundReturn = null;
      afterAdvice = null;
      afterReturn = null;
   }
   
   public void before(@Return Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing(@Return Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void around1(Invocation invocation) throws Exception
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void around2(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation) throws Exception
   {
      Assert.fail("This advice should never be executed");
   }
   
   public Object around3(Invocation invocation) throws Throwable
   {
      aroundAdvice = "around3";
      aroundReturn = invocation.invokeNext();
      return aroundAdvice;
   }
   
   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation) throws Throwable
   {
      aroundAdvice = "around4";
      aroundReturn = invocation.invokeNext();
      return aroundAdvice;
   }
   
   public Object around5()
   {
      aroundAdvice = "around5";
      return aroundAdvice;
   }
   
   public void around6()
   {
      Assert.fail("This advice should never be executed");
   }
   
   public String around7(@org.jboss.aop.advice.annotation.Invocation Invocation
         invocation) throws Throwable
   {
      aroundAdvice = "around7";
      aroundReturn = invocation.invokeNext();
      return aroundAdvice;
   }
   
   public SubValue around8()
   {
      aroundAdvice = "around8";
      return new SubValue(80);
   }
   
   public SuperValue around9(@org.jboss.aop.advice.annotation.Invocation Invocation
         invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }
   
   public SubValue around10(@org.jboss.aop.advice.annotation.Invocation Invocation
         invocation) throws Throwable
   {
      aroundAdvice = "around10";
      aroundReturn = invocation.invokeNext();
      return new SubValue(100);
   }
   
   public SuperValue around11(@org.jboss.aop.advice.annotation.Invocation Invocation
         invocation) throws Throwable
   {
      aroundAdvice = "around11";
      aroundReturn = invocation.invokeNext();
      return new SuperValue(110);
   }

   
   public void after1(@org.jboss.aop.advice.annotation.JoinPoint JoinPointInfo info) throws Exception
   {
      afterAdvice = "after1";
   }
   
   public void after2(@Return Object returnedValue) throws Exception
   {
      afterAdvice = "after2";
      afterReturn = returnedValue;
   }
   
   public Object after3(@JoinPoint JoinPointInfo info, @Return String returnedValue)
   throws Throwable
   {
      afterAdvice = "after3";
      afterReturn = returnedValue;
      return afterAdvice;
   }
   
   public Object after4(@Return Object returnedValue) throws Throwable
   {
      afterAdvice = "after4";
      afterReturn = returnedValue;
      return afterAdvice;
   }
   
   public Object after5()
   {
      afterAdvice = "after5";
      return afterAdvice;
   }
   
   public void after6()
   {
      afterAdvice = "after6";
   }
   
   public String after7(@JoinPoint JoinPointInfo joinPoint,
         @Return String returnedValue) throws Throwable
   {
      afterAdvice = "after7";
      afterReturn = returnedValue;
      return afterAdvice;
   }
   
   public SubValue after8()
   {
      afterAdvice = "after8";
      return new SubValue(800);
   }
   
   public SuperValue after9() throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }
   
   public SubValue after10(@Return SubValue returnedValue) throws Throwable
   {
      afterAdvice = "after10";
      afterReturn = returnedValue;
      return new SubValue(1000);
   }
   
   public SubValue after11(@Return SuperValue returnedValue) throws Throwable
   {
      afterAdvice = "after11";
      afterReturn = returnedValue;
      return new SubValue(1100);
   }
}