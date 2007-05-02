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

import org.jboss.aop.Dispatcher;
import org.jboss.remoting.InvocationRequest;
import org.jboss.remoting.callback.InvokerCallbackHandler;
import org.jboss.remoting.ServerInvocationHandler;
import org.jboss.remoting.ServerInvoker;

import javax.management.MBeanServer;

/**
 * AOPRemotingInvocationHandler is a ServerInvocationHandler that will forward requests to the
 * aop Dispatcher
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AOPRemotingInvocationHandler implements ServerInvocationHandler
{
   public AOPRemotingInvocationHandler()
   {
      super();
   }

   /**
    * set the invoker that owns this handler
    *
    * @param invoker
    */
   public void setInvoker(ServerInvoker invoker)
   {
   }

   /**
    * set the mbean server that the handler can reference
    *
    * @param server
    */
   public void setMBeanServer( MBeanServer server )
   {
   }

   /**
    * method is called to destroy the handler and remove all pending notifications and listeners
    * from the notification cache
    */
   public synchronized void destroy()
   {
   }

   protected void finalize() throws Throwable
   {
      destroy();
      super.finalize();
   }

   public Object invoke(InvocationRequest invocation)
      throws Throwable
   {
      org.jboss.aop.joinpoint.Invocation inv =(org.jboss.aop.joinpoint.Invocation)invocation.getParameter();
      return Dispatcher.singleton.invoke(inv);
   }

    /**
     * Adds a callback handler that will listen for callbacks from
     * the server invoker handler.
     * @param callbackHandler
     */
    public void addListener(InvokerCallbackHandler callbackHandler)
    {
        //TODO: implement for callback api -TME
    }

    /**
     * Removes the callback handler that was listening for callbacks
     * from the server invoker handler.
     * @param callbackHandler
     */
    public void removeListener(InvokerCallbackHandler callbackHandler)
    {
        //TODO: implement for callback api -TME
    }
}
