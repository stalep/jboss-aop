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
package org.jboss.aop.advice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import org.jboss.aop.AspectManager;
import org.jboss.aop.util.logging.AOPLogger;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision$
 */
public class PrecedenceSorter
{
   private static final AOPLogger logger = AOPLogger.getLogger(PrecedenceSorter.class);
   
   static Comparator<InterceptorEntry> interceptorComparator = new Comparator<InterceptorEntry>()
   {
      public int compare(InterceptorEntry objA, InterceptorEntry objB)
      {
         InterceptorEntry entryA = objA;
         InterceptorEntry entryB = objB;

         return entryA.precedenceOrder - entryB.precedenceOrder;
      }
   };

   static class InterceptorEntry
   {
      Interceptor interceptor;
      GeneratedAdvisorInterceptor factoryWrapper;
      int originalOrder;
      int precedenceOrder = -1;
      String classname;
      String method;

      InterceptorEntry(GeneratedAdvisorInterceptor factoryWrapper)
      {
         this.factoryWrapper = factoryWrapper;
         classname = factoryWrapper.getName();

         InterceptorFactory ifac = factoryWrapper.getDelegate();

         if (ifac instanceof GenericInterceptorFactory)
         {
            //Dynamically added interceptors
            classname = ((GenericInterceptorFactory)ifac).getClassName();
         }
         else
         {
            AspectFactory af = factoryWrapper.getAspect().getFactory();
            classname = af.getName();
         }

         if (ifac instanceof AdviceFactory)
         {
            method = ((AdviceFactory)ifac).getAdvice();
         }
      }


      InterceptorEntry(Interceptor interceptor)
      {
         this.interceptor = interceptor;

         String interceptorName = null;
         if (interceptor instanceof PerInstanceInterceptor)
         {
            PerInstanceInterceptor icptr = (PerInstanceInterceptor)interceptor;
            interceptorName = icptr.getName();
         }
         else if (interceptor instanceof PerJoinpointInterceptor)
         {
            PerJoinpointInterceptor icptr = (PerJoinpointInterceptor)interceptor;
            interceptorName = icptr.getName();
         }
         else if (interceptor instanceof CFlowInterceptor)
         {
            CFlowInterceptor icptr = (CFlowInterceptor)interceptor;
            interceptorName = icptr.getName();
         }
         else
         {
            interceptorName = interceptor.getClass().getName();
         }

         try
         {
            boolean isAdvice = interceptorName.startsWith("org.jboss.aop.advice.");
            if (isAdvice)
            {
               String name = interceptor.getName();
               int index = name.lastIndexOf(".");
               classname = name.substring(0, index);
               method = name.substring(index + 1);
            }
            else
            {
               classname = interceptorName;
            }
         }
         catch (RuntimeException e)
         {
            logger.error(interceptor.getName());
            throw e;
         }
      }

      public String toString()
      {
         return "Entry: " + precedenceOrder + " (" + originalOrder + ")interceptorClass=" + classname + "; adviceMethod=" + method;
      }

   }

   private static boolean matches(InterceptorEntry ientry, PrecedenceDefEntry pentry)
   {
      if (ientry.classname != null && pentry.interceptorClass != null)
      {
         if (ientry.classname.equals(pentry.interceptorClass))
         {
            if (ientry.method == null)
            {
               if (pentry.adviceMethod == null)
               {
                  return true;
               }
            }
            else if (pentry.adviceMethod != null)
            {
               //This was:
               //return ientry.classname.equals(pentry.interceptorClass);
               return ientry.method.equals(pentry.adviceMethod);
            }
         }
      }
      return false;
   }


   public static PrecedenceDefEntry[] createOverallPrecedence(AspectManager manager)
   {
      ArrayList<PrecedenceDefEntry> overall = new ArrayList<PrecedenceDefEntry>();

      LinkedHashMap<String, PrecedenceDef> precedenceDefs = manager.getPrecedenceDefs();
      boolean first = true;
      for (PrecedenceDef precedenceDef : precedenceDefs.values())
      {
         PrecedenceDefEntry[] entries = precedenceDef.getEntries();

         if (first)
         {
            //Populate overall with the initial precedence
            for (int i = 0 ; i < entries.length ; i++)
            {
               overall.add(entries[i]);
            }
            first = false;
            continue;
         }

         overall = mergePrecedenceDef(overall, precedenceDef);
      }
      return overall.toArray(new PrecedenceDefEntry[overall.size()]);
   }

   public static ArrayList<PrecedenceDefEntry> mergePrecedenceDef(ArrayList<PrecedenceDefEntry> overall, PrecedenceDef precedenceDef)
   {
      //TODO This can be improved. If you have the precedences
      //		1) A, D
      //    2) C, E
      //    3) C, D
      //After adding 2) to 1) since there is no relationship defined you get:
      //    i) A, D, C, E
      //After adding 3) to i) you end up with an overall precedence of
      //    ii) A, C, D, C, E,
      //In practice this should be fine, since the applyPrecedence() looks for the
      //first matching entry, so the second (duplicate) occurrence of C is ignored.
      PrecedenceDefEntry[] entries = precedenceDef.getEntries();
      int start = 0, end = 0;
      int size = overall.size();
      for (int i = 0 ; i < size ; i++)
      {
         PrecedenceDefEntry global = overall.get(i);
         boolean found = false;

         //find current overall precedence entry in the new set of defs
         for (int j = start ; j < entries.length ; j++)
         {
            PrecedenceDefEntry cur = entries[j];

            if (cur.equals(global))
            {
               found = true;
               end = j;
               break;
            }
         }

         //We found it. Now insert everything until this into global and
         //reset the counters
         if (found)
         {
            int insert = i;
            for (int j = start ; j < end ; j++)
            {
               overall.add(insert++, entries[j]);
            }
            end++;
            start = end;
         }
      }

      for (int j = start ; j < entries.length ; j++)
      {
         overall.add(entries[j]);
      }

      return overall;
   }

