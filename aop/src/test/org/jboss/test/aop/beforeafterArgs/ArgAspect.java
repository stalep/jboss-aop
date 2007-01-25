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
   
   static boolean after1 = false;
   static boolean after4 = false;
   
   static int before1X = 0;
   static int before2X = 0;
   static int before3Y = 0;
   static int before5X = 0;
   static int before5Y = 0;
   
   static int around5X = 0;
   static int around5Y = 0;
   
   static int after1X = 0;
   static int after4X = 0;
   static int after4Y = 0;
   
   public static void clear()
   {
      before1 = false;
      before2 = false;
      before3 = false;
      before4 = false;
      before5 = false;
      around4 = false;
      around5 = false;
      after4 = false;
      
      before1X = 0;
      before2X = 0;
      before3Y = 0;
      before5X = 0;
      before5Y = 0;
      around5X = 0;
      around5Y = 0;
      after1X = 0;
      after4X = 0;
      after4Y = 0;
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
   
   public void before3(@Arg(index=4) int y)
   {
      before3 = true;
      before3Y = y;
   }
   
   public void before4(@Args Object[] arguments)
   {
      before4 = true;
      arguments[0] = Integer.valueOf(((Integer) arguments[0]).intValue() * 5);
      arguments[4] = Integer.valueOf(((Integer) arguments[4]).intValue() * -17);
   }
   
   public void before5(@Arg int x, @Arg int y)
   {
      before5 = true;
      before5X = x;
      before5Y = y;
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
      invocation.setArguments(arguments);
      return invocation.invokeNext();
   }
   
   public int around5(@Arg(index = 4) int y, @Arg int x) throws Throwable
   {
      around5 = true;
      around5X = x;
      around5Y = y;
      return ((Integer) CurrentInvocation.proceed()).intValue();
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
   
   public void after4(@Arg int x, @Arg int y)
   {
      after4 = true;
      after4X = x;
      after4Y = y;
   }
   
   // TODO
   /*public void after5(@Arg(index = 4) int x, @Arg(index = -3) int y)
   {
      Assert.fail("This advice should never be executed");
   }*/
}