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
package org.jboss.aop.advice;

import java.lang.reflect.Method;

import javassist.CtClass;
import javassist.NotFoundException;

/** Contains the properties of an advice method that we want to find.
 * Once found it is populated with the arguments
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AdviceMethodProperties
{
   public static final int JOINPOINT_ARG = -1;
   public static final int INVOCATION_ARG = -2;
   public static final int TARGET_ARG = -3;
   public static final int RETURN_ARG = -4;
   public static final int THROWABLE_ARG = -5;
   public static final int ARGS_ARG = -6;
   public static final int CALLEE_ARG = -7;
   public static final int CALLER_ARG = -8;
   public static final int ARG_ARG = -9;
   
   public static final CtClass[] EMPTY_PARAMETERS = {};
   
   //find properties
   private Class aspectClass;
   private String adviceName;
   private Class infoType;
   private Class invocationType;
   private Class joinpointReturnType;
   private Class[] joinpointParameters;
   private Class[] joinpointExceptions;

   //found properties
   private Method adviceMethod;
   private int[] args;

   public AdviceMethodProperties(
         Class aspectClass, 
         String adviceName, 
         Class infoType,
         Class invocationType,
         Class joinpointReturnType,
         Class[] joinpointParameters,
         Class[] joinpointExceptions)
   {
      this.aspectClass = aspectClass;
      this.adviceName = adviceName;
      this.infoType = infoType;
      this.invocationType = invocationType;
      this.joinpointReturnType = joinpointReturnType;
      this.joinpointParameters = joinpointParameters;
      this.joinpointExceptions = joinpointExceptions;
   }

   public void setFoundProperties(Method adviceMethod, int[] args)
   {
      this.adviceMethod = adviceMethod;
      this.args = args;
   }

   public String getAdviceName()
   {
      return adviceName;
   }


   public Class getAspectClass()
   {
      return aspectClass;
   }


   public Class getInfoType()
   {
      return infoType;
   }


   public Class getInvocationType()
   {
      return invocationType;
   }


   public Class[] getJoinpointExceptions()
   {
      return joinpointExceptions;
   }


   public Class[] getJoinpointParameters()
   {
      return joinpointParameters;
   }


   public Class<?> getJoinpointReturnType()
   {
      return joinpointReturnType;
   }
   
   public boolean isAdviceVoid() throws NotFoundException
   {
      return adviceMethod.getReturnType().equals(void.class);
   }

   public Method getAdviceMethod()
   {
      return adviceMethod;
   }

   public int[] getArgs()
   {
      return args;
   }
   
}
