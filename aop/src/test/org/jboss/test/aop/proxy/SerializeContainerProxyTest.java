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
package org.jboss.test.aop.proxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.rmi.MarshalledObject;

import junit.framework.TestCase;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision: 64431 $
 */
public abstract class SerializeContainerProxyTest extends TestCase
{

   public SerializeContainerProxyTest(String name)
   {
      super(name);
   }

   private SomeInterface getProxy() throws Exception
   {
      File proxyFile = createProxyFile();
      
      ObjectInputStream in = new ObjectInputStream(new FileInputStream(proxyFile));
      Object o = in.readObject();
      assertNotNull(o);
      SomeInterface si = (SomeInterface)o;
      return si;
   }
   
   public void testContainerProxy() throws Exception
   {
      try
      {
         SomeInterface si = getProxy();
         
         TestInterceptor.invoked = false;
         TestAspect.invoked = false;
         si.helloWorld();
         assertTrue(TestInterceptor.invoked);
         assertFalse(TestAspect.invoked);
         
         OtherMixinInterface omi = (OtherMixinInterface)si;
         omi.other();
         
         OtherMixinInterface2 omi2 = (OtherMixinInterface2)si;
         int i = omi2.other2();
         assertEquals(20, i);
         
         TestInterceptor.invoked = false;
         TestAspect.invoked = false;
         si.otherWorld();
         assertFalse(TestInterceptor.invoked);
         assertTrue(TestAspect.invoked);
      }
      catch(Exception e)
      {
         e.printStackTrace();
         throw e;
      }
   }

   public void testReserializeProxy() throws Exception
   {
      try
      {
         SomeInterface si = getProxy();
         
         MarshalledObject mo = new MarshalledObject(si);
         Object o = mo.get();
         assertNotNull(o);
         SomeInterface rsi = (SomeInterface)o;
         
         TestInterceptor.invoked = false;
         TestAspect.invoked = false;
         rsi.helloWorld();
         assertTrue(TestInterceptor.invoked);
         assertFalse(TestAspect.invoked);
         
         OtherMixinInterface omi = (OtherMixinInterface)si;
         omi.other();
         
         OtherMixinInterface2 omi2 = (OtherMixinInterface2)si;
         int i = omi2.other2();
         assertEquals(20, i);
         
         TestInterceptor.invoked = false;
         TestAspect.invoked = false;
         rsi.otherWorld();
         assertFalse(TestInterceptor.invoked);
         assertTrue(TestAspect.invoked);
      }
      catch(Exception e)
      {
         e.printStackTrace();
         throw e;
      }
   }
   
   protected abstract File createProxyFile() throws Exception;
}
