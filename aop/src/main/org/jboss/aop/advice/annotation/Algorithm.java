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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 * 
 */
public class Algorithm
{
   private static final Algorithm INSTANCE = new Algorithm();
   public static Algorithm getInstance()
   {
      return INSTANCE;
   }

   public Map<String, VariableNode> getInitialHierarchy()
   {
      return new HashMap<String, VariableNode>();
   }
   
   public boolean isAssignable(Type type, Type fromType,
         Map<String, VariableNode> variableHierarchy)
   {
      if (type instanceof Class)
      {
         return isAssignable((Class<?>) type, fromType, variableHierarchy);
      }
      if (type instanceof ParameterizedType)
      {
         return isAssignable((ParameterizedType) type, fromType,
               variableHierarchy);
      }
      if (type instanceof TypeVariable)
      {
         VariableNode node = null;
         TypeVariable variable = (TypeVariable) type;
         if (variableHierarchy.containsKey(variable.getName()))
         {
            node = variableHierarchy.get(variable.getName());
         } else
         {
            node = new VariableNode(variable, variableHierarchy);
         }
         return node.addLowerBound(fromType);
      }
      if (type instanceof WildcardType)
      {
         throw new RuntimeException("This comparison should never happen");
      } else
      {
         return isAssignable((GenericArrayType) type, fromType,
               variableHierarchy);
      }
   }

   // is classType super of fromType?
   private boolean isAssignable(Class<?> classType, Type fromType,
         Map<String, VariableNode> variableHierarchy)
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
         Map<String, VariableNode> variableHierarchy)
   {
      if (fromType instanceof TypeVariable)
      {
         Type[] concreteBounds = getConcreteBounds((TypeVariable) fromType);
         for (int i = 0; i < concreteBounds.length; i++)
         {
            if (isAssignable(paramType, concreteBounds[i], variableHierarchy))
            {
               return true;
            }
         }
         return false;
      }
      return ParamTypeAssignabilityAlgorithm.isAssignable(
            paramType, fromType, CHECKER, variableHierarchy);
   }

   private boolean isAssignable(GenericArrayType arrayType, Type fromType,
         Map<String, VariableNode> variableHierarchy)
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
   private static final ParamTypeAssignabilityAlgorithm.EqualityChecker<Map<String, VariableNode>> CHECKER
      = new ParamTypeAssignabilityAlgorithm.EqualityChecker<Map<String,VariableNode>>()
   {
      public boolean isSame(Type type, Type fromType, Map<String, VariableNode> variableHierarchy)
      {
         if (type instanceof TypeVariable)
         {
            TypeVariable variable = (TypeVariable) type;
            VariableNode node = variableHierarchy.containsKey(variable.getName())?
                  variableHierarchy.get(variable.getName()):
                     new VariableNode(variable, variableHierarchy);
                  return node.assignValue(fromType);
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
            if (!isSame(paramType.getRawType(), fromParamType.getRawType(),
                  variableHierarchy))
            {
               return false;
            }
            return isSame(paramType.getActualTypeArguments(),
                  fromParamType.getActualTypeArguments(), variableHierarchy);
         }
         if (type instanceof WildcardType)
         {
            Type[] upperBounds = ((WildcardType) type).getUpperBounds();
            Algorithm algorithm = Algorithm.getInstance();
            if (fromType instanceof WildcardType)
            {
               Type[] fromUpperBounds = ((WildcardType) fromType).getUpperBounds();
               outer: for (int i = 0; i < upperBounds.length; i++)
               {
                  for (int j = 0; j < fromUpperBounds.length; j++)
                  {
                     if (algorithm.isAssignable(upperBounds[i],
                           fromUpperBounds[i], variableHierarchy))
                     {
                        continue outer;
                     }
                  }
                  return false;
               }
               // TODO lower bounds: inverted algorithm
               return true;
            }
            else
            {
               for (int i = 0; i < upperBounds.length; i++)
               {
                  if (!algorithm.isAssignable(upperBounds[i], fromType,
                        variableHierarchy))
                  {
                     return false;
                  }
               }
               // TODO lower bounds: inverted algorithm
               return true;
            }
         }
         return true;
      }
   };
}