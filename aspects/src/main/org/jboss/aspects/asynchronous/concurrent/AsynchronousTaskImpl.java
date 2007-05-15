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
import EDU.oswego.cs.dl.util.concurrent.TimeoutException;
import org.jboss.aspects.asynchronous.AsynchronousConstants;
import org.jboss.aspects.asynchronous.AsynchronousParameters;
import org.jboss.aspects.asynchronous.AsynchronousUserTask;
import org.jboss.aspects.asynchronous.ProcessingTime;
import org.jboss.aspects.asynchronous.ThreadManagerResponse;
import org.jboss.aspects.asynchronous.common.ThreadManagerResponseImpl;

import java.lang.reflect.InvocationTargetException;

/**
 * @author <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 * @version <tt>$Revision$</tt>
 */

public final class AsynchronousTaskImpl

implements AsynchronousConstants, org.jboss.aspects.asynchronous.concurrent.AsynchronousTask
{

   private long _timeout = 0;

   private AsynchronousParameters _inputParametersImpl = null;

   private AsynchronousUserTask _oneInstance = null;

   private FutureResult _futureResult = null;

   private Callable _callable = null;

   private String _id = null;

   AsynchronousTaskImpl(String id,

                        String taskImpl,

                        AsynchronousParameters inputParametersImpl,

                        long timeout)
   {

      this._id = id;

      _futureResult = new FutureResult();

      _timeout = timeout;

      _inputParametersImpl = inputParametersImpl;

      try
      {

         Class aClass = Class.forName(taskImpl);

         _oneInstance = (AsynchronousUserTask) aClass.newInstance();

      }
      catch (Exception e)
      {
         e.printStackTrace();

      }

   }

   AsynchronousTaskImpl(String id,

                        AsynchronousUserTask userTask,

                        AsynchronousParameters inputParametersImpl,

                        long timeout)
   {

      this._id = id;

      _futureResult = new FutureResult();

      _timeout = timeout;

      _oneInstance = userTask;

      _inputParametersImpl = inputParametersImpl;

   }

   public AsynchronousParameters getInputParameters()
   {

      return _inputParametersImpl;

   }

   public long getTimeout()
   {

      return _timeout;

   }

   public AsynchronousUserTask getTask()
   {

      return _oneInstance;

   }

   public ThreadManagerResponse getResponse()
   {

      try
      {

         if (isDone())

            return (ThreadManagerResponse) _futureResult.get();

         else
         {

            Object value = _futureResult.get();

            if (value != null)

               return (ThreadManagerResponse) value;

            else

               return new ThreadManagerResponseImpl(getId(),

               NOVALUE,

               null,

               null);

         }

      }
      catch (TimeoutException e)
      {

         return new ThreadManagerResponseImpl(getId(),

         TIMEOUT,

         e.getMessage(),

         e,

         getStartingTime(),

         getEndingTime());

      }
      catch (InterruptedException e)
      {

         return new ThreadManagerResponseImpl(getId(),

         INTERRUPTED,

         e.getMessage(),

         e,

         getStartingTime(),

         getEndingTime());

      }
      catch (InvocationTargetException e)
      {

         int errorCode = INVOCATION;

         if (e.getTargetException()

         instanceof EDU.oswego.cs.dl.util.concurrent.TimeoutException)

            errorCode = TIMEOUT;

         return new ThreadManagerResponseImpl(getId(),

         errorCode,

         e.getTargetException().getMessage(),

         e.getTargetException(),

         getStartingTime(),

         getEndingTime());

      }

   }

   public long getStartingTime()
   {

      return ((ProcessingTime) _callable).getStartingTime();

   }

   public long getEndingTime()
   {

      return ((ProcessingTime) _callable).getEndingTime();

   }


   public boolean isDone()
   {

      return _futureResult.isReady();

   }

   public Runnable add() throws Exception
   {

      try
      {

         if (_timeout == 0)

            _callable =

            new AdapterTask(getId(),

            _inputParametersImpl,

            _oneInstance);

         else

            _callable =

            new TimedCallableImpl(new AdapterTask(getId(),

            _inputParametersImpl,

            _oneInstance),

            _timeout);

         return _futureResult.setter(_callable);


      }
      catch (Exception e)
      {


         throw e;

      }

   }

   public String getId()
   {

      return _id;

   }

}

