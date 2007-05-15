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

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.logging.Logger;
import org.jboss.security.AnybodyPrincipal;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.NobodyPrincipal;
import org.jboss.security.RealmMapping;
import org.jboss.security.RunAsIdentity;
import org.jboss.security.SimplePrincipal;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * The RoleBasedAuthorizationInterceptor checks that the caller principal is
 * authorized to call a method by verifing that it contains at least one
 * of the required roled.
 *
 * @author <a href="bill@jboss.org">Bill Burke</a>
 * @author <a href="on@ibis.odessa.ua">Oleg Nitz</a>
 * @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
 * @author <a href="mailto:dain@daingroup.com">Dain Sundstrom</a>.
 * @version $Revision$
 */
public class RoleBasedAuthorizationInterceptor implements org.jboss.aop.advice.Interceptor
{
   protected Logger log = Logger.getLogger(this.getClass());
   protected AuthenticationManager securityManager;
   protected RealmMapping realmMapping;

   public RoleBasedAuthorizationInterceptor(AuthenticationManager manager, RealmMapping realmMapping)
   {
      this.securityManager = manager;
      this.realmMapping = realmMapping;
   }

   public String getName()
   {
      return "RoleBasedAuthorizationInterceptor";
   }

   protected Set getRoleSet(Invocation invocation)
   {
      Set roles = (Set) invocation.getMetaData("security", "roles");
      if (roles == null) roles = getAnnotationRoleSet(invocation);
      return roles;

   }

   protected Set getAnnotationRoleSet(Invocation invocation)
   {
      HashSet set = new HashSet();
      Exclude exclude = (Exclude) invocation.resolveAnnotation(Exclude.class);
      if (exclude != null)
      {
         set.add(NobodyPrincipal.NOBODY_PRINCIPAL);
         return set;
      }
      Unchecked unchecked = (Unchecked) invocation.resolveAnnotation(Unchecked.class);
      if (unchecked != null)
      {
         set.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
         return set;
      }
      Permissions permissions = (Permissions) invocation.resolveAnnotation(Permissions.class);
      if (permissions == null)
      {
         // Default behavior is unchecked
         set.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
         return set;
      }
      for (int i = 0; i < permissions.value().length; i++)
      {
         set.add(new SimplePrincipal(permissions.value()[i]));
      }
      return set;
   }
   
   /**
    * Check if the principal is authorized to call the method by verifying that
    * the it containes at least one of the required roles.
    */
   public Object invoke(Invocation invocation) throws Throwable
   {
      // If there is not a security manager then there is no authorization
      // required
      if (securityManager == null)
      {
         return invocation.invokeNext();
      }

      if (realmMapping == null)
      {
         throw new SecurityException("Role mapping manager has not been set");
      }

      Set roles = getRoleSet(invocation);
      if (roles == null)
      {
         /*
           REVISIT: for better message
         String message = "No method permissions assigned. to " +
               "method=" + invocation.getMethod().getName() +
               ", interface=" + invocation.getType();
         */
         String message = "No method permissions assigned.";
         log.error(message);
         throw new SecurityException(message);
      }

      // Check if the caller is allowed to access the method
      RunAsIdentity callerRunAsIdentity = SecurityActions.peekRunAsIdentity();
      if (roles.contains(AnybodyPrincipal.ANYBODY_PRINCIPAL) == false)
      {
         // The caller is using a the caller identity
         if (callerRunAsIdentity == null)
         {
            Principal principal = SecurityActions.getPrincipal();
            // Now actually check if the current caller has one of the required method roles
            if (realmMapping.doesUserHaveRole(principal, roles) == false)
            {
               Set userRoles = realmMapping.getUserRoles(principal);
               String msg = "Insufficient permissions, principal=" + principal
               + ", requiredRoles=" + roles + ", principalRoles=" + userRoles;
               log.error(msg);
               throw new SecurityException(msg);
            }
         }

         // The caller is using a run-as identity
         else
         {
            // Check that the run-as role is in the set of method roles
            if (callerRunAsIdentity.doesUserHaveRole(roles) == false)
            {
               String msg = "Insufficient permissions, runAsPrincipal=" + callerRunAsIdentity.getName()
               + ", requiredRoles=" + roles + ", runAsRoles=" + callerRunAsIdentity.getRunAsRoles();
               log.error(msg);
               throw new SecurityException(msg);
            }
         }
      }
      return invocation.invokeNext();
   }

}
