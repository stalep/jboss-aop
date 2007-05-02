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

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.logging.Logger;
import org.jboss.util.deadlock.ApplicationDeadlockException;


/**
 * TxSupport.java encapsulates the transaction handling possibilities
 * from the ejb spec.  The Tx interceptors call the clientInvoke and
 * serverInvoke methods on the subclass determined by the method's
 * transaction support.
 * <p/>
 * <p/>
 * Created: Sun Feb  2 23:25:09 2003
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */

public class TxPolicy
{
   protected final static Logger log = Logger.getLogger(TxPolicy.class);

   public static int MAX_RETRIES = 5;
   public static Random random = new Random();

   public void throwMandatory(Invocation invocation)
   {
      throw new RuntimeException("Transaction is mandatory.");
   }

   public Object invokeInNoTx(Invocation invocation) throws Throwable
   {
      return invocation.invokeNext();
   }

   public Object invokeInOurTx(Invocation invocation, TransactionManager tm) throws Throwable
   {
      for (int i = 0; i < MAX_RETRIES; i++)
      {
         tm.begin();
         Transaction tx = tm.getTransaction();
         try
         {
            try
            {
               return invocation.invokeNext();
            }
            catch (Throwable t)
            {
               handleExceptionInOurTx(invocation, t, tx);
            }
            finally
            {
               endTransaction(tm, tx);
            }
         }
         catch (Exception ex)
         {
            ApplicationDeadlockException deadlock = ApplicationDeadlockException.isADE(ex);
            if (deadlock != null)
            {
               if (!deadlock.retryable() ||
                   i + 1 >= MAX_RETRIES)
               {
                  throw deadlock;
               }
               log.warn(deadlock.getMessage() + " retrying " + (i + 1));

               Thread.sleep(random.nextInt(1 + i), random.nextInt(1000));
            }
            else
            {
               throw ex;
            }
         }
      }
      throw new RuntimeException("UNREACHABLE");
   }

   public void handleExceptionInOurTx(Invocation invocation, Throwable t, Transaction tx)
           throws Throwable
   {
      // if this is an ApplicationException, just rethrow it
      rethrowApplicationException(invocation, t);
      setRollbackOnly(tx);
      throw t;
   }

   public Object invokeInCallerTx(Invocation invocation, Transaction tx) throws Throwable
   {
      try
      {
         return invocation.invokeNext();
      }
      catch (Throwable t)
      {
         handleInCallerTx(invocation, t, tx);
      }
      throw new RuntimeException("UNREACHABLE");
   }

   public void handleInCallerTx(Invocation invocation, Throwable t, Transaction tx)
           throws Throwable
   {
      rethrowApplicationException(invocation, t);
      setRollbackOnly(tx);
      throw t;
   }

   /**
    * The <code>endTransaction</code> method ends a transaction and
    * translates any exceptions into
    * TransactionRolledBack[Local]Exception or SystemException.
    *
    * @param invocation an <code>Invocation</code> value
    * @param tm         a <code>TransactionManager</code> value
    * @param tx         a <code>Transaction</code> value
    * @throws javax.transaction.TransactionRolledbackException
    *          if an error occurs
    * @throws javax.transaction.SystemException
    *          if an error occurs
    */
   public void endTransaction(TransactionManager tm, Transaction tx)
   {
      try
      {
         if (tx != tm.getTransaction())
         {
            throw new IllegalStateException("Wrong tx on thread: expected " + tx + ", actual " + tm.getTransaction());
         }

         if (tx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
         {
            tm.rollback();
         }
         else
         {
            // Commit tx
            // This will happen if
            // a) everything goes well
            // b) app. exception was thrown
            tm.commit();
         }
      }
      catch (RollbackException e)
      {
         handleEndTransactionException(e);
      }
      catch (HeuristicMixedException e)
      {
         handleEndTransactionException(e);
      }
      catch (HeuristicRollbackException e)
      {
         handleEndTransactionException(e);
      }
      catch (SystemException e)
      {
         handleEndTransactionException(e);
      }
   }

   public void handleEndTransactionException(Exception e)
   {
      throw new RuntimeException(e);
   }

   /**
    * The <code>setRollbackOnly</code> method calls setRollbackOnly()
    * on the invocation's transaction and logs any exceptions than may
    * occur.
    *
    * @param invocation an <code>Invocation</code> value
    */
   public void setRollbackOnly(Transaction tx)
   {
      try
      {
         tx.setRollbackOnly();
      }
      catch (SystemException ex)
      {
         log.error("SystemException while setting transaction " +
                   "for rollback only", ex);
      }
      catch (IllegalStateException ex)
      {
         log.error("IllegalStateException while setting transaction " +
                   "for rollback only", ex);
      }
   }

   /**
    * The <code>rethrowApplicationException</code> method determines
    * if the supplied Throwable is an application exception and
    * rethrows it if it is.
    *
    * @param e a <code>Throwable</code> value
    * @throws Exception if an error occurs
    */
   public void rethrowApplicationException(Invocation inv, Throwable e) throws Throwable
   {
      Object applicationExceptions = inv.getMetaData("transaction", "application-exceptions");
      if (applicationExceptions == null) return;
      Class[] applicationExceptionsList;
      if (applicationExceptions instanceof String)
      {
         ArrayList tmpList = new ArrayList();
         String aes = (String) applicationExceptions;
         aes = aes.trim();
         StringTokenizer tokenizer = new StringTokenizer(aes, ",");
         while (tokenizer.hasMoreTokens())
         {
            String token = tokenizer.nextToken().trim();
            Class excClass = Thread.currentThread().getContextClassLoader().loadClass(token);
            tmpList.add(excClass);
         }
         applicationExceptionsList = (Class[]) tmpList.toArray(new Class[tmpList.size()]);
      }
      else
      {
         applicationExceptionsList = (Class[]) applicationExceptions;
      }
      for (int i = 0; i < applicationExceptionsList.length; i++)
      {
         if (applicationExceptionsList[i].isInstance(e)) throw e;
      }

   }

}
