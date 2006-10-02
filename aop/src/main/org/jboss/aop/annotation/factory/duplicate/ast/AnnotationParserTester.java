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
package org.jboss.aop.annotation.factory.duplicate.ast;



public class AnnotationParserTester implements AnnotationParserVisitor
{
   private int indent = 0;

   private String indentString()
   {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < indent; ++i)
      {
         sb.append(" ");
      }
      return sb.toString();
   }

   public Object visit(ASTSingleMemberValue node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(SimpleNode node, Object data)
   {
      System.out.println(indentString() + node +
              ": acceptor not unimplemented in subclass?");
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTStart node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTAnnotation node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTMemberValuePairs node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTMemberValuePair node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTMemberValueArrayInitializer node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTIdentifier node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTString node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public Object visit(ASTChar node, Object data)
   {
      System.out.println(indentString() + node);
      ++indent;
      data = node.childrenAccept(this, data);
      --indent;
      return data;
   }

   public static void main(String args[])
   {
//      System.out.println("----" + args[0]);
//      StringReader reader = new StringReader(args[0]);
      System.out.println("Reading from stdin");
      AnnotationParser t = new AnnotationParser(System.in);
      //PointcutExpressionParser t = new PointcutExpressionParser(System.in);
      try
      {
         ASTStart n = t.Start();
         AnnotationParserVisitor v = new AnnotationParserTester();
         n.jjtAccept(v, null);
      }
      catch (Exception e)
      {
         System.out.println("Oops.");
         System.out.println(e.getMessage());
         e.printStackTrace();
      }
   }
}

/*end*/
