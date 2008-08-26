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
import java.util.LinkedHashSet;

import org.jboss.aop.AspectManager;
import org.jboss.aop.util.BindingClassifier;
import org.jboss.aop.util.UnmodifiableEmptyCollections;
import org.jboss.aop.util.logging.AOPLogger;

/**
 * Manages the binding collection contained in a domain. All bindings
 * contained in this collection are indexed according to their classification.
 * <p>
 * <i>For internal use only.</i>
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public class ClassifiedBindingCollection
{
   private static final AOPLogger logger = AOPLogger.getLogger(AspectManager.class);
  
   private volatile LinkedHashMap<String, AdviceBinding> bindings;
   private volatile Collection<AdviceBinding> fieldReadBindings;
   private volatile Collection<AdviceBinding> fieldWriteBindings;
   private volatile Collection<AdviceBinding> constructionBindings;
   private volatile Collection<AdviceBinding> constructorExecutionBindings;
   private volatile Collection<AdviceBinding> methodExecutionBindings;
   private volatile Collection<AdviceBinding> constructorCallBindings;
   private volatile Collection<AdviceBinding> methodCallBindings;
   
   @SuppressWarnings("all")
   /**
    * Constructor.<p>
    * All created instances must be initialized before being used for addition and
    * removal operations, by calling {@code initialize()}.
    */
   public ClassifiedBindingCollection()
   {
      bindings = UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP;
      this.fieldReadBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.fieldWriteBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.constructionBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.constructorExecutionBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.methodExecutionBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.constructorCallBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
      this.methodCallBindings = UnmodifiableEmptyCollections.EMPTY_ARRAYLIST;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully field read
    * joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match field
    *         read pointcuts
    */
   public synchronized Collection<AdviceBinding> getFieldReadBindings()
   {
      return this.fieldReadBindings;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully field write
    * joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match field
    *         write pointcuts
    */
   public synchronized Collection<AdviceBinding> getFieldWriteBindings()
   {
      return this.fieldWriteBindings;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully construction
    * joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         construction pointcuts
    */
   public synchronized Collection<AdviceBinding> getConstructionBindings()
   {
      return this.constructionBindings;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully constructor
    * execution joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         constructor execution pointcuts
    */
   public synchronized Collection<AdviceBinding> getConstructorExecutionBindings()
   {
      return this.constructorExecutionBindings;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully method
    * execution joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         method execution pointcuts
    */
   public synchronized Collection<AdviceBinding> getMethodExecutionBindings()
   {
      return this.methodExecutionBindings;
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
   public synchronized Collection<AdviceBinding> getConstructorCallBindings()
   {
      return this.constructorCallBindings;
   }
   
   /**
    * Returns only the bindings whose pointcuts may match successfully method
    * call joinpoints.<p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    * 
    * @return a collection containing exclusively the bindings that may match
    *         method call pointcuts
    */
   public synchronized Collection<AdviceBinding> getMethodCallBindings()
   {
      return this.methodCallBindings;
   }
   
   /**
    * Indicate whether this collection is empty.
    */
   public synchronized boolean isEmpty()
   {
      return this.bindings.isEmpty();
   }
   
   /**
    * Returns the bindings map.
    * <p>
    * <b>Attention:</b> this collection is not supposed to be edited.
    */
   public LinkedHashMap<String, AdviceBinding> getBindings()
   {
      return bindings;
   }
   
   /**
    * Adds a binding to this collection.
    */
   public synchronized void add(AdviceBinding binding)
   {
      bindings.put(binding.getName(), binding);
      if (BindingClassifier.isGet(binding))
      {
         this.fieldReadBindings.add(binding);
      }
      if (BindingClassifier.isSet(binding))
      {
         this.fieldWriteBindings.add(binding);
      }
      if (BindingClassifier.isConstruction(binding))
      {
         this.constructionBindings.add(binding);
      }
      if (BindingClassifier.isConstructorExecution(binding))
      {
         this.constructorExecutionBindings.add(binding);
      }
      if (BindingClassifier.isMethodExecution(binding))
      {
         this.methodExecutionBindings.add(binding);
      }
      if (BindingClassifier.isConstructorCall(binding))
      {
         this.constructorCallBindings.add(binding);
      }
      if (BindingClassifier.isMethodCall(binding))
      {
         this.methodCallBindings.add(binding);
      }
   }
   
   /**
    * Removes the binding named {@code name}.
    * 
    * @param name name of the binding to be removed.
    * @return the removed binding. If {@code null}, indicates that there is no
    *         binding with name equal to {@code name} in this collection.
    */
   public synchronized AdviceBinding remove(String name)
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
   
   /**
    * Removes all bindings whose names are contained in {@code names}.
    * 
    * @param names names of all bindings to be removed
    * @return the collection of the removed bindings
    */
   public synchronized ArrayList<AdviceBinding> remove(ArrayList<String> names)
   {
      ArrayList<AdviceBinding> removedBindings = new ArrayList<AdviceBinding>();
      for (String name: names)
      {
         AdviceBinding binding = this.remove(name);
         if (binding == null)
         {
            logger.debug("ClassifiedBindingCollection.removeBindings() no binding found with name " + name);
            continue;
         }
         removedBindings.add(binding);
      }
      return removedBindings;
   }
   
   /**
    * Indicates if this collection is initialized. If it is not, no addition
    * operation can be performed.
    */
   public synchronized boolean isInitialized()
   {
      return bindings != UnmodifiableEmptyCollections.EMPTY_LINKED_HASHMAP;
   }
   
   /**
    * Initializes this collection. This method must be called only if this collection
    * is not initialized.
    */
   public synchronized void initialize()
   {
      bindings = new LinkedHashMap<String, AdviceBinding>();
      this.fieldReadBindings = new LinkedHashSet<AdviceBinding>(0);
      this.fieldWriteBindings = new LinkedHashSet<AdviceBinding>(0);
      this.constructionBindings = new LinkedHashSet<AdviceBinding>(0);
      this.constructorExecutionBindings = new LinkedHashSet<AdviceBinding>(0);
      this.methodExecutionBindings = new LinkedHashSet<AdviceBinding>(0);
      this.constructorCallBindings = new LinkedHashSet<AdviceBinding>(0);
      this.methodCallBindings = new LinkedHashSet<AdviceBinding>(0);
   }
}
