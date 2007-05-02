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
import org.jboss.logging.Logger;

/**
 * This interceptor handles transactions for AOP
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version $Revision$
 */
public class TxInterceptor
{
   private static final Logger log = Logger.getLogger(TxInterceptor.class);
   private static final TxTimeoutReader txTimeoutReader = TxTimeoutReaderFactory.getTxTimeoutReader();

   public static class Never implements Interceptor
   {
      protected TransactionManager tm;
      protected TxPolicy policy;
      protected int timeout;
      
      public Never(TransactionManager tm, TxPolicy policy)
      {
         this(tm, policy, -1);
      }

      public Never(TransactionManager tm, TxPolicy policy, int timeout)
      {
         this.tm = tm;
         this.policy = policy;
         this.timeout = timeout;
      }

      public Object invoke(Invocation invocation) throws Throwable
      {
         if (tm.getTransaction() != null)
         {
            throw new IllegalStateException("Transaction present on server in Never call");
         } // end of if ()
         return policy.invokeInNoTx(invocation);
      }

      public String getName()
      {
         return this.getClass().getName();
      }
   }

   public static class NotSupported implements Interceptor
   {
      protected TransactionManager tm;
      protected TxPolicy policy;
      protected int timeout;
      
      public NotSupported(TransactionManager tm, TxPolicy policy)
      {
         this(tm, policy, -1);
      }

      public NotSupported(TransactionManager tm, TxPolicy policy, int timeout)
      {
         this.tm = tm;
         this.policy = policy;
         this.timeout = timeout;
      }

      public Object invoke(Invocation invocation)
              throws Throwable
      {
         Transaction tx = tm.getTransaction();
         if (tx != null)
         {
            tm.suspend();
            try
            {
               return policy.invokeInNoTx(invocation);
            }
            finally
            {
               tm.resume(tx);
            }

         }
         else
         {
            return policy.invokeInNoTx(invocation);
         }
      }

      public String getName()
      {
         return this.getClass().getName();
      }
   }

   public static class Supports implements Interceptor
   {
      protected TransactionManager tm;
      protected TxPolicy policy;
      protected int timeout;
      
      public Supports(TransactionManager tm, TxPolicy policy)
      {
         this(tm, policy, -1);
      }

      public Supports(TransactionManager tm, TxPolicy policy, int timeout)
      {
         this.tm = tm;
         this.policy = policy;
         this.timeout = timeout;
      }

      public Object invoke(Invocation invocation) throws Throwable
      {
         if (tm.getTransaction() == null)
         {
            return policy.invokeInNoTx(invocation);
         }
         else
         {
            return policy.invokeInCallerTx(invocation, tm.getTransaction());
         }
      }

      public String getName()
      {
         return this.getClass().getName();
      }
   }

   public static class Required implements Interceptor
   {
      private static final Logger log = Logger.getLogger(Required.class);
      
      protected TransactionManager tm;
      protected TxPolicy policy;
      protected int timeout;
      
      public Required(TransactionManager tm, TxPolicy policy)
      {
         this(tm, policy, -1);
      }

      public Required(TransactionManager tm, TxPolicy policy, int timeout)
      {
         this.tm = tm;
         this.policy = policy;
         this.timeout = timeout;
      }

      public Object invoke(Invocation invocation) throws Throwable
      {
         int oldTimeout = txTimeoutReader.getTransactionTimeOut(tm);          

         try 
         {
            if (timeout != -1 && tm != null)
            {
               tm.setTransactionTimeout(timeout);
            }
               
            Transaction tx = tm.getTransaction();
            
            if (tx == null)
            {
               return policy.invokeInOurTx(invocation, tm);
            }
            else
            {
               return policy.invokeInCallerTx(invocation, tx);
            }
         }
         finally
         {
            if (tm != null)
            {
               tm.setTransactionTimeout(oldTimeout);
            }
         }
      }

      public String getName()
      {
         return this.getClass().getName();
      }
   }

   public static class RequiresNew implements Interceptor
   {
      protected TransactionManager tm;
      protected TxPolicy policy;
      protected int timeout;
      
      public RequiresNew(TransactionManager tm, TxPolicy policy)
      {
         this(tm, policy, -1);
      }

      public RequiresNew(TransactionManager tm, TxPolicy policy, int timeout)
      {
         this.tm = tm;
         this.policy = policy;
         this.timeout = timeout;
      }

      public String getName()
      {
         return this.getClass().getName();
      }

      public Object invoke(Invocation invocation) throws Throwable
      {
         int oldTimeout = txTimeoutReader.getTransactionTimeOut(tm);          

         try 
         {
            if (timeout != -1 && tm != null)
            {
               tm.setTransactionTimeout(timeout);
            }
            
            Transaction tx = tm.getTransaction();
            if (tx != null)
            {
               tm.suspend();
               try
               {
                  return policy.invokeInOurTx(invocation, tm);
               }
               finally
               {
                  tm.resume(tx);
               }
            }
            else
            {
               return policy.invokeInOurTx(invocation, tm);
            }
         }
         finally
         {
            if (tm != null)
            {
               tm.setTransactionTimeout(oldTimeout);
            }
         }
      }
   }

   public static class Mandatory implements Interceptor
   {
      protected TransactionManager tm;
      protected TxPolicy policy;
      protected int timeout;
      
      public Mandatory(TransactionManager tm, TxPolicy policy)
      {
         this(tm, policy, -1);
      }

      public Mandatory(TransactionManager tm, TxPolicy policy, int timeout)
      {
         this.tm = tm;
         this.policy = policy;
         this.timeout = timeout;
      }

      public String getName()
      {
         return this.getClass().getName();
      }

      public Object invoke(Invocation invocation) throws Throwable
      {
         Transaction tx = tm.getTransaction();
         if (tx == null)
         {
            policy.throwMandatory(invocation);
         }
         return policy.invokeInCallerTx(invocation, tx);
      }

   }
}
