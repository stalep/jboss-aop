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

package org.jboss.aspects.asynchronous.concurrent;

import EDU.oswego.cs.dl.util.concurrent.Callable;
import EDU.oswego.cs.dl.util.concurrent.FutureResult;
import EDU.oswego.cs.dl.util.concurrent.ThreadFactoryUser;
import org.jboss.aspects.asynchronous.ProcessingTime;


/**
 * @author <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 * @version <tt>$Revision$</tt>
 */

public class TimedCallableImpl extends ThreadFactoryUser implements Callable, ProcessingTime
{

   private final AdapterTask function;

   private final long millis;

   private long endingTime = -1;

   private long startingTime = -1;


   public TimedCallableImpl(AdapterTask function, long millis)
   {

      this.function = function;

      this.millis = millis;

   }

   public Object call() throws Exception
   {

      FutureResult result = new FutureResult();


      Thread thread = getThreadFactory().newThread(result.setter(function));

      thread.start();

      try
      {

         startingTime = System.currentTimeMillis();

         Object obj = result.timedGet(millis);

         endingTime = System.currentTimeMillis();

         return obj;

      }
      catch (InterruptedException ex)
      {

         endingTime = System.currentTimeMillis();

         function.cleanup();

         thread.interrupt();

         throw ex;

      }

   }


   public long getEndingTime()
   {

      return endingTime;

   }

   public long getStartingTime()
   {

      return startingTime;

   }

}

