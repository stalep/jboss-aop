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
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.CtPrimitiveType;
import javassist.Modifier;
import javassist.NotFoundException;

import org.jboss.aop.util.JavassistToReflect;

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public abstract class OptimizedBehaviourInvocations extends OptimizedInvocations
{
   /**
    * Returns a piece of code that sets all typed argument fields to the
    * parameter values of current behaviour (i.e., arg0 = $1; arg1 = $2...).
    * 
    * @param length number of arguments
    * @return the code that sets all argument fields to the values of current
    *         behaviour parameters
    */
   protected static String setArguments(int length)
   {
      return setArguments("invocation", length, 0);
   }

   /**
    * Adds typed argument fields to <code>invocation</code> and overwrites its
    * arguments accessor methods accordingly. 
    * 
    * @param pool                    the class pool that contains <code>invocation
    *                                <code>
    * @param invocation              the invocation class to which fields and methods
    *                                will be added
    * @param params                  the list of the parameter types
    * @param hasMarshalledArguments  indicates whether this invocation class has a
    *                                marshalled arguments field
    */
   protected static void addArgumentFieldsAndAccessors(ClassPool pool,
         CtClass invocation, CtClass[] params, boolean hasMarshalledArguments)
      throws NotFoundException, CannotCompileException
   {
      addArgumentFieldsToInvocation(invocation, params);
      addGetArguments(pool, invocation, params, hasMarshalledArguments);
      addSetArguments(pool, invocation, params);
   }
   
   protected static void addInvokeTarget(CtClass invocation, String dispatchLine, 
         CtClass[] params, String beforeDispatch, String afterDispatch)
   throws NotFoundException, CannotCompileException
   {
      StringBuffer sb = new StringBuffer("{");
      sb.append(beforeDispatch);
      if (params.length == 0)
      {
         sb.append(dispatchLine);
         sb.append("();");
      }
      else
      {
         sb.append("  if (inconsistentArgs){");
         sb.append(dispatchLine);
         sb.append('(');
         sb.append(JavassistToReflect.castInvocationValueToTypeString(params[0], "arguments[0]"));
         for (int i = 1; i < params.length; i++)
         {
            sb.append(", ");
            sb.append(JavassistToReflect.castInvocationValueToTypeString(params[i],
                  "arguments[" + i + "]"));
         }
         sb.append(");}   else {");
         sb.append(dispatchLine);
         sb.append("(arg0");
         for (int i = 1; i < params.length; i++)
         {
            sb.append(", ");
            sb.append("arg");
            sb.append(i);
         }
         sb.append(");} ");
      }
      sb.append(afterDispatch);
      sb.append("}");
      System.out.println("CODE: " + sb.toString());
      CtMethod invokeTarget = null;
      CtMethod in = invocation.getSuperclass().getDeclaredMethod("invokeTarget");
      try
      {
         invokeTarget = CtNewMethod.make(in.getReturnType(), "invokeTarget",
               in.getParameterTypes(), in.getExceptionTypes(), sb.toString(),
               invocation);
      }
      catch (CannotCompileException e)
      {
         System.out.println(sb.toString());
         throw e;
      }
      invokeTarget.setModifiers(in.getModifiers());
      invocation.addMethod(invokeTarget);
   }
   
   private static String setArguments(String inv, int length, int offset)
   {
      StringBuffer sb = new StringBuffer("");
      for (int i = 0 ; i < length ; i++)
      {
         sb.append(inv + ".arg" + (i) + " = $" + (i + 1 + offset) + "; ");
      }
      return sb.toString();
   }
   
   
   private static void addSetArguments(ClassPool pool, CtClass invocation, CtClass[] params)throws NotFoundException, CannotCompileException 
   {
      if (params == null || params.length == 0) return;
      CtClass methodInvocation = pool.get("org.jboss.aop.joinpoint.MethodInvocation");
      CtMethod template = methodInvocation.getDeclaredMethod("setArguments");
   
      StringBuffer code = new StringBuffer(
              "public void setArguments(java.lang.Object[] args){");
      code.append("   inconsistentArgs = false;");
      code.append("   arguments = args; ");
      for (int i = 0; i < params.length; i++)
      {
         if (params[i].isPrimitive())
         {
            CtPrimitiveType primitive = (CtPrimitiveType) params[i];
            code.append("   arg");
            code.append(i);
            code.append(" = ((");
            code.append(primitive.getWrapperName());
            code.append(")args[");
            code.append(i);
            code.append("]).");
            code.append(primitive.getGetMethodName());
            code.append("(); ");
         }
         else
         {
            code.append("   Object warg");
            code.append(i);
            code.append(" = args[");
            code.append(i);
            code.append("]; ");
            code.append("   arg");
            code.append(i);
            code.append(" = (");
            code.append(params[i].getName());
            code.append(")warg");
            code.append(i);
            code.append("; ");
         }
      }
      code.append("   inconsistentArgs = false;");
      code.append("}");
      CtMethod setArguments = CtNewMethod.make(code.toString(), invocation);
      setArguments.setModifiers(template.getModifiers());
      invocation.addMethod(setArguments);
   }

   private static void addGetArguments(ClassPool pool, CtClass invocation, CtClass[] params, boolean hasMarshalledArguments) throws CannotCompileException
   {
      if (params == null || params.length == 0) return;
      try {
         CtClass superInvocation = invocation.getSuperclass();
         CtMethod template = superInvocation.getDeclaredMethod("getArguments");
   
         StringBuffer code = new StringBuffer();
         code.append("public Object[] getArguments()");
         code.append("{ ");
         code.append("   inconsistentArgs = true;");
         if (hasMarshalledArguments)
         {
            code.append("   if (super.marshalledArguments != null)");
            code.append("   {");
            code.append("      Object[] args = super.getArguments();");
            code.append("      setArguments(args);");
            code.append("      return args;");
            code.append("   }");
         }
         
         code.append("   if (arguments != (Object[])null) { return (Object[])arguments; } ");
         code.append("   arguments = new Object[" + params.length + "]; ");
         for (int i = 0; i < params.length; i++)
         {
            code.append("   arguments[" + i + "] = ($w)arg" + i + "; ");
         }
   
         code.append("   return arguments; }");
         CtMethod getArguments = CtNewMethod.make(code.toString(), invocation);
         getArguments.setModifiers(template.getModifiers());
         invocation.addMethod(getArguments);
      } catch (NotFoundException e) {
        //Field invocations don't have a getArguments() method, that's fine
      } 
   }

   /** Adds fields arg0, arg1 etc. to the invocation class for storing the parameters for a method
    * 
    * @param invocation The invocation we want to add 
    * @param params Array of the types of the parameters
    * @throws CannotCompileException 
    */
   private static void addArgumentFieldsToInvocation(CtClass invocation, CtClass[] params)throws CannotCompileException
   {
      CtField inconsistentArgs = new CtField(CtClass.booleanType, "inconsistentArgs",
            invocation);
      invocation.addField(inconsistentArgs, CtField.Initializer.byExpr("false"));
      
      for (int i = 0 ; i < params.length ; i++)
      {
         CtField field = new CtField(params[i], "arg" + i, invocation);
         field.setModifiers(Modifier.PUBLIC);
         invocation.addField(field);
      }
   }
}