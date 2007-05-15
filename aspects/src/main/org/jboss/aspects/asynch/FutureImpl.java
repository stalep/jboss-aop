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
package org.jboss.aspects.asynch;

import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import org.jboss.aop.Dispatcher;
import org.jboss.util.id.GUID;

import java.lang.reflect.InvocationTargetException;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class FutureImpl
implements RemotableFuture
{
   private FutureResult result;
   private GUID remoteObjectID;

   public FutureImpl(FutureResult result)
   {
      this.result = result;
   }

   public void setRemoteObjectID(GUID remoteObjectID)
   {
      this.remoteObjectID = remoteObjectID;
   }

   public void release()
   {
      if (remoteObjectID != null)
      {
         Dispatcher.singleton.unregisterTarget(remoteObjectID);
      }
   }

   public Object get() throws InterruptedException, InvocationTargetException
   {
      try
      {
         Object rtn = result.get();
         release();
         return rtn;
      }
      catch (InvocationTargetException e)
      {
         release();
         throw e;
      }
   }

   public Object get(long milliseconds) throws TimeoutException, InterruptedException, InvocationTargetException
   {
      try
      {
         Object rtn = result.timedGet(milliseconds);
         release();
         return rtn;
      }
      catch (EDU.oswego.cs.dl.util.concurrent.TimeoutException e)
      {
         throw new TimeoutException(e);
      }
      catch (InvocationTargetException e)
      {
         release();
         throw e;
      }
   }

   public boolean isDone()
   {
      return result.isReady();
   }

}
