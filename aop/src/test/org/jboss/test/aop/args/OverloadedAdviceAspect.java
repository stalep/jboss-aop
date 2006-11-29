package org.jboss.test.aop.args;

import junit.framework.Assert;

import org.jboss.aop.FieldInfo;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.CurrentInvocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Aspect used on overloaded advice tests.
 *
 * @author Flavia Rainone
 */
public class OverloadedAdviceAspect
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

   /* BEFORE ADVICES */
   
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
   
   public void before1(@JoinPoint MethodInfo constructorInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before1(@Arg SuperValue text)
   {
      Assert.fail("This advice should never be executed");
   }
   
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
   
   /* AROUND ADVICES */
   
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