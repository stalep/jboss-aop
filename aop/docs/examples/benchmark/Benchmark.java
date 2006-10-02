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

import java.lang.reflect.Method;

public class Benchmark
{
   public static final int ITERATIONS = 20000000;

   public static Method getNoopMethod() 
   {
      Class clazz = UnadvisedPOJO.class;
      Method[] methods = clazz.getDeclaredMethods();
      return methods[0];
   }
   public static void main(String[] args) throws Exception
   {
      long start;

      {
         System.out.println("UnadvisedPOJO method");
         UnadvisedPOJO un = new UnadvisedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            un.noop();
         }
         long unadvisedMethod = System.currentTimeMillis() - start;
         System.out.println("UnadvisedPOJO method: " + unadvisedMethod);

         System.out.println("UnadvisedPOJO reflection method");
         Method noop = getNoopMethod();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            noop.invoke(un, null);
         }
         long unadvisedReflectionMethod = System.currentTimeMillis() - start;
         System.out.println("UnadvisedPOJO reflection method: " + unadvisedReflectionMethod);

         System.out.println("AdvisedPOJO method");
         AdvisedPOJO adv = new AdvisedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            adv.noop();
         }
         long advisedMethod = System.currentTimeMillis() - start;
         System.out.println("advisedPOJO method: " + advisedMethod);

         System.out.println("InterceptedPOJO method");
         InterceptedPOJO in = new InterceptedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            in.noop();
         }
         long interceptedMethod = System.currentTimeMillis() - start;
         System.out.println("interceptedPOJO method: " + interceptedMethod);

         System.out.println("AspectizedPOJO method");
         AspectizedPOJO as = new AspectizedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            as.noop();
         }
         long aspectizedMethod = System.currentTimeMillis() - start;
         System.out.println("aspectizedPOJO method: " + aspectizedMethod);
      }
      
      
      {
         System.out.println("UnadvisedPOJO field");
         UnadvisedPOJO un = new UnadvisedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            un.foo++;
         }
         long unadvisedMethod = System.currentTimeMillis() - start;
         System.out.println("UnadvisedPOJO field: " + unadvisedMethod);

         System.out.println("AdvisedPOJO field");
         AdvisedPOJO adv = new AdvisedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            adv.foo++;
         }
         long advisedMethod = System.currentTimeMillis() - start;
         System.out.println("advisedPOJO field: " + advisedMethod);
         adv.incfield();

         System.out.println("InterceptedPOJO field");
         InterceptedPOJO in = new InterceptedPOJO();
         start = System.currentTimeMillis();
         for (int i = 0; i < ITERATIONS; i++)
         {
            in.foo++;
         }
         long interceptedMethod = System.currentTimeMillis() - start;
         System.out.println("interceptedPOJO field: " + interceptedMethod);
         in.incfield();
      }
      
      
      System.out.println("UnadvisedPOJO construction");
      start = System.currentTimeMillis();
      for (int i = 0; i < ITERATIONS; i++)
      {
         new UnadvisedPOJO();
      }
      long unadvisedConstruction = System.currentTimeMillis() - start;
      System.out.println("UnadvisedPOJO construction: " + unadvisedConstruction);

      System.out.println("AdvisedPOJO construction");
      start = System.currentTimeMillis();
      for (int i = 0; i < ITERATIONS; i++)
      {
         new AdvisedPOJO();
      }
      long advisedConstruction = System.currentTimeMillis() - start;
      System.out.println("AdvisedPOJO construction: " + advisedConstruction);

      System.out.println("InterceptedPOJO construction");
      start = System.currentTimeMillis();
      for (int i = 0; i < ITERATIONS; i++)
      {
         new InterceptedPOJO();
      }
      long interceptedConstruction = System.currentTimeMillis() - start;
      System.out.println("InterceptedPOJO construction: " + interceptedConstruction);
      call();
      call2();
      new Benchmark();
   }

   public static void call()
   {
      CalledPOJO pojo = new CalledPOJO();
      long start = System.currentTimeMillis();
      pojo.noop();
      for (int i = 0; i < ITERATIONS; i++)
      {
         pojo.noop();
      }
      long advisedCall = System.currentTimeMillis() - start;
      System.out.println("advised call pointcut: " + advisedCall);
   }

   public static void call2()
   {
      CalledPOJO pojo = new CalledPOJO();
      long start = System.currentTimeMillis();
      for (int i = 0; i < ITERATIONS; i++)
      {
         pojo.noop();
      }
      long advisedCall = System.currentTimeMillis() - start;
      System.out.println("intercepted call pointcut: " + advisedCall);
   }

   private Benchmark()
   {
      System.out.println("calling CalledPOJO from within constructor");
      CalledPOJO pojo = new CalledPOJO();
      pojo.noop();
      System.out.println(pojo.foo);
   }
}
