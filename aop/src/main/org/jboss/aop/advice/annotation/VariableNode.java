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
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 *
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
         return isSame(this.assignedValue, value);
      }
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
      if (this.assignedValue != null && !isAssignable(assignedValue, lowerBound))
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
   private boolean isAssignable(Type bound, Type type)
   {
      if (type instanceof TypeVariable)
      {
         if (bound instanceof TypeVariable)
         {
            if (type == bound)
            {
               return true;
            }
            TypeVariable typeVariable = (TypeVariable) type;
            Type[] bounds = typeVariable.getBounds();
            while (bounds.length == 1 && bounds[0] instanceof TypeVariable)
            {
               if (bounds[0] == bound)
               {
                  return true;
               }
               type = (TypeVariable) bounds[0];
               bounds = typeVariable.getBounds();
            }
            TypeVariable variableBound = (TypeVariable) bound;
            Type[] variableBoundBounds = typeVariable.getBounds();
            while (variableBoundBounds.length == 1 && variableBoundBounds[0] instanceof TypeVariable)
            {
               if (variableBoundBounds[0] == type)
               {
                  return false;
               }
               variableBound = (TypeVariable) variableBoundBounds[0];
               variableBoundBounds = variableBound.getBounds();
            }
            outer: for (int i = 0; i < variableBoundBounds.length; i++)
            {
               for (int j = 0; j < bounds.length; j++)
               {
                  if (isAssignable(variableBoundBounds[i], bounds[j]))
                  {
                     continue outer;
                  }
               }
               return false;
            }
            return true;
         }
         for (Type variableBound: ((TypeVariable) type).getBounds())
         {
            if (isAssignable(bound, variableBound))
            {
               return true;
            }
         }
         return false;
      }
      if (bound instanceof Class)
      {
         if (bound == Object.class)
         {
            return true;
         }
         Class<?> boundClass = (Class<?>) bound;
         if (type instanceof Class)
         {
            return boundClass.isAssignableFrom((Class<?>) type);
         }
         if (type instanceof ParameterizedType)
         {
            return boundClass.isAssignableFrom((Class<?>) ((ParameterizedType) type).getRawType());
         }
         return boundClass.isArray() && isAssignable(boundClass.getComponentType(), ((GenericArrayType) type).
                     getGenericComponentType());
      }
      if (bound instanceof ParameterizedType)
      {
         return ContextualizedArguments.isAssignable(CHECKER, (ParameterizedType) bound, type, this);
      }
      if (bound instanceof TypeVariable)
      {
         for (Type typeBound: ((TypeVariable) bound).getBounds())
         {
            if (!isAssignable(typeBound, type))
            {
               return false;
            }
         }
         return true;
      }
      ChoiceBound choiceBound = (ChoiceBound) bound;
      for (Iterator<Type> it = choiceBound.bounds.iterator(); it.hasNext();)
      {
         Type option = it.next();
         if (!isAssignable(option, type))
         {
            it.remove();
         }
      }
      return !choiceBound.bounds.isEmpty();
   }
   
   protected boolean isSame(Type assignableArgument, Type argument)
   {
      if (assignableArgument instanceof WildcardType)
      {
         WildcardType assignableWildcardArg = (WildcardType) assignableArgument;
         Type[] upperAssignBounds = assignableWildcardArg.getUpperBounds();
         Type[] lowerAssignBounds = assignableWildcardArg.getUpperBounds();
         if (argument instanceof WildcardType)
         {
            WildcardType wildcardArg = (WildcardType) argument;
            Type[] upperBounds = wildcardArg.getUpperBounds();
            if (!isAssignable(upperAssignBounds, upperBounds))
            {
               return false;
            }
            
            Type[] lowerBounds = wildcardArg.getLowerBounds();
            outer: for (int i = 0; i < lowerBounds.length; i++)
            {
               for (int j = 0; j < lowerAssignBounds.length; j++)
               {
                  if (isAssignable(lowerBounds[i], lowerAssignBounds[j]))
                  {
                     continue outer;
                  }
               }
               return false;
            }
            return true;
         }
         else if(argument instanceof TypeVariable)
         {
            if (!isAssignable(upperAssignBounds, ((TypeVariable) argument).getBounds()))
            {
               return false;
            }
         }
         else
         {
            for (int i = 0; i < upperAssignBounds.length; i++)
            {
               if (!isAssignable(upperAssignBounds[i], argument))
               {
                  return false;
               }
            }
            return true;
         }
      }
      else if (assignableArgument instanceof GenericArrayType)
      {
         if (argument instanceof GenericArrayType)
         {
            return isSame(((GenericArrayType) assignableArgument).getGenericComponentType(),
                  ((GenericArrayType) argument).getGenericComponentType());
         }
         else
         {
            return false;
         }
      }
      return assignableArgument.equals(argument);
   }

   /**
    * @param node
    * @param upperBounds
    * @param upperAssignBounds
    */
   private boolean isAssignable(Type[] upperAssignBounds, Type[] upperBounds)
   {
      outer: for (int i = 0; i < upperAssignBounds.length; i++)
      {
         for (int j = 0; j < upperBounds.length; j++)
         {
            if (isAssignable(upperAssignBounds[i], upperBounds[j]))
            {
               continue outer;
            }
         }
         return false;
      }
      return true;
   }
   
   private static ContextualizedArguments.EqualityChecker<VariableNode> CHECKER =
      new ContextualizedArguments.EqualityChecker<VariableNode>()
      {
         public boolean isSame(Type assignableArgument, Type argument, VariableNode node)
         {
            return node.isSame(assignableArgument, argument);
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