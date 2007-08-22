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
 * Plain old java object used both on @Return parameter tests, and on advice return
 * type tests.
 * 
 * @author <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ReturnInvalidPOJO
{
   /* method1() */
   
   public void method1Before() {}

   /* method2() */
   
   public String method2Around2()
   {
      return "method2";
   }
   
   /* method3() */
   
   public String method3Before()
   {
      return "method3";
   }
   
   /* method5() */
   
   public String method5Finally5()
   {
      return "method5";
   }
   
   /* method6Around6() */
   
   public String method6Around6()
   {
      return "method6";
   }
   
   /* method8() */
   
   public SubValue method8Before()
   {
      return new SubValue(8);
   }
   
   /* method9() */
   
   public SubValue method9Around9()
   {
      return new SubValue(9);
   }
   
   public SubValue method9After9()
   {
      return new SubValue(9);
   }
   
   public SubValue method9Finally9()
   {
      return new SubValue(9);
   }
   
   /* method10() */
   
   public SuperValue method10Before()
   {
      return new SuperValue(10);
   }
   
   public SuperValue method10After10()
   {
      return new SuperValue(10);
   }
}