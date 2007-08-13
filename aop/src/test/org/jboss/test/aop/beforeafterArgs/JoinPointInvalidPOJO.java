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
package org.jboss.test.aop.beforeafterArgs;

/**
 * Plain old java object used on @JoinPoint parameter tests.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class JoinPointInvalidPOJO
{
   public int number;
   public String text;
   
   // constructor without advices
   public JoinPointInvalidPOJO() {}
   
   // constructor execution
   public JoinPointInvalidPOJO(short arg0, boolean shouldThrow) throws POJOException
   {
      if (shouldThrow)
      {
         throw new POJOException();
      }
   }
   
   // construction
   public JoinPointInvalidPOJO(float arg0, boolean shouldThrow) throws POJOException
   {
      if (shouldThrow)
      {
         throw new POJOException();
      }
   }
   
   // called constructor
   public JoinPointInvalidPOJO(boolean shouldThrow) throws POJOException
   {
      if (shouldThrow)
      {
         throw new POJOException();
      }
   }
   
   // constructor call constructor
   public JoinPointInvalidPOJO(int arg0, boolean shouldThrow) throws POJOException
   {
      new JoinPointInvalidPOJO(shouldThrow);
   }
   
   // constructor call method
   public JoinPointInvalidPOJO(boolean arg0, boolean shouldThrow) throws POJOException
   {
      this.calledMethod(shouldThrow);
   }
   
   // constructor call static method
   public JoinPointInvalidPOJO(char arg0, boolean shouldThrow) throws POJOException
   {
      calledStaticMethod(shouldThrow);
   }
   
   public void method1()
   {
      
   }
   
   public void method3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method5() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method6() throws POJOException
   {
      throw new POJOException();
   }
 
   // method call constructor
   public void callConstructor(boolean shouldThrow) throws POJOException
   {
      new JoinPointInvalidPOJO(shouldThrow);
   }
   
   // static method call constructor
   public static void staticCallConstructor(boolean shouldThrow) throws POJOException
   {
      new JoinPointInvalidPOJO(shouldThrow);
   }
   
   // method call method
   public void callMethod(boolean shouldThrow) throws POJOException
   {
      calledMethod(shouldThrow);
   }
   
   // static method call method
   public static void staticCallMethod(JoinPointInvalidPOJO pojo, boolean shouldThrow) throws POJOException
   {
      pojo.calledMethod(shouldThrow);
   }
   
   public void calledMethod(boolean shouldThrow) throws POJOException
   {
      if (shouldThrow)
      {
         throw new POJOException();
      }
   }
   
   // method call static method
   public void callStaticMethod(boolean shouldThrow) throws POJOException
   {
      calledStaticMethod(shouldThrow);
   }
   
   // static method call static method
   public static void staticCallStaticMethod(boolean shouldThrow) throws POJOException
   {
      calledStaticMethod(shouldThrow);
   }
   
   public static void calledStaticMethod(boolean shouldThrow) throws POJOException
   {
      if (shouldThrow)
      {
         throw new POJOException();
      }
   }
}