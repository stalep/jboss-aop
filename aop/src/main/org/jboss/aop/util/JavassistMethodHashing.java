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
 
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create a unique hash for method.  This is the same as
 * common: org.jboss.util.MethodHashing except that
 * it's using Javasssit constructs rather than java.lang.reflect.
 *
 * 
 * @author  <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author  <a href="mailto:marc@jboss.org">Marc Fleury</a>
 * @version $Revision$
 */
public class JavassistMethodHashing
{
   public static long methodHash(CtMethod method)
   {
      try
      {
         CtClass[] parameterTypes = method.getParameterTypes();
         String methodDesc = method.getName()+"(";
         for(int j = 0; j < parameterTypes.length; j++)
         {
            methodDesc += getTypeString(parameterTypes[j]);
         }
         methodDesc += ")"+getTypeString(method.getReturnType());
         
         long hash = 0;
         ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(512);
         MessageDigest messagedigest = MessageDigest.getInstance("SHA");
         DataOutputStream dataoutputstream = new DataOutputStream(new DigestOutputStream(bytearrayoutputstream, messagedigest));
         dataoutputstream.writeUTF(methodDesc);
         dataoutputstream.flush();
         byte abyte0[] = messagedigest.digest();
         for(int j = 0; j < Math.min(8, abyte0.length); j++)
            hash += (long)(abyte0[j] & 0xff) << j * 8;
         return hash;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public static long constructorHash(CtConstructor method)
   {
      try
      {
         CtClass[] parameterTypes = method.getParameterTypes();
         String methodDesc = method.getDeclaringClass().getName()+"(";
         for(int j = 0; j < parameterTypes.length; j++)
         {
            methodDesc += getTypeString(parameterTypes[j]);
         }
         methodDesc += ")";

         long hash = 0;
         ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(512);
         MessageDigest messagedigest = MessageDigest.getInstance("SHA");
         DataOutputStream dataoutputstream = new DataOutputStream(new DigestOutputStream(bytearrayoutputstream, messagedigest));
         dataoutputstream.writeUTF(methodDesc);
         dataoutputstream.flush();
         byte abyte0[] = messagedigest.digest();
         for(int j = 0; j < Math.min(8, abyte0.length); j++)
            hash += (long)(abyte0[j] & 0xff) << j * 8;
         return hash;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   static String getTypeString(CtClass cl)
      throws Exception
   {
      if (cl.equals(CtClass.byteType))
      {
         return "B";
      } else if (cl.equals(CtClass.charType))
      {
         return "C";
      } else if (cl.equals(CtClass.doubleType))
      {
         return "D";
      } else if (cl.equals(CtClass.floatType))
      {
         return "F";
      } else if (cl.equals(CtClass.intType))
      {
         return "I";
      } else if (cl.equals(CtClass.longType))
      {
         return "J";
      } else if (cl.equals(CtClass.shortType))
      {
         return "S";
      } else if (cl.equals(CtClass.booleanType))
      {
         return "Z";
      } else if (cl.equals(CtClass.voidType))
      {
         return "V";
      } else if (cl.isArray())
      {
         return "["+getTypeString(cl.getComponentType());
      } else
      {
         return "L"+cl.getName().replace('.','/')+";";
      }
   }
   
   private static void addDeclaredMethods(HashMap advised, List ignoredHash, CtClass superclass) throws Exception
   {
      CtMethod[] declaredMethods = superclass.getDeclaredMethods();
      for (int i = 0; i < declaredMethods.length; i++)
      {
         if (superclass.isInterface() || Advisable.isAdvisable(declaredMethods[i]))
         {
            //if a method is marked as a "volatile/bridge" method, we need to
            //ignore it and check that other implementations of that method
            // (in superclasses) are not added either.
            if(!Modifier.isVolatile(declaredMethods[i].getModifiers()))
            {
            long hash = methodHash(declaredMethods[i]);
            if(!ignoredHash.contains(new Long(hash)))
               advised.put(new Long(hash), declaredMethods[i]);
            }
            else
            {
               long hash = methodHash(declaredMethods[i]);
               ignoredHash.add(new Long(hash));
            }
         }
      }
   }
   private static void populateMethodTables(HashMap advised, List ignoredHash, CtClass superclass)
      throws Exception
   {
      if (superclass == null) return;
      if (superclass.getName().equals("java.lang.Object")) return;

      addDeclaredMethods(advised, ignoredHash, superclass);
      populateMethodTables(advised, ignoredHash, superclass.getSuperclass());
   }

   public static HashMap getMethodMap(CtClass clazz) throws Exception
   {
      HashMap map = new HashMap();
      List ignoredHash = new ArrayList();
      populateMethodTables(map, ignoredHash, clazz);
      return map;
   }

   public static HashMap getDeclaredMethodMap(CtClass clazz) throws Exception
   {
      HashMap map = new HashMap();
      List ignoredHash = new ArrayList();
      addDeclaredMethods(map, ignoredHash, clazz);
      return map;
   }

}
