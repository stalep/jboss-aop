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
package org.jboss.aspects.tx;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.HashMap;
import org.jboss.aop.Advisor;
import org.jboss.aop.InstanceAdvisor;
import org.jboss.aop.joinpoint.ConstructorJoinpoint;
import org.jboss.aop.joinpoint.Joinpoint;
import org.jboss.aop.joinpoint.MethodJoinpoint;
import org.jboss.aop.joinpoint.FieldJoinpoint;
import org.jboss.tm.TransactionManagerLocator;

/**
 *  This interceptor handles transactions for AOP 
 *
 *  @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 *  @version $Revision$
 */
public class TxInterceptorFactory implements org.jboss.aop.advice.AspectFactory
{
   protected TxPolicy policy;
   protected HashMap nameMap = new HashMap();

   protected void initializePolicy()
   {
      policy = new TxPolicy();
   }

   public void initialize()
   {
      if (policy != null) return;
      initializePolicy();
      nameMap.put("NEVER", new TxInterceptor.Never(TransactionManagerLocator.getInstance().locate(), policy));
      nameMap.put("NOTSUPPORTED", new TxInterceptor.NotSupported(TransactionManagerLocator.getInstance().locate(), policy));
      nameMap.put("SUPPORTS", new TxInterceptor.Supports(TransactionManagerLocator.getInstance().locate(), policy));
      nameMap.put("REQUIRED", new TxInterceptor.Required(TransactionManagerLocator.getInstance().locate(), policy));
      nameMap.put("REQUIRESNEW", new TxInterceptor.RequiresNew(TransactionManagerLocator.getInstance().locate(), policy));
      nameMap.put("MANDATORY", new TxInterceptor.Mandatory(TransactionManagerLocator.getInstance().locate(), policy));
   }

   protected String resolveTxType(Advisor advisor, Joinpoint jp)
   {
      if (jp instanceof ConstructorJoinpoint)
      {
         Constructor con = ((ConstructorJoinpoint)jp).getConstructor();
         String txType = (String)advisor.getConstructorMetaData().getConstructorMetaData(con, "transaction", "trans-attribute");
         if (txType != null) return txType;

         txType = (String)advisor.getDefaultMetaData().getMetaData("transaction", "trans-attribute");
         if (txType != null) return txType;

         Tx tx = (Tx)advisor.resolveAnnotation(con, Tx.class);
         if (tx == null)
         {
            tx = (Tx)advisor.resolveAnnotation(Tx.class);
         }
         if (tx == null) return "REQUIRED";
         return tx.value().name();
      }
      else if (jp instanceof MethodJoinpoint)
      {
         Method con = ((MethodJoinpoint)jp).getMethod();
         String txType = (String)advisor.getMethodMetaData().getMethodMetaData(con, "transaction", "trans-attribute");
         if (txType != null) return txType;

         txType = (String)advisor.getDefaultMetaData().getMetaData("transaction", "trans-attribute");
         if (txType != null) return txType;

         Tx tx = (Tx)advisor.resolveAnnotation(con, Tx.class);
         if (tx == null)
         {
            tx = (Tx)advisor.resolveAnnotation(Tx.class);
         }
         if (tx == null) return "REQUIRED";
         return tx.value().name();
      }
      else
      {
         Field con = ((FieldJoinpoint)jp).getField();
         String txType = (String)advisor.getFieldMetaData().getFieldMetaData(con, "transaction", "trans-attribute");
         if (txType != null) return txType;

         txType = (String)advisor.getDefaultMetaData().getMetaData("transaction", "trans-attribute");
         if (txType != null) return txType;

         Tx tx = (Tx)advisor.resolveAnnotation(con, Tx.class);
         if (tx == null)
         {
            tx = (Tx)advisor.resolveAnnotation(Tx.class);
         }
         if (tx == null) return "REQUIRED";
         return tx.value().name();
      }
   }


   public Object createPerJoinpoint(Advisor advisor, Joinpoint jp)
   {
      initialize();
      String txType = resolveTxType(advisor, jp);
      Object rtn = nameMap.get(txType.toUpperCase());
      if (rtn == null) throw new RuntimeException("TX TYPE was null for: " + txType);
      return rtn;
   }

   public Object createPerVM()
   {
      throw new IllegalStateException("Scope not allowed");
   }

   public Object createPerClass(Advisor advisor)
   {
      throw new IllegalStateException("Scope not allowed");
   }

   public Object createPerInstance(Advisor advisor, InstanceAdvisor instanceAdvisor)
   {
      throw new IllegalStateException("Scope not allowed");
   }

   public Object createPerJoinpoint(Advisor advisor, InstanceAdvisor instanceAdvisor, Joinpoint jp)
   {
      throw new IllegalStateException("Scope not allowed");
   }

   public String getName()
   {
      return getClass().getName();
   }
}
