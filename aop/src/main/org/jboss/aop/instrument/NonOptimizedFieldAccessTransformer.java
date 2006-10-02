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

import java.util.Iterator;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import org.jboss.aop.ClassAdvisor;
import org.jboss.aop.instrument.OptimizedFieldAccessTransformer.OptimizedFieldAccessExprEditor;

/**
 * Comment
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class NonOptimizedFieldAccessTransformer extends FieldAccessTransformer
{

   public NonOptimizedFieldAccessTransformer(Instrumentor instrumentor)
   {
      super(instrumentor);
   }
   
   protected void doBuildFieldWrappers(CtClass clazz, CtField field, int fieldIndex, JoinpointClassification classificationGet, JoinpointClassification classificationSet)
   throws NotFoundException, CannotCompileException
   {
      instrumentor.setupBasics(clazz);
      
      boolean wrappedGet = classificationGet.equals(JoinpointClassification.WRAPPED);
      boolean wrappedSet = classificationSet.equals(JoinpointClassification.WRAPPED);
      int mod = getStaticModifiers(field);
       
      // executeWrapping
      replaceFieldAccessInternally(clazz, field, wrappedGet, wrappedSet, fieldIndex);

      // don't need wrappers if the field is private as we inline
      // the conditional and interception directly within code.
      if (!javassist.Modifier.isPrivate(field.getModifiers()))
      {
         // prepareForWrapping
         if (isPrepared(classificationGet))
         {
            wrapper.prepareForWrapping(field, GET_INDEX);
         }
         if (isPrepared(classificationSet))
         {
            wrapper.prepareForWrapping(field, SET_INDEX);
         }
          
         // wrap
         if (wrappedGet)
         {
            wrapper.wrap(field, GET_INDEX);
            if (classificationGet.equals(JoinpointClassification.DYNAMICALY_WRAPPED))
            {
               instrumentor.dynamicTransformationObserver.fieldReadDynamicalyWrapped(field);
            }
         }
         if (wrappedSet)
         {
            wrapper.wrap(field, SET_INDEX);
            if (classificationSet.equals(JoinpointClassification.DYNAMICALY_WRAPPED))
            {
               instrumentor.dynamicTransformationObserver.fieldWriteDynamicalyWrapped(field);
            }
         }
         // generateWrapper
         buildWrapperPlaceHolders(clazz, field, isPrepared(classificationGet), isPrepared(classificationSet), mod);
         buildWrappers(clazz, field, isPrepared(classificationGet), isPrepared(classificationSet), fieldIndex);//mod);
      }
   }
   
   private void buildWrappers(CtClass clazz, CtField field, boolean doGet, boolean doSet, int index)
   throws NotFoundException, CannotCompileException
   {
      if (doGet)
      {
         String code = getWrapperBody(clazz, field, true, index);
         CtMethod method = clazz.getDeclaredMethod(fieldRead(field.getName()));
         method.setBody(code);
      }
      if (doSet)
      {
         String code = getWrapperBody(clazz, field, false, index);
         CtMethod method = clazz.getDeclaredMethod(fieldWrite(field.getName()));
         method.setBody(code);            
      }
   }
   
   protected String getWrapperBody(CtClass clazz, CtField field, boolean get, int fieldIndex) throws NotFoundException, CannotCompileException
   {
      String name = field.getName();
      boolean isStatic = (javassist.Modifier.isStatic(field.getModifiers()));
      String access = "";
      String instanceCheck = "";
      if (!isStatic)
      {
         access = "((" + clazz.getName() + ")$1).";
         instanceCheck = " || ((org.jboss.aop.ClassInstanceAdvisor)((org.jboss.aop.InstanceAdvised)$1)._getInstanceAdvisor()).hasInstanceAspects";
      }
      
      // read wrapper
      if (get)
      {
         return 
            "{ " +
            "    if (" + Instrumentor.HELPER_FIELD_NAME + ".hasAspects() " + instanceCheck + " ) " +
            "    { " +
            "       return ($r)" + Instrumentor.HELPER_FIELD_NAME + ".invokeRead($1, (int)" + (fieldIndex) + "); " +
            "    } " +
            "    return " + access + name + "; " +
            "}";
      }
      // write wrapper
      return 
             "{ " +
             "    if (" + Instrumentor.HELPER_FIELD_NAME + ".hasAspects() " + instanceCheck + " ) " +
             "    { " +
             "       " + Instrumentor.HELPER_FIELD_NAME + ".invokeWrite($1, (int)" + (fieldIndex) + ", ($w)$2); " +
             "    } " +
             "    else " +
             "    { " +
             "       " + access + name + " = $2; " +
             "    } " +
             "}";
   }

   protected void replaceFieldAccessInternally(CtClass clazz, CtField field, boolean doGet, boolean doSet, int index) throws CannotCompileException
   {
      NonOptimizedFieldAccessExprEditor expr = new NonOptimizedFieldAccessExprEditor(clazz, field, doGet, doSet, index);
      clazz.instrument(expr);
   }


   protected class NonOptimizedFieldAccessExprEditor extends FieldAccessExprEditor
   {
      public NonOptimizedFieldAccessExprEditor(CtClass clazz, CtField field, boolean doGet, boolean doSet, int index)
      {
         super(clazz, field, doGet, doSet, index);
      }

      protected void replaceRead(FieldAccess fieldAccess) throws CannotCompileException
      {
         String code = null;
         try
         {
            if (fieldAccess.isStatic())
            {
               code =
               "    if (" + Instrumentor.HELPER_FIELD_NAME + ".hasAspects()) " +
               "    { " +
               "       Object obj = null;" +
               "       $_ = ($r)" + Instrumentor.HELPER_FIELD_NAME + ".invokeRead(obj, (int)" + (fieldIndex) + "); " +
               "    } " +
               "    else " +
               "    { " +
               "       $_ = " + clazz.getName() + "." + field.getName() + "; " +
               "    } " +
               "";
               fieldAccess.replace(code);
            }
            else
            {
               code =
               "    org.jboss.aop.ClassInstanceAdvisor instAdv = (org.jboss.aop.ClassInstanceAdvisor)((org.jboss.aop.InstanceAdvised)$0)._getInstanceAdvisor();" +
               "    if (" + Instrumentor.HELPER_FIELD_NAME + ".hasAspects() || (instAdv != null && instAdv.hasInstanceAspects)) " +
               "    { " +
               "       $_ = ($r)" + Instrumentor.HELPER_FIELD_NAME + ".invokeRead($0, (int)" + (fieldIndex) + "); " +
               "    } " +
               "    else " +
               "    { " +
               "       $_ = $0." + fieldAccess.getFieldName() + "; " +
               "    } ";
               fieldAccess.replace(code);
            }
         }
         catch (CannotCompileException e)
         {
            throw new RuntimeException("failed with: " + code, e);
         }
      }

      protected void replaceWrite(FieldAccess fieldAccess) throws CannotCompileException
      {
         if (fieldAccess.isStatic())
         {
            String code =
                    "    if (" + Instrumentor.HELPER_FIELD_NAME + ".hasAspects()) " +
                    "    { " +
                    "       Object obj = null;" +
                    "      " + Instrumentor.HELPER_FIELD_NAME + ".invokeWrite(obj, (int)" + (fieldIndex) + ", ($w)$1); " +
                    "    } " +
                    "    else " +
                    "    { " +
                    "       " + clazz.getName() + "." + fieldAccess.getFieldName() + " = $1; " +
                    "    } ";
            fieldAccess.replace(code);
         }
         else
         {
            String code =
                    "    org.jboss.aop.ClassInstanceAdvisor instAdv = (org.jboss.aop.ClassInstanceAdvisor)((org.jboss.aop.InstanceAdvised)$0)._getInstanceAdvisor();" +
                    "    if (" + Instrumentor.HELPER_FIELD_NAME + ".hasAspects() || (instAdv != null && instAdv.hasInstanceAspects)) " +
                    "    { " +
                    "       " + Instrumentor.HELPER_FIELD_NAME + ".invokeWrite($0, (int)" + (fieldIndex) + ", ($w)$1); " +
                    "    } " +
                    "    else " +
                    "    { " +
                    "       $0." + fieldAccess.getFieldName() + " = $1; " +
                    "    } ";
            fieldAccess.replace(code);
         }
      }


   }//End Inner class FieldAccessExprEditor

}
