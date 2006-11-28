package org.jboss.test.aop.args;

/**
 * Plain old java object used on @Thrown parameter tests.
 * 
 * @author Flavia Rainone
 */
public class ThrownPOJO
{
   public void method1(int i) throws POJOException
   {
      throw new POJOException(i);
   }
   
   public void method2(int i) throws POJOException
   {
      throw new POJOException(i);
   }
   
   public void method3(int i) throws POJOException
   {
      throw new POJOException(i);
   }
   
   public void method4(int i) throws POJOException
   {
      throw new POJOException(i);
   }
   
   public void method5(int i) throws POJOException
   {
      throw new POJOException(i);
   }
   
   public void method6() throws POJOException
   {
      throw new POJOException(6);
   }
}