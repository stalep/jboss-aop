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
package mypackage;

import org.jboss.aop.advice.annotation.JoinPoint;

import org.jboss.aop.JoinPointInfo; 

public class MutexAspect
{
   private Object lock = new Object();
   private boolean locked = false;   
   
   public MutexAspect()
   {
      System.out.println("CONSTRUCTOR!!!!!");
   }
   
   public void beforeAdvice(@JoinPoint JoinPointInfo joinPoint)
   {
      synchronized(lock)
      {
         while (locked)
         {
            try
            {
               lock.wait();
            }
            catch(InterruptedException e)
            {
               Thread.currentThread().interrupt();
               return; 
            }
         }
         locked = true;
         System.out.println(">>> Retrieved concurrency lock");
      }
   }
   
   public void afterAdvice(@JoinPoint JoinPointInfo joinPoint)
   {
      synchronized(lock)
      {
         locked = false;
         lock.notifyAll();
         System.out.println("<<< Releasing concurrency lock");
      }
   }
}
