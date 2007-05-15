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
/**
 * Checks to see if this object is local in VM
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class IsLocalInterceptor implements org.jboss.aop.advice.Interceptor, java.io.Serializable
{
   private static final long serialVersionUID = 8067347185395345001L;
   
   public static final IsLocalInterceptor singleton = new IsLocalInterceptor();

   public String getName() { return "IsLocalInterceptor"; }

   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      Object oid = invocation.getMetaData(Dispatcher.DISPATCHER, Dispatcher.OID);
      if (Dispatcher.singleton.isRegistered(oid))
      {
         org.jboss.aop.joinpoint.InvocationResponse response = Dispatcher.singleton.invoke(invocation);
         invocation.setResponseContextInfo(response.getContextInfo());
         return response.getResponse();
      }
      return invocation.invokeNext();
   }
}
