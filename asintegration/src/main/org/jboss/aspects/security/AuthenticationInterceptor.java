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

import java.security.Principal;
import java.security.GeneralSecurityException;
import javax.security.auth.Subject;
import org.jboss.logging.Logger;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.RunAsIdentity;

/**
 * The AuthenticationInterceptor authenticates the caller.
 *
 * @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
 * @author <a href="bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AuthenticationInterceptor implements org.jboss.aop.advice.Interceptor
{
   protected Logger log = Logger.getLogger(this.getClass());
   protected AuthenticationManager authenticationManager;

   public AuthenticationInterceptor(AuthenticationManager manager)
   {
      authenticationManager = manager;
   }

   public String getName()
   {
      return "AuthenticationInterceptor";
   }

   protected void handleGeneralSecurityException(GeneralSecurityException gse)
   {
      throw new SecurityException(gse.getMessage());
   }

   /**
    * Authenticates the caller using the principal and credentials in the
    * Infocation if thre is a security manager and an invcocation method.
    */
   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      try
      {
         authenticate(invocation);
      }
      catch (GeneralSecurityException gse)
      {
         handleGeneralSecurityException(gse);
      }

      Object oldDomain = SecurityContext.currentDomain.get();
      try
      {
         SecurityContext.currentDomain.set(authenticationManager);
         return invocation.invokeNext();
      }
      finally
      {
         SecurityContext.currentDomain.set(oldDomain);
         
         // so that the principal doesn't keep being associated with thread if the thread is pooled
         // only pop if it's been pushed
         RunAsIdentity callerRunAsIdentity = SecurityActions.peekRunAsIdentity();
         if (authenticationManager == null || callerRunAsIdentity == null)
         {
            SecurityActions.popSubjectContext(); 
         } 
         if(authenticationManager != null)
            SecurityActions.clearSecurityContext();
     
         if (invocation.getMetaData("security", "principal") != null)
         {
            SecurityActions.setPrincipal(null);
            SecurityActions.setCredential(null);
         }
      }
   }

   protected void authenticate(org.jboss.aop.joinpoint.Invocation invocation) throws Exception
   {
      Principal principal = (Principal) invocation.getMetaData("security", "principal");
      Object credential = invocation.getMetaData("security", "credential");
      
      if (principal == null)
      {
         principal = SecurityActions.getPrincipal();
      }
      if (credential == null)
      {
         credential = SecurityActions.getCredential();
      }

      if (authenticationManager == null)
      {
         SecurityActions.pushSubjectContext(principal, credential, null);
         return;
      }


      // authenticate the current principal
      RunAsIdentity callerRunAsIdentity = SecurityActions.peekRunAsIdentity();
      if (callerRunAsIdentity == null)
      {
         // Check the security info from the method invocation
         Subject subject = new Subject();
         if (authenticationManager.isValid(principal, credential, subject) == false)
         {
            /* todo support CSIV2 authenticationObserver
            // Notify authentication observer
            if (authenticationObserver != null)
               authenticationObserver.authenticationFailed();
               */
            // Check for the security association exception
            Exception ex = SecurityActions.getContextException();
            if (ex != null)
               throw ex;
            // Else throw a generic SecurityException
            String msg = "Authentication exception, principal=" + principal;
            SecurityException e = new SecurityException(msg);
            throw e;
         }
         else
         {
            SecurityActions.pushSubjectContext(principal, credential, subject);
            SecurityActions.establishSecurityContext(authenticationManager.getSecurityDomain(),
                  principal, credential, subject);
            if (log.isTraceEnabled())
            {
               log.trace("Authenticated  principal=" + principal);
            }
         }
      }
   }
}
