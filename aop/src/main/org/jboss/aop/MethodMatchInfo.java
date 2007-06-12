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
package org.jboss.aop;

import java.util.ArrayList;

import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.joinpoint.MethodJoinpoint;
import org.jboss.aop.pointcut.PointcutMethodMatch;
import org.jboss.aop.util.logging.AOPLogger;
import org.jboss.logging.Logger;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
class MethodMatchInfo
{
   private static final Logger logger = AOPLogger.getLogger(MethodMatchInfo.class);
   
   Advisor advisor;
   MethodInfo info;
   ArrayList bindings;
   ArrayList pointcutMethodMatches;
   
   MethodMatchInfo(Advisor advisor, MethodInfo info)
   {
      this.advisor = advisor;
      this.info = info;
   }
   
   public void addMatchedBinding(AdviceBinding binding, PointcutMethodMatch pointcutMethodMatch)
   {
      if (bindings == null)
      {
         bindings = new ArrayList();
      }
      if (pointcutMethodMatches == null)
      {
         pointcutMethodMatches = new ArrayList();
      }
      bindings.add(binding);
      pointcutMethodMatches.add(pointcutMethodMatch);
   }

   public MethodInfo getInfo()
   {
      return info;
   }

   public void setInfo(MethodInfo info)
   {
      this.info = info;
   }
   
   public ArrayList populateBindings()
   {
      if (bindings != null)
      {
         ArrayList applicableBindings = new ArrayList();
         if (advisor.chainOverridingForInheritedMethods())
         {
            overridePopulateBindings(applicableBindings);
         }
         else
         {
            simplePopulateBindings(applicableBindings);
         }
         
         if (applicableBindings.size() > 0)
         {
            return applicableBindings;
         }
      }
      return null;
   }
   
   private void simplePopulateBindings(ArrayList applicableBindings)
   {
      int size = bindings.size();
      for (int i = 0 ; i < size ; i++)
      {
         AdviceBinding binding = (AdviceBinding)bindings.get(i);
         applyBinding(applicableBindings, binding);
      }
   }
   
   private void overridePopulateBindings(ArrayList applicableBindings)
   {
      if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug("populate bindings for " + info.getMethod() + " all bindings");
      int size = bindings.size();
      int minMatchLevel = 1000000;
      for (int i = 0 ; i < size ; i++)
      {
         AdviceBinding binding = (AdviceBinding)bindings.get(i);
         PointcutMethodMatch match = (PointcutMethodMatch)pointcutMethodMatches.get(i);
         if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug(match.getMatchLevel() + " " + match.getMatchedClass().getName() + " " + binding.getPointcut().getExpr() + " : " + binding.getInterceptorFactories().length);
         
         if (minMatchLevel > match.getMatchLevel() && !match.isInstanceOf())
         {
            minMatchLevel = match.getMatchLevel();
         }
      }

      if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug("populate bindings for " + info.getMethod() + " actual bindings");
      for (int i = 0 ; i < size ; i++)
      {
         AdviceBinding binding = (AdviceBinding)bindings.get(i);
         PointcutMethodMatch match = (PointcutMethodMatch)pointcutMethodMatches.get(i);
         
         if (match.isInstanceOf() || match.getMatchLevel() == minMatchLevel)
         {
            if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug(match.getMatchLevel() + " " + match.getMatchedClass().getName() + " " + binding.getPointcut().getExpr() + " : " + binding.getInterceptorFactories().length);
            applyBinding(applicableBindings, binding);
         }
      }
   }
   
   private void applyBinding(ArrayList applicableBindings, AdviceBinding binding)
   {
      applicableBindings.add(binding);
      binding.addAdvisor(advisor);
      advisor.pointcutResolved(info, binding, new MethodJoinpoint(info.getMethod()));
   }
   
   public void clear()
   {
      bindings = null;
      pointcutMethodMatches = null;
      info.clear();
   }
}
