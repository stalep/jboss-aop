package org.jboss.aop.advice.annotation;

/**
 * Exception thrown when there is need to create more than one {@link
 * AnnotatedParameterAdviceInfo} to represent the same advice method.
 * 
 * @author Flavia Rainone
 */
class MultipleAdviceInfoException extends Exception
{

   private static final long serialVersionUID = 1L;
   
   /**
    * Contains the total number of AdviceInfo instances needed to represent the advice
    * method that triggered this exception.
    */
   int totalInstances;
   
   /**
    * Contains information about all parameter annotations of the advice method that
    * triggered this exception. 
    */
   int[][] annotations;
   
   public MultipleAdviceInfoException(int totalInstances, int[][] annotations)
   {
      this.totalInstances = totalInstances;
      this.annotations = annotations;
   }
}