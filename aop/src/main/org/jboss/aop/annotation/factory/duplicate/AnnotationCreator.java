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
package org.jboss.aop.annotation.factory.duplicate;

import org.jboss.aop.annotation.factory.duplicate.ast.ASTAnnotation;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTChar;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTIdentifier;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTMemberValueArrayInitializer;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTMemberValuePair;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTMemberValuePairs;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTSingleMemberValue;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTStart;
import org.jboss.aop.annotation.factory.duplicate.ast.ASTString;
import org.jboss.aop.annotation.factory.duplicate.ast.AnnotationParser;
import org.jboss.aop.annotation.factory.duplicate.ast.AnnotationParserVisitor;
import org.jboss.aop.annotation.factory.duplicate.ast.Node;
import org.jboss.aop.annotation.factory.duplicate.ast.SimpleNode;
import org.jboss.aop.annotation.factory.duplicate.javassist.DefaultValueAnnotationValidator;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 46383 $
 */
public class AnnotationCreator implements AnnotationParserVisitor
{
   private Class annotation;
   private Class type;
   public Object typeValue;
   
   static final AnnotationValidator defaultAnnotationReader;
   static
   {
      boolean haveJavassist = false;
      try
      {
         Class clazz = Class.forName("javassist.CtClass");
         haveJavassist = true;
      }
      catch(ClassNotFoundException ignore)
      {
      }
      
      if (haveJavassist)
      {
         defaultAnnotationReader = new DefaultValueAnnotationValidator();
      }
      else
      {
         defaultAnnotationReader = new SimpleAnnotationValidator();
      }
      
   }

   public AnnotationCreator(Class annotation, Class type)
   {
      this.type = type;
      this.annotation = annotation;
   }


   public Object visit(ASTMemberValuePairs node, Object data)
   {
      node.childrenAccept(this, data);
      return null;
   }

   public Object visit(ASTMemberValuePair node, Object data)
   {
      String name = node.getIdentifier().getValue();
      node.getValue().jjtAccept(this, name);
      return data;
   }

   public Object visit(ASTSingleMemberValue node, Object data)
   {
      node.getValue().jjtAccept(this, "value");
      return data;
   }

