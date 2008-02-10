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
package org.jboss.test.aop.duplicatemethod;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.ProxyFactory;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * A DuplicateMethodTestCase.
 * 
 * @author <a href="stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class DuplicateMethodTestCase extends AOPTestWithSetup
{

   public DuplicateMethodTestCase(String name)
   {
      super(name);
   }
   
   public static Test suite()
   {
      TestSuite suite = new TestSuite("DuplicateMethodTestCase");
      suite.addTestSuite(DuplicateMethodTestCase.class);
      return suite;
   }
   
   public void testDupe()
   {
      
      if(System.getSecurityManager() != null)
      {
         try 
         {
            AccessController.doPrivileged(new PrivilegedExceptionAction()
            {
               public Object run()
               {
                  generateProxy();
                  return null;
               }
            });
         }
         catch (PrivilegedActionException e)
         {
            Exception ex = e.getException();
            if (ex instanceof RuntimeException)
            {
               throw (RuntimeException)ex;
            }
            throw new RuntimeException(ex);
         }

      }
      else
         System.out.println("SystemManager == NULL");
   }
   
   public void generateProxy()
   {
      System.out.println("Generating proxy");
      ProxyFactory f = new ProxyFactory();
      f.setSuperclass(TestDupe.class);
      f.setFilter(new MethodFilter() {
         public boolean isHandled(Method m) {
            // ignore finalize()
            return !m.getName().equals("finalize");
         }
      });
      Class c = f.createClass();

      try
      {
         TestDupe td = (TestDupe) c.newInstance();
         td.foo();
      }
      catch (InstantiationException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (IllegalAccessException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
