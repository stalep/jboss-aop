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
package org.jboss.aop.advice.annotation;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 *
 */
class ContextualizedArguments
{
   static abstract class EqualityChecker<T>
   {
      protected boolean isSame(Type[] arguments, Type[] assignableArguments, T token)
      {
         for (int i = 0; i < arguments.length; i++)
         {
            if (!isSame(arguments[i], assignableArguments[i], token))
            {
               return false;
            }
         }
         return true;
      }
      
      abstract boolean isSame(Type argument, Type assignableArgument, T token);
   }
   
   
   public static<T> boolean isAssignable(EqualityChecker<T> action, ParameterizedType paramType, Type assignable, T token)
   {
      Class<?> typeRaw = null;
      ParameterizedType assignableParamType = null;
      Class<?> desiredType = (Class<?>) paramType.getRawType();
      if (assignable instanceof Class)
      {
         typeRaw = (Class<?>) assignable;
         if (!desiredType.isAssignableFrom(typeRaw))
         {
            return false;
         }
         if (typeRaw.getTypeParameters().length > 0)
         {
            // notice that, if typeClass equals the fromParamType, we also have
            // the
            // result true with a warning (i.e., this if is not only for
            // typeClass
            // subclass of fromParamType, but also for typeCass same as
            // fromParamType raw
            return true;// TODO With warning
         }
      }
      else if (assignable instanceof ParameterizedType)
      {
         assignableParamType = (ParameterizedType) assignable;
         typeRaw = (Class<?>) assignableParamType.getRawType();
         if (typeRaw == desiredType)
         {
            // compare arguments with arguments
            return action.isSame(paramType.getActualTypeArguments(),
                  assignableParamType.getActualTypeArguments(), token);
         }
         else if (!desiredType.isAssignableFrom(typeRaw))
         {
            return false;
         }
      }
      else
      {
         return false;
      }
      // try to get, if null, warning, parameters lost in hierarchy
      Type[] arguments = getContextualizedArguments(assignableParamType, typeRaw, desiredType);
      if (arguments == null)
      {
         return true; // TODO with Warning
      }
      return action.isSame(paramType.getActualTypeArguments(), arguments, token);
   }
   
   
   
   
   public static final Type[] getContextualizedArguments(ParameterizedType paramType,
         Class rawType, Class desiredType)
   {
      ContextualizedArguments contextualizedArguments = getContextualizedArgumentsInternal(
            desiredType, rawType);
      if (contextualizedArguments == null)
      {
         return null;
      }
      if (paramType != null)
      {
         contextualizedArguments.contextualizeVariables(null, paramType);
      }
      return contextualizedArguments.getArguments();
   }

