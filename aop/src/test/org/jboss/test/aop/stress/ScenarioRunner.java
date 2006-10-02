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
package org.jboss.test.aop.stress;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class ScenarioRunner
{
   private static final String LOOPS = "loops";
   private static final String THREADS = "threads";
   private static final String RANDOM_SLEEP_INTERVAL = "random_sleep_interval";
   private static final String SLEEPTIME_MILLIS = "sleeptime_millis";
   private static final String LOGGING = "logging";
   
   private int loops;
   private int threads;
   private boolean randomSleepInterval;
   private int sleeptimeMillis;
   Random random = new Random(10);
   boolean logging;

   
   public ScenarioRunner()
   {
      try
      {
         URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
         System.out.println("class url: " + url);
         String location = url.toString();
         int index = location.indexOf("/output/");
         location = location.substring(0, index);
         
         location = location + "/src/resources/test/stress/config.properties";
         url = new URL(location);
         InputStream in = new FileInputStream(url.getFile());
         Properties properties = new Properties();
         properties.load(in);
         
         loops = Integer.parseInt(properties.getProperty(LOOPS, "10"));
         threads = Integer.parseInt(properties.getProperty(THREADS, "10"));
         String bool = properties.getProperty(RANDOM_SLEEP_INTERVAL, "false");
         randomSleepInterval = bool.equals("true");
         sleeptimeMillis = Integer.parseInt(properties.getProperty(SLEEPTIME_MILLIS, "100"));
         bool = properties.getProperty(LOGGING, "false");
         logging = bool.equals("true");
         
         System.out.println("============================================");
         System.out.println("Configured ScenarioRunner");
         System.out.println("   loops:                   " + loops);
         System.out.println("   threads:                 " + threads);
         System.out.println("   Random sleep Interval:   " + randomSleepInterval);
         System.out.println("   Sleep time millis:       " + randomSleepInterval);
         System.out.println("   Logging:                 " + logging);
         System.out.println("============================================");
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   public void executeScenario(Scenario scenario) throws Exception
   {
      Scenario[] scenarios = new Scenario[] {scenario};
      executeScenarios(scenarios);
   }

   public void executeScenarios(Scenario[] scenarios) throws Exception
   {
      System.out.println("Starting run with Scenarios " + Arrays.asList(scenarios));
      long start = System.currentTimeMillis();
      
      ScenarioLoader[] loaders = new ScenarioLoader[threads];
      for (int thread = 0 ; thread < threads ; thread++)
      {
         loaders[thread] = getLoader(thread, scenarios);
      }
      
      System.out.println("Starting threads...");
      for (int thread = 0 ; thread < loaders.length ; thread++)
      {
         loaders[thread].start();
      }
      
      for (int thread = 0 ; thread < loaders.length ; thread++)
      {
         loaders[thread].join();
      }

      long end = System.currentTimeMillis();
      boolean hadExceptions = false;
      for (int thread = 0 ; thread < loaders.length ; thread++)
      {
         if (loaders[thread].exceptions.size() > 0)
         {
            hadExceptions = true;
            for (Iterator it = loaders[thread].exceptions.iterator() ; it.hasNext() ; )
            {
               ((Exception)it.next()).printStackTrace(System.err);
            }
         }
      }
      
      System.out.println("--- DONE --- test took " + (end - start) + " ms");
      
      if (hadExceptions)
      {
         throw new Exception("Exceptions occurred, see System.err");
      }
   }

   
   private ScenarioLoader getLoader(int thread, Scenario[] scenarios)
   {
      int num = thread % scenarios.length;
      Scenario scenario = scenarios[num];
      if (logging)
      {
         scenario = new ScenarioLoggingDecorator(scenario);
      }
      return new ScenarioLoader(scenario, thread);
   }
   
   private int getSleepInterval()
   {
      if(sleeptimeMillis ==0) return sleeptimeMillis;

      if(randomSleepInterval)
      {
         return random.nextInt(sleeptimeMillis);
      } else
      {
         return sleeptimeMillis;
      }
   }
   
   private class ScenarioLoader extends Thread
   {
      int thread;
      Scenario scenario;
      int loop;
      ArrayList exceptions = new ArrayList();
      
      ScenarioLoader(Scenario scenario, int thread)
      {
         this.scenario = scenario;
         this.thread = thread;
      }
      
      public void run()
      {
         try
         {
            while (loop++ < loops)
            {
               scenario.execute(thread, loop);
               Thread.sleep(getSleepInterval());
            }
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();
         }
         catch(Exception e)
         {
            exceptions.add(e);
            e.printStackTrace();
         }
      }
   }
}
