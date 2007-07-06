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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


/**
 * 
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
class VariableNode
{
   private VariableHierarchy hierarchy;
   private VariableNode previous;
   private VariableNode next;
   private Collection<Type> lowerBounds;
   private TypeVariable variable;
   private Type assignedValue;
   
   public VariableNode(TypeVariable content, VariableHierarchy hierarchy)
   {
      this.hierarchy = hierarchy;
      this.variable = content;
      lowerBounds = new HashSet<Type>();
      Type[] bounds = content.getBounds();
      if (bounds.length == 1 && bounds[0] instanceof TypeVariable)
      {
         TypeVariable typeVariable = (TypeVariable) bounds[0];
         next = hierarchy.getVariableNode(typeVariable);
         next.previous = this;
      }
   }
   
   public final boolean assignValue(Type value, boolean boundLevel)
   {
      if (this.hierarchy.isBoundComparation() && !(value instanceof Class)
            && !(value instanceof ParameterizedType))
      {
         return false;
      }
      
      if (boundLevel && value instanceof WildcardType)
      {
         return false;
      }
      
      if (this.assignedValue != null)
      {
         // TODO HERE1 HERE HERE HERE HERE
         return isSame(value, this.assignedValue, true);
      }
      
      // TODO fix this
      /*if (value instanceof WildcardType)
      {
         return false;
      }*/
      
      if (!isInsideBounds(value, true) ||
            (this.previous != null && !this.previous.areBoundsInside(value)))
      {
         return false;
      }
      this.assignedValue = value;
      return true;
   }
   
   public final boolean addLowerBound(Type lowerBound)
   {
      if (!isInsideBounds(lowerBound, false) ||
            (this.previous != null && !this.previous.areBoundsInside(lowerBound)))
      {
         return false;
      }
      // TODO check this
      if (lowerBound instanceof TypeVariable)
      {
         Type[] bounds = ((TypeVariable) lowerBound).getBounds();
         this.lowerBounds.add(new ChoiceBound((TypeVariable) lowerBound, bounds));
      }
      else
      {
         this.lowerBounds.add(lowerBound);
      }
      return true;
   }
   
   public final boolean addUpperBound(Type upperBound)
   {
      // TODO
      return true;
   }
   
   private boolean areBoundsInside(Type bound)
   {
      if (this.assignedValue != null && !isAssignable(bound, assignedValue))
      {
         return false;
      }
      for (Type lowerBound: this.lowerBounds)
      {
         if (!isAssignable(bound, lowerBound))
         {
            return false;
         }
      }
      if (previous != null)
      {
         return previous.areBoundsInside(bound);
      }
      return true;
   }
   
   private boolean isInsideBounds(Type lowerBound, boolean checkLowerBounds)
   {
      if (this.assignedValue != null && !isAssignable(lowerBound, assignedValue))
      {
         return false;
      }
      if (checkLowerBounds)
      {
         for (Type bound: this.lowerBounds)
         {
            if (!isAssignable(bound, lowerBound))
            {
               return false;
            }
         }
      }
      if (next == null)
      {
         Type[] bounds = variable.getBounds();
         this.hierarchy.startBoundComparation();
         try
         {
            for (int i = 0; i < bounds.length; i++)
            {
               if (!Algorithm.VARIABLE_TARGET.isAssignable(bounds[i], lowerBound, hierarchy))
               {
                  return false;
               }
            }
         }
         finally
         {
            this.hierarchy.finishBoundComparation();
         }
         return true;
      }
      return next.isInsideBounds(lowerBound, true);
   }
   
   private boolean isAssignable(TypeVariable type, TypeVariable fromType)
   {
      Type[] fromBounds = fromType.getBounds();
      if (type == fromType)
      {
         return true;
      }
      while (fromBounds.length == 1 && fromBounds[0] instanceof TypeVariable)
      {
         if (fromBounds[0] == type)
         {
            return true;
         }
         fromType = (TypeVariable) fromBounds[0];
         fromBounds = fromType.getBounds();
      }
      TypeVariable variable = (TypeVariable) type;
      Type[] bounds = variable.getBounds();
      while (bounds.length == 1 && bounds[0] instanceof TypeVariable)
      {
         if (bounds[0] == fromType)
         {
            return false;
         }
         variable = (TypeVariable) bounds[0];
         bounds = variable.getBounds();
      }
      // TODO check if this should really be removed
      /*outer: for (int i = 0; i < bounds.length; i++)
      {
         for (int j = 0; j < fromBounds.length; j++)
         {
            if (isAssignable(bounds[i], fromBounds[j]))
            {
               continue outer;
            }
         }
         return false;
      }
      return true;*/
      return false;
   }
   
   // both type and bound belong to the same context
   private boolean isAssignable(Type type, Type fromType)
   {
      if (fromType instanceof TypeVariable)
      {
         TypeVariable fromVariable = (TypeVariable) fromType;
         if (type instanceof TypeVariable)
         {
            return isAssignable((TypeVariable) type, fromVariable);
         }
         Type[] fromBounds = fromVariable.getBounds();
         TypeVariable temp = fromVariable;
         while (fromBounds.length == 1 && fromBounds[0] instanceof TypeVariable)
         {
            temp = (TypeVariable) fromBounds[0];
            fromBounds = temp.getBounds();            
         }
         for (Type fromBound: fromBounds)
         {
            if (isAssignable(type, fromBound))
            {
               return true;
            }
         }
         return false;
      }
      if (fromType instanceof ChoiceBound)
      {
         ChoiceBound fromChoiceBound = (ChoiceBound) fromType;
         if (type instanceof TypeVariable &&
               !isAssignable((TypeVariable) type, fromChoiceBound.variable))
         {
            return false;
         }
         for (Iterator<Type> it = fromChoiceBound.bounds.iterator(); it.hasNext();)
         {
            Type fromOption = it.next();
            if (!isAssignable(type, fromOption))
            {
               it.remove();
            }
         }
         return !fromChoiceBound.bounds.isEmpty();
      }
      if (type instanceof Class)
      {
         if (type == Object.class)
         {
            return true;
         }
         Class<?> clazz = (Class<?>) type;
         if (fromType instanceof Class)
         {
            return clazz.isAssignableFrom((Class<?>) fromType);
         }
         if (fromType instanceof ParameterizedType)
         {
            return clazz.isAssignableFrom((Class<?>) ((ParameterizedType) fromType).getRawType());
         }
         if (fromType instanceof WildcardType)
         {
            WildcardType fromWildcard = (WildcardType) fromType;
            boolean boundOk = false;
            for (Type upperBound: fromWildcard.getUpperBounds())
            {
               if (isAssignable(type, upperBound))
               {
                  boundOk = true;
                  break;
               }
            }
            if (!boundOk)
            {
               return false;
            }
            for (Type lowerBound: fromWildcard.getLowerBounds())
            {
               if (isAssignable(type, lowerBound))
               {
                  return true;
               }
            }
            return false;
         }
         if (fromType instanceof TypeVariable)
         {
            for (Type upperBound: ((TypeVariable) fromType).getBounds())
            {
               if (isAssignable(type, upperBound))
               {
                  return true;
               }
            }
            return false;
         }
         return clazz.isArray() && isAssignable(clazz.getComponentType(), 
               ((GenericArrayType) fromType).getGenericComponentType());
      }
      if (type instanceof ParameterizedType)
      {
         return ParamTypeAssignabilityAlgorithm.isAssignable(
               (ParameterizedType) type, fromType, CHECKER, this, null, false);
      }
      if (type instanceof TypeVariable)
      {
         for (Type bound: ((TypeVariable) type).getBounds())
         {
            if (!isAssignable(bound, fromType))
            {
               return false;
            }
         }
         return true;
      }
      ChoiceBound choiceBound = (ChoiceBound) type;
      if (fromType instanceof TypeVariable &&
            !isAssignable(choiceBound.variable, (TypeVariable) fromType))
      {
         return false;
      }
      for (Iterator<Type> it = choiceBound.bounds.iterator(); it.hasNext();)
      {
         Type boundOption = it.next();
         if (!isAssignable(boundOption, fromType))
         {
            it.remove();
         }
      }
      return !choiceBound.bounds.isEmpty();
   }
   
   protected boolean isSame(Type argument, Type fromArgument, boolean argumentAssigned)
   {
      if (argument instanceof WildcardType)
      {
         WildcardType wildcard = (WildcardType) argument;
         Type[] upperBounds = wildcard.getUpperBounds();
         Type[] lowerBounds = wildcard.getLowerBounds();
         if (fromArgument instanceof WildcardType)
         {
            WildcardType fromWildcard = (WildcardType) fromArgument;
            Type[] fromUpperBounds = fromWildcard.getUpperBounds();
            if (!isAssignable(upperBounds, fromUpperBounds))
            {
               return false;
            }
            
            Type[] fromLowerBounds = fromWildcard.getLowerBounds();
            outer: for (int i = 0; i < fromLowerBounds.length; i++)
            {
               for (int j = 0; j < lowerBounds.length; j++)
               {
                  if (isAssignable(fromLowerBounds[i], lowerBounds[j]))
                  {
                     continue outer;
                  }
               }
               return false;
            }
            return true;
         }
         if (argumentAssigned)
         {
            return false;
         }
         if(fromArgument instanceof TypeVariable)
         {
            if (!isAssignable(upperBounds, ((TypeVariable) fromArgument).getBounds()))
            {
               return false;
            }
         }
         else
         {
            for (int i = 0; i < upperBounds.length; i++)
            {
               if (!isAssignable(upperBounds[i], fromArgument))
               {
                  return false;
               }
            }
            return true;
         }
      }
      else if (argument instanceof GenericArrayType)
      {
         if (fromArgument instanceof GenericArrayType)
         {
            return isSame(((GenericArrayType) argument).getGenericComponentType(),
                  ((GenericArrayType) fromArgument).getGenericComponentType(),
                  argumentAssigned);
         }
         else
         {
            return false;
         }
      }
      return argument.equals(fromArgument); // TODO check this works correctly
   }

   /**
    * @param node
    * @param upperBounds
    * @param upperBounds
    */
   private boolean isAssignable(Type[] upperBounds, Type[] fromUpperBounds)
   {
      outer: for (int i = 0; i < upperBounds.length; i++)
      {
         for (int j = 0; j < fromUpperBounds.length; j++)
         {
            if (isAssignable(upperBounds[i], fromUpperBounds[j]))
            {
               continue outer;
            }
         }
         return false;
      }
      return true;
   }
   
   private static ParamTypeAssignabilityAlgorithm.EqualityChecker<VariableNode, ?> CHECKER =
      new ParamTypeAssignabilityAlgorithm.EqualityChecker<VariableNode, Object>()
      {
         public boolean isSame(Type argument, Type fromArgument, VariableNode node,
               Object token, boolean boundLevel)
         {
            return node.isSame(argument, fromArgument, false);
         }
      };
}