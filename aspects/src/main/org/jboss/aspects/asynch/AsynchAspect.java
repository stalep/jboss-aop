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
package org.jboss.aspects.asynch;

import java.io.Serializable;

import org.jboss.aop.Advisor;
import org.jboss.aop.Dispatcher;
import org.jboss.aop.InstanceAdvised;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.proxy.ProxyFactory;
import org.jboss.aspects.NullOrZero;
import org.jboss.aspects.remoting.InvokeRemoteInterceptor;
import org.jboss.aspects.remoting.Remoting;
import org.jboss.remoting.InvokerLocator;
import org.jboss.util.id.GUID;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AsynchAspect
{
   public static final String FUTURE = "ASYNCH_FUTURE";

   private static GUID futureClassGUID = new GUID();
   private static Class[] futureIntf = {Future.class};
   private static Class[] futureDynamicIntf = {Future.class, Serializable.class, InstanceAdvised.class};
   private Advisor advisor;
   protected ExecutorAbstraction executor;
   private boolean generateFutureProxy = true;

   public Advisor getAdvisor()
   {
      return advisor;
   }

   public void setAdvisor(Advisor advisor)
   {
      this.advisor = advisor;
      Class executorClass = null;
      AsynchExecutor executorAnnotation = (AsynchExecutor) advisor.resolveAnnotation(AsynchExecutor.class);
      if (executorAnnotation == null)
      {
         executorClass = ThreadPoolExecutor.class;
      }
      else
      {
         executorClass = executorAnnotation.value();
      }

      try
      {
         executor = (ExecutorAbstraction) executorClass.newInstance();
      }
      catch (InstantiationException e)
      {
         throw new RuntimeException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new RuntimeException(e);
      }
      executor.setAdvisor(advisor);

   }

   public void setGenerateDynamicProxy(boolean gen)
   {
      this.generateFutureProxy = gen;
   }
   
   public Object execute(MethodInvocation invocation) throws Throwable
   {
      RemotableFuture future = executor.execute(invocation);

      InvokerLocator locator = (InvokerLocator) invocation.getMetaData(InvokeRemoteInterceptor.REMOTING, InvokeRemoteInterceptor.INVOKER_LOCATOR);

      // this is a remote invocation so just stuff the future within the response
      if (locator != null)
      {
         setupRemoteFuture(invocation, future, locator);
      }
      else
      {
         setupLocalFuture(invocation, future);
      }

      return NullOrZero.nullOrZero(invocation.getMethod().getReturnType());
   }
   
   protected void setupRemoteFuture(MethodInvocation invocation, RemotableFuture future, InvokerLocator locator)throws Exception
   {
      GUID futureGUID = new GUID();
      Dispatcher.singleton.registerTarget(futureGUID, future);
      InstanceAdvised ia = (generateProxy()) ? 
            ProxyFactory.createInterfaceProxy(futureClassGUID, Future.class.getClassLoader(), futureIntf) :
               (InstanceAdvised)FutureInvocationHandler.createFutureProxy(futureClassGUID, Future.class.getClassLoader(), futureDynamicIntf);
      Remoting.makeRemotable(ia, locator, futureGUID);
      future.setRemoteObjectID(futureGUID);
      invocation.addResponseAttachment(FUTURE, ia);
   }
   
   /**
    * Default behaviour is to generate a proxy using aop.
    * This can be overridden, e.g. by the EJB3 AsynchronousInterceptor to avoid dependencies on javassist for EJB3 clients  
    */
   protected boolean generateProxy()
   {
      return generateFutureProxy;
   }
   
   protected void setupLocalFuture(MethodInvocation invocation, Future future)
   {
      FutureHolder provider = (FutureHolder) invocation.getTargetObject();
      provider.setFuture(future);
      invocation.addResponseAttachment(FUTURE, future);
   }

}
