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

import org.jboss.aop.joinpoint.FieldReadInvocation;
import org.jboss.aop.joinpoint.FieldWriteInvocation;
import org.jboss.tm.TransactionLocal;

/**
 * This aspect should be scoped PER_JOINPOINT
 * It allows a field to have the value of itself pertain
 * to the current transaction.
 *
 * The fields transactional and non-transactional value is always separate
 * At tx commit, the transactional value the field had is wiped away.
 * At tx commit, the transactional value does not correspond to the non-transactional value
 *
 * If you are within a transaction and you get the value of the field before setting it
 * then the initial transational value will be set to the current non-transactional value
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class TransactionLocalAspect
{
   private TransactionLocal txLocal = new TransactionLocal();

   public Object access(FieldReadInvocation invocation) throws Throwable
   {
      // Return non-Tx value
      if (txLocal.getTransaction() == null) return invocation.invokeNext();

      // just in case we have a primitive, we can't return null so set txLocal to be the current nonTx value
      if (txLocal.get() == null)
      {
         txLocal.set(invocation.invokeNext());
      }
      return txLocal.get();
   }

   public Object access(FieldWriteInvocation invocation) throws Throwable
   {
      // Return non-Tx value
      if (txLocal.getTransaction() == null) return invocation.invokeNext();

      txLocal.set(invocation.getValue());
      return null;
   }
}
