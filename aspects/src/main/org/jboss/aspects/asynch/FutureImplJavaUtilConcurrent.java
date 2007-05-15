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


import org.jboss.aop.Dispatcher;
import org.jboss.util.id.GUID;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 37406 $
 */
public class FutureImplJavaUtilConcurrent implements RemotableFuture
{
   private java.util.concurrent.Future result;
   private GUID remoteObjectID;

   public FutureImplJavaUtilConcurrent(java.util.concurrent.Future result)
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
      catch (ExecutionException e)
      {
         release();
         throw new InvocationTargetException(e.getCause());
      }
   }

   public Object get(long milliseconds) throws org.jboss.aspects.asynch.TimeoutException, InterruptedException, InvocationTargetException
   {
      try
      {
         Object rtn = result.get(milliseconds, TimeUnit.MILLISECONDS);
         release();
         return rtn;
      }
      catch (ExecutionException e)
      {
         release();
         throw new InvocationTargetException(e.getCause());
      }
      catch (java.util.concurrent.TimeoutException e)
      {
         throw new org.jboss.aspects.asynch.TimeoutException(e);
      }
   }

   public boolean isDone()
   {
      return result.isDone();
   }

}
