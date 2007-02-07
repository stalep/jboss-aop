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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.jboss.aop.FieldInfo;
import org.jboss.aop.GeneratedClassAdvisor;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.advice.AdviceMethodProperties;
import org.jboss.aop.joinpoint.FieldReadInvocation;
import org.jboss.aop.joinpoint.FieldWriteInvocation;
import org.jboss.aop.util.JavassistToReflect;
import org.jboss.aop.util.ReflectToJavassist;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class FieldJoinPointGenerator extends JoinPointGenerator
{
   public static final String WRITE_GENERATOR_PREFIX = GENERATOR_PREFIX + "w_";
   public static final String READ_GENERATOR_PREFIX = GENERATOR_PREFIX + "r_";
   public static final String WRITE_JOINPOINT_FIELD_PREFIX = JOINPOINT_FIELD_PREFIX + "w_";
   public static final String READ_JOINPOINT_FIELD_PREFIX = JOINPOINT_FIELD_PREFIX + "r_";
   public static final String WRITE_JOINPOINT_CLASS_PREFIX = JOINPOINT_CLASS_PREFIX + "w_";
   public static final String READ_JOINPOINT_CLASS_PREFIX = JOINPOINT_CLASS_PREFIX + "r_";
   private static final Class READ_INVOCATION_TYPE = FieldReadInvocation.class;
   private static final Class WRITE_INVOCATION_TYPE = FieldWriteInvocation.class;
   private static final CtClass READ_INVOCATION_CT_TYPE;
   private static final CtClass WRITE_INVOCATION_CT_TYPE;
   static
   {
      try
      {
         READ_INVOCATION_CT_TYPE = ReflectToJavassist.classToJavassist(READ_INVOCATION_TYPE);
         WRITE_INVOCATION_CT_TYPE = ReflectToJavassist.classToJavassist(WRITE_INVOCATION_TYPE);
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   public FieldJoinPointGenerator(GeneratedClassAdvisor advisor, JoinPointInfo info)
   {
      super(advisor, info, getParameters((FieldInfo) info),
            ((FieldInfo) info).isRead()? 0: 1);
   }

   private static JoinPointParameters getParameters(FieldInfo info)
   {
      if (Modifier.isStatic(info.getAdvisedField().getModifiers()))
      {
         return JoinPointParameters.ONLY_ARGS;
      }
      return JoinPointParameters.TARGET_ARGS;
   }
   
   protected void initialiseJoinPointNames()
   {
      joinpointClassName =
         getInfoClassName(fieldName(), read());

      joinpointFieldName =
         getInfoFieldName(fieldName(), read());
   }

   private String fieldName()
   {
      return ((FieldInfo)info).getAdvisedField().getName();
   }

   private boolean read()
   {
      return ((FieldInfo)info).isRead();
   }

   protected boolean isVoid()
   {
      return !((FieldInfo)info).isRead();
   }

   protected Class getReturnType()
   {
      if (!read())
      {
         return null;
      }
      return ((FieldInfo)super.info).getAdvisedField().getType();
   }

   protected AdviceMethodProperties getAdviceMethodProperties(AdviceSetup setup)
   {
      Field field = ((FieldInfo)info).getAdvisedField();
      return new AdviceMethodProperties(
               setup.getAspectClass(),
               setup.getAdviceName(),
               info.getClass(),
               (read()) ? READ_INVOCATION_TYPE : WRITE_INVOCATION_TYPE,
               (read()) ? getReturnType() : Void.TYPE,
               (read()) ? new Class[] {} : new Class[] {field.getType()},
               null,
               field.getDeclaringClass(),
               hasTargetObject());
   }

   protected CtClass[] getJoinpointParameters() throws NotFoundException
   {
      if (isVoid()) return new CtClass[0];

      CtClass type = ReflectToJavassist.fieldToJavassist(((FieldInfo)super.info).getAdvisedField()).getType();
      return new CtClass[] {type};
   }

   protected boolean hasTargetObject()
   {
      return !Modifier.isStatic(((FieldInfo)info).getAdvisedField().getModifiers());
   }

   protected String getJoinPointGeneratorFieldName()
   {
      return getJoinPointGeneratorFieldName(fieldName(), read());
   }

   protected static String getInfoFieldName(String fieldName, boolean read)
   {
      if (read)
      {
         return READ_JOINPOINT_FIELD_PREFIX + fieldName;
      }
      else
      {
         return WRITE_JOINPOINT_FIELD_PREFIX + fieldName;
      }
   }

   private static String getInfoClassName(String fieldName, boolean read)
   {
      if (read)
      {
         return READ_JOINPOINT_CLASS_PREFIX + fieldName;
      }
      else
      {
         return WRITE_JOINPOINT_CLASS_PREFIX + fieldName;
      }
   }

   public static String getJoinPointGeneratorFieldName(String fieldName, boolean read)
   {
      if (read)
      {
         return READ_GENERATOR_PREFIX + fieldName;
      }
      else
      {
         return WRITE_GENERATOR_PREFIX + fieldName;
      }
   }

   protected static CtClass createReadJoinpointBaseClass(
         GeneratedAdvisorInstrumentor instrumentor,
         CtClass advisedClass,
         CtField advisedField,
         String finame,
         int index)throws NotFoundException, CannotCompileException
   {
      instrumentor.addJoinPointGeneratorFieldToGenAdvisor(
            getJoinPointGeneratorFieldName(advisedField.getName(), true));

      BaseClassGenerator factory = new ReadBaseClassGenerator(instrumentor, advisedClass, advisedField, finame, index);
      return factory.generate();
   }

   protected static CtClass createWriteJoinpointBaseClass(
         GeneratedAdvisorInstrumentor instrumentor,
         CtClass advisedClass,
         CtField advisedField,
         String finame,
         int index)throws NotFoundException, CannotCompileException
   {
      instrumentor.addJoinPointGeneratorFieldToGenAdvisor(
            getJoinPointGeneratorFieldName(advisedField.getName(), false));

      BaseClassGenerator factory = new WriteBaseClassGenerator(instrumentor, advisedClass, advisedField, finame, index);
      return factory.generate();
   }


   static abstract class BaseClassGenerator
   {
      GeneratedAdvisorInstrumentor instrumentor;
      CtClass advisedClass;
      CtField advisedField;
      String finame;
      boolean hasTargetObject;

      CtClass jp;
      CtClass fieldType;
      CtClass fieldInfoClass;
      boolean read;

      BaseClassGenerator(GeneratedAdvisorInstrumentor instrumentor,  CtClass advisedClass,
                        CtField advisedField, String finame, int index, boolean read) throws NotFoundException
      {
         this.instrumentor = instrumentor;
         this.advisedClass = advisedClass;
         this.advisedField = advisedField;
         this.finame = finame;
         this.fieldType = advisedField.getType();
         this.read = read;
         fieldInfoClass = instrumentor.forName(FieldAccessTransformer.FIELD_INFO_CLASS_NAME);
         hasTargetObject = !Modifier.isStatic(advisedField.getModifiers());
      }

      protected CtClass generate() throws CannotCompileException, NotFoundException
      {
         jp = setupClass();
         if (hasTargetObject)
         {
            addTypedTargetField();
         }
         addArgumentFieldAndAccessor();
         addInvokeJoinpointMethod();
         addFieldInfoField();
         addPublicConstructor();
         addProtectedConstructors();
         addDispatchMethods();

         TransformerCommon.compileOrLoadClass(advisedClass, jp);
         return jp;
      }

      protected void addArgumentFieldAndAccessor() throws CannotCompileException, NotFoundException
      {
         CtField argumentsField = new CtField(
               instrumentor.forName("java.lang.Object[]"), ARGUMENTS, jp);
         argumentsField.setModifiers(Modifier.PROTECTED);
         jp.addField(argumentsField);
         CtMethod getArguments = CtNewMethod.make(createGetArgumentsBody(), jp);
         getArguments.setModifiers(Modifier.PUBLIC);
         jp.addMethod(getArguments);
         CtMethod setArguments = CtNewMethod.make(createSetArgumentsBody(), jp);
         setArguments.setModifiers(Modifier.PUBLIC);
         jp.addMethod(setArguments);
      }

      protected abstract String createGetArgumentsBody();
      protected abstract String createSetArgumentsBody();
      
      private static String debugFields(CtClass clazz) throws NotFoundException
      {
         StringBuffer sb = new StringBuffer();
         sb.append(clazz.getName());
         CtField[] fields = clazz.getFields();
         for (int i = 0 ; i < fields.length ; i++)
         {
            sb.append("\n\t\t\t\t" + Modifier.toString(fields[i].getModifiers()) + " " + fields[i].getName() + " " + fields[i].getType());
         }
         
         return sb.toString();
      }

      private CtClass setupClass()throws NotFoundException, CannotCompileException
      {
         String className = getInfoClassName(advisedField.getName(), read);

         //Create inner joinpoint class in advised class, super class is
         CtClass superClass = (read) ? READ_INVOCATION_CT_TYPE : WRITE_INVOCATION_CT_TYPE;
         jp = TransformerCommon.makeNestedClass(advisedClass, className, true, Modifier.PUBLIC | Modifier.STATIC, superClass);
         addUntransformableInterface(instrumentor, jp);
         return jp;
      }

      protected abstract CtClass getSuperClass() throws NotFoundException;

      private void addTypedTargetField()throws CannotCompileException
      {
         CtField targetField = new CtField(advisedClass, TARGET_FIELD, jp);
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
         CtConstructor publicConstructor = CtNewConstructor.make(
               new CtClass[] {fieldInfoClass},
               new CtClass[0],
               "{super($1, $1.getInterceptors()); this." + INFO_FIELD + " = $1;}",
               jp);

         jp.addConstructor(publicConstructor);
      }

      /**
       * These constructors will be called by invokeJoinpoint in the generated
       * subclass when we need to instantiate a joinpoint containing target and args
       */
      protected void addProtectedConstructors() throws CannotCompileException, NotFoundException
      {
         CtClass[] ctorParams1 = (hasTargetObject) ? new CtClass[2] : new CtClass[1];
         CtClass[] ctorParams2 = (hasTargetObject) ? new CtClass[3] : new CtClass[2];
         
         ctorParams1[0] = ctorParams2[0] = jp;
         if (hasTargetObject) ctorParams1[1] = ctorParams2[1] = advisedClass;
         ctorParams2[ctorParams2.length - 1] = getArgumentType();
         
         StringBuffer body = new StringBuffer();
         body.append("{");
         body.append("   this($1." + INFO_FIELD + ");");

         if (hasTargetObject)
         {
            body.append("   this." + TARGET_FIELD + " = $2;");
            body.append("   super.setTargetObject($2);");
         }
         
         if (getArgumentType() != null)
         {
            CtConstructor protectedConstructor = CtNewConstructor.make(
                  ctorParams1,
                  new CtClass[0],
                  body.toString() + "}",
                  jp);
            protectedConstructor.setModifiers(Modifier.PROTECTED);
            jp.addConstructor(protectedConstructor);
         }
         else
         {
            ctorParams2 = ctorParams1;
         }
         String setArguments = createSetValue();
         CtConstructor protectedConstructor = CtNewConstructor.make(
               ctorParams2,
               new CtClass[0],
               body.toString() + setArguments + "}",
               jp);
         protectedConstructor.setModifiers(Modifier.PROTECTED);
         jp.addConstructor(protectedConstructor);
         
         
      }

      protected abstract CtClass getArgumentType() throws NotFoundException;
      protected abstract String createSetValue();
      protected abstract CtClass[] getInvokeJoinPointParams() throws NotFoundException;
      
      /**
       * Add an empty invokeJoinpoint() method. This method will be overridden by generated subclasses,
       * when the interceptors are rebuilt
       */
      protected abstract CtMethod addInvokeJoinpointMethod() throws CannotCompileException, NotFoundException;

      private void addFieldInfoField()throws CannotCompileException
      {
         CtField infoField = new CtField(fieldInfoClass, INFO_FIELD, jp);
         infoField.setModifiers(javassist.Modifier.PROTECTED);//Make visible to classes in child classloaders
         jp.addField(infoField);
      }

      private void addDispatchMethods() throws CannotCompileException, NotFoundException
      {
         addInvokeNextDispatchMethod();
         addInvokeJoinPointDispatchMethod();
         addInvokeTargetMethod();
      }

      private void addInvokeNextDispatchMethod() throws CannotCompileException, NotFoundException
      {
         //This dispatch method will be called by the invokeNext() methods for around advice

         String body = createInvokeNextDispatchMethodBody();

         try
         {
            CtMethod dispatch = CtNewMethod.make(
                  (read) ? advisedField.getType() : CtClass.voidType,
                  JoinPointGenerator.DISPATCH,
                  EMPTY_CTCLASS_ARRAY,
                  EMPTY_CTCLASS_ARRAY,
                  body,
                  jp);
            dispatch.setModifiers(Modifier.PROTECTED);
            jp.addMethod(dispatch);
         }
         catch (CannotCompileException e)
         {
            throw new RuntimeException("Could not compile code " + body + " for method " + getMethodString(jp, JoinPointGenerator.DISPATCH, EMPTY_CTCLASS_ARRAY), e);
         }
      }

      protected abstract String createInvokeNextDispatchMethodBody() throws NotFoundException;

      protected void addInvokeJoinPointDispatchMethod() throws NotFoundException, CannotCompileException
      {
         CtClass[] params = getInvokeJoinPointParams();
         if (params.length == 0)
         {
            return;
         }

         //This dispatch method will be called by the invokeJoinPoint() method if the joinpoint has no around advices

         String body = createInvokeJoinPointDispatchMethodBody();

         try
         {
            CtMethod dispatch = CtNewMethod.make(
                  (read) ? advisedField.getType() : CtClass.voidType,
                  JoinPointGenerator.DISPATCH,
                  params,
                  new CtClass[0],
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

      protected abstract String createInvokeJoinPointDispatchMethodBody() throws NotFoundException;
      
      private void addInvokeTargetMethod() throws CannotCompileException, NotFoundException
      {
         CtMethod template = READ_INVOCATION_CT_TYPE.getDeclaredMethod(INVOKE_TARGET);
         
         String body = (read) ? "{return ($w)dispatch();}" : "{dispatch(); return null;}" ;
         
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

   private static class ReadBaseClassGenerator extends BaseClassGenerator
   {
      ReadBaseClassGenerator(GeneratedAdvisorInstrumentor instrumentor,  CtClass advisedClass,
            CtField advisedField, String finame, int index) throws NotFoundException
      {
         super(instrumentor, advisedClass, advisedField, finame, index, true);
      }

      protected CtClass getSuperClass() throws NotFoundException
      {
         return READ_INVOCATION_CT_TYPE;
      }

      protected CtClass getArgumentType()
      {
         return null;
      }
      
      protected String createSetValue()
      {
         return "";
      }
      
      protected CtClass[] getInvokeJoinPointParams() throws NotFoundException
      {
         return (hasTargetObject) ? new CtClass[] {advisedClass} : new CtClass[0];
      }

      protected CtMethod addInvokeJoinpointMethod() throws CannotCompileException, NotFoundException
      {
         CtMethod invokeJoinpointMethod  = CtNewMethod.make(
               advisedField.getType(),
               INVOKE_JOINPOINT,
               getInvokeJoinPointParams(),
               new CtClass[0],
               null,
               jp);
         invokeJoinpointMethod.setModifiers(Modifier.PROTECTED);
         jp.addMethod(invokeJoinpointMethod);

         return invokeJoinpointMethod;
      }

      protected String createInvokeNextDispatchMethodBody()
      {
         return (hasTargetObject) ?
            "{return "  + TARGET_FIELD + "." + advisedField.getName() + ";}" :
            "{return " + advisedClass.getName() + "." + advisedField.getName() + ";}";
      }

      protected String createInvokeJoinPointDispatchMethodBody()
      {
         return (hasTargetObject) ?
            "{return "  + "$1." + advisedField.getName() + ";}" :
            "{return " + advisedClass.getName() + "." + advisedField.getName() + ";}";
      }
      
      protected String createGetArgumentsBody()
      {
         StringBuffer code = new StringBuffer("public java.lang.Object[] getArguments()");
         code.append("{ ");
         code.append("   if(");
         code.append(ARGUMENTS);
         code.append("  == null)");
         code.append("   {");
         code.append("      ");
         code.append(ARGUMENTS);
         code.append(" = new java.lang.Object[0];");
         code.append("   }");
         code.append("   return ");
         code.append(ARGUMENTS);
         code.append("; ");
         code.append("}");
         return code.toString();
      }
      
      protected String createSetArgumentsBody()
      {
         StringBuffer code = new StringBuffer(
         "public void setArguments(java.lang.Object[] args)");
         code.append("{   ");
         code.append(ARGUMENTS);
         code.append("=args;");
         code.append("}");
         return code.toString();
      }
   }

   private static class WriteBaseClassGenerator extends BaseClassGenerator
   {
      WriteBaseClassGenerator(GeneratedAdvisorInstrumentor instrumentor,  CtClass advisedClass,
            CtField advisedField, String finame, int index) throws NotFoundException
      {
         super(instrumentor, advisedClass, advisedField, finame, index, false);
      }

      protected CtClass getSuperClass() throws NotFoundException
      {
         return WRITE_INVOCATION_CT_TYPE;
      }
      
      protected CtClass getArgumentType() throws NotFoundException
      {
         return advisedField.getType();
      }

      protected String createSetValue()
      {
         if (hasTargetObject)
         {
            return "   super.value = ($w)$3;";
         }
         return "   super.value = ($w)$2;";
      }

      protected CtClass[] getInvokeJoinPointParams() throws NotFoundException
      {
         return (hasTargetObject) ? new CtClass[] {advisedClass, advisedField.getType()} : new CtClass[] {advisedField.getType()};
      }

      protected CtMethod addInvokeJoinpointMethod() throws CannotCompileException, NotFoundException
      {
         CtMethod invokeJoinpointMethod  = CtNewMethod.make(
               CtClass.voidType,
               JoinPointGenerator.INVOKE_JOINPOINT,
               getInvokeJoinPointParams(),
               new CtClass[0],
               null,
               jp);
         invokeJoinpointMethod.setModifiers(Modifier.PROTECTED);
         jp.addMethod(invokeJoinpointMethod);

         return invokeJoinpointMethod;
      }

      protected String createInvokeNextDispatchMethodBody() throws NotFoundException
      {
         CtClass type = advisedField.getType();
         String value = JavassistToReflect.castInvocationValueToTypeString(type) + ";";

         return
            "{" +
            ((hasTargetObject) ?
                  TARGET_FIELD + "." + advisedField.getName() + " = " +  value:
                     advisedClass.getName() + "." + advisedField.getName() + " = " +  value) +

            ((hasTargetObject) ?
                  "; return "  + TARGET_FIELD + "." + advisedField.getName() + ";" :
                     "; return " + advisedClass.getName() + "." + advisedField.getName() + ";") +
            "}";
      }

      protected String createInvokeJoinPointDispatchMethodBody()
      {
         return (hasTargetObject) ?
            "{$1." + advisedField.getName() + " = $2;}" :
            "{" + advisedClass.getName() + "." + advisedField.getName() + " = $1;}";
      }
      
      protected String createGetArgumentsBody()
      {
         StringBuffer code = new StringBuffer("public java.lang.Object[] getArguments()");
         code.append("{ ");
         code.append("   if(");
         code.append(ARGUMENTS);
         code.append("  == null)");
         code.append("   {");
         code.append("      ");
         code.append(ARGUMENTS);
         code.append(" = new java.lang.Object[]{super.value};");
         code.append("   }");
         code.append("   return ");
         code.append(ARGUMENTS);
         code.append("; ");
         code.append("}");
         return code.toString();
      }
      
      protected String createSetArgumentsBody()
      {
         StringBuffer code = new StringBuffer(
         "public void setArguments(java.lang.Object[] args)");
         code.append("{   ");
         code.append(ARGUMENTS);
         code.append("=args;");
         code.append("   super.value=args[0];");
         code.append("}");
         return code.toString();
      }
   }
}
