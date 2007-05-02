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

import org.jboss.aop.DispatcherConnectException;
import org.jboss.aop.util.PayloadKey;
import org.jboss.ha.framework.interfaces.GenericClusteringException;
import org.jboss.ha.framework.interfaces.LoadBalancePolicy;
import org.jboss.remoting.CannotConnectException;
import org.jboss.remoting.InvokerLocator;

import java.util.ArrayList;

/**
 * Pick an invocation target
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ClusterChooserInterceptor implements org.jboss.aop.advice.Interceptor, ClusterConstants, java.io.Serializable
{
   private static final long serialVersionUID = -8666382019058421135L;

   public static final ClusterChooserInterceptor singleton = new ClusterChooserInterceptor();

   public String getName()
   {
      return "ClusterChooserInterceptor";
   }

   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation)
   throws Throwable
   {
      LoadBalancePolicy lb = (LoadBalancePolicy) invocation.getMetaData(CLUSTERED_REMOTING, LOADBALANCE_POLICY);
      FamilyWrapper family = (FamilyWrapper) invocation.getMetaData(CLUSTERED_REMOTING, CLUSTER_FAMILY_WRAPPER);

      // we give the opportunity, to any server interceptor, to know if this a
      // first invocation to a node or if it is a failovered call
      //
      int failoverCounter = 0;
      String familyName = family.get().getFamilyName();
      invocation.getMetaData().addMetaData(CLUSTERED_REMOTING, CLUSTER_FAMILY, familyName, PayloadKey.AS_IS);
      InvokerLocator target = (InvokerLocator) lb.chooseTarget(family.get());
      Throwable lastException = null;
      while (target != null)
      {
         invocation.getMetaData().addMetaData(CLUSTERED_REMOTING, FAILOVER_COUNTER, new Integer(failoverCounter), PayloadKey.AS_IS);
         invocation.getMetaData().addMetaData(InvokeRemoteInterceptor.REMOTING, InvokeRemoteInterceptor.INVOKER_LOCATOR, target, PayloadKey.AS_IS);
         invocation.getMetaData().addMetaData(CLUSTERED_REMOTING, CLUSTER_VIEW_ID, new Long(family.get().getCurrentViewId()), PayloadKey.AS_IS);

         boolean definitivlyRemoveNodeOnFailure = true;
         lastException = null;
         try
         {



            Object rsp = invocation.invokeNext();
            ArrayList newReplicants = (ArrayList) invocation.getResponseAttachment("replicants");
            if (newReplicants != null)
            {
               long newViewId = ((Long) invocation.getResponseAttachment("viewId")).longValue();
               family.get().updateClusterInfo(newReplicants, newViewId);
            }
            return rsp;
         }
         catch(DispatcherConnectException dce)
         {
            //In case of graceful shutdown, the target object will no longer exist in the Dispatcher,
            //fail over to another server if we can...
            //For now remove the node "definitely"
            lastException = dce;
         }
         catch (CannotConnectException ex)
         {
            lastException = ex;
         }
         catch (GenericClusteringException gce)
         {
            lastException = gce;
            // this is a generic clustering exception that contain the
            // completion status: usefull to determine if we are authorized
            // to re-issue a query to another node
            //
            if (gce.getCompletionStatus() == GenericClusteringException.COMPLETED_NO)
            {
               // we don't want to remove the node from the list of failed
               // node UNLESS there is a risk to indefinitively loop
               //
               if (family.get().getTargets().size() >= failoverCounter)
               {
                  if (!gce.isDefinitive())
                     definitivlyRemoveNodeOnFailure = false;
               }
            }
            else
            {
               throw new RuntimeException("Clustering exception thrown", gce);
            }
         }
         catch (Throwable t)
         {
            // Just in case this get wrapped in a Throwable. This can happen when
            // the exception is generated inside the container and it is wrapped in
            // a ForwardId exception.
            if(t.getCause() instanceof GenericClusteringException)
            {
               GenericClusteringException gce = (GenericClusteringException)t.getCause();
               lastException = gce;
               // this is a generic clustering exception that contain the
               // completion status: usefull to determine if we are authorized
               // to re-issue a query to another node
               //
               if (gce.getCompletionStatus() == GenericClusteringException.COMPLETED_NO)
               {
                  // we don't want to remove the node from the list of failed
                  // node UNLESS there is a risk to indefinitively loop
                  //
                  if (family.get().getTargets().size() >= failoverCounter)
                  {
                     if (!gce.isDefinitive())
                        definitivlyRemoveNodeOnFailure = false;
                  }
               }
               else
               {
                  throw new RuntimeException("Clustering exception thrown", gce);
               }
            } else
            {
               throw t;
            }
         }

         // If we reach here, this means that we must fail-over
         family.get().removeDeadTarget(target);
         if (!definitivlyRemoveNodeOnFailure)
         {
            family.get().resetView();
         }

         target = (InvokerLocator) lb.chooseTarget(family.get());
         if (target == null)
         {
            if (lastException != null)
            {
               throw new RuntimeException("cluster invocation failed, last exception was: ", lastException);
            }
            else
            {
               throw new RuntimeException("cluster invocation failed");
            }
         }
         failoverCounter++;
      }
      // if we get here this means list was exhausted
      throw new RuntimeException("Unreachable?: Service unavailable.");
   }
}
