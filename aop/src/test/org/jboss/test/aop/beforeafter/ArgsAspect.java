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
package org.jboss.test.aop.beforeafter;

import junit.framework.Assert;

import org.jboss.aop.ConstructorInfo;
import org.jboss.aop.FieldInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Return;
import org.jboss.aop.advice.annotation.Thrown;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class ArgsAspect
{

   public static String before;
   public static String after;
   public static String throwing;
   public static Throwable exception;


   public static void clear()
   {
      before = null;
      after = null;
      throwing = null;
      exception = null;
      POJO.joinPointRun = false;
   }
   
   public void before(@Arg boolean b, @Arg int i)
   {
      before = "before1";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public void before(@JoinPoint MethodInfo mjp, @Arg int i)
   {
      before = "before2";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public void before(@JoinPoint FieldInfo fjp, @Arg int i)
   {
      before = "before3";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public void before(@JoinPoint FieldInfo fjp)
   {
      before = "before4";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public void before(@JoinPoint FieldInfo fjp, @Arg SubValue val)
   {
      before = "before5";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public void before(@Arg SubValue sup, @Arg SubValue sub)
   {
      before = "before6";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public void before(@Arg SuperValue sup, @Arg SubValue sub)
   {
      before = "before7";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public POJO after(@JoinPoint MethodInfo mjp, @Return POJO ret, @Arg int i,
         @Arg long l)
   {
      after = "after1";
      Assert.assertTrue(POJO.joinPointRun);
      return ret;
   }
   
   public POJO after(@JoinPoint ConstructorInfo cjp, @Return POJO ret)
   {
      after = "after2";
      Assert.assertTrue(POJO.joinPointRun);
      return ret;
   }
   
   //This should be able to handle writes
   public int after(@JoinPoint FieldInfo fp, @Arg int i)
   {
      after = "after3";
      return i;
   }
   
   //This should be able to handle reads
   public SubValue after(@JoinPoint FieldInfo fp, @Return SubValue ret)
   {
      after = "after4";
      ret.doubleValue();
      return ret;
   }
      
   //This should be able to handle reads
   public SuperValue after(@Return SuperValue ret)
   {
      after = "after5";
      ret.doubleValue();
      return ret;
   }

   public SubValue after(@Return SuperValue ret, @Arg SuperValue sup, @Arg SuperValue sup2)
   {
      throw new RuntimeException("Should not be called");
   }
   
   public SuperValue after(@Return SuperValue ret, @Arg SubValue sup, @Arg SuperValue sub)
   {
      after = "after6";
      ret.doubleValue();
      Assert.assertTrue(POJO.joinPointRun);
      return new SubValue(ret.getValue());
   }
   
   public void after(@Arg SuperValue sup, @Arg SuperValue sup2)
   {
      after = "after7";
      Assert.assertTrue(POJO.joinPointRun);
   }
   
   public void throwing(@Thrown Throwable t, @Arg int i)
   {
      throwing = "throwing1";
      exception = t;
   }
   
   public void throwing(@JoinPoint MethodInfo mjp, @Thrown Throwable t)
   {
      throwing = "throwing2";
      exception = t;
   }
   
   public void throwing(@JoinPoint MethodInfo mjp, @Thrown Throwable t, @Arg int i)
   {
      throwing = "throwing3";
      exception = t;
   }
}