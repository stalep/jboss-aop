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

import java.util.Collection;
import java.util.List;

/**
 * POJO whose joinpoint executions will throw a NoMatchingAdviceException, due to
 * the fact that there is no advice that matches the joinpoint.
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ArgsInvalidPOJO
{
   public Object field1;
   
   public String field2;
   
   public int field3;
   
   public static boolean field4;
   
   public int bunch1(int x, double y, float z, String str, int q)
   {
      return x + (int) y + (int) z + q;
   }
   
   public int bunch2(int x, double y, float z, int q)
   {
      return x + (int) y * 2 + (int) z + q;
   }
   
   public int bunch3(int x, double y, float z, int q)
   {
      return x + (int) y * 3 + (int) z + q;
   }
   
   public int bunch4(int x, double y, float z, int q)
   {
      return x + (int) y * 4 + (int) z + q;
   }
   
   public int bunch5(int x, double y, float z, int q)
   {
      return x + (int) y * 5 + (int) z + q;
   }
   
   public int bunch6(int x, double y, float z, int q)
   {
      return x + (int) y * 6 + (int) z + q;
   }
   
   public int bunch7(int x, double y, float z, int q)
   {
      return x + (int) y * 7 + (int) z + q;
   }
   
   public int bunch8(int x, double y, float z, int q)
   {
      return x + (int) y * 8 + (int) z + q;
   }
   
   public int bunch9(int x, double y, float z, int q)
   {
      return x + (int) y * 9 + (int) z + q;
   }
   
   public void method2(String param1, int param2, boolean param3, ArgsPOJO[] param4)
   {}
   
   public void method4(String param1, int param2, boolean param3, ArgsPOJO[] param4)
   {}
   
   public void method6(short param1, long param2) throws POJOException
   {
      throw new POJOException();
   }
   
   public void method8() throws POJOException
   {
      this.method6((short) -4, (long) 4);
   }
   
   public void method9(Interface param) throws POJOException {}
   
   public void method10(Interface param) throws POJOException
   {
      throw new POJOException();
   }
   
   public void method11(String param1, Collection param2) {}
   
   public void method12(String param1, Collection param2) throws POJOException
   {
      throw new POJOException();
   }
   
   public void method13(List<SuperValue> arg) {}
   
   public void method14(List<SuperValue> arg) throws POJOException
   {
      throw new POJOException();
   }
}