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
package org.jboss.test.aop.rebuildingchain;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.aop.AspectManager;
import org.jboss.test.aop.AOPTestWithSetup;
import org.jboss.test.aop.override.OverrideTestCase;

/**
 * A TestRebuildingChain.
 * 
 * @author <a href="stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class RebuildingChainTestCase extends AOPTestWithSetup
{

   private static boolean failed = false;
   
   public RebuildingChainTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("RebuildingChainTestCase");
      suite.addTestSuite(RebuildingChainTestCase.class);
      return suite;
   }
   
   public void testRebuildingChain() throws Exception
   {
      System.out.println("testing rebuildingchain!");
//      AspectManager.instance().verbose = true;
      SyncThread st = new SyncThread();
      RebuildThread rt = new RebuildThread();
      rt.linkNewAdvice();
      
      rt.start();
      st.start();
      
      try
      {
         long start = System.currentTimeMillis();
         Thread.sleep(20);
         System.out.println("Slept for: "+(System.currentTimeMillis()-start));
      }
      catch(InterruptedException ie)
      {
         System.err.println("BAH "+ie.getMessage());
      }
      
      st.setDone(true);
      rt.setDone(true);
      
      assertFalse("Failed to match pointcut when rebuilding the chain....", failed);
   }
   
   public static void setTestFailed()
   {
      failed = true;
   }
   
   public static void main(String[] args)throws Exception
   {
      try
      {
         RebuildingChainTestCase test = new RebuildingChainTestCase("XXX");
         test.testRebuildingChain();
      }
      catch (RuntimeException e)
      {
         System.err.println(e);
      }
   }
}
