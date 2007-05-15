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
public interface Future
{
   /**
    * get result, throws Exception of method or InterruptedException
    */
   public Object get() throws InterruptedException, InvocationTargetException;

   /**
    * get result, throws Exception of method or InterruptedException
    */
   public Object get(long milliseconds) throws TimeoutException, InterruptedException, InvocationTargetException;

   /**
    * is the method call done?
    */
   public boolean isDone();

   /**
    * Release this future so that it can be garbaged collected remotely
    * This is only useful for remote asynchronous.
    * This is import to call especially for remote otherwise the remote
    * future on the server will never get garbaged collected.
    * AsynchProvider will call release if it is holding a Future already
    * within its threadlocal when a new one is set on it.
    */
   public void release();
}
