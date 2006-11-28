package org.jboss.test.aop.args;

import junit.framework.Assert;

import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.FieldInvocation;
import org.jboss.aop.joinpoint.FieldReadInvocation;
import org.jboss.aop.joinpoint.Invocation;

/**
 * Aspect used on @org.jboss.aop.advice.annotation.Invocation parameter tests.
 * 
 * @author Flavia Rainone
 */
public class InvocationAspect
{
   static String advice = null;
   static Object invokeReturn = null;
   
   public static void clear()
   {
      advice = null;
      invokeReturn = null;
   }
   
   public void before(@org.jboss.aop.advice.annotation.Invocation Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after(@org.jboss.aop.advice.annotation.Invocation Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing(@org.jboss.aop.advice.annotation.Invocation Object object)
   {
      Assert.fail("This advice should never be executed");
   }

   public Object aroundMethodExecution1()
   {
      advice = "aroundMethodExecution1";
      return "aroundMethodExecution1";
   }
   
   public int aroundMethodExecution2(@org.jboss.aop.advice.annotation.Invocation
         ConstructorInvocation invocation) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return 0;
   }
   
   public Object aroundMethodCalledByMethod(@org.jboss.aop.advice.annotation.Invocation
         Invocation invocation) throws Throwable
   {
      advice = "aroundMethodCalledByMethod";
      invokeReturn = invocation.invokeNext();
      return 15000;
   }
   
   public Object aroundFieldWrite(@org.jboss.aop.advice.annotation.Invocation
         FieldInvocation invocation) throws Throwable
   {
      advice = "aroundFieldWrite";
      invokeReturn = invocation.invokeNext();
      return null;
   }
   
   public int aroundFieldRead(@org.jboss.aop.advice.annotation.Invocation
         FieldReadInvocation invocation) throws Throwable
   {
      advice = "aroundFieldRead";
      invokeReturn = invocation.invokeNext();
      return 500;
   }
}