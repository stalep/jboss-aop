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

package org.jboss.aspects.asynchronous.aspects.jboss;

import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aspects.asynchronous.AsynchronousConstants;
import org.jboss.aspects.asynchronous.AsynchronousParameters;
import org.jboss.aspects.asynchronous.AsynchronousResponse;
import org.jboss.aspects.asynchronous.AsynchronousUserTask;
import org.jboss.aspects.asynchronous.common.AsynchronousResponseImpl;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 * @version <tt>$Revision$</tt>
 */

public class AsynchronousInvokeTask

implements AsynchronousUserTask, AsynchronousConstants
{

   protected AsynchronousInvokeTask()
   {

      super();

   }

   public AsynchronousResponse process(AsynchronousParameters jp)
   {

      try
      {

         InvokeTaskInputParameters ijp = (InvokeTaskInputParameters) jp;

         MethodInvocation invocation = ijp.invocation;

         Object res = invocation.invokeNext();

         return new AsynchronousResponseImpl(OK, res);

      }
      catch (Throwable e)
      {

         return new AsynchronousResponseImpl(EXCEPTIONCAUGHT, e);

      }

   }

   private boolean invoke(Object targetedObject,

                          Class[] paramemeterTypes,

                          Object[] parameterObjects,

                          Class stringName)
   {

      try
      {

         Class aClass = null;

         if (targetedObject == null)

            aClass = stringName;

         else

            aClass = targetedObject.getClass();

         Method method = aClass.getMethod("cleanup", paramemeterTypes);

         method.invoke(targetedObject, parameterObjects);

      }
      catch (Exception e)
      {

         return false;

      }

      return true;

   }

   public void cleanup(AsynchronousParameters jp)
   {
      try
      {

         InvokeTaskInputParameters ijp = (InvokeTaskInputParameters) jp;

         MethodInvocation invocation = ijp.invocation;

         Object targetedClass = invocation.getTargetObject();

         if (!invoke(targetedClass,

         invocation.getActualMethod().getParameterTypes(),

         invocation.getArguments(),

         invocation.getActualMethod().getDeclaringClass()))

            invoke(targetedClass,

            null,

            null,

            invocation.getActualMethod().getDeclaringClass());

      }
      catch (Exception e)
      {


      }

   }

}

