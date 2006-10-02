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

import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.MemberValueVisitor;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 57050 $
 */
public class ProxyMapCreator implements MemberValueVisitor
{
   public Object value;
   private Class type;


   public ProxyMapCreator(Class type)
   {
      this.type = type;
   }

   public void visitAnnotationMemberValue(AnnotationMemberValue annotationMemberValue)
   {
      try
      {
         value = AnnotationProxy.createProxy(annotationMemberValue.getValue());
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
   }

   public void visitArrayMemberValue(ArrayMemberValue arrayMemberValue)
   {
      Class baseType = type.getComponentType();
      int size = 0;
      if (arrayMemberValue.getValue() == null || arrayMemberValue.getValue().length == 0)
      {
         size = 0;
      }
      else
      {
         size = arrayMemberValue.getValue().length;
      }
      value = Array.newInstance(baseType, size);
      MemberValue[] elements = arrayMemberValue.getValue();
      for (int i = 0; i < size; i++)
      {
         ProxyMapCreator creator = new ProxyMapCreator(baseType);
         elements[i].accept(creator);
         Array.set(value, i, creator.value);
      }
   }

   public void visitBooleanMemberValue(BooleanMemberValue booleanMemberValue)
   {
      value = new Boolean(booleanMemberValue.getValue());
   }

   public void visitByteMemberValue(ByteMemberValue byteMemberValue)
   {
      value = new Byte(byteMemberValue.getValue());
   }

   public void visitCharMemberValue(CharMemberValue charMemberValue)
   {
      value = new Character(charMemberValue.getValue());
   }

   public void visitDoubleMemberValue(DoubleMemberValue doubleMemberValue)
   {
      value = new Double(doubleMemberValue.getValue());
   }

   public void visitEnumMemberValue(EnumMemberValue enumMemberValue)
   {
      try
      {
         Field enumVal = type.getField(enumMemberValue.getValue());
         value = enumVal.get(null);
      }
      catch (NoSuchFieldException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      catch (SecurityException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      catch (IllegalArgumentException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      catch (IllegalAccessException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
   }

   public void visitFloatMemberValue(FloatMemberValue floatMemberValue)
   {
      value = new Float(floatMemberValue.getValue());
   }

   public void visitIntegerMemberValue(IntegerMemberValue integerMemberValue)
   {
      value = new Integer(integerMemberValue.getValue());
   }

   public void visitLongMemberValue(LongMemberValue longMemberValue)
   {
      value = new Long(longMemberValue.getValue());
   }

   public void visitShortMemberValue(ShortMemberValue shortMemberValue)
   {
      value = new Short(shortMemberValue.getValue());
   }

   public void visitStringMemberValue(StringMemberValue stringMemberValue)
   {
      value = stringMemberValue.getValue();
   }

   public void visitClassMemberValue(ClassMemberValue classMemberValue)
   {
      try
      {
         String classname = classMemberValue.getValue();
         if (classname.equals("void"))
         {
            value = void.class;
         }
         else if (classname.equals("int"))
         {
            value = int.class;
         }
         else if (classname.equals("byte"))
         {
            value = byte.class;
         }
         else if (classname.equals("long"))
         {
            value = long.class;
         }
         else if (classname.equals("double"))
         {
            value = double.class;
         }
         else if (classname.equals("float"))
         {
            value = float.class;
         }
         else if (classname.equals("char"))
         {
            value = char.class;
         }
         else if (classname.equals("short"))
         {
            value = short.class;
         }
         else if (classname.equals("boolean"))
         {
            value = boolean.class;
         }
         else
         {
            value = Thread.currentThread().getContextClassLoader().loadClass(classMemberValue.getValue());
         }
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }

   }

   public static Class getMemberType(Class annotation, String member)
   {
      Method[] methods = annotation.getMethods();
      for (int i = 0; i < methods.length; i++)
      {
         if (methods[i].getName().equals(member))
         {
            return methods[i].getReturnType();
         }
      }
      throw new RuntimeException("unable to determine member type for annotation: " + annotation.getName() + "." + member);
   }

   public static Map createProxyMap(Class annotation, javassist.bytecode.annotation.Annotation info)
   {
      //TODO: Need to handle default values for annotations in jdk 1.5
      Map map = new HashMap();

      if (info.getMemberNames() == null) return map;
      Set members = info.getMemberNames();
      Iterator it = members.iterator();
      while (it.hasNext())
      {
         String name = (String) it.next();
         MemberValue mv = info.getMemberValue(name);
         ProxyMapCreator creator = new ProxyMapCreator(getMemberType(annotation, name));
         mv.accept(creator);
         map.put(name, creator.value);
      }
      return map;
   }
}
