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

import gnu.trove.TLongObjectHashMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.introduction.AnnotationIntroduction;
import org.jboss.aop.microcontainer.lifecycle.LifecycleCallbackBinding;
import org.jboss.aop.microcontainer.lifecycle.LifecycleCallbackDefinition;
import org.jboss.aop.pointcut.AnnotationMatcher;
import org.jboss.aop.pointcut.PointcutMethodMatch;
import org.jboss.aop.proxy.container.InstanceProxyContainer;
import org.jboss.aop.util.Advisable;
import org.jboss.util.MethodHashing;

/**
 * comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 */
public class ReflectiveAspectBinder
{
   protected Class<?> clazz;
   protected HashSet<AspectDefinition> aspects = new HashSet<AspectDefinition>();
   protected HashMap<Method, ArrayList<InterceptorFactory>> methodAdvices = new HashMap<Method, ArrayList<InterceptorFactory>>();
   protected HashMap<Constructor<?>, ArrayList<InterceptorFactory>> constructorAdvices = new HashMap<Constructor<?>, ArrayList<InterceptorFactory>>();
   protected HashMap<Field, ArrayList<InterceptorFactory>> fieldReadAdvices = new HashMap<Field, ArrayList<InterceptorFactory>>();
   protected HashMap<Field, ArrayList<InterceptorFactory>> fieldWriteAdvices = new HashMap<Field, ArrayList<InterceptorFactory>>();
   protected Advisor advisor;
   protected boolean isInstanceContainer;
   TLongObjectHashMap methodMap = new TLongObjectHashMap();

   //Lifecycle callbacks are a microcontainer thing, the key is the MC ControllerState
   protected Map<Object, Set<LifecycleCallbackDefinition>> lifecycleCallbacks = new HashMap<Object, Set<LifecycleCallbackDefinition>>();
   boolean initialisedAspects;
   boolean intitialisedLifecycleCallbacks;
   
   public ReflectiveAspectBinder(Class<?> clazz, Advisor advisor)
   {
      this.clazz = clazz;
      this.advisor = advisor;
      isInstanceContainer = InstanceProxyContainer.class == advisor.getClass();
   }

   public Class<?> getClazz()
   {
      return clazz;
   }

   public HashSet<AspectDefinition> getAspects()
   {
      if (!initialisedAspects)
      {
         Map<String, AdviceBinding> bindings = advisor.getManager().getBindings();
         bindMethodAdvices(clazz, bindings);
         bindConstructorAdvices(bindings);
         bindFieldAdvices(bindings);
      }
      return aspects;
   }

   public Map<Object, Set<LifecycleCallbackDefinition>> getLifecycleCallbacks()
   {
      if (!intitialisedLifecycleCallbacks)
      {
         bindLifecycles();
      }
      return lifecycleCallbacks;
   }
   
   public HashMap<Method, ArrayList<InterceptorFactory>> getMethodAdvices()
   {
      return methodAdvices;
   }

   public HashMap<Constructor<?>, ArrayList<InterceptorFactory>> getConstructorAdvices()
   {
      return constructorAdvices;
   }

   public HashMap<Field, ArrayList<InterceptorFactory>> getFieldReadAdvices()
   {
      return fieldReadAdvices;
   }

   public HashMap<Field, ArrayList<InterceptorFactory>> getFieldWriteAdvices()
   {
      return fieldWriteAdvices;
   }

