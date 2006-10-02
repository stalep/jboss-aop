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

import org.jboss.aop.metadata.SimpleMetaData;
//import org.jboss.repository.spi.MetaDataContext;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AOPProxyFactoryParameters
{
   private Class proxiedClass;
   private Object target;
   //FIXME convert back to MetaDataContext once the mc 2.0 has been released 
   //private MetaDataContext metaDataContext;
   private Object metaDataContext;
   private Class[] interfaces;
   private AOPProxyFactoryMixin[] mixins; 
   private boolean objectAsSuperClass;
   private SimpleMetaData simpleMetaData;
   private ContainerCache containerCache;
   
   public AOPProxyFactoryParameters()
   {
   }

   public AOPProxyFactoryParameters(
         Class proxiedClass, 
         Object target, 
         Class[] interfaces,
         AOPProxyFactoryMixin[] mixins,
         Object context, 
         boolean objectAsSuperClass,
         SimpleMetaData simpleMetaData,
         ContainerCache containerCache)
   {
      this.interfaces = interfaces;
      this.metaDataContext = context;
      this.objectAsSuperClass = objectAsSuperClass;
      this.proxiedClass = proxiedClass;
      this.target = target;
      this.simpleMetaData = simpleMetaData;
      this.containerCache = containerCache;
   }

   public Class[] getInterfaces()
   {
      return interfaces;
   }

   public void setInterfaces(Class[] interfaces)
   {
      this.interfaces = interfaces;
   }

   public Object getMetaDataContext()
   {
      return metaDataContext;
   }

   public void setMetaDataContext(Object metaDataContext)
   {
      this.metaDataContext = metaDataContext;
   }

   public boolean isObjectAsSuperClass()
   {
      return objectAsSuperClass;
   }

   public void setObjectAsSuperClass(boolean objectAsSuperClass)
   {
      this.objectAsSuperClass = objectAsSuperClass;
   }

   public Class getProxiedClass()
   {
      return proxiedClass;
   }

   public void setProxiedClass(Class proxiedClass)
   {
      this.proxiedClass = proxiedClass;
   }

   public SimpleMetaData getSimpleMetaData()
   {
      return simpleMetaData;
   }

   public void setSimpleMetaData(SimpleMetaData simpleMetaData)
   {
      this.simpleMetaData = simpleMetaData;
   }

   public Object getTarget()
   {
      return target;
   }

   public void setTarget(Object target)
   {
      this.target = target;
   }
   
   public AOPProxyFactoryMixin[] getMixins()
   {
      return mixins;
   }

   public void setMixins(AOPProxyFactoryMixin[] mixins)
   {
      this.mixins = mixins;
   }

   public ContainerCache getContainerCache()
   {
      return containerCache;
   }

   public void setContainerCache(ContainerCache containerCache)
   {
      this.containerCache = containerCache;
   }
   
   
}
