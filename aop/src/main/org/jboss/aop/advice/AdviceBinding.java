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

import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.PointcutExpression;
import org.jboss.aop.pointcut.ast.ASTCFlowExpression;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.aop.pointcut.ast.PointcutExpressionParser;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class AdviceBinding
{
   private static volatile long counter = 0;

   protected String name;
   protected Pointcut pointcut;
   protected ASTCFlowExpression cflow;
   protected String cflowString;

   // not list because of redundancy caused by successive calls of ClassAdvisor.rebuildInterceptors
   protected Collection advisors = new HashSet();
   protected InterceptorFactory[] interceptorFactories = new InterceptorFactory[0];

   public AdviceBinding() {}

   public AdviceBinding(String name, Pointcut p, ASTCFlowExpression cflow, String cflowString, InterceptorFactory[] factories) throws ParseException
   {
      this.name = name;
      interceptorFactories = factories;
      this.cflow = cflow;

      pointcut = p;
      this.cflowString = cflowString;
   }

   /**
    * This constructor is used for creation of AdviceBinding programmatically
    *
    * @param pointcutExpression
    * @param cflow
    * @throws ParseException
    */
   public AdviceBinding(String pointcutExpression, String cflow) throws ParseException
   {
      this(Long.toString(System.currentTimeMillis()) + ":" + Long.toString(counter++), pointcutExpression, cflow);
   }

   /**
    * This constructor is used for creation of AdviceBinding programmatically
    *
    * @param pointcutExpression
    * @param cflow
    * @throws ParseException
    */
   public AdviceBinding(String name, String pointcutExpression, String cflow) throws ParseException
   {
      this.name = name;
      setPointcutExpression(pointcutExpression);
      setCFlowExpression(cflow);
      interceptorFactories = new InterceptorFactory[0];
   }

   public void setCFlowExpression(String cflow)
           throws ParseException
   {
      if (cflow != null)
      {
         cflowString = cflow;
         this.cflow = new PointcutExpressionParser(new StringReader(cflowString)).CFlowExpression();
      }
   }

   public void setPointcutExpression(String pointcutExpression)
           throws ParseException
   {
      pointcut = new PointcutExpression(Long.toString(System.currentTimeMillis()) + ":" + Long.toString(counter++), pointcutExpression);
   }

   public void addInterceptorFactory(InterceptorFactory factory)
   {
      List list = Arrays.asList(interceptorFactories);
      list = new ArrayList(list);
      list.add(factory);
      interceptorFactories = (InterceptorFactory[]) list.toArray(new InterceptorFactory[list.size()]);
   }


   /**
    * Add an interceptor to chain.  This is an actual class
    * that implements Interceptor.  A GenericInterceptorFactory will
    * be created to wrap the class.
    *
    * @param clazz
    */
   public void addInterceptor(Class clazz)
   {
      addInterceptorFactory(new GenericInterceptorFactory(clazz));
   }

   public String getName()
   {
      return name;
   }

   public InterceptorFactory[] getInterceptorFactories()
   {
      return interceptorFactories;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void addAdvisor(Advisor advisor)
   {
      if (AspectManager.verbose) System.out.println("[debug] added advisor: " + advisor.getName() + " from binding: " + name);
      // Don't hold a direct reference to an advisor because of undeploy and redeploy.  Use WeakRefrences because
      // we may be having in the future an Advisor per instance.
      synchronized (advisors)
      {
         if (advisors.size() > 0)
         {
            Iterator it = advisors.iterator();
            while (it.hasNext())
            {
               WeakReference ref = (WeakReference) it.next();
               Object obj = ref.get();
               if (obj == null) it.remove();
               else if(obj.equals(advisor))
               {
                  return; // don't add duplicate advisor
               }
            }
         }
         advisors.add(new WeakReference(advisor));
      }
      
   }

   public boolean hasAdvisors()
   {
      return advisors.size() > 0;
   }

   public ArrayList getAdvisors()
   {
      ArrayList list = new ArrayList(advisors.size());
      synchronized (advisors)
      {
         Iterator it = advisors.iterator();
         while (it.hasNext())
         {
            WeakReference ref = (WeakReference) it.next();
            Object advisor = ref.get();
            if (advisor != null)
            {
               list.add(advisor);
            }
            else
            {
               it.remove();
            }
         }
      }
      return list;
   }

   public void clearAdvisors()
   {
      synchronized (advisors)
      {
         for (Iterator it = advisors.iterator(); it.hasNext();)
         {
            WeakReference ref = (WeakReference) it.next();
            Object obj = ref.get();
            if (obj != null)
            {
               Advisor advisor = (Advisor) obj;
               if (advisor.getManager().isAdvisorRegistered(advisor))
               {
                  advisor.removeAdviceBinding(this);
               }
            }
         }
         advisors.clear();
      }
   }

   public boolean equals(Object obj)
   {
      if (obj == this) return true;
      if (!(obj instanceof AdviceBinding)) return false;
      return ((AdviceBinding) obj).getName().equals(name);
   }

   public int hashCode()
   {
      return name.hashCode();
   }

   public Pointcut getPointcut()
   {
      return pointcut;
   }

   public ASTCFlowExpression getCFlow()
   {
      return cflow;
   }

   public String getCFlowString()
   {
      return cflowString;
   }
}
