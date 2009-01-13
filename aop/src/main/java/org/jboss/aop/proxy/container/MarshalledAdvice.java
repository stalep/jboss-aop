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
package org.jboss.aop.proxy.container;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jboss.aop.advice.AbstractAdvice;
import org.jboss.aop.joinpoint.Invocation;

class MarshalledAdvice extends AbstractAdvice implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   private Object aspect;
   private String name;
   private volatile boolean initialised = false;

   public MarshalledAdvice(Object aspect, String name, String adviceName)
   {
      this.aspect = aspect;
      this.name = name;
      super.adviceName = adviceName;
      super.aspectClass = aspect.getClass();
   }
   
   @Override
   public Object getAspectInstance()
   {
      return aspect;
   }

   public String getName()
   {
      return name;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      if (!initialised)
      {
         super.init(adviceName, aspect.getClass());
         initialised = true;
      }
      Method advice = resolveAdvice(invocation);
      Object[] args = {invocation};
      
      try
      {
         return advice.invoke(aspect, args);
      }
      catch (InvocationTargetException e)
      {
         throw e.getCause();  //To change body of catch statement use Options | File Templates.
      }
   }
   
   private void writeObject(ObjectOutputStream out) throws IOException
   {
      out.writeUTF(name);
      out.writeUTF(super.adviceName);
      out.writeObject(aspect);
      out.writeObject(super.aspectClass);
   }
   
   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      name = in.readUTF();
      super.adviceName = in.readUTF();
      aspect = in.readObject();
      super.aspectClass = (Class<?>)in.readObject();
   }
}