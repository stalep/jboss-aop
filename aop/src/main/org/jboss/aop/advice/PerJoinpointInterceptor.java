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

import java.lang.reflect.Modifier;

import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.ClassAdvisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.joinpoint.CallerInvocation;
import org.jboss.aop.joinpoint.ConstructorCalledByMethodJoinpoint;
import org.jboss.aop.joinpoint.ConstructorJoinpoint;
import org.jboss.aop.joinpoint.FieldJoinpoint;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.aop.joinpoint.MethodCalledByMethodJoinpoint;
import org.jboss.aop.joinpoint.MethodJoinpoint;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class PerJoinpointInterceptor implements Interceptor
{
   public static Interceptor createInterceptor(Advisor advisor, Joinpoint joinpoint, AspectDefinition def) throws Exception
   {
      if (joinpoint instanceof MethodJoinpoint)
      {
         MethodJoinpoint method = (MethodJoinpoint) joinpoint;
         if (Modifier.isStatic(method.getMethod().getModifiers()))
         {
            return (Interceptor) def.getFactory().createPerJoinpoint(advisor, joinpoint);
         }
      }
      else if (joinpoint instanceof ConstructorJoinpoint)
      {
         return (Interceptor) def.getFactory().createPerJoinpoint(advisor, joinpoint);
      }
      else if (joinpoint instanceof MethodCalledByMethodJoinpoint)
      {
         MethodCalledByMethodJoinpoint method = (MethodCalledByMethodJoinpoint) joinpoint;
         if (Modifier.isStatic(method.getCalling().getModifiers()))
         {
            return (Interceptor) def.getFactory().createPerJoinpoint(advisor, joinpoint);
         }
      }
      else if (joinpoint instanceof ConstructorCalledByMethodJoinpoint)
      {
         ConstructorCalledByMethodJoinpoint method = (ConstructorCalledByMethodJoinpoint) joinpoint;
         if (Modifier.isStatic(method.getCalling().getModifiers()))
         {
            return (Interceptor) def.getFactory().createPerJoinpoint(advisor, joinpoint);
         }
      }
      else if (joinpoint instanceof FieldJoinpoint)
      {
         FieldJoinpoint field = (FieldJoinpoint) joinpoint;
         if (Modifier.isStatic(field.getField().getModifiers()))
         {
            ClassAdvisor classAdvisor = (ClassAdvisor) advisor;
            return (Interceptor)classAdvisor.getFieldAspect(field, def);
         }

      }
      return new PerJoinpointInterceptor(def, advisor, joinpoint);
   }

   AspectDefinition aspectDefinition;
   Joinpoint joinpoint;

   public PerJoinpointInterceptor(AspectDefinition a, Advisor advisor, Joinpoint joinpoint)
   {
      aspectDefinition = a;
      advisor.addPerInstanceJoinpointAspect(joinpoint, a);
      this.joinpoint = joinpoint;
   }

   public String getName()
   {
      return aspectDefinition.getName();
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      if (invocation instanceof CallerInvocation)
      {
         //TODO: Naive implementation. Ideally callers should be able to look up the aspect by target instance
         //to make sure that there is only one instance per target rather than caller
         Object callingObject = ((CallerInvocation) invocation).getCallingObject();

         if (callingObject == null) return invocation.invokeNext(); // called from static method
         
         Advised advised = (Advised) callingObject;
         InstanceAdvisor advisor = advised._getInstanceAdvisor();
         Interceptor interceptor = (Interceptor) advisor.getPerInstanceJoinpointAspect(joinpoint, aspectDefinition);
         return interceptor.invoke(invocation);         
         
      }
      else
      {
         Object targetObject = invocation.getTargetObject();
         if (targetObject == null) return invocation.invokeNext(); // static method call or static field call

         Advised advised = (Advised) targetObject;
         InstanceAdvisor advisor = advised._getInstanceAdvisor();
         Interceptor interceptor = (Interceptor) advisor.getPerInstanceJoinpointAspect(joinpoint, aspectDefinition);
         return interceptor.invoke(invocation);         
      }
   }
}