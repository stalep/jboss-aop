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
package org.jboss.aop;

import org.jboss.aop.advice.AdviceStack;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.aop.metadata.SimpleMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Holds an object instance's metadata and attached interceptors
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ClassInstanceAdvisor implements InstanceAdvisor, java.io.Serializable
{
   static final long serialVersionUID = -3057976129116723527L;

   protected ArrayList insertedInterceptors = null;
   protected ArrayList appendedInterceptors = null;
   protected WeakReference instanceRef;
   protected transient WeakReference classAdvisorRef;
   public boolean hasInstanceAspects = false;
   /**
    * aspects is a weak hash map of AspectDefinitions so that perinstance advices can be undeployed/redeployed
    */
   private InterceptorChainObserver interceptorChainObserver;
   InstanceAdvisorDelegate delegate;
   public ClassInstanceAdvisor()
   {
      delegate = new InstanceAdvisorDelegate(null, this);
      delegate.initialize();
   }

   public ClassInstanceAdvisor(Object obj)
   {
      this.instanceRef = new WeakReference(obj);
      if (obj instanceof Advised)
      {
         Advised advised = (Advised) obj;
         Advisor advizor = advised._getAdvisor();
         setAdvisorAndInitialise(advizor);
      }
   }
   
   public ClassInstanceAdvisor(Advisor advizor)
   {
      setAdvisorAndInitialise(advizor);
   }
   
   private void setAdvisorAndInitialise(Advisor advizor)
   {
      this.classAdvisorRef = new WeakReference(advizor);
      
      if (advizor instanceof ClassAdvisor)
      {
         delegate = new InstanceAdvisorDelegate(advizor, this);
         delegate.initialize();
         this.interceptorChainObserver = ((ClassAdvisor) advizor).getInterceptorChainObserver();
      }
   }
   
   public boolean hasInterceptors()
   {
      return appendedInterceptors != null || insertedInterceptors != null;
   }
   public Object getPerInstanceAspect(String def)
   {
      return delegate.getPerInstanceAspect(def);
   }

   public Object getPerInstanceAspect(AspectDefinition def)
   {
      return delegate.getPerInstanceAspect(def);
   }

   public Object getPerInstanceJoinpointAspect(Joinpoint joinpoint, AspectDefinition def)
   {
      return delegate.getPerInstanceJoinpointAspect(joinpoint, def);
   }

   public SimpleMetaData getMetaData()
   {
      return delegate.getMetaData();
   }

   public Interceptor[] getInterceptors()
   {
      ArrayList newlist = new ArrayList();
      if (insertedInterceptors != null) newlist.addAll(insertedInterceptors);
      if (appendedInterceptors != null) newlist.addAll(appendedInterceptors);
      return (Interceptor[]) newlist.toArray(new Interceptor[newlist.size()]);
   }

   /**
    * Called by the advisor
    */
   public Interceptor[] getInterceptors(Interceptor[] advisorChain)
   {
      if (insertedInterceptors == null && appendedInterceptors == null) return advisorChain;
      ArrayList newlist = new ArrayList();
      if (insertedInterceptors != null) newlist.addAll(insertedInterceptors);
      if (advisorChain != null)
      {
         newlist.addAll(Arrays.asList(advisorChain));
      }
      if (appendedInterceptors != null) newlist.addAll(appendedInterceptors);
      return (Interceptor[]) newlist.toArray(new Interceptor[newlist.size()]);
   }

   public void insertInterceptor(int index, Interceptor interceptor)
   {
      ArrayList newList = new ArrayList();
      if (insertedInterceptors != null)
      {
         newList.addAll(insertedInterceptors);
      }
      newList.add(index, interceptor);
      insertedInterceptors = newList;
      hasInstanceAspects = true;
      if (interceptorChainObserver != null)
      {
         interceptorChainObserver.instanceInterceptorAdded(this);
      }
   }

   public void insertInterceptor(Interceptor interceptor)
   {
      ArrayList newList = new ArrayList();
      if (insertedInterceptors != null)
      {
         newList.addAll(insertedInterceptors);
      }
      newList.add(interceptor);
      insertedInterceptors = newList;
      hasInstanceAspects = true;
      if (interceptorChainObserver != null)
      {
         interceptorChainObserver.instanceInterceptorAdded(this);
      }
   }

   public void appendInterceptor(Interceptor interceptor)
   {
      ArrayList newList = new ArrayList();
      if (appendedInterceptors != null)
      {
         newList.addAll(appendedInterceptors);
      }
      newList.add(interceptor);
      appendedInterceptors = newList;
      hasInstanceAspects = true;
      if (interceptorChainObserver != null)
      {      
         interceptorChainObserver.instanceInterceptorAdded(this);
      }
   }

   public void appendInterceptor(int index, Interceptor interceptor)
   {
      ArrayList newList = new ArrayList();
      if (appendedInterceptors != null)
      {
         newList.addAll(appendedInterceptors);
      }
      newList.add(index, interceptor);
      appendedInterceptors = newList;
      hasInstanceAspects = true;
      if (interceptorChainObserver != null)
      {
         interceptorChainObserver.instanceInterceptorAdded(this);
      }
   }

   /**
    * This will not remove interceptor pointcuts!  You will have to do this through AspectManager
    */
   public void removeInterceptor(String name)
   {
      int interceptorsRemoved = internalRemoveInterceptor(name);
      if (interceptorChainObserver != null)
      {
         interceptorChainObserver.instanceInterceptorsRemoved(this, interceptorsRemoved);
      }
   }

   /**
    * @param name
    * @return
    */
   private int internalRemoveInterceptor(String name)
   {
      int interceptorsRemoved = 0;
      if (insertedInterceptors != null)
      {
         for (int i = 0; i < insertedInterceptors.size(); i++)
         {
            Interceptor interceptor = (Interceptor) insertedInterceptors.get(i);
            if (interceptor.getName().equals(name))
            {
               ArrayList newList = new ArrayList();
               newList.addAll(insertedInterceptors);
               newList.remove(i);
               insertedInterceptors = newList;
               interceptorsRemoved ++;
            }
         }
      }
      if (appendedInterceptors != null)
      {
         for (int i = 0; i < appendedInterceptors.size(); i++)
         {
            Interceptor interceptor = (Interceptor) appendedInterceptors.get(i);
            if (interceptor.getName().equals(name))
            {
               ArrayList newList = new ArrayList();
               newList.addAll(appendedInterceptors);
               newList.remove(i);
               appendedInterceptors = newList;
               interceptorsRemoved ++;
            }
         }
      }
      hasInstanceAspects = ((insertedInterceptors != null && insertedInterceptors.size() > 0)
      || (appendedInterceptors != null && appendedInterceptors.size() > 0));
      return interceptorsRemoved;
   }

   public final boolean hasAspects()
   {
      return hasInstanceAspects;
   }

   public void insertInterceptorStack(String stackName)
   {
      AdviceStack stack = AspectManager.instance().getAdviceStack(stackName);
      if (stack == null) throw new RuntimeException("Stack " + stackName + " not found.");

      ClassAdvisor classAdvisor = null;
      if (getInstance() instanceof Advised)
      {
         Advised advised = (Advised) getInstance();
         classAdvisor = ((ClassAdvisor) advised._getAdvisor());
      }
      int interceptorsAdded = 0;
      Iterator it = stack.getInterceptorFactories().iterator();
      while (it.hasNext())
      {
         InterceptorFactory factory = (InterceptorFactory) it.next();
         if (!factory.isDeployed()) continue;
         Interceptor interceptor = factory.create(classAdvisor, null);
         insertInterceptor(interceptor);
         interceptorsAdded ++;
      }
      if (interceptorChainObserver != null)
      {
         this.interceptorChainObserver.instanceInterceptorsAdded(this, interceptorsAdded);
      }
   }

   public void appendInterceptorStack(String stackName)
   {
      AdviceStack stack = AspectManager.instance().getAdviceStack(stackName);
      if (stack == null) throw new RuntimeException("Stack " + stackName + " not found.");

      ClassAdvisor classAdvisor = null;
      if (getInstance() instanceof Advised)
      {
         Advised advised = (Advised) getInstance();
         classAdvisor = ((ClassAdvisor) advised._getAdvisor());
      }
      int interceptorsAdded = 0;
      Iterator it = stack.getInterceptorFactories().iterator();
      while (it.hasNext())
      {
         InterceptorFactory factory = (InterceptorFactory) it.next();
         if (!factory.isDeployed()) continue;
         Interceptor interceptor = factory.create(classAdvisor, null);
         appendInterceptor(interceptor);
         interceptorsAdded ++;
      }
      if (interceptorChainObserver != null)
      {
         this.interceptorChainObserver.instanceInterceptorsAdded(this, interceptorsAdded);
      }
   }

   public void removeInterceptorStack(String stackName)
   {
      AdviceStack stack = AspectManager.instance().getAdviceStack(stackName);
      if (stack == null) throw new RuntimeException("Stack " + stackName + " not found.");

      ClassAdvisor classAdvisor = null;
      if (getInstance() instanceof Advised)
      {
         Advised advised = (Advised) getInstance();
         classAdvisor = ((ClassAdvisor) advised._getAdvisor());
      }
      int interceptorsRemoved = 0;
      Iterator it = stack.getInterceptorFactories().iterator();
      while (it.hasNext())
      {
         InterceptorFactory factory = (InterceptorFactory) it.next();
         if (!factory.isDeployed()) continue;
         Interceptor interceptor = factory.create(classAdvisor, null);
         interceptorsRemoved += internalRemoveInterceptor(interceptor.getName());
      }
      if (interceptorChainObserver != null)
      {
         this.interceptorChainObserver.instanceInterceptorsRemoved(this, interceptorsRemoved);
      }
   }

   
   
   public Domain getDomain()
   {
      throw new RuntimeException("getDomain() is only available when you use weaving with generated advisors");
   }

   /**
    * Added to notify interceptor chain observer of interceptor chain garbage collection.
    */
   protected void finalize()
   {
      ClassLoader classLoader = getClassAdvisor().getClazz().getClassLoader();
      if (this.interceptorChainObserver == null || !AspectManager.getRegisteredCLs().containsKey(classLoader))
      {
         return;
      }
      this.interceptorChainObserver.allInstanceInterceptorsRemoved(this);
   }
   
   private Advisor getClassAdvisor()
   {
      if (classAdvisorRef != null)
      {
         return (Advisor)classAdvisorRef.get();
      }
      return null;
   }

   public Object getInstance()
   {
      if (instanceRef != null)
      {
         return instanceRef.get();
      }
      return null;
   }
}