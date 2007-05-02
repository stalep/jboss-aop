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

import java.util.Collection;

import junit.framework.Assert;

import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.Args;
import org.jboss.aop.advice.annotation.Thrown;
import org.jboss.aop.joinpoint.CurrentInvocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Aspect used on @Arg parameter tests (this class complements <code>
 * org.jboss.test.aop.args.ArgAspect</code>, by containing advices that are allowed
 * only with generated advisors).
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 * @see org.jboss.test.aop.args.ArgAspect
 */
public class ArgAspect
{
   static boolean before1 = false;
   static boolean before2 = false;
   static boolean before3 = false;
   static boolean before4 = false;
   static boolean before5 = false;
   
   static boolean around4 = false;
   static boolean around5 = false;
   static boolean around6 = false;
   
   static boolean after1 = false;
   static boolean after4 = false;
   static boolean after6 = false;
   
   static boolean finally1 = false;
   static boolean finally2 = false;
   
   static int before1X = 0;
   static int before2X = 0;
   static int before3Q = 0;
   static int before5X = 0;
   static int before5Q = 0;
   
   static int around5X = 0;
   static int around5Q = 0;
   
   static Object[] around6Args = null;
   
   static int after1X = 0;
   static int after4X = 0;
   static int after4Q = 0;
   static Object[] after6Args = null;
   
   static int finally1X = 0;
   static Object[] finally2Args = null;
   
   static boolean beforeInterface1 = false;
   static boolean beforeInterface2 = false;
   static boolean beforeInterface3 = false;
   static boolean aroundInterface1 = false;
   static boolean aroundInterface2 = false;
   static boolean aroundInterface3 = false;
   static boolean afterInterface1 = false;
   static boolean afterInterface2 = false;
   static boolean afterInterface3 = false;
   static boolean throwingInterface1 = false;
   static boolean throwingInterface2 = false;
   static boolean throwingInterface3 = false;
   static boolean finallyInterface1 = false;
   static boolean finallyInterface2 = false;
   static boolean finallyInterface3 = false;
   
   static boolean beforeInverted1 = false;
   static boolean aroundInverted1 = false;
   static boolean afterInverted1 = false;
   static boolean throwingInverted1 = false;
   static boolean finallyInverted1 = false;
   
   public static void clear()
   {
      before1 = false;
      before2 = false;
      before3 = false;
      before4 = false;
      before5 = false;
      around4 = false;
      around5 = false;
      around6 = false;
      after1 = false;
      after4 = false;
      after6 = false;
      finally1 = false;
      finally2 = false;
      
      before1X = 0;
      before2X = 0;
      before3Q = 0;
      before5X = 0;
      before5Q = 0;
      around5X = 0;
      around5Q = 0;
      around6Args = null;
      after1X = 0;
      after4X = 0;
      after4Q = 0;
      after6Args = null;
      finally1X = 0;
      finally2Args = null;
      
      beforeInterface1 = false;
      beforeInterface2 = false;
      beforeInterface3 = false;
      aroundInterface1 = false;
      aroundInterface2 = false;
      aroundInterface3 = false;
      afterInterface1 = false;
      afterInterface2 = false;
      afterInterface3 = false;
      throwingInterface1 = false;
      throwingInterface2 = false;
      throwingInterface3 = false;
      finallyInterface1 = false;
      finallyInterface2 = false;
      finallyInterface3 = false;
      
      beforeInverted1 = false;
      aroundInverted1 = false;
      afterInverted1 = false;
      throwingInverted1 = false;
      finallyInverted1 = false;
   }
   
   public void before1(@Arg(index=0) int x)
   {
      before1 = true;
      before1X = x;
   }
   
   public void before2(@Arg int x)
   {
      before2 = true;
      before2X = x;
   }
   
   public void before3(@Arg(index=4) int q)
   {
      before3 = true;
      before3Q = q;
   }
   
   public void before4(@Args Object[] arguments)
   {
      before4 = true;
      arguments[0] = Integer.valueOf(((Integer) arguments[0]).intValue() * 5);
      if (arguments.length > 4)
      {
         arguments[4] = Integer.valueOf(((Integer) arguments[4]).intValue() * -17);
      }
   }
   
   public void before5(@Arg int x, @Arg int q)
   {
      before5 = true;
      before5X = x;
      before5Q = q;
   }
   
   public int around1(@Arg int x, @Arg double y, @Arg float z, @Arg String str, @Arg long q)
   {
      Assert.fail("This advice should never be executed");
      return 0;
   }

   public int around2(@Arg int x, @Arg int q, @Arg float z, @Arg Collection str, @Arg double y)
   {
      Assert.fail("This advice should never be executed");
      return 0;
   }
   
   public int around3(@Arg int x, @Arg int q, @Arg int w, @Arg String str, @Arg double y, @Arg float z)
   {
      Assert.fail("This advice should never be executed");
      return 0;
   }
   
   public Object around4(MethodInvocation invocation) throws Throwable
   {
      around4 = true;
      Object[] arguments = invocation.getArguments();
      arguments[0] = Integer.valueOf(((Integer) arguments[0]).intValue() - 8);
      arguments[4] = Integer.valueOf(((Integer) arguments[4]).intValue() + 51);
      return invocation.invokeNext();
   }
   
   public int around5(@Arg(index = 4) int q, @Arg int x) throws Throwable
   {
      around5 = true;
      around5X = x;
      around5Q = q;
      return ((Integer) CurrentInvocation.proceed()).intValue();
   }
   
