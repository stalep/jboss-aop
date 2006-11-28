package org.jboss.test.aop.args;

import junit.framework.Assert;

import org.jboss.aop.ConstructorInfo;
import org.jboss.aop.FieldInfo;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Thrown;

/**
 * Aspect used on @JoinPoint parameter tests.
 * 
 * @author Flavia Rainone
 */
public class JoinPointAspect
{
   static String beforeAdvice = null;
   static JoinPointInfo beforeJoinPointInfo = null;
   static String afterAdvice = null;
   static JoinPointInfo afterJoinPointInfo = null;
   static String throwingAdvice = null;
   static JoinPointInfo throwingJoinPointInfo = null;
   
   public static void clear()
   {
      beforeAdvice = null;
      beforeJoinPointInfo = null;
      afterAdvice = null;
      afterJoinPointInfo = null;
      throwingAdvice = null;
      throwingJoinPointInfo = null;
   }
   
   public void before1()
   {
      beforeAdvice = "before1";
   }
   
   public void before2(@JoinPoint Object joinPointInfo)
   {
      beforeAdvice = "before2";
      beforeJoinPointInfo = (JoinPointInfo) joinPointInfo;
   }
   
   public void before3(@JoinPoint JoinPointInfo joinPointInfo)
   {
      beforeAdvice = "before3";
      beforeJoinPointInfo = joinPointInfo;
   }   
   
   public void before4(@JoinPoint MethodInfo joinPointInfo)
   {
      beforeAdvice = "before4";
      beforeJoinPointInfo = (JoinPointInfo) joinPointInfo;
   }
   
   public void before5(@JoinPoint MethodInfo joinPointInfo)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after1(@JoinPoint FieldInfo joinPointInfo)
   {
      afterAdvice = "after1";
      afterJoinPointInfo = joinPointInfo;
   }
   
   public void after2(@JoinPoint ConstructorInfo constructorInfo)
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
      afterJoinPointInfo = (JoinPointInfo) joinPointInfo;
   }
   
   public void after5(@JoinPoint JoinPointInfo joinPointInfo)
   {
      afterAdvice = "after5";
      afterJoinPointInfo = joinPointInfo;
   }
   
   public void throwing1(@Thrown Throwable throwable, @JoinPoint Object joinPointInfo)
   {
      throwingAdvice = "throwing1";
      throwingJoinPointInfo = (JoinPointInfo) joinPointInfo;
   }
   
   public void throwing2(@JoinPoint ConstructorInfo joinPointInfo,
         @Thrown Throwable throwable)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing3(@Thrown Throwable throwable,
         @JoinPoint JoinPointInfo joinPointInfo)
   {
      throwingAdvice = "throwing3";
      throwingJoinPointInfo = joinPointInfo;
   }
   
   public void throwing4(@Thrown Throwable throwable)
   {
      throwingAdvice = "throwing4";
   }
   
   public void throwing5(@JoinPoint MethodInfo joinPointInfo, @Thrown Throwable throwable)
   {
      throwingAdvice = "throwing5";
      throwingJoinPointInfo = joinPointInfo;
   }
}