package org.jboss.aop.advice.annotation;

import java.lang.reflect.Method;

import org.jboss.aop.advice.AdviceMethodProperties;
import org.jboss.aop.advice.annotation.AdviceMethodFactory.ReturnType;

/**
 * Contains information about an advice method and its matching process.
 * 
 * @author Flavia Rainone
 */
abstract class AdviceInfo implements Comparable<AdviceInfo>
{
   // the righest the rank the better this advice is
   protected int rank;
   // advice method
   protected Method method;
   // since method.getParameterTypes creates a vector, better store this information
   // instead of calling repeatedly getParameterTypes
   protected Class<?>[] parameterTypes;
   
   /**
    * Creates an advice info.
    * 
    * @param method the advice method
    * @param rank   the initial rank value of this advice
    */
   protected AdviceInfo(Method method, int rank)
   {
      this.method = method;
      this.rank = rank;
      this.parameterTypes = method.getParameterTypes();
   }
   
   /**
    * Indicates the distance between <code>class</code> and <code>lookingFor</code>
    * in the class hierarchy.
    * 
    * @param clazz      the type of an annotated parameter
    * @param lookingFor the expected type of the parameter
    * @return {@link AdviceMethodFactory#NOT_ASSIGNABLE_DEGREE if a value of type <code>
    *         lookingFor</code> can't be assigned to a parameter of type <code>class
    *         </code>; the distance between <code>class </code> and <code>lookingFor
    *         </code> otherwise.
    */
   protected short matchClass(Class clazz, Class lookingFor)
   {
      return matchClass(clazz, lookingFor, (short) 0);
   }
   
   /**
    * Recursive method that return the distance between <code>wanted</code> and <code>
    * candidate</code> in the class hierarchy.
    * 
    * @param wanted      the expected type of the parameter
    * @param candidate   the current type being matched
    * @param matchDegree the current matchDegree
    * @return -1 if a value of type <code>lookingFor</code> can't be assigned to
    *         a parameter of type <code>class</code>; the distance between <code>class
    *         </code> and <code>lookingFor</code> otherwise.
    */
   private short matchClass(Class wanted, Class candidate, short matchDegree)
   {
      if (candidate == null)
      {
         return AdviceMethodFactory.NOT_ASSIGNABLE_DEGREE;
      }
      if (candidate.equals(wanted))
      {
         return matchDegree;
      }

      matchDegree++;
      
      Class[] interfaces = candidate.getInterfaces();
      for (int i = 0 ; i < interfaces.length ; i++)
      {
         if (matchClass(wanted, interfaces[i], matchDegree) > 0)
         {
            return matchDegree;
         }
      }
      
      if (matchClass(wanted, candidate.getSuperclass(), matchDegree) > 0)
      {
         return matchDegree;
      }
      return AdviceMethodFactory.NOT_ASSIGNABLE_DEGREE;
   }

   /**
    * Returns the distance in hierarchy between the annotated parameter identified by
    * <code>annotationIndex</code>, and the expected type of this parameter.
    * 
    * @param annotationIndex  identifies a parameter annotation rule
    * @param properties       contains information about the queried advice method
    * @return                 the assignability degree if there is a parameter with the
    *                         annotation identified by <code>typeIndex</code>;
    *                         {@link AdviceMethodFactory#NOT_ASSIGNABLE_DEGREE} otherwise.
    */
   public short getReturnAssignabilityDegree(AdviceMethodProperties properties)
   {
      Class returnType = this.method.getReturnType();
      if (returnType == void.class)
      {
         return AdviceMethodFactory.NOT_ASSIGNABLE_DEGREE;
      }
      short degree = this.matchClass(properties.getJoinpointReturnType(), returnType);
      if (degree == AdviceMethodFactory.NOT_ASSIGNABLE_DEGREE)
      {
         // return type is Object.class and join point return type is not
         // Object is worse than join point return type, but better than -1
         return AdviceMethodFactory.MAX_ASSIGNABLE_DEGREE;
      }
      return degree;
   }

   
   /**
    * Returns the rank of this advice.
    * @return the rank value
    */
   public final int getRank()
   {
      return rank;
   }
   
   /**
    * Compares this advice info against <code>o</code> in decreasing order of the rank
    * value.
    */
   public int compareTo(AdviceInfo o)
   {
      return ((AdviceInfo)o).rank - rank;
   }
   
   public String toString()
   {
      return method.toString();
   }
   
   /**
    * Validate this advice, indicating whether it can be the answer to the method query
    * contained in <code>properties</code>.
    * 
    * @param properties        contains information about the queried method
    * @param mutuallyExclusive a list of mutually exclusive rules
    * @param returnType        the expected return type
    * @return                  <code>true</code> only if this advice is valid
    */
   public abstract boolean validate(AdviceMethodProperties properties,
         int[][] mutuallyExclusive, ReturnType returnType);

   /**
    * Returns the distance in hierarchy between the annotated parameter identified by
    * <code>annotationIndex</code>, and the expected type of this parameter.
    * 
    * @param annotationIndex  identifies a parameter annotation rule
    * @param properties       contains information about the queried advice method
    * @return                 the assignability degree if there is a parameter with the
    *                         annotation identified by <code>typeIndex</code>;
    *                         {@link AdviceMethodFactory#NOT_ASSIGNABLE_DEGREE} otherwise.
    */
   public abstract short getAssignabilityDegree(int annotationIndex,
         AdviceMethodProperties properties);
   
   /**
    * Assign information of this advice to <code>properties</code>.
    * 
    * @param properties contains information about the queried advice method.
    */
   public abstract void assignAdviceInfo(AdviceMethodProperties properties);
}