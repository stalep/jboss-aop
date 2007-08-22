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
package org.jboss.test.aop.beforeafterArgs;

enum InvalidCallType {CONSTRUCTOR_BEFORE, CONSTRUCTOR_AROUND, CONSTRUCTOR_AFTER,
   CONSTRUCTOR_THROWING, CONSTRUCTOR_FINALLY,
   METHOD_BEFORE, METHOD_AROUND, METHOD_AFTER, METHOD_THROWING, METHOD_FINALLY,
   STATIC_METHOD_BEFORE, STATIC_METHOD_AROUND, STATIC_METHOD_AFTER,
   STATIC_METHOD_THROWING, STATIC_METHOD_FINALLY}

/**
 * POJO whose joinpoint executions will throw a NoMatchingAdviceException, due to
 * the fact that there is no advice that matches the joinpoint.
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class TargetCallerInvalidPOJO
{
   public TargetCallerInvalidPOJO(){}
   
   // test constructor calls
   public TargetCallerInvalidPOJO(InvalidCallType callType, boolean throwException) throws POJOException
   {
      switch(callType)
      {
         case CONSTRUCTOR_BEFORE:
            if (throwException)
            {
               new TargetCallerInvalidPOJO2((short) 0, null);
            }
            new TargetCallerInvalidPOJO2((short) 5);
            break;
         case CONSTRUCTOR_AROUND:
            if (throwException)
            {
               new TargetCallerInvalidPOJO2(0, null);
            }
            new TargetCallerInvalidPOJO2(5);
            break;
         case CONSTRUCTOR_AFTER:
            if (throwException)
            {
               new TargetCallerInvalidPOJO2(0l, null);
            }
            new TargetCallerInvalidPOJO2(5l);
            break;
         case CONSTRUCTOR_THROWING:
            if (throwException)
            {
               new TargetCallerInvalidPOJO2((float) 0.0, null);
            }
            new TargetCallerInvalidPOJO2((float) 5.0);
            break;
         case CONSTRUCTOR_FINALLY:
            if (throwException)
            {
               new TargetCallerInvalidPOJO2((double) 0.0, null);
            }
            new TargetCallerInvalidPOJO2((double) 5);
            break;
            
         case METHOD_BEFORE:
            TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
            if (throwException)
            {
               pojo2.method3_Before4();
            }
            pojo2.method1_Before4();
            break;
         case METHOD_AROUND:
            pojo2 = new TargetCallerInvalidPOJO2();
            if (throwException)
            {
               pojo2.method3_Around3();
            }
            pojo2.method1_Around3();
            break;
         case METHOD_AFTER:
            pojo2 = new TargetCallerInvalidPOJO2();
            if (throwException)
            {
               pojo2.method3_After3();
            }
            pojo2.method1_After3();
            break;
            
         case METHOD_THROWING:
            pojo2 = new TargetCallerInvalidPOJO2();
            if (throwException)
            {
               pojo2.method3_Throwing2();
            }
            pojo2.method1_Throwing2();
            break;
         case METHOD_FINALLY:
            pojo2 = new TargetCallerInvalidPOJO2();
            if (throwException)
            {
               pojo2.method3_Finally3();
            }
            pojo2.method1_Finally3();
            break;
            
         case STATIC_METHOD_BEFORE:
            if (throwException)
            {
               TargetCallerInvalidPOJO2.method4_Before4();
            }
            TargetCallerInvalidPOJO2.method2_Before4();
            break;
         case STATIC_METHOD_AROUND:
            if (throwException)
            {
               TargetCallerInvalidPOJO2.method4_Around3();
            }
            TargetCallerInvalidPOJO2.method2_Around3();
            break;
         case STATIC_METHOD_AFTER:
            if (throwException)
            {
               TargetCallerInvalidPOJO2.method4_After3();
            }
            TargetCallerInvalidPOJO2.method2_After3();
            break;
         case STATIC_METHOD_THROWING:
            if (throwException)
            {
               TargetCallerInvalidPOJO2.method4_Throwing2();
            }
            TargetCallerInvalidPOJO2.method2_Throwing2();
            break;
         case STATIC_METHOD_FINALLY:
            if (throwException)
            {
               TargetCallerInvalidPOJO2.method4_Finally3();
            }
            TargetCallerInvalidPOJO2.method2_Finally3();
            break;
      }
   }
   
   /* method1() */
   
   public void method1Before2(){}
   
   public void method1Before3(){}
   
   public void method1Before4(){}
   
   public void method1Around2(){}
   
   public void method1Around3(){}
   
   public void method1Around4(){}
   
   public void method1After2(){}
   
   public void method1After3(){}
   
   public void method1Throwing2(){}
   
   public void method1Throwing3(){}
   
   public void method1Finally1(){}
   
   public void method1Finally3(){}
   
   public void method1Finally4(){}
   
   /* method3() */
   
   public void method3Before2() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Before3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Before4() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Around2() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Around3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Around4() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3After2() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3After3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Throwing2() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Throwing3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Finally1() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Finally3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3Finally4() throws POJOException
   {
      throw new POJOException();
   }
   
   /* method5() */
   
   public void method5Before4()
   {
      new TargetCallerInvalidPOJO2((short) 4);
   }
   
   public void method5Around3()
   {
      new TargetCallerInvalidPOJO2(3);
   }
   
   public void method5After3()
   {
      new TargetCallerInvalidPOJO2(3l);
   }
   
   public void method5Throwing2()
   {
      new TargetCallerInvalidPOJO2((float) 2.0);
   }
   
   public void method5Finally3()
   {
      new TargetCallerInvalidPOJO2((double) 3.0);
   }
   
   /* method6() */
   
   public void method6Before4() throws POJOException
   {
      new TargetCallerInvalidPOJO2((short) 4, "method6");
   }
   
   public void method6Around3() throws POJOException
   {
      new TargetCallerInvalidPOJO2(3, "method6");
   }
   
   public void method6After3() throws POJOException
   {
      new TargetCallerInvalidPOJO2(3l, "method6");
   }
   
   public void method6Throwing2() throws POJOException
   {
      new TargetCallerInvalidPOJO2((float) 2.0, "method6");
   }
   
   public void method6Finally3() throws POJOException
   {
      new TargetCallerInvalidPOJO2((double) 3.0, "method6");
   }
   
   /* method7() */
   
   public void method7Before4()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Before4();
   }
   
   public void method7Around3()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Around3();
   }
   
   public void method7After3()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_After3();
   }
   
   public void method7Throwing2()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Throwing2();
   }
   
   public void method7Finally3()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Finally3();
   }
   
   /* method8() */
   
   public void method8Before4()
   {
      TargetCallerInvalidPOJO2.method2_Before4();
   }
   
   public void method8Around3()
   {
      TargetCallerInvalidPOJO2.method2_Around3();
   }
   
   public void method8After3()
   {
      TargetCallerInvalidPOJO2.method2_After3();
   }
   
   public void method8Throwing2()
   {
      TargetCallerInvalidPOJO2.method2_Throwing2();
   }
   
   public void method8Finally3()
   {
      TargetCallerInvalidPOJO2.method2_Finally3();
   }
   
   /* method9() */
   
   public void method9Before4() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Before4();
   }
   
   public void method9Around3() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Around3();
   }
   
   public void method9After3() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_After3();
   }
   
   public void method9Throwing2() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Throwing2();
   }
   
   public void method9Finally3() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Finally3();
   }
   
   /* method10() */
   
   public void method10Before4() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Before4();
   }
   
   public void method10Around3() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Around3();
   }
   
   public void method10After3() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_After3();
   }
   
   public void method10Throwing2() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Throwing2();
   }
   
   public void method10Finally3() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Finally3();
   }
   
   /* method11() */
   
   public static void method11Before4(){
      new TargetCallerInvalidPOJO2((short) 4);
   }
   
   public static void method11Around3(){
      new TargetCallerInvalidPOJO2(3);
   }
   
   public static void method11After3(){
      new TargetCallerInvalidPOJO2(3l);
   }
   
   public static void method11Throwing2(){
      new TargetCallerInvalidPOJO2((float) 2.0);
   }
   
   public static void method11Finally3(){
      new TargetCallerInvalidPOJO2((double) 3.0);
   }
   
   /* method12() */
   
   public static void method12Before4()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Before4();
   }
   
   public static void method12Around3()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Around3();
   }
   
   public static void method12After3()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_After3();
   }
   
   public static void method12Throwing2()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Throwing2();
   }
   
   public static void method12Finally3()
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method1_Finally3();
   }
   
   /* method13() */
   
   public static void method13Before4()
   {
      TargetCallerInvalidPOJO2.method2_Before4();
   }
   
   public static void method13Around3()
   {
      TargetCallerInvalidPOJO2.method2_Around3();
   }
   
   public static void method13After3()
   {
      TargetCallerInvalidPOJO2.method2_After3();
   }
   
   public static void method13Throwing2()
   {
      TargetCallerInvalidPOJO2.method2_Throwing2();
   }
   
   public static void method13Finally3()
   {
      TargetCallerInvalidPOJO2.method2_Finally3();
   }
   
   /* method14() */
   
   public static void method14Before4() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Before4();
   }
   
   public static void method14Around3() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Around3();
   }
   
   public static void method14After3() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_After3();
   }
   
   public static void method14Throwing2() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Throwing2();
   }
   
   public static void method14Finally3() throws POJOException
   {
      TargetCallerInvalidPOJO2 pojo2 = new TargetCallerInvalidPOJO2();
      pojo2.method3_Finally3();
   }
   
   /* method15() */
   
   public static void method15Before4() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Before4();
   }
   
   public static void method15Around3() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Around3();
   }
   
   public static void method15After3() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_After3();
   }
   
   public static void method15Throwing2() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Throwing2();
   }
   
   public static void method15Finally3() throws POJOException
   {
      TargetCallerInvalidPOJO2.method4_Finally3();
   }
}

