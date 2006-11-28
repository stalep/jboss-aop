package org.jboss.test.aop.args;

/**
 * Class used on annotated parameter tests.
 * 
 * @author Flavia Rainone
 */
public class POJOException extends Exception
{
   private static final long serialVersionUID = 1L;

   /**
    * A number that identifies the exception thrown.
    */
   int number;

   /**
    * Constructor.
    */
   public POJOException()
   {
      this(0);
   }

   /**
    * Constructor.
    * 
    * @param number a number identifying the exception.
    */
   public POJOException(int number)
   {
      this.number = number;  
   }
}