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

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.jboss.aop.advice.GeneratedAdvisorInterceptor;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.JoinPointBean;
import org.jboss.aop.joinpoint.Joinpoint;

public abstract class JoinPointInfo implements JoinPointBean
{
   private Interceptor[] interceptors;

   private ArrayList interceptorChain = new ArrayList();
   
   private WeakReference advisor;
   
   protected volatile Joinpoint joinpoint;
   
   protected WeakReference clazz;
   
   private String adviceString;

   protected JoinPointInfo()
   {
      this.clazz = new WeakReference(null);
   }
   
   protected JoinPointInfo(Advisor advisor, Class clazz)
   {
      setAdvisor(advisor);
      this.clazz = new WeakReference(clazz); 
   }
   
   /*
    * For copying
    */
   protected JoinPointInfo(JoinPointInfo other)
   {
      this.advisor = other.advisor;
      this.clazz = other.clazz;
      if (other.interceptors != null)
      {
         this.interceptors = new Interceptor[other.interceptors.length];
         System.arraycopy(other.interceptors, 0, this.interceptors, 0, other.interceptors.length);
      }
      if (other.interceptorChain != null)this.interceptorChain.addAll(interceptorChain);
   }

   protected void clear()
   {
      interceptorChain.clear();
      interceptors = null;
   }
   
   public Advisor getAdvisor() 
   {
      if (advisor == null)
      {
         return null;
      }
      return (Advisor)advisor.get();
   }

   public Class getClazz()
   {
      return (Class)clazz.get(); 
   }
   
   public void setAdvisor(Advisor advisor) 
   {
      this.advisor = new WeakReference(advisor);
   }

   public boolean hasAdvices()
   {
      return (interceptors != null && interceptors.length > 0);
   }
   
   public boolean equalChains(JoinPointInfo other)
   {
      if (this.interceptors == null && other.interceptors == null) return true;
      if (!(this.interceptors != null && other.interceptors != null))return false;
      if (this.interceptors.length != other.interceptors.length) return false;
      
      for (int i = 0 ; i < this.interceptors.length ; i++)
      {
         if(!this.interceptors[i].equals(other.interceptors[i])) return false;
      }

      return true;
   }
   
   public Joinpoint getJoinpoint()
   {
      if (joinpoint == null)
      {
         joinpoint = internalGetJoinpoint();
      }
      return joinpoint;
   }
   
   public ArrayList getInterceptorChain() {
      return interceptorChain;
   }

   public void setInterceptorChain(ArrayList interceptorChain) {
      adviceString = null;
      this.interceptorChain = interceptorChain;
   }

   public Interceptor[] getInterceptors() {
      return interceptors;
   }

   public void setInterceptors(Interceptor[] interceptors) {
      adviceString = null;
      this.interceptors = interceptors;
   }

   protected abstract Joinpoint internalGetJoinpoint();
   public abstract JoinPointInfo copy();
   
   public Object resolveClassMetaData(Object key, Object attr)
   {
      return getAdvisor().getClassMetaData().getMetaData(key, attr);
   }
   
   public Object resolveClassAnnotation(Class annotation)
   {
      Advisor advisor = getAdvisor();
      if (advisor != null)
      {
         return advisor.resolveAnnotation(annotation);
      }
      return null;
   }
   
   public Object resolveAnnotation(Class annotation)
   {
      return null;
   }
   
   public void cloneChains(JoinPointInfo other)
   {
      interceptorChain = (ArrayList)other.interceptorChain.clone();
      if (other.interceptors == null)
      {
         interceptors = null;
      }
      else
      {
         interceptors = other.interceptors.clone();
      }
   }
   
   public String getAdviceString()
   {
      if (adviceString == null)
      {
         if (interceptors == null || interceptors.length == 0)
         {
            return "";
         }
         
         StringBuffer buf = new StringBuffer();
         for (int i = 0 ; i < interceptors.length ; i++)
         {
            if (i > 0)
            {
               buf.append(",");
            }
            
            GeneratedAdvisorInterceptor icptr = (GeneratedAdvisorInterceptor)interceptors[i];
            buf.append(icptr.getAdviceString());
         }
         
         return buf.toString();
      }
      
      return adviceString; 
   }
}
