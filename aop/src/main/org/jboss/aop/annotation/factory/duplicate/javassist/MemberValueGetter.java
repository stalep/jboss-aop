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

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import javassist.bytecode.annotation.Annotation;
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

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 57050 $
 */
public class MemberValueGetter implements MemberValueVisitor
{
   Object value;
   Method method;

   public MemberValueGetter(Method method)
   {
      this.method = method;
   }
   
   public Object getValue()
   {
      return value;
   }
   
   public void visitAnnotationMemberValue(AnnotationMemberValue node)
   {
      try
      {
         Annotation ann = node.getValue();
         value = AnnotationProxy.createProxy(ann);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public void visitArrayMemberValue(ArrayMemberValue node)
   {
      MemberValue[] values = node.getValue();
      value = node.getValue();
      Object vals = Array.newInstance(getAttributeType(), values.length);
      
      for (int i = 0 ; i < values.length ; i++)
      {
         values[i].accept(this);
         Array.set(vals, i, value);
      }

      value = vals;
   }

   public void visitBooleanMemberValue(BooleanMemberValue node)
   {
      value = new Boolean(node.getValue());
   }

   public void visitByteMemberValue(ByteMemberValue node)
   {
      value = new Byte(node.getValue());
   }

   public void visitCharMemberValue(CharMemberValue node)
   {
      value = new Character(node.getValue());
   }

   public void visitDoubleMemberValue(DoubleMemberValue node)
   {
      value = new Double(node.getValue());
   }

   public void visitEnumMemberValue(EnumMemberValue node)
   {
      value = Enum.valueOf(getAttributeType(), node.getValue());
   }

   public void visitFloatMemberValue(FloatMemberValue node)
   {
      value = new Float(node.getValue());
   }

   public void visitIntegerMemberValue(IntegerMemberValue node)
   {
      value = new Integer(node.getValue());
   }

   public void visitLongMemberValue(LongMemberValue node)
   {
      value = new Long(node.getValue());
   }

   public void visitShortMemberValue(ShortMemberValue node)
   {
      value = new Short(node.getValue());
   }

   public void visitStringMemberValue(StringMemberValue node)
   {
      value = node.getValue();
   }

   public void visitClassMemberValue(ClassMemberValue node)
   {
      try
      {
         value = Class.forName(node.getValue());
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   private Class getAttributeType()
   {
      Class rtn = method.getReturnType();
      
      while (rtn.isArray())
      {
         rtn = rtn.getComponentType();         
      }
      
      return rtn;
   }
   
}
