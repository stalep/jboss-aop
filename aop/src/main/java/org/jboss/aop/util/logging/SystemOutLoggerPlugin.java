/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.util.logging;

import org.jboss.logging.LoggerPlugin;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class SystemOutLoggerPlugin implements LoggerPlugin
{
   String name;
   public void debug(Object message)
   {
      print("[aop-debug]", message, null);
   }

   public void debug(Object message, Throwable t)
   {
      print("[aop-debug]", message, t);
   }

   public void error(Object message)
   {
      print("[aop-error]", message, null);
   }

   public void error(Object message, Throwable t)
   {
      print("[aop-error]", message, t);
   }

   public void fatal(Object message)
   {
      print("[aop-fatal]", message, null);
   }

   public void fatal(Object message, Throwable t)
   {
      print("[aop-debug]", message, t);
   }

   public void info(Object message)
   {
      print("[aop-info]", message, null);
   }

   public void info(Object message, Throwable t)
   {
      print("[aop-info]", message, t);
   }

   public void init(String name)
   {
      this.name = name;
   }

   public boolean isDebugEnabled()
   {
      return true;
   }

   public boolean isInfoEnabled()
   {
      return true;
   }

   public boolean isTraceEnabled()
   {
      return true;
   }

   public void trace(Object message)
   {
      print("[aop-trace]", message, null);
   }

   public void trace(Object message, Throwable t)
   {
      print("[aop-trace]", message, null);
   }

   public void warn(Object message)
   {
      print("[aop-warn]", message, null);
   }

   public void warn(Object message, Throwable t)
   {
      print("[aop-warn]", message, null);
   }

   private void print(String prefix, Object message, Throwable t)
   {
      System.out.println(prefix + " " + name + " " + message);
      if (t != null)
      {
         t.printStackTrace(System.out);
      }
   }
}
