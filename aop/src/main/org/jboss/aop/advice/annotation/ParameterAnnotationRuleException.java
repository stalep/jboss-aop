package org.jboss.aop.advice.annotation;

import org.jboss.aop.advice.InvalidAdviceException;

/**
 * Exception thrown when an advice method does not comply with a parameter rule.  
 * 
 * @author Flavia Rainone
 */
public class ParameterAnnotationRuleException extends InvalidAdviceException
{
   private static final long serialVersionUID = 9190978361997650638L;

   /**
    * Constructor.
    * <p>
    * @param message a message describing why the parameter annotation rule could
    *                not be applied to an advice method
    */
   public ParameterAnnotationRuleException(String message)
   {
      super(message);
   }
}