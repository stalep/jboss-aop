/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.util;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AOPLock
{
   private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
   AOPLock parent;
   
   public AOPLock()
   {
      
   }
   
   public AOPLock(AOPLock parent)
   {
      this.parent = parent;
   }
   
   /**
    * Read-lock just this level
    */
   public final void lockRead()
   {
      lock.readLock().lock();
   }

   /**
    * Read-unlock just this level
    */
   protected final void unlockRead()
   {
      lock.readLock().unlock();
   }
   
   /**
    * Write-lock just this level
    */
   public final void lockWrite()
   {
      lock.writeLock().lock();
   }
   
   /**
    * Write-unlock this level
    */
   public final void unlockWrite()
   {
      lock.writeLock().unlock();
   }

   /**
    * Read-lock this level
    * @param if true, parent levels will be locked too
    */
   public void lockRead(boolean lockParents)
   {
      if (lockParents && parent != null)
      {
         parent.lockRead(lockParents);
      }
      lockRead();
   }
   
   /**
    * Read-unlock this level
    * @param if true, parent levels will be unlocked too
    */
   public void unlockRead(boolean lockParents)
   {
      unlockRead();
      if (lockParents && parent != null)
      {
         parent.unlockRead(lockParents);
      }
   }
   
   /**
    * Write-lock this level
    * @param if true, parent levels will be locked too
    */
   public void lockWrite(boolean lockParents)
   {
      if (lockParents && parent != null)
      {
         parent.lockWrite(lockParents);
      }
      lockWrite();
   }
   
   /**
    * Write-unlock this level
    * @param if true, parent levels will be unlocked too
    */
   public void unlockWrite(boolean lockParents)
   {
      unlockWrite();
      if (lockParents && parent != null)
      {
         parent.unlockWrite(lockParents);
      }
   }


}
