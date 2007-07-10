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

import org.jboss.aop.ClassAdvisor;
import org.jboss.aop.util.JavassistMethodHashing;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * Used with GeneratedAdvisorInstrumentor
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class GeneratedAdvisorMethodExecutionTransformer extends
      MethodExecutionTransformer
{
   public GeneratedAdvisorMethodExecutionTransformer(GeneratedAdvisorInstrumentor instrumentor)
   {
      super(instrumentor);
   }

   private String addMethodInfoFieldToGenAdvisor(MethodTransformation trans)throws NotFoundException, CannotCompileException
   {
      GeneratedAdvisorInstrumentor instrumentor = (GeneratedAdvisorInstrumentor)trans.getInstrumentor();
      CtClass genadvisor = instrumentor.getGenadvisor();
      String miname = addMethodInfoField(
            Modifier.PROTECTED,
            genadvisor,
            trans);

      addJoinpoint(miname, trans);

      instrumentor.initaliseMethodInfo(miname, trans.getHash(), JavassistMethodHashing.methodHash(trans.getMethod()));
      return miname;
   }

   protected boolean addInfoAsWeakReference()
   {
      return false;
   }

   public static String getJoinPointFieldName(MethodTransformation trans)
   {
      return MethodJoinPointGenerator.getGeneratedJoinPointFieldName(trans.getOriginalName(), trans.getHash());
   }

   private void addJoinpoint(String miname, MethodTransformation trans)throws CannotCompileException, NotFoundException
   {
      CtClass joinpoint = createJoinpointClass(miname, trans);
      CtClass genadvisor = ((GeneratedAdvisorInstrumentor)trans.getInstrumentor()).getGenadvisor();
      CtField field = new CtField(
            joinpoint,
            getJoinPointFieldName(trans),
            genadvisor);
      field.setModifiers(Modifier.PROTECTED);
      genadvisor.addField(field);
   }

   private CtClass createJoinpointClass(String miname, MethodTransformation trans) throws CannotCompileException, NotFoundException
   {
      return MethodJoinPointGenerator.createJoinpointBaseClass(
            (GeneratedAdvisorInstrumentor)trans.getInstrumentor(),
            trans.getClazz(),
            trans.getMethod(),
            trans.getWMethod(),
            miname,
            trans.getOriginalName(),
            trans.getWrappedName(),
            trans.getHash());
   }

   public CtMethod addMixinWrappersAndInfo(
         GeneratedAdvisorInstrumentor instrumentor,
         CtClass clazz,
         CtClass mixinClass,
         String initializer,
         CtClass genadvisor,
         CtMethod mixinMethod) throws CannotCompileException, NotFoundException
   {
      String originalName = mixinMethod.getName();
      String originalBody =
         "{" +
         "   " + getReturnStr(mixinMethod) + " " + Instrumentor.mixinFieldName(mixinClass) + "." + mixinMethod.getName() + "($$);" +
         "}";

      CtMethod original = CtNewMethod.make(
            Modifier.PUBLIC,
            mixinMethod.getReturnType(),
            mixinMethod.getName(),
            mixinMethod.getParameterTypes(),
            mixinMethod.getExceptionTypes(),
            originalBody,
            clazz);
      clazz.addMethod(original);
      long hash = JavassistMethodHashing.methodHash(original);
      moveAnnotationsAndCopySignature(mixinMethod, original);

      String wrappedName = ClassAdvisor.notAdvisedMethodName(clazz.getName(), originalName);
      CtMethod wmethod = CtNewMethod.copy(original, clazz, null);

      wmethod.setName(wrappedName);
      clazz.addMethod(wmethod);
      moveAnnotationsAndCopySignature(original, wmethod);

      original.setName(wrappedName);
      wmethod.setName(originalName);

      MethodTransformation trans = new MethodTransformation(instrumentor, clazz, original, originalName, wmethod, wrappedName, hash);

      String methodInfoField = addMethodInfoFieldToGenAdvisor(trans);
      addMethodToGeneratedAdvisor(trans, methodInfoField);

      String wrapperBody =
         "{" +
         "   if (" + Instrumentor.mixinFieldName(mixinClass) + " == null)" +
         "   {" +
         "      " + Instrumentor.mixinFieldName(mixinClass) + " = " + initializer + ";" +
         "   }" +
         "   " + getReturnStr(trans.getMethod()) + " ((" + GeneratedAdvisorInstrumentor.getAdvisorFQN(trans.getClazz()) + ")" + GeneratedAdvisorInstrumentor.GET_CURRENT_ADVISOR + ")." + getAdvisorMethodName(trans) + "(this,$$);" +
         "}";
      wmethod.setBody(wrapperBody);
      return wmethod;
   }

   protected void transformMethod(MethodTransformation trans, boolean wrap)
         throws CannotCompileException, NotFoundException
   {
      // generate Wrapper
      String wrappedName = ClassAdvisor.notAdvisedMethodName(trans.getClazzName(),
                                                             trans.getMethod().getName());
      CtMethod wmethod = CtNewMethod.copy(trans.getMethod(), trans.getClazz(), null);

      String originalName = trans.getOriginalName();
      wmethod.setName(wrappedName);
      trans.getClazz().addMethod(wmethod);
      moveAnnotationsAndCopySignature(trans.getMethod(), wmethod);
      trans.getMethod().setName(wrappedName);
      wmethod.setName(originalName);

      trans.setWMethod(wmethod, wrappedName);

      String methodInfoField = addMethodInfoFieldToGenAdvisor(trans);
      addMethodToGeneratedAdvisor(trans, methodInfoField);

      // prepareForWrapping
      getWrapper().prepareForWrapping(wmethod, WrapperTransformer.SINGLE_TRANSFORMATION_INDEX);


      if (wrap)
      {
         // wrap
         getWrapper().wrap(wmethod, WrapperTransformer.SINGLE_TRANSFORMATION_INDEX);

         // executeWrapping
         setWrapperBody(trans, methodInfoField);
      }
   }

   protected void doWrap(MethodTransformation trans, String methodInfoFieldName) throws NotFoundException,
         Exception
   {
      // TODO Auto-generated method stub

   }

   private void setWrapperBody(MethodTransformation trans, String methodInfoField) throws NotFoundException
   {
      String code = null;
      final String className =
         GeneratedAdvisorInstrumentor.getAdvisorFQN(trans.getClazz());

      try
      {
         if (Modifier.isStatic(trans.getMethod().getModifiers()))
         {
            code =
               "{" +
               "   " + getReturnStr(trans.getMethod()) + " ((" + className + ")" + Instrumentor.HELPER_FIELD_NAME + ")." + getAdvisorMethodName(trans) + "($$);" +
               "}";
            trans.setWMethodBody(code);
         }
         else
         {
            code =
               "{" +
               "   " + getReturnStr(trans.getMethod()) + " ((" + className + ")" + GeneratedAdvisorInstrumentor.GET_CURRENT_ADVISOR + ")." + getAdvisorMethodName(trans) + "(this,$$);" +
               "}";

            trans.setWMethodBody(code);
         }
      }
      catch (CannotCompileException e)
      {
         e.printStackTrace();
         throw new RuntimeException("code was: " + code + " for method " + trans.getOriginalName());
      }
   }
   
   protected static CtClass[] addTargetToParamsForNonStaticMethod(CtClass outer, CtMethod method)throws NotFoundException
   {
      CtClass[] params = method.getParameterTypes();

      if (!Modifier.isStatic(method.getModifiers()))
      {
         CtClass[] tempParams = params;
         params = new CtClass[params.length + 1];
         params[0] = outer;
         System.arraycopy(tempParams, 0, params, 1, tempParams.length);
      }

      return params;
   }

   /**
    * Generates the method name for the inner advisor class. It cannot have the same
    * name/signature as the original in the outer class, or javassist gets
    * confused (Jira: JASSIST-12)
    *
    */
   private String getAdvisorMethodName(MethodTransformation trans)
   {
      if (trans.getHash() >= 0)
      {
         return trans.getOriginalName() + trans.getHash();
      }
      else
      {
         return trans.getOriginalName() + "_N_" + Math.abs(trans.getHash());
      }
   }

   private void addMethodToGeneratedAdvisor(MethodTransformation trans, String methodInfoField)throws CannotCompileException, NotFoundException
   {
      CtClass genadvisor = ((GeneratedAdvisorInstrumentor)trans.getInstrumentor()).getGenadvisor();

      CtClass[] params = addTargetToParamsForNonStaticMethod(trans.getClazz(), trans.getWMethod());

      String code = createAdvisorMethodBody(trans);
      try
      {
         CtMethod advisorMethod = CtNewMethod.make(
               Modifier.PROTECTED,
               trans.getWMethod().getReturnType(),
               getAdvisorMethodName(trans),
               params,
               trans.getWMethod().getExceptionTypes(),
               code,
               genadvisor);

         genadvisor.addMethod(advisorMethod);
         advisorMethod.setModifiers(Modifier.setProtected(advisorMethod.getModifiers()));
      }
      catch (CannotCompileException e)
      {
         throw new RuntimeException("code was: " + code + " for method " + getAdvisorMethodName(trans), e);
      }
   }

   private String createAdvisorMethodBody(MethodTransformation trans)throws NotFoundException
   {
      if (Modifier.isStatic(trans.getWMethod().getModifiers()))
      {
         return createStaticAdvisorMethodBody(trans);
      }
      else
      {
         return createNonStaticAdvisorMethodBody(trans);
      }
   }

   private String createStaticAdvisorMethodBody(MethodTransformation trans)throws NotFoundException
   {
      String joinpointName = getJoinPointFieldName(trans);
      String infoName = getMethodInfoFieldName(trans.getOriginalName(), trans.getHash());

      String code =
         "{" +
         "   if (" + joinpointName + " == null && " + infoName + " != null && " + infoName + ".hasAdvices())" +
         "   {" +
         "       super." + JoinPointGenerator.GENERATE_JOINPOINT_CLASS + "(" + infoName + ");" +
         "   }" +
         "   if (" + joinpointName + " == null)" +
         "   { " +
         "      " + getReturnStr(trans.getWMethod()) + trans.getClazzName() + "." + trans.getWrappedName() +"($$);" +
         "   }" +
         "   else" +
         "   {" +
         "    " + getAopReturnStr(trans.getWMethod()) + joinpointName + "." + JoinPointGenerator.INVOKE_JOINPOINT + "($$);" +
         "   }" +
         "}";

      return code;
   }

   private String createNonStaticAdvisorMethodBody(MethodTransformation trans)throws NotFoundException
   {
      String joinpointName = getJoinPointFieldName(trans);
      String infoName = getMethodInfoFieldName(trans.getOriginalName(), trans.getHash());

      String code =
         "{" +
         "   if (" + joinpointName + " == null && " + infoName + " != null && " + infoName + ".hasAdvices())" +
         "   {" +
         "       super." + JoinPointGenerator.GENERATE_JOINPOINT_CLASS + "(" + infoName + ");" +
         "   }" +
         "   if (" + joinpointName + " == null)" +
         "   { " +
         "      " + getAopReturnStr(trans.getWMethod()) + "$1." + trans.getWrappedName() + "(" + getNonStaticJavasistParamString(trans.getWMethod().getParameterTypes().length) + ");" +
         "   }" +
         "   else" +
         "   {" +
         "    " + getAopReturnStr(trans.getWMethod()) + joinpointName + "." + MethodJoinPointGenerator.INVOKE_JOINPOINT + "($$);" +
         "   }" +
         "}";

      return code;
   }

   public static String getNonStaticJavasistParamString(long parameterLength)throws NotFoundException
   {
      StringBuffer str = new StringBuffer();

      for (int i = 0 ; i < parameterLength ; i++)
      {
         if (i > 0)
         {
            str.append(", ");
         }
         str.append("$" + (i + 2));
      }

      return str.toString();
   }
}
