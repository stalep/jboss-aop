/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.util;

import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.PointcutStats;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class BindingClassifier
{
   public static boolean isExecution(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isExecution();
      }
      return true;
   }

   public static boolean isMethodExecution(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isMethodExecution();
      }
      return true;
   }

   public static boolean isConstructorExecution(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isConstructorExecution();
      }
      return true;
   }

   public static boolean isConstruction(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isConstruction();
      }
      return true;
   }

   public static boolean isCall(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isCall();
      }
      return true;
   }
   
   public static boolean isMethodCall(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isMethodCall();
      }
      return true;
   }

   public static boolean isConstructorCall(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isConstructorCall();
      }
      return true;
   }

   public static boolean isWithin(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isWithin();
      }
      return true;
   }

   public static boolean isGet(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isGet();
      }
      return true;
   }

   public static boolean isSet(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isSet();
      }
      return true;
   }

   public static boolean isWithincode(AdviceBinding binding)
   {
      PointcutStats stats = getPointcutStats(binding);
      if (stats != null)
      {
         return stats.isWithincode();
      }
      return true;
   }

   private static PointcutStats getPointcutStats(AdviceBinding binding)
   {
      Pointcut pointcut = binding.getPointcut();
      if (pointcut instanceof PointcutExpression)
      {
         PointcutExpression expr =  (PointcutExpression)pointcut;
         return expr.getStats();
      }
      return null;
   }
}