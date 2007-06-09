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
import org.jboss.aop.joinpoint.ConstructorExecution;
import org.jboss.aop.joinpoint.FieldAccess;
import org.jboss.aop.joinpoint.JoinPointBean;
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
   
   public void before2(@JoinPoint Object joinPointInfo)
   {
      beforeAdvice = "before2";
      beforeJoinPoint = (JoinPointBean) joinPointInfo;
   }
   
   public void before3(@JoinPoint JoinPointBean joinPointInfo)
   {
      beforeAdvice = "before3";
      beforeJoinPoint = joinPointInfo;
   }   
   
   public void before4(@JoinPoint MethodExecution joinPointInfo)
   {
      beforeAdvice = "before4";
      beforeJoinPoint = (JoinPointBean) joinPointInfo;
   }
   
   public void before5(@JoinPoint MethodExecution joinPointInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after1(@JoinPoint FieldAccess joinPointInfo)
   {
      afterAdvice = "after1";
      afterJoinPoint = joinPointInfo;
   }
   
   public void after2(@JoinPoint ConstructorExecution constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after3()
   {
      afterAdvice = "after3";
   }
   
   public void after4(@JoinPoint Object joinPointInfo)
   {
      afterAdvice = "after4";
      afterJoinPoint = (JoinPointBean) joinPointInfo;
   }
   
   public void after5(@JoinPoint JoinPointBean joinPointInfo)
   {
      afterAdvice = "after5";
      afterJoinPoint = joinPointInfo;
   }
   
   public void throwing1(@Thrown Throwable throwable, @JoinPoint Object joinPointInfo)
   {
      throwingAdvice = "throwing1";
      throwingJoinPoint = (JoinPointBean) joinPointInfo;
   }
   
   public void throwing2(@JoinPoint ConstructorExecution joinPointInfo,
         @Thrown Throwable throwable)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing3(@Thrown Throwable throwable,
         @JoinPoint JoinPointBean joinPointInfo)
   {
      throwingAdvice = "throwing3";
      throwingJoinPoint = joinPointInfo;
   }
   
   public void throwing4(@Thrown Throwable throwable)
   {
      throwingAdvice = "throwing4";
   }
   
   public void throwing5(@JoinPoint MethodExecution joinPointInfo, @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing5";
      throwingJoinPoint = joinPointInfo;
   }
   

   public void finally1(@JoinPoint FieldAccess joinPointInfo)
   {
      finallyAdvice = "finally1";
      finallyJoinPoint = joinPointInfo;
   }
   
   public void finally2()
   {
      finallyAdvice = "finally2";
   }
   
   public void finally3(@JoinPoint Object joinPointInfo)
   {
      finallyAdvice = "finally3";
      finallyJoinPoint = (JoinPointBean) joinPointInfo;
   }
   
   public void finally4(@JoinPoint ConstructorExecution joinPointInfo)
   {
      Assert.fail("This advice should never be executed");
   }
}