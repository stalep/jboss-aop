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

import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.CurrentInvocation;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Aspect used on overloaded around advice tests.
 *
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
// TODO temporary in this package
public class OverloadedAroundAspect
{
   static String around1 = null;
   static String around2 = null;
   static String around3 = null;
   static String around4 = null;
   static String around5 = null;
   static String around6 = null;
   static String around7 = null;
   static String around8 = null;
   static String around9 = null;
   static String around10 = null;
   static String around11 = null;
   static String around12 = null;
   
   public static void clear()
   {
      around1 = null;
      around2 = null;
      around3 = null;
      around4 = null;
      around5 = null;
      around6 = null;
      around7 = null;
      around8 = null;
      around9 = null;
      around10 = null;
      around11 = null;
      around12 = null;
   }

   /* AROUND1 ADVICE */
   
   public Object around1(Invocation invocation) throws Throwable
   {
      around1 = "defaultSignature";
      return invocation.invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around1 = "MethodInvocation,int,long";
      return invocation.invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around1 = "Invocation,int,long";
      return invocation.invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around1 = "Object,int,long";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1) throws Throwable
   {
      around1 = "MethodInvocation,int";
      return invocation.invokeNext();
   }

   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg long arg2) throws Throwable
   {
      around1 = "MethodInvocation,long";
      return invocation.invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1) throws Throwable
   {
      around1 = "Invocation,int";
      return invocation.invokeNext();
   }

   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg long arg2) throws Throwable
   {
      around1 = "Invocation,long";
      return invocation.invokeNext();
   }

   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around1 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around1 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around1 = "MethodInvocation";
      return invocation.invokeNext();
   }
   
   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around1 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around1(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around1 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around1(@Arg int arg1) throws Throwable
   {
      around1 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around1(@Arg long arg2) throws Throwable
   {
      around1 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around1() throws Throwable
   {
      around1 = "";
      return CurrentInvocation.proceed();
   }

   public Object around1(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }
   
   /* AROUND2 ADVICE */
   
   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around2 = "MethodInvocation,int,long";
      return invocation.invokeNext();
   }
   
   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around2 = "Invocation,int,long";
      return invocation.invokeNext();
   }
   
   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around2 = "Object,int,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1) throws Throwable
   {
      around2 = "MethodInvocation,int";
      return invocation.invokeNext();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg long arg2) throws Throwable
   {
      around2 = "MethodInvocation,long";
      return invocation.invokeNext();
   }
   
   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1) throws Throwable
   {
      around2 = "Invocation,int";
      return invocation.invokeNext();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg long arg2) throws Throwable
   {
      around2 = "Invocation,long";
      return invocation.invokeNext();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around2 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around2 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around2 = "MethodInvocation";
      return invocation.invokeNext();
   }
   
   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around2 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around2(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around2 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around2(@Arg int arg1) throws Throwable
   {
      around2 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around2(@Arg long arg2) throws Throwable
   {
      around2 = "long";
      return CurrentInvocation.proceed();
   }
   
   public Object around2() throws Throwable
   {
      around2 = "";
      return CurrentInvocation.proceed();
   }

   public Object around2(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND3 ADVICE */
   
   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around3 = "Invocation,int,long";
      return invocation.invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around3 = "Object,int,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1) throws Throwable
   {
      around3 = "MethodInvocation,int";
      return invocation.invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg long arg2) throws Throwable
   {
      around3 = "MethodInvocation,long";
      return invocation.invokeNext();
   }
   
   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1) throws Throwable
   {
      around3 = "Invocation,int";
      return invocation.invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg long arg2) throws Throwable
   {
      around3 = "Invocation,long";
      return invocation.invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around3 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around3 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around3 = "MethodInvocation";
      return invocation.invokeNext();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around3 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around3(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around3 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around3(@Arg int arg1) throws Throwable
   {
      around3 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around3(@Arg long arg2) throws Throwable
   {
      around3 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around3() throws Throwable
   {
      around3 = "";
      return CurrentInvocation.proceed();
   }

   public Object around3(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND4 ADVICE */
   
   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1, @Arg long arg2) throws Throwable
   {
      around4 = "Object,int,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1) throws Throwable
   {
      around4 = "MethodInvocation,int";
      return invocation.invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg long arg2) throws Throwable
   {
      around4 = "MethodInvocation,long";
      return invocation.invokeNext();
   }
   
   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1) throws Throwable
   {
      around4 = "Invocation,int";
      return invocation.invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg long arg2) throws Throwable
   {
      around4 = "Invocation,long";
      return invocation.invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around4 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around4 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around4 = "MethodInvocation";
      return invocation.invokeNext();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around4 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around4(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around4 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around4(@Arg int arg1) throws Throwable
   {
      around4 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around4(@Arg long arg2) throws Throwable
   {
      around4 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around4() throws Throwable
   {
      around4 = "";
      return CurrentInvocation.proceed();
   }

   public Object around4(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND5 ADVICE */
   
   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg int arg1) throws Throwable
   {
      around5 = "MethodInvocation,int";
      return invocation.invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation, @Arg long arg2) throws Throwable
   {
      around5 = "MethodInvocation,long";
      return invocation.invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1) throws Throwable
   {
      around5 = "Invocation,int";
      return invocation.invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg long arg2) throws Throwable
   {
      around5 = "Invocation,long";
      return invocation.invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around5 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around5 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around5 = "MethodInvocation";
      return invocation.invokeNext();
   }

   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around5 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around5(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around5 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around5(@Arg int arg1) throws Throwable
   {
      around5 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around5(@Arg long arg2) throws Throwable
   {
      around5 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around5() throws Throwable
   {
      around5 = "";
      return CurrentInvocation.proceed();
   }
   
   public Object around5(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND6 ADVICE */
   
   public Object around6(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg int arg1) throws Throwable
   {
      around6 = "Invocation,int";
      return invocation.invokeNext();
   }

   public Object aroun6(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation, @Arg long arg2) throws Throwable
   {
      around6 = "Invocation,long";
      return invocation.invokeNext();
   }

   public Object around6(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around6 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around6(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around6 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around6(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around6 = "MethodInvocation";
      return invocation.invokeNext();
   }
   
   public Object around6(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around6 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around6(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around6 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around6(@Arg int arg1) throws Throwable
   {
      around6 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around6(@Arg long arg2) throws Throwable
   {
      around6 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around6() throws Throwable
   {
      around6 = "";
      return CurrentInvocation.proceed();
   }

   public Object around6(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND7 ADVICE */
   
   public Object around7(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg int arg1) throws Throwable
   {
      around7 = "Object,int";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around7(@org.jboss.aop.advice.annotation.Invocation
         Object invocation, @Arg long arg2) throws Throwable
   {
      around7 = "Object,long";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around7(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around7 = "MethodInvocation";
      return invocation.invokeNext();
   }
   
   public Object around7(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around7 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around7(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around7 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around7(@Arg int arg1) throws Throwable
   {
      around7 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around7(@Arg long arg2) throws Throwable
   {
      around7 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around7() throws Throwable
   {
      around7 = "";
      return CurrentInvocation.proceed();
   }

   public Object around7(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND8 ADVICE */
   
   public Object around8(@org.jboss.aop.advice.annotation.Invocation
         MethodInvocation invocation) throws Throwable
   {
      around8 = "MethodInvocation";
      return invocation.invokeNext();
   }

   public Object around8(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around8 = "Object";
      return ((Invocation) invocation).invokeNext();
   }
   
   public Object around8(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around8 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around8(@Arg int arg1) throws Throwable
   {
      around8 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around8(@Arg long arg2) throws Throwable
   {
      around8 = "long";
      return CurrentInvocation.proceed();
   }
   
   public Object around8() throws Throwable
   {
      around8 = "";
      return CurrentInvocation.proceed();
   }

   public Object around8(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND9 ADVICE */
   
   public Object around9(@org.jboss.aop.advice.annotation.Invocation
         Object invocation) throws Throwable
   {
      around9 = "Object";
      return ((Invocation) invocation).invokeNext();
   }

   public Object around9(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around9 = "int,long";
      return CurrentInvocation.proceed();
   }
   
   public Object around9(@Arg int arg1) throws Throwable
   {
      around9 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around9(@Arg long arg2) throws Throwable
   {
      around9 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around9() throws Throwable
   {
      around9 = "";
      return CurrentInvocation.proceed();
   }

   public Object around9(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND10 ADVICE */
   
   public Object around10(@Arg int arg1, @Arg long arg2) throws Throwable
   {
      around10 = "int,long";
      return CurrentInvocation.proceed();
   }

   public Object around10(@Arg int arg1) throws Throwable
   {
      around10 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around10(@Arg long arg2) throws Throwable
   {
      around10 = "long";
      return CurrentInvocation.proceed();
   }

   public Object around10() throws Throwable
   {
      around10 = "";
      return CurrentInvocation.proceed();
   }
   
   public Object around10(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }

   /* AROUND11 ADVICE */
   
   public Object around11(@Arg int arg1) throws Throwable
   {
      around11 = "int";
      return CurrentInvocation.proceed();
   }

   public Object around11(@Arg long arg2) throws Throwable
   {
      around11 = "long";
      return CurrentInvocation.proceed();
   }
   
   public Object around11() throws Throwable
   {
      around11 = "";
      return CurrentInvocation.proceed();
   }
   
   public Object around11(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }
   
   /* AROUND12 ADVICE */
   
   public Object around12() throws Throwable
   {
      around12 = "";
      return CurrentInvocation.proceed();
   }
   
   public Object around12(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return null;
   }   
}