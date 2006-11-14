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

import java.lang.reflect.Constructor;

import org.jboss.aop.Advised;
import org.jboss.aop.AspectManager;
import org.jboss.aop.instrument.Untransformable;
import org.jboss.aop.metadata.SimpleMetaData;
//import org.jboss.repository.spi.MetaDataContext;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class GeneratedAOPProxyFactory implements AOPProxyFactory
{
   public Object createAdvisedProxy(AOPProxyFactoryParameters params)
   {
//      return createAdvisedProxy(
//            params.isObjectAsSuperClass(), 
//            params.getProxiedClass(), 
//            params.getInterfaces(), 
//            params.getMixins(),
//            params.getSimpleMetaData(), 
//            params.getTarget(),
//            params.getMetaDataContext(),
//            params.getContainerCache(),
//            params.getCtor(),
//            params.getCtorArguments());
//   }
//   
//   //FIXME - make metaDataContext a MetaDataContext once MC 2.0 is released
//   private Object createAdvisedProxy(boolean objectAsSuper, Class proxiedClass, Class[] interfaces, 
//         AOPProxyFactoryMixin[] mixins, SimpleMetaData simpleMetaData, Object target, /*MetaDataContext*/Object metaDataContext, ContainerCache containerCache, Constructor ctor, Object[] ctorArguments)
//   {
      if (params.getTarget() != null)
      {
         if (params.getProxiedClass() != null)
         {
            if (params.getProxiedClass().isAssignableFrom(params.getTarget().getClass()) == false)
            {
               throw new RuntimeException("Specified class type " + params.getProxiedClass().getName() + " and target " + params.getTarget().getClass().getName() + " are not compatible");
            }
         }
         else
         {
            params.setProxiedClass(params.getTarget().getClass());
         }
      }
      else if (params.getProxiedClass() == null)
      {
         params.setProxiedClass(Object.class);
      }
      
//      return getProxy(objectAsSuper, manager, proxiedClass, interfaces, mixins, simpleMetaData, target, metaDataContext, containerCache, ctor, ctorArguments);
      return getProxy(params);
   }

   //FIXME - make metaDataContext a MetaDataContext once MC 2.0 is released
//   private Object getProxy(boolean objectAsSuper, AspectManager manager, Class proxiedClass,  
//         Class[] interfaces, AOPProxyFactoryMixin[] mixins, SimpleMetaData simpleMetaData, Object target, /*MetaDataContext*/ Object metaDataContext, ContainerCache containerCache, Constructor ctor, Object[] ctorArguments)
   private Object getProxy(AOPProxyFactoryParameters params)
   {
      try
      {
         Class proxyClass = null;
         
         boolean isAdvised = Advised.class.isAssignableFrom(params.getProxiedClass());
         
         if (params.getTarget() instanceof Untransformable || (isAdvised && params.getInterfaces() == null && params.getMixins() == null && params.getMetaDataContext() == null && params.getSimpleMetaData() == null))
         {
            return params.getTarget();
         }
         
         
         synchronized (ContainerCache.mapLock)
         {
            if (params.getContainerCache() == null)
            {
               params.setContainerCache(
                     ContainerCache.initialise(
                     AspectManager.instance(), 
                     params.getProxiedClass(), 
                     params.getInterfaces(), 
                     params.getMixins(), 
                     params.getMetaDataContext(), 
                     params.getSimpleMetaData()));
            }
            
            if (!params.getContainerCache().hasAspects() && !params.getContainerCache().requiresInstanceAdvisor())
            {
               return params.getTarget();
            }
            else
            {  
               proxyClass = generateProxy(params);
            }
         }
         
         return instantiateAndConfigureProxy(proxyClass, params);
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private Class generateProxy(AOPProxyFactoryParameters params) throws Exception
   {
      Class proxyClass = ContainerProxyFactory.getProxyClass(params.isObjectAsSuperClass(), params.getContainerCache().getKey(), params.getContainerCache().getAdvisor());
      
      return proxyClass;
   }
   
//   private Object instantiateAndConfigureProxy(Class proxyClass, ContainerCache cache, SimpleMetaData metadata, Object target, Constructor ctor, Object[] ctorArguments) throws Exception
   private Object instantiateAndConfigureProxy(Class proxyClass, AOPProxyFactoryParameters params) throws Exception
   {
      AspectManaged proxy;
      if (params.getCtor() != null)
      {
         Constructor ctor = proxyClass.getConstructor(params.getCtor().getSignature());
         proxy = (AspectManaged)ctor.newInstance(params.getCtor().getArguments());
      }
      else
      {
         proxy = (AspectManaged)proxyClass.newInstance();
      }
         
      proxy.setAdvisor(params.getContainerCache().getClassAdvisor());
      
      if (params.getContainerCache().getInstanceContainer() != null)
      {
         proxy.setInstanceAdvisor(params.getContainerCache().getInstanceContainer());
      }
      
      if (params.getSimpleMetaData() != null)
      {
         proxy.setMetadata(params.getSimpleMetaData());
      }
      
      if (params.getTarget() != null)
      {
         ((Delegate)proxy).setDelegate(params.getTarget());
      }
      else
      {
         ((Delegate)proxy).setDelegate(new Object());
      }
   
      return proxy;
   }
}
