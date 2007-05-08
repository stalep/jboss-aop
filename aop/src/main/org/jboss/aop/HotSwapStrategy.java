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
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.WeakHashMap;

import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.instrument.DynamicTransformationObserver;
import org.jboss.aop.instrument.HotSwapper;
import org.jboss.aop.instrument.Instrumentor;
import org.jboss.aop.instrument.InstrumentorFactory;
import org.jboss.aop.instrument.JoinpointClassifier;
import org.jboss.aop.instrument.JoinpointFullClassifier;
import org.jboss.aop.instrument.JoinpointStatusUpdate;

/**
 * Dynamic AOP strategy that hot swaps a class code.
 * A class code is hot swapped whenever one or more joinpoints 
 * become advised or unadvised through dynamic aop operations.
 * @see org.jboss.aop.DynamicAOPStrategy
 * @author Flavia Rainone
 */
public class HotSwapStrategy implements DynamicAOPStrategy
{
   private HotSwapper hotSwapper;
   private Collection joinpointUpdates;
   private Instrumentor instrumentor;

   /**
    * Constructor.
    * @param hotSwapper a hot swapper is necessary for this strategy.
    */
   public HotSwapStrategy(HotSwapper hotSwapper)
   {
      this.hotSwapper = hotSwapper;
      this.joinpointUpdates = new ArrayList();
      this.instrumentor = InstrumentorFactory.getInstrumentor(AspectManager.instance(), getJoinpointClassifier());
   }
   
   /**
    * Notifies the strategy that one or more interceptor chains in the
    * system may have been updated. This method hot swaps all classes
    * whose joinpoints became advised or unadvised due to recent dynamic
    * aop operations.
    * @see org.jboss.aop.DynamicAOPStrategy#interceptorChainsUpdated()
    */
   public synchronized void interceptorChainsUpdated()
   {
      synchronized(joinpointUpdates)
      {
         if (!joinpointUpdates.isEmpty())
         {
            instrumentor.interceptorChainsUpdated(new ArrayList(joinpointUpdates), hotSwapper);
            joinpointUpdates.clear();   
         }
      }
   }
   
   /**
    * Returns a <code>org.jboss.aop.instrument.JoinpointFullClassifier</code>
    * instance.
    * @see org.jboss.aop.DynamicAOPStrategy#getJoinpointClassifier()
    */
   public JoinpointClassifier getJoinpointClassifier()
   {
      return new JoinpointFullClassifier();
   }

   /**
    * Notifies the dynamic strategy that some joinpoints were wrapped due to only
    * dynamic aop operations.
    * @param clazz class containing the joinpoints.
    * @param constructor <code>true</code> if the constructor execution was wrapped due
    * to only dynamic aop operations.
    * @param fieldRead collection of <code>CtField</code> instances whose read joinpoint
    * was wrapped due to only dynamic aop operations.
    * @param fieldWrite collection of <code>CtField</code> instances whose write joinpoint
    * was wrapped due to only dynamic aop operations.
    */
   
   /**
    * Returns a dynamic transformation observer for <code>clazz</code>.
    * @see org.jboss.aop.DynamicAOPStrategy#getDynamicTransformationObserver
    */
   public DynamicTransformationObserver getDynamicTransformationObserver(CtClass clazz)
   {
      return new DynamicTransformationTracker(clazz);
   }
   
   /**
    * Returns an interceptor chain observer for <code>clazz</code>.
    * @see org.jboss.aop.DynamicAOPStrategy#getInterceptorChainObserver
    */
   public InterceptorChainObserver getInterceptorChainObserver(Class clazz)
   {
      ClassPool classPool = AspectManager.instance().findClassPool(clazz.getClassLoader());
      CtClass ctClass = null;
      try
      {
         ctClass = classPool.get(clazz.getName());
      } catch(NotFoundException e)
      {
         throw new RuntimeException("Class " + clazz.getName() + " was not found at class pool.");
      }
      return new JoinpointStatusUpdater(ctClass);
   }
   

