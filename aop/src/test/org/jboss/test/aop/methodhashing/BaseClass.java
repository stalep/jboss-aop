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
package org.jboss.test.aop.methodhashing;


/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class BaseClass
{
   public int method(
         BaseClass bc, 
         java.lang.String s, 
         byte by,
         char ch,
         double db,
         float fl,
         int i,
         long l,
         short sh,
         boolean b,
         BaseClass[] bca, 
         java.lang.String[] sa, 
         byte[] bya,
         char[] cha,
         double[] dba,
         float[] fla,
         int[] ia,
         long[] la,
         short[] sha,
         boolean[] ba,
         BaseClass[][] bcaa, 
         java.lang.String[][] saa, 
         byte[][] byaa,
         char[][] chaa,
         double[][] dbaa,
         float[][] flaa,
         int[][] iaa,
         long[][] laa,
         short[][] shaa,
         boolean[][] baa)
   {
      return i;
   }
   
   public void method()
   {
   }
   

}
