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

import org.jboss.logging.Logger;
import org.jboss.util.deadlock.DeadlockDetector;
import org.jboss.util.deadlock.Resource;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class is holds threads awaiting the transactional lock to be free
 * in a fair FIFO transactional queue.  Non-transactional threads
 * are also put in this wait queue as well. This class pops the next waiting transaction from the queue
 * and notifies only those threads waiting associated with that transaction.
 *
 * As of 04/10/2002, you can now specify in jboss.xml method attributes that define
 * methods as read-only.  read-only methods(and read-only beans) will release transactional
 * locks at the end of the invocation.  This decreases likelyhood of deadlock and increases
 * performance.
 *
 * FIXME marcf: we should get solid numbers on this locking, bench in multi-thread environments
 * We need someone with serious SUN hardware to run this lock into the ground
 *
 * @author <a href="marc.fleury@jboss.org">Marc Fleury</a>
 * @author <a href="bill@burkecentral.com">Bill Burke</a>
 *
 * @version $Revision$
 */
public class QueuedTxLock implements Resource
{
   public static final String TXLOCK = "TxLock";
   public static final String TIMEOUT = "timeout";

   private HashMap txLocks = new HashMap();
   private LinkedList txWaitQueue = new LinkedList();

   /** Transaction holding lock on bean */
   private Transaction tx = null;
   private boolean synched = false;
   private boolean isSynchronized = false;
   private Logger log = Logger.getLogger(this.getClass());

   public Object getResourceHolder()
   {
      return tx;
   }

   public void sync() throws InterruptedException
   {
      synchronized (this)
      {
         while (synched)
         {
            this.wait();
         }
         synched = true;
      }
   }

   public void releaseSync()
   {
      synchronized (this)
      {
         synched = false;
         this.notify();
      }
   }

   /**
    * The setTransaction associates a transaction with the lock.
    * The current transaction is associated by the schedule call.
    */
   public void setTransaction(Transaction tx)
   {
      this.tx = tx;
   }

   public Transaction getTransaction()
   {
      return tx;
   }


   private class TxLock
   {

      public Transaction waitingTx;
      public String threadName;
      public boolean isQueued;

      public TxLock(Transaction trans)
      {
         this.threadName = Thread.currentThread().toString();
         this.waitingTx = trans;
         this.isQueued = true;
      }

      public boolean equals(Object obj)
      {
         if (obj == this) return true;

         TxLock lock = (TxLock) obj;

         return lock.waitingTx.equals(this.waitingTx);
      }

      public int hashCode()
      {
         return waitingTx.hashCode();
      }

      public String toString()
      {
         StringBuffer buffer = new StringBuffer(100);
         buffer.append("TXLOCK waitingTx=").append(waitingTx);
         buffer.append(" thread=").append(threadName);
         buffer.append(" queued=").append(isQueued);
         return buffer.toString();
      }
   }

   protected TxLock getTxLock(Transaction miTx)
   {
      TxLock lock = null;
      TxLock key = new TxLock(miTx);
      lock = (TxLock) txLocks.get(key);
      if (lock == null)
      {
         txLocks.put(key, key);
         txWaitQueue.addLast(key);
         lock = key;
      }
      return lock;
   }

   protected boolean isTxExpired(Transaction miTx) throws Exception
   {
      if (miTx != null && miTx.getStatus() == Status.STATUS_MARKED_ROLLBACK)
      {
         return true;
      }
      return false;
   }


   public boolean lockNoWait(Transaction transaction) throws Exception
   {
      this.sync();
      if (log.isTraceEnabled())
         log.trace("lockNoWait tx=" + transaction + " " + toString());
      try
      {
         // And are we trying to enter with another transaction?
         if (getTransaction() != null && !getTransaction().equals(transaction))
         {
            return false;
         }
         setTransaction(transaction);
         return true;
      }
      finally
      {
         this.releaseSync();
      }
   }

   /**
    * doSchedule(Invocation)
    *
    * doSchedule implements a particular policy for scheduling the threads coming in.
    * There is always the spec required "serialization" but we can add custom scheduling in here
    *
    * Synchronizing on lock: a failure to get scheduled must result in a wait() call and a
    * release of the lock.  Schedulation must return with lock.
    *
    */
   public void schedule(Transaction miTx, org.jboss.aop.joinpoint.Invocation mi)
           throws Exception
   {
      boolean trace = log.isTraceEnabled();
      this.sync();
      try
      {
         if (trace) log.trace("Begin schedule");

         if (isTxExpired(miTx))
         {
            log.error("Saw rolled back tx=" + miTx);
            throw new RuntimeException("Transaction marked for rollback, possibly a timeout");
         }

         //Next test is independent of whether the context is locked or not, it is purely transactional
         // Is the instance involved with another transaction? if so we implement pessimistic locking
         waitForTx(mi, miTx, trace);
         if (!isSynchronized)
         {
            isSynchronized = true;
            miTx.registerSynchronization(new TxLockSynchronization());
         }
      }
      finally
      {
         this.releaseSync();
      }

   }

