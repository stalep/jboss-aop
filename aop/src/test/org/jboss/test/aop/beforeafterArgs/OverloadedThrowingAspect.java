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

import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Thrown;

/**
 * Aspect used on overloaded throwing advice tests.
 *
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class OverloadedThrowingAspect
{
   static String throwing1 = null;
   
   static void clear()
   {
      throwing1 = null;
   }

   // TODO finish this test when we can define the throwing to catch POJOException
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SubInterface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SubInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg Interface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,Interface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SubInterface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SubInterface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SubInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SubInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SuperInterface arg,
         @Arg Implementor implementor)
   {
      throwing1 = "JoinPointInfo,POJOException,SuperInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SubInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SubInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg Interface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,Interface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SuperInterface arg1,
         @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SuperInterface,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SuperInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SuperInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg Interface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,Interface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SubInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SubInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SuperInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SuperInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg Interface arg1, @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,Interface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown POJOException thrown, @Arg SuperInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,POJOException,SuperInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SubInterface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SubInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg Interface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,Exception,Interface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SubInterface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SubInterface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SubInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SubInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SuperInterface arg,
         @Arg Implementor implementor)
   {
      throwing1 = "JoinPointInfo,Exception,SuperInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SubInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SubInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg Interface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,Interface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SuperInterface arg1,
         @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SuperInterface,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SuperInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SuperInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg Interface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,Interface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SubInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SubInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SuperInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SuperInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg Interface arg1, @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,Interface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Exception thrown, @Arg SuperInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Exception,SuperInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SubInterface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SubInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg Interface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,Interface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SubInterface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SubInterface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SubInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SubInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SuperInterface arg,
         @Arg Implementor implementor)
   {
      throwing1 = "JoinPointInfo,Throwable,SuperInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SubInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SubInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg Interface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,Interface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SuperInterface arg1,
         @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SuperInterface,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SuperInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SuperInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg Interface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,Interface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SubInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SubInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SuperInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SuperInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg Interface arg1, @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,Interface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Throwable thrown, @Arg SuperInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Throwable,SuperInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SubInterface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,Object,SubInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg Interface arg1, @Arg Implementor arg2)
   {
      throwing1 = "JoinPointInfo,Object,Interface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SubInterface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Object,SubInterface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SubInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,Object,SubInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SuperInterface arg,
         @Arg Implementor implementor)
   {
      throwing1 = "JoinPointInfo,Object,SuperInterface,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SubInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Object,SubInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg Interface arg1, @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Object,Interface,SubInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SuperInterface arg1,
         @Arg SubInterface arg2)
   {
      throwing1 = "JoinPointInfo,Object,SuperInterface,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SuperInterface arg1, @Arg Object arg2)
   {
      throwing1 = "JoinPointInfo,Object,SuperInterface,Object";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg Interface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Object,Interface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SubInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Object,SubInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SuperInterface arg1, @Arg Interface arg2)
   {
      throwing1 = "JoinPointInfo,Object,SuperInterface,Interface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg Interface arg1, @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Object,Interface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinpoint,
         @Thrown Object thrown, @Arg SuperInterface arg1, 
         @Arg SuperInterface arg2)
   {
      throwing1 = "JoinPointInfo,Object,SuperInterface,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown POJOException thrown, @Arg SubInterface arg1)
   {
      throwing1 = "JoinPointInfo,POJOException,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown POJOException thrown, @Arg Implementor arg1)
   {
      throwing1 = "JoinPointInfo,POJOException,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown POJOException thrown, @Arg Interface arg1)
   {
      throwing1 = "JoinPointInfo,POJOException,Interface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown POJOException thrown, @Arg Object arg1)
   {
      throwing1 = "JoinPointInfo,POJOException,Object";
   }
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown POJOException thrown, @Arg SuperInterface arg1)
   {
      throwing1 = "JoinPointInfo,POJOException,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Exception thrown, @Arg SubInterface arg1)
   {
      throwing1 = "JoinPointInfo,Exception,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Exception thrown, @Arg Implementor arg1)
   {
      throwing1 = "JoinPointInfo,Exception,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Exception thrown, @Arg Interface arg1)
   {
      throwing1 = "JoinPointInfo,Exception,Interface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Exception thrown, @Arg Object arg1)
   {
      throwing1 = "JoinPointInfo,Exception,Object";
   }
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Exception thrown, @Arg SuperInterface arg1)
   {
      throwing1 = "JoinPointInfo,Exception,SuperInterface";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Throwable thrown, @Arg SubInterface arg1)
   {
      throwing1 = "JoinPointInfo,Throwable,SubInterface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Throwable thrown, @Arg Implementor arg1)
   {
      throwing1 = "JoinPointInfo,Throwable,Implementor";
   }
   
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Throwable thrown, @Arg Interface arg1)
   {
      throwing1 = "JoinPointInfo,Throwable,Interface";
   }

   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Throwable thrown, @Arg Object arg1)
   {
      throwing1 = "JoinPointInfo,Throwable,Object";
   }
   public void throwing1(@JoinPoint JoinPointInfo joinPoint,
         @Thrown Throwable thrown, @Arg SuperInterface arg1)
   {
      throwing1 = "JoinPointInfo,Throwable,SuperInterface";
   }
   
   public void throwing1(@Thrown POJOException thrown)
   {
      throwing1 = "POJOException";
   }
   
   public void throwing1(@Thrown Exception thrown)
   {
      throwing1 = "Exception";
   }
   
   public void throwing1(@Thrown Throwable thrown)
   {
      throwing1 = "Throwable";
   }
   
   public void throwing1(@Thrown Object thrown)
   {
      throwing1 = "Object";
   }
}