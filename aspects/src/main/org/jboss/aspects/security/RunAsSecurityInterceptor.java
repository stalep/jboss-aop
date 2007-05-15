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
import org.jboss.security.AuthenticationManager;
import org.jboss.security.RealmMapping;
import org.jboss.security.RunAsIdentity;

/** 
 * An interceptor that enforces the run-as identity declared by a bean.
 *
 * @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
 * @author <a href="mailto:dain@daingroup.com">Dain Sundstrom</a>.
 * @version $Revision$
 */
public class RunAsSecurityInterceptor implements org.jboss.aop.advice.Interceptor
{
   private static final Logger log = Logger.getLogger(RunAsSecurityInterceptor.class);
   
   protected AuthenticationManager securityManager;
   protected RealmMapping realmMapping;

   public RunAsSecurityInterceptor(AuthenticationManager manager, RealmMapping realmMapping)
   {
      this.securityManager = manager;
      this.realmMapping = realmMapping;
   }
   
   public String getName() { return "RunAsSecurityInterceptor"; }

   protected RunAsIdentity getRunAsIdentity(Invocation invocation)
   {
      RunAsIdentity identity = (RunAsIdentity)invocation.getMetaData("security", "run-as");
      if (identity == null) identity = getAnnotationRunAsIdentity(invocation);
      return identity;
   }

   protected RunAsIdentity getAnnotationRunAsIdentity(Invocation invocation)
   {
      RunAs runAs = (RunAs) invocation.resolveAnnotation(RunAs.class);
      if (runAs == null) return null;
      RunAsIdentity runAsRole = new RunAsIdentity(runAs.value(), null);
      return runAsRole;
   }
   public Object invoke(org.jboss.aop.joinpoint.Invocation invocation) throws Throwable
   {
      RunAsIdentity runAsRole = getRunAsIdentity(invocation);
      // If a run-as role was specified, push it so that any calls made
      // by this bean will have the runAsRole available for declarative
      // security checks.
      /*if(runAsRole != null)
      {*/
         SecurityActions.pushRunAsIdentity(runAsRole);
      /*}*/
     
      try
      {
         return invocation.invokeNext();
      }
      finally
      {
        /* if(runAsRole != null)
         {*/
            SecurityActions.popRunAsIdentity();
         /*}*/
      }
   }
}
