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

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public abstract class OptimizedBehaviourInvocations extends OptimizedInvocations
{
   protected static void addSetArguments(ClassPool pool, CtClass invocation, CtClass[] params)throws NotFoundException, CannotCompileException 
   {
      if (params == null || params.length == 0) return;
      CtClass methodInvocation = pool.get("org.jboss.aop.joinpoint.MethodInvocation");
      CtMethod template = methodInvocation.getDeclaredMethod("setArguments");
   
      String code =
              "public void setArguments(java.lang.Object[] args)" +
              "{ ";
      code += "   arguments = args; ";
      for (int i = 0; i < params.length; i++)
      {
         if (params[i].isPrimitive())
         {
            CtPrimitiveType primitive = (CtPrimitiveType) params[i];
            code += "   arg" + i + " = ((" + primitive.getWrapperName() + ")args[" + i + "])." + primitive.getGetMethodName() + "(); ";
         }
         else
         {
            code += "   Object warg" + i + " = args[" + i + "]; ";
            code += "   arg" + i + " = (" + params[i].getName() + ")warg" + i + "; ";
         }
      }
      code += "}";
      CtMethod setArguments = CtNewMethod.make(code, invocation);
      setArguments.setModifiers(template.getModifiers());
      invocation.addMethod(setArguments);
   }

   public static void addGetArguments(ClassPool pool, CtClass invocation, CtClass[] params) throws CannotCompileException
   {
      addGetArguments(pool, invocation, params, false);
   }

   public static void addGetArguments(ClassPool pool, CtClass invocation, CtClass[] params, boolean hasMarshalledArguments) throws CannotCompileException
   {
      if (params == null || params.length == 0) return;
      try {
         CtClass superInvocation = invocation.getSuperclass();
         CtMethod template = superInvocation.getDeclaredMethod("getArguments");
   
         StringBuffer code = new StringBuffer();
         code.append("public Object[] getArguments()");
         code.append("{ ");
         
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

   protected static String setArguments(int length)
   {
      return setArguments("invocation", length, 0);
   }

   protected static String setArguments(String inv, int length, int offset)
   {
      StringBuffer sb = new StringBuffer("");
      for (int i = 0 ; i < length ; i++)
      {
         sb.append(inv + ".arg" + (i) + " = $" + (i + 1 + offset) + "; ");
      }
      return sb.toString();
   }

   /** Adds fields arg0, arg1 etc. to the invocation class for storing the parameters for a method
    * 
    * @param invocation The invocation we want to add 
    * @param params Array of the types of the parameters
    * @throws CannotCompileException 
    */
   public static void addArgumentFieldsToInvocation(CtClass invocation, CtClass[] params)throws CannotCompileException
   {
      for (int i = 0 ; i < params.length ; i++)
      {
         CtField field = new CtField(params[i], "arg" + i, invocation);
         field.setModifiers(Modifier.PUBLIC);
         invocation.addField(field);
      }
   }
}
