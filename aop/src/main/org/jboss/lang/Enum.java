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
package org.jboss.lang;

/**
 * Base class for JDK 1.4 Enums
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 *
 **/
public abstract class Enum implements java.io.Serializable
{
   protected final transient String name;
   protected final int ordinal;

   protected Enum(String name, int ordinal)
   {
      this.name = name;
      this.ordinal = ordinal;
   }
   public String toString()
   {
      return name;
   }

   public String name()
   {
      return name;
   }

   public int ordinal()
   {
      return ordinal;
   }

   public boolean equals(Object o)
   {
      if (o == this) return true;
      if (o == null) return false;
      if (!(o instanceof Enum)) return false;
      if (!o.getClass().equals(this.getClass())) return false;
      Enum en = (Enum)o;
      return en.ordinal == this.ordinal;
   }

   public int hashCode()
   {
      return name.hashCode();
   }

}
