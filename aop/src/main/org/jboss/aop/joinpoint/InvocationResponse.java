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
package org.jboss.aop.joinpoint;
 
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Allows interceptors to communicate information back down the chain
 *   @see <related>
 *   @author  <a href="mailto:bill@jboss.org">Bill Burke</a>
 *   @version $Revision$
 *   Revisions:
 *
 *   <p><b>Revisions:</b>
 *
 */
public class InvocationResponse
   implements java.io.Externalizable
{
   // Constants -----------------------------------------------------

   private static final long serialVersionUID = 2974596986988236395L;

   public Map getContextInfo()
   {
      return contextInfo;
   }

   public void setContextInfo(Map contextInfo)
   {
      this.contextInfo = contextInfo;
   }

   // The Map of methods used by this Invocation
   protected Map contextInfo = null;
   protected Object response = null;

   // Constructors --------------------------------------------------
   public InvocationResponse()
   {
   }
   public InvocationResponse(Object obj)
   {
      if (obj instanceof InvocationResponse)
      {
         new Exception().printStackTrace();
         throw new RuntimeException("Stuffing an InvocationResponse within an InvocationResponse!!!!");
      }
      this.response = obj;
   }

   public Object getResponse() { return response; }
   public void setResponse(Object obj)
   { 
      if (obj instanceof InvocationResponse)
      {
         new Exception().printStackTrace();
         throw new RuntimeException("Stuffing an InvocationResponse within an InvocationResponse!!!!");
      }
      response = obj; 
   }

   public void addAttachment(Object key, Object val)
   {
      if (contextInfo == null) contextInfo = new HashMap(1);
      contextInfo.put(key, val);
   }

   public Object getAttachment(Object key)
   {
      if (contextInfo == null) return null;
      return contextInfo.get(key);
   }
   
   // Externalizable implementation ---------------------------------
   public void writeExternal(java.io.ObjectOutput out)
   throws IOException
   {
      out.writeObject(response);
      if (contextInfo == null)
      {
         out.writeInt(0);
      }
      else
      {
         out.writeInt(contextInfo.size());
         Iterator keys = contextInfo.keySet().iterator();
         while (keys.hasNext())
         {
            Object currentKey = keys.next();
            out.writeObject(currentKey);
            out.writeObject(contextInfo.get(currentKey));
         }
      }
   }
   
   public void readExternal(java.io.ObjectInput in)
   throws IOException, ClassNotFoundException
   {
      response = in.readObject();

      // contextInfo
      int size = in.readInt();
      if (size == 0)
      {
         contextInfo = null;
      }
      else
      {
         contextInfo = new HashMap(size);
         for (int i = 0; i < size; i++)
         {
            Object key = in.readObject();
            Object value = in.readObject();
            contextInfo.put(key, value);
         }
      }
   }
}
