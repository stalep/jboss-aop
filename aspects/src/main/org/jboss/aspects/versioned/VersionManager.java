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
package org.jboss.aspects.versioned;

import org.jboss.aop.InstanceAdvised;
import org.jboss.util.id.GUID;

/**
 *
 *  @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 *  @version $Revision$
 */
public abstract class VersionManager
{
   public static final String VERSION_MANAGER = "VERSION_MANAGER";
   public static final String VERSION_ID = "VERSION_ID";


   public static GUID getGUID(InstanceAdvised obj)
   {
      return (GUID)obj._getInstanceAdvisor().getMetaData().getMetaData(VERSION_MANAGER, VERSION_ID);
   }

   public abstract boolean isVersioned(Object advised);
   public abstract Object makeVersioned(Object obj) throws Exception;
}