/**
 * POJO whose joinpoint executions will throw a NoMatchingAdviceException, due to
 * the fact that there is no advice that matches the joinpoint.
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
class TargetCallerInvalidPOJO2 extends TargetCallerInvalidPOJO
{
   public TargetCallerInvalidPOJO2() {}
   
   /* Constructors with invalid advices*/
   
   // advice before4
   public TargetCallerInvalidPOJO2(short x){}
   
   public TargetCallerInvalidPOJO2(short x, String y) throws POJOException
   {
      throw new POJOException();
   }
   
   // advice around3
   public TargetCallerInvalidPOJO2(int x){}
   
   public TargetCallerInvalidPOJO2(int x, String y) throws POJOException
   {
      throw new POJOException();
   }
   
   // advice after3
   public TargetCallerInvalidPOJO2(long x){}
   
   public TargetCallerInvalidPOJO2(long x, String y) throws POJOException
   {
      throw new POJOException();
   }
   
   // advice throwing2
   public TargetCallerInvalidPOJO2(float x){}
   
   public TargetCallerInvalidPOJO2(float x, String y) throws POJOException
   {
      throw new POJOException();
   }
   
   // advice finally3
   public TargetCallerInvalidPOJO2(double x){}
   
   public TargetCallerInvalidPOJO2(double x, String y) throws POJOException
   {
      throw new POJOException();
   }
   
   /* method1_() */
   
   public void method1_Before4(){}
   
   public void method1_Around3(){}
   
   public void method1_After3(){}
   
   public void method1_Throwing2(){}
   
   public void method1_Finally3(){}
   
   /* method2_() */
   
   public static void method2_Before4(){}
   
   public static void method2_Around3(){}
   
   public static void method2_After3(){}
   
   public static void method2_Throwing2(){}
   
   public static void method2_Finally3(){}
   
   /* method3_() */
   
   public void method3_Before4() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3_Around3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3_After3() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3_Throwing2() throws POJOException
   {
      throw new POJOException();
   }
   
   public void method3_Finally3() throws POJOException
   {
      throw new POJOException();
   }
   
   /* method4_() */
   
   public static void method4_Before4() throws POJOException
   {
      throw new POJOException();
   }
   
   public static void method4_Around3() throws POJOException
   {
      throw new POJOException();
   }
   
   public static void method4_After3() throws POJOException
   {
      throw new POJOException();
   }
   
   public static void method4_Throwing2() throws POJOException
   {
      throw new POJOException();
   }
   
   public static void method4_Finally3() throws POJOException
   {
      throw new POJOException();
   }
}