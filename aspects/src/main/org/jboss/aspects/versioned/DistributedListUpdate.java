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
import org.jboss.util.id.GUID;

import java.util.ArrayList;

public class DistributedListUpdate implements java.io.Serializable, DistributedUpdate
{
   private static final long serialVersionUID = -1313127751220246092L;

   public GUID guid;
   public long versionId;
   /**
    * This is a list of VersionReference's or serializable objects
    */
   public ArrayList listUpdates;

   public DistributedListUpdate() {}

   public DistributedListUpdate(GUID guid, ArrayList updates, long versionId)
   {
      this.guid = guid;
      this.listUpdates = updates;
      this.versionId = versionId;
   }
   
   public GUID getGUID() { return guid; }

}
