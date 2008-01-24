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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
 *
 **/
public class PerInstanceAdvice extends AbstractAdvice
{
   private boolean initialized = false;
   AspectDefinition aspectDefinition;

   public PerInstanceAdvice(String adviceName, AspectDefinition a, Advisor advisor)
   {
      aspectDefinition = a;
      this.adviceName = adviceName;
      advisor.addPerInstanceAspect(a);
   }

   public String getName()
   {
      return aspectDefinition.getName() + "." + adviceName;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      Object aspect = null;
      if (invocation instanceof CallerInvocation)
      {
         //TODO: Naive implementation. Ideally callers should be able to look up the aspect by target instance
         //to make sure that there is only one instance per target rather than caller
         Object callingObject = ((CallerInvocation) invocation).getCallingObject();

         if (callingObject == null) return invocation.invokeNext(); // called from static method
         
         Advised advised = (Advised) callingObject;
         InstanceAdvisor advisor = advised._getInstanceAdvisor();
         aspect = advisor.getPerInstanceAspect(aspectDefinition);
      }
      else
      {    
	      Object targetObject = invocation.getTargetObject();
	      if (targetObject == null) return invocation.invokeNext(); // static method call or static field call
	
          InstanceAdvisor instanceAdvisor = null;
          if (targetObject instanceof Advised)
          {
             Advised advised = (Advised)targetObject;
             instanceAdvisor = advised._getInstanceAdvisor();
          }
          else
          {
             Advisor advisor = invocation.getAdvisor();
             if (advisor == null)
             {
                return invocation.invokeNext();
             }
             else if (advisor instanceof InstanceAdvisor)
             {
                instanceAdvisor = (InstanceAdvisor) advisor;
             }
             else if (advisor instanceof ClassProxyContainer && invocation instanceof ContainerProxyMethodInvocation)
             {
                ContainerProxyMethodInvocation pi = (ContainerProxyMethodInvocation)invocation;
                instanceAdvisor = pi.getProxy().getInstanceAdvisor();
             }
             else
             {
                return invocation.invokeNext();
             }
          }
          aspect = instanceAdvisor.getPerInstanceAspect(aspectDefinition);
      }
      
      if (!initialized)
      {
         init(adviceName, aspect.getClass());
         initialized = true;
      }
      Method advice = resolveAdvice(invocation);
      Object[] args = {invocation};

      try
      {
         return advice.invoke(aspect, args);
      }
      catch (InvocationTargetException e)
      {
         throw e.getCause();  //To change body of catch statement use Options | File Templates.
      }
   }

   @Override
   public Object getAspectInstance()
   {
      return null;
   }

}
