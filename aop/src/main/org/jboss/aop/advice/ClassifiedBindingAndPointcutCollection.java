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
package org.jboss.aop.advice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.PointcutInfo;
import org.jboss.aop.pointcut.PointcutStats;
import org.jboss.aop.util.BindingClassifier;
import org.jboss.aop.util.UnmodifiableEmptyCollections;
import org.jboss.aop.util.UnmodifiableLinkedHashMap;
import org.jboss.aop.util.logging.AOPLogger;

/**
 * Manages the binding, pointcut and pointcutInfo collections contained in a domain. All entries
 * contained in this collection are indexed according to their classification.
 * <p>
 * <i>For internal use only.</i>
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 * @author  <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ClassifiedBindingAndPointcutCollection
{
   private static final AOPLogger logger = AOPLogger.getLogger(AspectManager.class);
   
   private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  
   //Collections of bindings
   private volatile LinkedHashMap<String, AdviceBinding> bindings;
   private volatile Collection<AdviceBinding> fieldReadBindings;
   private volatile Collection<AdviceBinding> fieldWriteBindings;
   private volatile Collection<AdviceBinding> constructionBindings;
   private volatile Collection<AdviceBinding> constructorExecutionBindings;
   private volatile Collection<AdviceBinding> methodExecutionBindings;
   private volatile Collection<AdviceBinding> constructorCallBindings;
   private volatile Collection<AdviceBinding> methodCallBindings;
   
   //Collections of pointcuts
   private volatile LinkedHashMap<String, Pointcut> pointcuts;
   
   //Collections of pointcutInfos
   private volatile LinkedHashMap<String, PointcutInfo> pointcutInfos;
   
   //Pointcut stats 
   private boolean execution = false;
   private boolean construction = false;
   private boolean call = false;
   private boolean within = false;
   private boolean get = false;
   private boolean set = false;
   private boolean withincode = false;

   /**
    * Constructor.<p>
    */
   @SuppressWarnings("all")
   public ClassifiedBindingAndPointcutCollection()
   {
      bindings = UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP;
      this.fieldReadBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.fieldWriteBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.constructionBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.constructorExecutionBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.methodExecutionBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.constructorCallBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.methodCallBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      
      pointcuts = UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP;
      
      pointcutInfos = UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP;
   }
   
   /**
    * Check if there are any pointcuts stored at this level
    */
   public boolean hasPointcuts()
   {
      return pointcuts.size() > 0;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully field read
    * joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match field
    *         read pointcuts
    */
   public Collection<AdviceBinding> getFieldReadBindings()
   {
      lockRead();
      try
      {
         return this.fieldReadBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully field write
    * joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match field
    *         write pointcuts
    */
   public Collection<AdviceBinding> getFieldWriteBindings()
   {
      lockRead();
      try
      {
         return this.fieldWriteBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully construction
    * joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         construction pointcuts
    */
   public Collection<AdviceBinding> getConstructionBindings()
   {
      lockRead();
      try
      {
         return this.constructionBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully constructor
    * execution joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         constructor execution pointcuts
    */
   public Collection<AdviceBinding> getConstructorExecutionBindings()
   {
      lockRead();
      try
      {
         return this.constructorExecutionBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully method
    * execution joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         method execution pointcuts
    */
   public Collection<AdviceBinding> getMethodExecutionBindings()
   {
      lockRead();
      try
      {
         return this.methodExecutionBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully constructor
    * call joinpoints.
    * <p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         constructor call pointcuts
    */
   public Collection<AdviceBinding> getConstructorCallBindings()
   {
      lockRead();
      try
      {
         return this.constructorCallBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully method
    * call joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         method call pointcuts
    */
   public Collection<AdviceBinding> getMethodCallBindings()
   {
      lockRead();
      try
      {
         return this.methodCallBindings;
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Indicate whether this collection is empty.
    */
   public boolean isEmpty()
   {
      lockRead();
      try
      {
         return this.bindings.isEmpty();
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Returns the bindings map.
    * @return an unmodifiable map containing all the bindings
    */
   public LinkedHashMap<String, AdviceBinding> getBindings()
   {
      lockRead();
      try
      {
         return new UnmodifiableLinkedHashMap<String, AdviceBinding>(bindings);
      }
      finally
      { 
         unlockRead();
      }
   }
   
   /**
    * Returns the bindings map. This method is only for internal use, hence the @Deprecated
    * @return a map containing all the bindings
    */
   @Deprecated
   public LinkedHashMap<String, AdviceBinding> getBindingsInternal()
   {
      lockRead();
      try
      {
         return new UnmodifiableLinkedHashMap<String, AdviceBinding>(bindings);
      }
      finally
      { 
         unlockRead();
      }
   }
   
   /**
    * Returns the pointcuts map.
    * @return an unmodifiable map containing all the pointcuts
    */
   public LinkedHashMap<String, Pointcut> getPointcuts()
   {
      lockRead();
      try
      {
         return pointcuts;
      }
      finally
      { 
         unlockRead();
      }
   }
   
   /**
    * Returns the pointcuts map. This method is only for internal use, hence the @Deprecated
    * @return a map containing all the pointcuts
    */
   @Deprecated
   public LinkedHashMap<String, Pointcut> getPointcutsInternal()
   {
      lockRead();
      try
      {
         return pointcuts;
      }
      finally
      { 
         unlockRead();
      }
   }
   
   /**
    * Returns the pointcutInfos map.
    * @return an unmodifiable map containing all the pointcutInfos
    */
   public LinkedHashMap<String, PointcutInfo> getPointcutInfos()
   {
      lockRead();
      try
      {
         return new UnmodifiableLinkedHashMap<String, PointcutInfo>(pointcutInfos);
      }
      finally
      { 
         unlockRead();
      }
   }
   
   /**
    * Returns the pointcutInfos map. This method is only for internal use, hence the @Deprecated
    * @return an unmodifiable map containing all the pointcutInfos
    */
   @Deprecated
   public LinkedHashMap<String, PointcutInfo> getPointcutInfosInternal()
   {
      lockRead();
      try
      {
         return pointcutInfos;
      }
      finally
      { 
         unlockRead();
      }
   }
   
   /**
    * Adds a binding to this collection.
    */
   public void add(AdviceBinding binding, AspectManager manager)
   {
      lockWrite();
      try
      {
         addBinding(binding);
         addGet(binding);
         addSet(binding);
         addConstruction(binding);
         addConstructorExecution(binding);
         addMethodExecution(binding);
         addConstructorCall(binding);
         addMethodCall(binding);
         updatePointcutStats(binding.getPointcut(), manager);
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Adds a pointcut to this collection
    */
   public void add(Pointcut pointcut, AspectManager manager)
   {
      lockWrite();
      try
      {
         removePointcut(pointcut.getName());
         addPointcut(pointcut);
         updatePointcutStats(pointcut, manager);
      }
      finally
      {
         unlockWrite();
      }
   }
   
   /**
    * Removes the binding named {@code name}.
    * 
    * @param name name of the binding to be removed.
    * @return the removed binding. If {@code null}, indicates that there is no
    *         binding with name equal to {@code name} in this collection.
    */
   public AdviceBinding removeBinding(String name)
   {
      lockWrite();
      try
      {
         AdviceBinding binding = bindings.remove(name);
         if (binding != null)
         {
            this.fieldReadBindings.remove(binding);
            this.fieldWriteBindings.remove(binding);
            this.constructionBindings.remove(binding);
            this.constructorExecutionBindings.remove(binding);
            this.methodExecutionBindings.remove(binding);
            this.constructorCallBindings.remove(binding);
            this.methodCallBindings.remove(binding);
         }
         return binding;
      }
      finally
      {
         unlockWrite();
      }
   }
   
   /**
    * Removes the pointcut and pointcutInfo named {@code name}
    * @param name the name of the pointcut to be removed
    */
   public void removePointcut(String name)
   {
      lockWrite();
      try
      {
         pointcuts.remove(name);
         pointcutInfos.remove(name);
      }
      finally
      {
         unlockWrite();
      }
   }
   
   /**
    * Gets the pointcut named {@code name}
    * @param name the name of the pointcut to get
    * @return the pointcut
    */
   public Pointcut getPointcut(String name)
   {
      lockRead();
      try
      {
         return pointcuts.get(name);
      }
      finally
      {
         unlockRead();
      }
   }
   
   /**
    * Removes all bindings whose names are contained in {@code names}.
    * 
    * @param names names of all bindings to be removed
    * @return the collection of the removed bindings
    */
   public ArrayList<AdviceBinding> removeBindings(ArrayList<String> names)
   {
      lockWrite();
      try
      {
         ArrayList<AdviceBinding> removedBindings = new ArrayList<AdviceBinding>();
         for (String name: names)
         {
            AdviceBinding binding = this.removeBinding(name);
            if (binding == null)
            {
               logger.debug("ClassifiedBindingCollection.removeBindings() no binding found with name " + name);
               continue;
            }
            removedBindings.add(binding);
         }
         return removedBindings;
      }
      finally
      {
         unlockWrite();
      }
   }

   public boolean isExecution()
   {
      return execution;
   }

   public boolean isConstruction()
   {
      return construction;
   }

   public boolean isCall()
   {
      return call;
   }

   public boolean isWithin()
   {
      return within;
   }

   public boolean isWithincode()
   {
      return withincode;
   }

   public boolean isGet()
   {
      return get;
   }

   public boolean isSet()
   {
      return set;
   }

   /**
    * Read-lock just this collection
    */
   public final void lockRead()
   {
      lock.readLock().lock();
   }
   
   /**
    * Read-unlock just this collection
    */
   protected final void unlockRead()
   {
      lock.readLock().unlock();
   }
   
   /**
    * Write-lock just this collection
    */
   public final void lockWrite()
   {
      lock.writeLock().lock();
   }
   
   /**
    * Write-unlock this collection
    */
   public final void unlockWrite()
   {
      lock.writeLock().unlock();
   }

   /**
    * Read-lock this collection
    * @param if true, parent collections will be locked too
    */
   public void lockRead(boolean lockParents)
   {
      lockRead();
   }
   
   /**
    * Read-unlock this collection
    * @param if true, parent collections will be unlocked too
    */
   public void unlockRead(boolean lockParents)
   {
      unlockRead();
   }
   
   /**
    * Write-lock this collection
    * @param if true, parent collections will be locked too
    */
   public void lockWrite(boolean lockParents)
   {
      lockWrite();
   }
   
   /**
    * Write-unlock this collection
    * @param if true, parent collections will be unlocked too
    */
   public void unlockWrite(boolean lockParents)
   {
      unlockWrite();
   }
   
   private void addBinding(AdviceBinding binding)
   {
      if (bindings == UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP)
      {
         bindings = new LinkedHashMap<String, AdviceBinding>();
      }
      bindings.put(binding.getName(), binding);
      
      addPointcut(binding.getPointcut());
   }
   
   private void addGet(AdviceBinding binding)
   {
      if (BindingClassifier.isGet(binding))
      {
         if (fieldReadBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            fieldReadBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.fieldReadBindings.add(binding);
      }
   }
   
   private void addSet(AdviceBinding binding)
   {
      if (BindingClassifier.isSet(binding))
      {
         if (fieldWriteBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            fieldWriteBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.fieldWriteBindings.add(binding);
      }
   }
   
   private void addConstruction(AdviceBinding binding)
   {
      if (BindingClassifier.isConstruction(binding))
      {
         if (constructionBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            constructionBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.constructionBindings.add(binding);
      }
   }
   
   private void addConstructorExecution(AdviceBinding binding)
   {
      if (BindingClassifier.isConstructorExecution(binding))
      {
         if (constructorExecutionBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            constructorExecutionBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.constructorExecutionBindings.add(binding);
      }
   }
   
   private void addMethodExecution(AdviceBinding binding)
   {
      if (BindingClassifier.isMethodExecution(binding))
      {
         if (methodExecutionBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            methodExecutionBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.methodExecutionBindings.add(binding);
      }
   }
   
   private void addMethodCall(AdviceBinding binding)
   {
      if (BindingClassifier.isMethodCall(binding))
      {
         if (methodCallBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            methodCallBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.methodCallBindings.add(binding);
      }
   }
   
   private void addConstructorCall(AdviceBinding binding)
   {
      if (BindingClassifier.isConstructorCall(binding))
      {
         if (constructorCallBindings == UnmodifiableEmptyCollections.EMPTY_ARRAYLIST)
         {
            constructorCallBindings = new CopyOnWriteArraySet<AdviceBinding>();
         }
         this.constructorCallBindings.add(binding);
      }
   }
   
   private void addPointcut(Pointcut pointcut)
   {
      if (pointcuts == UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP)
      {
         pointcuts = new LinkedHashMap<String, Pointcut>();
      }
      pointcuts.put(pointcut.getName(), pointcut);

      if (pointcutInfos == UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP)
      {
         pointcutInfos = new LinkedHashMap<String, PointcutInfo>();
      }
      pointcutInfos.put(pointcut.getName(), new PointcutInfo(pointcut, AspectManager.hasTransformationStarted()));
   }
   
   private void updatePointcutStats(Pointcut pointcut, AspectManager manager)
   {
      // the following is for performance reasons.
      if (pointcut instanceof PointcutExpression)
      {
         PointcutExpression expr = (PointcutExpression) pointcut;
         expr.setManager(manager);
         PointcutStats stats = expr.getStats();
         updateStats(stats);
      }
      else
      {
         // can't be sure so set all
         execution = true;
         construction = true;
         call = true;
         within = true;
         get = true;
         set = true;
         withincode = true;
      }
   }

   public void updateStats(PointcutStats stats)
   {
      lockWrite();
      try
      {
         if (stats != null)
         {
            construction |= stats.isConstruction();
            execution |= stats.isExecution();
            call |= stats.isCall();
            within |= stats.isWithin();
            get |= stats.isGet();
            set |= stats.isSet();
            withincode |= stats.isWithincode();
         }
         else
         {
            if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug("Setting all pointcut stats to true");
            // can't be sure so set all
            execution = true;
            construction = true;
            call = true;
            within = true;
            get = true;
            set = true;
            withincode = true;
         }
      }
      finally
      {
         unlockWrite();
      }
   }

}
