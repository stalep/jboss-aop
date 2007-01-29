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

import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.jboss.aop.ClassAdvisor;
import org.jboss.aop.classpool.AOPClassPool;

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class OptimizedMethodInvocations extends OptimizedBehaviourInvocations
{
   static String getOptimizedInvocationClassName(CtClass clazz, CtMethod method) 
   {
      long hash = org.jboss.aop.util.JavassistMethodHashing.methodHash(method);
      StringBuffer sb = new StringBuffer(clazz.getName());
      sb.append(".")
      .append(method.getName())
      .append("_")
      .append(Long.toString(hash).replace('-', 'N'));
      return sb.toString();
   }

   public static String getOptimizedInvocationClassName(Method method) throws Exception
   {
      long hash = org.jboss.aop.util.MethodHashing.methodHash(method);
      StringBuffer sb = new StringBuffer(method.getDeclaringClass().getName());
      sb.append(".")
      .append(method.getName())
      .append("_")
      .append(Long.toString(hash).replace('-', 'N'));
      return sb.toString();
   }

   protected static String createOptimizedInvocationClass(Instrumentor instrumentor, CtClass clazz, CtMethod method) throws NotFoundException, CannotCompileException
   {
      String wrappedName = ClassAdvisor.notAdvisedMethodName(clazz.getName(),
                                                             method.getName());
      AOPClassPool pool = (AOPClassPool) instrumentor.getClassPool();
      CtClass methodInvocation = pool.get("org.jboss.aop.joinpoint.MethodInvocation");
   
      String className = getOptimizedInvocationClassName(clazz, method);
      boolean makeInnerClass = true; //!Modifier.isPublic(method.getModifiers());
   
      CtClass invocation = makeInvocationClass(pool, makeInnerClass, clazz, className, methodInvocation);
      CtClass[] params = method.getParameterTypes();
      addArgumentFieldsAndAccessors(pool, invocation, params, true);
   
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
      code += wrappedName + "(";
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
      
      
      addCopy(pool, invocation, method.getParameterTypes(), isStatic);
   
      TransformerCommon.compileOrLoadClass(method.getDeclaringClass(), invocation);
   
      //Return fully qualified name of class (may be an inner class)
      return invocation.getName();
   }

   static void addCopy(ClassPool pool, CtClass invocation, CtClass[] params, boolean isStatic) throws NotFoundException, CannotCompileException
   {
      CtClass methodInvocation = pool.get("org.jboss.aop.joinpoint.MethodInvocation");
      CtMethod template = methodInvocation.getDeclaredMethod("copy");
   
      StringBuffer code = new StringBuffer("{");
              code.append("   ").append(invocation.getName()).append(" wrapper = new ").append(invocation.getName()).append("(this.interceptors, methodHash, advisedMethod, unadvisedMethod, advisor); ")
              .append("   wrapper.arguments = this.arguments; ")
              .append("   wrapper.metadata = this.metadata; ")
              .append("   wrapper.currentInterceptor = this.currentInterceptor; ")
              .append("   wrapper.instanceResolver = this.instanceResolver; ");
      if (!isStatic)
      {
         code.append("   wrapper.typedTargetObject = this.typedTargetObject; ");
         code.append("   wrapper.targetObject = this.targetObject; ");
      }
   
      for (int i = 0; i < params.length; i++)
      {
         code.append("   wrapper.arg").append(i).append(" = this.arg").append(i).append("; ");
      }
      code.append("   return wrapper; }");
   
      CtMethod copy = CtNewMethod.make(template.getReturnType(), "copy", template.getParameterTypes(), template.getExceptionTypes(), code.toString(), invocation);
      copy.setModifiers(template.getModifiers());
      invocation.addMethod(copy);
   
   }

}