   public static Interceptor[] applyPrecedence(Interceptor[] interceptors, AspectManager manager)
   {
      if (interceptors.length == 0)
         return interceptors;

      ArrayList<InterceptorEntry> all = new ArrayList<InterceptorEntry>(interceptors.length);
      ArrayList<InterceptorEntry> precedence = new ArrayList<InterceptorEntry>(interceptors.length);
      PrecedenceDefEntry[] precedenceEntries = manager.getSortedPrecedenceDefEntries();

      //Figure out what interceptors have precedence
      for (int i = 0 ; i < interceptors.length ; i++)
      {
         InterceptorEntry interceptorEntry = new InterceptorEntry(interceptors[i]);
         all.add(interceptorEntry);
         for (int j = 0 ; j < precedenceEntries.length ; j++)
         {
            if (matches(interceptorEntry, precedenceEntries[j]))
            {
               //This interceptor is defined in the precedence
               interceptorEntry.originalOrder = i;
               interceptorEntry.precedenceOrder = j;
               precedence.add(interceptorEntry);
               break;
            }
         }
      }

      //Sort the interceptors having precedence
      Collections.sort(precedence, interceptorComparator);
      Interceptor[] sortedInterceptors = new Interceptor[interceptors.length];

      //Build up new array of interceptors depending on their precedence
      int prec = 0;
      int allSize = all.size();
      int precedenceSize = precedence.size();

      for (int i = 0 ; i < allSize ; i++)
      {
         InterceptorEntry entry = all.get(i);

         if (entry.precedenceOrder >= 0 && prec < precedenceSize)
         {
            entry = precedence.get(prec++);
         }
         sortedInterceptors[i] = entry.interceptor;
      }

      return sortedInterceptors;
   }

   public static GeneratedAdvisorInterceptor[] applyPrecedence(GeneratedAdvisorInterceptor[] interceptors, AspectManager manager)
   {
      ArrayList<InterceptorEntry> all = new ArrayList<InterceptorEntry>(interceptors.length);
      ArrayList<InterceptorEntry> precedence = new ArrayList<InterceptorEntry>(interceptors.length);
      PrecedenceDefEntry[] precedenceEntries = manager.getSortedPrecedenceDefEntries();

      //Figure out what interceptors have precedence
      for (int i = 0 ; i < interceptors.length ; i++)
      {
         InterceptorEntry interceptorEntry = new InterceptorEntry(interceptors[i]);
         all.add(interceptorEntry);
         for (int j = 0 ; j < precedenceEntries.length ; j++)
         {
            if (matches(interceptorEntry, precedenceEntries[j]))
            {
               //This interceptor is defined in the precedence
               interceptorEntry.originalOrder = i;
               interceptorEntry.precedenceOrder = j;
               precedence.add(interceptorEntry);
               break;
            }
         }
      }

      //Sort the interceptors having precedence
      Collections.sort(precedence, interceptorComparator);
      GeneratedAdvisorInterceptor[] sortedInterceptors = new GeneratedAdvisorInterceptor[interceptors.length];

      //Build up new array of interceptors depending on their precedence
      int prec = 0;
      int allSize = all.size();
      int precedenceSize = precedence.size();

      for (int i = 0 ; i < allSize ; i++)
      {
         InterceptorEntry entry = all.get(i);

         if (entry.precedenceOrder >= 0 && prec < precedenceSize)
         {
            entry = precedence.get(prec++);
         }
         sortedInterceptors[i] = entry.factoryWrapper;
      }

      return sortedInterceptors;
   }

/*   public static void main(String[] args)
   {
//      System.out.println("Hello");
      AspectManager manager = new AspectManager();
      PrecedenceDef def = new PrecedenceDef("3",new PrecedenceDefEntry[]{
            new PrecedenceDefEntry("A", null),
         	new PrecedenceDefEntry("D", null)});
      manager.addPrecedence(def);
      outputOverAll(manager);

      def = new PrecedenceDef("4",new PrecedenceDefEntry[]{
            new PrecedenceDefEntry("C", null),
            new PrecedenceDefEntry("E", null)});
      manager.addPrecedence(def);
      outputOverAll(manager);

      def = new PrecedenceDef("5",new PrecedenceDefEntry[]{
            new PrecedenceDefEntry("C", null),
            new PrecedenceDefEntry("D", null)});
      manager.addPrecedence(def);
      outputOverAll(manager);

   }

   private static void outputOverAll(AspectManager manager)
   {
      PrecedenceDefEntry[] entries = manager.getSortedPrecedenceDefEntries();
      for (int i = 0 ; i < entries.length ; i++)
      {
//         System.out.println("\t" + entries[i]);
      }
//      System.out.println("==================================");
   }
*/
}
