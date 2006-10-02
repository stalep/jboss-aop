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
package org.jboss.test.aop.annotated;

import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.FieldReadInvocation;
import org.jboss.aop.joinpoint.FieldWriteInvocation;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.pointcut.Pointcut;

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 * @@org.jboss.aop.Aspect (scope=org.jboss.aop.advice.Scope.PER_VM)
 */
public class AspectPerVM
{
   public int constructorCalled;
   public int methodCalled;
   public int fieldRead;
   public int fieldWrite;
   public int anotherPOJOAccess;
   
   /**
    * @@org.jboss.aop.PointcutDef ("execution(org.jboss.test.aop.annotated.AnotherPOJO->new(..))") 
    */   
   public static Pointcut anotherPOJOConstructors;
   
   /**
    * @@org.jboss.aop.PointcutDef ("get(* org.jboss.test.aop.annotated.AnotherPOJO->field)") 
    */
   public static Pointcut anotherPOJOFieldReads;

   /**
    * @@org.jboss.aop.PointcutDef ("set(* org.jboss.test.aop.annotated.AnotherPOJO->field)") 
    */
   public static Pointcut anotherPOJOFieldWrites;
   
   /**
    * @@org.jboss.aop.PointcutDef ("execution(* org.jboss.test.aop.annotated.AnotherPOJO->*(..))") 
    */
   public static Pointcut anotherPOJOPublicMethods;

   /**
    * @@org.jboss.aop.PointcutDef ("org.jboss.test.aop.annotated.AspectPerVM.anotherPOJOFieldWrites OR org.jboss.test.aop.annotated.AspectPerVM.anotherPOJOFieldReads") 
    */
   public static Pointcut anotherPOJOFields;

   /**
    * @@org.jboss.aop.Prepare ("all(org.jboss.test.aop.annotated.PreparePOJO)") 
    */
   public static Pointcut preparePOJO;
   
   /**
    * @@org.jboss.aop.Bind (pointcut="execution(org.jboss.test.aop.annotated.POJO*->new())")
    */
   public Object constructorAdvice(ConstructorInvocation invocation) throws Throwable
   {
      System.out.println("AspectPerVM.constructorAdvice accessing: " + invocation.getConstructor().toString());
      constructorCalled++;
      return invocation.invokeNext();
   }

   /**
    * @@org.jboss.aop.Bind (pointcut="execution(void org.jboss.test.aop.annotated.POJO*->someMethod())")
    */
   public Object methodAdvice(MethodInvocation invocation) throws Throwable
   {
      System.out.println("AspectPerVM.methodAdvice accessing: " + invocation.getMethod().toString());
      methodCalled++;
      return invocation.invokeNext();
   }

   /**
    * @@org.jboss.aop.Bind (pointcut="set(* org.jboss.test.aop.annotated.POJO*->field)")
    */
   public Object fieldAdvice(FieldWriteInvocation invocation) throws Throwable
   {
      System.out.println("AspectPerVM.fieldAdvice writing to field: " + invocation.getField().getName());
      fieldWrite++;
      return invocation.invokeNext();
   }

   /**
    * @@org.jboss.aop.Bind (pointcut="get(* org.jboss.test.aop.annotated.POJO*->field)")
    */
   public Object fieldAdvice(FieldReadInvocation invocation) throws Throwable
   {
      System.out.println("AspectPerVM.fieldAdvice reading field: " + invocation.getField().getName());
      fieldRead++;
      return invocation.invokeNext();
   }

   /**
    * @@org.jboss.aop.Bind (pointcut = "org.jboss.test.aop.annotated.AspectPerVM.anotherPOJOFields OR org.jboss.test.aop.annotated.AspectPerVM.anotherPOJOConstructors OR org.jboss.test.aop.annotated.AspectPerVM.anotherPOJOPublicMethods")
    */
   public Object anotherPOJOAdvice(Invocation invocation) throws Throwable
   {
      System.out.println("AspectPerVM.anotherPOJOAdvice");
      anotherPOJOAccess++;
      return invocation.invokeNext();
   }
   
   public void reset()
   {
      constructorCalled = 0;
      methodCalled = 0;
      fieldRead = 0;
      fieldWrite = 0;
      anotherPOJOAccess = 0;      
   }

}
