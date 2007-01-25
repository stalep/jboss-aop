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
 * Plain old java object used on @Args parameter tests.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ArgsPOJO
{
   public Object field1;
   
   public String field2;
   
   public int field3;
   
   public static boolean field4;
   
   public int bunch(int x, double y, float z, String str, int q)
   {
      return x + (int) y + (int) z + q;
   }
   
   public void method1(String param1, int param2, boolean param3, ArgsPOJO[] param4)
   {}
   
   public void method2(String param1, int param2, boolean param3, ArgsPOJO[] param4)
   {}
   
   public void method3(short param1, long param2) throws POJOException
   {
      throw new POJOException();
   }
   
   public void method4() throws POJOException
   {
      this.method3((short) -4, (long) 4);
   }
}