   public Object visit(ASTIdentifier node, Object data)
   {
      try
      {
         if (type.equals(Class.class))
         {
            String classname = node.getValue();
            if (classname.endsWith(".class"))
            {
               classname = classname.substring(0, classname.indexOf(".class"));
            }
            if (classname.equals("void"))
            {
               typeValue = void.class;
            }
            else if (classname.equals("int"))
            {
               typeValue = int.class;
            }
            else if (classname.equals("byte"))
            {
               typeValue = byte.class;
            }
            else if (classname.equals("long"))
            {
               typeValue = long.class;
            }
            else if (classname.equals("double"))
            {
               typeValue = double.class;
            }
            else if (classname.equals("float"))
            {
               typeValue = float.class;
            }
            else if (classname.equals("char"))
            {
               typeValue = char.class;
            }
            else if (classname.equals("short"))
            {
               typeValue = short.class;
            }
            else if (classname.equals("boolean"))
            {
               typeValue = boolean.class;
            }
            else
            {
               typeValue = Thread.currentThread().getContextClassLoader().loadClass(classname);
            }
         }
         else if (type.isPrimitive())
         {
            if (type.equals(boolean.class))
            {
               typeValue = new Boolean(node.getValue());
            }
            else if (type.equals(short.class))
            {
               typeValue = Short.valueOf(node.getValue());
            }
            else if (type.equals(float.class))
            {
               typeValue = Float.valueOf(node.getValue());
            }
            else if (type.equals(double.class))
            {
               typeValue = Double.valueOf(node.getValue());
            }
            else if (type.equals(long.class))
            {
               typeValue = Long.valueOf(node.getValue());
            }
            else if (type.equals(byte.class))
            {
               typeValue = new Byte(node.getValue());
            }
            else if (type.equals(int.class))
            {
               typeValue = new Integer(node.getValue());
            }
         }
         else // its an enum
         {
            int index = node.getValue().lastIndexOf('.');
            if (index == -1) throw new RuntimeException("Enum must be fully qualified: " + node.getValue());
            String className = node.getValue().substring(0, index);
            String en = node.getValue().substring(index + 1);
            Class enumClass = Thread.currentThread().getContextClassLoader().loadClass(className);

            if (enumClass.getSuperclass().getName().equals("java.lang.Enum"))
            {
               Method valueOf = null;
               Method[] methods = enumClass.getSuperclass().getMethods();
               for (int i = 0; i < methods.length; i++)
               {
                  if (methods[i].getName().equals("valueOf"))
                  {
                     valueOf = methods[i];
                     break;
                  }
               }
               Object[] args = {enumClass, en};
               typeValue = valueOf.invoke(null, args);
            }
            else
            {
               Field field = enumClass.getField(en);
               typeValue = field.get(null);
            }
         }
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      catch (IllegalAccessException e)
      {
         throw new RuntimeException(e);
      }
      catch (InvocationTargetException e)
      {
         throw new RuntimeException(e);
      }
      catch (NoSuchFieldException e)
      {
         throw new RuntimeException(e);
      }
      return null;
   }

   public Object visit(ASTString node, Object data)
   {
      if (!type.equals(String.class)) throw new RuntimeException(annotation.getName() + "." + data + " is not an String");
      typeValue = node.getValue();
      return null;
   }

   public Object visit(ASTChar node, Object data)
   {
      if (!type.equals(char.class)) throw new RuntimeException(annotation.getName() + "." + data + " is not an char");
      typeValue = new Character(node.getValue());
      return null;
   }


   public Object visit(ASTMemberValueArrayInitializer node, Object data)
   {
      if (!type.isArray()) throw new RuntimeException(annotation.getName() + "." + data + " is not an array");
      Class baseType = type.getComponentType();
      int size = node.jjtGetNumChildren();
      typeValue = Array.newInstance(baseType, size);

      for (int i = 0; i < size; i++)
      {
         AnnotationCreator creator = new AnnotationCreator(annotation, baseType);
         node.jjtGetChild(i).jjtAccept(creator, null);
         Array.set(typeValue, i, creator.typeValue);
      }
      return null;
   }

   public Object visit(ASTAnnotation node, Object data)
   {
      try
      {
         Class subAnnotation = Thread.currentThread().getContextClassLoader().loadClass(node.getIdentifier());
         typeValue = createAnnotation(node, subAnnotation);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      return null;
   }

   // Unneeded

   public Object visit(SimpleNode node, Object data)
   {
      return null;
   }

   public Object visit(ASTStart node, Object data)
   {
      return null;
   }

   private static Class getMemberType(Class annotation, String member)
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
   
   private static ASTAnnotation getRootExpr(final String annotationExpr) throws Exception
   {
      try
      {
         ASTAnnotation node = (ASTAnnotation)AccessController.doPrivileged(new PrivilegedExceptionAction() {
           public Object run() throws Exception{
              AnnotationParser parser = new AnnotationParser(new StringReader(annotationExpr));
              org.jboss.aop.annotation.factory.duplicate.ast.ASTStart start = parser.Start();
              ASTAnnotation node = (ASTAnnotation) start.jjtGetChild(0);
              
              return node;
           }
         });
         
         return node;
      }
      catch (PrivilegedActionException e)
      {
         throw new RuntimeException(e.getException());
      }
   }
   
   
   public static Object createAnnotation(ASTAnnotation node, Class annotation, ClassLoader cl) throws Exception
   {
      HashMap map = new HashMap();
      if (annotation == null)
      {
         ClassLoader loader = (cl != null) ? cl : Thread.currentThread().getContextClassLoader();
         annotation = loader.loadClass(node.getIdentifier());
      }
      
      if (node.jjtGetNumChildren() > 0)
      {
         Node contained = node.jjtGetChild(0);
         if (contained instanceof ASTSingleMemberValue)
         {
            Class type = getMemberType(annotation, "value");
            AnnotationCreator creator = new AnnotationCreator(annotation, type);
            contained.jjtAccept(creator, "value");
            map.put("value", creator.typeValue);
         }
         else
         {
            ASTMemberValuePairs pairs = (ASTMemberValuePairs) contained;
            for (int i = 0; i < pairs.jjtGetNumChildren(); i++)
            {
               ASTMemberValuePair member = (ASTMemberValuePair) pairs.jjtGetChild(i);
               Class type = getMemberType(annotation, member.getIdentifier().getValue());
               AnnotationCreator creator = new AnnotationCreator(annotation, type);
               member.jjtAccept(creator, null);
               map.put(member.getIdentifier().getValue(), creator.typeValue);
            }
         }
      }
      
      defaultAnnotationReader.validate(map, annotation);
      return AnnotationProxy.createProxy(map, annotation);
   }

   public static Object createAnnotation(ASTAnnotation node, Class annotation) throws Exception
   {
      return createAnnotation(node, annotation, null);
   }
   
   public static Object createAnnotation(final String annotationExpr, final Class annotation) throws Exception
   {
      return createAnnotation(getRootExpr(annotationExpr), annotation, null);
   }

   public static Object createAnnotation(String annotationExpr, ClassLoader cl) throws Exception
   {
      return createAnnotation(getRootExpr(annotationExpr), null, cl);
   }

   
}
