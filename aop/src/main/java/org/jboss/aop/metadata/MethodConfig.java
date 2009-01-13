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
package org.jboss.aop.metadata;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.jboss.aop.util.XmlHelper;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * The combination of the method-permission, container-transaction
 *
 * <p>
 * The method-permission element specifies that one or more security
 * roles are allowed to invoke one or more enterprise bean methods. The
 * method-permission element consists of an optional description, a list
 * of security role names, or an indicator to specify that the methods
 * are not to be checked for authorization, and a list of method elements.
 * The security roles used in the method-permission element must be
 * defined in the security-role element of the deployment descriptor,
 * and the methods must be methods defined in the enterprise bean�s component
 * and/or home interfaces.
 * </p>
 *
 * @author <a href="mailto:sebastien.alborini@m4x.org">Sebastien Alborini</a>
 * @author <a href="mailto:Scott.Stark@jboss.org">Scott Stark</a>.
 * @version $Revision$
 */
public class MethodConfig
{
   String methodName = null;
   String signature = null;
   // Constructors --------------------------------------------------
   public MethodConfig()
   {
   }

   // Public --------------------------------------------------------

   public boolean patternMatches(Method method)
   {
      // the wildcard matches everything
      if (methodName.equals("*"))
      {
         return true;
      }

      if ( methodName.equals(method.getName()) == false )
      {
         // different names -> no
         return false;
      }
      if (signature == null) return true;

      String sig = getSignature(method.getParameterTypes());
      return sig.equals(signature);
   }

   public boolean patternMatches(CtMethod method) throws NotFoundException
   {
      // the wildcard matches everything
      if (methodName.equals("*"))
      {
         return true;
      }

      if ( methodName.equals(method.getName()) == false )
      {
         // different names -> no
         return false;
      }
      if (signature == null) return true;

      String sig = getSignature(method.getParameterTypes());
      return sig.equals(signature);
   }

   public void importXml(Element element)
      throws Exception
   {
      methodName = XmlHelper.getElementContent(XmlHelper.getUniqueChild(element, "method-name"));
      signature = "(";
      Element paramsElement = XmlHelper.getOptionalChild(element, "method-params");
      if (paramsElement != null)
      {
         Iterator<Element> paramsIterator = XmlHelper.getChildrenByTagName(paramsElement,
                                                                  "method-param");
         while (paramsIterator.hasNext())
         {
            signature += XmlHelper.getElementContent(paramsIterator.next());
            signature += " ";
         }
      }
      signature += ")";
   }

   public static String toGenericString(Method method)
   {
      StringBuffer buf = new StringBuffer();
      buf.append(method.getName());
      buf.append("(");
      getSignature(method.getParameterTypes());
      buf.append(")");
      return buf.toString();
   }

   public static String getSignature(Class<?>[] source)
   {
      StringBuffer sig = new StringBuffer("(");
      for( int i=0; i < source.length; i++ )
      {
         String brackets = "";
         Class<?> cls = source[i];
         while(cls.isArray())
         {
            brackets += "[]";
            cls = cls.getComponentType();
         }
         sig.append(cls.getName());
         sig.append(brackets);
         sig.append(" ");
      }
      sig.append(")");
      return sig.toString();
   }

   public static String getSignature(CtClass[] source)
   {
      StringBuffer sig = new StringBuffer("(");
      for( int i=0; i < source.length; i++ )
      {
         CtClass cls = source[i];
         sig.append(cls.getName());
         sig.append(" ");
      }
      sig.append(")");
      return sig.toString();
   }

   // Inner classes -------------------------------------------------
}
