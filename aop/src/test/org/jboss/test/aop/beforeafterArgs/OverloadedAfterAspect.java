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

import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.Args;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Return;

/**
 * Aspect used on overloaded around advice tests (for JoinPoint, Return, Arg and
 * Args tests).
 * 
 * This class includes hierarchy on multiple Arg parameters.
 *
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class OverloadedAfterAspect
{
   static String after1 = null;
   static String after2 = null;
   static String after3 = null;
   static String after4 = null;
   static String after5 = null;
   static String after6 = null;
   static String after7 = null;
   static String after8 = null;
   static String after9 = null;
   static String after10 = null;
   static String after11 = null;
   static String after12 = null;
   static String after13 = null;
   static String after14 = null;
   static String after15 = null;
   static String after16 = null;
   static String after17 = null;
   static String after18 = null;
   static String after19 = null;
   static String after20 = null;
   static String after21 = null;
   static String after22 = null;
   static String after23 = null;
   static String after24 = null;
   static String after25 = null;
   static String after26 = null;
   static String after27 = null;
   static String after28 = null;
   static String after29 = null;
   static String after30 = null;
   static String after31 = null;
   static String after32 = null;
   static String after33 = null;
   static String after34 = null;
   static String after35 = null;
   static String after36 = null;
   static String after37 = null;
   static String after38 = null;
   static String after39 = null;
   static String after40 = null;
   static String after41 = null;
   static String after42 = null;
   static String after43 = null;
   static String after44 = null;
   static String after45 = null;
   static String after46 = null;
   static String after47 = null;
   static String after48 = null;
   static String after49 = null;
   static String after50 = null;
   static String after51 = null;
   static String after52 = null;
   static String after53 = null;
   static String after54 = null;
   static String after55 = null;
   static String after56 = null;
   static String after57 = null;
   static String after58 = null;
   static String after59 = null;
   static String after60 = null;
   static String after61 = null;
   static String after62 = null;
   static String after63 = null;
   static String after64 = null;
   static String after65 = null;
   static String after66 = null;

   public static void clear()
   {
      after1 = null;
      after2 = null;
      after3 = null;
      after4 = null;
      after5 = null;
      after6 = null;
      after7 = null;
      after8 = null;
      after9 = null;
      after10 = null;
      after11 = null;
      after12 = null;
      after13 = null;
      after14 = null;
      after15 = null;
      after16 = null;
      after17 = null;
      after18 = null;
      after19 = null;
      after20 = null;
      after21 = null;
      after22 = null;
      after23 = null;
      after24 = null;
      after25 = null;
      after26 = null;
      after27 = null;
      after28 = null;
      after29 = null;
      after30 = null;
      after31 = null;
      after32 = null;
      after33 = null;
      after34 = null;
      after35 = null;
      after36 = null;
      after37 = null;
      after38 = null;
      after39 = null;
      after40 = null;
      after41 = null;
      after42 = null;
      after43 = null;
      after44 = null;
      after45 = null;
      after46 = null;
      after47 = null;
      after48 = null;
      after49 = null;
      after50 = null;
      after51 = null;
      after52 = null;
      after53 = null;
      after54 = null;
      after55 = null;
      after56 = null;
      after57 = null;
      after58 = null;
      after59 = null;
      after60 = null;
      after61 = null;
      after62 = null;
      after63 = null;
      after64 = null;
      after65 = null;
      after66 = null;
   }
   
   /* AFTER2 ADVICE */
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after1 = "Object,MethodInfo,SuperClass,float,SubValue";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SuperValue arg2)
   {
      after1 = "Object,MethodInfo,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg Object arg2)
   {
      after1 = "Object,MethodInfo,SuperClass,float,Object";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SubValue arg2)
   {
      after1 = "Object,MethodInfo,Object,float,SubValue";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SuperValue arg2)
   {
      after1 = "Object,MethodInfo,Object,float,SuperValue";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg Object arg2)
   {
      after1 = "Object,MethodInfo,Object,float,Object";
      return null;
   }
   
   public Object after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after1 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after1 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after1 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after1 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after1 = "SuperClass,MethodInfo,SuperClass,Object";
      return null;
   }

   public SuperClass after1(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after1 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after1 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after1(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after1 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after1(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after1 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after1 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after1 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after1 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after1 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after1(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after1 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after1(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after1 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after1(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after1 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after1(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after1 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after1 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after1 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after1 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after1 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after1 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after1 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after1 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned)
   {
      after1 = "Object,MethodInfo,Object";
      return null;
   }

   public Object after1(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after1 = "Object,JoinPointInfo,SuperClass";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after1 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after1 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after1 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after1(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after1 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after1(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after1 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after1 = "Object,MethodInfo,float";
      return null;
   }

   public Object after1(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after1 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SuperClass after1(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after1 = "SuperClass,JoinPointInfo,SuperValue,SubValue";
      return null;
   }
   
   public SubClass after1(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after1 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after1(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after1 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after1(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after1 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after1(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after1 = "Object,Object,float";
      return null;
   }

   public SubClass after1(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after1 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after1(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after1 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after1(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after1 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after1(@JoinPoint MethodInfo joinPointInfo)
   {
      after1 = "Object,MethodInfo";
      return null;
   }
        
   public Object after1(@JoinPoint JoinPointInfo joinPointInfo)
   {
      after1 = "Object,JoinPointInfo";
      return null;
   }
   
   public Object after1(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after1 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after1(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after1 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after1(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after1 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after1(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after1 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after1(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after1 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after1(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after1 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after1(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after1 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after1(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after1 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after1(@Return Object valueReturned, @Args Object[] args)
   {
      after1 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after1(@Return SuperClass valueReturned)
   {
      after1 = "Object,SuperClass";
      return null;
   }

   public Object after1(@Return Object valueReturned)
   {
      after1 = "Object,Object";
      return null;
   }
   
   public void after1(@Arg float arg1, @Arg SubValue arg2)
   {
      after1 = "void,float,SubValue";
   }

   public Object after1(@Arg float arg1)
   {
      after1 = "Object,float";
      return null;
   }
   
   public void after1(@Arg SubValue arg2)
   {
      after1 = "void,SubValue";
   }
   
   public void after1(@Arg SuperValue arg2)
   {
      after1 = "void,SuperValue";
   }
   
   public void after1(@Args Object[] args)
   {
      after1 = "void,Object[]";
   }
   
   public void after1()
   {
      after1 = "void";
   }
   
   public void after1(@Arg int arg1)
   {
      after1 = "void,int";
   }
   
   /* AFTER2 ADVICE */
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SuperValue arg2)
   {
      after2 = "Object,MethodInfo,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg Object arg2)
   {
      after2 = "Object,MethodInfo,SuperClass,float,Object";
      return null;
   }

   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SubValue arg2)
   {
      after2 = "Object,MethodInfo,Object,float,SubValue";
      return null;
   }

   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SuperValue arg2)
   {
      after2 = "Object,MethodInfo,Object,float,SuperValue";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg Object arg2)
   {
      after2 = "Object,MethodInfo,Object,float,Object";
      return null;
   }
   
   public Object after2(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after2 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after2 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after2 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after2 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after2 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after2 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after2 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after2 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after2 = "Object,MethodInfo,float";
      return null;
   }

   public Object after2(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after2 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after2(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after2 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after2(@JoinPoint MethodInfo joinPointInfo)
   {
      after2 = "Object,MethodInfo";
      return null;
   }
        
   public Object after2(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after2 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after2(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after2 = "Object,MethodInfo,SuperClass,float,SuperValue";
      return null;
   }
   
   public SuperClass after2(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after2 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after2(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after2 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after2(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after2 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after2(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after2 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after2(@Return SuperClass valueReturned)
   {
      after2 = "Object,SuperClass";
      return null;
   }
   
   public void after2(@Arg float arg1, @Arg SubValue arg2)
   {
      after2 = "void,float,SubValue";
   }

   public Object after2(@Arg float arg1)
   {
      after2 = "Object,float";
      return null;
   }
   
   public void after2(@Arg SubValue arg2)
   {
      after2 = "void,SubValue";
   }
   
   public void after2(@Arg SuperValue arg2)
   {
      after2 = "void,SuperValue";
   }
   
   public void after2(@Args Object[] args)
   {
      after2 = "void,Object[]";
   }
   
   public void after2()
   {
      after2 = "void";
   }
   
   public void after2(@Arg int arg1)
   {
      after2 = "void,int";
   }
   
   /* AFTER3 ADVICE */
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg Object arg2)
   {
      after3 = "Object,MethodInfo,SuperClass,float,Object";
      return null;
   }

   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SubValue arg2)
   {
      after3 = "Object,MethodInfo,Object,float,SubValue";
      return null;
   }

   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SuperValue arg2)
   {
      after3 = "Object,MethodInfo,Object,float,SuperValue";
      return null;
   }
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg Object arg2)
   {
      after3 = "Object,MethodInfo,Object,float,Object";
      return null;
   }
   
   public Object after3(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after3 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after3(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after3 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after3 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after3(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after3 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after3 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after3 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after3 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after3 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public SuperClass after3(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after3 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after3 = "Object,MethodInfo,float";
      return null;
   }

   public Object after3(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after3 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after3(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after3 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after3(@JoinPoint MethodInfo joinPointInfo)
   {
      after3 = "Object,MethodInfo";
      return null;
   }
        
   public Object after3(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after3 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after3(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
    after3 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after3(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after3 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after3(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after3 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after3(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after3 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after3(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after3 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after3(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after3 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after3(@Return SuperClass valueReturned)
   {
      after3 = "Object,SuperClass";
      return null;
   }

   public void after3(@Arg float arg1, @Arg SubValue arg2)
   {
      after3 = "void,float,SubValue";
   }

   public Object after3(@Arg float arg1)
   {
      after3 = "Object,float";
      return null;
   }
   
   public void after3(@Arg SubValue arg2)
   {
      after3 = "void,SubValue";
   }
   
   public void after3(@Arg SuperValue arg2)
   {
      after3 = "void,SuperValue";
   }
   
   public void after3(@Args Object[] args)
   {
      after3 = "void,Object[]";
   }
   
   public void after3()
   {
      after3 = "void";
   }
   
   public void after3(@Arg int arg1)
   {
      after3 = "void,int";
   }
   
   /* AFTER4 ADVICE */
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SubValue arg2)
   {
      after4 = "Object,MethodInfo,Object,float,SubValue";
      return null;
   }

   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SuperValue arg2)
   {
      after4 = "Object,MethodInfo,Object,float,SuperValue";
      return null;
   }
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg Object arg2)
   {
      after4 = "Object,MethodInfo,Object,float,Object";
      return null;
   }
   
   public Object after4(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after4 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after4(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after4 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after4 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after4(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after4 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after4 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after4(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after4 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after4 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after4 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after4 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public SuperClass after4(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after4 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after4 = "Object,MethodInfo,float";
      return null;
   }

   public Object after4(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after4 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after4(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after4 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after4(@JoinPoint MethodInfo joinPointInfo)
   {
      after4 = "Object,MethodInfo";
      return null;
   }
        
   public Object after4(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after4 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after4(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after4 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public SuperClass after4(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after4 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after4(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after4 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after4(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after4 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after4(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after4 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after4(@Return SuperClass valueReturned)
   {
      after4 = "Object,SuperClass";
      return null;
   }

   public void after4(@Arg float arg1, @Arg SubValue arg2)
   {
      after4 = "void,float,SubValue";
   }

   public Object after4(@Arg float arg1)
   {
      after4 = "Object,float";
      return null;
   }
   
   public void after4(@Arg SubValue arg2)
   {
      after4 = "void,SubValue";
   }
   
   public void after4(@Arg SuperValue arg2)
   {
      after4 = "void,SuperValue";
   }

   public void after4(@Args Object[] args)
   {
      after4 = "void,Object[]";
   }
   
   public void after4()
   {
      after4 = "void";
   }
   
   public void after4(@Arg int arg1)
   {
      after4 = "void,int";
   }
   
   /* AFTER5 ADVICE */
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg SuperValue arg2)
   {
      after5 = "Object,MethodInfo,Object,float,SuperValue";
      return null;
   }
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg Object arg2)
   {
      after5 = "Object,MethodInfo,Object,float,Object";
      return null;
   }
   
   public Object after5(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after5 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after5(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after5 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after5 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after5(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after5 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after5(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after5 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after5 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after5 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after5(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after5 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after5(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after5 = "Object,MethodInfo,float,SuperClass";
      return null;
   }
   
   public SuperClass after5(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after5 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after5 = "Object,MethodInfo,float";
      return null;
   }

   public Object after5(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after5 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after5(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after5 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after5(@JoinPoint MethodInfo joinPointInfo)
   {
      after5 = "Object,MethodInfo";
      return null;
   }
        
   public Object after5(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after5 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after5(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after5 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after5(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after5 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after5(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after5 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after5(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after5 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after5(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after5 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after5(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after5 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after5(@Return SuperClass valueReturned)
   {
      after5 = "Object,SuperClass";
      return null;
   }

   public void after5(@Arg float arg1, @Arg SubValue arg2)
   {
      after5 = "void,float,SubValue";
   }

   public Object after5(@Arg float arg1)
   {
      after5 = "Object,float";
      return null;
   }
   
   public void after5(@Arg SubValue arg2)
   {
      after5 = "void,SubValue";
   }
   
   public void after5(@Arg SuperValue arg2)
   {
      after5 = "void,SuperValue";
   }
   
   public void after5(@Args Object[] args)
   {
      after5 = "void,Object[]";
   }
   
   public void after5()
   {
      after5 = "void";
   }
   
   public void after5(@Arg int arg1)
   {
      after5 = "void,int";
   }
   
   /* AFTER6 ADVICE */
   
   public Object after6(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1, @Arg Object arg2)
   {
      after6 = "Object,MethodInfo,Object,float,Object";
      return null;
   }
   
   public Object after6(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after6 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after6(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after6 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after6(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after6 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after6(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after6 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public Object after6(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after6 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after6(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after6 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after6(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after6 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after6(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after6 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public SuperClass after6(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after6 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after6(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after6 = "Object,MethodInfo,float";
      return null;
   }

   public Object after6(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after6 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after6(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after6 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after6(@JoinPoint MethodInfo joinPointInfo)
   {
      after6 = "Object,MethodInfo";
      return null;
   }
        
   public Object after6(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after6 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after6(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after6 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public SuperClass after6(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after6 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after6(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after6 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after6(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after6 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after6(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after6 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after6(@Return SuperClass valueReturned)
   {
      after6 = "Object,SuperClass";
      return null;
   }

   public void after6(@Arg float arg1, @Arg SubValue arg2)
   {
      after6 = "void,float,SubValue";
   }

   public Object after6(@Arg float arg1)
   {
      after6 = "Object,float";
      return null;
   }
   
   public void after6(@Arg SubValue arg2)
   {
      after6 = "void,SubValue";
   }
   
   public void after6(@Arg SuperValue arg2)
   {
      after6 = "void,SuperValue";
   }
   
   public void after6(@Args Object[] args)
   {
      after6 = "void,Object[]";
   }
   
   public void after6()
   {
      after6 = "void";
   }
   
   public void after6(@Arg int arg1)
   {
      after6 = "void,int";
   }
   
   /* AFTER7 ADVICE */
   
   public Object after7(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1, @Arg SubValue arg2)
   {
      after7 = "Object,JoinPointInfo,SuperClass,float,SubValue";
      return null;
   }

   public SuperClass after7(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after7 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after7 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after7(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after7 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after7(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after7 = "SuperClass,MethodInfo,SuperClass,Object";
      return null;
   }

   public SuperClass after7(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after7 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after7 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after7(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after7 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after7(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after7 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SubClass after7(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after7 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after7(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after7 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after7(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after7 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after7(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after7 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after7(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after7 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after7 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after7(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after7 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after7(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after7 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after7 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after7(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after7 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after7(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after7 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after7 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public SuperClass after7(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after7 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after7 = "Object,MethodInfo,float";
      return null;
   }

   public Object after7(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after7 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after7(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after7 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after7(@JoinPoint MethodInfo joinPointInfo)
   {
      after7 = "Object,MethodInfo";
      return null;
   }
        
   public Object after7(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after7 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after7(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after7 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after7(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after7 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after7(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after7 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after7(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after7 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after7(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after7 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after7(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after7 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after7(@Return SuperClass valueReturned)
   {
      after7 = "Object,SuperClass";
      return null;
   }

   public void after7(@Arg float arg1, @Arg SubValue arg2)
   {
      after7 = "void,float,SubValue";
   }

   public Object after7(@Arg float arg1)
   {
      after7 = "Object,float";
      return null;
   }
   
   public void after7(@Arg SubValue arg2)
   {
      after7 = "void,SubValue";
   }
   
   public void after7(@Arg SuperValue arg2)
   {
      after7 = "void,SuperValue";
   }
   
   public void after7(@Args Object[] args)
   {
      after7 = "void,Object[]";
   }
   
   public void after7()
   {
      after7 = "void";
   }
   
   public void after7(@Arg int arg1)
   {
      after7 = "void,int";
   }
   
   /* AFTER8 ADVICE */
   
   public SuperClass after8(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after8 = "SuperClass,MethodInfo,SuperClass,SubValue";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after8 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after8(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after8 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after8(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after8 = "SuperClass,MethodInfo,SuperClass,Object";
      return null;
   }

   public SuperClass after8(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after8 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after8 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after8(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after8 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after8(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after8 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after8(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after8 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after8(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after8 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after8(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after8 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after8(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after8 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after8(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after8 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after8(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after8 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after8(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after8 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after8(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after8 = "Object,Object,SuperClass,Object";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after8 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after8 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after8(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after8 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after8(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after8 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after8(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after8 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after8(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after8 = "Object,Object,Object,Object[]";
      return null;
   }

   public Object after8(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after8 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after8(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after8 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after8(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after8 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after8 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after8(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after8 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after8(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after8 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after8 = "Object,MethodInfo,float";
      return null;
   }

   public Object after8(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after8 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after8(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after8 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after8(@JoinPoint MethodInfo joinPointInfo)
   {
      after8 = "Object,MethodInfo";
      return null;
   }
        
   public Object after8(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after8 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after8(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after8 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after8(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after8 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after8(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after8 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after8(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after8 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after8(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after8 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after8(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after8 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after8(@Return SuperClass valueReturned)
   {
      after8 = "Object,SuperClass";
      return null;
   }
   
   public void after8(@Arg float arg1, @Arg SubValue arg2)
   {
      after8 = "void,float,SubValue";
   }

   public Object after8(@Arg float arg1)
   {
      after8 = "Object,float";
      return null;
   }
   
   public void after8(@Arg SubValue arg2)
   {
      after8 = "void,SubValue";
   }
   
   public void after8(@Arg SuperValue arg2)
   {
      after8 = "void,SuperValue";
   }
   
   public void after8(@Args Object[] args)
   {
      after8 = "void,Object[]";
   }
   
   public void after8()
   {
      after8 = "void";
   }
   
   public void after8(@Arg int arg1)
   {
      after8 = "void,int";
   }
   
   /* AFTER9 ADVICE */
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after9 = "Object,MethodInfo,SuperClass,float";
      return null;
   }

   public Object after9(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after9 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after9(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after9 = "SuperClass,MethodInfo,SuperClass,Object";
      return null;
   }

   public SuperClass after9(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after9 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after9 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after9(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after9 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after9(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after9 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after9 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after9 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after9 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after9 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after9(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after9 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after9(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after9 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after9(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after9 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after9(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after9 = "Object,Object,SuperClass,Object";
      return null;
   }
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after9 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after9 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after9 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after9 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after9(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after9 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after9(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after9 = "Object,Object,Object,Object[]";
      return null;
   }

   public Object after9(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after9 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after9(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after9 = "Object,JoinPointInfo,SuperClass";
      return null;
   }

   public Object after9(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after9 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after9(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after9 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after9 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after9(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after9 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after9(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after9 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after9 = "Object,MethodInfo,float";
      return null;
   }

   public Object after9(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after9 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after9(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after1 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after9(@JoinPoint MethodInfo joinPointInfo)
   {
      after9 = "Object,MethodInfo";
      return null;
   }
        
   public Object after9(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after9 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after9(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after9 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after9(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after9 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after9(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after9 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after9(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after9 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after9(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after9 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after9(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after9 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after9(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after9 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after9(@Return SuperClass valueReturned)
   {
      after9 = "Object,SuperClass";
      return null;
   }

   public void after9(@Arg float arg1, @Arg SubValue arg2)
   {
      after9 = "void,float,SubValue";
   }

   public Object after9(@Arg float arg1)
   {
      after9 = "Object,float";
      return null;
   }
   
   public void after9(@Arg SubValue arg2)
   {
      after9 = "void,SubValue";
   }
   
   public void after9(@Arg SuperValue arg2)
   {
      after9 = "void,SuperValue";
   }
   
   public void after9(@Args Object[] args)
   {
      after9 = "void,Object[]";
   }
   
   public void after9()
   {
      after9 = "void";
   }
   
   public void after9(@Arg int arg1)
   {
      after9 = "void,int";
   }
   
   /* AFTER10 ADVICE */
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after10 = "Object,MethodInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after10(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after10 = "SuperClass,MethodInfo,SuperClass,Object";
      return null;
   }

   public SuperClass after10(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after10 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after10 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after10(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after10 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after10(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after10 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after10(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after10 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after10(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after10 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after10(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after10 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after10(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after10 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after10(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after10 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after10(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after10 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after10(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after10 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after10(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after10 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after10(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after10 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after10 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after10 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after10 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after10 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after10 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after10 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after10(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after10 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after10(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after10 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after10 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after10(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after10 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after10(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after10 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after10 = "Object,MethodInfo,float";
      return null;
   }

   public Object after10(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after10 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after10(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after10 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after10(@JoinPoint MethodInfo joinPointInfo)
   {
      after10 = "Object,MethodInfo";
      return null;
   }
        
   public Object after10(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after10 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after10(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after10 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after10(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after10 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after10(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after10 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after10(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after10 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after10(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after10 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after10(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after10 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after10(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after10 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after10(@Return SuperClass valueReturned)
   {
      after10 = "Object,SuperClass";
      return null;
   }

   public void after10(@Arg float arg1, @Arg SubValue arg2)
   {
      after10 = "void,float,SubValue";
   }

   public Object after10(@Arg float arg1)
   {
      after10 = "Object,float";
      return null;
   }
   
   public void after10(@Arg SubValue arg2)
   {
      after10 = "void,SubValue";
   }
   
   public void after10(@Arg SuperValue arg2)
   {
      after10 = "void,SuperValue";
   }
   
   public void after10(@Args Object[] args)
   {
      after10 = "void,Object[]";
   }
   
   public void after10()
   {
      after10 = "void";
   }
   
   public void after10(@Arg int arg1)
   {
      after10 = "void,int";
   }
   
   /* AFTER11 ADVICE */
   
   public SuperClass after11(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after11 = "SuperClass,MethodInfo,SuperClass,Object";
      return null;
   }

   public SuperClass after11(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after11 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after11(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after11 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after11(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after11 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after11(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after11 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after11(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after11 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after11(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after11 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after11(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after11 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after11(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after11 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after11(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after11 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after11(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after11 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after11(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after11 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after11(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after11 = "Object,Object,SuperClass,Object";
      return null;
   }
   
   public Object after11(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after11 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after11(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after11 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after11(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after11 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after11(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after11 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after11(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after11 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after11(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after11 = "Object,Object,Object,Object[]";
      return null;
   }

   public Object after11(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after11 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after11(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after11 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after11(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after11 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after11(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after11 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after11(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after11 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after11(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after11 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after11(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after11 = "Object,MethodInfo,float";
      return null;
   }

   public Object after11(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after11 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after11(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after11 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after11(@JoinPoint MethodInfo joinPointInfo)
   {
      after11 = "Object,MethodInfo";
      return null;
   }
        
   public Object after11(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after11 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after11(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after11 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after11(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after11 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after11(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after11 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after11(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after11 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after11(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after11 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after11(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after11 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after11(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after11 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after11(@Return SuperClass valueReturned)
   {
      after11 = "Object,SuperClass";
      return null;
   }

   public void after11(@Arg float arg1, @Arg SubValue arg2)
   {
      after11 = "void,float,SubValue";
   }

   public Object after11(@Arg float arg1)
   {
      after11 = "Object,float";
      return null;
   }
   
   public void after11(@Arg SubValue arg2)
   {
      after11 = "void,SubValue";
   }
   
   public void after11(@Arg SuperValue arg2)
   {
      after11 = "void,SuperValue";
   }
   
   public void after11(@Args Object[] args)
   {
      after11 = "void,Object[]";
   }
   
   public void after11()
   {
      after11 = "void";
   }
   
   public void after11(@Arg int arg1)
   {
      after11 = "void,int";
   }
   
   /* AFTER12 ADVICE */
 
   public SuperClass after12(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SubValue arg2)
   {
      after12 = "SuperClass,MethodInfo,Object,SubValue";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after12 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after12(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after12 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after12(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after12 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after12(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after12 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after12(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after12 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after12(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after12 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after12(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after12 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }
   
   public SubClass after12(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after12 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after12(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after12 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after12(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after12 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after12(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after12 = "Object,Object,SuperClass,Object";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after12 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after12 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after12 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after12 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after12 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after12 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after12 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after12(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after12 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after12(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after12 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after12 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after12(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after12 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after12(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after12 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after12 = "Object,MethodInfo,float";
      return null;
   }

   public Object after12(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after12 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after12(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after12 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after12(@JoinPoint MethodInfo joinPointInfo)
   {
      after12 = "Object,MethodInfo";
      return null;
   }
        
   public Object after12(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after12 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after12(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after12 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after12(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after12 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after12(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after12 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after12(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after12 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after12(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after12 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after12(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after12 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after12(@Return SuperClass valueReturned)
   {
      after12 = "Object,SuperClass";
      return null;
   }

   public void after12(@Arg float arg1, @Arg SubValue arg2)
   {
      after12 = "void,float,SubValue";
   }

   public Object after12(@Arg float arg1)
   {
      after12 = "Object,float";
      return null;
   }
   
   public void after12(@Arg SubValue arg2)
   {
      after12 = "void,SubValue";
   }
   
   public void after12(@Arg SuperValue arg2)
   {
      after12 = "void,SuperValue";
   }
   
   public void after12(@Args Object[] args)
   {
      after12 = "void,Object[]";
   }
   
   public void after12()
   {
      after12 = "void";
   }
   
   public void after12(@Arg int arg1)
   {
      after12 = "void,int";
   }
   
   /* AFTER13 ADVICE */
   
   public Object after13(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned,
         @Arg float arg1)
   {
      after13 = "Object,MethodInfo,Object,float";
      return null;
   }

   public SubClass after13(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after13 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after13(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after13 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after13(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after13 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after13(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after13 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after13(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after13 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after13(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after13 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after13(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after13 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after13(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after13 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after13(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after13 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after13(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after13 = "Object,Object,SuperClass,Object";
      return null;
   }
   
   public Object after13(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after13 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after13(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after13 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after13(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after13 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after13(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after13 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after13(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after13 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after13(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after13 = "Object,Object,Object,Object[]";
      return null;
   }

   public Object after13(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after13 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after13(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after13 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after13(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after13 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after13(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after13 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after13(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after13 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after13(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after13 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after13(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after13 = "Object,MethodInfo,float";
      return null;
   }

   public Object after13(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after13 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after13(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after13 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after13(@JoinPoint MethodInfo joinPointInfo)
   {
      after13 = "Object,MethodInfo";
      return null;
   }
        
   public Object after13(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after13 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after13(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after13 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after13(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after13 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after13(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after13 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after13(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after13 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after13(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after13 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after13(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after13 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after13(@Return SuperClass valueReturned)
   {
      after13 = "Object,SuperClass";
      return null;
   }

   public void after13(@Arg float arg1, @Arg SubValue arg2)
   {
      after13 = "void,float,SubValue";
   }

   public Object after13(@Arg float arg1)
   {
      after13 = "Object,float";
      return null;
   }
   
   public void after13(@Arg SubValue arg2)
   {
      after13 = "void,SubValue";
   }
   
   public void after13(@Arg SuperValue arg2)
   {
      after13 = "void,SuperValue";
   }
   
   public void after13(@Args Object[] args)
   {
      after13 = "void,Object[]";
   }
   
   public void after13()
   {
      after13 = "void";
   }
   
   public void after13(@Arg int arg1)
   {
      after13 = "void,int";
   }
   
   /* AFTER14 ADVICE */
   
   public SubClass after14(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg SuperValue arg2)
   {
      after14 = "SubClass,MethodInfo,Object,SuperValue";
      return null;
   }
   
   public SubClass after14(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after14 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after14(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after14 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after14(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after14 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after14(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after14 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after14(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after14 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after14(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after14 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after14(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after14 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after14(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after14 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after14(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after14 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after14(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after14 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after14 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after14 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after14 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after14 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after14 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after14 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after14(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after14 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after14(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after14 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after14(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after14 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after14(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after14 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after14(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after14 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after14(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after14 = "Object,MethodInfo,float";
      return null;
   }

   public Object after14(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after14 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after14(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after14 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after14(@JoinPoint MethodInfo joinPointInfo)
   {
      after14 = "Object,MethodInfo";
      return null;
   }
        
   public Object after14(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after14 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after14(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after14 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after14(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after14 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after14(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after14 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after14(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after14 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after14(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after14 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after14(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after14 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after14(@Return SuperClass valueReturned)
   {
      after14 = "Object,SuperClass";
      return null;
   }

   public void after14(@Arg float arg1, @Arg SubValue arg2)
   {
      after14 = "void,float,SubValue";
   }

   public Object after14(@Arg float arg1)
   {
      after14 = "Object,float";
      return null;
   }
   
   public void after14(@Arg SubValue arg2)
   {
      after14 = "void,SubValue";
   }
   
   public void after14(@Arg SuperValue arg2)
   {
      after14 = "void,SuperValue";
   }
   
   public void after14(@Args Object[] args)
   {
      after14 = "void,Object[]";
   }
   
   public void after14()
   {
      after14 = "void";
   }
   
   public void after14(@Arg int arg1)
   {
      after14 = "void,int";
   }
   
   /* AFTER15 ADVICE */
   
   public SubClass after15(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Arg Object arg2)
   {
      after15 = "SubClass,MethodInfo,Object,Object";
      return null;
   }
   
   public SuperClass after15(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after15 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after15(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after15 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after15(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after15 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after15(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after15 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after15(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after15 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after15(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after15 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after15(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after15 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after15(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after15 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after15(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after15 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after15 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after15 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after15 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after15 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after15 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after15 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after15(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after15 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after15(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after15 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after15(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after15 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after15(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after15 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after15(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after15 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after15(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after15 = "Object,MethodInfo,float";
      return null;
   }

   public Object after15(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after15 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after15(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after15 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after15(@JoinPoint MethodInfo joinPointInfo)
   {
      after15 = "Object,MethodInfo";
      return null;
   }
        
   public Object after15(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after15 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after15(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after15 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after15(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after15 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after15(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after15 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after15(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after15 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after15(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after15 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after15(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after15 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after15(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after15 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after15(@Return Object valueReturned, @Args Object[] args)
   {
      after15 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after15(@Return SuperClass valueReturned)
   {
      after15 = "Object,SuperClass";
      return null;
   }

   public Object after15(@Return Object valueReturned)
   {
      after15 = "Object,Object";
      return null;
   }

   public void after15(@Arg float arg1, @Arg SubValue arg2)
   {
      after15 = "void,float,SubValue";
   }

   public Object after15(@Arg float arg1)
   {
      after15 = "Object,float";
      return null;
   }
   
   public void after15(@Arg SubValue arg2)
   {
      after15 = "void,SubValue";
   }
   
   public void after15(@Arg SuperValue arg2)
   {
      after15 = "void,SuperValue";
   }
   
   public void after15(@Args Object[] args)
   {
      after15 = "void,Object[]";
   }
   
   public void after15()
   {
      after15 = "void";
   }
   
   public void after15(@Arg int arg1)
   {
      after15 = "void,int";
   }
   
   /* AFTER16 ADVICE */

   public SuperClass after16(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after16 = "SuperClass,JoinPointInfo,SuperClass,SubValue";
      return null;
   }
   
   public SubClass after16(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after16 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after16(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after16 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after16(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after16 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after16(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after16 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after16(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after16 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after16(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after16 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after16(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after16 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after16(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after16 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after16 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after16 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after16 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after16 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after16 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after16 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after16(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned)
   {
      after16 = "Object,MethodInfo,Object";
      return null;
   }

   public Object after16(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after16 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after16(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after16 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after16(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after16 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after16(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after16 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after16(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after16 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after16(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after16 = "Object,MethodInfo,float";
      return null;
   }

   public Object after16(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after16 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after16(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after16 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after16(@JoinPoint MethodInfo joinPointInfo)
   {
      after16 = "Object,MethodInfo";
      return null;
   }
        
   public Object after16(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after16 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after16(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after16 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after16(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after16 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after16(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after16 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after16(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after16 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after16(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after16 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after16(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after16 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after16(@Return SuperClass valueReturned)
   {
      after16 = "Object,SuperClass";
      return null;
   }
   
   public void after16(@Arg float arg1, @Arg SubValue arg2)
   {
      after16 = "void,float,SubValue";
   }

   public Object after16(@Arg float arg1)
   {
      after16 = "Object,float";
      return null;
   }
   
   public void after16(@Arg SubValue arg2)
   {
      after16 = "void,SubValue";
   }
   
   public void after16(@Arg SuperValue arg2)
   {
      after16 = "void,SuperValue";
   }
   
   public void after16(@Args Object[] args)
   {
      after16 = "void,Object[]";
   }
   
   public void after16()
   {
      after16 = "void";
   }
   
   public void after16(@Arg int arg1)
   {
      after16 = "void,int";
   }
   
   /* AFTER17 ADVICE */

   public SubClass after17(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg float arg1)
   {
      after17 = "SubClass,JoinPointInfo,SuperClass,float";
      return null;
   }

   public SubClass after17(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after17 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after17(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after17 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after17(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after17 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after17(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after17 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after17(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after17 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after17(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after17 = "Object,Object,SuperClass,Object";
      return null;
   }
   
   public Object after17(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after17 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after17 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after17 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after17 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after17 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after17 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after17 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after17(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after17 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after17(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after17 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after17(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after17 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after17(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after17 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after17(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after17 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after17(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after17 = "Object,MethodInfo,float";
      return null;
   }

   public Object after17(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after17 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after17(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after17 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after17(@JoinPoint MethodInfo joinPointInfo)
   {
      after17 = "Object,MethodInfo";
      return null;
   }
        
   public Object after17(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after17 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after17(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after17 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after17(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after17 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after17(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after17 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after17(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after17 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after17(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after17 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after17(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after17 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after17(@Return SuperClass valueReturned)
   {
      after17 = "Object,SuperClass";
      return null;
   }
   
   public void after17(@Arg float arg1, @Arg SubValue arg2)
   {
      after17 = "void,float,SubValue";
   }

   public Object after17(@Arg float arg1)
   {
      after17 = "Object,float";
      return null;
   }
   
   public void after17(@Arg SubValue arg2)
   {
      after17 = "void,SubValue";
   }
   
   public void after17(@Arg SuperValue arg2)
   {
      after17 = "void,SuperValue";
   }
   
   public void after17(@Args Object[] args)
   {
      after17 = "void,Object[]";
   }
   
   public void after17()
   {
      after17 = "void";
   }
   
   public void after17(@Arg int arg1)
   {
      after17 = "void,int";
   }
   
   /* AFTER18 ADVICE */
   
   public SubClass after18(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after18 = "SubClass,JoinPointInfo,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after18(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after18 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after18(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after18 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after18(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after18 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after18(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after18 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after18(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after18 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after18(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after18 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after18 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after18 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after18 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after18 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after18 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after18 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after18(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after18 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after18(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after18 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after18(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after18 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after18(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after18 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after18(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after18 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after18(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after18 = "Object,MethodInfo,float";
      return null;
   }

   public Object after18(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after18 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after18(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after18 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after18(@JoinPoint MethodInfo joinPointInfo)
   {
      after18 = "Object,MethodInfo";
      return null;
   }
        
   public Object after18(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after18 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after18(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after18 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after18(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after18 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after18(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after18 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after18(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after18 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after18(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after18 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after18(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after18 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after18(@Return SuperClass valueReturned)
   {
      after18 = "Object,SuperClass";
      return null;
   }
   
   public void after18(@Arg float arg1, @Arg SubValue arg2)
   {
      after18 = "void,float,SubValue";
   }

   public Object after18(@Arg float arg1)
   {
      after18 = "Object,float";
      return null;
   }
   
   public void after18(@Arg SubValue arg2)
   {
      after18 = "void,SubValue";
   }
   
   public void after18(@Arg SuperValue arg2)
   {
      after18 = "void,SuperValue";
   }
   
   public void after18(@Args Object[] args)
   {
      after18 = "void,Object[]";
   }
   
   public void after18()
   {
      after18 = "void";
   }
   
   public void after18(@Arg int arg1)
   {
      after18 = "void,int";
   }
   
   /* AFTER19 ADVICE */
   
   public SuperClass after19(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after19 = "SuperClass,JoinPointInfo,SuperClass,Object";
      return null;
   }

   public SubClass after19(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after19 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after19(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after19 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after19(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after19 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after19(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after19 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after19(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after19 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after19 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after19 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after19 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after19 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after19 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after19 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after19(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after19 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after19(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after19 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after19(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after19 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after19(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after19 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after19(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after19 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after19(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after19 = "Object,MethodInfo,float";
      return null;
   }

   public Object after19(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after19 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after19(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after19 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after19(@JoinPoint MethodInfo joinPointInfo)
   {
      after19 = "Object,MethodInfo";
      return null;
   }
        
   public Object after19(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after19 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after19(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after19 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after19(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after19 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after19(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after19 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after19(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after19 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after19(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after19 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after19(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after19 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after19(@Return SuperClass valueReturned)
   {
      after19 = "Object,SuperClass";
      return null;
   }
   
   public void after19(@Arg float arg1, @Arg SubValue arg2)
   {
      after19 = "void,float,SubValue";
   }

   public Object after19(@Arg float arg1)
   {
      after19 = "Object,float";
      return null;
   }
   
   public void after19(@Arg SubValue arg2)
   {
      after19 = "void,SubValue";
   }
   
   public void after19(@Arg SuperValue arg2)
   {
      after19 = "void,SuperValue";
   }
   
   public void after19(@Args Object[] args)
   {
      after19 = "void,Object[]";
   }
   
   public void after19()
   {
      after19 = "void";
   }
   
   public void after19(@Arg int arg1)
   {
      after19 = "void,int";
   }
   
   /* AFTER20 ADVICE */

   public SubClass after20(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after20 = "SubClass,Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after20(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after20 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after20(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after20 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after20(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after20 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after20(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after20 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after20 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after20 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after20 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after20 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after20 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after20 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after20(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after20 = "Object,JoinPointInfo,SuperClass";
      return null;
   }

   public Object after20(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after20 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after20(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after20 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after20(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after20 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after20(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after20 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after20(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after20 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after20(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after20 = "Object,MethodInfo,float";
      return null;
   }

   public Object after20(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after20 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after20(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after20 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after20(@JoinPoint MethodInfo joinPointInfo)
   {
      after20 = "Object,MethodInfo";
      return null;
   }
        
   public Object after20(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after20 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after20(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after20 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after20(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after20 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after20(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after20 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after20(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after20 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after20(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after20 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after20(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after20 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after20(@Return SuperClass valueReturned)
   {
      after20 = "Object,SuperClass";
      return null;
   }
   
   public void after20(@Arg float arg1, @Arg SubValue arg2)
   {
      after20 = "void,float,SubValue";
   }

   public Object after20(@Arg float arg1)
   {
      after20 = "Object,float";
      return null;
   }
   
   public void after20(@Arg SubValue arg2)
   {
      after20 = "void,SubValue";
   }
   
   public void after20(@Arg SuperValue arg2)
   {
      after20 = "void,SuperValue";
   }
   
   public void after20(@Args Object[] args)
   {
      after20 = "void,Object[]";
   }
   
   public void after20()
   {
      after20 = "void";
   }
   
   public void after20(@Arg int arg1)
   {
      after20 = "void,int";
   }
   
   /* AFTER21 ADVICE */
   
   public Object after21(@JoinPoint Object joinPointInfo, @Return SuperClass valueReturned,
         @Arg float arg1)
   {
      after21 = "Object,Object,SuperClass,float";
      return null;
   }

   public SubClass after21(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after21 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after21(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after21 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after21(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after21 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after21 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after21 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after21 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after21 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after21 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after21 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after21(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after21 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after21(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after21 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after21(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after21 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after21(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after21 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after21(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after21 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after21(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after21 = "Object,MethodInfo,float";
      return null;
   }

   public Object after21(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after21 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after21(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after21 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after21(@JoinPoint MethodInfo joinPointInfo)
   {
      after21 = "Object,MethodInfo";
      return null;
   }
        
   public Object after21(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after21 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after21(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after21 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after21(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after21 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after21(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after21 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after21(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after21 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after21(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after21 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after22(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after22 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after21(@Return SuperClass valueReturned)
   {
      after21 = "Object,SuperClass";
      return null;
   }
   
   public void after21(@Arg float arg1, @Arg SubValue arg2)
   {
      after21 = "void,float,SubValue";
   }

   public Object after21(@Arg float arg1)
   {
      after21 = "Object,float";
      return null;
   }
   
   public void after21(@Arg SubValue arg2)
   {
      after21 = "void,SubValue";
   }
   
   public void after21(@Arg SuperValue arg2)
   {
      after21 = "void,SuperValue";
   }
   
   public void after21(@Args Object[] args)
   {
      after21 = "void,Object[]";
   }
   
   public void after21()
   {
      after21 = "void";
   }
   
   public void after21(@Arg int arg1)
   {
      after21 = "void,int";
   }
   
   /* AFTER22 ADVICE */
   
   public SubClass after22(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after22 = "SubClass,Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after22(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after22 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after22(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after22 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after22 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after22 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after22 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after22 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after22 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after22 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after22(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after22 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after22(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after22 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after22(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after22 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after22(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after22 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after22(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after22 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after22(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after22 = "Object,MethodInfo,float";
      return null;
   }

   public Object after22(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after22 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after22(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after22 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after22(@JoinPoint MethodInfo joinPointInfo)
   {
      after22 = "Object,MethodInfo";
      return null;
   }
        
   public Object after22(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after22 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after22(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after22 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after22(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after22 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after22(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after22 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after22(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after22 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after22(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after22 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after22(@Return SuperClass valueReturned)
   {
      after22 = "Object,SuperClass";
      return null;
   }
   
   public void after22(@Arg float arg1, @Arg SubValue arg2)
   {
      after22 = "void,float,SubValue";
   }

   public Object after22(@Arg float arg1)
   {
      after22 = "Object,float";
      return null;
   }
   
   public void after22(@Arg SubValue arg2)
   {
      after22 = "void,SubValue";
   }
   
   public void after22(@Arg SuperValue arg2)
   {
      after22 = "void,SuperValue";
   }
   
   public void after22(@Args Object[] args)
   {
      after22 = "void,Object[]";
   }
   
   public void after22()
   {
      after22 = "void";
   }
   
   public void after22(@Arg int arg1)
   {
      after22 = "void,int";
   }
   
   /* AFTER23 ADVICE */
   
   public Object after23(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Arg Object arg2)
   {
      after23 = "Object,Object,SuperClass,Object";
      return null;
   }

   public Object after23(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after23 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after23 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after23 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after23 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after23 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after23 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after23 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after23(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after23 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after23(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after23 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after23(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after23 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after23(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after23 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after23(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after23 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after23(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after23 = "Object,MethodInfo,float";
      return null;
   }

   public Object after23(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after23 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after23(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after23 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after23(@JoinPoint MethodInfo joinPointInfo)
   {
      after23 = "Object,MethodInfo";
      return null;
   }
        
   public Object after23(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after23 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after23(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after23 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after23(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after23 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after23(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after23 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after23(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after23 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after23(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after23 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after23(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after23 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after23(@Return SuperClass valueReturned)
   {
      after23 = "Object,SuperClass";
      return null;
   }
   
   public void after23(@Arg float arg1, @Arg SubValue arg2)
   {
      after23 = "void,float,SubValue";
   }

   public Object after23(@Arg float arg1)
   {
      after23 = "Object,float";
      return null;
   }
   
   public void after23(@Arg SubValue arg2)
   {
      after23 = "void,SubValue";
   }
   
   public void after23(@Arg SuperValue arg2)
   {
      after23 = "void,SuperValue";
   }
   
   public void after23(@Args Object[] args)
   {
      after23 = "void,Object[]";
   }
   
   public void after23()
   {
      after23 = "void";
   }
   
   public void after23(@Arg int arg1)
   {
      after23 = "void,int";
   }
   
   /* AFTER24 ADVICE */
   
   public Object after24(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after24 = "Object,MethodInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after24 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after24 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after24 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after24 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after24 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after24 = "Object,MethodInfo,SuperClass";
      return null;
   }

   public Object after24(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after24 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after24(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after24 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after24(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after24 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after24(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after24 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after24(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after24 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after24(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after24 = "Object,MethodInfo,float";
      return null;
   }

   public Object after24(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after24 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after24(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after24 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after24(@JoinPoint MethodInfo joinPointInfo)
   {
      after24 = "Object,MethodInfo";
      return null;
   }
        
   public Object after24(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after24 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after24(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after24 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after24(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after24 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after24(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after24 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after24(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after24 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after24(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after24 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after24(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after24 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after24(@Return SuperClass valueReturned)
   {
      after24 = "Object,SuperClass";
      return null;
   }
   
   public void after24(@Arg float arg1, @Arg SubValue arg2)
   {
      after24 = "void,float,SubValue";
   }

   public Object after24(@Arg float arg1)
   {
      after24 = "Object,float";
      return null;
   }
   
   public void after24(@Arg SubValue arg2)
   {
      after24 = "void,SubValue";
   }
   
   public void after24(@Arg SuperValue arg2)
   {
      after24 = "void,SuperValue";
   }
   
   public void after24(@Args Object[] args)
   {
      after24 = "void,Object[]";
   }
   
   public void after24()
   {
      after24 = "void";
   }
   
   public void after24(@Arg int arg1)
   {
      after24 = "void,int";
   }
   
   /* AFTER25 ADVICE */

   public Object after25(@JoinPoint MethodInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after25 = "Object,MethodInfo,Object,Object[]";
      return null;
   }
   
   public Object after25(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after25 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after25(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after25 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after25(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after25 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after25(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after25 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after25(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after25 = "Object,MethodInfo,SuperClass";
      return null;
   }
   
   public Object after25(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after25 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after25(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after25 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after25(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after25 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after25(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after25 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after25(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after25 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after25(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after25 = "Object,MethodInfo,float";
      return null;
   }

   public Object after25(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after25 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after25(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after25 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after25(@JoinPoint MethodInfo joinPointInfo)
   {
      after25 = "Object,MethodInfo";
      return null;
   }
        
   public Object after25(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after25 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after25(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after25 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after25(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after25 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after25(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after25 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after25(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after25 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after25(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after25 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after25(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after25 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after25(@Return SuperClass valueReturned)
   {
      after25 = "Object,SuperClass";
      return null;
   }
   
   public void after25(@Arg float arg1, @Arg SubValue arg2)
   {
      after25 = "void,float,SubValue";
   }

   public Object after25(@Arg float arg1)
   {
      after25 = "Object,float";
      return null;
   }
   
   public void after25(@Arg SubValue arg2)
   {
      after25 = "void,SubValue";
   }
   
   public void after25(@Arg SuperValue arg2)
   {
      after25 = "void,SuperValue";
   }
   
   public void after25(@Args Object[] args)
   {
      after25 = "void,Object[]";
   }
   
   public void after25()
   {
      after25 = "void";
   }
   
   public void after25(@Arg int arg1)
   {
      after25 = "void,int";
   }
   
   /* AFTER26 ADVICE */
   
   public Object after26(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after26 = "Object,JoinPointInfo,SuperClass,Object[]";
      return null;
   }
   
   public Object after26(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after26 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after26(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after26 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after26(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after26 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after26(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after26 = "Object,MethodInfo,SuperClass";
      return null;
   }
   
   public Object after26(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after26 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after26(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after26 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after26(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after26 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after26(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after26 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after26(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after26 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after26(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after26 = "Object,MethodInfo,float";
      return null;
   }

   public Object after26(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after26 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after26(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after26 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after26(@JoinPoint MethodInfo joinPointInfo)
   {
      after26 = "Object,MethodInfo";
      return null;
   }
        
   public Object after26(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after26 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after26(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after26 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after26(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after26 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after26(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after26 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after26(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after26 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after26(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after26 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after26(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after26 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after26(@Return SuperClass valueReturned)
   {
      after26 = "Object,SuperClass";
      return null;
   }
   
   public void after26(@Arg float arg1, @Arg SubValue arg2)
   {
      after26 = "void,float,SubValue";
   }

   public Object after26(@Arg float arg1)
   {
      after26 = "Object,float";
      return null;
   }
   
   public void after26(@Arg SubValue arg2)
   {
      after26 = "void,SubValue";
   }
   
   public void after26(@Arg SuperValue arg2)
   {
      after26 = "void,SuperValue";
   }
   
   public void after26(@Args Object[] args)
   {
      after26 = "void,Object[]";
   }
   
   public void after26()
   {
      after26 = "void";
   }
   
   public void after26(@Arg int arg1)
   {
      after26 = "void,int";
   }
   
   /* AFTER27 ADVICE */
   
   public Object after27(@JoinPoint JoinPointInfo joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after27 = "Object,JoinPointInfo,Object,Object[]";
      return null;
   }
   
   public Object after27(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after27 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after27(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after27 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after27(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after27 = "Object,MethodInfo,SuperClass";
      return null;
   }
   
   public Object after27(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after27 = "Object,MethodInfo,float,SubValue";
      return null;
   }

   public Object after27(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after27 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after27(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after27 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after27(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after27 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after27(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after27 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after27(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after27 = "Object,MethodInfo,float";
      return null;
   }

   public Object after27(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after27 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after27(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after27 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after27(@JoinPoint MethodInfo joinPointInfo)
   {
      after27 = "Object,MethodInfo";
      return null;
   }
        
   public Object after27(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after27 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after27(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after27 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after27(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after27 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after27(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after27 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after27(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after27 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after27(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after27 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after27(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after27 = "Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after27(@Return SuperClass valueReturned)
   {
      after27 = "Object,SuperClass";
      return null;
   }
   
   public void after27(@Arg float arg1, @Arg SubValue arg2)
   {
      after27 = "void,float,SubValue";
   }

   public Object after27(@Arg float arg1)
   {
      after27 = "Object,float";
      return null;
   }
   
   public void after27(@Arg SubValue arg2)
   {
      after27 = "void,SubValue";
   }
   
   public void after27(@Arg SuperValue arg2)
   {
      after27 = "void,SuperValue";
   }
   
   public void after27(@Args Object[] args)
   {
      after27 = "void,Object[]";
   }
   
   public void after27()
   {
      after27 = "void";
   }
   
   public void after27(@Arg int arg1)
   {
      after27 = "void,int";
   }
   
   /* AFTER28 ADVICE */
   
   public Object after28(@JoinPoint Object joinPointInfo,
         @Return SuperClass valueReturned, @Args Object[] args)
   {
      after28 = "Object,Object,SuperClass,Object[]";
      return null;
   }
   
   public Object after28(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after28 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after28(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after28 = "Object,MethodInfo,SuperClass";
      return null;
   }
   
   public Object after28(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after28 = "Object,MethodInfo,float,SubValue";
      return null;
   }
   
   public Object after28(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after28 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after28(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after28 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after28(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after28 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after28(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after28 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after28(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after28 = "Object,MethodInfo,float";
      return null;
   }

   public Object after28(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after28 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after28(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after28 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after28(@JoinPoint MethodInfo joinPointInfo)
   {
      after28 = "Object,MethodInfo";
      return null;
   }
        
   public Object after28(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after28 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after28(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after28 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after28(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after28 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after28(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after28 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after28(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after28 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after28(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after28 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after28(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after28 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after28(@Return SuperClass valueReturned)
   {
      after28 = "Object,SuperClass";
      return null;
   }

   public void after28(@Arg float arg1, @Arg SubValue arg2)
   {
      after28 = "void,float,SubValue";
   }

   public Object after28(@Arg float arg1)
   {
      after28 = "Object,float";
      return null;
   }
   
   public void after28(@Arg SubValue arg2)
   {
      after28 = "void,SubValue";
   }
   
   public void after28(@Arg SuperValue arg2)
   {
      after28 = "void,SuperValue";
   }
   
   public void after28(@Args Object[] args)
   {
      after28 = "void,Object[]";
   }
   
   public void after28()
   {
      after28 = "void";
   }
   
   public void after28(@Arg int arg1)
   {
      after28 = "void,int";
   }
   
   /* AFTER29 ADVICE */
   
   public Object after29(@JoinPoint Object joinPointInfo,
         @Return Object valueReturned, @Args Object[] args)
   {
      after29 = "Object,Object,Object,Object[]";
      return null;
   }
   
   public Object after29(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after29 = "Object,MethodInfo,SuperClass";
      return null;
   }
   
   public Object after29(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after29 = "Object,MethodInfo,float,SubValue";
      return null;
   }
   
   public Object after29(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after29 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after29(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after29 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after29(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after29 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after29(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after29 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after29(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after29 = "Object,MethodInfo,float";
      return null;
   }

   public Object after29(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after29 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SubClass after29(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after29 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public Object after29(@JoinPoint MethodInfo joinPointInfo)
   {
      after29 = "Object,MethodInfo";
      return null;
   }
        
   public Object after29(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after29 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after29(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after29 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after29(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after29 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after29(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after29 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after29(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after29 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after29(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after29 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after29(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after29 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after29(@Return SuperClass valueReturned)
   {
      after29 = "Object,SuperClass";
      return null;
   }
   
   public void after29(@Arg float arg1, @Arg SubValue arg2)
   {
      after29 = "void,float,SubValue";
   }

   public Object after29(@Arg float arg1)
   {
      after29 = "Object,float";
      return null;
   }
   
   public void after29(@Arg SubValue arg2)
   {
      after29 = "void,SubValue";
   }
   
   public void after29(@Arg SuperValue arg2)
   {
      after29 = "void,SuperValue";
   }

   public void after29(@Args Object[] args)
   {
      after29 = "void,Object[]";
   }
   
   public void after29()
   {
      after29 = "void";
   }
   
   public void after29(@Arg int arg1)
   {
      after29 = "void,int";
   }
   
   /* AFTER30 ADVICE */
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after30 = "Object,MethodInfo,SuperClass";
      return null;
   }
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned)
   {
      after30 = "Object,MethodInfo,Object";
      return null;
   }
   
   public Object after30(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after30 = "Object,JoinPointInfo,SuperClass";
      return null;
   }
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after30 = "Object,MethodInfo,float,SubValue";
      return null;
   }
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after30 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after30 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after30(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after30 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }

   public SuperClass after30(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after30 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after30 = "Object,MethodInfo,float";
      return null;
   }

   public Object after30(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after30 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SuperClass after30(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after30 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after30(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after30 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after30(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after30 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after30(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after30 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after30(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after30 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after30(@JoinPoint MethodInfo joinPointInfo)
   {
      after30 = "Object,MethodInfo";
      return null;
   }
   
   public Object after30(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after30 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after30(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after30 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after30(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after30 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after30(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after30 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after30(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after30 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after30(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after30 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after30(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after30 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after30(@Return SuperClass valueReturned)
   {
      after30 = "Object,SuperClass";
      return null;
   }
   
   public void after30(@Arg float arg1, @Arg SubValue arg2)
   {
      after30 = "void,float,SubValue";
   }

   public Object after30(@Arg float arg1)
   {
      after30 = "Object,float";
      return null;
   }
   
   public void after30(@Arg SubValue arg2)
   {
      after30 = "void,SubValue";
   }
   
   public void after30(@Arg SuperValue arg2)
   {
      after30 = "void,SuperValue";
   }
   
   public void after30(@Args Object[] args)
   {
      after30 = "void,Object[]";
   }
   
   public void after30()
   {
      after30 = "void";
   }
   
   public void after30(@Arg int arg1)
   {
      after30 = "void,int";
   }
   
   /* AFTER31 ADVICE */
   
   public Object after31(@JoinPoint MethodInfo joinPointInfo, @Return Object valueReturned)
   {
      after31 = "Object,MethodInfo,Object";
      return null;
   }
   
   public Object after31(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after31 = "Object,JoinPointInfo,SuperClass";
      return null;
   }
   
   public Object after31(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after31 = "Object,MethodInfo,float,SubValue";
      return null;
   }
   
   public Object after31(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after31 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after31(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after31 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after31(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after31 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after31(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after31 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after31(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after31 = "Object,MethodInfo,float";
      return null;
   }

   public Object after31(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after31 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SuperClass after31(@JoinPoint JoinPointInfo joinPointInfo, @Arg SubValue arg2)
   {
      after31 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after31(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after31 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after31(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after31 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after31(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after31 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after31(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after31 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after31(@JoinPoint MethodInfo joinPointInfo)
   {
      after31 = "Object,MethodInfo";
      return null;
   }
        
   public Object after31(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after31 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after31(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after31 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after31(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after31 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after31(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after31 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after31(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after31 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after31(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after31 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after31(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after31 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after31(@Return SuperClass valueReturned)
   {
      after31 = "Object,SuperClass";
      return null;
   }
   
   public void after31(@Arg float arg1, @Arg SubValue arg2)
   {
      after31 = "void,float,SubValue";
   }

   public Object after31(@Arg float arg1)
   {
      after31 = "Object,float";
      return null;
   }
   
   public void after31(@Arg SubValue arg2)
   {
      after31 = "void,SubValue";
   }
   
   public void after31(@Arg SuperValue arg2)
   {
      after31 = "void,SuperValue";
   }
   
   public void after31(@Args Object[] args)
   {
      after31 = "void,Object[]";
   }
   
   public void after31()
   {
      after31 = "void";
   }
   
   public void after31(@Arg int arg1)
   {
      after31 = "void,int";
   }
   
   /* AFTER32 ADVICE */

   public Object after32(@JoinPoint JoinPointInfo joinPointInfo,
         @Return SuperClass valueReturned)
   {
      after32 = "Object,JoinPointInfo,SuperClass";
      return null;
   }
   
   public Object after32(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after32 = "Object,MethodInfo,float,SubValue";
      return null;
   }
   
   public Object after32(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after32 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after32(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after32 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after32(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after32 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after32(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after32 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after32(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after32 = "Object,MethodInfo,float";
      return null;
   }

   public Object after32(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after32 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SuperClass after32(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after32 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after32(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after32 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after32(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after32 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after32(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after32 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after32(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after32 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after32(@JoinPoint MethodInfo joinPointInfo)
   {
      after32 = "Object,MethodInfo";
      return null;
   }
        
   public Object after32(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after32 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after32(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after32 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after32(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after32 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after32(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after32 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after32(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after32 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after32(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after32 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after32(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after32 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after32(@Return SuperClass valueReturned)
   {
      after32 = "Object,SuperClass";
      return null;
   }
   
   public void after32(@Arg float arg1, @Arg SubValue arg2)
   {
      after32 = "void,float,SubValue";
   }

   public Object after32(@Arg float arg1)
   {
      after32 = "Object,float";
      return null;
   }
   
   public void after32(@Arg SubValue arg2)
   {
      after32 = "void,SubValue";
   }
   
   public void after32(@Arg SuperValue arg2)
   {
      after32 = "void,SuperValue";
   }
   
   public void after32(@Args Object[] args)
   {
      after32 = "void,Object[]";
   }
   
   public void after32()
   {
      after32 = "void";
   }
   
   public void after32(@Arg int arg1)
   {
      after32 = "void,int";
   }
   
   /* AFTER33 ADVICE */

   public Object after33(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after33 = "Object,MethodInfo,float,SubValue";
      return null;
   }
   
   public Object after33(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after33 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after33(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after33 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after33(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after33 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after33(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after33 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after33(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after33 = "Object,MethodInfo,float";
      return null;
   }
   
   public Object after33(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after33 = "Object,MethodInfo,SuperValue";
      return null;
   }
      
   public SuperClass after33(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after33 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after33(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after33 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after33(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after33 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after33(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after33 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after33(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after33 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after33(@JoinPoint MethodInfo joinPointInfo)
   {
      after33 = "Object,MethodInfo";
      return null;
   }
        
   public Object after33(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after33 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after33(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after33 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after33(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after33 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after33(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after33 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after33(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after33 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after33(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after33 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after33(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after33 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after33(@Return SuperClass valueReturned)
   {
      after33 = "Object,SuperClass";
      return null;
   }
   
   public void after33(@Arg float arg1, @Arg SubValue arg2)
   {
      after33 = "void,float,SubValue";
   }

   public Object after33(@Arg float arg1)
   {
      after33 = "Object,float";
      return null;
   }
   
   public void after33(@Arg SubValue arg2)
   {
      after33 = "void,SubValue";
   }
   
   public void after33(@Arg SuperValue arg2)
   {
      after33 = "void,SuperValue";
   }
   
   public void after33(@Args Object[] args)
   {
      after33 = "void,Object[]";
   }
   
   public void after33()
   {
      after33 = "void";
   }
   
   public void after33(@Arg int arg1)
   {
      after33 = "void,int";
   }
   
   /* AFTER34 ADVICE */
   
   public Object after34(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after34 = "Object,MethodInfo,float,SuperValue";
      return null;
   }
   
   public Object after34(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after34 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after34(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after34 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after34(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after34 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after34(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after34 = "Object,MethodInfo,float";
      return null;
   }
   
   public Object after34(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after34 = "Object,MethodInfo,SuperValue";
      return null;
   }
   
   public SuperClass after34(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after34 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after34(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after34 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after34(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after34 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after34(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after34 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after34(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after34 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after34(@JoinPoint MethodInfo joinPointInfo)
   {
      after34 = "Object,MethodInfo";
      return null;
   }
        
   public Object after34(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after34 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after34(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after34 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after34(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after34 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after34(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after34 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after34(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after34 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after34(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after34 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after34(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after34 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after34(@Return Object valueReturned, @Args Object[] args)
   {
      after34 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after34(@Return SuperClass valueReturned)
   {
      after34 = "Object,SuperClass";
      return null;
   }
   
   public void after34(@Arg float arg1, @Arg SubValue arg2)
   {
      after34 = "void,float,SubValue";
   }

   public Object after34(@Arg float arg1)
   {
      after34 = "Object,float";
      return null;
   }
   
   public void after34(@Arg SubValue arg2)
   {
      after34 = "void,SubValue";
   }
   
   public void after34(@Arg SuperValue arg2)
   {
      after34 = "void,SuperValue";
   }
  
   public void after34(@Args Object[] args)
   {
      after34 = "void,Object[]";
   }
   
   public void after34()
   {
      after34 = "void";
   }
   
   public void after34(@Arg int arg1)
   {
      after34 = "void,int";
   }
   
   /* AFTER35 ADVICE */
   
   public Object after35(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1,
         @Arg Object arg2)
   {
      after35 = "Object,MethodInfo,float,Object";
      return null;
   }
   
   public Object after35(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after35 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after35(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after35 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after35(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after35 = "Object,MethodInfo,float";
      return null;
   }
   
   public Object after35(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after35 = "Object,MethodInfo,SuperValue";
      return null;
   }
   
   public SuperClass after35(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after35 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after35(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after35 = "SubClass,JoinPointInfo,float";
      return null;
   }

   public SuperClass after35(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after35 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after35(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after35 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after35(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after35 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after35(@JoinPoint MethodInfo joinPointInfo)
   {
      after35 = "Object,MethodInfo";
      return null;
   }
        
   public Object after35(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after35 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after35(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after35 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after35(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after35 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after35(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after35 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after35(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after35 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after35(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after35 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after35(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after35 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after35(@Return SuperClass valueReturned)
   {
      after35 = "Object,SuperClass";
      return null;
   }
   
   public void after35(@Arg float arg1, @Arg SubValue arg2)
   {
      after35 = "void,float,SubValue";
   }

   public Object after35(@Arg float arg1)
   {
      after35 = "Object,float";
      return null;
   }
   
   public void after35(@Arg SubValue arg2)
   {
      after35 = "void,SubValue";
   }
   
   public void after35(@Arg SuperValue arg2)
   {
      after35 = "void,SuperValue";
   }
   
   public void after35(@Args Object[] args)
   {
      after35 = "void,Object[]";
   }
   
   public void after35()
   {
      after35 = "void";
   }
   
   public void after35(@Arg int arg1)
   {
      after35 = "void,int";
   }
   
   /* AFTER36 ADVICE */
   
   public Object after36(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after36 = "Object,JoinPointInfo,float,SubValue";
      return null;
   }
   
   public SuperClass after36(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after36 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after36(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after36 = "Object,MethodInfo,float";
      return null;
   }
   
   public Object after36(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after36 = "Object,MethodInfo,SuperValue";
      return null;
   }
   
   public SuperClass after36(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after36 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after36(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after36 = "SubClass,JoinPointInfo,float";
      return null;
   }
   
   public SuperClass after36(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after36 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after36(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after36 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after36(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after36 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after36(@JoinPoint MethodInfo joinPointInfo)
   {
      after36 = "Object,MethodInfo";
      return null;
   }
        
   public SuperClass after36(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after36 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after36(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after36 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after36(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after36 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public Object after36(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after36 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after36(@Return SuperClass valueReturned)
   {
      after36 = "Object,SuperClass";
      return null;
   }
   
   public void after36(@Arg float arg1, @Arg SubValue arg2)
   {
      after36 = "void,float,SubValue";
   }

   public Object after36(@Arg float arg1)
   {
      after36 = "Object,float";
      return null;
   }
   
   public void after36(@Arg SubValue arg2)
   {
      after36 = "void,SubValue";
   }
   
   public void after36(@Arg SuperValue arg2)
   {
      after36 = "void,SuperValue";
   }
   
   public void after36(@Args Object[] args)
   {
      after36 = "void,Object[]";
   }
   
   public void after36()
   {
      after36 = "void";
   }
   
   public void after36(@Arg int arg1)
   {
      after36 = "void,int";
   }
   
   /* AFTER37 ADVICE */
   
   public SuperClass after37(@JoinPoint MethodInfo joinPointInfo, @Arg SubValue arg2)
   {
      after37 = "SuperClass,MethodInfo,SubValue";
      return null;
   }
   
   public Object after37(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after37 = "Object,MethodInfo,float";
      return null;
   }
   
   public Object after37(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after37 = "Object,MethodInfo,SuperValue";
      return null;
   }
   
   public SuperClass after37(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after37 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after37(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after37 = "SubClass,JoinPointInfo,float";
      return null;
   }
   
   public SuperClass after37(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after37 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after37(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after37 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after37(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after37 = "Object,Object,float";
      return null;
   }

   public SubClass after37(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after37 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after37(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after37 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after37(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after37 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after37(@JoinPoint MethodInfo joinPointInfo)
   {
      after37 = "Object,MethodInfo";
      return null;
   }
        
   public Object after37(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after37 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after37(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after37 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after37(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after37 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after37(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after37 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after37(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after37 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after37(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after37 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after37(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after37 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after37(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after37 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after37(@Return Object valueReturned, @Args Object[] args)
   {
      after37 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after37(@Return SuperClass valueReturned)
   {
      after37 = "Object,SuperClass";
      return null;
   }
   
   public void after37(@Arg float arg1, @Arg SubValue arg2)
   {
      after37 = "void,float,SubValue";
   }

   public Object after37(@Arg float arg1)
   {
      after37 = "Object,float";
      return null;
   }
   
   public void after37(@Arg SubValue arg2)
   {
      after37 = "void,SubValue";
   }
   
   public void after37(@Arg SuperValue arg2)
   {
      after37 = "void,SuperValue";
   }
   
   public void after37(@Args Object[] args)
   {
      after37 = "void,Object[]";
   }
   
   public void after37()
   {
      after37 = "void";
   }
   
   public void after37(@Arg int arg1)
   {
      after37 = "void,int";
   }
   
   /* AFTER38 ADVICE */
   
   public Object after38(@JoinPoint MethodInfo joinPointInfo, @Arg float arg1)
   {
      after38 = "Object,MethodInfo,float";
      return null;
   }
   
   public Object after38(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after38 = "Object,MethodInfo,SuperValue";
      return null;
   }
   
   public SuperClass after38(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after38 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after38(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after38 = "SubClass,JoinPointInfo,float";
      return null;
   }
   
   public SuperClass after38(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after38 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after38(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after38 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after38(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after38 = "Object,Object,float";
      return null;
   }
   
   public SubClass after38(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after38 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after38(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after38 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after38(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after38 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after38(@JoinPoint MethodInfo joinPointInfo)
   {
      after38 = "Object,MethodInfo";
      return null;
   }
        
   public Object after38(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after38 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after38(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after38 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after38(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after38 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after38(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after38 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after38(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after38 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after38(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after38 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after38(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after38 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after38(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after38 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after38(@Return Object valueReturned, @Args Object[] args)
   {
      after38 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after38(@Return SuperClass valueReturned)
   {
      after38 = "Object,SuperClass";
      return null;
   }
   
   public void after38(@Arg float arg1, @Arg SubValue arg2)
   {
      after38 = "void,float,SubValue";
   }

   public Object after38(@Arg float arg1)
   {
      after38 = "Object,float";
      return null;
   }
   
   public void after38(@Arg SubValue arg2)
   {
      after38 = "void,SubValue";
   }
   
   public void after38(@Arg SuperValue arg2)
   {
      after38 = "void,SuperValue";
   }
   
   public void after38(@Args Object[] args)
   {
      after38 = "void,Object[]";
   }
   
   public void after38()
   {
      after38 = "void";
   }
   
   public void after38(@Arg int arg1)
   {
      after38 = "void,int";
   }
   
   /* AFTER39 ADVICE */
   
   public Object after39(@JoinPoint MethodInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after39 = "Object,MethodInfo,SuperValue";
      return null;
   }
   
   public SuperClass after39(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after39 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after39(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after39 = "SubClass,JoinPointInfo,float";
      return null;
   }
   
   public SuperClass after39(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after39 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after39(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after39 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after39(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after39 = "Object,Object,float";
      return null;
   }
   
   public SubClass after39(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after39 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after39(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after39 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after39(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after39 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after39(@JoinPoint MethodInfo joinPointInfo)
   {
      after39 = "Object,MethodInfo";
      return null;
   }
   
   public Object after39(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after39 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after39(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after39 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after39(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after39 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after39(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after39 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after39(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after39 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after39(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after39 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after39(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after39 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after39(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after39 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after39(@Return Object valueReturned, @Args Object[] args)
   {
      after39 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after39(@Return SuperClass valueReturned)
   {
      after39 = "Object,SuperClass";
      return null;
   }
   
   public void after39(@Arg float arg1, @Arg SubValue arg2)
   {
      after39 = "void,float,SubValue";
   }

   public Object after39(@Arg float arg1)
   {
      after39 = "Object,float";
      return null;
   }
   
   public void after39(@Arg SubValue arg2)
   {
      after39 = "void,SubValue";
   }
   
   public void after39(@Arg SuperValue arg2)
   {
      after39 = "void,SuperValue";
   }
   
   public void after39(@Args Object[] args)
   {
      after39 = "void,Object[]";
   }
   
   public void after39()
   {
      after39 = "void";
   }
   
   public void after39(@Arg int arg1)
   {
      after39 = "void,int";
   }
   
   /* AFTER40 ADVICE */

   public SuperClass after40(@JoinPoint JoinPointInfo joinPointInfo,@Arg SubValue arg2)
   {
      after40 = "SuperClass,JoinPointInfo,SubValue";
      return null;
   }
   
   public SubClass after40(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after40 = "SubClass,JoinPointInfo,float";
      return null;
   }
   
   public SuperClass after40(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after40 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after40(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after40 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after40(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after40 = "Object,Object,float";
      return null;
   }
   
   public SubClass after40(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after40 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after40(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after40 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after40(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after40 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after40(@JoinPoint MethodInfo joinPointInfo)
   {
      after40 = "Object,MethodInfo";
      return null;
   }
        
   public Object after40(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after40 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after40(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after40 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after40(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after40 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after40(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after40 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after40(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after40 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after40(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after40 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after40(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after40 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after40(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after40 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after40(@Return Object valueReturned, @Args Object[] args)
   {
      after40 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after40(@Return SuperClass valueReturned)
   {
      after40 = "Object,SuperClass";
      return null;
   }
   
   public void after40(@Arg float arg1, @Arg SubValue arg2)
   {
      after40 = "void,float,SubValue";
   }

   public Object after40(@Arg float arg1)
   {
      after40 = "Object,float";
      return null;
   }
   
   public void after40(@Arg SubValue arg2)
   {
      after40 = "void,SubValue";
   }
   
   public void after40(@Arg SuperValue arg2)
   {
      after40 = "void,SuperValue";
   }
   
   public void after40(@Args Object[] args)
   {
      after40 = "void,Object[]";
   }
   
   public void after40()
   {
      after40 = "void";
   }
   
   public void after40(@Arg int arg1)
   {
      after40 = "void,int";
   }
   
   /* AFTER41 ADVICE */

   public SubClass after41(@JoinPoint JoinPointInfo joinPointInfo, @Arg float arg1)
   {
      after41 = "SubClass,JoinPointInfo,float";
      return null;
   }
   
   public SuperClass after41(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after41 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after41(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after41 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after41(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after41 = "Object,Object,float";
      return null;
   }
   
   public SubClass after41(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after41 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after41(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after41 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after41(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after41 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after41(@JoinPoint MethodInfo joinPointInfo)
   {
      after41 = "Object,MethodInfo";
      return null;
   }
   
   public Object after41(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after41 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after41(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after41 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after41(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after41 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after41(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after41 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after41(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after41 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after41(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after41 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after41(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after41 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after41(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after41 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after41(@Return Object valueReturned, @Args Object[] args)
   {
      after41 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after41(@Return SuperClass valueReturned)
   {
      after41 = "Object,SuperClass";
      return null;
   }
   
   public void after41(@Arg float arg1, @Arg SubValue arg2)
   {
      after41 = "void,float,SubValue";
   }

   public Object after41(@Arg float arg1)
   {
      after41 = "Object,float";
      return null;
   }
   
   public void after41(@Arg SubValue arg2)
   {
      after41 = "void,SubValue";
   }
   
   public void after41(@Arg SuperValue arg2)
   {
      after41 = "void,SuperValue";
   }
   
   public void after41(@Args Object[] args)
   {
      after41 = "void,Object[]";
   }
   
   public void after41()
   {
      after41 = "void";
   }
   
   public void after41(@Arg int arg1)
   {
      after41 = "void,int";
   }
   
   /* AFTER42 ADVICE */
   
   public SuperClass after42(@JoinPoint JoinPointInfo joinPointInfo, @Arg SuperValue arg2)
   {
      after42 = "SubClass,JoinPointInfo,SuperValue";
      return null;
   }
   
   public SubClass after42(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after42 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after42(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after42 = "Object,Object,float";
      return null;
   }
   
   public SubClass after42(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after42 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after42(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after42 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after42(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after42 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after42(@JoinPoint MethodInfo joinPointInfo)
   {
      after42 = "Object,MethodInfo";
      return null;
   }
   
   public Object after42(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after42 = "Object,SuperClass,float,SubValue";
      return null;
   }

   public Object after42(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after42 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after42(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after42 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after42(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after42 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after42(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after42 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after42(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after42 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after42(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after42 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after42(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after42 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after42(@Return Object valueReturned, @Args Object[] args)
   {
      after42 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after42(@Return SuperClass valueReturned)
   {
      after42 = "Object,SuperClass";
      return null;
   }
   
   public void after42(@Arg float arg1, @Arg SubValue arg2)
   {
      after42 = "void,float,SubValue";
   }

   public Object after42(@Arg float arg1)
   {
      after42 = "Object,float";
      return null;
   }
   
   public void after42(@Arg SubValue arg2)
   {
      after42 = "void,SubValue";
   }
   
   public void after42(@Arg SuperValue arg2)
   {
      after42 = "void,SuperValue";
   }
   
   public void after42(@Args Object[] args)
   {
      after42 = "void,Object[]";
   }
   
   public void after42()
   {
      after42 = "void";
   }
   
   public void after42(@Arg int arg1)
   {
      after42 = "void,int";
   }
   
   /* AFTER43 ADVICE */
   
   public SubClass after43(@JoinPoint Object joinPointInfo, @Arg SubValue arg2)
   {
      after43 = "SubClass,Object,SubValue";
      return null;
   }
   
   public Object after43(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after43 = "Object,Object,float";
      return null;
   }
   
   public SubClass after43(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after43 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after43(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after43 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after43(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after43 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after43(@JoinPoint MethodInfo joinPointInfo)
   {
      after43 = "Object,MethodInfo";
      return null;
   }
   
   public Object after43(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after43 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after43(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after43 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after43(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after43 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after43(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after43 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after43(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after43 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after43(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after43 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after43(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after43 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after43(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after43 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after43(@Return Object valueReturned, @Args Object[] args)
   {
      after43 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after43(@Return SuperClass valueReturned)
   {
      after43 = "Object,SuperClass";
      return null;
   }
   
   public void after43(@Arg float arg1, @Arg SubValue arg2)
   {
      after43 = "void,float,SubValue";
   }

   public Object after43(@Arg float arg1)
   {
      after43 = "Object,float";
      return null;
   }
   
   public void after43(@Arg SubValue arg2)
   {
      after43 = "void,SubValue";
   }
   
   public void after43(@Arg SuperValue arg2)
   {
      after43 = "void,SuperValue";
   }
   
   public void after43(@Args Object[] args)
   {
      after43 = "void,Object[]";
   }
   
   public void after43()
   {
      after43 = "void";
   }
   
   public void after43(@Arg int arg1)
   {
      after43 = "void,int";
   }
   
   /* AFTER44 ADVICE */
   
   public Object after44(@JoinPoint Object joinPointInfo, @Arg float arg1)
   {
      after44 = "Object,Object,float";
      return null;
   }
   
   public SubClass after44(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after44 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after44(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after44 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after44(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after44 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after44(@JoinPoint MethodInfo joinPointInfo)
   {
      after44 = "Object,MethodInfo";
      return null;
   }
   
   public Object after44(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after44 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after44(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after44 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after44(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after44 = "Object,SuperClass,float,Object";
      return null;
   }

   public SuperClass after44(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after44 = "SuperClass,MethodInfo,SuperClass,float";
      return null;
   }
   
   public Object after44(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after44 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after44(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after44 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after44(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after44 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after44(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after44 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after44(@Return Object valueReturned, @Args Object[] args)
   {
      after44 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after44(@Return SuperClass valueReturned)
   {
      after44 = "Object,SuperClass";
      return null;
   }
   
   public void after44(@Arg float arg1, @Arg SubValue arg2)
   {
      after44 = "void,float,SubValue";
   }

   public Object after44(@Arg float arg1)
   {
      after44 = "Object,float";
      return null;
   }
   
   public void after44(@Arg SubValue arg2)
   {
      after44 = "void,SubValue";
   }
   
   public void after44(@Arg SuperValue arg2)
   {
      after44 = "void,SuperValue";
   }
   
   public void after44(@Args Object[] args)
   {
      after44 = "void,Object[]";
   }
   
   public void after44()
   {
      after44 = "void";
   }
   
   public void after44(@Arg int arg1)
   {
      after44 = "void,int";
   }
   
   /* AFTER45 ADVICE */
   
   public SubClass after45(@JoinPoint Object joinPointInfo, @Arg SuperValue arg2)
   {
      after45 = "SubClass,Object,SuperValue";
      return null;
   }
   
   public SubClass after45(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after45 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after45(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after45 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after45(@JoinPoint MethodInfo joinPointInfo)
   {
      after45 = "Object,MethodInfo";
      return null;
   }
   
   public Object after45(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after45 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after45(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after45 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after45(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after45 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after45(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after45 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after45(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after45 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after45(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after45 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after45(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after45 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after45(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after45 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after45(@Return Object valueReturned, @Args Object[] args)
   {
      after45 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after45(@Return SuperClass valueReturned)
   {
      after45 = "Object,SuperClass";
      return null;
   }
   
   public void after45(@Arg float arg1, @Arg SubValue arg2)
   {
      after45 = "void,float,SubValue";
   }

   public Object after45(@Arg float arg1)
   {
      after45 = "Object,float";
      return null;
   }
   
   public void after45(@Arg SubValue arg2)
   {
      after45 = "void,SubValue";
   }
   
   public void after45(@Arg SuperValue arg2)
   {
      after45 = "void,SuperValue";
   }
   
   public void after45(@Args Object[] args)
   {
      after45 = "void,Object[]";
   }
   
   public void after45()
   {
      after45 = "void";
   }
   
   public void after45(@Arg int arg1)
   {
      after45 = "void,int";
   }
   
   /* AFTER46 ADVICE */
   
   public SubClass after46(@JoinPoint MethodInfo joinPointInfo, @Args Object[] args)
   {
      after46 = "SubClass,MethodInfo,Object[]";
      return null;
   }
   
   public SubClass after46(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after46 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after46(@JoinPoint MethodInfo joinPointInfo)
   {
      after46 = "Object,MethodInfo";
      return null;
   }
   
   public Object after46(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after46 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after46(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after46 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after46(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after46 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after46(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after46 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after46(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after46 = "Object,SuperClass,SubValue";
      return null;
   }

   public Object after46(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after46 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after46(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after46 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after46(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after46 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after46(@Return Object valueReturned, @Args Object[] args)
   {
      after46 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after46(@Return SuperClass valueReturned)
   {
      after46 = "Object,SuperClass";
      return null;
   }
   
   public void after46(@Arg float arg1, @Arg SubValue arg2)
   {
      after46 = "void,float,SubValue";
   }

   public Object after46(@Arg float arg1)
   {
      after46 = "Object,float";
      return null;
   }
   
   public void after46(@Arg SubValue arg2)
   {
      after46 = "void,SubValue";
   }
   
   public void after46(@Arg SuperValue arg2)
   {
      after46 = "void,SuperValue";
   }
   
   public void after46(@Args Object[] args)
   {
      after46 = "void,Object[]";
   }
   
   public void after46()
   {
      after46 = "void";
   }
   
   public void after46(@Arg int arg1)
   {
      after46 = "void,int";
   }
   
   /* AFTER47 ADVICE */
   
   public SubClass after47(@JoinPoint JoinPointInfo joinPointInfo, @Args Object[] args)
   {
      after47 = "SubClass,JoinPointInfo,Object[]";
      return null;
   }
   
   public Object after47(@JoinPoint MethodInfo joinPointInfo)
   {
      after47 = "Object,MethodInfo";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after47 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after47 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after47 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after47(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after47 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after47 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after47 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after47(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after47 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after47 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after47(@Return Object valueReturned, @Args Object[] args)
   {
      after47 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after47(@Return SuperClass valueReturned)
   {
      after47 = "Object,SuperClass";
      return null;
   }
   
   public void after47(@Arg float arg1, @Arg SubValue arg2)
   {
      after47 = "void,float,SubValue";
   }

   public Object after47(@Arg float arg1)
   {
      after47 = "Object,float";
      return null;
   }
   
   public void after47(@Arg SubValue arg2)
   {
      after47 = "void,SubValue";
   }
   
   public void after47(@Arg SuperValue arg2)
   {
      after47 = "void,SuperValue";
   }
   
   public void after47(@Args Object[] args)
   {
      after47 = "void,Object[]";
   }
   
   public void after47()
   {
      after47 = "void";
   }
   
   public void after47(@Arg int arg1)
   {
      after47 = "void,int";
   }
   
   /* AFTER48 ADVICE */
   
   public Object after48(@JoinPoint MethodInfo joinPointInfo)
   {
      after48 = "Object,MethodInfo";
      return null;
   }
   
   public Object after48(@JoinPoint JoinPointInfo joinPointInfo)
   {
      after48 = "Object,JoinPointInfo";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after48 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after48 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after48 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after48(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after48 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after48 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after48 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after48(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after48 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after48 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after48(@Return Object valueReturned, @Args Object[] args)
   {
      after48 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after48(@Return SuperClass valueReturned)
   {
      after48 = "Object,SuperClass";
      return null;
   }
   
   public Object after48(@Return Object valueReturned)
   {
      after48 = "Object,Object";
      return null;
   }
   
   public void after48(@Arg float arg1, @Arg SubValue arg2)
   {
      after48 = "void,float,SubValue";
   }

   public Object after48(@Arg float arg1)
   {
      after48 = "Object,float";
      return null;
   }
   
   public void after48(@Arg SubValue arg2)
   {
      after48 = "void,SubValue";
   }
   
   public void after48(@Arg SuperValue arg2)
   {
      after48 = "void,SuperValue";
   }
   
   public void after48(@Args Object[] args)
   {
      after48 = "void,Object[]";
   }
   
   public void after48()
   {
      after48 = "void";
   }
   
   public void after48(@Arg int arg1)
   {
      after48 = "void,int";
   }
   
   /* AFTER49 ADVICE */
   
   public Object after49(@JoinPoint JoinPointInfo joinPointInfo)
   {
      after49 = "Object,JoinPointInfo";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after49 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after49 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after49 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after49(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after49 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after49 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after49 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after49(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after49 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after49 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after49(@Return Object valueReturned, @Args Object[] args)
   {
      after49 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after49(@Return SuperClass valueReturned)
   {
      after49 = "Object,SuperClass";
      return null;
   }
   
   public Object after49(@Return Object valueReturned)
   {
      after49 = "Object,Object";
      return null;
   }
   
   public void after49(@Arg float arg1, @Arg SubValue arg2)
   {
      after49 = "void,float,SubValue";
   }

   public Object after49(@Arg float arg1)
   {
      after49 = "Object,float";
      return null;
   }
   
   public void after49(@Arg SubValue arg2)
   {
      after49 = "void,SubValue";
   }
   
   public void after49(@Arg SuperValue arg2)
   {
      after49 = "void,SuperValue";
   }
   
   public void after49(@Args Object[] args)
   {
      after49 = "void,Object[]";
   }
   
   public void after49()
   {
      after49 = "void";
   }
   
   public void after49(@Arg int arg1)
   {
      after49 = "void,int";
   }
   
   /* AFTER50 ADVICE */
   
   public Object after50(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SubValue arg2)
   {
      after50 = "Object,SuperClass,float,SubValue";
      return null;
   }
   
   public Object after50(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after50 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after50(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after50 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after50(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after50 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after50(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after50 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after50(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after50 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after50(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after50 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after50(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after50 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after50(@Return Object valueReturned, @Args Object[] args)
   {
      after50 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after50(@Return SuperClass valueReturned)
   {
      after50 = "Object,SuperClass";
      return null;
   }
   
   public Object after50(@Return Object valueReturned)
   {
      after50 = "Object,Object";
      return null;
   }
   
   public void after50(@Arg float arg1, @Arg SubValue arg2)
   {
      after50 = "void,float,SubValue";
   }

   public Object after50(@Arg float arg1)
   {
      after50 = "Object,float";
      return null;
   }
   
   public void after50(@Arg SubValue arg2)
   {
      after50 = "void,SubValue";
   }
   
   public void after50(@Arg SuperValue arg2)
   {
      after50 = "void,SuperValue";
   }
   
   public void after50(@Args Object[] args)
   {
      after50 = "void,Object[]";
   }
   
   public void after50()
   {
      after50 = "void";
   }
   
   public void after50(@Arg int arg1)
   {
      after50 = "void,int";
   }
   
   /* AFTER51 ADVICE */
   
   public Object after51(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg SuperValue arg2)
   {
      after51 = "Object,SuperClass,float,SuperValue";
      return null;
   }
   
   public Object after51(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after51 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after51(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after51 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after51(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after51 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after51(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after51 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after51(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after51 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after51(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after51 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after51(@Return Object valueReturned, @Args Object[] args)
   {
      after51 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after51(@Return SuperClass valueReturned)
   {
      after51 = "Object,SuperClass";
      return null;
   }
   
   public Object after51(@Return Object valueReturned)
   {
      after51 = "Object,Object";
      return null;
   }
   
   public void after51(@Arg float arg1, @Arg SubValue arg2)
   {
      after51 = "void,float,SubValue";
   }

   public Object after51(@Arg float arg1)
   {
      after51 = "Object,float";
      return null;
   }
   
   public void after51(@Arg SubValue arg2)
   {
      after51 = "void,SubValue";
   }
   
   public void after51(@Arg SuperValue arg2)
   {
      after51 = "void,SuperValue";
   }
   
   public void after51(@Args Object[] args)
   {
      after51 = "void,Object[]";
   }
   
   public void after51()
   {
      after51 = "void";
   }
   
   public void after51(@Arg int arg1)
   {
      after51 = "void,int";
   }
   
   /* AFTER52 ADVICE */
   
   public Object after52(@Return SuperClass valueReturned, @Arg float arg1,
         @Arg Object arg2)
   {
      after52 = "Object,SuperClass,float,Object";
      return null;
   }
   
   public SuperClass after52(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after52 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after52(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after52 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after52(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after52 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after52(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after52 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after52(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after52 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after52(@Return Object valueReturned, @Args Object[] args)
   {
      after52 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after52(@Return SuperClass valueReturned)
   {
      after52 = "Object,SuperClass";
      return null;
   }
   
   public Object after52(@Return Object valueReturned)
   {
      after52 = "Object,Object";
      return null;
   }
   
   public void after52(@Arg float arg1, @Arg SubValue arg2)
   {
      after52 = "void,float,SubValue";
   }
   
   public Object after52(@Arg float arg1)
   {
      after52 = "Object,float";
      return null;
   }
   
   public void after52(@Arg SubValue arg2)
   {
      after52 = "void,SubValue";
   }
   
   public void after52(@Arg SuperValue arg2)
   {
      after52 = "void,SuperValue";
   }
   
   public void after52(@Args Object[] args)
   {
      after52 = "void,Object[]";
   }
   
   public void after52()
   {
      after52 = "void";
   }
   
   public void after52(@Arg int arg1)
   {
      after52 = "void,int";
   }
   
   /* AFTER53  ADVICE */
   
   public SuperClass after53(@Return SuperClass valueReturned, @Arg float arg1)
   {
      after53 = "SuperClass,SuperClass,float";
      return null;
   }
   
   public Object after53(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after53 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after53(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after53 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after53(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after53 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after53(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after53 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after53(@Return Object valueReturned, @Args Object[] args)
   {
      after53 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after53(@Return SuperClass valueReturned)
   {
      after53 = "Object,SuperClass";
      return null;
   }
   
   public Object after53(@Return Object valueReturned)
   {
      after53 = "Object,Object";
      return null;
   }
   
   public void after53(@Arg float arg1, @Arg SubValue arg2)
   {
      after53 = "void,float,SubValue";
   }
   
   public Object after53(@Arg float arg1)
   {
      after53 = "Object,float";
      return null;
   }
   
   public void after53 (@Arg SubValue arg2)
   {
      after53  = "void,SubValue";
   }
   
   public void after53 (@Arg SuperValue arg2)
   {
      after53  = "void,SuperValue";
   }
   
   public void after53(@Args Object[] args)
   {
      after53 = "void,Object[]";
   }
   
   public void after53 ()
   {
      after53  = "void";
   }
   
   public void after53 (@Arg int arg1)
   {
      after53  = "void,int";
   }
   
   /* AFTER54  ADVICE */
   
   public Object after54(@Return SuperClass valueReturned, @Arg SubValue arg2)
   {
      after54 = "Object,SuperClass,SubValue";
      return null;
   }
   
   public Object after54(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after54 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after54(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after54 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after54(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after54 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after54(@Return Object valueReturned, @Args Object[] args)
   {
      after54 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after54(@Return SuperClass valueReturned)
   {
      after54 = "Object,SuperClass";
      return null;
   }
   
   public Object after54(@Return Object valueReturned)
   {
      after54 = "Object,Object";
      return null;
   }
   
   public void after54(@Arg float arg1, @Arg SubValue arg2)
   {
      after54 = "void,float,SubValue";
   }
   
   public Object after54(@Arg float arg1)
   {
      after54 = "Object,float";
      return null;
   }
   
   public void after54(@Arg SubValue arg2)
   {
      after54  = "void,SubValue";
   }
   
   public void after54 (@Arg SuperValue arg2)
   {
      after54  = "void,SuperValue";
   }
   
   public void after54(@Args Object[] args)
   {
      after54 = "void,Object[]";
   }
   
   public void after54 ()
   {
      after54  = "void";
   }
   
   public void after54 (@Arg int arg1)
   {
      after54  = "void,int";
   }
   
   /* AFTER55  ADVICE */
   
   public Object after55(@Return SuperClass valueReturned, @Arg SuperValue arg2)
   {
      after55 = "Object,SuperClass,SuperValue";
      return null;
   }
   
   public SuperClass after55(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after55 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after55(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after55 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after55(@Return Object valueReturned, @Args Object[] args)
   {
      after55 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after55(@Return SuperClass valueReturned)
   {
      after55 = "Object,SuperClass";
      return null;
   }
   
   public Object after55(@Return Object valueReturned)
   {
      after55 = "Object,Object";
      return null;
   }
   
   public void after55(@Arg float arg1, @Arg SubValue arg2)
   {
      after55 = "void,float,SubValue";
   }
   
   public Object after55(@Arg float arg1)
   {
      after55 = "Object,float";
      return null;
   }
   
   public void after55(@Arg SubValue arg2)
   {
      after55  = "void,SubValue";
   }
   
   public void after55 (@Arg SuperValue arg2)
   {
      after55  = "void,SuperValue";
   }
   
   public void after55(@Args Object[] args)
   {
      after55 = "void,Object[]";
   }
   
   public void after55 ()
   {
      after55  = "void";
   }
   
   public void after55 (@Arg int arg1)
   {
      after55  = "void,int";
   }
   
   /* AFTER56  ADVICE */
   
   public SuperClass after56(@Return SuperClass valueReturned, @Arg Object arg2)
   {
      after56 = "SuperClass,SuperClass,Object";
      return null;
   }
   
   public Object after56(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after56 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after56(@Return Object valueReturned, @Args Object[] args)
   {
      after56 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after56(@Return SuperClass valueReturned)
   {
      after56 = "Object,SuperClass";
      return null;
   }
   
   public Object after56(@Return Object valueReturned)
   {
      after56 = "Object,Object";
      return null;
   }
   
   public void after56(@Arg float arg1, @Arg SubValue arg2)
   {
      after56 = "void,float,SubValue";
   }
   
   public Object after56(@Arg float arg1)
   {
      after56 = "Object,float";
      return null;
   }
   
   public void after56(@Arg SubValue arg2)
   {
      after56  = "void,SubValue";
   }
   
   public void after56 (@Arg SuperValue arg2)
   {
      after56  = "void,SuperValue";
   }
   
   public void after56(@Args Object[] args)
   {
      after56 = "void,Object[]";
   }
   
   public void after56 ()
   {
      after56  = "void";
   }
   
   public void after56 (@Arg int arg1)
   {
      after56  = "void,int";
   }
   
   /* AFTER57  ADVICE */
   
   public Object after57(@Return SuperClass valueReturned, @Args Object[] args)
   {
      after57 = "Object,SuperClass,Object[]";
      return null;
   }

   public Object after57(@Return Object valueReturned, @Args Object[] args)
   {
      after57 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after57(@Return SuperClass valueReturned)
   {
      after57 = "Object,SuperClass";
      return null;
   }
   
   public Object after57(@Return Object valueReturned)
   {
      after57 = "Object,Object";
      return null;
   }
   
   public void after57(@Arg float arg1, @Arg SubValue arg2)
   {
      after57 = "void,float,SubValue";
   }
   
   public Object after57(@Arg float arg1)
   {
      after57 = "Object,float";
      return null;
   }
   
   public void after57(@Arg SubValue arg2)
   {
      after57  = "void,SubValue";
   }
   
   public void after57 (@Arg SuperValue arg2)
   {
      after57  = "void,SuperValue";
   }
   
   public void after57(@Args Object[] args)
   {
      after57 = "void,Object[]";
   }
   
   public void after57 ()
   {
      after57  = "void";
   }
   
   public void after57 (@Arg int arg1)
   {
      after57  = "void,int";
   }
   
   /* AFTER58  ADVICE */
   
   public Object after58(@Return Object valueReturned, @Args Object[] args)
   {
      after58 = "Object,Object,Object[]";
      return null;
   }
   
   public Object after58(@Return SuperClass valueReturned)
   {
      after58 = "Object,SuperClass";
      return null;
   }
   
   public Object after58(@Return Object valueReturned)
   {
      after58 = "Object,Object";
      return null;
   }
   
   public void after58(@Arg float arg1, @Arg SubValue arg2)
   {
      after58 = "void,float,SubValue";
   }
   
   public Object after58(@Arg float arg1)
   {
      after58 = "Object,float";
      return null;
   }
   
   public void after58(@Arg SubValue arg2)
   {
      after58  = "void,SubValue";
   }
   
   public void after58 (@Arg SuperValue arg2)
   {
      after58  = "void,SuperValue";
   }
   
   public void after58(@Args Object[] args)
   {
      after58 = "void,Object[]";
   }
   
   public void after58 ()
   {
      after58  = "void";
   }
   
   public void after58 (@Arg int arg1)
   {
      after58  = "void,int";
   }
   
   /* AFTER59  ADVICE */
   
   public Object after59(@Return SuperClass valueReturned)
   {
      after59 = "Object,SuperClass";
      return null;
   }
   
   public Object after59(@Return Object valueReturned)
   {
      after59 = "Object,Object";
      return null;
   }
   
   public void after59(@Arg float arg1, @Arg SubValue arg2)
   {
      after59 = "void,float,SubValue";
   }
   
   public Object after59(@Arg float arg1)
   {
      after59 = "Object,float";
      return null;
   }
   
   public void after59(@Arg SubValue arg2)
   {
      after59  = "void,SubValue";
   }
   
   public void after59 (@Arg SuperValue arg2)
   {
      after59  = "void,SuperValue";
   }
   
   public void after59(@Args Object[] args)
   {
      after59 = "void,Object[]";
   }
   
   public void after59 ()
   {
      after59  = "void";
   }
   
   public void after59 (@Arg int arg1)
   {
      after59  = "void,int";
   }
   
   /* AFTER60  ADVICE */
   
   public Object after60(@Return Object valueReturned)
   {
      after60 = "Object,Object";
      return null;
   }
   
   public void after60(@Arg float arg1, @Arg SubValue arg2)
   {
      after60 = "void,float,SubValue";
   }
   
   public Object after60(@Arg float arg1)
   {
      after60 = "Object,float";
      return null;
   }
   
   public void after60(@Arg SubValue arg2)
   {
      after60  = "void,SubValue";
   }
   
   public void after60 (@Arg SuperValue arg2)
   {
      after60  = "void,SuperValue";
   }
   
   public void after60(@Args Object[] args)
   {
      after60 = "void,Object[]";
   }
   
   public void after60 ()
   {
      after60  = "void";
   }
   
   public void after60 (@Arg int arg1)
   {
      after60  = "void,int";
   }
   
   /* AFTER61  ADVICE */
   
   public void after61(@Arg float arg1, @Arg SubValue arg2)
   {
      after61 = "void,float,SubValue";
   }
   
   public Object after61(@Arg float arg1)
   {
      after61 = "Object,float";
      return null;
   }
   
   public void after61(@Arg SubValue arg2)
   {
      after61  = "void,SubValue";
   }
   
   public void after61 (@Arg SuperValue arg2)
   {
      after61  = "void,SuperValue";
   }
   
   public void after61(@Args Object[] args)
   {
      after61 = "void,Object[]";
   }
   
   public void after61 ()
   {
      after61  = "void";
   }
   
   public void after61 (@Arg int arg1)
   {
      after61  = "void,int";
   }
   
   /* AFTER62  ADVICE */
   
   public Object after62(@Arg float arg1)
   {
      after62 = "Object,float";
      return null;
   }
   
   public void after62(@Arg SubValue arg2)
   {
      after62  = "void,SubValue";
   }
   
   public void after62 (@Arg SuperValue arg2)
   {
      after62  = "void,SuperValue";
   }
   
   public void after62(@Args Object[] args)
   {
      after62 = "void,Object[]";
   }
   
   public void after62 ()
   {
      after62  = "void";
   }
   
   public void after62 (@Arg int arg1)
   {
      after62  = "void,int";
   }
   
   /* AFTER63  ADVICE */
   
   public void after63(@Arg SubValue arg2)
   {
      after63  = "void,SubValue";
   }
   
   public void after63 (@Arg SuperValue arg2)
   {
      after63  = "void,SuperValue";
   }
   
   public void after63(@Args Object[] args)
   {
      after63 = "void,Object[]";
   }
   
   public void after63 ()
   {
      after63  = "void";
   }
   
   public void after63 (@Arg int arg1)
   {
      after63  = "void,int";
   }
   
   /* AFTER64  ADVICE */
   
   public void after64 (@Arg SuperValue arg2)
   {
      after64  = "void,SuperValue";
   }
   
   public void after64(@Args Object[] args)
   {
      after64 = "void,Object[]";
   }
   
   public void after64 ()
   {
      after64  = "void";
   }
   
   public void after64 (@Arg int arg1)
   {
      after64  = "void,int";
   }
   
   /* AFTER65  ADVICE */
   
   public void after65(@Args Object[] args)
   {
      after65 = "void,Object[]";
   }
   
   public void after65 ()
   {
      after65  = "void";
   }
   
   public void after65 (@Arg int arg1)
   {
      after65  = "void,int";
   }
   
   /* AFTER66  ADVICE */
   
   public void after66 ()
   {
      after66  = "void";
   }
   
   public void after66 (@Arg int arg1)
   {
      after66  = "void,int";
   }
}