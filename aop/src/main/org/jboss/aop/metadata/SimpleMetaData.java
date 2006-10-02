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
package org.jboss.aop.metadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.util.MarshalledValue;
import org.jboss.aop.util.PayloadKey;

/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class SimpleMetaData implements MetaDataResolver, java.io.Externalizable
{
   static final long serialVersionUID = -3873275588469743345L;
   protected HashMap metaData = new HashMap();

   public class MetaDataValue implements java.io.Serializable
   {
      static final long serialVersionUID = -8024138149680591337L;
      public final PayloadKey type;
      public Object value;

      public MetaDataValue(PayloadKey type, Object value)
      {
         this.type = type;
         this.value = value;
      }

      public Object get()
      throws java.io.IOException, ClassNotFoundException
      {
         if (value instanceof MarshalledValue)
         {
            value = ((MarshalledValue) value).get();
         }
         return value;
      }
      
      public String toString()
      {
         StringBuffer sb = new StringBuffer(100);
         sb.append("[");
         sb.append("type=").append(type);
         sb.append("value=").append(value);
         sb.append("]");
         return sb.toString();
      }

   }

   public synchronized int size()
   {
      return metaData.size();
   }

   public synchronized HashSet tags()
   {
      return new HashSet(metaData.keySet());
   }

   public synchronized HashMap tag(String name)
   {
      HashMap map = (HashMap) metaData.get(name);
      if (map == null) return null;
      return (HashMap) map.clone();
   }

   public synchronized boolean hasTag(String name)
   {
      return metaData.get(name) != null;
   }

   /**
    * Tag metadata to structure.  Use for tags with no attributes
    * i.e. @Singleton, etc...
    *
    * @param group
    */
   public void tag(Object tag)
   {
      addMetaData(tag, EMPTY_TAG, new Object(), PayloadKey.TRANSIENT);
   }

   public void addMetaData(Object tag, Object attr, Object value)
   {
      addMetaData(tag, attr, value, PayloadKey.MARSHALLED);
   }

   public synchronized void addMetaData(Object tag, Object attr, Object value, PayloadKey type)
   {
      HashMap groupData = (HashMap) metaData.get(tag);
      if (groupData == null)
      {
         groupData = new HashMap();
         metaData.put(tag, groupData);
      }
      MetaDataValue val = new MetaDataValue(type, value);
      groupData.put(attr, val);
   }

   public synchronized Object getMetaData(Object tag, Object attr)
   {
      try
      {
         HashMap groupData = (HashMap) metaData.get(tag);
         if (groupData == null) return null;
         MetaDataValue val = (MetaDataValue) groupData.get(attr);
         if (val == null) return null;
         return val.get();
      }
      catch (IOException ioex)
      {
         throw new RuntimeException("failed on MarshalledValue", ioex);
      }
      catch (ClassNotFoundException ex)
      {
         throw new RuntimeException("failed on MarshalledValue", ex);
      }
   }

   public synchronized void removeMetaData(Object tag, Object attr)
   {
      HashMap groupData = (HashMap) metaData.get(tag);
      if (groupData != null)
      {
         groupData.remove(attr);
      }
   }

   public synchronized void removeGroupData(Object group)
   {
      metaData.remove(group);
   }

   public synchronized void clear()
   {
      metaData.clear();
   }

   /**
    * merges incoming data.  Incoming data overrides existing data
    */
   public synchronized void mergeIn(SimpleMetaData data)
   {
      Iterator it = data.metaData.keySet().iterator();
      while (it.hasNext())
      {
         Object tag = it.next();
         HashMap attrs = (HashMap) data.metaData.get(tag);
         HashMap map = (HashMap) metaData.get(tag);
         if (map == null)
         {
            map = new HashMap();
            this.metaData.put(tag, map);
         }
         map.putAll(attrs);
      }
   }

   public synchronized Object resolve(Invocation invocation, Object tag, Object attr)
   {
      return getMetaData(tag, attr);
   }

   public SimpleMetaData getAllMetaData(Invocation invocation)
   {
      return this;
   }

   public void writeExternal(java.io.ObjectOutput out)
   throws IOException
   {
      //System.out.println("******** marshalling metadata");
      Iterator it = metaData.keySet().iterator();
      while (it.hasNext())
      {
         Object group = it.next();
         HashMap map = (HashMap) metaData.get(group);
         //System.out.println("******** marshalling group " + group + " size = " + map.size());
         if (map != null && map.size() > 0)
         {
            boolean groupWritten = false;
            Iterator attrs = map.keySet().iterator();
            while (attrs.hasNext())
            {
               Object attr = attrs.next();
               //System.out.println("******** marshalling attr: " + group + "." + attr);
               MetaDataValue value = (MetaDataValue) map.get(attr);
               if (value.type == PayloadKey.TRANSIENT) continue;
               if (!groupWritten)
               {
                  groupWritten = true;
                  out.writeObject(group);
               }
               out.writeObject(attr);
               if (value.type == PayloadKey.AS_IS)
               {
                  out.writeObject(value.value);
               }
               else
               {
                  out.writeObject(new MarshalledValue(value.value));
               }
            }
            if (groupWritten) out.writeObject(null); // placeholder for end of attributes
         }
      }
      out.writeObject(null); // place holder for end of marshall
   }

   public void readExternal(java.io.ObjectInput in)
   throws IOException, ClassNotFoundException
   {
      //System.out.println("******** unmarshalling metadata");
      metaData = new HashMap();
      Object group;
      while ((group = in.readObject()) != null)
      {
         //System.out.println("******** unmarshalling group: " + group);
         HashMap map = new HashMap();
         metaData.put(group, map);
         Object attr;
         while ((attr = in.readObject()) != null)
         {
            //System.out.println("******** unmarshalling attr: " + group + "." + attr);
            Object obj = in.readObject();
            if (obj instanceof MarshalledValue)
            {
               map.put(attr, new MetaDataValue(PayloadKey.MARSHALLED, obj));
            }
            else
            {
               map.put(attr, new MetaDataValue(PayloadKey.AS_IS, obj));
            }
         }
      }
   }

   public String toString()
   {
      StringBuffer sb = new StringBuffer(100);
      sb.append("[");
      sb.append("metaData=").append(metaData);
      sb.append("]");
      return sb.toString();
   }
}
