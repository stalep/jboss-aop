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

import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.aop.metadata.SimpleMetaData;

/**
 * Holds an object instance's metadata and attached interceptors
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public interface InstanceAdvisor
{
   public SimpleMetaData getMetaData();

   public boolean hasInterceptors(); 
   
   public Interceptor[] getInterceptors();

   public Interceptor[] getInterceptors(Interceptor[] baseChain);

   public boolean hasAspects();

   public void insertInterceptor(Interceptor interceptor);

   public void removeInterceptor(String name);

   public void appendInterceptor(Interceptor interceptor);

   public void insertInterceptorStack(String stackName);

   public void removeInterceptorStack(String name);

   public void appendInterceptorStack(String stackName);

   /**
    * Get the instance of an aspect.  An aspect encapsulates
    * a set of advices.
    *
    * @param aspectName
    * @return
    */
   public Object getPerInstanceAspect(String aspectName);

   void insertInterceptor(int index, Interceptor interceptor);

   void appendInterceptor(int index, Interceptor interceptor);
   
   public Object getPerInstanceAspect(AspectDefinition def);

   public Object getPerInstanceJoinpointAspect(Joinpoint joinpoint, AspectDefinition def);
   
   public Domain getDomain();
}
