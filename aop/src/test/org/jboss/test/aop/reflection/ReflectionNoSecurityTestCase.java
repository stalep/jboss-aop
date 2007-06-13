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
package org.jboss.test.aop.reflection;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Completes the ReflectionTester, and runs some tests with no security manager enabled 
 * since the tests have a different ProtectionDomain 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ReflectionNoSecurityTestCase extends TestCase
{
   ReflectionPOJO reflectionPOJO;
   public static Test suite()
   {
      TestSuite suite = new TestSuite("ReflectionTester");
      suite.addTestSuite(ReflectionNoSecurityTestCase.class);
      return suite;
   }
   public ReflectionNoSecurityTestCase(String name)
   {
      super(name);
   }

   public void testSetAccessibleField()
   {
     System.out.println("RUNNING TEST SET ACCESSIBLE");
     ReflectionPOJO reflectionPOJO = new ReflectionPOJO();
     reflectionPOJO.testSetAccessible();
   }

}
