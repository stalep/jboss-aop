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

import org.jboss.aop.FieldInfo;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.JoinPoint;

/**
 * Aspect used on overloaded before advice tests.
 *
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class OverloadedBeforeAspect
{
   static String before1 = null;
   static String before2 = null;
   static String before3 = null;
   static String before4 = null;
   static String before5 = null;
   static String before6 = null;
   static String before7 = null;
   static String before8 = null;
   static String before9 = null;
   static String before10 = null;
   static String before11 = null;
   static String before12 = null;
   
   public static void clear()
   {
      before1 = null;
      before2 = null;
      before3 = null;
      before4 = null;
      before5 = null;
      before6 = null;
      before7 = null;
      before8 = null;
      before9 = null;
      before10 = null;
      before11 = null;
      before12 = null;
   }
   
   /* BEFORE1 ADVICE */
   
   public void before1(@JoinPoint FieldInfo joinPointInfo, @Arg String text)
   {
      before1 = "FieldInfo,String";
   }

   public void before1(@JoinPoint FieldInfo joinPointInfo, @Arg Object text)
   {
      before1 = "FieldInfo,Object";
   }
   
   public void before1(@JoinPoint JoinPointInfo joinPointInfo, @Arg String text)
   {
      before1 = "JoinPointInfo,String";
   }

   public void before1(@JoinPoint JoinPointInfo joinPointInfo, @Arg Object text)
   {
      before1 = "JoinPointInfo,Object";
   }

   public void before1(@JoinPoint Object joinPointInfo, @Arg String text)
   {
      before1 = "Object,String";
   }

   public void before1(@JoinPoint Object joinPointInfo, @Arg Object text)
   {
      before1 = "Object,Object";
   }

   public void before1(@JoinPoint FieldInfo joinPointInfo)
   {
      before1 = "FieldInfo";
   }
   
   public void before1(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before1 = "JoinPointInfo";
   }

   public void before1(@JoinPoint Object joinPointInfo)
   {
      before1 = "Object";
   }
   
   public void before1(@Arg String text)
   {
      before1 = "String";
   }
      
   public void before1()
   {
      before1 = "";
   }
   
