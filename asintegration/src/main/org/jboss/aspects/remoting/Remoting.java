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
package org.jboss.aspects.remoting;

import org.jboss.aop.Dispatcher;
import org.jboss.aop.InstanceAdvised;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.proxy.ClassProxy;
import org.jboss.aop.proxy.ClassProxyFactory;
import org.jboss.aop.proxy.ProxyFactory;
import org.jboss.aop.util.PayloadKey;
import org.jboss.aspects.security.SecurityClientInterceptor;
import org.jboss.aspects.tx.ClientTxPropagationInterceptor;
import org.jboss.remoting.InvokerLocator;
import org.jboss.util.id.GUID;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Remoting proxy utility methods.
 * 
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class Remoting
{
   public static ClassProxy createRemoteProxy(Object objectId, Class clazz, String uri)
      throws Exception
   {
      return createRemoteProxy(objectId, clazz, new InvokerLocator(uri));
   }

   public static ClassProxy createRemoteProxy(Object objectId, Class clazz, InvokerLocator locator)
      throws Exception
   {
      ClassProxy proxy = ClassProxyFactory.newInstance(clazz);
      makeRemotable(proxy, locator, objectId);

      return proxy;
   }

   /**
    * Create a remote proxy for the given ifaces, Dispatch.OID, locator.
    * @param objectId
    * @param loader
    * @param ifaces
    * @param locator
    * @return the remote enabled proxy
    * @throws Exception
    */
   public static org.jboss.aop.proxy.Proxy createRemoteProxy(Object objectId, ClassLoader loader,
         Class[] ifaces, InvokerLocator locator)
      throws Exception
   {
      GUID guid = new GUID();
      org.jboss.aop.proxy.Proxy proxy = ProxyFactory.createInterfaceProxy(guid, loader, ifaces);
      Remoting.makeRemotable(proxy, locator, objectId);
      return proxy;
   }
   /**
    * Create a remote proxy for the given ifaces, Dispatch.OID, locator, interceptors
    * and subsystem.
    * 
    * @param objectId
    * @param loader
    * @param ifaces
    * @param locator
    * @param interceptors
    * @param subsystem
    * @return the remote enabled proxy
    * @throws Exception
    */
   public static org.jboss.aop.proxy.Proxy createRemoteProxy(Object objectId, ClassLoader loader,
         Class[] ifaces, InvokerLocator locator,
         List<Interceptor> interceptors, String subsystem)
      throws Exception
   {
      GUID guid = new GUID();
      org.jboss.aop.proxy.Proxy proxy = ProxyFactory.createInterfaceProxy(guid, loader, ifaces);
      Remoting.makeRemotable(proxy, locator, objectId, interceptors, subsystem);
      return proxy;
   }

   /**
    * Does'nt propagate security/tx
    * @param oid
    * @param interfaces
    * @param uri
    * @return
    * @throws Exception
    */
   public static Object createPojiProxy(Object oid, Class[] interfaces, String uri) throws Exception
   {
      InvokerLocator locator = new InvokerLocator(uri);
      Interceptor[] interceptors = {IsLocalInterceptor.singleton, InvokeRemoteInterceptor.singleton};
      PojiProxy proxy = new PojiProxy(oid, locator, interceptors);
      return Proxy.newProxyInstance(interfaces[0].getClassLoader(), interfaces, proxy);

   }

   /**
    *
    * @param oid
    * @param interfaces
    * @param uri
    * @return
    * @throws Exception
    */
   public static Object createPojiProxy(Object oid, Class[] interfaces, String uri, Interceptor[] interceptors) throws Exception
   {
      InvokerLocator locator = new InvokerLocator(uri);
      PojiProxy proxy = new PojiProxy(oid, locator, interceptors);
      return Proxy.newProxyInstance(interfaces[0].getClassLoader(), interfaces, proxy);

   }


   /**
    * Create a remote proxy given the advised proxy, locator and Dispatcher.OID.
    * This uses a default interceptor stack and AOP substem handler.
    * 
    * @param proxy
    * @param locator
    * @param objectId
    */
   public static void makeRemotable(InstanceAdvised proxy, InvokerLocator locator,
         Object objectId)
   {
      ArrayList<Interceptor> interceptors = new ArrayList<Interceptor>();
      interceptors.add(IsLocalInterceptor.singleton);
      interceptors.add(SecurityClientInterceptor.singleton);
      interceptors.add(ClientTxPropagationInterceptor.singleton);
      interceptors.add(MergeMetaDataInterceptor.singleton);
      interceptors.add(InvokeRemoteInterceptor.singleton);
      makeRemotable(proxy, locator, objectId, interceptors, "AOP");
   }
   /**
    * Create a remote proxy given the advised proxy, locator, Dispatcher.OID,
    * interceptors and remoting subystem.
    * 
    * @param proxy
    * @param locator
    * @param objectId
    * @param interceptors
    * @param subsystem
    */
   public static void makeRemotable(InstanceAdvised proxy, InvokerLocator locator,
         Object objectId, List<Interceptor> interceptors, String subsystem)
   {
      InstanceAdvisor advisor = proxy._getInstanceAdvisor();
      for(Interceptor i : interceptors)
         advisor.insertInterceptor(i);
      advisor.getMetaData().addMetaData(InvokeRemoteInterceptor.REMOTING,
         InvokeRemoteInterceptor.INVOKER_LOCATOR,
         locator,
         PayloadKey.AS_IS);
      advisor.getMetaData().addMetaData(InvokeRemoteInterceptor.REMOTING,
         InvokeRemoteInterceptor.SUBSYSTEM,
         subsystem,
         PayloadKey.AS_IS);
      advisor.getMetaData().addMetaData(Dispatcher.DISPATCHER,
         Dispatcher.OID,
         objectId,
         PayloadKey.AS_IS);
   }


}
