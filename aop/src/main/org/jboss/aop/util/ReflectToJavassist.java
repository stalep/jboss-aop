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
package org.jboss.aop.util;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.jboss.aop.AspectManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 *
 **/
public class ReflectToJavassist
{
   public static CtClass classToJavassist(Class clazz) throws NotFoundException
   {
      ClassPool pool = AspectManager.instance().findClassPool(clazz.getClassLoader());
      CtClass ct = pool.get(clazz.getName());
      return ct;
   }
   
   public static CtField fieldToJavassist(Field field) throws NotFoundException
   {
      Class clazz = field.getDeclaringClass();
      return classToJavassist(field.getDeclaringClass()).getField(field.getName());
   }
   public static CtConstructor constructorToJavassist(Constructor con) throws NotFoundException
   {
      Class clazz = con.getDeclaringClass();
      Class[] params = con.getParameterTypes();
      String[] strParams = new String[params.length];
      for (int i = 0; i < params.length; i++)
      {
         strParams[i] = simpleType(params[i]);
      }
      CtClass ct = classToJavassist(clazz);

      CtConstructor[] methods = ct.getDeclaredConstructors();
      for (int i = 0; i < methods.length; i++)
      {
         CtClass[] ctParams = methods[i].getParameterTypes();
         if (ctParams.length == params.length)
         {
            boolean found = true;
            for (int j = 0; j < ctParams.length; j++)
            {
               if (!ctParams[j].getName().equals(strParams[j]))
               {
                  found = false;
                  break;
               }
            }
            if (found) return methods[i];
         }
      }
      return null;
   }

   protected static String simpleType(Class type)
   {
      Class ret = type;
      if (ret.isArray())
      {
         Class arr = ret;
         String array = "";
         while (arr.isArray())
         {
            array += "[]";
            arr = arr.getComponentType();
         }
         return arr.getName() + array;
      }
      return ret.getName();
   }
   
   public static CtMethod methodToJavassist(Method method) throws NotFoundException
   {
      Class clazz = method.getDeclaringClass();
      Class[] params = method.getParameterTypes();
      String[] strParams = new String[params.length];
      for (int i = 0; i < params.length; i++)
      {
         strParams[i] = simpleType(params[i]);
      }
      CtClass ct = classToJavassist(clazz);

      while (!ct.getName().equals(clazz.getName()))
      {
         ct = ct.getSuperclass();
      }
      CtMethod[] methods = ct.getDeclaredMethods();
      //String state = "LOOPING THROUGH CtMethods";
      for (int i = 0; i < methods.length; i++)
      {
         if (methods[i].getName().equals(method.getName()))
         {
            //state = "FOUND METHOD OF SAME NAME: " + method.toString();
            CtClass[] ctParams = methods[i].getParameterTypes();
            if (ctParams.length == params.length)
            {
               boolean found = true;
               for (int j = 0; j < ctParams.length; j++)
               {
                  if (!ctParams[j].getName().equals(strParams[j]))
                  {
                     //state ="**** method " + method.getName() + " ctParams: '" + ctParams[j].getName() + "' != " + "'" + strParams[j] + "'";
                     found = false;
                     break;
                  }
               }
               if (found) return methods[i];
            }
         }
      }
      //throw new Exception("failed to find method: " + method.toString() + " state: " + state);
      return null;
   }

}
