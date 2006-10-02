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

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class ReflectUtils
{
   public static Class[] EMPTY_CLASS_ARRAY = new Class[0];
   
   public static Method[] getMethodsWithName(Class clazz, String name)
   {
      Method[] methods = clazz.getMethods();
      return getMethodsWithName(methods, name);
   }
   
   public static Method[] getDeclaredMethodsWithName(Class clazz, String name)
   {
      Method[] methods = clazz.getDeclaredMethods();
      return getMethodsWithName(methods, name);
   }
   
   private static Method[] getMethodsWithName(Method[] methods, String name)
   {
      ArrayList foundMethods = new ArrayList();
      for (int i = 0 ; i < methods.length ; i++)
      {
         if (methods[i].getName().equals(name))
         {
            foundMethods.add(methods[i]);
         }
      }
      return (Method[])foundMethods.toArray(new Method[foundMethods.size()]);
   }
   
}
