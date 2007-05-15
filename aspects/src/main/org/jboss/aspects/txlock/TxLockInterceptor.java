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
package org.jboss.aspects.txlock;

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.logging.Logger;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

/**
 *  This interceptor handles chooses an object to invoke
 *  on based on the transaction
 *
 *  @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 *  @version $Revision$
 */
public class TxLockInterceptor implements org.jboss.aop.advice.Interceptor
{
   /**
    * Logging instance
    */
   protected Logger log = Logger.getLogger(this.getClass());

   private final TransactionManager tm;
   private final QueuedTxLock lock;

   public TxLockInterceptor(TransactionManager tm, QueuedTxLock lock)
   {
      this.tm = tm;
      this.lock = lock;
   }

   public String getName()
   {
      return "TxLockInterceptor";
   }

   /**
    *
    */
   public Object invoke(Invocation invocation) throws Throwable
   {
      Transaction tx = tm.getTransaction();
      if (tx == null) return invocation.invokeNext();

      else
      {
      }


      lock.schedule(tx, invocation);
      try
      {
         return invocation.invokeNext();
      }
      finally
      {
         lock.endInvocation(tx);
      }
   }


}
