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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.aop.Advisor;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ThreadPoolExecutor implements ExecutorAbstraction
{
   private static ExecutorService executor = Executors.newCachedThreadPool();

   public void setAdvisor(Advisor advisor)
   {

   }

   public RemotableFuture execute(MethodInvocation invocation) throws Exception
   {
      final MethodInvocation copy = (MethodInvocation) invocation.copy();
      final ClassLoader cl = Thread.currentThread().getContextClassLoader();
      
      java.util.concurrent.Future future = executor.submit(new Callable()
      {
         public Object call() throws Exception
         {
            try
            {
               Thread.currentThread().setContextClassLoader(cl);
               return copy.invokeNext();
            }
            catch (Throwable throwable)
            {
               if (throwable instanceof Exception)
               {
                  throw ((Exception) throwable);
               }
               else
                  throw new Exception(throwable);
            }
         }
      });

      return new FutureImplJavaUtilConcurrent(future);
   }

}