   private static final ContextualizedArguments getContextualizedArgumentsInternal(
         Class<?> desiredType, Class<?> classType)
   {
      Type superType = null;
      if (desiredType.isInterface())
      {
         for (Type superInterface : classType.getGenericInterfaces())
         {
            if ((superInterface instanceof Class && desiredType
                  .isAssignableFrom((Class<?>) superInterface))
                  || (superInterface instanceof ParameterizedType &&
                     desiredType.isAssignableFrom((Class<?>)
                           ((ParameterizedType) superInterface).getRawType())))
            {
               superType = superInterface;
               break;
            }
         }
      }
      else
      {
         superType = classType.getGenericSuperclass();
      }
      ContextualizedArguments result = null;
      if (superType instanceof Class)
      {
         if (superType == desiredType)
         {
            return null;
         }
         result = getContextualizedArgumentsInternal(desiredType,
               (Class<?>) superType);
      }
      else
      {
         ParameterizedType superParamType = (ParameterizedType) superType;
         Class<?> superClassType = (Class<?>) superParamType.getRawType();
         if (superClassType == desiredType)
         {
            return new ContextualizedArguments(superParamType
                  .getActualTypeArguments(), classType);
         }
         else
         {
            result = getContextualizedArgumentsInternal(desiredType,
                  superClassType);
         }
      }
      if (result == null
            || !result.contextualizeVariables(classType, superType))
      {
         return null;
      }
      return result;
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   private static ReplacementRecorder<ContextualizedArguments> replacementCreator = new ReplacementRecorder<ContextualizedArguments>()
   {
      public void recordReplacement(ContextualizedArguments outer, int variableIndex, Type[] replacementTarget, int targetIndex)
      {
         outer.initialize();
         outer.createVariableReplacement(variableIndex, outer.arguments, targetIndex);
      }
   };
   
   private Type[] arguments;
   //private Collection<VariableReplacement> variableReplacements;
   private LinkedList<VariableReplacement> variableReplacements;
   
   // declaring class extends queried class (DeclaringClass<A, B, C> extends Queried<X, Y, Z, ..., W>,
   // where X, Y, Z...W, are a list of types for which we need to map (contextualize)
   // variables
   // A, B, C... D are variables of DeclaringClass, that may be used in the contextualization proccess
   public ContextualizedArguments(Type[] result, Class<?> declaringClass)
   {
      this.arguments = result;
      for (int i = 0; i < result.length; i++)
      {
         Type newArgument = processArgument(result, i, declaringClass,
               replacementCreator, this);
         if (newArgument != null)
         {
            this.arguments[i] = newArgument;
         }
      }
   }

   private static <O> Type processArgument(Type[] argumentContainer, int argumentIndex,
         Class<?> declaringClass, ReplacementRecorder<O> recorder, O outer)
   {
      Type argument = argumentContainer[argumentIndex];
      if (argument instanceof Class)
      {
         return null;
      }
      if (argument instanceof ParameterizedType)
      {
         ParameterizedType paramType = (ParameterizedType) argument;
         ParameterizedType_ newParamType = null;
         Type[] arguments = paramType.getActualTypeArguments();
         for (int i = 0; i < arguments.length; i++)
         {
            Type newType = processArgument(arguments, i, declaringClass, recorder,
                  outer);
            if (newType != null)
            {
               if (newParamType == null)
               {
                  newParamType = new ParameterizedType_(paramType);
               }
               newParamType.getActualTypeArguments()[i] = newType;
            }
         }
         return newParamType;
      }
      //else if (bounds[i] instanceof TypeVariable)
      if (declaringClass == null)
      {
         return null;
      }
      String paramName = ((TypeVariable) argument).getName();
      int index = 0;
      TypeVariable[] typeVariables = declaringClass.getTypeParameters();
      for (index = 0; index < typeVariables.length; index ++)
      {
         if (typeVariables[index].getName().equals(paramName)){
            break;
         }
      }
      recorder.recordReplacement(outer, index, argumentContainer, argumentIndex);
      return argument; 
   }

   boolean initialized = false;
   
   private void initialize()
   {
      if (!initialized)
      {
         Type[] oldResult = this.arguments;
         this.arguments = new Type[oldResult.length];
         System.arraycopy(oldResult, 0, this.arguments, 0, this.arguments.length);
         this.variableReplacements = new LinkedList<VariableReplacement>();
         initialized = true;
      }
   }
   private ListIterator<VariableReplacement> iterator;
   // newDeclaringClass extends/implements oldDeclaringType
   // returns false = warning (work with raw type hence)
   public boolean contextualizeVariables(Class newDeclaringClass, Type oldDeclaringType)
   {
      if (!initialized || variableReplacements.isEmpty())
      {
         initialized = false;
         return true;
      }
      if (oldDeclaringType instanceof Class)
      {
         return false; // warning
      }
      ParameterizedType oldParamType = (ParameterizedType) oldDeclaringType;
      for (iterator = variableReplacements.listIterator(); iterator.hasNext(); )
      {
         if(iterator.next().replace(oldParamType, newDeclaringClass))
         {
            iterator.remove();
         }
      }
      iterator = null;
      return true; 
   }
   
   public Type[] getArguments()
   {
      return this.arguments;
   }
   
   public void createVariableReplacement(int variableIndex, Type[] replacementTarget,
         int targetIndex)
   {
      if (iterator != null)
      {
         iterator.add(new VariableReplacement(variableIndex, replacementTarget,
            targetIndex));
      }
      else
      {
         this.variableReplacements.add(new VariableReplacement(
               variableIndex, replacementTarget, targetIndex));
      }
   }
   private static ReplacementRecorder<VariableReplacement> updater = new ReplacementRecorder<VariableReplacement>()
   {
      public void recordReplacement(VariableReplacement outer, int variableIndex, Type[] replacementTarget, int targetIndex)
      {
         if (outer.pendingReplacement) // outer is already busy
         {
            outer.getOuter().createVariableReplacement(variableIndex,
                  replacementTarget, targetIndex);
         }
         else
         {
            outer.variableIndex = variableIndex;
            outer.target = replacementTarget;
            outer.targetIndex = targetIndex;
            outer.pendingReplacement = true;
         }
      }
   };
   
   class VariableReplacement
   {
      private int variableIndex;
      private Type[] target;
      private int targetIndex;
      private boolean pendingReplacement;
      
      public VariableReplacement(int variableIndex, Type[] target, int targetIndex)
      {
         this.variableIndex = variableIndex;
         this.target = target;
         this.targetIndex = targetIndex;
         this.pendingReplacement = true;
      }
      
      // return true if replacement has been done for good
      public boolean replace(ParameterizedType paramType, Class<?> declaringClass)
      {
         target[targetIndex] = paramType.getActualTypeArguments()[variableIndex];
         this.pendingReplacement = false;
         Type newType = ContextualizedArguments.processArgument(target, targetIndex,
               declaringClass, updater, this);
         if (newType != null)
         {
            target[targetIndex] = newType;
            return false;
         }
         return true;
      }
      
      ContextualizedArguments getOuter()
      {
         return ContextualizedArguments.this;
      }
   }
   
   private static class ParameterizedType_ implements ParameterizedType
   {
      private Type[] arguments;
      private Type ownerType;
      private Type rawType;
      
      ParameterizedType_(ParameterizedType type)
      {
         Type[] actualArguments = type.getActualTypeArguments();
         this.arguments = new Type[actualArguments.length];
         System.arraycopy(actualArguments, 0, arguments, 0, actualArguments.length);
         this.ownerType = type.getOwnerType();
         this.rawType = type.getRawType();
      }
      
      
      public Type[] getActualTypeArguments()
      {
         return this.arguments;
      }

      public Type getOwnerType()
      {
         return this.ownerType;
      }

      public Type getRawType()
      {
         return this.rawType;
      }
      
      public boolean equals(Object obj)
      {
         if (!(obj instanceof ParameterizedType))
         {
            return false;
         }
         ParameterizedType other = (ParameterizedType) obj;
         if (!this.ownerType.equals(other.getOwnerType()) ||
             !this.rawType.equals(other.getRawType()))
         {
            return false;
         }
         Type[] otherArguments = other.getActualTypeArguments();
         for (int i = 0; i < arguments.length; i++)
         {
            if (!arguments[i].equals(otherArguments[i]))
            {
               return false;
            }
         }
         return true;
      }
   }
   
   interface ReplacementRecorder<T>
   {
      public void recordReplacement(T outer, int variableIndex, Type[] replacementTarget, int targetIndex);
      
   }
}