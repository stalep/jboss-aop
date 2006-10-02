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
package org.jboss.test.aop.beforeafter;

import org.jboss.aop.ConstructorInfo;
import org.jboss.aop.FieldInfo;
import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.MethodInfo;
import org.jboss.test.aop.construction.SuperPOJO;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class ArgsAspect
{

   public static String before;
   public static String after;
   public static String throwing;


   public static void clear()
   {
      before = null;
      after = null;
      throwing = null;
   }
   
   public void before(boolean b, int i)
   {
      before = "before1";
   }
   
   public void before(MethodInfo mjp, int i)
   {
      before = "before2";
   }
   
   public void before(FieldInfo fjp, int i)
   {
      before = "before3";
   }
   
   public void before(FieldInfo fjp)
   {
      before = "before4";
   }
   
   public void before(FieldInfo fjp, SubValue val)
   {
      before = "before5";
   }
   
   public void before(SubValue sup, SubValue sub)
   {
      before = "before6";
   }
   
   public void before(SuperValue sup, SubValue sub)
   {
      before = "before7";
   }
   
   public POJO after(MethodInfo mjp, POJO ret, int i, long l)
   {
      after = "after1";
      return ret;
   }
   
   public POJO after(ConstructorInfo cjp, POJO ret)
   {
      after = "after2";
      return ret;
   }
   
   //This should be able to handle both reads and writes
   public int after(FieldInfo fp, int i)
   {
      after = "after3";
      return i;
   }
   
   //This should be able to handle both reads and writes
   public SubValue after(FieldInfo fp, SubValue ret)
   {
      after = "after4";
      ret.doubleValue();
      return ret;
   }
      
   //This should be able to handle both reads and writes
   public SuperValue after(SuperValue ret)
   {
      after = "after5";
      ret.doubleValue();
      return ret;
   }

   public SubValue after(SuperValue ret, SuperValue sup, SuperValue sup2)
   {
      throw new RuntimeException("Should not be called");
   }
   
   public SuperValue after(SuperValue ret, SubValue sup, SuperValue sub)
   {
      after = "after6";
      ret.doubleValue();
      return new SubValue(ret.getValue());
   }
   
   public void after(SuperValue sup, SuperValue sup2)
   {
      after = "after7";
   }
   
   public Throwable throwing(Throwable t, int i)
   {
      throwing = "throwing1";
      return t;
   }
   
   public Throwable throwing(MethodInfo mjp, Throwable t)
   {
      throwing = "throwing2";
      return t;
   }
   
   public Throwable throwing(MethodInfo mjp, Throwable t, int i)
   {
      throwing = "throwing3";
      return t;
   }
}
