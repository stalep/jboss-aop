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

import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.jboss.aop.Advisor;
import org.jboss.aop.metadata.ClassMetaDataBinding;
import org.jboss.aop.util.PayloadKey;
import org.jboss.aop.util.XmlHelper;
import org.jboss.security.AnybodyPrincipal;
import org.jboss.security.NobodyPrincipal;
import org.jboss.security.SimplePrincipal;
import org.w3c.dom.Element;

import javax.naming.InitialContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This interceptor handles authentication creation and the initial
 * population of class metadata
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class SecurityClassMetaDataLoader implements org.jboss.aop.metadata.ClassMetaDataLoader
{
   public org.jboss.aop.metadata.ClassMetaDataBinding importMetaData(Element element, String name, String group, String classExpr) throws Exception
   {
      SecurityClassMetaDataBinding data = new SecurityClassMetaDataBinding(this, name, group, classExpr);
      ArrayList securityRoles = loadSecurityRoles(element);
      ArrayList methodPermissions = loadMethodPermissions(element);
      ArrayList methodExcludeList = loadMethodExcludeList(element);
      HashMap fieldPermissions = loadFieldPermissions(element);
      ArrayList fieldExcludeList = loadFieldExcludeList(element);
      ArrayList constructorPermissions = loadConstructorPermissions(element);
      ArrayList constructorExcludeList = loadConstructorExcludeList(element);
      String runAs = loadRunAs(element);

      String securityDomain = XmlHelper.getOptionalChildContent(element, "security-domain");
      if (securityDomain == null) throw new RuntimeException("you must define a security-domain");
      data.setSecurityDomain(securityDomain);
      data.setSecurityRoles(securityRoles);
      data.setMethodPermissions(methodPermissions);
      data.setMethodExcludeList(methodExcludeList);
      data.setFieldPermissions(fieldPermissions);
      data.setFieldExcludeList(fieldExcludeList);
      data.setConstructorPermissions(constructorPermissions);
      data.setConstructorExcludeList(constructorExcludeList);
      data.setRunAs(runAs);
      return data;
   }

   public void bind(Advisor advisor, org.jboss.aop.metadata.ClassMetaDataBinding data, Method[] methods, Field[] fields, Constructor[] constructors) throws Exception
   {
      SecurityClassMetaDataBinding meta = (SecurityClassMetaDataBinding) data;
      try
      {
         String securityDomain = "java:/jaas/" + meta.getSecurityDomain();
         Object domain = new InitialContext().lookup(securityDomain);
         advisor.getDefaultMetaData().addMetaData("security", "authentication-manager", domain, PayloadKey.TRANSIENT);
         advisor.getDefaultMetaData().addMetaData("security", "realm-mapping", domain, PayloadKey.TRANSIENT);
      }
      catch (Exception ex)
      {
         throw new RuntimeException("failed to load security domain: " + meta.getSecurityDomain(), ex);
      }

      for (int i = 0; i < methods.length; i++)
      {
         Set permissions = getMethodPermissions(methods[i], meta);
         if (permissions != null)
         {
            advisor.getMethodMetaData().addMethodMetaData(methods[i], "security", "roles", permissions, PayloadKey.TRANSIENT);
         }
      }

      for (int i = 0; i < fields.length; i++)
      {
         Set permissions = getFieldPermissions(fields[i], meta);
         if (permissions != null)
         {
            advisor.getFieldMetaData().addFieldMetaData(fields[i], "security", "roles", permissions, PayloadKey.TRANSIENT);
         }
      }

      for (int i = 0; i < constructors.length; i++)
      {
         Set permissions = getConstructorPermissions(constructors[i], meta);
         if (permissions != null)
         {
            advisor.getConstructorMetaData().addConstructorMetaData(constructors[i], "security", "roles", permissions, PayloadKey.TRANSIENT);
         }
      }

      if (meta.getRunAs() != null)
      {
         advisor.getDefaultMetaData().addMetaData("security", "run-as", new SimplePrincipal(meta.getRunAs()), PayloadKey.TRANSIENT);
      }
   }

   public Set getMethodPermissions(Method method, SecurityClassMetaDataBinding meta)
   {
      Set result = new HashSet();
      // First check the excluded method list as this takes priority
      // over all other assignments
      Iterator iterator = meta.getMethodExcludeList().iterator();
      while (iterator.hasNext())
      {
         SecurityMethodConfig m = (SecurityMethodConfig) iterator.next();
         if (m.patternMatches(method))
         {
            /* No one is allowed to execute this method so add a role that
               fails to equate to any Principal or Principal name and return.
               We don't return null to differentiate between an explicit
               assignment of no access and no assignment information.
            */
            result.add(NobodyPrincipal.NOBODY_PRINCIPAL);
            return result;
         }
      }

      // Check the permissioned methods list
      iterator = meta.getMethodPermissions().iterator();
      while (iterator.hasNext())
      {
         SecurityMethodConfig m = (SecurityMethodConfig) iterator.next();
         if (m.patternMatches(method))
         {
            // If this is an unchecked method anyone can access it so
            // set the result set to a role that equates to any Principal
            // or Principal name and return.
            if (m.isUnchecked())
            {
               result.clear();
               result.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
               break;
            }
            // Else, add all roles
            else
            {
               Iterator rolesIterator = m.getRoles().iterator();
               while (rolesIterator.hasNext())
               {
                  String roleName = (String) rolesIterator.next();
                  result.add(new SimplePrincipal(roleName));
               }
            }
         }
      }

      // If no permissions were assigned to the method return null to
      // indicate no access
      if (result.isEmpty())
      {
         result = null;
      }

      return result;
   }


   public Set getFieldPermissions(Field field, SecurityClassMetaDataBinding meta)
   {
      String fieldName = field.getName();
      Set result = new HashSet();
      // First check the excluded method list as this takes priority
      // over all other assignments
      Iterator iterator = meta.getFieldExcludeList().iterator();
      while (iterator.hasNext())
      {
         String expr = (String) iterator.next();
         if (expr.equals("*") || expr.equals(fieldName))
         {
            /* No one is allowed to execute this method so add a role that
               fails to equate to any Principal or Principal name and return.
               We don't return null to differentiate between an explicit
               assignment of no access and no assignment information.
            */
            result.add(NobodyPrincipal.NOBODY_PRINCIPAL);
            return result;
         }
      }

      // Check the permissioned methods list
      iterator = meta.getFieldPermissions().keySet().iterator();
      while (iterator.hasNext())
      {
         String expr = (String) iterator.next();

         if (expr.equals("*") || expr.equals(fieldName))
         {
            Object permission = meta.getFieldPermissions().get(expr);
            // If this is an unchecked method anyone can access it so
            // set the result set to a role that equates to any Principal
            // or Principal name and return.
            if (permission instanceof Boolean)
            {
               result.clear();
               result.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
               break;
            }
            // Else, add all roles
            else
            {
               Set roles = (Set) permission;
               Iterator rolesIterator = roles.iterator();
               while (rolesIterator.hasNext())
               {
                  String roleName = (String) rolesIterator.next();
                  result.add(new SimplePrincipal(roleName));
               }
            }
         }
      }

      // If no permissions were assigned to the method return null to
      // indicate no access
      if (result.isEmpty())
      {
         result = null;
      }

      return result;
   }


   protected String loadRunAs(Element element)
   throws Exception
   {
      Element securityIdentityElement = XmlHelper.getOptionalChild(element,
      "security-identity");
      if (securityIdentityElement == null) return null;
      Element callerIdent = XmlHelper.getOptionalChild(securityIdentityElement, "use-caller-identity");
      Element runAs = XmlHelper.getOptionalChild(securityIdentityElement, "run-as");
      if (callerIdent == null && runAs == null)
         throw new RuntimeException("security-identity: either use-caller-identity or run-as must be specified");
      if (callerIdent != null && runAs != null)
         throw new RuntimeException("security-identity: only one of use-caller-identity or run-as can be specified");

      String runAsRoleName = null;
      if (runAs != null)
      {
         runAsRoleName = XmlHelper.getElementContent(XmlHelper.getUniqueChild(runAs, "role-name"));
      }
      return runAsRoleName;
   }


   protected ArrayList loadSecurityRoles(Element assemblyDescriptor) throws Exception
   {
      ArrayList securityRoles = new ArrayList();
      // set the security roles (optional)
      Iterator iterator = XmlHelper.getChildrenByTagName(assemblyDescriptor, "security-role");
      while (iterator.hasNext())
      {
         Element securityRole = (Element) iterator.next();
         try
         {
            String role = XmlHelper.getUniqueChildContent(securityRole, "role-name");
            securityRoles.add(role);
         }
         catch (Exception e)
         {
            throw new RuntimeException("Error in metadata " +
            "for security-role: ", e);
         }
      }
      return securityRoles;
   }

   protected ArrayList loadMethodPermissions(Element assemblyDescriptor) throws Exception
   {
      ArrayList permissionMethods = new ArrayList();
      // set the method permissions (optional)
      Iterator iterator = XmlHelper.getChildrenByTagName(assemblyDescriptor,
      "method-permission");
      while (iterator.hasNext())
      {
         Element methodPermission = (Element) iterator.next();
         // Look for the unchecked element
         Element unchecked = XmlHelper.getOptionalChild(methodPermission,
         "unchecked");

         boolean isUnchecked = false;
         Set roles = null;
         if (unchecked != null)
         {
            isUnchecked = true;
         }
         else
         {
            // Get the role-name elements
            roles = new HashSet();
            Iterator rolesIterator = XmlHelper.getChildrenByTagName(methodPermission, "role-name");
            while (rolesIterator.hasNext())
            {
               roles.add(XmlHelper.getElementContent((Element) rolesIterator.next()));
            }
            if (roles.size() == 0)
               throw new RuntimeException("An unchecked " +
               "element in security metadata or one or more role-name elements " +
               "must be specified in method-permission");
         }
         
         // find the methods
         Iterator methods = XmlHelper.getChildrenByTagName(methodPermission,
         "method");
         while (methods.hasNext())
         {
            // load the method
            SecurityMethodConfig method = new SecurityMethodConfig();
            method.importXml((Element) methods.next());
            if (isUnchecked)
            {
               method.setUnchecked();
               permissionMethods.add(0, method);
            }
            else
            {
               method.setRoles(roles);
               permissionMethods.add(method);
            }
         }
      }
      return permissionMethods;
   }

   protected ArrayList loadMethodExcludeList(Element assemblyDescriptor) throws Exception
   {
      ArrayList excluded = new ArrayList();
      // Get the exclude-list methods
      Element excludeList = XmlHelper.getOptionalChild(assemblyDescriptor,
      "exclude-list");
      if (excludeList != null)
      {
         Iterator iterator = XmlHelper.getChildrenByTagName(excludeList, "method");
         while (iterator.hasNext())
         {
            Element methodInf = (Element) iterator.next();
            // load the method
            SecurityMethodConfig method = new SecurityMethodConfig();
            method.importXml(methodInf);
            method.setExcluded();
            excluded.add(method);
         }
      }
      return excluded;
   }

   protected HashMap loadFieldPermissions(Element assemblyDescriptor) throws Exception
   {
      HashMap permissionFields = new HashMap();
      // set the field permissions (optional)
      Iterator iterator = XmlHelper.getChildrenByTagName(assemblyDescriptor,
      "field-permission");
      while (iterator.hasNext())
      {
         Element fieldPermission = (Element) iterator.next();
         // Look for the unchecked element
         Element unchecked = XmlHelper.getOptionalChild(fieldPermission,
         "unchecked");

         boolean isUnchecked = false;
         Set roles = null;
         if (unchecked != null)
         {
            isUnchecked = true;
         }
         else
         {
            // Get the role-name elements
            roles = new HashSet();
            Iterator rolesIterator = XmlHelper.getChildrenByTagName(fieldPermission, "role-name");
            while (rolesIterator.hasNext())
            {
               roles.add(XmlHelper.getElementContent((Element) rolesIterator.next()));
            }
            if (roles.size() == 0)
               throw new RuntimeException("An unchecked " +
               "element in security metadata or one or more role-name elements " +
               "must be specified in field-permission");
         }
         
         // find the fields
         Iterator fields = XmlHelper.getChildrenByTagName(fieldPermission,
         "field");
         while (fields.hasNext())
         {
            // load the field
            Element field = (Element) fields.next();
            String fieldName = XmlHelper.getElementContent(XmlHelper.getUniqueChild(field, "field-name"));

            if (isUnchecked)
            {
               permissionFields.put(fieldName, Boolean.TRUE); // mark as unchecked
            }
            else
            {

               Object permission = permissionFields.get(fieldName);
               if (permission != null && permission instanceof Boolean) //unchecked
               {
                  continue;
               }
               if (permission != null)
               {
                  Set curr = (Set) permission;
                  curr.addAll(roles);
               }
               else
               {
                  permissionFields.put(fieldName, new HashSet(roles));
               }
            }
         }
      }
      return permissionFields;
   }

   protected ArrayList loadFieldExcludeList(Element assemblyDescriptor) throws Exception
   {
      ArrayList excluded = new ArrayList();
      // Get the exclude-list fields
      Element excludeList = XmlHelper.getOptionalChild(assemblyDescriptor,
      "exclude-list");
      if (excludeList != null)
      {
         Iterator iterator = XmlHelper.getChildrenByTagName(excludeList, "field");
         while (iterator.hasNext())
         {
            Element fieldInf = (Element) iterator.next();
            String fieldName = XmlHelper.getElementContent(XmlHelper.getUniqueChild(fieldInf, "field-name"));
            excluded.add(fieldName);
         }
      }
      return excluded;
   }

   protected ArrayList loadConstructorPermissions(Element assemblyDescriptor) throws Exception
   {
      ArrayList permissionConstructors = new ArrayList();
      // set the constructor permissions (optional)
      Iterator iterator = XmlHelper.getChildrenByTagName(assemblyDescriptor,
      "constructor-permission");
      while (iterator.hasNext())
      {
         Element constructorPermission = (Element) iterator.next();
         // Look for the unchecked element
         Element unchecked = XmlHelper.getOptionalChild(constructorPermission,
         "unchecked");

         boolean isUnchecked = false;
         Set roles = null;
         if (unchecked != null)
         {
            isUnchecked = true;
         }
         else
         {
            // Get the role-name elements
            roles = new HashSet();
            Iterator rolesIterator = XmlHelper.getChildrenByTagName(constructorPermission, "role-name");
            while (rolesIterator.hasNext())
            {
               roles.add(XmlHelper.getElementContent((Element) rolesIterator.next()));
            }
            if (roles.size() == 0)
               throw new RuntimeException("An unchecked " +
               "element in security metadata or one or more role-name elements " +
               "must be specified in constructor-permission");
         }
         
         // find the constructors
         Iterator constructors = XmlHelper.getChildrenByTagName(constructorPermission,
         "constructor");
         while (constructors.hasNext())
         {
            // load the constructor
            SecurityConstructorConfig constructor = new SecurityConstructorConfig();
            constructor.importXml((Element) constructors.next());
            if (isUnchecked)
            {
               constructor.setUnchecked();
               permissionConstructors.add(0, constructor);
            }
            else
            {
               constructor.setRoles(roles);
               permissionConstructors.add(constructor);
            }
         }
      }
      return permissionConstructors;
   }

   protected ArrayList loadConstructorExcludeList(Element assemblyDescriptor) throws Exception
   {
      ArrayList excluded = new ArrayList();
      // Get the exclude-list constructors
      Element excludeList = XmlHelper.getOptionalChild(assemblyDescriptor,
      "exclude-list");
      if (excludeList != null)
      {
         Iterator iterator = XmlHelper.getChildrenByTagName(excludeList, "constructor");
         while (iterator.hasNext())
         {
            Element constructorInf = (Element) iterator.next();
            // load the constructor
            SecurityConstructorConfig constructor = new SecurityConstructorConfig();
            constructor.importXml(constructorInf);
            constructor.setExcluded();
            excluded.add(constructor);
         }
      }
      return excluded;
   }

   public Set getConstructorPermissions(Constructor constructor, SecurityClassMetaDataBinding meta)
   {
      Set result = new HashSet();
      // First check the excluded constructor list as this takes priority
      // over all other assignments
      Iterator iterator = meta.getConstructorExcludeList().iterator();
      while (iterator.hasNext())
      {
         SecurityConstructorConfig m = (SecurityConstructorConfig) iterator.next();
         if (m.patternMatches(constructor))
         {
            /* No one is allowed to execute this constructor so add a role that
               fails to equate to any Principal or Principal name and return.
               We don't return null to differentiate between an explicit
               assignment of no access and no assignment information.
            */
            result.add(NobodyPrincipal.NOBODY_PRINCIPAL);
            return result;
         }
      }

      // Check the permissioned constructors list
      iterator = meta.getConstructorPermissions().iterator();
      while (iterator.hasNext())
      {
         SecurityConstructorConfig m = (SecurityConstructorConfig) iterator.next();
         if (m.patternMatches(constructor))
         {
            // If this is an unchecked constructor anyone can access it so
            // set the result set to a role that equates to any Principal
            // or Principal name and return.
            if (m.isUnchecked())
            {
               result.clear();
               result.add(AnybodyPrincipal.ANYBODY_PRINCIPAL);
               break;
            }
            // Else, add all roles
            else
            {
               Iterator rolesIterator = m.getRoles().iterator();
               while (rolesIterator.hasNext())
               {
                  String roleName = (String) rolesIterator.next();
                  result.add(new SimplePrincipal(roleName));
               }
            }
         }
      }

      // If no permissions were assigned to the constructor return null to
      // indicate no access
      if (result.isEmpty())
      {
         result = null;
      }

      return result;
   }


   /**
    * This is minimal stuff as Instrumentor requires that ClassMetadata be bound at least at the group
    * level for every class, method, field, and constructor so that annotated joinpoints can be done
    *
    * @param advisor
    * @param data
    * @param methods
    * @param fields
    * @param constructors
    * @throws Exception
    */
   public void bind(Advisor advisor, ClassMetaDataBinding data, CtMethod[] methods, CtField[] fields, CtConstructor[] constructors) throws Exception
   {
      SecurityClassMetaDataBinding meta = (SecurityClassMetaDataBinding) data;
      for (int i = 0; i < methods.length; i++)
      {
         boolean permissions = getMethodPermissions(methods[i], meta);
         if (permissions)
         {
            advisor.getMethodMetaData().addMethodMetaData(methods[i], "security", "roles", Boolean.TRUE, PayloadKey.TRANSIENT);
         }
      }

      for (int i = 0; i < fields.length; i++)
      {
         boolean permissions = getFieldPermissions(fields[i], meta);
         if (permissions)
         {
            advisor.getFieldMetaData().addFieldMetaData(fields[i].getName(), "security", "roles", Boolean.TRUE, PayloadKey.TRANSIENT);
         }
      }

      for (int i = 0; i < constructors.length; i++)
      {
         boolean permissions = getConstructorPermissions(constructors[i], meta);
         if (permissions)
         {
            //Use getMethodInfo2() to avoid frozen check
            advisor.getConstructorMetaData().addConstructorMetaData(constructors[i].getMethodInfo2().getDescriptor(), "security", "roles", Boolean.TRUE, PayloadKey.TRANSIENT);
         }
      }
   }

   /**
    * Remember we only need to map in the "security" group tag for annotated joinpoint resolution
    *
    * @param method
    * @param meta
    * @return
    * @throws Exception
    */
   public boolean getMethodPermissions(CtMethod method, SecurityClassMetaDataBinding meta) throws Exception
   {
      // First check the excluded method list as this takes priority
      // over all other assignments
      Iterator iterator = meta.getMethodExcludeList().iterator();
      while (iterator.hasNext())
      {
         SecurityMethodConfig m = (SecurityMethodConfig) iterator.next();
         if (m.patternMatches(method))
         {
            return true;
         }
      }

      // Check the permissioned methods list
      iterator = meta.getMethodPermissions().iterator();
      while (iterator.hasNext())
      {
         SecurityMethodConfig m = (SecurityMethodConfig) iterator.next();
         if (m.patternMatches(method))
         {
            return true;
         }
      }

      return false;
   }


   public boolean getFieldPermissions(CtField field, SecurityClassMetaDataBinding meta)
   {
      String fieldName = field.getName();
      // First check the excluded method list as this takes priority
      // over all other assignments
      Iterator iterator = meta.getFieldExcludeList().iterator();
      while (iterator.hasNext())
      {
         String expr = (String) iterator.next();
         if (expr.equals("*") || expr.equals(fieldName))
         {
            return true;
         }
      }

      // Check the permissioned methods list
      iterator = meta.getFieldPermissions().keySet().iterator();
      while (iterator.hasNext())
      {
         String expr = (String) iterator.next();

         if (expr.equals("*") || expr.equals(fieldName))
         {
            return true;
         }
      }

      return false;
   }


   public boolean getConstructorPermissions(CtConstructor constructor, SecurityClassMetaDataBinding meta) throws NotFoundException
   {
      // First check the excluded constructor list as this takes priority
      // over all other assignments
      Iterator iterator = meta.getConstructorExcludeList().iterator();
      while (iterator.hasNext())
      {
         SecurityConstructorConfig m = (SecurityConstructorConfig) iterator.next();
         if (m.patternMatches(constructor))
         {
            return true;
         }
      }

      // Check the permissioned constructors list
      iterator = meta.getConstructorPermissions().iterator();
      while (iterator.hasNext())
      {
         SecurityConstructorConfig m = (SecurityConstructorConfig) iterator.next();
         if (m.patternMatches(constructor))
         {
            return true;
         }
      }

      return false;
   }


}
