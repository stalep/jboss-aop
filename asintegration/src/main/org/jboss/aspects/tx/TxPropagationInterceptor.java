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

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.tm.TransactionManagerLocator;
import org.jboss.tm.TransactionPropagationContextUtil;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class TxPropagationInterceptor implements Interceptor
{
   private TransactionManager tm;

   public TxPropagationInterceptor(TransactionManager tm)
   {
      this.tm = tm;
   }

   public TxPropagationInterceptor()
   {
      tm = TransactionManagerLocator.getInstance().locate();
   }

   public String getName()
   {
      return "TxPropagationInterceptor";
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      Object tpc = invocation.getMetaData(ClientTxPropagationInterceptor.TRANSACTION_PROPAGATION_CONTEXT, ClientTxPropagationInterceptor.TRANSACTION_PROPAGATION_CONTEXT);
      if (tpc != null)
      {
         Transaction tx = tm.getTransaction();
         if (tx != null) throw new RuntimeException("cannot import a transaction context when a transaction is already associated with the thread");
         Transaction importedTx = TransactionPropagationContextUtil.getTPCImporter().importTransactionPropagationContext(tpc);
         tm.resume(importedTx);
         try
         {
            return invocation.invokeNext();
         }
         finally
         {
            tm.suspend();
         }
      }
      else
      {
         return invocation.invokeNext();
      }
   }
}
