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
package org.jboss.aspects.remoting;

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.InvocationBase;
import org.jboss.aop.metadata.MetaDataResolver;
import org.jboss.aop.metadata.SimpleMetaData;
/**
 * Prepare Metadata to be marshalled across the wire.
 * This interceptor iterates through an Invocation's
 * metadata resolver list to pull out transportable
 * metadata and stuff it into the invocation
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class MergeMetaDataInterceptor implements org.jboss.aop.advice.Interceptor, java.io.Serializable
{
   private static final long serialVersionUID = 2424149906770308705L;

   public static final MergeMetaDataInterceptor singleton = new MergeMetaDataInterceptor();

   public String getName() { return "MergeMetaDataInterceptor"; }

   protected org.jboss.aop.metadata.SimpleMetaData merge(Invocation invocation, MetaDataResolver resolver, SimpleMetaData merged)
   {
      if (resolver == null) return merged;
      org.jboss.aop.metadata.SimpleMetaData metadata =  resolver.getAllMetaData(invocation);
      if (metadata == null) return merged;
      if (merged == null)
      {
         merged = new org.jboss.aop.metadata.SimpleMetaData();
      }
      merged.mergeIn(metadata);
      return merged;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      org.jboss.aop.metadata.SimpleMetaData merged = null;
      merged = merge(invocation, ((InvocationBase)invocation).getInstanceResolver(), merged);

      org.jboss.aop.metadata.MetaDataResolver threadMetaData = org.jboss.aop.metadata.ThreadMetaData.instance().getAllMetaData(invocation);
      merged = merge(invocation, threadMetaData, merged);

      if (merged != null)
      {
         // Invocation data should override all merged data
         merged.mergeIn(invocation.getMetaData());

         // replace invocation's metadata with merged
         invocation.setMetaData(merged);
      }
      return invocation.invokeNext();
   }
}