   public Object around6(MethodInvocation invocation) throws Throwable
   {
      around6 = true;
      around6Args = new Object[4];
      around6Args[0] = 6;
      around6Args[1] = 12.0;
      around6Args[2] = (float) 24;
      around6Args[3] = 48;
      invocation.setArguments(around6Args);
      return Integer.valueOf(((Integer) invocation.invokeNext()).intValue() + 6);
   }
   
   public void after1(@Arg String str, @Arg(index = 0) int x)
   {
      after1 = true;
      after1X = x;
   }
   
   public void after2(@Arg float z, @Arg(index = 2) String str)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after3(@Arg(index = 5) double z)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after4(@Arg int x, @Arg int q)
   {
      after4 = true;
      after4X = x;
      after4Q = q;
   }
   
   public void after5(@Arg(index = 4) int x, @Arg(index = -3) int y)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after6(@Args Object[] args)
   {
      after6 = true;
      args[0] = Integer.valueOf(((Integer) args[0]).intValue() - 1);
      after6Args = args;
   }
   
   public void finally1(@Arg int x)
   {
      finally1 = true;
      finally1X = x;
   }
   
   public void finally2(@Args Object[] args)
   {
      finally2 = true;
      finally2Args = args;
   }
   
   public void finally3(@Arg Collection arg)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void beforeInterface1(@Arg Interface param)
   {
      beforeInterface1 = true;
   }
   
   public void beforeInterface2(@Arg SuperInterface param)
   {
      beforeInterface2 = true;
   }
   
   public void beforeInterface3(@Arg Object param)
   {
      beforeInterface3 = true;
   }
   
   public void beforeInterface4(@Arg Implementor param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void beforeInterface5(@Arg SubInterface param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public Object aroundInterface1(@Arg Interface param) throws Throwable
   {
      aroundInterface1 = true;
      return CurrentInvocation.proceed();
   }
   
   public Object aroundInterface2(@Arg SuperInterface param) throws Throwable
   {
      aroundInterface2 = true;
      return CurrentInvocation.proceed();
   }
   
   public Object aroundInterface3(@Arg Object param) throws Throwable
   {
      aroundInterface3 = true;
      return CurrentInvocation.proceed();
   }
   
   public Object aroundInterface4(@Arg Implementor param) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return CurrentInvocation.proceed();
   }
   
   public Object aroundInterface5(@Arg SubInterface param) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return CurrentInvocation.proceed();
   }
   
   public void afterInterface1(@Arg Interface param)
   {
      afterInterface1 = true;
   }
   
   public void afterInterface2(@Arg SuperInterface param)
   {
      afterInterface2 = true;
   }
   
   public void afterInterface3(@Arg Object param)
   {
      afterInterface3 = true;
   }
   
   public void afterInterface4(@Arg Implementor param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void afterInterface5(@Arg SubInterface param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwingInterface1(@Thrown Throwable thrown, @Arg Interface param)
   {
      throwingInterface1 = true;
   }
   
   public void throwingInterface2(@Thrown Throwable thrown, @Arg SuperInterface param)
   {
      throwingInterface2 = true;
   }
   
   public void throwingInterface3(@Thrown Throwable thrown, @Arg Object param)
   {
      throwingInterface3 = true;
   }
   
   public void throwingInterface4(@Thrown Throwable thrown, @Arg Implementor param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwingInterface5(@Thrown Throwable thrown, @Arg SubInterface param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void finallyInterface1(@Arg Interface param)
   {
      finallyInterface1 = true;
   }
   
   public void finallyInterface2(@Arg SuperInterface param)
   {
      finallyInterface2 = true;
   }
   
   public void finallyInterface3(@Arg Object param)
   {
      finallyInterface3 = true;
   }
   
   public void finallyInterface4(@Arg Implementor param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void finallyInterface5(@Arg SubInterface param)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void beforeInvertedArgs1(@Arg Object arg2, @Arg (index = 0) String arg1)
   {
      beforeInverted1 = true;
   }
   
   public void beforeInvertedArgs2(@Arg (index = 0) Object arg2,
         @Arg (index = 0) String arg1)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public Object aroundInvertedArgs1(@Arg Object arg2, @Arg (index = 0) String arg1)
      throws Throwable
   {
      aroundInverted1 = true;
      return CurrentInvocation.proceed();
   }
   
   public void aroundInvertedArgs2(@Arg (index = 0) Object arg2,
         @Arg (index = 0) String arg1)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void afterInvertedArgs1(@Arg Object arg2, @Arg (index = 0) String arg1)
   {
      afterInverted1 = true;
   }
   
   public void afterInvertedArgs2(@Arg (index = 0) Object arg2,
         @Arg (index = 0) String arg1)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwingInvertedArgs1(@Thrown Throwable thrown, @Arg Object arg2,
         @Arg (index = 0) String arg1)
   {
      throwingInverted1 = true;
   }
   
   public void throwingInvertedArgs2(@Arg (index = 0) Object arg2,
         @Arg (index = 0) String arg1)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void finallyInvertedArgs1(@Arg Object arg2, @Arg (index = 0) String arg1)
   {
      finallyInverted1 = true;
   }
   
   public void finallyInvertedArgs2(@Arg (index = 0) Object arg2,
         @Arg (index = 0) String arg1)
   {
      Assert.fail("This advice should never be executed");
   }
}