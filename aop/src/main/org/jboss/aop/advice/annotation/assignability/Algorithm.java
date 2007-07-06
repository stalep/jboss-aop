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
package org.jboss.aop.advice.annotation.assignability;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public enum Algorithm
{
   
   VARIABLE_TARGET()
   {
      protected Algorithm getInverseAlgorithm()
      {
         return FROM_VARIABLE;
      }
      
      protected boolean isVariableOperationApplicable(Type type, Type fromType)
      {
         return type instanceof TypeVariable;
      }
      
      protected boolean assignValue(Type type, Type fromType,
            VariableHierarchy variableHierarchy, boolean boundLevel)
      {
         VariableNode node = variableHierarchy.getVariableNode((TypeVariable) type);
         return node.assignValue(fromType, boundLevel);
      }
      
      protected boolean addBound(Type type, Type fromType,
            VariableHierarchy variableHierarchy)
      {
         VariableNode node = variableHierarchy.getVariableNode((TypeVariable) type);
         return node.addLowerBound(fromType);
      }
   },
   FROM_VARIABLE()
   {
      protected Algorithm getInverseAlgorithm()
      {
         return VARIABLE_TARGET;
      }
      
      protected boolean isVariableOperationApplicable(Type type, Type fromType)
      {
         return fromType instanceof TypeVariable;
      }
      
      protected boolean assignValue(Type type, Type fromType,
            VariableHierarchy variableHierarchy, boolean boundLevel)
      {
         VariableNode fromNode = variableHierarchy.getVariableNode((TypeVariable) fromType);
         return fromNode.assignValue(type, boundLevel);
      }
      
      protected boolean addBound(Type type, Type fromType,
            VariableHierarchy variableHierarchy)
      {
         VariableNode fromNode = variableHierarchy.getVariableNode((TypeVariable) fromType);
         return fromNode.addUpperBound(type);
      }
   };

   protected abstract Algorithm getInverseAlgorithm();
   protected abstract boolean isVariableOperationApplicable(Type type, Type fromType);
   
   protected abstract boolean assignValue(Type type, Type fromType,
         VariableHierarchy variableHierarchy, boolean boundLevel);
   
   protected abstract boolean addBound(Type type, Type fromType,
         VariableHierarchy variableHierarchy);
   
   public boolean isAssignable(Type type, Type fromType,
         VariableHierarchy variableHierarchy)
   {
      // special case, check fromType
      if (fromType instanceof WildcardType)
      {
         return isAssignable(type, (WildcardType) fromType, variableHierarchy);
      }
      if (isVariableOperationApplicable(type, fromType))
      {
         return addBound(type, fromType, variableHierarchy);
      }
      if (type instanceof Class)
      {
         return isAssignable((Class<?>) type, fromType, variableHierarchy);
      }
      if (type instanceof ParameterizedType)
      {
         return isAssignable((ParameterizedType) type, fromType,
               variableHierarchy, false);
      }
      if (type instanceof WildcardType)
      {
         throw new RuntimeException("This comparison should never happen");
      }
      else
      {
         return isAssignable((GenericArrayType) type, fromType,
               variableHierarchy);
      }
   }
   
   private boolean isAssignable(Type type, WildcardType fromWildcardType,
         VariableHierarchy variableHierarchy)
   {
      boolean boundOk = false;
      for (Type upperBound: fromWildcardType.getUpperBounds())
      {
         if (isAssignable(type, upperBound, variableHierarchy))
         {
            boundOk = true;
            break;
         }
      }
      if (!boundOk)
      {
         return false;
      }
      for (Type lowerBound: fromWildcardType.getLowerBounds())
      {
         if (isAssignable(type, lowerBound, variableHierarchy))
         {
            return true;
         }
      }
      return fromWildcardType.getLowerBounds().length == 0;
   }

   // is classType super of fromType?
   private boolean isAssignable(Class<?> classType, Type fromType,
         VariableHierarchy variableHierarchy)
   {
      if (fromType instanceof Class)
      {
         return classType.isAssignableFrom((Class<?>) fromType);
      }
      else if (fromType instanceof ParameterizedType)
      {
         return classType.isAssignableFrom(
               (Class<?>) ((ParameterizedType) fromType).getRawType());
      }
      else if (fromType instanceof TypeVariable)
      {
         Type[] bounds = getConcreteBounds(fromType);
         boolean inside = false;
         for (int i = 0; i < bounds.length && !inside; i++)
         {
            if (bounds[i] instanceof Class)
            {
               if (classType.isAssignableFrom((Class<?>) bounds[i]))
               {
                  inside = true;
               }
            }
            else
            {
               // bound must be a parameterized type
               if (classType.isAssignableFrom(
                     (Class<?>) ((ParameterizedType) bounds[i]).getRawType()))
               {
                  inside = true;
               }
            }
         }
         return inside;
      }
      // type instanceof GenericArrayType (ommitting check for performance
      // reasons)
      if (classType == Object.class)
      {
         return true;
      }
      if (classType.isArray())
      {
         return isAssignable(classType.getComponentType(),
               ((GenericArrayType) fromType).getGenericComponentType(),
               variableHierarchy);
      }
      return false;
   }

   /**
    * @param type
    * @return
    */
   private Type[] getConcreteBounds(Type type)
   {
      TypeVariable current = (TypeVariable) type;
      Type[] bounds = current.getBounds();
      while (bounds.length == 1 && bounds[0] instanceof TypeVariable)
      {
         current = (TypeVariable) bounds[0];
         bounds = current.getBounds();
      }
      return bounds;
   }

   private boolean isAssignable(ParameterizedType paramType, Type fromType, 
         VariableHierarchy variableHierarchy, boolean boundLevel)
   {
      if (fromType instanceof TypeVariable)
      {
         Type[] concreteBounds = getConcreteBounds((TypeVariable) fromType);
         for (int i = 0; i < concreteBounds.length; i++)
         {
            if (isAssignable(paramType, concreteBounds[i], variableHierarchy, true))
            {
               return true;
            }
         }
         return false;
      }
      return ParamTypeAssignabilityAlgorithm.isAssignable(
            paramType, fromType, CHECKER, this, variableHierarchy, boundLevel);
   }

   private boolean isAssignable(GenericArrayType arrayType, Type fromType,
         VariableHierarchy variableHierarchy)
   {
      if (fromType instanceof Class)
      {
         Class<?> fromClass = (Class<?>) fromType;
         if (!fromClass.isArray())
         {
            return false;
         }
         return isAssignable(arrayType.getGenericComponentType(),
               fromClass.getComponentType(), variableHierarchy);
      }
      if (fromType instanceof GenericArrayType)
      {
         GenericArrayType fromArrayType = (GenericArrayType) fromType;
         return isAssignable(arrayType.getGenericComponentType(),
               fromArrayType.getGenericComponentType(), variableHierarchy);
      }
      return false;
   }

   //////////////////////////////////////////////////////////
   private static final ParamTypeAssignabilityAlgorithm.EqualityChecker<Algorithm, VariableHierarchy> CHECKER
      = new ParamTypeAssignabilityAlgorithm.EqualityChecker<Algorithm, VariableHierarchy>()
   {
      public boolean isSame(Type type, Type fromType, Algorithm client, VariableHierarchy variableHierarchy, boolean boundLevel)
      {
         if(Algorithm.VARIABLE_TARGET.isVariableOperationApplicable(type, fromType))
         {
            return Algorithm.VARIABLE_TARGET.assignValue(type, fromType, variableHierarchy, boundLevel);
         }
         if (type instanceof Class)
         {
            return type.equals(fromType);
         }
         if (type instanceof ParameterizedType)
         {
            if (!(fromType instanceof ParameterizedType))
            {
               return false;
            }
            ParameterizedType fromParamType = (ParameterizedType) fromType;
            ParameterizedType paramType = (ParameterizedType) type;
            if (!isSame(paramType.getRawType(), fromParamType.getRawType(), client,
                  variableHierarchy, boundLevel))
            {
               return false;
            }
            return isSame(paramType.getActualTypeArguments(),
                  fromParamType.getActualTypeArguments(), client, variableHierarchy, boundLevel);
         }
         if (type instanceof WildcardType)
         {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            Type[] lowerBounds = ((WildcardType) type).getLowerBounds();
            if (fromType instanceof WildcardType)
            {
               Type[] fromUpperBounds = ((WildcardType) fromType).getUpperBounds();
               outer: for (int i = 0; i < upperBounds.length; i++)
               {
                  for (int j = 0; j < fromUpperBounds.length; j++)
                  {
                     if (client.isAssignable(upperBounds[i],
                           fromUpperBounds[i], variableHierarchy))
                     {
                        continue outer;
                     }
                  }
                  return false;
               }
               Type[] fromLowerBounds = ((WildcardType) fromType).getLowerBounds();
               outer: for (int i = 0; i < lowerBounds.length; i++)
               {
                  for (int j = 0; j < fromLowerBounds.length; j++)
                  {
                     if (client.getInverseAlgorithm().isAssignable(
                           fromLowerBounds[i],
                           lowerBounds[i], variableHierarchy))
                     {
                        continue outer;
                     }
                  }
                  return false;
               }
               return true;
            }
            else
            {
               for (int i = 0; i < upperBounds.length; i++)
               {
                  if (!client.isAssignable(upperBounds[i], fromType,
                        variableHierarchy))
                  {
                     return false;
                  }
               }
               for (int i = 0; i < lowerBounds.length; i++)
               {
                  if (!client.getInverseAlgorithm().isAssignable(
                        fromType, lowerBounds[i], variableHierarchy))
                  {
                     return false;
                  }
               }
               return true;
            }
         }
         return true;
      }
   };
}