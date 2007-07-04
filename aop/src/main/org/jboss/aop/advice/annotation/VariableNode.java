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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class VariableNode
{
   private VariableNode previous;
   private VariableNode next;
   private Collection<Type> lowerBounds;
   private TypeVariable variable;
   private Type assignedValue;
   
   public VariableNode(TypeVariable content, Map<String, VariableNode> map)
   {
      this.variable = content;
      lowerBounds = new HashSet<Type>();
      Type[] bounds = content.getBounds();
      if (bounds.length == 1 && bounds[0] instanceof TypeVariable)
      {
         TypeVariable typeVariable = (TypeVariable) bounds[0];
         if (map.containsKey(typeVariable.getName()))
         {
            next = map.get(typeVariable.getName());
         }
         else
         {
            next = new VariableNode(typeVariable, map);
         }
         next.previous = this;
      }
      map.put(content.getName(), this);
   }
   
   public final boolean assignValue(Type value)
   {
      if (this.assignedValue != null)
      {
         return isSame(this.assignedValue, value, true);
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
         if (bounds.length == 1)
         {
            this.lowerBounds.add(bounds[0]);
         }
         else
         {
            this.lowerBounds.add(new ChoiceBound(bounds));
         }
      }
      else
      {
         this.lowerBounds.add(lowerBound);
      }
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
            if (!isAssignable(lowerBound, bound))
            {
               return false;
            }
         }
      }
      if (next == null)
      {
         Type[] bounds = variable.getBounds();
         for (int i = 0; i < bounds.length; i++)
         {
            if (isAssignable(bounds[i], lowerBound))
            {
               return true;
            }
         }
         return false;
      }
      return next.isInsideBounds(lowerBound, true);
   }
   
   // both type and bound belong to the same context
   private boolean isAssignable(Type type, Type fromType)
   {
      if (fromType instanceof TypeVariable)
      {
         TypeVariable fromVariable = (TypeVariable) fromType;
         Type[] fromBounds = fromVariable.getBounds();
         if (type instanceof TypeVariable)
         {
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
               fromBounds = fromVariable.getBounds();
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
            outer: for (int i = 0; i < bounds.length; i++)
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
            return true;
         }
         while (fromBounds.length == 1 && fromBounds[0] instanceof TypeVariable)
         {
            fromBounds = fromVariable.getBounds();
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
            for (Type lowerBound: fromWildcard.getUpperBounds())
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
               (ParameterizedType) type, fromType, CHECKER, this, false);
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
   
   private static ParamTypeAssignabilityAlgorithm.EqualityChecker<VariableNode> CHECKER =
      new ParamTypeAssignabilityAlgorithm.EqualityChecker<VariableNode>()
      {
         public boolean isSame(Type argument, Type fromArgument, VariableNode node, boolean boundLevel)
         {
            return node.isSame(fromArgument, argument, false);
         }
      };
}

class ChoiceBound implements Type
{
   public ChoiceBound(Type[] bounds)
   {
      this.bounds = new LinkedList<Type>();
      Collections.addAll(this.bounds, bounds);
   }
   
   Collection<Type> bounds;
}