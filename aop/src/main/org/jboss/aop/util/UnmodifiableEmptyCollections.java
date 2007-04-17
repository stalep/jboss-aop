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
package org.jboss.aop.util;

import gnu.trove.TLongObjectHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.jboss.util.collection.WeakValueHashMap;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArraySet;


/**
 * Implementation of different types of maps, lists etc. that do not support modification.
 * Collections.unmodifiableMap() returns an instanceof Collections$UnmodifiableMap
 * which can only be cast to the Map interface and the same goes for lists
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class UnmodifiableEmptyCollections
{
   public static final LinkedHashMap EMPTY_LINKED_HASHMAP = new LockedLinkedHashMap();
   public static final ConcurrentReaderHashMap EMPTY_CONCURRENT_READER_HASHMAP = new LockedConcurrentReaderHashMap();
   public static final HashMap EMPTY_HASHMAP = new LockedHashMap();
   public static final WeakHashMap EMPTY_WEAK_HASHMAP = new LockedWeakHashMap();
   public static final WeakValueHashMap EMPTY_WEAK_VALUE_HASHMAP = new LockedWeakValueHashMap();
   public static final ArrayList EMPTY_ARRAYLIST = new LockedArrayList();
   public static final CopyOnWriteArraySet EMPTY_COPYONWRITE_ARRAYSET = new LockedCopyOnWriteArraySet();
   public static final TLongObjectHashMap EMPTY_TLONG_OBJECT_HASHMAP = new LockedTLongObjectHashMap();

   private static class LockedHashMap<K,V> extends HashMap<K,V>
   {
      private static final long serialVersionUID = 1L;

      @Override
      public V put(K key, V value) 
      {
         throw new UnsupportedOperationException();
      }
      @Override
      public void putAll(Map<? extends K, ? extends V> t) 
      {
         throw new UnsupportedOperationException();
      }
   }

   private static class LockedLinkedHashMap<K,V> extends LinkedHashMap<K,V>
   {
      private static final long serialVersionUID = 1L;

      @Override
      public V put(K key, V value) 
      {
         throw new UnsupportedOperationException();
      }
      @Override
      public void putAll(Map<? extends K, ? extends V> t) 
      {
         throw new UnsupportedOperationException();
      }
   }

   private static class LockedConcurrentReaderHashMap extends ConcurrentReaderHashMap
   {
      private static final long serialVersionUID = 1L;

      @Override
      public Object put(Object arg0, Object arg1)
      {
         return super.put(arg0, arg1);
      }
      @Override
      public synchronized void putAll(Map arg0)
      {
         super.putAll(arg0);
      }
   }
   
   private static class LockedWeakHashMap<K,V> extends WeakHashMap<K,V>
   {
      private static final long serialVersionUID = 1L;
      @Override
      public V put(K key, V value) 
      {
         throw new UnsupportedOperationException();
      }
      @Override
      public void putAll(Map<? extends K, ? extends V> t) 
      {
         throw new UnsupportedOperationException();
      }
   }
   
   private static class LockedWeakValueHashMap extends WeakValueHashMap
   {
      private static final long serialVersionUID = 1L;
      @Override
      public Object put(Object arg0, Object arg1)
      {
         return super.put(arg0, arg1);
      }
      @Override
      public synchronized void putAll(Map arg0)
      {
         super.putAll(arg0);
      }
   }
   
   private static class LockedArrayList<E> extends ArrayList<E>
   {
      private static final long serialVersionUID = 1L;
      
      @Override
      public E set(int index, E element) 
      {
         throw new UnsupportedOperationException();
      }
      @Override
      public void add(int index, E element) 
      {
         throw new UnsupportedOperationException();
      }
      @Override
      public boolean addAll(int index, Collection<? extends E> c) 
      {
         throw new UnsupportedOperationException();
      }
   }
   
   private static class LockedCopyOnWriteArraySet extends CopyOnWriteArraySet
   {
      private static final long serialVersionUID = 1L;

      @Override
      public boolean add(Object arg0)
      {
         return super.add(arg0);
      }

      @Override
      public boolean addAll(Collection arg0)
      {
         return super.addAll(arg0);
      }
   }

   
   private static class LockedTLongObjectHashMap extends TLongObjectHashMap
   {
      private static final long serialVersionUID = 1L;
      @Override
      public Object put(long arg0, Object arg1)
      {
         throw new UnsupportedOperationException();
      }
   }
}
