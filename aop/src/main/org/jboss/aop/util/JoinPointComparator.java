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

import org.jboss.aop.JoinPointInfo;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JoinPointComparator
{
   public static boolean hasSameChains(JoinPointInfo myInfo, JoinPointInfo otherInfo)
   {
      if (myInfo != null && otherInfo != null)
      {
         if (!hasSameInterceptors(myInfo, otherInfo))
         {
            return false;
         }
         if (!hasSameInterceptorChains(myInfo, otherInfo))
         {
            return false;
         }
      }
      else if (myInfo == null && otherInfo == null)
      {
         //same
      }
      else
      {
         return false;
      }

      return true;
   }
   
   public static boolean hasSameInterceptors(JoinPointInfo myInfo, JoinPointInfo otherInfo)
   {
      if (myInfo.getInterceptors() != null && otherInfo.getInterceptors() != null)
      {
         if (myInfo.getInterceptors().length != otherInfo.getInterceptors().length)
         {
            return false;
         }
         else if (myInfo.getInterceptors() == null && otherInfo.getInterceptors() == null)
         {
            //same
         }
         else
         {
            return false;
         }
      }
      return true;
   }

   public static boolean hasSameInterceptorChains(JoinPointInfo myInfo, JoinPointInfo otherInfo)
   {
      if (myInfo.getInterceptorChain() != null && otherInfo.getInterceptorChain() != null)
      {
         if (myInfo.getInterceptorChain().size() != otherInfo.getInterceptorChain().size())
         {
            return false;
         }
         else if (myInfo.getInterceptorChain() == null && otherInfo.getInterceptorChain() == null)
         {
            //same
         }
         else
         {
            return false;
         }
      }
      return true;
   }

}
