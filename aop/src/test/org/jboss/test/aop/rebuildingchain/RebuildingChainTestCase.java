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
      AspectManager.instance().verbose = true;
      RebuildThread.linkNewAdvice();
//      RebuildThread.unlinkAdvice();
//      RebuildThread.linkNewAdvice();
      
      Thread t1 = new Thread(new SyncThread());
      RebuildThread rt = new RebuildThread();
      Thread t2 = new Thread(rt);
      t1.start();
      t2.start();
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
      
      SyncThread.setDone(false);
      rt.setDone(true);
      
      t2.join();
      if(t2.isAlive())
         System.out.println("rebuildingthread is still alive!!");
      
      t1.start();
      
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
      
      SyncThread.setDone(false);
      
      assertFalse("All well....", failed);
   }
   
   public static void setTestFailed()
   {
      failed = true;
   }
}
