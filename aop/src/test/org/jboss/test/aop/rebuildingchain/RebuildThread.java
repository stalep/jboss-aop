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

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.pointcut.ast.ParseException;

/**
 * A RebuildThread.
 * 
 * @author <a href="stalep@gmail.com">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class RebuildThread extends Thread
{
   private static volatile boolean done = false;

   private int id = 0;
   
   @Override
   public void run()
   {
      for(int i=0; i < 30; i++)
      {
         linkNewAdvice();
         unlinkAdvice();

         if(isDone())
            return;
      }

   }
   
   public  void setDone(boolean b)
   {
      done = b;
   }
   
   public boolean isDone()
   {
      return done;
   }
   
   public void linkNewAdvice()
   {
//      System.out.println("adding new advice");
      AdviceBinding binding1 = null;
      try
      {
         binding1 = new AdviceBinding("execution(* org.jboss.test.aop.rebuildingchain.SyncThread->checkStatus())", null);
      }
      catch (ParseException e)
      {
         e.printStackTrace();
      }
      binding1.addInterceptor(SyncInterceptor.class);
      binding1.setName(Integer.toString(id));
      AspectManager.instance().addBinding(binding1);
      id++;
   }
   
   public void unlinkAdvice()
   {
//      System.out.println("unlinking "+(id-1)); 
      AspectManager.instance().removeBinding(Integer.toString((id-1)));
   }
  
}
