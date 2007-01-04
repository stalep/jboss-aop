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
 * Plain old java object used on @Target parameter tests.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class TargetPOJO
{
   public TargetPOJO(){}
   public TargetPOJO(int x){}
   
   public int field1;
   public static int field2;
   
   public void method1(){}
   public static void method2(){}
   
   public void method3(){
      new TargetPOJO2(3);
   }
   
   public void method4()
   {
      TargetPOJO2 pojo2 = new TargetPOJO2();
      pojo2.method1();
   }
   
   public void method5()
   {
      TargetPOJO2.method2();
   }
   
   public static void method6(){
      new TargetPOJO2(3);
   }
   
   public static void method7()
   {
      TargetPOJO2 pojo2 = new TargetPOJO2();
      pojo2.method1();
   }
   
   public static void method8()
   {
      TargetPOJO2.method2();
   }
}

class TargetPOJO2
{
   public TargetPOJO2(){}
   public TargetPOJO2(int x){}
   public void method1(){}
   public static void method2(){}
}