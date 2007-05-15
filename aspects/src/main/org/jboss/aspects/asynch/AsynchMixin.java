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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AsynchMixin implements FutureHolder, AsynchProvider, Externalizable
{
   private static final long serialVersionUID = 3408652015381364191L;

   private transient ThreadLocal currentFuture = new ThreadLocal();

   public AsynchMixin()
   {
   }

   public void setFuture(Future future)
   {
      Future oldFuture = (Future) currentFuture.get();
      try
      {
         if (oldFuture != null) oldFuture.release();
      }
      catch (Exception e)
      {
         // ignore
      }
      currentFuture.set(future);
   }

   public Future getFuture()
   {
      Future future = (Future) currentFuture.get();
      currentFuture.set(null);
      return future;
   }

   public void writeExternal(ObjectOutput out) throws IOException
   {

   }

   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      currentFuture = new ThreadLocal();
   }
}
