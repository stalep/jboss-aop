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

import junit.framework.Assert;

import org.jboss.aop.JoinPointInfo;
import org.jboss.aop.advice.annotation.Arg;
import org.jboss.aop.advice.annotation.JoinPoint;
import org.jboss.aop.advice.annotation.Return;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class GeneralAspect
{
   public static String before;
   public static String after;

   public static void clear()
   {
      before = null;
      after = null;
      POJO.joinPointRun = false;
   }
   

   public void before(@JoinPoint JoinPointInfo jp, @Arg SuperValue superValue, @Arg int i)
   {
      before = "before";
      Assert.assertFalse(POJO.joinPointRun);
   }
   
   public Object after(@JoinPoint JoinPointInfo jp, @Return Object ret,
         @Arg SuperValue superValue, @Arg int i)
   {
      after = "after";
      Assert.assertTrue(POJO.joinPointRun);
      return ret;
   }
}