   /**
    * Wait until no other transaction is running with this lock.
    *
    * @return    Returns true if this thread was scheduled in txWaitQueue
    */
   protected void waitForTx(org.jboss.aop.joinpoint.Invocation mi, Transaction miTx, boolean trace) throws Exception
   {
      boolean wasScheduled = false;
      // Do we have a running transaction with the context?
      // We loop here until either until success or until transaction timeout
      // If we get out of the loop successfully, we can successfully
      // set the transaction on this puppy.
      TxLock txLock = null;
      while (getTransaction() != null &&
              // And are we trying to enter with another transaction?
              !getTransaction().equals(miTx))
      {
         // Check for a deadlock on every cycle
         try
         {
            DeadlockDetector.singleton.deadlockDetection(miTx, this);
         }
         catch (Exception e)
         {
            // We were queued, not any more
            if (txLock != null && txLock.isQueued)
            {
               txLocks.remove(txLock);
               txWaitQueue.remove(txLock);
            }
            throw e;
         }

         wasScheduled = true;
         // That's no good, only one transaction per context
         // Let's put the thread to sleep the transaction demarcation will wake them up
         if (trace) log.trace("Transactional contention on context miTx=" + miTx + " " + toString());

         if (txLock == null)
            txLock = getTxLock(miTx);

         if (trace) log.trace("Begin wait on " + txLock + " " + toString());

         // And lock the threads on the lock corresponding to the Tx in MI
         synchronized (txLock)
         {
            releaseSync();
            try
            {
               int txTimeout = 0;
               Integer timeout = (Integer) mi.getMetaData(TXLOCK, TIMEOUT);
               if (timeout != null) txTimeout = timeout.intValue();
               txLock.wait(txTimeout);
            }
            catch (InterruptedException ignored)
            {
            }
         } // end synchronized(txLock)

         this.sync();

         if (trace) log.trace("End wait on " + txLock + " " + toString());
         if (isTxExpired(miTx))
         {
            log.error(Thread.currentThread() + "Saw rolled back tx=" + miTx + " waiting for txLock");
            if (txLock.isQueued)
            {
               // Remove the TxLock from the queue because this thread is exiting.
               // Don't worry about notifying other threads that share the same transaction.
               // They will timeout and throw the below RuntimeException
               txLocks.remove(txLock);
               txWaitQueue.remove(txLock);
            }
            else if (getTransaction() != null && getTransaction().equals(miTx))
            {
               // We're not qu
               nextTransaction(trace);
            }
            if (miTx != null)
            {
               DeadlockDetector.singleton.removeWaiting(miTx);
            }
            throw new RuntimeException("Transaction marked for rollback, possibly a timeout");
         }
      } // end while(tx!=miTx)

      // If we get here, this means that we have the txlock
      if (!wasScheduled) setTransaction(miTx);
      return;
   }

   /*
    * nextTransaction()
    *
    * nextTransaction will
    * - set the current tx to null
    * - schedule the next transaction by notifying all threads waiting on the transaction
    * - setting the thread with the new transaction so there is no race with incoming calls
    */
   protected void nextTransaction(boolean trace)
   {
      if (!synched)
      {
         throw new IllegalStateException("do not call nextTransaction while not synched!");
      }

      setTransaction(null);
      // is there a waiting list?
      TxLock thelock = null;
      if (!txWaitQueue.isEmpty())
      {
         thelock = (TxLock) txWaitQueue.removeFirst();
         txLocks.remove(thelock);
         thelock.isQueued = false;
         // The new transaction is the next one, important to set it up to avoid race with
         // new incoming calls
         if (thelock.waitingTx != null)
            DeadlockDetector.singleton.removeWaiting(thelock.waitingTx);
         setTransaction(thelock.waitingTx);
         synchronized (thelock)
         {
            // notify All threads waiting on this transaction.
            // They will enter the methodLock wait loop.
            thelock.notifyAll();
         }
      }
      if (trace)
         log.trace("nextTransaction: " + thelock + " " + toString());
   }

   public void endTransaction()
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("endTransaction: " + toString());
      nextTransaction(trace);
   }

   public void endInvocation(Transaction thetx)
   {
      if (log.isTraceEnabled())
         log.trace("endInvocation: miTx=" + thetx + " " + toString());
   }

   public String toString()
   {
      StringBuffer buffer = new StringBuffer(100);
      buffer.append(" hash=").append(hashCode());
      buffer.append(" tx=").append(getTransaction());
      buffer.append(" synched=").append(synched);
      buffer.append(" queue=").append(txWaitQueue);
      return buffer.toString();
   }

   private final class TxLockSynchronization implements Synchronization
   {
      public void beforeCompletion()
      {
      }

      public void afterCompletion(int status)
      {
         try
         {
            sync();
         }
         catch (InterruptedException ignored)
         {
         }
         isSynchronized = false;
         endTransaction();
         releaseSync();
      }
   }
}