   /**
    * Register a joinpoint status update. This registered update will be used in <code>
    * interceptorChainsUpdated</code> to notify <code>org.jboss.aop.Instrumentor</code>
    * of the interceptor chains changes.
    * @param update the update to be registered.
    */
   private synchronized void newJoinpointUpdate(JoinpointStatusUpdate update)
   {
      synchronized(this.joinpointUpdates)
      {
         this.joinpointUpdates.add(update);
      }
   }

   /**
    * Implements the <code>org.jboss.aop.instrument.DynamicTransformationObserver</code>
    * interface in order to update callers code when the callee is instrumented due
    * to only dynamicaly added bindings.
    * This instance is associated with a class, and should be used to observe the
    * transformation process of this class.
    */
   private class  DynamicTransformationTracker implements DynamicTransformationObserver
   {
      private CtClass clazz;
      private Collection fieldReads;
      private Collection fieldWrites;
      private boolean constructor;

      /**
       * Constructor.
       * @param clazz to class whose transformation process this object will observe.
       */
      public DynamicTransformationTracker(CtClass clazz)
      {
         this.clazz = clazz;
         this.fieldReads = new ArrayList();
         this.fieldWrites = new ArrayList();
         this.constructor = false;
      }
      
      /**
       * Notifies that during the transformation of <code>clazz</code>, the <code>
       * field</code> read joinpoint was wrapped due only to bindings added
       * dynamicaly.
       * @param field the field whose read joinpoint was dynamicaly wrapped.
       * @see DynamicTransformationObserver#fieldReadDynamicalyWrapped(CtField)
       */
      public void fieldReadDynamicalyWrapped(CtField field)
      {
         this.fieldReads.add(field);
      }

      /**
       * Notifies that during the transformation of <code>clazz</code>, the <code>
       * field</code> read joinpoint was wrapped due only to bindings added
       * dynamicaly.
       * @param field the field whose read joinpoint was dynamicaly wrapped.
       * @see DynamicTransformationObserver#fieldWriteDynamicalyWrapped(CtField)
       */
      public void fieldWriteDynamicalyWrapped(CtField field)
      {
         this.fieldWrites.add(field);
      }

      /**
       * Notifies that during the transformation of <code>clazz</code>, the <code>
       * constructor</code> execution joinpoints were wrapped due only to bindings added
       * dynamicaly.
       * @see DynamicTransformationObserver#constructorDynamicalyWrapped(CtField)
       */
      public void constructorDynamicalyWrapped()
      {
       this.constructor = true; 
      }
      
      /**
       * Notifies that the transformation of <code>clazz</code> is finished.
       */
      public void transformationFinished(CtClass clazz, CodeConverter converter)
      {
         if (constructor || !fieldReads.isEmpty() || !fieldWrites.isEmpty())
         {
            instrumentor.convertProcessedClasses(hotSwapper, clazz, fieldReads, fieldWrites, constructor);
         }
      }
   }
   
   /**
    * Implements the <code>org.jboss.aop.InteceptorChainObserver</code>
    * interface in order to register joinpoint status updates in the
    * <code>HotSwapStrategy</code>.
    */
   private class JoinpointStatusUpdater implements InterceptorChainObserver
   {
      private JoinpointStatusUpdate.ClassJoinpoints newlyAdvised;
      private JoinpointStatusUpdate.ClassJoinpoints newlyUnadvised;
      
      private int instanceInterceptors;
      private WeakHashMap instanceAdvisors;
      
      private CtClass clazz;
      private int fields;
      private int constructors;
      private int methods;
      private Interceptor[][] fieldReadInterceptors;
      private Interceptor[][] fieldWriteInterceptors;
      private Interceptor[][] constructorInterceptors;
      private TLongObjectHashMap methodInterceptors;
      private int[] constructorIndexMap;
      
      /**
       * Constructor.
       * @param clazz the class associated to this observer.
       */
      public JoinpointStatusUpdater(CtClass clazz)
      {
         this.clazz = clazz;
         this.instanceAdvisors = new WeakHashMap();
      }
      
