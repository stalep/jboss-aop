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
package org.jboss.aop.annotation.factory.duplicate.javassist;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Map;

import org.jboss.aop.annotation.factory.duplicate.AnnotationValidationException;
import org.jboss.aop.annotation.factory.duplicate.AnnotationValidator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationDefaultAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.MemberValue;
import javassist.scopedpool.ScopedClassPoolRepository;
import javassist.scopedpool.ScopedClassPoolRepositoryImpl;

/**
 * Validates if all attributes have been filled in for an annotation. 
 * Attempts to read default values where they exist
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 57050 $
 */
public class DefaultValueAnnotationValidator implements AnnotationValidator
{
   ScopedClassPoolRepository repository = ScopedClassPoolRepositoryImpl.getInstance();
   
   public void validate(Map map, Class annotation)
   {
      ArrayList notAssignedAttributes = null;
      CtClass ctClass = null;
      
      Method[] methods = getDeclaredMethods(annotation);
      for (int i = 0 ; i < methods.length ; i++)
      {
         if (map.get(methods[i].getName()) == null)
         {
            if (ctClass == null)
            {
               ctClass = getCtClass(annotation);
            }
            
            CtMethod method;
            try
            {
               method = ctClass.getDeclaredMethod(methods[i].getName());
            }
            catch (NotFoundException e)
            {
               throw new RuntimeException("Unable to find method " + methods[i].getName() + " for " + annotation.getName());
            }
            Object defaultValue = null;
            MethodInfo minfo = method.getMethodInfo2();
            AnnotationDefaultAttribute defAttr = (AnnotationDefaultAttribute)minfo.getAttribute(AnnotationDefaultAttribute.tag);
            
            if (defAttr != null)
            {
               MemberValue value = defAttr.getDefaultValue();    // default value of age
               MemberValueGetter getter = new MemberValueGetter(methods[i]);
               value.accept(getter);
               defaultValue = getter.getValue();
            }
            
            if (defaultValue != null)
            {
               map.put(methods[i].getName(), defaultValue);
            }
            else
            {
               if (notAssignedAttributes == null)
               {
                  notAssignedAttributes = new ArrayList();
               }
               notAssignedAttributes.add(methods[i].getName());
            }

         }
      }

      if (notAssignedAttributes != null)
      {
         throw new AnnotationValidationException("Unable to fill in default attributes for " + annotation + " " + notAssignedAttributes);
      }
   }
   
   
   CtClass getCtClass(Class clazz)
   {
      try
      {
         ClassPool pool = repository.findClassPool(clazz.getClassLoader());
         return pool.get(clazz.getName());
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException("Unable to load CtClass for " + clazz, e);
      }
   }

   private Method[] getDeclaredMethods(final Class clazz)
   {
      return (Method[])AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() 
         {
            return clazz.getDeclaredMethods();
         };  
      });
   }
}

