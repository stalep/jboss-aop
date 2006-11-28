package org.jboss.test.aop.args;

/**
 * Plain old java object used on @org.jboss.aop.advice.annotation.Invocation parameter
 * tests.
 * 
 * @author Flavia Rainone
 */
public class InvocationPOJO
{
   public int number;

   public int callerMethod(int i)
   {
      return calleeMethod(i);
   }
   
   private int calleeMethod(int i)
   {
      return i*2;
   }
   
   public String method1()
   {
      return "method";
   }
   
   public int method2()
   {
      return 2;
   }
}