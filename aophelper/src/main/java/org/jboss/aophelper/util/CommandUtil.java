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
package org.jboss.aophelper.util;

/**
 * A CommandUtil.
 * 
 * @author <a href="stalep@gmail.com">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class CommandUtil
{
   
   public static String getAopPaths()
   {
      StringBuilder aopPaths = new StringBuilder();
      String pathSeparator = System.getProperty("path.separator");
      String[] paths = System.getProperty("java.class.path").split(pathSeparator);
      for(String p : paths)
      {
         if(p.endsWith("jboss-aop-jdk50.jar"))
            aopPaths.append(p).append(pathSeparator);
         else if(p.contains("trove-2"))
            aopPaths.append(p).append(pathSeparator);
         else if(p.contains("javassist-3"))
            aopPaths.append(p).append(pathSeparator);
         else if(p.contains("jboss-common-core-2"))
            aopPaths.append(p).append(pathSeparator);
         else if(p.contains("jboss-logging-spi-2"))
            aopPaths.append(p).append(pathSeparator);
         else if(p.contains("jboss-container-"))
            aopPaths.append(p).append(pathSeparator);
         else if(p.contains("jboss-common-logging-spi"))
            aopPaths.append(p).append(pathSeparator);
         
      }
      aopPaths.append(".");
      
      return aopPaths.toString();
   }

}