   public void createMethodMap(final Class<?> superClass)
   {
      try
      {
         if (superClass == null || (superClass == Object.class && !isInstanceContainer))
         {
            return;
         }
         createMethodMap(superClass.getSuperclass());
         
         Method[] methods = AccessController.doPrivileged(new PrivilegedExceptionAction<Method[]>() 
         {
            public Method[] run() throws Exception
            {
               return superClass.getDeclaredMethods();
            }
         });
         for (int i = 0 ; i < methods.length ; i++)
         {
            if (!Advisable.isAdvisable(methods[i]))
            {
               continue;
            }

            long hash = MethodHashing.methodHash(methods[i]);
            methodMap.put(hash, methods[i]);
         }
      }
      catch (PrivilegedActionException e)
      {
         throw new RuntimeException(e.getException());
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   protected void bindMethodAdvices(Class<?> superClass, Map<String, AdviceBinding> bindings)
   {
      createMethodMap(superClass); 
      if (methodMap != null)
      {
         Object[] methods = methodMap.getValues();
         for (int i = 0 ; i < methods.length ; i++)
         {
            bindMethodAdvice((Method)methods[i], bindings);
         }
      }
   }

   protected void bindConstructorAdvices(Map<String, AdviceBinding> bindings)
   {
      Constructor<?>[] cons = AccessController.doPrivileged(new PrivilegedAction<Constructor<?>[]>() 
      {
         public Constructor<?>[] run()
         {
            return clazz.getDeclaredConstructors();
         }
      });
      for (int i = 0; i < cons.length; i++)
      {
         bindConstructorAdvice(cons[i], bindings);
      }
   }

   protected void bindFieldAdvices(Map<String, AdviceBinding> bindings)
   {
      Field[] fields = AccessController.doPrivileged(new PrivilegedAction<Field[]>() 
      {
         public Field[] run()
         {
            return clazz.getDeclaredFields();
         }
      });
      for (int i = 0; i < fields.length; i++)
      {
         bindFieldGetAdvice(fields[i], bindings);
         bindFieldSetAdvice(fields[i], bindings);
      }
   }

   protected boolean matches(AnnotationIntroduction ai, Object element)
   {
      AnnotationMatcher matcher = new AnnotationMatcher(advisor, element);
      return ((Boolean) ai.getTarget().jjtAccept(matcher, null)).booleanValue();
   }

   protected void bindMethodAdvice(Method mi, Map<String, AdviceBinding> bindings)
   {
      ArrayList<InterceptorFactory> advices = methodAdvices.get(mi);
      for (AdviceBinding binding : bindings.values())
      {
         PointcutMethodMatch pmatch= binding.getPointcut().matchesExecution(advisor, mi);
         
         if (pmatch != null && pmatch.isMatch())
         {
            if (advices == null)
            {
               advices = new ArrayList<InterceptorFactory>();
               methodAdvices.put(mi, advices);
            }
            advices.addAll(Arrays.asList(binding.getInterceptorFactories()));
            for (int i = 0; i < binding.getInterceptorFactories().length; i++)
            {
               aspects.add(binding.getInterceptorFactories()[i].getAspect());
            }
         }
      }
   }

   protected void bindConstructorAdvice(Constructor<?> mi, Map<String, AdviceBinding> bindings)
   {
      ArrayList<InterceptorFactory> advices = constructorAdvices.get(mi);
      for (AdviceBinding binding : bindings.values())
      {
         if (binding.getPointcut().matchesExecution(advisor, mi))
         {
            if (advices == null)
            {
               advices = new ArrayList<InterceptorFactory>();
               constructorAdvices.put(mi, advices);
            }
            advices.addAll(Arrays.asList(binding.getInterceptorFactories()));
            for (int i = 0; i < binding.getInterceptorFactories().length; i++)
            {
               aspects.add(binding.getInterceptorFactories()[i].getAspect());
            }
         }
      }
   }

   protected void bindFieldGetAdvice(Field mi, Map<String, AdviceBinding> bindings)
   {
      ArrayList<InterceptorFactory> advices = fieldReadAdvices.get(mi);
      for (AdviceBinding binding : bindings.values())
      {
         if (binding.getPointcut().matchesGet(advisor, mi))
         {
            if (advices == null)
            {
               advices = new ArrayList<InterceptorFactory>();
               fieldReadAdvices.put(mi, advices);
            }
            advices.addAll(Arrays.asList(binding.getInterceptorFactories()));
            for (int i = 0; i < binding.getInterceptorFactories().length; i++)
            {
               aspects.add(binding.getInterceptorFactories()[i].getAspect());
            }
         }
      }
   }

   protected void bindFieldSetAdvice(Field mi, Map<String, AdviceBinding> bindings)
   {
      ArrayList<InterceptorFactory> advices = fieldWriteAdvices.get(mi);
      for (AdviceBinding binding : bindings.values())
      {
         if (binding.getPointcut().matchesSet(advisor, mi))
         {
            if (advices == null)
            {
               advices = new ArrayList<InterceptorFactory>();
               fieldWriteAdvices.put(mi, advices);
            }
            advices.addAll(Arrays.asList(binding.getInterceptorFactories()));
            for (int i = 0; i < binding.getInterceptorFactories().length; i++)
            {
               aspects.add(binding.getInterceptorFactories()[i].getAspect());
            }
         }
      }
   }
   
   protected void bindLifecycles()
   {
      for (LifecycleCallbackBinding binding : advisor.getManager().getLifecycleBindings().values())
      {
         if (binding.matches(advisor, clazz))
         {
            final Object state = binding.getControllerState();
            Set<LifecycleCallbackDefinition> callbacks = lifecycleCallbacks.get(state);
            if (callbacks == null)
            {
               callbacks = new LinkedHashSet<LifecycleCallbackDefinition>();
               lifecycleCallbacks.put(state, callbacks);
            }
            
            List<LifecycleCallbackDefinition> boundCallbacks = binding.getLifecycleCallbacks();
            for (LifecycleCallbackDefinition callback : boundCallbacks)
            {
               callbacks.add(callback);
            }
         }
      }
   }
}
