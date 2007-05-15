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
package org.jboss.aspects.security;

import java.util.Set;

public class SecurityConstructorConfig extends org.jboss.aop.metadata.ConstructorConfig
{
   /** The unchecked element specifies that a constructor is not checked for
    * authorization by the container prior to invocation of the constructor.
    * Used in: constructor-permission
    */
   private boolean unchecked = false;
   /** The exclude-list element defines a set of constructors which the Assembler
    * marks to be uncallable. It contains one or more constructors. If the constructor
    * permission relation contains constructors that are in the exclude list, the
    * Deployer should consider those constructors to be uncallable.
    */
   private boolean excluded = false;
   private Set permissions;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------
   public SecurityConstructorConfig()
   {
   }

   // Public --------------------------------------------------------

   public boolean isUnchecked()
   {
      return unchecked;
   }

   public boolean isExcluded()
   {
      return excluded;
   }

   public Set getRoles()
   {
      return permissions;
   }

   public void setRoles(Set perm)
   {
      permissions = perm;
   }

   public void setUnchecked()
   {
      unchecked = true;
   }

   public void setExcluded()
   {
      excluded = true;
   }
}