/*   public void before1(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before1(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
  */ 
   /* BEFORE2 ADVICE */
   
   public void before2(@JoinPoint FieldInfo joinPointInfo, @Arg Object text)
   {
      before2 = "FieldInfo,Object";
   }
   
   public void before2(@JoinPoint JoinPointInfo joinPointInfo, @Arg String text)
   {
      before2 = "JoinPointInfo,String";
   }

   public void before2(@JoinPoint JoinPointInfo joinPointInfo, @Arg Object text)
   {
      before2 = "JoinPointInfo,Object";
   }

   public void before2(@JoinPoint Object joinPointInfo, @Arg String text)
   {
      before2 = "Object,String";
   }

   public void before2(@JoinPoint Object joinPointInfo, @Arg Object text)
   {
      before2 = "Object,Object";
   }

   public void before2(@JoinPoint FieldInfo joinPointInfo)
   {
      before2 = "FieldInfo";
   }
   
   public void before2(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before2 = "JoinPointInfo";
   }

   public void before2(@JoinPoint Object joinPointInfo)
   {
      before2 = "Object";
   }
   
   public void before2(@Arg String text)
   {
      before2 = "String";
   }
      
   public void before2()
   {
      before2 = "";
   }
   
   public void before2(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before2(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE3 ADVICE */
   
   public void before3(@JoinPoint JoinPointInfo joinPointInfo, @Arg String text)
   {
      before3 = "JoinPointInfo,String";
   }

   public void before3(@JoinPoint JoinPointInfo joinPointInfo, @Arg Object text)
   {
      before3 = "JoinPointInfo,Object";
   }

   public void before3(@JoinPoint Object joinPointInfo, @Arg String text)
   {
      before3 = "Object,String";
   }

   public void before3(@JoinPoint Object joinPointInfo, @Arg Object text)
   {
      before3 = "Object,Object";
   }

   public void before3(@JoinPoint FieldInfo joinPointInfo)
   {
      before3 = "FieldInfo";
   }
   
   public void before3(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before3 = "JoinPointInfo";
   }

   public void before3(@JoinPoint Object joinPointInfo)
   {
      before3 = "Object";
   }
   
   public void before3(@Arg String text)
   {
      before3 = "String";
   }
      
   public void before3()
   {
      before3 = "";
   }
   
   public void before3(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before3(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE4 ADVICE */
   
   public void before4(@JoinPoint JoinPointInfo joinPointInfo, @Arg Object text)
   {
      before4 = "JoinPointInfo,Object";
   }

   public void before4(@JoinPoint Object joinPointInfo, @Arg String text)
   {
      before4 = "Object,String";
   }

   public void before4(@JoinPoint Object joinPointInfo, @Arg Object text)
   {
      before4 = "Object,Object";
   }

   public void before4(@JoinPoint FieldInfo joinPointInfo)
   {
      before4 = "FieldInfo";
   }
   
   public void before4(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before4 = "JoinPointInfo";
   }

   public void before4(@JoinPoint Object joinPointInfo)
   {
      before4 = "Object";
   }
   
   public void before4(@Arg String text)
   {
      before4 = "String";
   }
      
   public void before4()
   {
      before4 = "";
   }
   
   public void before4(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before4(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE5 ADVICE */
   
   public void before5(@JoinPoint Object joinPointInfo, @Arg String text)
   {
      before5 = "Object,String";
   }

   public void before5(@JoinPoint Object joinPointInfo, @Arg Object text)
   {
      before5 = "Object,Object";
   }

   public void before5(@JoinPoint FieldInfo joinPointInfo)
   {
      before5 = "FieldInfo";
   }
   
   public void before5(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before5 = "JoinPointInfo";
   }

   public void before5(@JoinPoint Object joinPointInfo)
   {
      before5 = "Object";
   }
   
   public void before5(@Arg String text)
   {
      before5 = "String";
   }
      
   public void before5()
   {
      before5 = "";
   }
   
   public void before5(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before5(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE6 ADVICE */
   
   public void before6(@JoinPoint Object joinPointInfo, @Arg Object text)
   {
      before6 = "Object,Object";
   }

   public void before6(@JoinPoint FieldInfo joinPointInfo)
   {
      before6 = "FieldInfo";
   }
   
   public void before6(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before6 = "JoinPointInfo";
   }

   public void before6(@JoinPoint Object joinPointInfo)
   {
      before6 = "Object";
   }
   
   public void before6(@Arg String text)
   {
      before6 = "String";
   }
      
   public void before6()
   {
      before6 = "";
   }
   
   public void before6(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before6(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE7 ADVICE */
   
   public void before7(@JoinPoint FieldInfo joinPointInfo)
   {
      before7 = "FieldInfo";
   }
   
   public void before7(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before7 = "JoinPointInfo";
   }

   public void before7(@JoinPoint Object joinPointInfo)
   {
      before7 = "Object";
   }
   
   public void before7(@Arg String text)
   {
      before7 = "String";
   }
      
   public void before7()
   {
      before7 = "";
   }
   
   public void before7(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before7(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE8 ADVICE */
   
   public void before8(@JoinPoint JoinPointInfo joinPointInfo)
   {
      before8 = "JoinPointInfo";
   }

   public void before8(@JoinPoint Object joinPointInfo)
   {
      before8 = "Object";
   }
   
   public void before8(@Arg String text)
   {
      before8 = "String";
   }
      
   public void before8()
   {
      before8 = "";
   }
   
   public void before8(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before8(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE9 ADVICE */
   
   public void before9(@JoinPoint Object joinPointInfo)
   {
      before9 = "Object";
   }
   
   public void before9(@Arg String text)
   {
      before9 = "String";
   }
      
   public void before9()
   {
      before9 = "";
   }
   
   public void before9(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before9(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE10 ADVICE */
   
   public void before10(@Arg String text)
   {
      before10 = "String";
   }

   public void before10(@Arg Object text)
   {
      before10 = "Object";
   }
   
   public void before10()
   {
      before10 = "";
   }
   
   public void before10(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before10(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE11 ADVICE */
   
   public void before11(@Arg Object text)
   {
      before11 = "Object";
   }
   
   public void before11()
   {
      before11 = "";
   }
   
   public void before11(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before11(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
   /* BEFORE12 ADVICE */
   
   public void before12()
   {
      before12 = "";
   }
   
   public void before12(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before12(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
}