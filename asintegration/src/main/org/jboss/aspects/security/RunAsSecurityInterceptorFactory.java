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

import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.security.AuthenticationManager;
import org.jboss.security.RealmMapping;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Comment
 *
 * @author <a href="mailto:bdecoste@jboss.com">William DeCoste</a>
 * @version $Revision$
 *
 **/
public class RunAsSecurityInterceptorFactory implements AspectFactory
{
   public Object createPerVM()
   {
      throw new RuntimeException("PER_VM not supported for this interceptor factory, only PER_CLASS");
   }

   public Object createPerClass(Advisor advisor)
   {
      AuthenticationManager manager = (AuthenticationManager)advisor.getDefaultMetaData().getMetaData("security", "authentication-manager");
      RealmMapping mapping = (RealmMapping)advisor.getDefaultMetaData().getMetaData("security", "realm-mapping");
      if (manager == null)
      {
         SecurityDomain domain = (SecurityDomain)advisor.resolveAnnotation(SecurityDomain.class);
         if (domain == null) throw new RuntimeException("Unable to determine security domain");
         try
         {
            manager = (AuthenticationManager)new InitialContext().lookup("java:/jaas/" + domain.value());
         }
         catch (NamingException e)
         {
            throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
         }
         mapping = (RealmMapping)manager;
      }
      if (manager == null) throw new RuntimeException("Unable to find Security Domain");
      return new RunAsSecurityInterceptor(manager, mapping);
   }

   public Object createPerInstance(Advisor advisor, InstanceAdvisor instanceAdvisor)
   {
      throw new RuntimeException("PER_VM not supported for this interceptor factory, only PER_CLASS");
   }

   public Object createPerJoinpoint(Advisor advisor, Joinpoint jp)
   {
      throw new RuntimeException("PER_VM not supported for this interceptor factory, only PER_CLASS");
   }

   public Object createPerJoinpoint(Advisor advisor, InstanceAdvisor instanceAdvisor, Joinpoint jp)
   {
      throw new RuntimeException("PER_VM not supported for this interceptor factory, only PER_CLASS");
   }

   public String getName()
   {
      return getClass().getName();
   }
}
