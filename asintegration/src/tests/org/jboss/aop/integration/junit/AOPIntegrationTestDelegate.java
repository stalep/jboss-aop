/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.aop.integration.junit;

import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.aop.AspectManager;
import org.jboss.aop.AspectXmlLoader;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.AbstractTestDelegate;

/**
 * AOPTestDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 62730 $
 */
public class AOPIntegrationTestDelegate extends AbstractTestDelegate
{
   /** The deployed urls */
   private static final CopyOnWriteArrayList<URL> urls = new CopyOnWriteArrayList<URL>();
   
   /** The classloader system */
   private ClassLoaderSystem system;
   
   /**
    * Create a new AOPTestDelegate.
    * 
    * @param clazz the test class
    * @param system the classloader system
    */
   public AOPIntegrationTestDelegate(Class clazz, ClassLoaderSystem system) 
   {
      super(clazz);
      this.system = system;
   }

   public void setUp() throws Exception
   {
      super.setUp();
      
      AspectManager manager = AspectManager.instance();
      
      system.setTranslator(manager);
      try
      {
         deploy(clazz.getClassLoader());
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw e;
      }
      catch (Error e)
      {
         throw e;
      }
      catch (Throwable e)
      {
         throw new RuntimeException(e);
      }
   }

   public void tearDown() throws Exception
   {
      super.tearDown();
      undeploy();
      system.shutdown();
      system.setTranslator(null);
   }
   
   /**
    * Deploy the aop config
    * 
    * @param classLoader
    * @throws Exception for any error
    */
   protected void deploy(ClassLoader classLoader) throws Exception
   {
      String testName = clazz.getName();
      int index = testName.indexOf("UnitTestCase");
      if (index != -1)
         testName = testName.substring(0, index);
      testName = testName.replace('.', '/') + "-aop.xml";
      URL url = clazz.getClassLoader().getResource(testName);
      if (url != null)
         deploy(url, classLoader);
      else
         log.debug("No test specific deployment " + testName);
   }
   
   /**
    * Deploy the aop config
    * 
    * @param suffix the suffix
    * @param classLoader the classloader
    * @return the url
    * @throws Exception for any error
    */
   protected URL deploy(String suffix, ClassLoader classLoader) throws Exception
   {
      String testName = clazz.getName();
      int index = testName.indexOf("UnitTestCase");
      if (index != -1)
         testName = testName.substring(0, index);
      testName = testName.replace('.', '/') + "-" + suffix + "-aop.xml";
      URL url = clazz.getClassLoader().getResource(testName);
      if (url != null)
         deploy(url, classLoader);
      else
         throw new RuntimeException(testName + " not found");
      return url;
   }
   
   protected void undeploy()
   {
      for (Iterator i = urls.iterator(); i.hasNext();)
      {
         URL url = (URL) i.next();
         undeploy(url);
      }
   }
   
   /**
    * Deploy the aop config
    *
    * @param url the url
    * @param classLoader
    * @throws Exception for any error
    */
   protected void deploy(URL url, ClassLoader classLoader) throws Exception
   {
      log.debug("Deploying " + url);
      urls.add(url);
      AspectXmlLoader.deployXML(url, classLoader);
   }

   /**
    * Undeploy the aop config
    * 
    * @param url the url
    */
   protected void undeploy(URL url)
   {
      try
      {
         log.debug("Undeploying " + url);
         urls.remove(url);
         AspectXmlLoader.undeployXML(url);
      }
      catch (Exception e)
      {
         log.warn("Ignored error undeploying " + url, e);
      }
   }
}
