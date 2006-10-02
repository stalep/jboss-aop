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
package org.jboss.aop.proxy.container;

import java.lang.ref.WeakReference;

public class AOPProxyFactoryMixin
{
   private String construction;
   private WeakReference mixinClassRef;
   private WeakReference[] interfaceClassRefs;
   private int hashcode;

   public AOPProxyFactoryMixin(Class mixin, Class[] interfaces)
   {
      mixinClassRef = new WeakReference(mixin);
      interfaceClassRefs = ContainerCacheUtil.getSortedWeakReferenceForInterfaces(interfaces);
   }

   public AOPProxyFactoryMixin(Class mixin, Class[] interfaces, String parameters)
   {
      this(mixin, interfaces);
      StringBuffer construction = new StringBuffer(" new ");
      construction.append(mixin.getName());
      
      if (!parameters.startsWith("("))
      {
         construction.append("(");
      }
      
      construction.append(parameters);
      
      if (!parameters.endsWith(")"))
      {
         construction.append(")");
      }
      this.construction = construction.toString();
   }

   public String getConstruction()
   {
      return construction;
   }


   public Class[] getInterfaces()
   {
      if (interfaceClassRefs != null)
      {
         Class[] interfaces = new Class[interfaceClassRefs.length];
         for (int i = 0 ; i < interfaces.length ; i++)
         {
            interfaces[i] = (Class)interfaceClassRefs[i].get();
         }
         return interfaces;
      }
      return null;
   }

   public Class getMixin()
   {
      return (Class)mixinClassRef.get();
   }

   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      
      if (!(obj instanceof AOPProxyFactoryMixin))
      {
         return false;
      }
      
      AOPProxyFactoryMixin other = (AOPProxyFactoryMixin)obj;
      
      if (!compareConstruction(other))
      {
         return false;
      }
      if (!ContainerCacheUtil.compareClassRefs(this.mixinClassRef, other.mixinClassRef))
      {
         return false;
      }
      if (!ContainerCacheUtil.compareInterfaceRefs(this.interfaceClassRefs, other.interfaceClassRefs))
      {
         return false;
      }
      return true;
   }

   public int hashCode()
   {
      if (hashcode == 0)
      {
         
         Class clazz = (Class)mixinClassRef.get();
         StringBuffer sb = new StringBuffer();
         
         if (clazz != null)
         {
            sb.append(clazz.getName());
         }
         
         if (interfaceClassRefs != null)
         {
            for (int i = 0 ; i < interfaceClassRefs.length ; i++)
            {
               sb.append(";");
               sb.append(((Class)interfaceClassRefs[i].get()).getName());
            }
         }
         hashcode = sb.toString().hashCode();
         if (construction != null)
         {
            hashcode += construction.hashCode();
         }
      }
      
      return hashcode;
   }

   public String toString()
   {
      return super.toString();
   }
   
   private boolean compareConstruction(AOPProxyFactoryMixin other)
   {
      if (this.construction == null && other.construction != null)
      {
         return false;
      }
      if (this.construction != null && other.construction == null)
      {
         return false;
      }
      if (this.construction != null)
      {
         if (!this.construction.equals(other.construction))
         {
            return false;
         }
      }
      return true;
   }
}