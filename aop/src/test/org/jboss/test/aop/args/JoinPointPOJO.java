package org.jboss.test.aop.args;

/**
 * Plain old java object used on @JoinPoint parameter tests.
 * 
 * @author Flavia Rainone
 */
public class JoinPointPOJO
{
   public int number;
   public String text;
   
   public void method1()
   {
      
   }
   
   public String method2(boolean shouldThrow) throws POJOException
   {
      if (shouldThrow)
      {
         throw new POJOException();
      }
      return "method3";
   }
   
   public void method3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method4() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method5() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method6() throws POJOException
   {
      throw new POJOException();
   }
}