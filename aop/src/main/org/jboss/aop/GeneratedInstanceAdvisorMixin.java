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
import org.jboss.aop.advice.AdviceType;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.advice.GeneratedAdvisorInterceptor;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.aop.metadata.SimpleMetaData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Adapts the old instance advisor api to the new generated advisor stuff. 
 * Old API calls on generated instance advisors will delegate to this class
 *
 * @author <a href="mailto:kabir@khan.org">Kabir Khan</a>
 * @version $Revision$
 */
public class GeneratedInstanceAdvisorMixin implements InstanceAdvisor, java.io.Serializable
{
   static final long serialVersionUID = -3057976129116723527L;

   protected ArrayList<Interceptor> insertedInterceptors = null;
   protected ArrayList<Interceptor> appendedInterceptors = null;
   protected WeakReference<Object> instanceRef;
   public boolean hasInstanceAspects = false;
   private InterceptorChainObserver interceptorChainObserver;
   InstanceAdvisorDelegate delegate;
   
   public GeneratedInstanceAdvisorMixin()
   {
   }

   public GeneratedInstanceAdvisorMixin(Object instance, GeneratedClassAdvisor genadvisor)
   {
      this.instanceRef = new WeakReference<Object>(instance);
      delegate = new InstanceAdvisorDelegate(genadvisor, this);
      delegate.initialize();
      this.interceptorChainObserver = ((ClassAdvisor) genadvisor).getInterceptorChainObserver();
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
      if (insertedInterceptors != null) 
      {
         for (Iterator it = insertedInterceptors.iterator() ; it.hasNext() ; )
         {
            newlist.add(((GeneratedAdvisorInterceptor)it.next()).create(null, null));
         }
      }
      if (appendedInterceptors != null) 
      {
         for (Iterator it = appendedInterceptors.iterator() ; it.hasNext() ; )
         {
            newlist.add(((GeneratedAdvisorInterceptor)it.next()).create(null, null));
         }
      }
      return (Interceptor[]) newlist.toArray(new Interceptor[newlist.size()]);
   }

   /**
    * Called by the advisor
    */
   public Interceptor[] getInterceptors(Interceptor[] advisorChain)
   {
      if (insertedInterceptors == null && appendedInterceptors == null) return advisorChain;
      ArrayList newlist = new ArrayList();
      if (insertedInterceptors != null) 
      {
         for (Iterator it = insertedInterceptors.iterator() ; it.hasNext() ; )
         {
            newlist.add(((GeneratedAdvisorInterceptor)it.next()).create(null, null));
         }
      }
      if (advisorChain != null)
      {
         newlist.addAll(Arrays.asList(advisorChain));
      }
      if (appendedInterceptors != null) 
      {
         for (Iterator it = appendedInterceptors.iterator() ; it.hasNext() ; )
         {
            newlist.add(((GeneratedAdvisorInterceptor)it.next()).create(null, null));
         }
      }
      return (Interceptor[]) newlist.toArray(new Interceptor[newlist.size()]);
   }

   public GeneratedAdvisorInterceptor[] getWrappers()
   {
      ArrayList newlist = new ArrayList();
      if (insertedInterceptors != null) newlist.addAll(insertedInterceptors);
      if (appendedInterceptors != null) newlist.addAll(appendedInterceptors);
      return (GeneratedAdvisorInterceptor[]) newlist.toArray(new GeneratedAdvisorInterceptor[newlist.size()]);
   }

   /**
    * Called by the advisor
    * @param An array of GeneratedAdvisorInterceptor from the advisor's intereceptor chain for the joinpoint
    * @return The advisor's original array along with extra GeneratedAdvisorInterceptors appended/prepended to this instance 
    */
   public Interceptor[] getWrappers(Interceptor[] advisorChain)
   {
      if (insertedInterceptors == null && appendedInterceptors == null) return advisorChain;
      ArrayList newlist = new ArrayList();
      if (insertedInterceptors != null) newlist.addAll(insertedInterceptors);
      if (advisorChain != null)
      {
         newlist.addAll(Arrays.asList(advisorChain));
      }
      if (appendedInterceptors != null) newlist.addAll(appendedInterceptors);
      return (GeneratedAdvisorInterceptor[]) newlist.toArray(new GeneratedAdvisorInterceptor[newlist.size()]);
   }

   public void insertInterceptor(int index, Interceptor interceptor)
   {
      ArrayList newList = new ArrayList();
      if (insertedInterceptors != null)
      {
         newList.addAll(insertedInterceptors);
      }
      newList.add(index, createWrapper(interceptor));
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
      newList.add(createWrapper(interceptor));
      insertedInterceptors = newList;
      hasInstanceAspects = true;
      if (interceptorChainObserver != null)
      {
         interceptorChainObserver.instanceInterceptorAdded(this);
      }
   }

   public void appendInterceptor(Interceptor interceptor)
   {
      ArrayList<Interceptor> newList = new ArrayList<Interceptor>();
      if (appendedInterceptors != null)
      {
         newList.addAll(appendedInterceptors);
      }
      newList.add(createWrapper(interceptor));
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
      newList.add(index, createWrapper(interceptor));
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
            GeneratedAdvisorInterceptor interceptor = (GeneratedAdvisorInterceptor) insertedInterceptors.get(i);
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
            GeneratedAdvisorInterceptor interceptor = (GeneratedAdvisorInterceptor) appendedInterceptors.get(i);
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
      Object inst = getInstance();
      if (inst instanceof Advised)
      {
         Advised advised = (Advised) inst;
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
      Object inst = getInstance();
      if (inst instanceof Advised)
      {
         Advised advised = (Advised) inst;
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
      Object inst = getInstance();
      if (inst instanceof Advised)
      {
         Advised advised = (Advised) inst;
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
      throw new RuntimeException("Should be handled by generated advisors");
   }

   /**
    * Added to notify interceptor chain observer of interceptor chain garbage collection.
    */
   protected void finalize()
   {
      ClassLoader classLoader = delegate.getAdvisor().getClazz().getClassLoader();
      if (this.interceptorChainObserver == null || !AspectManager.getRegisteredCLs().containsKey(classLoader))
      {
         return;
      }
      this.interceptorChainObserver.allInstanceInterceptorsRemoved(this);
   }
   
   private GeneratedAdvisorInterceptor createWrapper(Interceptor interceptor)
   {
      return new GeneratedAdvisorInterceptor(new InstanceInterceptorFactory(interceptor), null, null);
   }
   
   public Object getInstance()
   {
      if (instanceRef != null)
      {
         Object instance = instanceRef.get();
         return instance;
      }
      return null;
   }

   public class InstanceInterceptorFactory implements InterceptorFactory
   {
      private Interceptor interceptor;
      
      private InstanceInterceptorFactory(Interceptor interceptor)
      {
         this.interceptor = interceptor;
      }

      public Interceptor create(Advisor advisor, Joinpoint joinpoint)
      {
         return interceptor;
      }
      
      public String getClassName()
      {
         return interceptor.getClass().getName();
      }

      public String getAdvice()
      {
         return "invoke";
      }

      public AspectDefinition getAspect()
      {
         return null;
      }

      public String getName()
      {
         return interceptor.getName();
      }

      public boolean isDeployed()
      {
         return true;
      }

      public AdviceType getType()
      {
         return AdviceType.AROUND;
      }
   }
}