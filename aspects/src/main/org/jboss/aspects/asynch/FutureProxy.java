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

import java.lang.reflect.InvocationTargetException;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class FutureProxy implements Future
{
   private Future delegate;
   private boolean released = false;

   public void setDelegate(Future delegate)
   {
      synchronized (this)
      {
         this.delegate = delegate;
         if (released) delegate.release();
         this.notifyAll();
      }
   }

   public void release()
   {
      synchronized (this)
      {
         if (delegate == null)
         {
            released = true;
            return;
         }
      }
      delegate.release();
   }

   public Object get() throws InterruptedException, InvocationTargetException
   {
      synchronized (this)
      {
         if (delegate == null)
            this.wait();
         if (delegate == null) throw new RuntimeException("Failed to get delegate and timed out");
         return delegate.get();
      }
   }

   public Object get(long milliseconds) throws TimeoutException, InterruptedException, InvocationTargetException
   {
      synchronized (this)
      {
         if (delegate == null)
         {
            this.wait(milliseconds);
            if (delegate == null) throw new TimeoutException("Failed to get delegate and timed out");
         }
      }
      return delegate.get(milliseconds);
   }

   public boolean isDone()
   {
      return delegate == null || delegate.isDone();
   }

}
