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
import org.jboss.aop.advice.AdviceFactory;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.AspectFactory;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.pointcut.ast.ParseException;

/**
 * A RebuildThread.
 * 
 * @author <a href="stalep@gmail.com">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class RebuildThread implements Runnable
{
   private static Boolean done = false;

   private static int id = 0;
   
   @Override
   public void run()
   {

      while(!isDone());
      {
//       System.out.println("Inside rebuildthread");
         if(id > 0)
         {
            unlinkAdvice();
            linkNewAdvice();
         }
//       linkNewAdvice();

//       try
//       {
//       Thread.sleep(2);
//       }
//       catch(InterruptedException ie)
//       {
//       System.err.println("Exception during sleep "+ie.getMessage());
//       }
      }
      System.out.println("RebuildingThread jumping out of run()");
   }
   
   public synchronized  void setDone(boolean b)
   {
      done = b;
   }
   
   public synchronized Boolean isDone()
   {
      return done;
   }
   
   public static void linkNewAdvice()
   {
      System.out.println("adding new advice");
      AdviceBinding binding1 = null;
      try
      {
         binding1 = new AdviceBinding("execution(* org.jboss.test.aop.rebuildingchain.SyncThread->checkStatus())", null);
      }
      catch (ParseException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      binding1.addInterceptor(SyncInterceptor.class);
      binding1.setName(Integer.toString(id));
      AspectManager.instance().addBinding(binding1);
      id++;
   }
   
   public static void unlinkAdvice()
   {
//      System.out.println("unlinking "+id); 
      AspectManager.instance().removeBinding(Integer.toString((id-1)));
   }
   
   
//   public static void link(String aspect_id, String methodname, String binding, String binding_id)
//   {
//      System.out.println("link aspect_id=" + aspect_id + " method=" + methodname + " binding=" + binding
//            + " binding_id=" + binding_id);
//      AdviceBinding adviceBinding = null;
//      try
//      {
//         adviceBinding = new AdviceBinding(binding, null);
//      }
//      catch (ParseException e)
//      {
//         // TODO Auto-generated catch block 
//         e.printStackTrace();
//      }
//
//      AspectDefinition ad = AspectManager.instance().getAspectDefinition(aspect_id);
//      adviceBinding.addInterceptorFactory(new AdviceFactory(ad, methodname));
//      adviceBinding.setName(binding_id);
//      AspectManager.instance().addBinding(adviceBinding);
//   }
 
   

}
