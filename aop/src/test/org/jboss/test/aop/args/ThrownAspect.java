package org.jboss.test.aop.args;

import junit.framework.Assert;

import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.Thrown;

/**
 * Aspect used on @Thrown parameter tests.
 * 
 * @author Flavia Rainone
 */
public class ThrownAspect
{

   public static String advice;
   public static Throwable thrown;
   public static int thrownNumber;
   
   public static void clear()
   {
      advice = null;
      thrown = null;
      thrownNumber = 0;
   }
   
   public void throwing1()
   {
      advice = "throwing1";
   }
   
   public void throwing2(@Thrown Throwable throwable, @Arg int i)
   {
      advice = "throwing2";
      thrownNumber = i;
      thrown = throwable;
   }
   
   public void throwing3(@Thrown Exception exception)
   {
      advice = "throwing3";
      thrownNumber = ((POJOException) exception).number;
      thrown = exception;
   }
   
   public void throwing4(@Thrown POJOException pojoException, @Arg int i)
   {
      advice = "throwing4";
      thrownNumber = i;
      thrown = pojoException;
   }
   
   public void throwing5(@Thrown RuntimeException runtimeException)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing6(Throwable throwable)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing7(@Arg int i)
   {
      Assert.fail("This advice should never be executed");
   }
}