package org.jboss.aop.advice.annotation;

import org.jboss.aop.AspectManager;

/**
 * Exception thrown when an advice method does not comply with a parameter rule.  
 * 
 * @author Flavia Rainone
 */
class ParameterAnnotationRuleException extends Exception
{
   private static final long serialVersionUID = 1L;
   
   /**
    * Constructor.
    * <p>
    * Adds <code>errorMessage</code> to {@link AdviceMethodFactory#adviceMatchingMessage}
    * on verbose mode.
    * 
    * @param message a message describing why the parameter annotation rule could
    *                not be applied to an advice method
    */
   public ParameterAnnotationRuleException(String message)
   {
      super(message);
      if (AspectManager.verbose)
      {
         AdviceMethodFactory.adviceMatchingMessage.append(message);
      }
   }
}