/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.aop.annotationoverride;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassContainer;
import org.jboss.test.aop.AOPTestWithSetup;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AnnotationOverrideTestCase extends AOPTestWithSetup
{
   public AnnotationOverrideTestCase(String arg0)
   {
      // FIXME AnnotationOverrideTestCase constructor
      super(arg0);
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("AnnotationOverrideTestCase");
      suite.addTestSuite(AnnotationOverrideTestCase.class);
      return suite;
   }

   public void testWovenClass() throws Exception
   {
      Woven woven = new Woven();
      Advisor advisor = ((Advised)woven)._getAdvisor();
      
      Some some = (Some)advisor.resolveAnnotation(Some.class);
      assertNotNull(some);
      Other other = (Other)advisor.resolveAnnotation(Other.class);
      assertNull(other);
      
      Method m = Woven.class.getDeclaredMethod("method");
      some = (Some)advisor.resolveAnnotation(m, Some.class);
      assertNull(some);
      other = (Other)advisor.resolveAnnotation(m, Other.class);
      assertNotNull(other);
      assertEquals("method", other.value());
      
      Field f = Woven.class.getDeclaredField("field");
      some = (Some)advisor.resolveAnnotation(f, Some.class);
      assertNull(some);
      other = (Other)advisor.resolveAnnotation(f, Other.class);
      assertNotNull(other);
      assertEquals("field", other.value());
      
      Constructor<Woven> c = Woven.class.getDeclaredConstructor();
      some = (Some)advisor.resolveAnnotation(c, Some.class);
      assertNull(some);
      other = (Other)advisor.resolveAnnotation(c, Other.class);
      assertNotNull(other);
      assertEquals("ctor", other.value());
   }
   
   public void testClassContainerProxiedClass() throws Exception
   {
      ClassContainer container = new ClassContainer("test", AspectManager.instance());
      container.setClass(Proxied.class);
      container.initializeClassContainer();
      
      Some some = (Some)container.resolveAnnotation(Some.class);
      assertNotNull(some);
      Other other = (Other)container.resolveAnnotation(Other.class);
      assertNull(other);
      
      Method m = Proxied.class.getDeclaredMethod("method");
      some = (Some)container.resolveAnnotation(m, Some.class);
      assertNull(some);
      other = (Other)container.resolveAnnotation(m, Other.class);
      assertNotNull(other);
      assertEquals("method", other.value());
      
      
   }

}
