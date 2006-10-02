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
package org.jboss.aop.joinpoint;

import org.jboss.aop.Advisor;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.metadata.MetaDataResolver;
import org.jboss.aop.metadata.SimpleMetaData;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class MethodCalledByMethodInvocationWrapper extends MethodCalledByMethodInvocation
{
   private static final long serialVersionUID = 5130026569290691583L;

   MethodCalledByMethodInvocation wrapped;

   public MethodCalledByMethodInvocationWrapper(MethodCalledByMethodInvocation wrapped, Interceptor[] interceptors)
   {
      super(interceptors);
      this.wrapped = wrapped;
   }

   public Object getMetaData(Object group, Object attr)
   {
      return wrapped.getMetaData(group, attr);
   }

   public Object invokeNext() throws Throwable
   {
      if (interceptors != null && currentInterceptor < interceptors.length)
      {
         try
         {
            return interceptors[currentInterceptor++].invoke(this);
         }
         finally
         {
            // so that interceptors like clustering can reinvoke down the chain
            currentInterceptor--;
         }
      }
      try
      {
         return wrapped.invokeNext();
      }
      finally
      {
         responseContextInfo = wrapped.getResponseContextInfo();
      }
   }

   public MetaDataResolver getInstanceResolver()
   {
      return wrapped.getInstanceResolver();
   }

   public Invocation copy()
   {
      MethodCalledByMethodInvocationWrapper invocation = new MethodCalledByMethodInvocationWrapper((MethodCalledByMethodInvocation) wrapped.copy(), interceptors);
      invocation.currentInterceptor = this.currentInterceptor;
      return invocation;
   }

   public Object[] getArguments()
   {
      return wrapped.getArguments();
   }

   public void setArguments(Object[] arguments)
   {
      wrapped.setArguments(arguments);
   }

   public Class getCallingClass()
   {
      return wrapped.getCallingClass();
   }

   public Method getCallingMethod()
   {
      return wrapped.getCallingMethod();
   }

   public Map getResponseContextInfo()
   {
      return wrapped.getResponseContextInfo();
   }

   public void setResponseContextInfo(Map responseContextInfo)
   {
      wrapped.setResponseContextInfo(responseContextInfo);
   }

   public void addResponseAttachment(Object key, Object val)
   {
      wrapped.addResponseAttachment(key, val);
   }

   public Object getResponseAttachment(Object key)
   {
      return wrapped.getResponseAttachment(key);
   }

   public void setMetaData(SimpleMetaData data)
   {
      wrapped.setMetaData(data);
   }

   public Advisor getAdvisor()
   {
      return wrapped.getAdvisor();
   }

   public Object getTargetObject()
   {
      return wrapped.getTargetObject();
   }

   public void setTargetObject(Object targetObject)
   {
      wrapped.setTargetObject(targetObject);
   }

   /**
    * @return the method that is being called
    */
   public Method getCalledMethod()
   {
      return wrapped.getCalledMethod();
   }

   public Object getCallingObject()
   {
      return wrapped.getCallingObject();
   }

}
