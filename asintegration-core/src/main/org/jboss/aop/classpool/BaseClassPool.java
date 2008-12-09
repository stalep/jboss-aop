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
package org.jboss.aop.classpool;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * Intermediate class containing commonly needed functionality for the new classpools. I don't want to
 * modify AOPClassPool too much
 *  
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class BaseClassPool extends AOPClassPool
{
   private IsLocalResourcePlugin isLocalResourcePlugin;

   public BaseClassPool(ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository)
   {
      super(cl, parent, repository);
      isLocalResourcePlugin = IsLocalResourcePluginFactoryRegistry.getPluginFactory(cl).create(this);

   }

   public BaseClassPool(ClassLoader cl, ClassPool parent, ScopedClassPoolRepository repository, Class<? extends AOPCLassPoolSearchStrategy> searchStrategy)
   {
      super(cl, parent, repository, searchStrategy);
      isLocalResourcePlugin = IsLocalResourcePluginFactoryRegistry.getPluginFactory(cl).create(this);
   }
   
   /**
    * Make createCtClass public so that we can override it 
    */
   @Override
   public CtClass createCtClass(String classname, boolean useCache)
   {
      CtClass clazz = super.createCtClass(classname, useCache);
      if (clazz != null)
      {
         cacheCtClass(classname, clazz, false);
      }
      return clazz;
   }

   protected CtClass createParentCtClass(String classname, boolean useCache)
   {
      
      CtClass clazz = null;
      if (parent != null)
      {
         //Make parent create class
         if (parent instanceof BaseClassPool)
         {
            clazz = ((BaseClassPool)parent).createCtClass(classname, useCache);
         }
         else
         {
            try
            {
               clazz = parent.get(classname);
            }
            catch (NotFoundException e)
            {
            }
         }
      }
      
      if (clazz != null)
      {
         ClassPool pool = clazz.getClassPool();
         if (pool instanceof BaseClassPool)
         {
            ((BaseClassPool)pool).cacheCtClass(classname, clazz, false);
         }
      }
      return clazz;
   }

   public ClassPool getParent()
   {
      return parent;
   }
   
   @Override
   protected boolean isLocalClassLoaderResource(String classResourceName)
   {
      return isLocalResourcePlugin.isMyResource(classResourceName);
   }
}
