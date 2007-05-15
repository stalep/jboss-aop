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
package org.jboss.aspects.versioned;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.Query;
import javax.management.QueryExp;

import org.jboss.aop.InstanceAdvised;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.interfaces.HAPartition.HAPartitionStateTransfer;
import org.jboss.ha.framework.server.ClusterPartitionMBean;
import org.jboss.logging.Logger;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;
import org.jboss.util.id.GUID;
/**
 * This is a LRU cache.  The TxCache itself is not transactional
 * but any accesses to objects within the cache ARE transactional.
 */
public class DistributedTxCache implements HAPartitionStateTransfer
{

   private static class LRUCache extends LinkedHashMap
   {
      private static final long serialVersionUID = -402696519285213913L;

      private int maxSize;
      public LRUCache(int max)
      {
         super(16, 0.75F, true);
         this.maxSize = max;
      }
      protected boolean removeEldestEntry(Map.Entry eldest)
      {
         return this.size() > maxSize;
      }
   }

   protected static Logger log = Logger.getLogger(DistributedTxCache.class);
   protected long lockTimeout;
   protected DistributedSynchronizationManager synchManager;
   protected DistributedVersionManager versionManager;
   protected String partitionName;
   protected HAPartition partition;
   protected String cacheName;
   protected LRUCache cache = null;
   protected int maxSize;

   public DistributedTxCache(int maxSize, long lockTimeout, String cacheName)
   {
      this(maxSize, lockTimeout, cacheName, "DefaultPartition");
   }

   public DistributedTxCache(int maxSize, long lockTimeout, String cacheName, String pName)
   {
      this.lockTimeout = lockTimeout;
      this.partitionName = pName;
      this.maxSize = maxSize;
      this.cacheName = "DistributedTxCache/" + cacheName;
   }

   // HAPartition.HAPartitionStateTransfer Implementation --------------------------------------------------------
   
   protected HAPartition findHAPartitionWithName (String name) throws Exception
   {
      HAPartition result = null;
      MBeanServer server = MBeanServerLocator.locate();
      // Class name match does not work with the AOP proxy :(
//    QueryExp classEQ = Query.eq(Query.classattr(),
//             Query.value(ClusterPartition.class.getName()));
      QueryExp exp = Query.and(
                        Query.match(
                           Query.attr("Name"),
                           Query.value("ClusterPartition")
                        ),
                        Query.match(
                           Query.attr("PartitionName"),
                           Query.value(name)
                        )
                      );

      Set mbeans = server.queryMBeans (null, exp);
      if (mbeans != null && mbeans.size () > 0)
      {
         for (Iterator iter = mbeans.iterator(); iter.hasNext();)
         {
            ObjectInstance inst = (ObjectInstance) iter.next();
            try
            {
               ClusterPartitionMBean cp = (ClusterPartitionMBean) MBeanProxyExt.create (
                                                   ClusterPartitionMBean.class, 
                                                   inst.getObjectName (),
                                                   server);
               result = cp.getHAPartition();
               break;
            }
            catch (Exception e) {}
         }
      }
      
      return result;
   }

   public void create() throws Exception
   {
      this.partition = findHAPartitionWithName(partitionName);
      //REVISIT: doesn't really buy us anything until JGroups synchronizes
      // initial state correctly
      //partition.subscribeToStateTransferEvents(cacheName, this);
      //REVISIT AGAIN: Actually I talked to Bela about this.  I can change the
      //Clustering framework to do state transfer correctly
      partition.registerRPCHandler(cacheName, this);
      synchManager = new DistributedSynchronizationManager(cacheName, null, partition);
      versionManager = new DistributedVersionManager(lockTimeout, synchManager);
      synchManager.versionManager = versionManager;
      synchManager.create();
   }

   public synchronized void start() throws Exception
   {
      synchManager.start();
      pullState();
      if (cache == null) cache = new LRUCache(maxSize);
   }

   protected void pullState() throws Exception
   {
      Object[] args = {};
      List rsp = partition.callMethodOnCluster(cacheName, "getCurrentState", args, true);
      if (rsp.size() > 0)
      {
         setCurrentState((Serializable)rsp.get(0));
      }
   }


   public synchronized void _insert(Object key, Object obj)
   {
      cache.put(key, obj);
   }

   public void insert(Object key, Object obj) throws Exception
   {
      try
      {
         obj = versionManager.makeVersioned(obj);
         if (versionManager.isVersioned(obj))
         {
            log.trace("Inserting versioned object");
            obj = VersionManager.getGUID((InstanceAdvised)obj);
         }
         else
         {
            log.trace("Inserting a non-Versioned object");
         }
         Object[] args = {key, obj};
         partition.callMethodOnCluster(cacheName, "_insert", args, false);
      }
      catch (Exception ex)
      {
         ex.printStackTrace();
         throw ex;
      }
   }

   public synchronized void _remove(Object key)
   {
      cache.remove(key);
   }

   public void remove(Object key)
   {
      Object[] args = {key};
      try
      {
         partition.callMethodOnCluster(cacheName, "_remove", args, false);
      }
      catch (Exception ex)
      {
         throw new RuntimeException(ex);
      }
   }

   
   public synchronized void _flush()
   {
      cache.clear();
   }

   public void flush(Object key)
   {
      Object[] args = {};
      try
      {
         partition.callMethodOnCluster(cacheName, "_flush", args, false);
      }
      catch (Exception ex)
      {
         throw new RuntimeException(ex);
      }
   }

   
   public synchronized Object get(Object key)
   {
      Object obj = cache.get(key);
      if (obj instanceof GUID)
      {
         GUID guid = (GUID)obj;
         obj = synchManager.getObject(guid);
      }
      return obj;
   }

   public Serializable getCurrentState()
   {
      log.trace("getCurrentState called on cache");
      return cache;
   }

   public void setCurrentState(Serializable newState)
   {
      log.trace("setCurrentState called on cache");
      synchronized (this)
      {
         this.cache = (LRUCache)newState;
      }
   }

}
