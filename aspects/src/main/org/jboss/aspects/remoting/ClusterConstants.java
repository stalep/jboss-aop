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

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */

public interface ClusterConstants
{
   public static final String CLUSTERED_REMOTING = "CLUSTERED_REMOTING";
   public static final String LOADBALANCE_POLICY = "LOADBALANCE_POLICY";
   public static final String FAILOVER_COUNTER = "FAILOVER_COUNTER";
   public static final String CLUSTER_FAMILY_WRAPPER = "CLUSTER_FAMILY_WRAPPER";
   public static final String CLUSTER_FAMILY = "CLUSTER_FAMILY";
   public static final String CLUSTER_FAMILIES = "CLUSTER_FAMILIES";
   public static final String HA_TARGET = "HA_TARGET";
   public static final String CLUSTER_VIEW_ID = "CLUSTER_VIEW_ID";
}
