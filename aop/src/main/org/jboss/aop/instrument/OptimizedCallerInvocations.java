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
package org.jboss.aop.instrument;


import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.jboss.aop.classpool.AOPClassPool;

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class OptimizedCallerInvocations extends OptimizedBehaviourInvocations
{

   protected static String createOptimizedMethodCalledByConInvocationClass(
         Instrumentor instrumentor, 
         String className, 
         CtClass callingClass, 
         CtMethod method, 
         int callingIndex, 
         long calledHash) throws NotFoundException, CannotCompileException
   {
      AOPClassPool pool = (AOPClassPool) instrumentor.getClassPool();
      CtClass methodInvocation = pool.get("org.jboss.aop.joinpoint.MethodCalledByConstructorInvocation");
   
      ////////////////
      //Create the class
      boolean makeInnerClass = Modifier.isPrivate(method.getModifiers());
      CtClass invocation = makeInvocationClass(pool, makeInnerClass, callingClass, className, methodInvocation);
   
      CtClass[] params = method.getParameterTypes();
      addArgumentFieldsToInvocation(invocation, params);
   
      /////////
      //Create invokeTarget() body
      boolean isStatic = javassist.Modifier.isStatic(method.getModifiers());
      if (!isStatic)
      {
         CtField target = new CtField(method.getDeclaringClass(), "typedTargetObject", invocation);
         target.setModifiers(Modifier.PUBLIC);
         invocation.addField(target);
      }
   
   
      CtMethod in = methodInvocation.getDeclaredMethod("invokeTarget");
   
      String code = "{";
   
      String returnStr = (method.getReturnType().equals(CtClass.voidType)) ? "" : "return ($w)";
      if (isStatic)
      {
         code +=
         "   " + returnStr + " " + method.getDeclaringClass().getName() + ".";
      }
      else
      {
         code +=
         "   " + returnStr + " typedTargetObject.";
      }
      code += method.getName() + "(";
      for (int i = 0; i < params.length; i++)
      {
         if (i > 0) code += ", ";
         code += "arg" + i;
      }
      code += ");  ";
      if (method.getReturnType().equals(CtClass.voidType))
      {
         code += " return null; ";
      }
      code += "}";
      CtMethod invokeTarget = null;
      try
      {
         invokeTarget = CtNewMethod.make(in.getReturnType(), "invokeTarget", in.getParameterTypes(), in.getExceptionTypes(), code, invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(code);
         throw e;
      }
      invokeTarget.setModifiers(in.getModifiers());
      invocation.addMethod(invokeTarget);
      addGetArguments(pool, invocation, method.getParameterTypes());
      addSetArguments(pool, invocation, method.getParameterTypes());
      
   
      ////////////
      //Create copy() method
      CtMethod copyTemplate = methodInvocation.getDeclaredMethod("copy");
   
      String copyCode = "{ "
      + "   "
      + invocation.getName()
      + " wrapper = new "
      + invocation.getName()
      + "(this.advisor, this.calling, this.method, this.callingObject, this.targetObject, this.arguments, this.interceptors);"
      + "   wrapper.metadata = this.metadata; "
      + "   wrapper.currentInterceptor = this.currentInterceptor; "
      + "   wrapper.instanceResolver = this.instanceResolver; "
      + "   wrapper.targetObject = this.targetObject; "
      + "   wrapper.responseContextInfo = this.responseContextInfo; ";
   
      if (!isStatic)
      {
         copyCode += "wrapper.typedTargetObject = typedTargetObject;";
      }
   
      for (int i = 0; i < params.length; i++)
      {
         copyCode += "   wrapper.arg" + i + " = this.arg" + i + "; ";
      }
      copyCode += "   return wrapper; }";
   
      CtMethod copy = null;
      try
      {
         copy = CtNewMethod.make(
               copyTemplate.getReturnType(), 
               "copy",
               copyTemplate.getParameterTypes(), 
               copyTemplate.getExceptionTypes(), 
               copyCode, 
               invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(copyCode);
         throw e;
      }
      copy.setModifiers(copyTemplate.getModifiers());
      invocation.addMethod(copy);
   
      TransformerCommon.compileOrLoadClass(callingClass, invocation);
   
      //Return fully qualified name of class (may be an inner class)
      return invocation.getName();
   }

   protected static String createOptimizedConCalledByConInvocationClass(
         Instrumentor instrumentor, 
         String className, 
         CtClass callingClass, 
         CtConstructor con, 
         int callingIndex, 
         long calledHash)  throws NotFoundException, CannotCompileException
   {
      //TODO: Merge this method with createOptimizedConCalledByMethodInvocationClass()
      AOPClassPool pool = (AOPClassPool) instrumentor.getClassPool();
      CtClass conInvocation = pool.get("org.jboss.aop.joinpoint.ConstructorCalledByConstructorInvocation");
   
      ////////////////
      //Create the class
      //String className = getOptimizedConCalledByConInvocationClassName(callingIndex, callingClass.getName(), calledHash);
      boolean makeInnerClass = Modifier.isPrivate(con.getModifiers());
      CtClass invocation = makeInvocationClass(pool, makeInnerClass, callingClass, className, conInvocation);
   
      CtClass[] params = con.getParameterTypes();
      addArgumentFieldsToInvocation(invocation, params);
   
      /////////
      //Create invokeTarget() body
      CtMethod in = conInvocation.getDeclaredMethod("invokeTarget");

      String code = "{";
      
      code += "setTargetObject(new " + con.getDeclaringClass().getName() + "(";
      for (int i = 0; i < params.length; i++)
      {
         if (i > 0) code += ", ";
         code += "arg" + i;
      }
      code += "));  ";
      code += "return getTargetObject();";
      code += "}";

      CtMethod invokeTarget = null;
      try
      {
         invokeTarget = CtNewMethod.make(
               in.getReturnType(),
               "invokeTarget", 
               in.getParameterTypes(), 
               in.getExceptionTypes(),
               code, 
               invocation);
      }
      catch (Exception e)
      {
         throw new RuntimeException("code: " + code, e);
      }
      invokeTarget.setModifiers(in.getModifiers());
      invocation.addMethod(invokeTarget);
      addGetArguments(pool, invocation, con.getParameterTypes());
      addSetArguments(pool, invocation, con.getParameterTypes());
   
      ////////////
      //Create copy() method
      CtMethod copyTemplate = conInvocation.getDeclaredMethod("copy");
   
      String copyCode = "{ "
         + "   "
         + invocation.getName()
         + " wrapper = new "
         + invocation.getName()
         + "(this.advisor, this.calling, this.constructor, this.wrappingMethod, this.callingObject, this.arguments, this.interceptors);"
         + "   wrapper.metadata = super.metadata; "
         + "   wrapper.currentInterceptor = super.currentInterceptor; "
         + "   wrapper.instanceResolver = super.instanceResolver; "
         + "   wrapper.interceptors = super.interceptors; "
         + "   wrapper.responseContextInfo = super.responseContextInfo; "
         + "   wrapper.targetObject = super.targetObject; ";
      
      for (int i = 0; i < params.length; i++)
      {
         copyCode += "   wrapper.arg" + i + " = this.arg" + i + "; ";
      }
      copyCode += "   return wrapper; }";

      CtMethod copy = null;
      try
      {
         copy = CtNewMethod.make(
               copyTemplate.getReturnType(), 
               "copy",
               copyTemplate.getParameterTypes(), 
               copyTemplate.getExceptionTypes(), 
               copyCode, 
               invocation);
      }
      catch (Exception e)
      {
         throw new RuntimeException("code: " + code, e);
      }
      copy.setModifiers(copyTemplate.getModifiers());
      invocation.addMethod(copy);
   
      TransformerCommon.compileOrLoadClass(callingClass, invocation);
   
      //Return fully qualified name of class (may be an inner class)
      return invocation.getName();
   }

   protected static String createOptimizedConCalledByMethodInvocationClass(
         Instrumentor instrumentor, 
         String className, 
         CtClass callingClass, 
         CtConstructor con, 
         long callingHash, 
         long calledHash) throws NotFoundException, CannotCompileException
   {
      AOPClassPool pool = (AOPClassPool) instrumentor.getClassPool();
      CtClass conInvocation = pool.get("org.jboss.aop.joinpoint.ConstructorCalledByMethodInvocation");
   
      ////////////////
      //Create the class
      boolean makeInnerClass = Modifier.isPrivate(con.getModifiers());
      CtClass invocation = makeInvocationClass(pool, makeInnerClass, callingClass, className, conInvocation);
   
      CtClass[] params = con.getParameterTypes();
      addArgumentFieldsToInvocation(invocation, params);
   
      /////////
      //Create invokeTarget() body
      CtMethod in = conInvocation.getDeclaredMethod("invokeTarget");

      String code = "{";
      code += "setTargetObject(new " + con.getDeclaringClass().getName() + "(";
      for (int i = 0; i < params.length; i++)
      {
         if (i > 0) code += ", ";
         code += "arg" + i;
      }
      code += "));  ";
      code += "return getTargetObject();";
      code += "}";
      
      CtMethod invokeTarget = null;
      try
      {
         invokeTarget = CtNewMethod.make(
               in.getReturnType(),
               "invokeTarget", 
               in.getParameterTypes(), 
               in.getExceptionTypes(),
               code, 
               invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(code);
         throw e;
      }

      invokeTarget.setModifiers(in.getModifiers());
   
      invocation.addMethod(invokeTarget);
      addGetArguments(pool, invocation, con.getParameterTypes());
      addSetArguments(pool, invocation, con.getParameterTypes());
   
      ////////////
      //Create copy() method
      CtMethod copyTemplate = conInvocation.getDeclaredMethod("copy");

      String copyCode = "{ "
         + "   "
         + invocation.getName()
         + " wrapper = new "
         + invocation.getName()
         + "(this.advisor, this.callingClass, this.callingMethod, this.constructor, this.wrappingMethod, this.callingObject, this.arguments, this.interceptors);"
         + "   wrapper.metadata = this.metadata; "
         + "   wrapper.currentInterceptor = this.currentInterceptor; "
         + "   wrapper.instanceResolver = this.instanceResolver; "
         + "   wrapper.targetObject = this.targetObject; "
         + "   wrapper.responseContextInfo = this.responseContextInfo; ";
      
      for (int i = 0; i < params.length; i++)
      {
         copyCode += "   wrapper.arg" + i + " = this.arg" + i + "; ";
      }
      copyCode += "   return wrapper; }";

      CtMethod copy = null;
      try
      {
         copy = CtNewMethod.make(
               copyTemplate.getReturnType(), 
               "copy",
               copyTemplate.getParameterTypes(), 
               copyTemplate.getExceptionTypes(), 
               copyCode, 
               invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(copyCode);
         throw e;
      }
      copy.setModifiers(copyTemplate.getModifiers());
   
      invocation.addMethod(copy);
   
      TransformerCommon.compileOrLoadClass(callingClass, invocation);
      
      //Return fully qualified name of class (may be an inner class)
      return invocation.getName();
   }

   protected static String createOptimizedMethodCalledByMethodInvocationClass(
         Instrumentor instrumentor, 
         String className, 
         CtClass callingClass, 
         CtMethod method, 
         long callingHash, 
         long calledHash)  throws NotFoundException, CannotCompileException
   {
      AOPClassPool pool = (AOPClassPool) instrumentor.getClassPool();
      CtClass methodInvocation = pool.get("org.jboss.aop.joinpoint.MethodCalledByMethodInvocation");
   
      ////////////////
      //Create the class
      boolean makeInnerClass = Modifier.isPrivate(method.getModifiers());
      CtClass invocation = makeInvocationClass(pool, makeInnerClass, callingClass, className, methodInvocation);
   
      CtClass[] params = method.getParameterTypes();
      addArgumentFieldsToInvocation(invocation, params);
   
      /////////
      //Create invokeTarget() body
      boolean isStatic = javassist.Modifier.isStatic(method.getModifiers());
      if (!isStatic)
      {
         CtField target = new CtField(method.getDeclaringClass(), "typedTargetObject", invocation);
         target.setModifiers(Modifier.PUBLIC);
         invocation.addField(target);
      }
   
   
      CtMethod in = methodInvocation.getDeclaredMethod("invokeTarget");
   
      String code = "{";
   
      String returnStr = (method.getReturnType().equals(CtClass.voidType)) ? "" : "return ($w)";
      if (isStatic)
      {
         code +=
         "   " + returnStr + " " + method.getDeclaringClass().getName() + ".";
      }
      else
      {
         code +=
         "   " + returnStr + " typedTargetObject.";
      }
      code += method.getName() + "(";
      for (int i = 0; i < params.length; i++)
      {
         if (i > 0) code += ", ";
         code += "arg" + i;
      }
      code += ");  ";
      if (method.getReturnType().equals(CtClass.voidType))
      {
         code += " return null; ";
      }
      code += "}";
      
      CtMethod invokeTarget = null;
      try
      {
         invokeTarget = CtNewMethod.make(in.getReturnType(), "invokeTarget", in.getParameterTypes(), in.getExceptionTypes(), code, invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(code);
         throw e;
      }
      invokeTarget.setModifiers(in.getModifiers());
      invocation.addMethod(invokeTarget);
      addGetArguments(pool, invocation, method.getParameterTypes());
      addSetArguments(pool, invocation, method.getParameterTypes());
   
      ////////////
      //Create copy() method
      CtMethod copyTemplate = methodInvocation.getDeclaredMethod("copy");
   
      String copyCode = "{ "
      + "   "
      + invocation.getName()
      + " wrapper = new "
      + invocation.getName()
      + "(this.advisor, this.callingClass, this.callingMethod, this.method, this.callingObject, this.targetObject, this.arguments, this.interceptors);"
      + "   wrapper.metadata = this.metadata; "
      + "   wrapper.currentInterceptor = this.currentInterceptor; "
      + "   wrapper.instanceResolver = this.instanceResolver; "
      + "   wrapper.targetObject = this.targetObject; "
      + "   wrapper.responseContextInfo = this.responseContextInfo; ";
   
      if (!isStatic)
      {
         copyCode += "wrapper.typedTargetObject = typedTargetObject;";
      }
   
      for (int i = 0; i < params.length; i++)
      {
         copyCode += "   wrapper.arg" + i + " = this.arg" + i + "; ";
      }
      copyCode += "   return wrapper; }";
   
      CtMethod copy = null;
      try
      {
         copy = CtNewMethod.make(
               copyTemplate.getReturnType(), 
               "copy",
               copyTemplate.getParameterTypes(), 
               copyTemplate.getExceptionTypes(), 
               copyCode, 
               invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(copyCode);
         throw e;
      }
      copy.setModifiers(copyTemplate.getModifiers());
      invocation.addMethod(copy);
   
      TransformerCommon.compileOrLoadClass(callingClass, invocation);
   
      //Return fully qualified name of class (may be an inner class)
      return invocation.getName();
   }
   
}
