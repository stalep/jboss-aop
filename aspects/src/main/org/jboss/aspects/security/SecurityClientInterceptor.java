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
import java.io.ObjectStreamException;

/** 
 * The AuthenticationInterceptor authenticates the caller.
 *
 * @author <a href="bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public final class SecurityClientInterceptor implements org.jboss.aop.advice.Interceptor, java.io.Serializable
{
   private static final long serialVersionUID = -6366165968174741107L;

   public static final SecurityClientInterceptor singleton = new SecurityClientInterceptor();
   public String getName() { return "SecurityClientInterceptor"; }

   /**
    * Authenticates the caller using the principal and credentials in the 
    * Infocation if thre is a security manager and an invcocation method.
    */
   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      // Get Principal and credentials 
      Principal principal = SecurityActions.getPrincipal();
      if (principal != null) invocation.getMetaData().addMetaData("security", "principal", principal);

      Object credential = SecurityActions.getCredential();
      if (credential != null) invocation.getMetaData().addMetaData("security", "credential", credential);
      
      return invocation.invokeNext();
   }

   Object readResolve() throws ObjectStreamException {
      return singleton;
   }
}
