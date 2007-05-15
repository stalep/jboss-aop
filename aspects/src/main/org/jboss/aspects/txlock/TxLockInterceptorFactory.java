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

import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.aop.metadata.SimpleMetaData;
import org.jboss.aop.util.PayloadKey;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.TransactionManager;

/**
 *
 *  @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 *  @version $Revision$
 */
public class TxLockInterceptorFactory implements org.jboss.aop.advice.AspectFactory
{

   private static TransactionManager getTransactionManager()
   {
      try
      {
         InitialContext ctx = new InitialContext();
         TransactionManager tm = (TransactionManager) ctx.lookup("java:/TransactionManager");
         return tm;
      }
      catch (NamingException e)
      {
         throw new RuntimeException(e);
      }
   }

   public Object createPerVM()
   {
      throw new RuntimeException("this interceptor only supports PER_CLASS and PER_INSTANCE");
   }

   public Object createPerClass(Advisor advisor)
   {
      QueuedTxLock lock = new QueuedTxLock();
      advisor.getClassMetaData().addMetaData(QueuedTxLock.TXLOCK, QueuedTxLock.TXLOCK, lock, PayloadKey.TRANSIENT);
      return new TxLockInterceptor(getTransactionManager(), lock);

   }

   public Object createPerInstance(Advisor advisor, InstanceAdvisor instanceAdvisor)
   {
      SimpleMetaData instanceData = instanceAdvisor.getMetaData();
      QueuedTxLock lock = null;
      synchronized(instanceData)
      {
         lock = (QueuedTxLock)instanceData.getMetaData(QueuedTxLock.TXLOCK, QueuedTxLock.TXLOCK);
         if (lock == null)
         {
            lock = new QueuedTxLock();
            instanceData.addMetaData(QueuedTxLock.TXLOCK, QueuedTxLock.TXLOCK, lock, PayloadKey.TRANSIENT);
         }
      }
      return new TxLockInterceptor(getTransactionManager(), lock);
   }

   public Object createPerJoinpoint(Advisor advisor, Joinpoint jp)
   {
      throw new RuntimeException("this interceptor only supports PER_CLASS and PER_INSTANCE");
   }

   public Object createPerJoinpoint(Advisor advisor, InstanceAdvisor instanceAdvisor, Joinpoint jp)
   {
      throw new RuntimeException("this interceptor only supports PER_CLASS and PER_INSTANCE");
   }

   public String getName()
   {
      return getClass().getName();
   }
}
