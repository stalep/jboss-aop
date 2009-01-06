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
package org.jboss.aop.reflectprototype;

import java.lang.ref.WeakReference;
import java.util.Map;

import javassist.CtClass;
import javassist.NotFoundException;

import org.jboss.reflect.plugins.javassist.JavassistTypeInfoFactoryImpl;

/**
 * A JavassistAopTypeInfoFactoryImpl.
 * 
 * @author <a href="stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class JavassistAopTypeInfoFactoryImpl extends JavassistTypeInfoFactoryImpl
{
   
   private ClassPoolFactory poolFactory = new AopClassPoolFactory();
   
   /**
    * Get the information for a class
    * 
    * @param name the name
    * @param cl the classloader
    * @return the info
    * @throws ClassNotFoundException when the class cannot be found
    */
   @Override
   public Object get(String name, ClassLoader cl) throws ClassNotFoundException
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (cl == null)
         throw new IllegalArgumentException("Null classloader");
      try
      {
         CtClass clazz = poolFactory.getPoolForLoader(cl).get(name);

         return get(clazz);
      }
      catch(NotFoundException nfe)
      {
         throw new ClassNotFoundException(nfe.getMessage());
      }
   }
   
   /**
    * Get the information for a class
    * 
    * @param clazz the class
    * @return the info
    */
   public Object get(CtClass clazz)
   {
      if (clazz == null)
         throw new IllegalArgumentException("Null class");
      
      Map classLoaderCache = getClassLoaderCache(clazz.getClassPool().getClassLoader());

      WeakReference weak = (WeakReference) classLoaderCache.get(clazz.getName());
      if (weak != null)
      {
         Object result = weak.get();
         if (result != null)
            return result;
      }

//      how do we call instantiate?
      Object result = null; //instantiate(clazz);
      

      weak = new WeakReference(result);
      classLoaderCache.put(clazz.getName(), weak);
      
//      we just ignore generate(..) since it doesnt do anything atm
//      generate(clazz, result);
      
      return result;
   }

}
