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

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.jboss.aop.Advisor;
import org.jboss.aop.ConstructorInfo;
import org.jboss.aop.GeneratedClassAdvisor;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.advice.AdviceMethodProperties;
import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.util.ReflectToJavassist;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class ConstructorJoinPointGenerator extends JoinPointGenerator
{
   public static final String GENERATOR_PREFIX = JoinPointGenerator.GENERATOR_PREFIX + "constructor_";
   public static final String JOINPOINT_CLASS_PREFIX = JoinPointGenerator.JOINPOINT_CLASS_PREFIX + "constructor_";
   public static final String JOINPOINT_FIELD_PREFIX = JoinPointGenerator.JOINPOINT_FIELD_PREFIX + "constructor_";
   private static final Class INVOCATION_TYPE = ConstructorInvocation.class;
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


   public ConstructorJoinPointGenerator(GeneratedClassAdvisor advisor, JoinPointInfo info)
   {
      super(advisor, info, JoinPointParameters.ONLY_ARGS);
   }


   protected void initialiseJoinPointNames()
   {
      joinpointClassName = getInfoClassName(
               classSimpleName(),
               index());

      joinpointFieldName = getInfoFieldName(
               classSimpleName(),
               index());
   }

   private String classSimpleName()
   {
      Constructor ctor = ((ConstructorInfo)info).getConstructor();
      return Advisor.getSimpleName(ctor.getDeclaringClass());
   }

   private int index()
   {
      return ((ConstructorInfo)info).getIndex();
   }

   protected boolean isVoid()
   {
      return false;
   }

   protected Class getReturnType()
   {
      return ((ConstructorInfo)super.info).getConstructor().getDeclaringClass();
   }

   protected AdviceMethodProperties getAdviceMethodProperties(AdviceSetup setup)
   {
      Constructor ctor = ((ConstructorInfo)info).getConstructor();
      return new AdviceMethodProperties(
            setup.getAspectClass(),
            setup.getAdviceName(),
            info.getClass(),
            INVOCATION_TYPE,
            ctor.getDeclaringClass(),
            ctor.getParameterTypes(),
            ctor.getExceptionTypes());
   }

   protected boolean hasTargetObject()
   {
      return false;
   }

   protected static CtClass createJoinpointBaseClass(
         GeneratedAdvisorInstrumentor instrumentor,
         CtClass advisedClass,
         CtConstructor advisedCtor,
         String ciname,
         int index)throws NotFoundException, CannotCompileException
   {
      instrumentor.addJoinPointGeneratorFieldToGenAdvisor(
            getJoinPointGeneratorFieldName(advisedClass.getSimpleName(), index));

      BaseClassGenerator generator = new BaseClassGenerator(instrumentor, advisedClass, advisedCtor, ciname, index);
      return generator.generate();
   }

   protected String getJoinPointGeneratorFieldName()
   {
      return getJoinPointGeneratorFieldName(classSimpleName(), index());
   }

   protected static String getInfoFieldName(String className, int index)
   {
      return JOINPOINT_FIELD_PREFIX + className + "_" + index;
   }

   private static String getInfoClassName(String className, int index)
   {
      return JOINPOINT_CLASS_PREFIX + className + "_" + index;
   }

   protected static String getJoinPointGeneratorFieldName(String className, int index)
   {
      return GENERATOR_PREFIX + className + "_" + index;
   }

   private static class BaseClassGenerator
   {
      GeneratedAdvisorInstrumentor instrumentor;
      CtClass advisedClass;
      CtConstructor advisedCtor;
      String ciname;
      int index;

      CtClass jp;
      CtMethod invokeJoinpointMethod;
      CtConstructor publicConstructor;
      CtConstructor protectedConstructor;
      CtField targetField;
      CtClass[] params;
      CtClass constructorInfoClass;

      BaseClassGenerator(
            GeneratedAdvisorInstrumentor instrumentor,
            CtClass advisedClass,
            CtConstructor advisedCtor,
            String ciname,
            int index) throws NotFoundException
      {
         this.instrumentor = instrumentor;
         this.advisedClass = advisedClass;
         this.advisedCtor = advisedCtor;
         this.ciname = ciname;
         this.index = index;
         this.params = advisedCtor.getParameterTypes();
         constructorInfoClass = instrumentor.forName(ConstructorExecutionTransformer.CONSTRUCTOR_INFO_CLASS_NAME);
      }

      protected CtClass generate() throws CannotCompileException, NotFoundException
      {
         jp = setupClass();
         addArgumentsFieldsAndAccessors();
         addInvokeJoinpointMethod();
         addMethodInfoField();
         addPublicConstructor();
         addProtectedConstructor();
         addDispatchMethods();

         TransformerCommon.compileOrLoadClass(advisedClass, jp);
         return jp;
      }


      private CtClass setupClass()throws NotFoundException, CannotCompileException
      {
         String className = getInfoClassName(advisedClass.getSimpleName(), index);

         //Create inner joinpoint class in advised class, super class is ConstructorInvocation
         jp = TransformerCommon.makeNestedClass(advisedClass, className, true, Modifier.PUBLIC | Modifier.STATIC, INVOCATION_CT_TYPE);
         addUntransformableInterface(instrumentor, jp);
         return jp;
      }

      private void addArgumentsFieldsAndAccessors() throws NotFoundException, CannotCompileException
      {
         OptimizedBehaviourInvocations.addArgumentFieldsToInvocation(jp, params);
         OptimizedBehaviourInvocations.addSetArguments(instrumentor.getClassPool(), jp, params);
         OptimizedBehaviourInvocations.addGetArguments(instrumentor.getClassPool(), jp, params);
      }

      /**
       * This constructor is used by the advisor when we have regenerated the joinpoint.
       * This just creates a generic JoinPoint instance with no data specific to the
       * method call
       */
      private void addPublicConstructor() throws CannotCompileException
      {
         publicConstructor = CtNewConstructor.make(
               new CtClass[] {constructorInfoClass},
               new CtClass[0],
               "{super($1, $1.getInterceptors()); this." + INFO_FIELD + " = $1;}",
               jp);

         jp.addConstructor(publicConstructor);
      }

      /**
       * This constructor will be called by invokeJoinpoint in the generated subclass when we need to
       * instantiate a joinpoint containing target and args
       */
      protected void addProtectedConstructor() throws CannotCompileException
      {
         CtClass[] ctorParams = new CtClass[params.length + 1];
         ctorParams[0] = jp;
         System.arraycopy(params, 0, ctorParams, 1, params.length);

         StringBuffer body = new StringBuffer();
         body.append("{");
         body.append("   this($1." + INFO_FIELD + ");");

         int offset = 1;
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

      /**
       * Add an empty invokeJoinpoint() method. This method will be overridden by generated subclasses,
       * when the interceptors are rebuilt
       */
      private CtMethod addInvokeJoinpointMethod() throws CannotCompileException, NotFoundException
      {
         invokeJoinpointMethod  = CtNewMethod.make(
               advisedClass,
               INVOKE_JOINPOINT,
               params,
               advisedCtor.getExceptionTypes(),
               null,
               jp);
         invokeJoinpointMethod.setModifiers(Modifier.PROTECTED);
         jp.addMethod(invokeJoinpointMethod);
         return invokeJoinpointMethod;
       }

      private void addMethodInfoField()throws CannotCompileException
      {
         CtField infoField = new CtField(constructorInfoClass, INFO_FIELD, jp);
         infoField.setModifiers(javassist.Modifier.PROTECTED);//Make visible to classes in child classloaders
         jp.addField(infoField);
      }

      private void addDispatchMethods() throws CannotCompileException, NotFoundException
      {
         addInvokeNextDispatchMethod();

         if (params.length > 0)
         {
            addInvokeJoinPointDispatchMethod();
         }
         
         addInvokeTargetMethod();
      }

      private void addInvokeNextDispatchMethod() throws CannotCompileException, NotFoundException
      {
         //This dispatch method will be called by the invokeNext() methods for around advice
         StringBuffer parameters = new StringBuffer("(");
         for (int i = 0 ; i < params.length ; i++)
         {
            if (i > 0)parameters.append(", ");
            parameters.append("arg" + i);
         }
         parameters.append(")");

         String body =
            "{" +
            "   " + advisedClass.getName() + " obj = new " + advisedClass.getName() + parameters + ";" +
            "   setTargetObject(obj);" +
            "   return obj;" +
            "}";

         try
         {
            CtMethod dispatch = CtNewMethod.make(
                  advisedClass,
                  JoinPointGenerator.DISPATCH,
                  new CtClass[0],
                  advisedCtor.getExceptionTypes(),
                  body,
                  jp);
            dispatch.setModifiers(Modifier.PROTECTED);
            jp.addMethod(dispatch);
         }
         catch (CannotCompileException e)
         {
            throw new RuntimeException("Could not compile code " + body + " for method " + getMethodString(jp, JoinPointGenerator.DISPATCH, params), e);
         }
      }

      private void addInvokeJoinPointDispatchMethod() throws CannotCompileException, NotFoundException
      {
         //This dispatch method will be called by the invokeJoinPoint() method if the joinpoint has no around advices

         String body =
            "{" +
            "   " + advisedClass.getName() + " obj = new " + advisedClass.getName() + "($$);" +
            "   setTargetObject(obj);" +
            "   return obj;" +
            "}";

         try
         {
            CtMethod dispatch = CtNewMethod.make(
                  advisedClass,
                  JoinPointGenerator.DISPATCH,
                  params,
                  advisedCtor.getExceptionTypes(),
                  body,
                  jp);
            dispatch.setModifiers(Modifier.PROTECTED);
            jp.addMethod(dispatch);
         }
         catch (CannotCompileException e)
         {
            throw new RuntimeException("Could not compile code " + body + " for method " + getMethodString(jp, JoinPointGenerator.DISPATCH, params), e);
         }
      }
      
      private void addInvokeTargetMethod() throws CannotCompileException, NotFoundException
      {
         CtMethod template = INVOCATION_CT_TYPE.getDeclaredMethod(INVOKE_TARGET);
         
         String body = "{return dispatch();}";
         
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
