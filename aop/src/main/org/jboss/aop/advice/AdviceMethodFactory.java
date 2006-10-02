/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/ 
package org.jboss.aop.advice;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javassist.NotFoundException;

import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.util.ReflectUtils;

/**
 * Utility class to figure out which advice method to use for a given joinpoint
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AdviceMethodFactory
{
   private static final int IS_BEFORE = 1;
   private static final int IS_AFTER = 2;
   private static final int IS_THROWING = 3;
   private static final int IS_AROUND = 4;
   
   public static final AdviceMethodFactory BEFORE = new AdviceMethodFactory(IS_BEFORE, false, true, false, false);
   public static final AdviceMethodFactory AFTER = new AdviceMethodFactory(IS_AFTER, false, true, true, false);
   public static final AdviceMethodFactory THROWING = new AdviceMethodFactory(IS_THROWING, false, true, false, true);
   public static final AdviceMethodFactory AROUND = new AdviceMethodFactory(IS_AROUND, true, false, false, false);
   
   private static final Class INVOCATION = Invocation.class;
   private static final Class THROWABLE = Throwable.class;
         
   int type;
   boolean canHaveJoinpoint;
   boolean canHaveInvocation;
   boolean mustHaveReturnType;
   boolean mustHaveThrowable;
   
   static final MatchData BEST_MATCH_START = new MatchData();
   
   private AdviceMethodFactory(int type, boolean canHaveInvocation, boolean canHaveJoinpoint, boolean canHaveReturnType, boolean canHaveThrowable)
   {
      this.type = type;
      this.canHaveInvocation = canHaveInvocation;
      this.canHaveJoinpoint = canHaveJoinpoint;
      this.mustHaveReturnType = canHaveReturnType;
      this.mustHaveThrowable = canHaveThrowable;
   }
   
   public AdviceMethodProperties findAdviceMethod(AdviceMethodProperties properties)
   {
      Method[] methods = ReflectUtils.getMethodsWithName(properties.getAspectClass(), properties.getAdviceName());
    
      if (methods.length == 0) return null;
      if (methods.length == 1 && methods[0].getParameterTypes().length == 0)
      {
         if (mustHaveReturnType && !properties.getJoinpointReturnType().equals(Void.TYPE))
         {
            return null;
         }
         if (mustHaveThrowable)
         {
            return null;
         }
         properties.setFoundProperties(methods[0], new ArrayList());
         return properties;
      }
      
      MatchData bestMatch = BEST_MATCH_START;
      for (int i = 0 ; i < methods.length ; i++)
      {
         MatchData matchData = matchParameters(properties, methods[i], bestMatch);
         if (matchData != null)
         {
            bestMatch = matchData;
         }
      }
      
      if (bestMatch.method != null)
      {
         return setupMethodAndArgsInProperties(properties, bestMatch);
      }
         
      
      return null;
   }
   
   private AdviceMethodProperties setupMethodAndArgsInProperties(AdviceMethodProperties properties, MatchData matchData)
   {
      ArrayList args = new ArrayList();
      if (matchData.invocationMatchDegree >= 0)
      {
         if (canHaveInvocation) args.add(AdviceMethodProperties.INVOCATION_ARG);
         else if (canHaveJoinpoint) args.add(AdviceMethodProperties.JOINPOINT_ARG);
      }

      if (matchData.returnOrThrowingMatchDegree >= 0)
      {
         if (mustHaveReturnType) args.add(AdviceMethodProperties.RETURN_ARG);
         else if (mustHaveThrowable) args.add(AdviceMethodProperties.THROWABLE_ARG);
      }

      if (matchData.argsIndices != null)
      {
         args.addAll(matchData.argsIndices);
      }
      
      properties.setFoundProperties(matchData.method, args);
      
      return properties;
   }

   private MatchData matchParameters(AdviceMethodProperties properties, Method adviceMethod, MatchData bestMatch)
   {
      Class[] adviceParams = adviceMethod.getParameterTypes();
      if (adviceParams.length == 0 && bestMatch.method == null) return new MatchData(adviceMethod); 

      MatchData currentMatch = lookForJoinPointInfoOrInvocation(properties, adviceMethod, bestMatch);
      if (currentMatch == null)
      {
         return null;
      }

      currentMatch = lookForThrowingOrReturn(properties, adviceMethod, currentMatch, bestMatch);
      if (currentMatch == null)
      {
         return null;
      }

      currentMatch = matchActualArgs(properties, adviceMethod, currentMatch, bestMatch);
      if (currentMatch == null)
      {
         return null;
      }

      if (currentMatch.currentParam == adviceMethod.getParameterTypes().length) return currentMatch;
      
      return currentMatch;
   }
   
   
   
   /**
    * Looks for an Invocation or a JoinPointInfo in the first parameter of the looked for method.
    * Returns -1 if it cannot find the required class, 0 means an exact match, 1 the superclass of what we would expect etc.
    */
   private MatchData lookForJoinPointInfoOrInvocation(AdviceMethodProperties properties, Method method, MatchData bestMatch)
   {
      Class[] adviceParams = method.getParameterTypes();
      int index = 0;
      int matchDegree = -1;
      boolean firstIsSpecial = false;
   
      if (canHaveInvocation)
      {
         //Check if adviceParams[index] is invocation of correct type
         if (isInvocation(adviceParams[index]))
         {
            firstIsSpecial = true;
            matchDegree = matchClass(adviceParams[index], properties.getInvocationType());
         }
      }
      else if (canHaveJoinpoint)
      {
         //Check if adviceParams[index] is JoinPoint of correct type
         if (isInfo(adviceParams[index]))
         {
            firstIsSpecial = true;
            matchDegree = matchClass(adviceParams[index], properties.getInfoType());
         }
      }
      
      if (firstIsSpecial)
      {
         if (matchDegree < 0)
         {
            //First param was an invocation/joinpoint, but not of the right type
            return null;
         }
         else
         {
            index++;
         }
      }

      //Check if the best match is still the best
      if (bestMatch.invocationMatchDegree >= 0 && (bestMatch.invocationMatchDegree < matchDegree || !firstIsSpecial))
      {
         return null;
      }

      return new MatchData(method, index, matchDegree);
   }
   
   private MatchData lookForTargetObject(AdviceMethodProperties properties, Method adviceMethod, MatchData currentMatch, MatchData bestMatch) throws NotFoundException
   {
      return currentMatch;
   }
   
   private MatchData lookForThrowingOrReturn(AdviceMethodProperties properties, Method adviceMethod, MatchData currentMatch, MatchData bestMatch)
   {
      if (currentMatch.currentParam == adviceMethod.getParameterTypes().length)
      {
         return currentMatch;
      }

      if (mustHaveReturnType)
      {
         currentMatch = lookForReturn(properties, adviceMethod, currentMatch, bestMatch);
         if (currentMatch == null)
         {
            return null;
         }
      }
      else if (mustHaveThrowable)
      {
         //Check if adviceParams[index] is subclass of throwable/one of the exceptions thrown by the method/ctor
         currentMatch.returnOrThrowingMatchDegree = matchClass(adviceMethod.getParameterTypes()[currentMatch.currentParam], THROWABLE);
         currentMatch.currentParam++;
      }

      //Check if the best match is still the best
      if (bestMatch.returnOrThrowingMatchDegree >= 0 && bestMatch.returnOrThrowingMatchDegree < currentMatch.returnOrThrowingMatchDegree)
      {
         return null;
      }
      return currentMatch;
   }
   
   private MatchData lookForReturn(AdviceMethodProperties properties, Method adviceMethod, MatchData currentMatch, MatchData bestMatch)
   {
      //Check if adviceParams[index] has same return type as method/field get/ctor
      Class returnType = properties.getJoinpointReturnType(); 
      if (returnType == null || returnType.equals(Void.TYPE))
      {
      }
      else
      {
         //Check that the return type is correct - the return type of the called method must be a 
         //subclass of the return type of the advice
         currentMatch.returnOrThrowingMatchDegree = subClassMatch(returnType, adviceMethod.getReturnType());
         
         
         
         if (currentMatch.returnOrThrowingMatchDegree < 0)
         {
            return null;
         }
         
         //Now check that we take the correct type of return parameter
         Class param = adviceMethod.getParameterTypes()[currentMatch.currentParam];

         if (!returnType.equals(param))
         {
            int match2 = subClassMatch(returnType, param);
            if (match2 < 0)
            {
               return null;
            }
            
            currentMatch.returnOrThrowingMatchDegree = (currentMatch.returnOrThrowingMatchDegree + match2)/2;
         }
         currentMatch.currentParam++;
      }
      
      return currentMatch;
   }
   
   private MatchData matchActualArgs(AdviceMethodProperties properties, Method adviceMethod, MatchData currentMatch, MatchData bestMatch)
   {
      int adviceParam = currentMatch.currentParam;
      Class[] adviceParams = adviceMethod.getParameterTypes();
      
      Class[] targetParams = properties.getJoinpointParameters();
      
      if (adviceParams.length - currentMatch.currentParam > targetParams.length)
      {
         //The advice method takes more args than the target joinpoint itself! 
         return null;
      }
      
      if (adviceParam == adviceParams.length)
      {
         //There are no more parameters, return without initialising the argsIndices
         return nullOrCurrentMatch(currentMatch, bestMatch);
      }
      
      currentMatch.argsIndices = new ArrayList();
      for (int i = 0; i < targetParams.length && adviceParam < adviceParams.length; i++)
      {
         int match = matchClass(adviceParams[adviceParam], targetParams[i]);
         if (match < 0)
         {
            continue;
         }
         else
         {
            adviceParam++;
            currentMatch.argsIndices.add(new Integer(i));
            currentMatch.argsDegreeSum += match;
         }
      }
      
      if (currentMatch.argsIndices.size() != adviceParams.length - currentMatch.currentParam)
      {
         return null;
      }
      
      return nullOrCurrentMatch(currentMatch, bestMatch);
   }
   
   /**
    * Returns null if bestMatch was the best, otherwise returns currentMatch
    */
   private MatchData nullOrCurrentMatch(MatchData currentMatch, MatchData bestMatch)
   {
      if (currentMatch.compare(bestMatch, true) < 0)
      {
         return currentMatch;
      }
      
      return null;
   }
   
   private boolean isInvocation(Class clazz)
   {
      return Invocation.class.isAssignableFrom(clazz);
   }
   
   private boolean isInfo(Class clazz)
   {
      return JoinPointInfo.class.isAssignableFrom(clazz);
   }
   
   /**
    * Checks if clazz is of type lookingFor. The returned "match degree" indicates how many superclasses/interfaces we
    * had to go through until finding lookingFor
    */
   private int matchClass(Class clazz, Class lookingFor)
   {
      return matchClass(clazz, lookingFor, 0);
   }

   private int subClassMatch(Class clazz, Class superClass)
   {
      return matchClass(superClass, clazz);
   }
   
   private int matchClass(Class wanted, Class candidate, int matchDegree)
   {
      if (candidate == null) return -1;
      if (candidate.equals(wanted))
      {
         return matchDegree;
      }

      matchDegree++;
      
      Class[] interfaces = candidate.getInterfaces();
      for (int i = 0 ; i < interfaces.length ; i++)
      {
         if (matchClass(wanted, interfaces[i], matchDegree) > 0) return matchDegree;
      }
      
      if (matchClass(wanted, candidate.getSuperclass(), matchDegree) > 0) return matchDegree;
      
      return -1;
   }
   
}

