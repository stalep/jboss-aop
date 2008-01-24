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

import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.joinpoint.CallerInvocation;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.proxy.container.ClassProxyContainer;
import org.jboss.aop.proxy.container.ContainerProxyMethodInvocation;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class PerInstanceInterceptor implements Interceptor
{
   AspectDefinition aspectDefinition;

   public PerInstanceInterceptor(AspectDefinition a, Advisor advisor)
   {
      aspectDefinition = a;
      advisor.addPerInstanceAspect(a);
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
         Interceptor interceptor = (Interceptor) advisor.getPerInstanceAspect(aspectDefinition);
         return interceptor.invoke(invocation);         
         
      }
      else
      {
         Object targetObject = invocation.getTargetObject();
         if (targetObject == null) return invocation.invokeNext(); // static method call or static field call

         InstanceAdvisor instanceAdvisor = null;
         if (targetObject instanceof Advised)
         {
            Advised advised = (Advised) targetObject;
            instanceAdvisor = advised._getInstanceAdvisor();
         }
         else
         {
            Advisor advisor = invocation.getAdvisor();
            if (advisor == null)
            {
               return invocation.invokeNext();
            }
            
            if (advisor instanceof InstanceAdvisor)
            {
               instanceAdvisor = (InstanceAdvisor) advisor;
            }
            else 
            {
               if (advisor instanceof ClassProxyContainer && invocation instanceof ContainerProxyMethodInvocation)
               {
                  ContainerProxyMethodInvocation pi = (ContainerProxyMethodInvocation)invocation;
                  instanceAdvisor = pi.getProxy().getInstanceAdvisor();
               }
               else
               {
                  return invocation.invokeNext();
               }
            }
         }
         Interceptor interceptor = getAspectInstance(instanceAdvisor);
         return interceptor.invoke(invocation);
      }
   }
   
   public Interceptor getAspectInstance(InstanceAdvisor ia)
   {
      return (Interceptor) ia.getPerInstanceAspect(aspectDefinition);
   }

}
