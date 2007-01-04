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
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.jboss.aop.GeneratedClassAdvisor;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.MethodByMethodInfo;
import org.jboss.aop.advice.AdviceMethodProperties;
import org.jboss.aop.advice.AdviceMethodProperties.Context;
import org.jboss.aop.joinpoint.MethodCalledByMethodInvocation;
import org.jboss.aop.util.ReflectToJavassist;

public class MethodByMethodJoinPointGenerator extends JoinPointGenerator
{
   public static final String GENERATOR_PREFIX = JoinPointGenerator.GENERATOR_PREFIX + "MByM_";
   public static final String JOINPOINT_CLASS_PREFIX = JoinPointGenerator.JOINPOINT_CLASS_PREFIX + "MByM_";
   public static final String JOINPOINT_FIELD_PREFIX = JoinPointGenerator.JOINPOINT_FIELD_PREFIX + "MByM_";
   private static final Class INVOCATION_TYPE = MethodCalledByMethodInvocation.class;
   private static final CtClass INVOCATION_CT_TYPE;
   static
   {
      try
      {
         INVOCATION_CT_TYPE = ReflectToJavassist.classToJavassist(INVOCATION_TYPE);
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public MethodByMethodJoinPointGenerator(GeneratedClassAdvisor advisor, JoinPointInfo info)
   {
      super(advisor, info, getParameters((MethodByMethodInfo) info));
   }

   private static JoinPointParameters getParameters(MethodByMethodInfo info)
   {
      if (Modifier.isStatic(info.getCallingMethod().getModifiers()))
      {
         if (Modifier.isStatic(info.getMethod().getModifiers()))
         {
            return JoinPointParameters.ONLY_ARGS;
         }
         return JoinPointParameters.TARGET_ARGS;
      }
      if (Modifier.isStatic(info.getMethod().getModifiers()))
      {
         return JoinPointParameters.CALLER_ARGS;
      }
      return JoinPointParameters.TARGET_CALLER_ARGS;
   }
   
   protected void initialiseJoinPointNames()
   {
      joinpointClassName = getInfoClassName(
               callingMethodHash(),
               calledClass(),
               calledMethodHash());

      joinpointFieldName = getInfoFieldName(
            callingMethodHash(),
            calledClass(),
            calledMethodHash());
   }

   private long callingMethodHash()
   {
      return ((MethodByMethodInfo)info).getCallingMethodHash();
   }

   private String calledClass()
   {
      return ((MethodByMethodInfo)info).getCalledClass().getName();
   }

   private long calledMethodHash()
   {
      return ((MethodByMethodInfo)info).getCalledMethodHash();
   }

   protected boolean isVoid()
   {
      return ((MethodByMethodInfo)info).getMethod().getReturnType().equals(Void.TYPE);
   }

   protected Class getReturnType()
   {
      if (isVoid()) return null;
      return ((MethodByMethodInfo)info).getMethod().getReturnType();
   }

   protected AdviceMethodProperties getAdviceMethodProperties(AdviceSetup setup)
   {
      Method method = ((MethodByMethodInfo)info).getMethod();
      if (hasTargetObject())
      {
         return new AdviceMethodProperties(
               setup.getAspectClass(),
               setup.getAdviceName(),
               info.getClass(),
               INVOCATION_TYPE,
               method.getReturnType(),
               method.getParameterTypes(),
               method.getExceptionTypes(),
               method.getDeclaringClass(),
               hasCallingObject()?
               Context.TARGET_CALLER_AVAILABLE:
               Context.TARGET_AVAILABLE);
      }
      return new AdviceMethodProperties(
            setup.getAspectClass(),
            setup.getAdviceName(),
            info.getClass(),
            INVOCATION_TYPE,
            method.getReturnType(),
            method.getParameterTypes(),
            method.getExceptionTypes(),
            null,
            hasCallingObject()?
                  Context.TARGET_CALLER_AVAILABLE:
                  Context.TARGET_AVAILABLE);
   }

   protected boolean isCaller()
   {
      return true;
   }

   protected boolean hasCallingObject()
   {
      return !java.lang.reflect.Modifier.isStatic(((MethodByMethodInfo)info).getCallingMethod().getModifiers());
   }

   protected boolean hasTargetObject()
   {
      return !java.lang.reflect.Modifier.isStatic(((MethodByMethodInfo)info).getMethod().getModifiers());
   }

   protected static CtClass createJoinpointBaseClass(
         GeneratedAdvisorInstrumentor instrumentor,
         long callingHash,
         boolean hasCallingObject,
         CtClass callingClass,
         CtMethod targetMethod,
         String classname,
         long calledHash,
         String ciname) throws NotFoundException, CannotCompileException
   {
      instrumentor.addJoinPointGeneratorFieldToGenAdvisor(
            getJoinPointGeneratorFieldName(callingHash, classname, calledHash));

      BaseClassGenerator generator = new BaseClassGenerator(instrumentor, callingClass, callingHash, hasCallingObject, classname, targetMethod, calledHash, ciname);
      return generator.generate();
   }

   protected String getJoinPointGeneratorFieldName()
   {
      return getJoinPointGeneratorFieldName(callingMethodHash(), calledClass(), calledMethodHash());
   }

   protected static String getInfoClassName(long callingHash, String classname, long calledHash)
   {
      return JOINPOINT_CLASS_PREFIX + CallerTransformer.getUniqueInvocationFieldname(callingHash, classname, calledHash);
   }

   protected static String getInfoFieldName(long callingHash, String classname, long calledHash)
   {
      return JOINPOINT_FIELD_PREFIX + CallerTransformer.getUniqueInvocationFieldname(callingHash, classname, calledHash);
   }

   protected static String getJoinPointGeneratorFieldName(long callingHash, String classname, long calledHash)
   {
      return GENERATOR_PREFIX + CallerTransformer.getUniqueInvocationFieldname(callingHash, classname, calledHash);
   }

   private static class BaseClassGenerator
   {
      GeneratedAdvisorInstrumentor instrumentor;
      CtClass callingClass;
      long callingHash;
      boolean hasCallingObject;
      String classname;
      CtClass targetClass;
      CtMethod targetMethod;
      long calledHash;
      String ciname;
      boolean hasTargetObject;

      CtClass jp;
      CtMethod invokeJoinpointMethod;
      CtConstructor publicConstructor;
      CtConstructor protectedConstructor;
      CtField targetField;
      CtClass[] params;
      CtClass methodInfoClass;

      BaseClassGenerator(
            GeneratedAdvisorInstrumentor instrumentor,
            CtClass callingClass,
            long callingHash,
            boolean hasCallingObject,
            String classname,
            CtMethod targetMethod,
            long calledHash,
            String ciname) throws NotFoundException
      {
         this.instrumentor = instrumentor;
         this.callingClass = callingClass;
         this.callingHash = callingHash;
         this.classname = classname;
         this.hasCallingObject = hasCallingObject;
         this.targetClass = instrumentor.forName(classname);
         this.targetMethod = targetMethod;
         this.calledHash = calledHash;
         this.ciname = ciname;
         this.params = targetMethod.getParameterTypes();
         methodInfoClass = instrumentor.forName(CallerTransformer.METHOD_BY_METHOD_INFO_CLASS_NAME);
         hasTargetObject = !Modifier.isStatic(targetMethod.getModifiers());
      }

      protected CtClass generate() throws CannotCompileException, NotFoundException
      {
         jp = setupClass();
         addArgumentsFieldsAndAccessors();
         if (hasTargetObject)
         {
            addTypedTargetField();
         }
         addInvokeJoinpointMethod();
         addMethodInfoField();
         addPublicConstructor();
         addProtectedConstructor();
         addDispatchMethods();

         TransformerCommon.compileOrLoadClass(callingClass, jp);
         return jp;
      }


      private CtClass setupClass()throws NotFoundException, CannotCompileException
      {
         String className = getInfoClassName(callingHash, targetClass.getName(), calledHash);

         //Create inner joinpoint class in advised class, super class is ConstructorInvocation
         jp = TransformerCommon.makeNestedClass(callingClass, className, true);
         int mod = jp.getModifiers();
         jp.setModifiers(mod | Modifier.PUBLIC);

         CtClass invocation = INVOCATION_CT_TYPE;
         jp.setSuperclass(invocation);
         addUntransformableInterface(instrumentor, jp);
         return jp;
      }

      private void addArgumentsFieldsAndAccessors() throws NotFoundException, CannotCompileException
      {
         OptimizedBehaviourInvocations.addArgumentFieldsToInvocation(jp, params);
         OptimizedBehaviourInvocations.addSetArguments(instrumentor.getClassPool(), jp, params);
         OptimizedBehaviourInvocations.addGetArguments(instrumentor.getClassPool(), jp, params);
      }

      private void addTypedTargetField()throws CannotCompileException
      {
         targetField = new CtField(targetClass, TARGET_FIELD, jp);
         jp.addField(targetField);
         targetField.setModifiers(Modifier.PROTECTED);
      }
      /**
       * This constructor is used by the advisor when we have regenerated the joinpoint.
       * This just creates a generic JoinPoint instance with no data specific to the
       * method call
       */
      private void addPublicConstructor() throws CannotCompileException
      {
         publicConstructor = CtNewConstructor.make(
               new CtClass[] {methodInfoClass},
               new CtClass[0],
               "{super($1, null, null, $1.getInterceptors()); this." + INFO_FIELD + " = $1;}",
               jp);

         jp.addConstructor(publicConstructor);
      }

      /**
       * This constructor will be called by invokeJoinpoint in the generated subclass when we need to
       * instantiate a joinpoint containing target and args
       */
      protected void addProtectedConstructor() throws CannotCompileException
      {
         int offset = 1;
         if (hasTargetObject) offset++;
         if (hasCallingObject) offset++;
         CtClass[] ctorParams = new CtClass[params.length + offset];

         int index = 0;
         ctorParams[index++] = jp;
         if (hasTargetObject) ctorParams[index++] = targetClass;
         if (hasCallingObject) ctorParams[index] = callingClass;
         System.arraycopy(params, 0, ctorParams, offset, params.length);

         StringBuffer body = new StringBuffer();
         body.append("{");
         body.append("   this($1." + INFO_FIELD + ");");
         if (hasTargetObject)
         {
            body.append("   super.targetObject=$2;");
            body.append("   this.tgt=$2;");
         }
         if (hasCallingObject) body.append("   super.callingObject=$" + (hasTargetObject ? 3 : 2) + ";");

         for (int i = offset ; i < ctorParams.length ; i++)
         {
            body.append("   arg" + (i - offset) + " = $" + (i + 1)  + ";");
         }

         body.append("}");
         protectedConstructor = CtNewConstructor.make(
               ctorParams,
               new CtClass[0],
               body.toString(),
               jp);
         protectedConstructor.setModifiers(Modifier.PROTECTED);

         jp.addConstructor(protectedConstructor);

      }

      private CtClass[] getInvokeJoinpointParameters()
      {
         if (!hasCallingObject && ! hasTargetObject)
         {
            return params;
         }

         int offset = 0;
         if (hasTargetObject) offset++;
         if (hasCallingObject) offset++;
         CtClass[] invokeParams = new CtClass[params.length + offset];

         int index = 0;
         if (hasTargetObject) invokeParams[index++] = targetClass;
         if (hasCallingObject) invokeParams[index++] = callingClass;
         System.arraycopy(params, 0, invokeParams, offset, params.length);

         return invokeParams;
      }
      /**
       * Add an empty invokeJoinpoint() method. This method will be overridden by generated subclasses,
       * when the interceptors are rebuilt
       */
      private CtMethod addInvokeJoinpointMethod() throws CannotCompileException, NotFoundException
      {
         invokeJoinpointMethod  = CtNewMethod.make(
               targetMethod.getReturnType(),
               INVOKE_JOINPOINT,
               getInvokeJoinpointParameters(),
               targetMethod.getExceptionTypes(),
               null,
               jp);

         invokeJoinpointMethod.setModifiers(Modifier.PROTECTED);
         jp.addMethod(invokeJoinpointMethod);
         return invokeJoinpointMethod;
       }

      private void addMethodInfoField()throws CannotCompileException
      {
         CtField infoField = new CtField(methodInfoClass, INFO_FIELD, jp);
         infoField.setModifiers(javassist.Modifier.PROTECTED);//Make visible to classes in child classloaders
         jp.addField(infoField);
      }

      private void addDispatchMethods() throws CannotCompileException, NotFoundException
      {
         addInvokeNextDispatchMethod();
         if (hasCallingObject || hasTargetObject || params.length > 0)
         {
            addInvokeJoinPointDispatchMethod();
         }
         
         addInvokeTargetMethod();
      }

      private void addInvokeNextDispatchMethod() throws CannotCompileException, NotFoundException
      {
         //This dispatch method will be called by the invokeNext() methods for around advice
         final boolean isVoid = targetMethod.getReturnType().equals(CtClass.voidType);

         StringBuffer parameters = new StringBuffer();
         for (int i = 0 ; i < params.length ; i++)
         {
            if (i > 0)parameters.append(", ");
            parameters.append("arg" + i);
         }

         StringBuffer body = new StringBuffer("{");

         if (hasTargetObject)
         {
            body.append(MethodExecutionTransformer.getAopReturnStr(isVoid) + TARGET_FIELD + "." + targetMethod.getName() + "(" + parameters + ");");
         }
         else
         {
            body.append(MethodExecutionTransformer.getReturnStr(isVoid) + targetClass.getName() + "." + targetMethod.getName() + "(" + parameters + ");");
         }

         body.append("}");
         try
         {
            CtMethod dispatch = CtNewMethod.make(
                  (isVoid) ? CtClass.voidType : targetMethod.getReturnType(),
                  JoinPointGenerator.DISPATCH,
                  EMPTY_CTCLASS_ARRAY,
                  targetMethod.getExceptionTypes(),
                  body.toString(),
                  jp);
            dispatch.setModifiers(Modifier.PROTECTED);
            jp.addMethod(dispatch);
         }
         catch (CannotCompileException e)
         {
            throw new RuntimeException("Could not compile code " + body + " for method " + getMethodString(jp, JoinPointGenerator.DISPATCH, EMPTY_CTCLASS_ARRAY), e);
         }
      }

      private void addInvokeJoinPointDispatchMethod() throws CannotCompileException, NotFoundException
      {
         //This dispatch method will be called by the invokeJoinPoint() method if the joinpoint has no around advices
         final boolean isVoid = targetMethod.getReturnType().equals(CtClass.voidType);
         CtClass[] invokeParams = getInvokeJoinpointParameters();

         final int offset = invokeParams.length - params.length;
         StringBuffer parameters = new StringBuffer();
         for (int i = 0 ; i < params.length ; i++)
         {
            if (i > 0)parameters.append(", ");
            parameters.append("$" + (i + offset + 1));
         }

         StringBuffer body = new StringBuffer("{");

         if (hasTargetObject)
         {
            body.append(MethodExecutionTransformer.getAopReturnStr(isVoid) + "$1." + targetMethod.getName() + "(" + parameters + ");");
         }
         else
         {
            body.append(MethodExecutionTransformer.getReturnStr(isVoid) + targetClass.getName() + "." + targetMethod.getName() + "(" + parameters + ");");
         }

         body.append("}");
         try
         {
            CtMethod dispatch = CtNewMethod.make(
                  (isVoid) ? CtClass.voidType : targetMethod.getReturnType(),
                  JoinPointGenerator.DISPATCH,
                  invokeParams,
                  targetMethod.getExceptionTypes(),
                  body.toString(),
                  jp);
            dispatch.setModifiers(Modifier.PROTECTED);
            jp.addMethod(dispatch);
         }
         catch (CannotCompileException e)
         {
            throw new RuntimeException("Could not compile code " + body + " for method " + getMethodString(jp, JoinPointGenerator.DISPATCH, invokeParams), e);
         }
      }

      private void addInvokeTargetMethod() throws CannotCompileException, NotFoundException
      {
         CtMethod template = INVOCATION_CT_TYPE.getDeclaredMethod(INVOKE_TARGET);
         
         boolean isVoid = targetMethod.getReturnType().equals(CtClass.voidType);
         String body = (isVoid) ? "{dispatch(); return null;}" : "{return ($w)dispatch();}";
         
         CtMethod invokeTarget = CtNewMethod.make(
               template.getReturnType(),
               template.getName(),
               template.getParameterTypes(),
               template.getExceptionTypes(),
               body,
               jp);
         jp.addMethod(invokeTarget);
      }
   }
}