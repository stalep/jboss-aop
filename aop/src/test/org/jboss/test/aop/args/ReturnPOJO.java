package org.jboss.test.aop.args;

/**
 * Plain old java object used both on @Return parameter tests, and on advice return type
 * tests.
 * 
 * @author Flavia Rainone
 */
public class ReturnPOJO
{
   public void method1() {}

   public String method2()
   {
      return "method2";
   }
   
   public String method3()
   {
      return "method3";
   }
   
   public String method4()
   {
      return "method4";
   }
   
   public String method5()
   {
      return "method5";
   }
   
   public String method6()
   {
      return "method6";
   }
   
   public String method7()
   {
      return "method7";
   }
   
   public SubValue method8()
   {
      return new SubValue(8);
   }
   
   public SubValue method9()
   {
      return new SubValue(9);
   }
   
   public SuperValue method10()
   {
      return new SuperValue(10);
   }
   
   public SuperValue method11()
   {
      return new SuperValue(11);
   }
   
   public Object method12() throws POJOException
   {
      throw new POJOException();
   }
}