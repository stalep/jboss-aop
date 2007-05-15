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

import org.jboss.security.RealmMapping;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SimplePrincipal;

import java.security.Principal;
import java.util.HashSet;

/**
 * Obtain security information based on the current security domain.
 *
 * 
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class SecurityContext
{
   protected static ThreadLocal currentDomain = new ThreadLocal();
   
   public static ThreadLocal getCurrentDomain()
   {
      return currentDomain;
   }

   /**
    * Get the caller's principal
    * @return
    */
   public static Principal getCallerPrincipal()
   {
      return SecurityActions.getCallerPrincipal();
   }

   /**
    * Get the current principal.  Could be run-as, or propagated
    * @return
    */
   public static Principal getCurrentPrincipal()
   {
     return SecurityActions.getPrincipal();
   }

   /**
    * Is the caller's security identity within the role
    *
    * Does not include current run-as
    * @param roleName
    * @return
    */
   public static boolean isCallerInRole(String roleName)
   {
      return isInRole(getCallerPrincipal(), roleName);
   }

   /**
    * Checks current identity is within roleName
    *
    * Does include current run-as
    * 
    * @param roleName
    * @return
    */
   public static boolean isCurrentInRole(String roleName)
   {
      return isInRole(getCurrentPrincipal(), roleName);
   }

   private static boolean isInRole(Principal principal, String roleName)
   {
      RealmMapping rm = (RealmMapping)currentDomain.get();
      if (rm == null) return false;

      HashSet set = new HashSet();
      set.add(new SimplePrincipal(roleName));

      if (principal instanceof RunAsIdentity)
      {
         return ((RunAsIdentity)principal).doesUserHaveRole(set);
      }
      else
      {
         return rm.doesUserHaveRole(principal, set);
      }
   }


}
