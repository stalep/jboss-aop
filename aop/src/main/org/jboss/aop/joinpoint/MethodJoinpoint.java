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
package org.jboss.aop.joinpoint;

import java.lang.reflect.Method;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class MethodJoinpoint implements Joinpoint
{
   private final Method method;
   int hashCode;

   public MethodJoinpoint(Method method)
   {
      this.method = method;
   }

   public boolean equals(Object o)
   {
      if (o == null) return false;
      if (o == this) return true;
      if (!(o instanceof MethodJoinpoint)) return false;
      MethodJoinpoint jp = (MethodJoinpoint)o;
      return method.equals(jp.method);
   }

   public int hashCode()
   {
      if (hashCode == 0)
      {
         hashCode = method.hashCode();
      }
      return hashCode;
   }

   public Method getMethod()
   {
      return method;
   }
   
   public String toString()
   {
      return method.toString();
   }
}
