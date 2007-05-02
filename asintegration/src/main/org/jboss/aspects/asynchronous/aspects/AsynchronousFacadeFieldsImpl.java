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

package org.jboss.aspects.asynchronous.aspects;

import org.jboss.aspects.asynchronous.AsynchronousTask;

/**
 * @author <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 * @version <tt>$Revision$</tt>
 */

public class AsynchronousFacadeFieldsImpl implements AsynchronousFacadeFields
{

   private AsynchronousTask asynchronousTask = null;

   private long timeout = 0;

   private String id = "None";

   public AsynchronousFacadeFieldsImpl()
   {

      super();

   }

   public String getId()
   {

      return this.id;

   }

   public void setId(String id)
   {

      this.id = id;

   }

   public long getTimeout()
   {

      return this.timeout;

   }

   public void setTimeout(long timeout)
   {

      this.timeout = timeout;

   }

   public AsynchronousTask getAsynchronousTask()
   {

      return this.asynchronousTask;

   }

   public void setAsynchronousTask(AsynchronousTask asynchronousTask)
   {

      this.asynchronousTask = asynchronousTask;

   }

}

