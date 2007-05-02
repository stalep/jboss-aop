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
package org.jboss.aspects.tx;

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.tm.TransactionPropagationContextFactory;
import org.jboss.tm.TransactionPropagationContextUtil;

import java.io.ObjectStreamException;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ClientTxPropagationInterceptor implements Interceptor, java.io.Serializable
{
   private static final long serialVersionUID = 4536126296306191076L;

   public static String TRANSACTION_PROPAGATION_CONTEXT = "TransactionPropagationContext";

   public static final ClientTxPropagationInterceptor singleton = new ClientTxPropagationInterceptor();

   public String getName()
   {
      return "ClientTxPropagationInterceptor";
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      TransactionPropagationContextFactory tpcFactory = TransactionPropagationContextUtil.getTPCFactoryClientSide();
      if (tpcFactory != null)
      {
         Object tpc = tpcFactory.getTransactionPropagationContext();
         if (tpc != null)
         {
            invocation.getMetaData().addMetaData(TRANSACTION_PROPAGATION_CONTEXT, TRANSACTION_PROPAGATION_CONTEXT, tpc);
         }
      }
      return invocation.invokeNext();
   }

   Object readResolve() throws ObjectStreamException {
      return singleton;
   }
}
