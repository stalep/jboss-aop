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

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.pointcut.CFlowStack;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 * @@org.jboss.aop.Aspect (scope=org.jboss.aop.advice.Scope.PER_VM)
 */
public class CFlowAspect
{
   public static int cflowAccess;
   
   /**
    * @@org.jboss.aop.CFlowStackDef (cflows={@org.jboss.aop.CFlowDef (expr = "void org.jboss.test.aop.annotated.VariaPOJO->cflowMethod1()", called=true), @org.jboss.aop.CFlowDef (expr = "void org.jboss.test.aop.annotated.VariaPOJO->cflowMethod2()", called=true)})
    */
   public static CFlowStack cf1And2Stack;
      
   /**
    * @@org.jboss.aop.CFlowStackDef (cflows={@org.jboss.aop.CFlowDef (expr = "void org.jboss.test.aop.annotated.VariaPOJO->cflowMethod1()", called=false), @org.jboss.aop.CFlowDef (expr = "void org.jboss.test.aop.annotated.VariaPOJO->cflowMethod2()", called=true)})
    */
   public static CFlowStack cfNot1And2Stack;

   
   /**
    * @@org.jboss.aop.Bind (pointcut="execution(void org.jboss.test.aop.annotated.VariaPOJO*->privateMethod())", cflow="(org.jboss.test.aop.annotated.CFlowAspect.cf1And2Stack OR org.jboss.test.aop.annotated.CFlowAspect.cfNot1And2Stack)")
    */
   public Object cflowAdvice(Invocation invocation) throws Throwable
   {
      System.out.println("CFlowAspect.cflowAdvice");
      cflowAccess++;
      return invocation.invokeNext();
   }
   
   /**
    * @@org.jboss.aop.Bind (pointcut="execution(void org.jboss.test.aop.annotated.VariaPOJO*->dynamicCFlowMethod())", cflow="org.jboss.test.aop.annotated.SimpleDynamicCFlow")
    */
   public Object dynamicCFlowAdvice(Invocation invocation) throws Throwable
   {
      System.out.println("CFlowAspect.dynamicCFlowAdvice");
      cflowAccess++;
      return invocation.invokeNext();
   }
}
