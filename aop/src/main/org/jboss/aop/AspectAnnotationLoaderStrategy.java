/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop;

import org.jboss.aop.advice.PrecedenceDefEntry;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.CFlowStack;
import org.jboss.aop.pointcut.ast.ASTCFlowExpression;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public interface AspectAnnotationLoaderStrategy
{

   void deployAspect(AspectAnnotationLoader loader, boolean isFactory, String name, Scope scope);
   
   void deployAspectMethodBinding(
         AspectAnnotationLoader loader, 
         org.jboss.aop.advice.AdviceType internalAdviceType, 
         String aspectDefName, 
         String methodName, 
         String bindingName,
         String pointcutString,
         String cflow,
         ASTCFlowExpression cflowExpression) throws Exception;
   
   void undeployAspect(AspectAnnotationLoader loader, String name);
   
   void undeployAspectMethodBinding(AspectAnnotationLoader loader, String bindingName, String className, String methodName);
   
   void deployInterceptor(AspectAnnotationLoader loader, boolean isFactory, String name, Scope scope);
   
   void deployInterceptorBinding(AspectAnnotationLoader loader, String name, String pointcutString, String cflow, ASTCFlowExpression cflowExpression) throws Exception;
   
   void undeployInterceptor(AspectAnnotationLoader loader, String name);
   
   void undeployInterceptorBinding(AspectAnnotationLoader loader, String name);
   
   void deployDynamicCFlow(AspectAnnotationLoader loader, String name, String clazz);
   
   void undeployDynamicCFlow(AspectAnnotationLoader loader, String name);
   
   void deployPointcut(AspectAnnotationLoader loader, String name, String expr) throws Exception;
   
   void undeployPointcut(AspectAnnotationLoader loader, String name);
   
   void deployPrecedence(AspectAnnotationLoader loader, String name, PrecedenceDefEntry[] pentries);

   void undeployPrecedence(AspectAnnotationLoader loader, String name);
   
   void deployTypedef(AspectAnnotationLoader loader, String name, String expr) throws Exception;
   
   void undeployTypedef(AspectAnnotationLoader loader, String name);

   void deployDeclare(AspectAnnotationLoader loader, String name, String expr, boolean warning, String msg) throws Exception;
   
   void deployAnnotationIntroduction(AspectAnnotationLoader loader, String expr, String annotation, boolean invisible);

   void undeployAnnotationIntroduction(AspectAnnotationLoader loader, String expr, String annotation, boolean invisible);

   void deployCFlow(AspectAnnotationLoader loader, CFlowStack stack);
   
   void undeployCFlow(AspectAnnotationLoader loader, String name);
   
   void deployInterfaceIntroduction(AspectAnnotationLoader loader, InterfaceIntroduction introduction);

   void undeployInterfaceIntroduction(AspectAnnotationLoader loader, String name);
}
