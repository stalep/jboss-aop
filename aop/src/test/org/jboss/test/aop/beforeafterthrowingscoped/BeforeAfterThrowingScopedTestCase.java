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
package org.jboss.test.aop.beforeafterthrowingscoped;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.aop.AOPTestWithSetup;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class BeforeAfterThrowingScopedTestCase extends AOPTestWithSetup
{
   public BeforeAfterThrowingScopedTestCase(String arg0)
   {
      super(arg0);
   }

   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("BeforeAfterThrowingScopedTestCase");
      suite.addTestSuite(BeforeAfterThrowingScopedTestCase.class);
      return suite;
   }

   public void testPerInstanceAspects()
   {
      System.out.println("===== testPerInstanceAspects()");
      System.out.println("Calling POJO 1");
      PerInstanceAspect.reset();
      POJO pojo1 = new POJO();
      try
      {
         pojo1.methodWithPerInstanceAspects(false);
      }
      catch (ThrownByTestException e)
      {
         fail("Did not expect excpeption here");
      }
    
      assertNotNull(PerInstanceAspect.before);
      assertNotNull(PerInstanceAspect.after);
      assertNull(PerInstanceAspect.throwing);
      assertNotNull(PerInstanceAspect.finaly);
      assertSame(PerInstanceAspect.before, PerInstanceAspect.after);
      assertSame(PerInstanceAspect.after, PerInstanceAspect.finaly);
      PerInstanceAspect aspect1 = PerInstanceAspect.before;
      
      System.out.println("Calling POJO 2");
      PerInstanceAspect.reset();
      POJO pojo2 = new POJO();
      try
      {
         pojo2.methodWithPerInstanceAspects(true);
         fail("Should have thrown an exception");
      }
      catch (ThrownByTestException expected)
      {
      }
      
      assertNotNull(PerInstanceAspect.before);
      assertNull(PerInstanceAspect.after);
      assertNotNull(PerInstanceAspect.throwing);
      assertNotNull(PerInstanceAspect.finaly);
      assertSame(PerInstanceAspect.before, PerInstanceAspect.throwing);
      assertSame(PerInstanceAspect.throwing, PerInstanceAspect.finaly);
      PerInstanceAspect aspect2 = PerInstanceAspect.before;
    
      assertNotSame(aspect1, aspect2);
      
      
      System.out.println("Calling POJO 1 again");
      try
      {
         pojo1.methodWithPerInstanceAspects(false);
      }
      catch (ThrownByTestException e)
      {
         fail("Did not expect excpeption here");
      }
    
      PerInstanceAspect aspect1a = PerInstanceAspect.before;
      
      assertSame(aspect1, aspect1a);
      System.out.println("DONE");
   }
}
