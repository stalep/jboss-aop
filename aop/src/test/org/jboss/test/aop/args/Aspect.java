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
package org.jboss.test.aop.args;

import org.jboss.aop.advice.Arg;
import org.jboss.aop.advice.Invocation;
import org.jboss.aop.joinpoint.CurrentInvocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class Aspect implements PojoInterface
{
   public static boolean echoCalled = false;

   public String echo(@Arg String arg)
   {
      echoCalled = true;

      if (!arg.equals("hello")) throw new RuntimeException("Args don't match");

      try
      {
         return (String) CurrentInvocation.proceed();
      }
      catch (Throwable throwable)
      {
         throw new RuntimeException(throwable);
      }
   }

   public static boolean wrapped = false;

   public Object wrap(MethodInvocation invocation) throws Throwable
   {
      wrapped = true;
      return invocation.invokeNext();
   }

   public static boolean args = false;
// TODO talk to kabir returned Object
   public int bunchArgs(@Arg int x, @Arg double y, @Arg float z,
         @Arg String str, @Arg int q) throws Throwable
   {
      args = true;
      return (Integer)CurrentInvocation.proceed();
   }

   public static boolean argsWithInvocation = false;
// TODO idem
   public int bunchArgsWithInvocation(
         @Invocation MethodInvocation invocation,
         @Arg int x, @Arg double y, @Arg float z, @Arg String str, @Arg int q) throws Throwable
   {
      argsWithInvocation = true;
      return (Integer) invocation.invokeNext();
   }


   public static boolean bunchCalled = false;

   public int bunch(@Arg int x, @Arg double y, @Arg float z, @Arg String str, @Arg int q)
   {
      bunchCalled = true;

      if (x != 1 && y != 2.2 && z != 3.3F && !str.equals("four") && q != 5)
      {
         throw new RuntimeException("Arguments don't match");
      }
      try
      {
         return ((Integer) CurrentInvocation.proceed()).intValue();
      }
      catch (Throwable throwable)
      {
         throw new RuntimeException(throwable);
      }
   }

   public static boolean arg1Called = false;
// TODO Idem
   public int arg1(@Arg int x) throws Throwable
   {
      arg1Called = true;

      if (x != 1) throw new RuntimeException("Args don't match");
      return (Integer) CurrentInvocation.proceed();
   }

   public static boolean arg2Called = false;
// TODO idem
   public int arg2(@Arg double y) throws Throwable
   {
      arg2Called = true;

      if (y != 2.2) throw new RuntimeException("Args don't match");
      return (Integer) CurrentInvocation.proceed();
   }

   public static boolean arg3Called = false;
// TODO Idem
   public int arg3(@Arg float z) throws Throwable
   {
      arg3Called = true;

      if (z != 3.3F) throw new RuntimeException("Args don't match for arg3: " + z);
      return (Integer) CurrentInvocation.proceed();
   }

   public static boolean arg4Called = false;
// TODO Idem
   public int arg4(@Arg String str) throws Throwable
   {
      arg4Called = true;

      if (!str.equals("four")) throw new RuntimeException("Args don't match");
      return (Integer) CurrentInvocation.proceed();
   }

   public static boolean arg15Called = false;
// TODO Idem
   public int arg15(@Arg int x, @Arg int q) throws Throwable
   {
      arg15Called = true;

      if (x != 1 && q != 5) throw new RuntimeException("Args don't match");
      return (Integer) CurrentInvocation.proceed();
   }

   public static boolean arg24Called = false;
// TODO Idem
   public int arg24(@Arg double y, @Arg String str) throws Throwable
   {
      arg24Called = true;

      if (y != 2.2 && !str.equals("four")) throw new RuntimeException("Args don't match");
      return (Integer) CurrentInvocation.proceed();
   }
}