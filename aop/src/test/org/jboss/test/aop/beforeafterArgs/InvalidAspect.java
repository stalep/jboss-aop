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
package org.jboss.test.aop.beforeafterArgs;

import junit.framework.Assert;

import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.Args;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Return;
import org.jboss.aop.joinpoint.CurrentInvocation;
import org.jboss.aop.joinpoint.Invocation;

/**
 * Aspect whose advices must never be called.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
/* TODO remove configuration from jboss-aop.xml file (in org.jboss.test.aop.args
 * and use it here
 */
public class InvalidAspect
{
   public void before(@JoinPoint Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void after(@JoinPoint Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing(@JoinPoint Object object)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void before1(@Arg int arg, @Args Object[] args)
   {
      Assert.fail("This advice should never be executed");
   }

   public Object around1(@Arg int arg, @Args Object[] args) throws Throwable
   {
      Assert.fail("This advice should never be executed");
      return CurrentInvocation.proceed();
   }
   
   public void finally72(@Return String valueReturned)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void finally2(@Return String returnedValue)
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void around1(Invocation invocation) throws Exception
   {
      Assert.fail("This advice should never be executed");
   }
   
   public void throwing7(Throwable throwable)
   {
      Assert.fail("This advice should never be executed");
   }
}