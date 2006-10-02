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
package org.jboss.aop.advice;

import java.io.ObjectStreamException;

/**
 * Defines the lifecycle of an aspect or interceptor instance
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class Scope extends org.jboss.lang.Enum
{
   static final long serialVersionUID = 7873910692644730377L;

   private Scope(String name, int v)
   {
      super(name, v);
   }

   public static final Scope PER_VM = new Scope("PER_VM", 0);
   public static final Scope PER_CLASS = new Scope("PER_CLASS", 1);
   public static final Scope PER_INSTANCE = new Scope("PER_INSTANCE", 2);
   public static final Scope PER_JOINPOINT = new Scope("PER_JOINPOINT", 3);
   public static final Scope PER_CLASS_JOINPOINT = new Scope("PER_CLASS_JOINPOINT", 4);
   private static final Scope[] values = {PER_VM, PER_CLASS, PER_INSTANCE, PER_JOINPOINT, PER_CLASS_JOINPOINT};

   Object readResolve() throws ObjectStreamException
   {
      return values[ordinal];
   }

}
