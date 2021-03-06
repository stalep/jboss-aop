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
package org.jboss.test.aop.rebuildingchain;


/**
 * This thread executes a constructor call by method joinpoint.
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ConstructorCallByMethodSyncThread extends SyncThread
{
   public static final String POINTCUT = "call(org.jboss.test.aop.rebuildingchain.ConstructorCallByMethodSyncThread$Pojo->new())";
   public static final String NAME = "ConstructorCallByMethod";
   
   protected void invokeJoinPoint()
   {
      new Pojo();
   }
   
   private static class Pojo
   {
      // TODO JBAOP-567 Remove this constructor.
      public Pojo(){}
   }
}