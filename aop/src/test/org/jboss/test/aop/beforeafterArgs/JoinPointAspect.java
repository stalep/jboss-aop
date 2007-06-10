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

import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Thrown;
import org.jboss.aop.joinpoint.Construction;
import org.jboss.aop.joinpoint.ConstructorCall;
import org.jboss.aop.joinpoint.ConstructorCallByConstructor;
import org.jboss.aop.joinpoint.ConstructorCallByMethod;
import org.jboss.aop.joinpoint.ConstructorExecution;
import org.jboss.aop.joinpoint.FieldAccess;
import org.jboss.aop.joinpoint.JoinPointBean;
import org.jboss.aop.joinpoint.MethodCall;
import org.jboss.aop.joinpoint.MethodCallByConstructor;
import org.jboss.aop.joinpoint.MethodCallByMethod;
import org.jboss.aop.joinpoint.MethodExecution;

/**
 * Aspect used on @JoinPoint parameter tests.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class JoinPointAspect
{
   static String beforeAdvice = null;
   static JoinPointBean beforeJoinPoint = null;
   static String afterAdvice = null;
   static JoinPointBean afterJoinPoint = null;
   static String throwingAdvice = null;
   static JoinPointBean throwingJoinPoint = null;
   static String finallyAdvice = null;
   static JoinPointBean finallyJoinPoint = null;
   
   public static void clear()
   {
      beforeAdvice = null;
      beforeJoinPoint = null;
      afterAdvice = null;
      afterJoinPoint = null;
      throwingAdvice = null;
      throwingJoinPoint = null;
      finallyAdvice = null;
      finallyJoinPoint = null;
   }
   
   public void before1()
   {
      beforeAdvice = "before1";
   }
   
   public void before2(@JoinPoint Object joinPoint)
   {
      beforeAdvice = "before2";
      beforeJoinPoint = (JoinPointBean) joinPoint;
   }
   
   public void before3(@JoinPoint JoinPointBean joinPoint)
   {
      beforeAdvice = "before3";
      beforeJoinPoint = joinPoint;
   }   
   
   public void before4(@JoinPoint MethodExecution joinPoint)
   {
      beforeAdvice = "before4";
      beforeJoinPoint = (JoinPointBean) joinPoint;
   }
   
   public void before5(@JoinPoint MethodExecution joinPoint)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before6(@JoinPoint ConstructorCallByConstructor joinPoint)
   {
      beforeAdvice = "before6";
      beforeJoinPoint = joinPoint;
   }
   
   public void before7(@JoinPoint MethodCallByConstructor joinPoint)
   {
      beforeAdvice="before7";
      beforeJoinPoint = joinPoint;
   }
   
   public void before8(@JoinPoint MethodCallByMethod joinPoint)
   {
      beforeAdvice="before8";
      beforeJoinPoint = joinPoint;
   }
   
   public void after1(@JoinPoint FieldAccess joinPoint)
   {
      afterAdvice = "after1";
      afterJoinPoint = joinPoint;
   }
   
   public void after2(@JoinPoint ConstructorExecution constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after3()
   {
      afterAdvice = "after3";
   }
   
   public void after4(@JoinPoint Object joinPoint)
   {
      afterAdvice = "after4";
      afterJoinPoint = (JoinPointBean) joinPoint;
   }
   
   public void after5(@JoinPoint JoinPointBean joinPoint)
   {
      afterAdvice = "after5";
      afterJoinPoint = joinPoint;
   }
   
   public void after6(@JoinPoint MethodCall joinPoint)
   {
      afterAdvice = "after6";
      afterJoinPoint = joinPoint;
   }
   
   public void after7(@JoinPoint ConstructorExecution joinPoint)
   {
      afterAdvice = "after7";
      afterJoinPoint = joinPoint;
   }
   
   public void throwing1(@Thrown Throwable throwable, @JoinPoint Object joinPoint)
   {
      throwingAdvice = "throwing1";
      throwingJoinPoint = (JoinPointBean) joinPoint;
   }
   
   public void throwing2(@JoinPoint ConstructorExecution joinPoint,
         @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing2";
      throwingJoinPoint = joinPoint;
   }
   
   public void throwing3(@Thrown Throwable throwable,
         @JoinPoint JoinPointBean joinPoint)
   {
      throwingAdvice = "throwing3";
      throwingJoinPoint = joinPoint;
   }
   
   public void throwing4(@Thrown Throwable throwable)
   {
      throwingAdvice = "throwing4";
   }
   
   public void throwing5(@JoinPoint MethodExecution joinPoint, @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing5";
      throwingJoinPoint = joinPoint;
   }
   
   public void throwing6(@JoinPoint ConstructorCallByConstructor joinPoint, @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing6";
      throwingJoinPoint = joinPoint;
   }
   
   public void throwing7(@JoinPoint ConstructorCallByMethod joinPoint, @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing7";
      throwingJoinPoint = joinPoint;
   }
   
   public void throwing8(@JoinPoint MethodCall joinPoint, @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing8";
      throwingJoinPoint = joinPoint;
   }

   public void finally1(@JoinPoint FieldAccess joinPoint)
   {
      finallyAdvice = "finally1";
      finallyJoinPoint = joinPoint;
   }
   
   public void finally2()
   {
      finallyAdvice = "finally2";
   }
   
   public void finally3(@JoinPoint Object joinPoint)
   {
      finallyAdvice = "finally3";
      finallyJoinPoint = (JoinPointBean) joinPoint;
   }
   
   public void finally4(@JoinPoint ConstructorExecution joinPoint)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void finally5(@JoinPoint ConstructorCall joinPoint)
   {
      finallyAdvice = "finally5";
      finallyJoinPoint = joinPoint;
   }
   
   public void finally6(@JoinPoint ConstructorCallByMethod joinPoint)
   {
      finallyAdvice = "finally6";
      finallyJoinPoint = joinPoint;
   }
   
   public void finally7(@JoinPoint JoinPointBean joinPoint)
   {
      finallyAdvice = "finally7";
      finallyJoinPoint = joinPoint;
   }
   
   public void finally8(@JoinPoint Construction joinPoint)
   {
      finallyAdvice = "finally8";
      finallyJoinPoint = joinPoint;
   }
}