      /**
       * This method must be called before any other notification method is invoked.
       * @see org.jboss.aop.InterceptorChainObserver#initialInterceptorChains(Interceptor[][], Interceptor[][], Interceptor[][], TLongObjectHashMap)
       */
      public synchronized void initialInterceptorChains(final Class reflectionClass, Interceptor[][] fieldReadInterceptors, Interceptor[][] fieldWriteInterceptors,
            Interceptor[][] constructorInterceptors, TLongObjectHashMap methodInterceptors)
      {
         Constructor[] declaredConstructors = null;
         if (System.getSecurityManager() == null)
         {
            declaredConstructors = reflectionClass.getDeclaredConstructors();
         }
         else
         {
            try
            {
               declaredConstructors = 
                  AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor[]>()
               {
                  public Constructor[] run() throws Exception
                  {
                     return reflectionClass.getDeclaredConstructors();
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw new RuntimeException("Error retrieving declared constructors of " + reflectionClass.getName(), e.getException());
            }
            
         }
         
         
         constructorIndexMap = new int[declaredConstructors.length];
         int javassistIndex = 0;
         for (int reflectionIndex = 0; reflectionIndex < declaredConstructors.length; reflectionIndex++)
         {
            Class[] params = declaredConstructors[reflectionIndex].getParameterTypes();
            if (params.length > 0 && params[params.length-1].getName().equals("javassist.runtime.Inner"))
            {
               constructorIndexMap[reflectionIndex] = -1;
            }     
            else
            {
               
               constructorIndexMap[reflectionIndex] = javassistIndex ++ ;
            }
         }
         
         this.fieldReadInterceptors = fieldReadInterceptors;
         this.fieldWriteInterceptors = fieldWriteInterceptors;
         this.constructorInterceptors = constructorInterceptors;
         this.methodInterceptors = methodInterceptors;
         this.fields = fieldReadInterceptors.length;
         this.constructors = constructorInterceptors.length;
         this.methods = methodInterceptors.size();
         this.newlyAdvised = new JoinpointStatusUpdate.ClassJoinpoints(fields, constructors, methods);
         this.newlyUnadvised = new JoinpointStatusUpdate.ClassJoinpoints(fields, constructors, methods);
      }
      
      /**
       * Notification method.
       * @see InterceptorChainObserver#interceptorChainsUpdated(Interceptor[][], Interceptor[][], Interceptor[][], TLongObjectHashMap)
       */
      public synchronized void interceptorChainsUpdated(Interceptor[][] newFieldReadInterceptors, Interceptor[][] newFieldWriteInterceptors,
            Interceptor[][] newConstructorInterceptors, TLongObjectHashMap newMethodInterceptors)
      {
         if (instanceInterceptors == 0)
         {
            long[] methodKeys = methodInterceptors.keys();
            for (int i = 0; i < methodKeys.length; i++)
            {
               long key = methodKeys[i];
               MethodInfo oldMethodInfo = (MethodInfo) methodInterceptors.get(key);
               MethodInfo newMethodInfo = (MethodInfo) newMethodInterceptors.get(key);
               if (oldMethodInfo.getInterceptorChain().isEmpty() && !newMethodInfo.getInterceptorChain().isEmpty())
               {
                  newlyAdvised.methodExecutions.add(newMethodInfo);  
               }
               else if (!oldMethodInfo.getInterceptorChain().isEmpty() && newMethodInfo.getInterceptorChain().isEmpty())
               {
                  newlyUnadvised.methodExecutions.add(newMethodInfo);
               }
            }
            fillNewStateCollections(fieldReadInterceptors, newFieldReadInterceptors, newlyAdvised.fieldReads, newlyUnadvised.fieldReads, null);
            fillNewStateCollections(fieldWriteInterceptors, newFieldWriteInterceptors, newlyAdvised.fieldWrites, newlyUnadvised.fieldWrites, null);
            fillNewStateCollections(constructorInterceptors, newConstructorInterceptors, newlyAdvised.constructorExecutions, newlyUnadvised.constructorExecutions, this.constructorIndexMap);
            newJoinpointUpdate(this.getJoinpointStatusUpdate());
         }
         this.fieldReadInterceptors = newFieldReadInterceptors;
         this.fieldWriteInterceptors = newFieldWriteInterceptors;
         this.constructorInterceptors = newConstructorInterceptors;
         this.methodInterceptors = newMethodInterceptors;
      }
      
      /**
       * Notification method.
       * @see InterceptorChainObserver#instanceInterceptorAdded(InstanceAdvisor)
       */
      public synchronized void instanceInterceptorAdded(InstanceAdvisor instanceAdvisor)
      {
         this.instanceInterceptorsAdded(instanceAdvisor, 1);
      }

      /**
       * Notification method.
       * @see InterceptorChainObserver#instanceInterceptorsAdded(InstanceAdvisor, int)
       */
      public synchronized void instanceInterceptorsAdded(InstanceAdvisor instanceAdvisor, int howMany)
      {
         updateInstanceInterceptorsTable(instanceAdvisor, howMany);
         updateAdvisenessStatus(this.newlyAdvised);
         this.instanceInterceptors += howMany;
         HotSwapStrategy.this.interceptorChainsUpdated();
      }
      
      /**
       * Notification method.
       * @see InterceptorChainObserver#instanceInterceptorRemoved(InstanceAdvisor)
       */
      public synchronized void instanceInterceptorRemoved(InstanceAdvisor instanceAdvisor)
      {
         this.instanceInterceptorsRemoved(instanceAdvisor, 1);
      }
      
      /**
       * Notification method.
       * @see InterceptorChainObserver#instanceInterceptorsRemoved(InstanceAdvisor, int)
       */
      public synchronized void instanceInterceptorsRemoved(InstanceAdvisor instanceAdvisor, int howMany)
      {
         updateInstanceInterceptorsTable(instanceAdvisor, -howMany);
         this.instanceInterceptors -= howMany;
         updateAdvisenessStatus(this.newlyUnadvised);
         HotSwapStrategy.this.interceptorChainsUpdated();
      }
      
      /**
       * Notification method.
       * @see InterceptorChainObserver#allInstanceInterceptorsRemoved(InstanceAdvisor)
       */
      public synchronized void allInstanceInterceptorsRemoved(InstanceAdvisor instanceAdvisor)
      {
         if (this.instanceAdvisors.containsKey(instanceAdvisor))
         {
            this.instanceAdvisors.remove(instanceAdvisor);
         }
         if (this.instanceInterceptors == 0)
            return;
         this.instanceInterceptors = 0;
         for (Iterator iterator = instanceAdvisors.values().iterator(); iterator.hasNext(); )
         {
            Integer interceptors = (Integer) iterator.next();
            instanceInterceptors += interceptors.intValue();
         }
         if (this.instanceInterceptors > 0)
            return;
         updateAdvisenessStatus(this.newlyUnadvised);
         HotSwapStrategy.this.interceptorChainsUpdated();
      }

      /**
       * Gets the joinpoint status update containing all the observed interceptor
       * chain changes information.
       * @return the joinpoint status update.
       */
      private JoinpointStatusUpdate getJoinpointStatusUpdate() {
         JoinpointStatusUpdate update = new JoinpointStatusUpdate();
         update.clazz = this.clazz;
         update.newlyAdvisedJoinpoints = this.newlyAdvised;
         update.newlyUnadvisedJoinpoints = this.newlyUnadvised;
         this.newlyAdvised = new JoinpointStatusUpdate.ClassJoinpoints(this.fields, this.constructors, this.methods);
         this.newlyUnadvised = new JoinpointStatusUpdate.ClassJoinpoints(this.fields, this.constructors, this.methods);
         return update;
      }

      
      /**
       * Compares the two interceptors chains and adds the newly advised joinpoints
       * to <code>newlyAdvised</code> and the newly unadvised to <code>newlyUnadvised</code>. 
       * @param interceptors old interceptors chain.
       * @param newInterceptors new interceptors chain.
       * @param newlyAdvised collection to which the newly advised joinpoints will be added.
       * @param newlyAdvised collection to which the newly unadvised joinpoints will be added.
       */
      private void fillNewStateCollections(Interceptor[][] interceptors, Interceptor[][] newInterceptors,
            Collection newlyAdvised, Collection newlyUnadvised, int[] indexMap)
      {
         if (instanceInterceptors > 0)
            return;
         for (int i = 0; i < interceptors.length; i++) {
            Interceptor[] oldInterceptorsChain = interceptors[i];
            Interceptor[] newInterceptorsChain = newInterceptors[i];
            boolean interceptedBefore = oldInterceptorsChain != null && oldInterceptorsChain.length > 0;
            boolean interceptedNow = newInterceptorsChain != null && newInterceptorsChain.length > 0;
            if (!interceptedBefore && interceptedNow)
            {
               if (indexMap != null)
               {
                  if(indexMap[i] != -1)
                  {
                     newlyAdvised.add(new Integer(indexMap[i]));
                  }
               }
               else
               {
                  newlyAdvised.add(new Integer(i));
               }

            }
            else if (interceptedBefore && !interceptedNow)
            {
               if (indexMap != null)
               {
                  if(indexMap[i] != -1)
                  {
                     newlyUnadvised.add(new Integer(indexMap[i]));
                  }
               }
               else
               {
                  newlyUnadvised.add(new Integer(i));
               }
            }
         }
      }

      /**
       * Updates the instance interceptor table to contain the <code>interceptorsAdded</code> information.
       * @param instanceAdvisor the <code>org.jboss.aop.InstanceAdvisor</code> whose interceptors chain
       * size is being updated.
       * @param interceptorsAdded the number of interceptors added (or removed, if <code>interceptorsAdded</code>
       * is negative) to <code>instanceAdvisor</code>.
       */
      private void updateInstanceInterceptorsTable(InstanceAdvisor instanceAdvisor, int interceptorsAdded)
      {
         if (this.instanceAdvisors.containsKey(instanceAdvisor))
         {
            Integer interceptors = (Integer) instanceAdvisors.get(instanceAdvisor);
            instanceAdvisors.put(instanceAdvisor, new Integer(interceptors.intValue() + interceptorsAdded));
         }
         else
         {
            instanceAdvisors.put(instanceAdvisor, new Integer(interceptorsAdded));
         }
      }

      /**
       * If there are no instance interceptors to any instance of the class associated to this observer,
       * this method adds all unadvised joinpoints to <code>joinpoints</code> and records the joinpoint
       * status update in the <code>HotSwapStrategy</code>.
       * @param joinpoints <code>ClassJoinpoints</code> to which the unadvised joinpoints will be added.
       */
      private void updateAdvisenessStatus(JoinpointStatusUpdate.ClassJoinpoints joinpoints)
      {
         if (this.instanceInterceptors == 0)
         {
            long[] methodKeys = this.methodInterceptors.keys();
            for (int i = 0; i < methodKeys.length; i++)
            {
               long key = methodKeys[i];
               MethodInfo methodInfo = (MethodInfo) this.methodInterceptors.get(key);
               if (methodInfo.getInterceptorChain().isEmpty())
               {
                  joinpoints.methodExecutions.add(methodInfo);  
               }
            }
            findUnadvisedJoinpoints(this.fieldReadInterceptors, joinpoints.fieldReads);
            findUnadvisedJoinpoints(this.fieldWriteInterceptors, joinpoints.fieldWrites);
            findUnadvisedJoinpoints(this.constructorInterceptors, joinpoints.constructorExecutions);
            newJoinpointUpdate(getJoinpointStatusUpdate());
         }
      }
      
      /**
       * Finds all unadvised joinponts.
       * @param interceptors the interceptors chains applied to the joinponts.
       * @param joinpointsFound the collection to which the found joinpoints will be added.
       */
      private void findUnadvisedJoinpoints(Interceptor[][] interceptors, Collection joinpointsFound)
      {
         for (int i = 0; i < interceptors.length; i++)
         {
            if (interceptors[i] == null || interceptors[i].length == 0)
            {
               joinpointsFound.add(new Integer(i));
            }
         }
      }
   }
}