class MatchData
{
   Method method;
   int invocationMatchDegree = -1;
   int returnOrThrowingMatchDegree = -1;
   ArrayList argsIndices = null;
   int argsDegreeSum = 0;
   
   int currentParam;
   
   MatchData()
   {
   }
   
   MatchData(Method method)
   {
      this.method = method;
   }
   
   MatchData(Method method, int currentParam, int firstParamMatchDegree)
   {
      this.method = method;
      this.currentParam = currentParam;
      this.invocationMatchDegree = firstParamMatchDegree;
   }
   
   public String toString()
   {
      return "MatchData[invMatch="+ invocationMatchDegree + "rtnMatch=" + returnOrThrowingMatchDegree + "args=" + ((argsIndices != null )? argsIndices.size() : 0) + "sum=" + argsDegreeSum + "]";
   }
   
   /**
    * Return -1 if mine is a better match, 1 if other is, and 0 if equal
    */
   int compare(MatchData other, boolean checkArgs)
   {
      int invMatch = compareMatchDegrees(this.invocationMatchDegree, other.invocationMatchDegree);
      if (invMatch != 0)
      {
         return invMatch;
      }
      
      int retMatch = compareMatchDegrees(this.returnOrThrowingMatchDegree, other.returnOrThrowingMatchDegree);
      if (retMatch != 0)
      {
         return retMatch;
      }
      
      if (checkArgs)
      {
         if (this.argsIndices == null && other.argsIndices == null) return 0;
         else if (this.argsIndices != null && other.argsIndices == null) return -1;
         else if (this.argsIndices == null && other.argsIndices != null) return 1;
         else
         {
            if (this.argsIndices.size() > other.argsIndices.size()) return -1;
            else if (this.argsIndices.size() < other.argsIndices.size()) return 1;
            else
            {
               return this.argsDegreeSum < other.argsDegreeSum ? -1 : 1;
            }
         }      
      }      
      
      return 0;
   }
   
   /**
    * Return -1 if mine is a better match, 1 if other is, and 0 if equal
    */
   int compareMatchDegrees(int mine, int other)
   {
      if (mine < 0 && other >= 0)
      {
         return 1;
      }
      else if (mine >= 0 && other < 0)
      {
         return -1;
      }
      else if (mine >= 0 && other >= 0 && mine != other)
      {
         return mine > other ? -1 : 1;
      }
      
      return 0;//They are equal
   }
   
}
