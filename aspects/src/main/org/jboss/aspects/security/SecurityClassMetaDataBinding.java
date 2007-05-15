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

import java.util.ArrayList;
import java.util.HashMap;
/**
 *
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 *
 */
public class SecurityClassMetaDataBinding extends org.jboss.aop.metadata.ClassMetaDataBinding
{
   protected ArrayList securityRoles = new ArrayList();
   protected ArrayList methodPermissions = new ArrayList();
   protected ArrayList methodExcludeList = new ArrayList();
   protected ArrayList constructorPermissions = new ArrayList();
   protected ArrayList constructorExcludeList = new ArrayList();
   protected HashMap fieldPermissions = new HashMap();
   protected ArrayList fieldExcludeList = new ArrayList();
   protected String runAs;
   protected String securityDomain;

   public SecurityClassMetaDataBinding(org.jboss.aop.metadata.ClassMetaDataLoader loader, String name, String group, String expr)
   {
      super(loader, name, group, expr);
   }

   public String getSecurityDomain() { return securityDomain; }
   public void setSecurityDomain(String domain) { securityDomain = domain; }

   public ArrayList getSecurityRoles() { return securityRoles; }
   public void setSecurityRoles(ArrayList roles) { securityRoles = roles; }

   public ArrayList getMethodPermissions() { return methodPermissions; }
   public void setMethodPermissions(ArrayList permissions) { methodPermissions = permissions; }

   public ArrayList getMethodExcludeList() { return methodExcludeList; }
   public void setMethodExcludeList(ArrayList list) { methodExcludeList = list; }

   public HashMap getFieldPermissions() { return fieldPermissions; }
   public void setFieldPermissions(HashMap permissions) { fieldPermissions = permissions; }

   public ArrayList getFieldExcludeList() { return fieldExcludeList; }
   public void setFieldExcludeList(ArrayList list) { fieldExcludeList = list; }

   public ArrayList getConstructorPermissions() { return constructorPermissions; }
   public void setConstructorPermissions(ArrayList permissions) { constructorPermissions = permissions; }

   public ArrayList getConstructorExcludeList() { return constructorExcludeList; }
   public void setConstructorExcludeList(ArrayList list) { constructorExcludeList = list; }

   public String getRunAs() { return runAs; }
   public void setRunAs(String as) { runAs = as; }

}
