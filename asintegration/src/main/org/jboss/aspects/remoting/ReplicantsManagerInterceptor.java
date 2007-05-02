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

import org.jboss.ha.framework.server.HATarget;

import java.util.ArrayList;
import java.util.Map;

/**
 * Checks to see if this object is local in VM
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ReplicantsManagerInterceptor implements org.jboss.aop.advice.Interceptor, ClusterConstants
{
   private final Map families;

   public ReplicantsManagerInterceptor(Map families)
   {
      this.families = families;
   }

   public String getName()
   {
      return this.getClass().getName();
   }

   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      Object response = invocation.invokeNext();
      String clientFamily = (String) invocation.getMetaData(CLUSTERED_REMOTING, CLUSTER_FAMILY);

      HATarget target = (HATarget) families.get(clientFamily);
      if (target == null)
      {
         return response;
      }

      Long clientViewId = (Long) invocation.getMetaData(CLUSTERED_REMOTING, CLUSTER_VIEW_ID);
      if (clientViewId == null)
      {
         // Maybe we're being invoked upon by a non-clustered proxy
         return response;
      }
      if (clientViewId.longValue() != target.getCurrentViewId())
      {
         invocation.addResponseAttachment("replicants", new ArrayList(target.getReplicants()));
         invocation.addResponseAttachment("viewId", new Long(target.getCurrentViewId()));
      }
      return response;
   }
}
