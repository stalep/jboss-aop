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

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jboss.aop.advice.AdviceBinding;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.instrument.ConstructorExecutionTransformer;
import org.jboss.aop.instrument.FieldAccessTransformer;
import org.jboss.aop.instrument.MethodExecutionTransformer;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.joinpoint.ConstructorCalledByConstructorInvocation;
import org.jboss.aop.joinpoint.ConstructorCalledByConstructorJoinpoint;
import org.jboss.aop.joinpoint.ConstructorCalledByMethodInvocation;
import org.jboss.aop.joinpoint.ConstructorCalledByMethodJoinpoint;
import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.joinpoint.FieldJoinpoint;
import org.jboss.aop.joinpoint.FieldReadInvocation;
import org.jboss.aop.joinpoint.FieldWriteInvocation;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodCalledByConstructorInvocation;
import org.jboss.aop.joinpoint.MethodCalledByConstructorJoinpoint;
import org.jboss.aop.joinpoint.MethodCalledByMethodInvocation;
import org.jboss.aop.joinpoint.MethodCalledByMethodJoinpoint;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.aop.metadata.ClassMetaDataBinding;
import org.jboss.aop.metadata.ClassMetaDataLoader;
import org.jboss.aop.util.Advisable;
import org.jboss.aop.util.ConstructorComparator;
import org.jboss.aop.util.FieldComparator;
import org.jboss.aop.util.MethodHashing;
import org.jboss.aop.util.UnmodifiableEmptyCollections;
import org.jboss.aop.util.logging.AOPLogger;
import org.jboss.logging.Logger;
import org.jboss.util.NotImplementedException;

/**
 * Advises a class and provides access to the class's aspect chain.
 * Each advisable class has an associated <code>Advisor</code> instance.
 * References methods using <code>int</code> IDs rather than the actual
 * instances for
 * optimal performance. Provides ability to invoke methods on an advised
 * object without advice (see
 * <code>Advisor.invokeWithoutAdvisement()</code>).
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ClassAdvisor extends Advisor
{
   private static final Logger logger = AOPLogger.getLogger(ClassAdvisor.class);
   
   /**
    * Suffix added to unadvised methods.
    */
   public static final String NOT_TRANSFORMABLE_SUFFIX = "$aop";

   //Common sense suggests that this should be lazily initialised for generated advisors, profiling shows that is a major performance hit...
   protected TLongObjectHashMap unadvisedMethods = new TLongObjectHashMap();

   //Information about method by method caller pointcuts, lazy initialized when needed
   private volatile MethodByMethodData methodByMethodData;

   //Information about con by method caller pointcuts, lazy initialized when needed
   private volatile ConByMethodData conByMethodData;

   // caller pointcut support for constructors calling methods
   protected HashMap[] methodCalledByConBindings;
   protected HashMap[] methodCalledByConInterceptors;
   protected HashMap backrefMethodCalledByConstructorBindings = new HashMap();

   // caller pointcut support for constructors calling methods
   protected HashMap[] conCalledByConBindings;
   protected HashMap[] conCalledByConInterceptors;
   protected HashMap backrefConCalledByConstructorBindings = new HashMap();

   // Used by instrumentor to access separate interceptor chains for read and write access
   /** @deprecated Use fieldReadInfos instead*/
   private Interceptor[][] fieldReadInterceptors;
   protected FieldInfo[] fieldReadInfos;
   /** @deprecated Use fieldWriteInfos instead */
   private Interceptor[][] fieldWriteInterceptors;
   protected FieldInfo[] fieldWriteInfos;


   protected Field[] advisedFields;
   //PER_JOINPOINT aspects for static fields or PER_CLASS_JOINPOINT aspects
   //all apply to fields, and we need this since the same aspect should be used for
   //read and write
   private HashMap fieldAspectsWithNoInstance = new HashMap();

   protected boolean initialized = false;

   public ClassAdvisor(String classname, AspectManager manager)
   {
      super(classname, manager);
   }

   public ClassAdvisor(Class clazz, AspectManager manager)
   {
      this(clazz.getName(), manager);
      this.clazz = clazz;
   }


   /**
    * This method is to support PER_JOINPOINT scoping of Aspects for static fields
    * Fields are special in that a get and set do not create separate aspect instances.
    *
    * Also used to support PER_CLASS_JOINPOINT, since that behaves similarly to static fields
    *
    * @param joinpoint
    * @param def
    * @return
    */
   public Object getFieldAspect(FieldJoinpoint joinpoint, AspectDefinition def)
   {
      HashMap map = (HashMap)fieldAspectsWithNoInstance.get(def);
      if (map == null)
      {
         synchronized (fieldAspectsWithNoInstance)
         {
            map = (HashMap)fieldAspectsWithNoInstance.get(def);
            if (map == null)
            {
               map = new HashMap();
               fieldAspectsWithNoInstance.put(def, map);
            }
         }
      }

      Object aspect = map.get(joinpoint);
      if (aspect == null)
      {
         synchronized (map)
         {
            aspect = map.get(joinpoint);
            if (aspect == null)
            {
               aspect = def.getFactory().createPerJoinpoint(this, joinpoint);
               map.put(joinpoint, aspect);
            }
         }
      }

      return aspect;
   }

   public Field[] getAdvisedFields()
   {
      return advisedFields;
   }

   public TLongObjectHashMap getAdvisedMethods()
   {
      return advisedMethods;
   }

   protected TLongObjectHashMap getUnadvisedMethods()
   {
      return unadvisedMethods;
   }

   public Constructor[] getConstructors()
   {
      return constructors;
   }

   public TLongObjectHashMap getMethodCalledByMethodInterceptors()
   {
      return getMethodByMethodData().getMethodCalledByMethodInterceptors();
   }

   public HashMap[] getMethodCalledByConInterceptors()
   {
      return methodCalledByConInterceptors;
   }

   public HashMap[] getConCalledByConInterceptors()
   {
      return conCalledByConInterceptors;
   }

   public TLongObjectHashMap getConCalledByMethodInterceptors()
   {
      return getConByMethodData().getConCalledByMethodInterceptors();
   }

   public TLongObjectHashMap getMethodCalledByMethodBindings()
   {
      return getMethodByMethodData().getMethodCalledByMethodBindings();
   }

   /** @deprecated use getFieldReadInfos instead */
   public Interceptor[][] getFieldReadInterceptors()
   {
      throw new NotImplementedException("Use getFieldReadInfos");
   }

   public FieldInfo[] getFieldReadInfos()
   {
      return fieldReadInfos;
   }

   /** @deprecated use getFieldWriteInfos instead */
   public Interceptor[][] getFieldWriteInterceptors()
   {
      throw new NotImplementedException("Use getFieldWriteInfos");
   }

   public FieldInfo[] getFieldWriteInfos()
   {
      return fieldWriteInfos;
   }

   public TLongObjectHashMap getMethodInterceptors()
   {
      return methodInfos.infos;
   }
   

   /**
    * Constructs a new helper.
    */
   public synchronized void attachClass(final Class clazz)
   {
      if (initialized) return;
      try
      {
         //long start = System.currentTimeMillis();

         final ClassAdvisor THIS = this;
         final AspectManager theManager = manager;
         //register class loader: necessary when clazz was precompiled through aopc
         manager.registerClassLoader(clazz.getClassLoader());
         AccessController.doPrivileged(new PrivilegedExceptionAction<Object>()
         {
            public Object run() throws Exception
            {
               theManager.attachMetaData(THIS, clazz);
               interfaceIntroductions.clear();
               // metadata should always come before creation of interceptor chain
               // so that the interceptor factories have access to metadata.
               // and so that metadata joinpoints can be checked
               //

               THIS.clazz = clazz;

               // Also metadata needs to be applied before applyIntroductionPointcuts because
               // an annotation may be triggered by XML metadata as well as
               // after populateMixinMethods so that proper metadata is applied to added methods
               rebindClassMetaData();
               theManager.applyInterfaceIntroductions(THIS, clazz);
               createFieldTable();
               createMethodTables();
               createConstructorTables();
               populateMixinMethods();
               // metadata should always come before creation of interceptor chain
               // so that the interceptor factories have access to metadata.
               // and so that metadata joinpoints can be checked
               //
               // Also metadata needs to be applied before applyIntroductionPointcuts because
               // an annotation may be triggered by XML metadata as well as
               // after populateMixinMethods so that proper metadata is applied to added methods
               rebindClassMetaData();
               createInterceptorChains();
               initialized = true;
               return null;
            }
         });
         /*
         System.out.println("******************");
         System.out.println("attachClass: " + clazz.getName() + " took " + (System.currentTimeMillis() - start));
         System.out.println("******************");
         */
      }
      catch (PrivilegedActionException e)
      {
         throw new RuntimeException(e.getException());
      }
   }

   /**
    * Get method from clazz .If method not found,get the method
    * from the clazz's parent.
    */
   static private Method getMethod(Class clazz, Method method) throws NoSuchMethodException
   {

      if ((clazz == null) || (clazz.equals(Object.class))) throw new NoSuchMethodException(method.getName());
      try
      {
         String wrappedName = ClassAdvisor.notAdvisedMethodName(clazz.getName(), method.getName());
         return clazz.getMethod(wrappedName, method.getParameterTypes());
      }
      catch (NoSuchMethodException e)
      {
         return getMethod(clazz.getSuperclass(), method);
      }
   }

   /**
    * Get a constructor's index in the class. Returns -1 if not there
    */
   public int getConstructorIndex(Constructor constructor)
   {
      for (int i = 0; i < constructors.length; i++)
      {
         if (constructor.equals(constructors[i]))
         {
            return i;
         }
      }

      return -1;
   }

   /**
    * Get a field's index in the class. Returns -1 if not there
    */
   public int getFieldIndex(Field field)
   {
      for (int i = 0; i < advisedFields.length; i++)
      {
         if (field.equals(advisedFields[i]))
         {
            return i;
         }
      }

      return -1;
   }


   /**
    * Put mixin methods from mixin class into unadvisedMethods map so that
    * they can be correctly invoked upon.
    */
   protected void populateMixinMethods() throws Exception
   {
      ArrayList pointcuts = getInterfaceIntroductions();
      Iterator it = pointcuts.iterator();
      while (it.hasNext())
      {
         InterfaceIntroduction pointcut = (InterfaceIntroduction) it.next();
         ArrayList mixins = pointcut.getMixins();
         for (int i = 0; i < mixins.size(); i++)
         {
            InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin) mixins.get(i);
            // FIXME ClassLoader - how do we know the class is visible from the context classloader?
            ClassLoader cl = SecurityActions.getContextClassLoader();
            cl.loadClass(mixin.getClassName());
            String[] interfaces = mixin.getInterfaces();
            for (int j = 0; j < interfaces.length; j++)
            {
               Class intf = cl.loadClass(interfaces[j]);
               if (intf.isAssignableFrom(clazz))//This is a fix for JBAOP-365. Class may have been woven, with the extra mixin information only available at init time
               {
                  Method[] methods = intf.getMethods();
                  for (int k = 0; k < methods.length; k++)
                  {
                     //Put wrapped method in the class itself into the unadvisedMethods map
                     //   String wrappedName = ClassAdvisor.notAdvisedMethodName(clazz.getName(), methods[k].getName());
                     //   Method method = clazz.getMethod(wrappedName, methods[k].getParameterTypes());
                     Method method = getMethod(clazz, methods[k]);
                     long hash = MethodHashing.methodHash(method);
                     unadvisedMethods.put(hash, method);
                  }
               }
            }
         }
      }
   }


   public synchronized void removeAdviceBinding(AdviceBinding binding)
   {
      removeCallerPointcut(binding); // if binding is a caller remove references to it
      super.removeAdviceBinding(binding);
   }

   public synchronized void removeAdviceBindings(ArrayList bindings)
   {
      for (int i = 0; i < bindings.size(); i++)
      {
         AdviceBinding binding = (AdviceBinding) bindings.get(i);
         removeCallerPointcut(binding);
      }
      adviceBindings.removeAll(bindings);
      rebuildInterceptors();
      doesHaveAspects = adviceBindings.size() > 0;
   }

   protected void resolveFieldPointcut(FieldInfo[] newFieldInfos, AdviceBinding binding, boolean write)
   {
      for (int i = 0; i < newFieldInfos.length; i++)
      {
         Field field = newFieldInfos[i].getField();

         if ((!write && binding.getPointcut().matchesGet(this, field))
         || (write && binding.getPointcut().matchesSet(this, field)))
         {
            if (AspectManager.verbose) System.err.println("[debug] field matched " + ((write) ? "write" : "read") + " binding: " + field);
            adviceBindings.add(binding);
            binding.addAdvisor(this);
            pointcutResolved(newFieldInfos[i], binding, new FieldJoinpoint(field));
         }
      }
   }
   
   protected void initializeMethodChain()
   {
      long[] keys = advisedMethods.keys();
      for (int i = 0; i < keys.length; i++)
      {
         MethodInfo info = new MethodInfo();
         Method amethod = (Method) advisedMethods.get(keys[i]);
         info.setAdvisedMethod(amethod);
         Method umethod = (Method) unadvisedMethods.get(keys[i]);

         if (umethod == null) umethod = amethod;
         info.setUnadvisedMethod(umethod);
         info.setHash(keys[i]);
         info.setAdvisor(this);
         methodInfos.put(keys[i], info);
         try
         {
            Field infoField = clazz.getDeclaredField(MethodExecutionTransformer.getMethodInfoFieldName(amethod.getName(), keys[i]));
            infoField.setAccessible(true);
            infoField.set(null, new WeakReference<MethodInfo>(info));
         }
         catch (NoSuchFieldException e)
         {
            // ignore, method may not be advised.
         }
         catch (IllegalAccessException e)
         {
            throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
         }
      }
   }

   protected void initializeFieldReadChain()
   {
      this.fieldReadInfos = new FieldInfo[advisedFields.length];
      for (int i = 0; i < advisedFields.length; i++)
      {
         FieldInfo info = new FieldInfo();
         info.setAdvisedField(advisedFields[i]);
         info.setAdvisor(this);
         info.setIndex(i);

         try
         {
            info.setWrapper(clazz.getDeclaredMethod(
                  FieldAccessTransformer.fieldRead(advisedFields[i].getName()),
                  new Class[] {Object.class}));
         }
         catch (NoSuchMethodException e)
         {
            //Just means not advised
         }

         fieldReadInfos[i] = info;

         try
         {
            Field infoField = clazz.getDeclaredField(FieldAccessTransformer.getFieldReadInfoFieldName(advisedFields[i].getName()));
            infoField.setAccessible(true);
            infoField.set(null, new WeakReference<FieldInfo>(info));
         }
         catch (NoSuchFieldException e)
         {
            // ignore, method may not be advised.
         }
         catch (IllegalAccessException e)
         {
            throw new RuntimeException(e);
         }
      }
   }

   protected void initializeFieldWriteChain()
   {
      this.fieldWriteInfos = new FieldInfo[advisedFields.length];
      for (int i = 0; i < advisedFields.length; i++)
      {
         FieldInfo info = new FieldInfo();
         info.setAdvisedField(advisedFields[i]);
         info.setAdvisor(this);
         info.setIndex(i);

         try
         {
            info.setWrapper(clazz.getDeclaredMethod(
                  FieldAccessTransformer.fieldWrite(advisedFields[i].getName()),
                  new Class[] {Object.class, advisedFields[i].getType()}));
         }
         catch (NoSuchMethodException e)
         {
            //Just means not advised
         }

         fieldWriteInfos[i] = info;

         try
         {
            Field infoField = clazz.getDeclaredField(FieldAccessTransformer.getFieldWriteInfoFieldName(advisedFields[i].getName()));
            infoField.setAccessible(true);
            infoField.set(null, new WeakReference<FieldInfo>(info));
         }
         catch (NoSuchFieldException e)
         {
            // ignore, method may not be advised.
         }
         catch (IllegalAccessException e)
         {
            throw new RuntimeException(e);
         }

      }
   }

//   protected void finalizeChain(JoinPointInfo[] infos)
//   {
//      for (int i = 0; i < infos.length; i++)
//      {
//         JoinPointInfo info = infos[i];
//         ArrayList<Interceptor> list = info.getInterceptorChain();
//         Interceptor[] interceptors = null;
//         if (list.size() > 0)
//         {
//          interceptors = applyPrecedence(list.toArray(new Interceptor[list.size()]));
//         }
//         info.setInterceptors(interceptors);
//      }
//   }
   
   protected void createInterceptorChains() throws Exception
   {
      if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug("Creating chains for " + clazz + " " + ((clazz != null) ? clazz.getClassLoader() : null ));
      // TODO flavia remove this
      // this if is here because the subclass GeneratedClassAdvisor shouldn't be calling
      // this method anymore: the infos are already created during this class initalization (initalise method)
      // we call this method because we have the method chains
      if (fieldReadInfos == null)
      {
         initializeFieldReadChain();
         initializeFieldWriteChain();
         initializeMethodChain();
      }
      
      initializeConstructorChain();
      initializeConstructionChain();

      synchronized (manager.getBindings())
      {
         Iterator it = manager.getBindings().values().iterator();
         while (it.hasNext())
         {
            AdviceBinding binding = (AdviceBinding) it.next();
            if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug("iterate binding " + binding.getName() + " " + binding.getPointcut().getExpr());
            resolveMethodPointcut(binding);
            resolveFieldPointcut(fieldReadInfos, binding, false);
            resolveFieldPointcut(fieldWriteInfos, binding, true);
            resolveConstructorPointcut(binding);
            resolveConstructionPointcut(binding);
         }
      }

      finalizeChains();
      populateInterceptorsFromInfos();

      doesHaveAspects = adviceBindings.size() > 0;
      // Notify observer about this change
      if (this.interceptorChainObserver != null)
      {
         this.interceptorChainObserver.interceptorChainsUpdated(fieldReadInterceptors, fieldWriteInterceptors,
               constructorInterceptors, methodInterceptors);
      }
   }
   
   protected void updateInterceptorChains() throws Exception
   {
      if (AspectManager.verbose && logger.isDebugEnabled())
      {
         logger.debug("Updating chains for " + clazz + " " + ((clazz != null) ? clazz.getClassLoader() : null ));  
      }
      logger.debug("Updating chains for " + clazz + " " + ((clazz != null) ? clazz.getClassLoader() : null ));
      resetChain(methodInfos);
      resetChain(fieldReadInfos);
      resetChain(fieldWriteInfos);
      resetChain(constructorInfos);
      resetChain(constructionInfos);

      synchronized (manager.getBindings())
      {
         Iterator it = manager.getBindings().values().iterator();
         while (it.hasNext())
         {
            AdviceBinding binding = (AdviceBinding) it.next();
            if (AspectManager.verbose && logger.isDebugEnabled()) logger.debug("iterate binding " + binding.getName() + " " + binding.getPointcut().getExpr());
            resolveMethodPointcut(binding);
            resolveFieldPointcut(fieldReadInfos, binding, false);
            resolveFieldPointcut(fieldWriteInfos, binding, true);
            resolveConstructorPointcut(binding);
            resolveConstructionPointcut(binding);
         }
      }

      finalizeChains();
      populateInterceptorsFromInfos();

      doesHaveAspects = adviceBindings.size() > 0;
      // Notify observer about this change
      if (this.interceptorChainObserver != null)
      {
         this.interceptorChainObserver.interceptorChainsUpdated(fieldReadInterceptors, fieldWriteInterceptors,
               constructorInterceptors, methodInterceptors);
      }
   }

   protected void finalizeChains()
   {
      finalizeMethodChain();
      finalizeChain(fieldReadInfos);
      finalizeChain(fieldWriteInfos);
      finalizeChain(constructorInfos);
      finalizeChain(constructionInfos);
   }
   
   private MethodByConInfo initializeConstructorCallerInterceptorsMap(Class callingClass, int callingIndex, String calledClass, long calledMethodHash, Method calledMethod) throws Exception
   {
      HashMap calledClassesMap = methodCalledByConInterceptors[callingIndex];
      if (calledClassesMap == null)
      {
         calledClassesMap = new HashMap();
         methodCalledByConInterceptors[callingIndex] = calledClassesMap;
      }
      TLongObjectHashMap calledMethodsMap = (TLongObjectHashMap) calledClassesMap.get(calledClass);
      if (calledMethodsMap == null)
      {
         calledMethodsMap = new TLongObjectHashMap();
         calledClassesMap.put(calledClass, calledMethodsMap);
      }

      //The standard MethodCalledByXXXXInvocation class calls by reflection and needs access
      calledMethod.setAccessible(true);
      // FIXME ClassLoader - how do we know the class is visible from the context classloader?
      Class calledClazz = SecurityActions.getContextClassLoader().loadClass(calledClass);
      MethodByConInfo info = new MethodByConInfo(this, calledClazz, callingClass, callingIndex, calledMethod, calledMethodHash, null);
      calledMethodsMap.put(calledMethodHash, info);
      return info;
   }

   private ConByConInfo initializeConCalledByConInterceptorsMap(Class callingClass, int callingIndex, String calledClass, long calledConHash, Constructor calledCon) throws Exception
   {
      HashMap calledClassesMap = conCalledByConInterceptors[callingIndex];
      if (calledClassesMap == null)
      {
         calledClassesMap = new HashMap();
         conCalledByConInterceptors[callingIndex] = calledClassesMap;
      }
      TLongObjectHashMap calledMethodsMap = (TLongObjectHashMap) calledClassesMap.get(calledClass);
      if (calledMethodsMap == null)
      {
         calledMethodsMap = new TLongObjectHashMap();
         calledClassesMap.put(calledClass, calledMethodsMap);
      }
      ConByConInfo info = createConByConInfo(callingClass, callingIndex, calledClass, calledCon, calledConHash);
      calledMethodsMap.put(calledConHash, info);
      return info;
   }


   private ConByConInfo createConByConInfo(Class callingClass, int callingIndex, String calledClass, Constructor calledCon, long calledConHash) throws Exception
   {
      //The standard ConstructorCalledByXXXXInvocation class calls by reflection and needs access
      calledCon.setAccessible(true);
      // FIXME ClassLoader - how do we know the class is visible from the context classloader?
      Class calledClazz = SecurityActions.getContextClassLoader().loadClass(calledClass);

      try
      {
         int index = calledClass.lastIndexOf('.');
         String baseClassName = calledClass.substring(index + 1);
         Method wrapper = calledCon.getDeclaringClass().getDeclaredMethod(ConstructorExecutionTransformer.constructorFactory(baseClassName), calledCon.getParameterTypes());
         return new ConByConInfo(this, calledClazz, callingClass, callingIndex, calledCon, calledConHash, wrapper, null);
      }
      catch (NoSuchMethodException e)
      {
         return new ConByConInfo(this, calledClazz, callingClass, callingIndex, calledCon, calledConHash, null, null);
      }
   }

   protected void rebuildCallerInterceptors() throws Exception
   {
      if (methodByMethodData != null)
      {
         getMethodByMethodData().rebuildCallerInterceptors();
      }
      for (int i = 0; i < methodCalledByConInterceptors.length; i++)
      {
         HashMap calledClasses = methodCalledByConInterceptors[i];
         if (calledClasses == null) continue;
         Iterator classesIterator = calledClasses.entrySet().iterator();
         while (classesIterator.hasNext())
         {
            Map.Entry entry = (Map.Entry) classesIterator.next();
            String cname = (String) entry.getKey();
            TLongObjectHashMap calledMethods = (TLongObjectHashMap) entry.getValue();
            long[] calledKeys = calledMethods.keys();
            for (int j = 0; j < calledKeys.length; j++)
            {
               long calledHash = calledKeys[j];
               ArrayList bindings = getConstructorCallerBindings(i, cname, calledHash);
               bindConstructorCallerInterceptorChain(bindings, i, cname, calledHash);
            }
         }
      }
      if (conByMethodData != null)
      {
         getConByMethodData().rebuildCallerInterceptors();
      }
      for (int i = 0; i < conCalledByConInterceptors.length; i++)
      {
         HashMap calledClasses = conCalledByConInterceptors[i];
         if (calledClasses == null) continue;
         Iterator classesIterator = calledClasses.entrySet().iterator();
         while (classesIterator.hasNext())
         {
            Map.Entry entry = (Map.Entry) classesIterator.next();
            String cname = (String) entry.getKey();
            TLongObjectHashMap calledMethods = (TLongObjectHashMap) entry.getValue();
            long[] calledKeys = calledMethods.keys();
            for (int j = 0; j < calledKeys.length; j++)
            {
               long calledHash = calledKeys[j];
               ArrayList bindings = getConCalledByConBindings(i, cname, calledHash);
               bindConCalledByConInterceptorChain(bindings, i, cname, calledHash);
            }
         }
      }
   }

   private ArrayList getConstructorCallerBindings(int callingIndex, String cname, long calledHash)
   {
      HashMap calledClasses = methodCalledByConBindings[callingIndex];
      TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(cname);
      return (ArrayList) calledMethods.get(calledHash);
   }

   private ArrayList getConCalledByConBindings(int callingIndex, String cname, long calledHash)
   {
      HashMap calledClasses = conCalledByConBindings[callingIndex];
      TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(cname);
      return (ArrayList) calledMethods.get(calledHash);
   }

   protected void finalizeMethodCalledByMethodInterceptorChain(MethodByMethodInfo info)
   {
      ArrayList list = info.getInterceptorChain();
      Interceptor[] interceptors = null;
      if (list.size() > 0)
      {
         interceptors = (Interceptor[]) list.toArray(new Interceptor[list.size()]);
      }
      info.setInterceptors(interceptors);
   }

   protected void finalizeConCalledByMethodInterceptorChain(ConByMethodInfo info)
   {
      ArrayList list = info.getInterceptorChain();
      Interceptor[] interceptors = null;
      if (list.size() > 0)
      {
         interceptors = (Interceptor[]) list.toArray(new Interceptor[list.size()]);
      }
      info.setInterceptors(interceptors);
   }

   private void bindConCalledByConInterceptorChain(ArrayList bindings, int callingIndex, String cname, long calledHash)
   {
      ConByConInfo info = getConCalledByCon(callingIndex, cname, calledHash);
      info.clear();
      Iterator it = bindings.iterator();
      while (it.hasNext())
      {
         AdviceBinding binding = (AdviceBinding) it.next();
         pointcutResolved(info, binding, new ConstructorCalledByConstructorJoinpoint(info.getCallingConstructor(), info.getConstructor()));
      }
      finalizeConCalledByConInterceptorChain(info);
   }

   protected void finalizeConCalledByConInterceptorChain(ConByConInfo info)
   {
      ArrayList list = info.getInterceptorChain();
      Interceptor[] interceptors = null;
      if (list.size() > 0)
      {
         interceptors = (Interceptor[]) list.toArray(new Interceptor[list.size()]);
      }
      info.setInterceptors(interceptors);
   }

   private void bindConstructorCallerInterceptorChain(ArrayList bindings, int callingIndex, String cname, long calledHash)
   {
      MethodByConInfo info = getConstructorCallerMethodInfo(callingIndex, cname, calledHash);
      info.clear();
      Iterator it = bindings.iterator();
      while (it.hasNext())
      {
         AdviceBinding binding = (AdviceBinding) it.next();
         pointcutResolved(info, binding, new MethodCalledByConstructorJoinpoint(info.getCallingConstructor(), info.getMethod()));
      }
      finalizeMethodCalledByConInterceptorChain(info);
   }

   protected void finalizeMethodCalledByConInterceptorChain(MethodByConInfo info)
   {
      ArrayList list = info.getInterceptorChain();
      Interceptor[] interceptors = null;
      if (list.size() > 0)
      {
         interceptors = (Interceptor[]) list.toArray(new Interceptor[list.size()]);
      }
      info.setInterceptors(interceptors);
   }

   protected void rebuildInterceptors()
   {
      if (initialized)
      {
         if (System.getSecurityManager() == null)
         {
            RebuildInterceptorsAction.NON_PRIVILEGED.rebuildInterceptors(this);
         }
         else
         {
            RebuildInterceptorsAction.PRIVILEGED.rebuildInterceptors(this);
         }
      }
   }

   protected void doRebuildInterceptors()
   {
      try
      {
         adviceBindings.clear();
         if (this.constructionInfos == null)
         {
            createInterceptorChains();
         }
         else
         {
            updateInterceptorChains();
         }
         rebuildCallerInterceptors();
      }
      catch (Exception ex)
      {
         if (ex instanceof RuntimeException)
         {
            throw (RuntimeException) ex;
         }
         throw new RuntimeException(ex);
      }
   }
   protected void bindClassMetaData(ClassMetaDataBinding data)
   {
      try
      {
         ClassMetaDataLoader loader = data.getLoader();
         Object[] objs = advisedMethods.getValues();
         Method[] methods = new Method[objs.length];
         Field[] fields = advisedFields;
         // set to empty array because advisedFields may not have been initialized yet
         if (fields == null) fields = new Field[0];
         Constructor[] cons = constructors;
         // set to empty array because constructors may not have been initialized yet
         if (cons == null) cons = new Constructor[0];
         for (int i = 0; i < objs.length; i++) methods[i] = (Method) objs[i];
         loader.bind(this, data, methods, fields, cons);
      }
      catch (Exception ex)
      {
         // REVISIT:  Need to know how errors affects deployment
         ex.printStackTrace();
      }
   }

   protected void rebindClassMetaData()
   {
      defaultMetaData.clear();
      methodMetaData.clear();
      fieldMetaData.clear();
      constructorMetaData.clear();
      classMetaData.clear();

      for (int i = 0; i < classMetaDataBindings.size(); i++)
      {
         ClassMetaDataBinding data = (ClassMetaDataBinding) classMetaDataBindings.get(i);
         bindClassMetaData(data);
      }

      deployAnnotationOverrides();
   }


   public synchronized void addClassMetaData(ClassMetaDataBinding data)
   {
      initClassMetaDataBindingsList();
      classMetaDataBindings.add(data);
      if (this.clazz == null) return;  // don't bind till later.

      bindClassMetaData(data);
      // Recalculate interceptorPointcuts because of MetaDataInterceptorPointcuts
      adviceBindings.clear();
      doesHaveAspects = false;
      rebuildInterceptors();
   }

   public synchronized void removeClassMetaData(ClassMetaDataBinding data)
   {
      if (classMetaDataBindings.remove(data))
      {
         if (this.clazz == null) return; // not bound yet
         rebindClassMetaData();
         // Recalculate interceptorPointcuts because of MetaDataInterceptorPointcuts
         adviceBindings.clear();
         doesHaveAspects = false;
         rebuildInterceptors();
      }


   }

   private void initializeEmptyConstructorCallerChain(int callingIndex, String calledClass, long calledMethodHash) throws Exception
   {
      HashMap callingCon = methodCalledByConBindings[callingIndex];
      if (callingCon == null)
      {
         callingCon = new HashMap();
         methodCalledByConBindings[callingIndex] = callingCon;
      }
      TLongObjectHashMap classMap = (TLongObjectHashMap) callingCon.get(calledClass);
      if (classMap == null)
      {
         classMap = new TLongObjectHashMap();
         callingCon.put(calledClass, classMap);
      }
      ArrayList bindings = (ArrayList) classMap.get(calledMethodHash);
      if (bindings == null)
      {
         bindings = new ArrayList();
         classMap.put(calledMethodHash, bindings);
      }
   }

   private void initializeConCalledByConEmptyChain(int callingIndex, String calledClass, long calledConHash) throws Exception
   {
      HashMap callingCon = conCalledByConBindings[callingIndex];
      if (callingCon == null)
      {
         callingCon = new HashMap();
         conCalledByConBindings[callingIndex] = callingCon;
      }
      TLongObjectHashMap classMap = (TLongObjectHashMap) callingCon.get(calledClass);
      if (classMap == null)
      {
         classMap = new TLongObjectHashMap();
         callingCon.put(calledClass, classMap);
      }
      ArrayList bindings = (ArrayList) classMap.get(calledConHash);
      if (bindings == null)
      {
         bindings = new ArrayList();
         classMap.put(calledConHash, bindings);
      }
   }

   public void addConstructorCallerPointcut(int callingIndex, String calledClass, long calledMethodHash, AdviceBinding binding) throws Exception
   {
      if (AspectManager.verbose) System.err.println("constructor call matched binding " + binding.getPointcut().getExpr());
      adviceBindings.add(binding);
      binding.addAdvisor(this);
      HashMap callingCon = methodCalledByConBindings[callingIndex];
      if (callingCon == null)
      {
         callingCon = new HashMap();
         methodCalledByConBindings[callingIndex] = callingCon;
      }
      TLongObjectHashMap classMap = (TLongObjectHashMap) callingCon.get(calledClass);
      if (classMap == null)
      {
         classMap = new TLongObjectHashMap();
         callingCon.put(calledClass, classMap);
      }
      ArrayList bindings = (ArrayList) classMap.get(calledMethodHash);
      boolean createdBindings = false;
      if (bindings == null)
      {
         bindings = new ArrayList();
         classMap.put(calledMethodHash, bindings);
         createdBindings = true;
      }
      if (!bindings.contains(binding)) bindings.add(binding);

      // this is so that we can undeploy a caller
      ArrayList backrefs = (ArrayList) backrefMethodCalledByConstructorBindings.get(binding.getName());
      if (backrefs == null)
      {
         backrefs = new ArrayList();
         backrefMethodCalledByConstructorBindings.put(binding.getName(), backrefs);
         backrefs.add(bindings);
      }
      else if (createdBindings) backrefs.add(bindings);
   }

   private void addConstructorCalledByConPointcut(int callingIndex, String calledClass, long calledConHash, AdviceBinding binding) throws Exception
   {
      if (AspectManager.verbose) System.err.println("constructor call matched binding " + binding.getPointcut().getExpr());
      adviceBindings.add(binding);
      binding.addAdvisor(this);
      HashMap callingCon = conCalledByConBindings[callingIndex];
      if (callingCon == null)
      {
         callingCon = new HashMap();
         conCalledByConBindings[callingIndex] = callingCon;
      }
      TLongObjectHashMap classMap = (TLongObjectHashMap) callingCon.get(calledClass);
      if (classMap == null)
      {
         classMap = new TLongObjectHashMap();
         callingCon.put(calledClass, classMap);
      }
      ArrayList bindings = (ArrayList) classMap.get(calledConHash);
      boolean createdBindings = false;
      if (bindings == null)
      {
         bindings = new ArrayList();
         classMap.put(calledConHash, bindings);
         createdBindings = true;
      }
      if (!bindings.contains(binding)) bindings.add(binding);

      // this is so that we can undeploy a caller
      ArrayList backrefs = (ArrayList) backrefConCalledByConstructorBindings.get(binding.getName());
      if (backrefs == null)
      {
         backrefs = new ArrayList();
         backrefConCalledByConstructorBindings.put(binding.getName(), backrefs);
         backrefs.add(bindings);
      }
      else if (createdBindings) backrefs.add(bindings);
   }

   private void removeCallerPointcut(AdviceBinding binding)
   {
      getMethodByMethodData().removeCallerPointcut(binding);
   }

   /**
    * Generates internal, unadvised version of a method name.
    */
   public static String notAdvisedMethodName(String className,
                                             String methodName)
   {
      return className.replace('.', '$') + "$" + methodName +
      NOT_TRANSFORMABLE_SUFFIX;
   }

   /**
    * Is this the name of a private, unadvised thing?
    */
   public static boolean isWithoutAdvisement(String name)
   {
      return name.endsWith(NOT_TRANSFORMABLE_SUFFIX);
   }

   /**
    * Is the method advisable?
    */
   public static boolean isAdvisable(Method method)
   {
      // note: this should match the implementation in the instrumentor.
      int modifiers = method.getModifiers();
      return (
      !isWithoutAdvisement(method.getName()) &&
      !Modifier.isAbstract(modifiers) &&
      !Modifier.isNative(modifiers) &&
      !(method.getName().equals("_getAdvisor") &&
            method.getParameterTypes().length == 0 &&
            method.getReturnType().equals(Advisor.class)) &&
      !(method.getName().equals("_getClassAdvisor") &&
            method.getParameterTypes().length == 0 &&
            method.getReturnType().equals(Advisor.class)) &&
      !(method.getName().equals("_getInstanceAdvisor") &&
            method.getParameterTypes().length == 0 &&
            method.getReturnType().equals(InstanceAdvisor.class)) &&
      !(method.getName().equals("_setInstanceAdvisor") &&
            method.getParameterTypes().length == 1 &&
            method.getParameterTypes()[0].equals(InstanceAdvisor.class)));
   }

   private void populateFieldTable(ArrayList fields, Class superclass)
   throws Exception
   {
      if (superclass == null) return;
      if (superclass.equals(Object.class)) return;

      populateFieldTable(fields, superclass.getSuperclass());

      // if (!isAdvised(superclass)) return;

      ArrayList temp = new ArrayList();
      Field[] declaredFields = superclass.getDeclaredFields();
      for (int i = 0; i < declaredFields.length; i++)
      {
         if (Advisable.isAdvisable(declaredFields[i]))
         {
            // Need to do this because notadvisable fields maybe private or protected
            declaredFields[i].setAccessible(true);
            temp.add(declaredFields[i]);
         }
      }
      Collections.sort(temp, FieldComparator.INSTANCE);
      fields.addAll(temp);
   }

   /**
    * Gets advised methods.
    */
   protected void createFieldTable() throws Exception
   {
      ArrayList fields = new ArrayList();

      populateFieldTable(fields, clazz);

      advisedFields = (Field[]) fields.toArray(new Field[fields.size()]);

   }

   protected void addDeclaredMethods(Class superclass) throws Exception
   {
      Method[] declaredMethods = superclass.getDeclaredMethods();
      for (int i = 0; i < declaredMethods.length; i++)
      {
         if (ClassAdvisor.isAdvisable(declaredMethods[i]))
         {
            long hash = MethodHashing.methodHash(declaredMethods[i]);
            advisedMethods.put(hash, declaredMethods[i]);
            try
            {
               Method m = declaredMethods[i];
               Method un = superclass.getDeclaredMethod(ClassAdvisor.notAdvisedMethodName(superclass.getName(),
               m.getName()),
               m.getParameterTypes());
               un.setAccessible(true);
               unadvisedMethods.put(hash, un);
            }
            catch (NoSuchMethodException ignored)
            {
            }
         }
      }
   }

   /**
    * Create a HashMap of method hash and Method
    * Superclasses get added first so subclasses will override with
    * correct overriden method
    */
   private void populateMethodTables(Class superclass)
   throws Exception
   {
      if (superclass == null) return;
      if (superclass.equals(Object.class)) return;

      populateMethodTables(superclass.getSuperclass());

      //The advisor for the superclass may be a container
      Advisor superAdvisor = manager.getAnyAdvisorIfAdvised(superclass);
      if (superAdvisor != null && superAdvisor instanceof ClassAdvisor)
      {
         TLongObjectHashMap superHash = ((ClassAdvisor)superAdvisor).getUnadvisedMethods();
         long[] keys = superHash.keys();
         for (int i = 0; i < keys.length; i++)
         {
            unadvisedMethods.put(keys[i], superHash.get(keys[i]));
         }
      }
      addDeclaredMethods(superclass);
   }

   protected void createMethodTables()
   throws Exception
   {
      initAdvisedMethodsMap();
      populateMethodTables(clazz.getSuperclass());
      addDeclaredMethods(clazz);
   }

   protected void createConstructorTables() throws Exception
   {
      constructors = clazz.getDeclaredConstructors();
      methodCalledByConBindings = new HashMap[constructors.length];
      methodCalledByConInterceptors = new HashMap[constructors.length];

      conCalledByConBindings = new HashMap[constructors.length];
      conCalledByConInterceptors = new HashMap[constructors.length];
      for (int i = 0; i < constructors.length; i++)
      {
         constructors[i].setAccessible(true);
      }
      Arrays.sort(constructors, ConstructorComparator.INSTANCE);
   }

   public MethodByMethodInfo resolveCallerMethodInfo(long callingMethodHash, String calledClass, long calledMethodHash)
   {
      return getMethodByMethodData().resolveCallerMethodInfo(callingMethodHash, calledClass, calledMethodHash);
   }

   public WeakReference resolveCallerMethodInfoAsWeakReference(long callingMethodHash, String calledClass, long calledMethodHash)
   {
      //Javassist doesn't like this in a field initialiser hence this method
      return new WeakReference(resolveCallerMethodInfo(callingMethodHash, calledClass, calledMethodHash));
   }

   public ConByMethodInfo resolveCallerConstructorInfo(long callingMethodHash, String calledClass, long calledConHash)
   {
      return getConByMethodData().resolveCallerConstructorInfo(callingMethodHash, calledClass, calledConHash);
   }

   public WeakReference resolveCallerConstructorInfoAsWeakReference(long callingMethodHash, String calledClass, long calledConHash)
   {
      //Javassist doesn't like this in a field initialiser hence this method
      return new WeakReference(resolveCallerConstructorInfo(callingMethodHash, calledClass, calledConHash));
   }

   @Deprecated
   public MethodByConInfo resolveConstructorCallerMethodInfo(int callingIndex, String calledClass, long calledMethodHash)
   {
      return resolveConstructorCallerMethodInfo(this.getClazz(), callingIndex, calledClass, calledMethodHash);
   }
   
   public MethodByConInfo resolveConstructorCallerMethodInfo(Class callingClass, int callingIndex, String calledClass, long calledMethodHash)
   {
      if (System.getSecurityManager() == null)
      {
         return ResolveConstructorCallerMethodInfoAction.NON_PRIVILEGED.resolveInfo(this, callingClass, callingIndex, calledClass, calledMethodHash);
      }
      else
      {
         return ResolveConstructorCallerMethodInfoAction.PRIVILEGED.resolveInfo(this, callingClass, callingIndex, calledClass, calledMethodHash);
      }
   }

   private MethodByConInfo doResolveConstructorCallerMethodInfo(Class callingClass, int callingIndex, String calledClass, long calledMethodHash)
   {
      try
      {
         Constructor callingConstructor = constructors[callingIndex];
         if (callingConstructor == null) throw new RuntimeException("Unable to figure out calling method of a caller pointcut");
         // FIXME ClassLoader - how do we know the class is visible from the context classloader?
         Class called = SecurityActions.getContextClassLoader().loadClass(calledClass);
         Method calledMethod = MethodHashing.findMethodByHash(called, calledMethodHash);
         if (calledMethod == null) throw new RuntimeException("Unable to figure out calledmethod of a caller pointcut");

         boolean matched = false;

         synchronized (manager.getBindings())
         {
            Iterator it = manager.getBindings().values().iterator();
            while (it.hasNext())
            {
               AdviceBinding binding = (AdviceBinding) it.next();
               if (binding.getPointcut().matchesCall(this, callingConstructor, called, calledMethod))
               {
                  addConstructorCallerPointcut(callingIndex, calledClass, calledMethodHash, binding);
                  matched = true;
               }
            }
         }
         if (!matched) initializeEmptyConstructorCallerChain(callingIndex, calledClass, calledMethodHash);
         MethodByConInfo info = initializeConstructorCallerInterceptorsMap(callingClass, callingIndex, calledClass, calledMethodHash, calledMethod);
         ArrayList bindings = getConstructorCallerBindings(callingIndex, calledClass, calledMethodHash);
         bindConstructorCallerInterceptorChain(bindings, callingIndex, calledClass, calledMethodHash);
         return info;
      }
      catch (Exception x)
      {
         throw new RuntimeException(x);
      }
   }

   @Deprecated
   public WeakReference resolveConstructorCallerMethodInfoAsWeakReference(int callingIndex, String calledClass, long calledMethodHash)
   {
      //Javassist doesn't like this in a field initialiser hence this method
      return new WeakReference(resolveConstructorCallerMethodInfo(callingIndex, calledClass, calledMethodHash));
   }

   public WeakReference resolveConstructorCallerMethodInfoAsWeakReference(Class callingClass, int callingIndex, String calledClass, long calledMethodHash)
   {
      //Javassist doesn't like this in a field initialiser hence this method
      return new WeakReference(resolveConstructorCallerMethodInfo(callingClass, callingIndex, calledClass, calledMethodHash));
   }

   public ConByConInfo resolveConstructorCallerConstructorInfo(int callingIndex, String calledClass, long calledConHash)
   {
      return resolveConstructorCallerConstructorInfo(this.getClazz(), callingIndex, calledClass, calledConHash);
   }
   
   public ConByConInfo resolveConstructorCallerConstructorInfo(Class callingClass, int callingIndex, String calledClass, long calledConHash)
   {
      if (System.getSecurityManager() == null)
      {
         return ResolveConstructorCallerConstructorInfoAction.NON_PRIVILEGED.resolveInfo(this, callingClass, callingIndex, calledClass, calledConHash);
      }
      else
      {
         return ResolveConstructorCallerConstructorInfoAction.PRIVILEGED.resolveInfo(this, callingClass, callingIndex, calledClass, calledConHash);
      }
   }

   private ConByConInfo doResolveConstructorCallerConstructorInfo(Class callingClass, int callingIndex, String calledClass, long calledConHash)
   {
      try
      {
         Constructor callingConstructor = constructors[callingIndex];
         if (callingConstructor == null) throw new RuntimeException("Unable to figure out calling method of a caller pointcut");
         // FIXME ClassLoader - how do we know the class is visible from the context classloader?
         Class called = SecurityActions.getContextClassLoader().loadClass(calledClass);
         Constructor calledCon = MethodHashing.findConstructorByHash(called, calledConHash);
         if (calledCon == null) throw new RuntimeException("Unable to figure out calledcon of a caller pointcut");

         boolean matched = false;
         synchronized (manager.getBindings())
         {
            Iterator it = manager.getBindings().values().iterator();
            while (it.hasNext())
            {
               AdviceBinding binding = (AdviceBinding) it.next();
               if (binding.getPointcut().matchesCall(this, callingConstructor, called, calledCon))
               {
                  addConstructorCalledByConPointcut(callingIndex, calledClass, calledConHash, binding);
                  matched = true;
               }
            }
         }
         if (!matched) initializeConCalledByConEmptyChain(callingIndex, calledClass, calledConHash);
         ConByConInfo info = initializeConCalledByConInterceptorsMap(callingClass, callingIndex, calledClass, calledConHash, calledCon);
         ArrayList bindings = getConCalledByConBindings(callingIndex, calledClass, calledConHash);
         bindConCalledByConInterceptorChain(bindings, callingIndex, calledClass, calledConHash);
         return info;
      }
      catch (Exception x)
      {
         throw new RuntimeException(x);
      }
   }

   @Deprecated
   public WeakReference resolveConstructorCallerConstructorInfoAsWeakReference(int callingIndex, String calledClass, long calledConHash)
   {
      //Javassist doesn't like this in a field initialiser hence this method
      return new WeakReference(resolveConstructorCallerConstructorInfo(callingIndex, calledClass, calledConHash));
   }
   
   public WeakReference resolveConstructorCallerConstructorInfoAsWeakReference(Class callingClass, int callingIndex, String calledClass, long calledConHash)
   {
      //Javassist doesn't like this in a field initialiser hence this method
      return new WeakReference(resolveConstructorCallerConstructorInfo(callingClass, callingIndex, calledClass, calledConHash));
   }
   
   /////////////////////////
   // Invoking

   /**
    * Invokes target object without applying interceptors.
    */
   public Object invokeWithoutAdvisement(Object target, long methodHash,
                                         Object[] arguments) throws Throwable
   {
      try
      {
         Method method = (Method) unadvisedMethods.get(methodHash);
         return method.invoke(target, arguments);
      }
      catch (InvocationTargetException e)
      {
         throw e.getTargetException();
      }
   }

   public Object invokeNewWithoutAdvisement(Object[] arguments, Constructor constructor) throws Throwable
   {
      try
      {
         return constructor.newInstance(arguments);
      }
      catch (InstantiationException in)
      {
         throw new RuntimeException("failed to call constructor", in);
      }
      catch (IllegalAccessException ill)
      {
         throw new RuntimeException("illegal access", ill);
      }
      catch (InvocationTargetException ite)
      {
         throw ite.getCause();
      }
   }


   public Object invokeMethod(long methodHash, Object[] arguments) throws Throwable
   {
      return invokeMethod(null, methodHash, arguments);
   }

   public Object invokeMethod(Object target, long methodHash, Object[] arguments) throws Throwable
   {
      InstanceAdvisor advisor = null;
      if (target != null)
      {
         InstanceAdvised advised = (InstanceAdvised) target;
         advisor = advised._getInstanceAdvisor();
      }
      MethodInfo info = methodInfos.getMethodInfo(methodHash);
      return invokeMethod(advisor, target, methodHash, arguments, info);
   }


   public Object invokeMethod(InstanceAdvisor instanceAdvisor, Object target, long methodHash, Object[] arguments)
   throws Throwable
   {
      MethodInfo info = (MethodInfo) methodInfos.getMethodInfo(methodHash);
      if (info == null && logger.isDebugEnabled())
      {
         logger.debug("info is null for hash: " + methodHash + " of " + clazz.getName());
      }
      return invokeMethod(instanceAdvisor, target, methodHash, arguments, info);
   }

   public Object invokeMethod(InstanceAdvisor instanceAdvisor, Object target, long methodHash, Object[] arguments, MethodInfo info)
   throws Throwable
   {
      Interceptor[] aspects = info.getInterceptors();
      if (instanceAdvisor != null && (instanceAdvisor.hasInterceptors()))
      {
         aspects = instanceAdvisor.getInterceptors(aspects);
      }
      MethodInvocation invocation = new MethodInvocation(info, aspects);

      invocation.setArguments(arguments);
      invocation.setTargetObject(target);
      return invocation.invokeNext();
   }

   /**
    *@deprecated
    */
   public Object invokeCaller(long callingMethodHash, Object target, Object[] args, CallerMethodInfo info, Object callingObject)
   throws Throwable
   {
      return invokeCaller((MethodByMethodInfo)info, callingObject, target, args);
   }

   public Object invokeCaller(MethodByMethodInfo info, Object callingObject, Object target, Object[] args) throws Throwable
   {
      MethodCalledByMethodInvocation invocation = new MethodCalledByMethodInvocation(info, callingObject, target, args, info.getInterceptors());
      invocation.setTargetObject(target);
      return invocation.invokeNext();
   }

   /**
    *@deprecated
    */
   public Object invokeConCalledByMethod(long callingMethodHash, Object[] args, CallerConstructorInfo info, Object callingObject)
   throws Throwable
   {
      return invokeConCalledByMethod((ConByMethodInfo)info, callingObject, args);
   }

   public Object invokeConCalledByMethod(ConByMethodInfo info, Object callingObject, Object[] args)
   throws Throwable
   {
      ConstructorCalledByMethodInvocation invocation = new ConstructorCalledByMethodInvocation(info, callingObject, args, info.getInterceptors());
      return invocation.invokeNext();
   }

   /**
    *@deprecated
    */
   public Object invokeConstructorCaller(int callingIndex, Object target, Object[] args, CallerMethodInfo info)
   throws Throwable
   {
      return invokeConstructorCaller((MethodByConInfo)info, null, target, args);
   }

   /**
    *@deprecated
    */
   public Object invokeConstructorCaller(int callingIndex, Object callingObject, Object target, Object[] args, CallerMethodInfo info)
   throws Throwable
   {
      return invokeConstructorCaller((MethodByConInfo)info, callingObject, target, args);
   }

   /**
    * @deprecated
    *
    * Prefer using the version with <code>callingObject</code> instead of this one,
    * since this object is available for call invocations made inside constructors.
    *
    * @see #invokeConstructorCaller(MethodByConInfo, Object, Object, Object[])
    */
   public Object invokeConstructorCaller(MethodByConInfo info, Object target, Object[] args)
   throws Throwable
   {
      return this.invokeConstructorCaller(info, null, target, args);
   }

   public Object invokeConstructorCaller(MethodByConInfo info, Object callingObject, Object target, Object[] args)
   throws Throwable
   {
      MethodCalledByConstructorInvocation invocation = new MethodCalledByConstructorInvocation(info, callingObject, target, args, info.getInterceptors());
      invocation.setTargetObject(target);
      return invocation.invokeNext();
   }

   /**
    *@deprecated
    */
   public Object invokeConCalledByCon(int callingIndex, Object[] args, CallerConstructorInfo info)
   throws Throwable
   {
      return invokeConCalledByCon((ConByConInfo)info, null, args);
   }

   /**
    *@deprecated
    */
   public Object invokeConCalledByCon(int callingIndex, Object callingObject, Object[] args, CallerConstructorInfo info)
   throws Throwable
   {
      return invokeConCalledByCon((ConByConInfo)info, callingObject, args);
   }

   /**
    * @deprecated
    *
    * Prefer using the version with <code>callingObject</code> instead of this one,
    * since this object is available for call invocations made inside constructors.
    *
    * @see #invokeConCalledByCon(ConByConInfo, Object, Object[])
    */
   public Object invokeConCalledByCon(ConByConInfo info, Object[] args)
   throws Throwable
   {
      return invokeConCalledByCon(info, null, args);
   }

   public Object invokeConCalledByCon(ConByConInfo info, Object callingObject, Object[] args)
   throws Throwable
   {
      ConstructorCalledByConstructorInvocation invocation = new ConstructorCalledByConstructorInvocation(info, callingObject, args, info.getInterceptors());
      return invocation.invokeNext();
   }

   private MethodByConInfo getConstructorCallerMethodInfo(int callingIndex, String calledClass, long calledMethodHash)
   {
      HashMap calledClasses = methodCalledByConInterceptors[callingIndex];
      TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(calledClass);
      MethodByConInfo info = (MethodByConInfo) calledMethods.get(calledMethodHash);
      return info;
   }

   private ConByConInfo getConCalledByCon(int callingIndex, String calledClass, long calledConHash)
   {
      HashMap calledClasses = conCalledByConInterceptors[callingIndex];
      TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(calledClass);
      ConByConInfo info = (ConByConInfo) calledMethods.get(calledConHash);
      return info;
   }


   public Object invokeNew(Object[] args, int idx) throws Throwable
   {
      Interceptor[] cInterceptors = constructorInfos[idx].getInterceptors();
      if (cInterceptors == null) cInterceptors = new Interceptor[0];
      ConstructorInvocation invocation = new ConstructorInvocation(cInterceptors);

      invocation.setAdvisor(this);
      invocation.setArguments(args);
      invocation.setConstructor(constructors[idx]);
      return invocation.invokeNext();
   }

   /**
    * Invokes interceptor chain.
    * This is the beginning
    */
   public Object invokeRead(Object target, int index)
   throws Throwable
   {
      Interceptor[] aspects = fieldReadInfos[index].getInterceptors();
      if (aspects == null) aspects = new Interceptor[0];
      FieldReadInvocation invocation;
      if (target != null)
      {
         InstanceAdvised advised = (InstanceAdvised) target;
         InstanceAdvisor advisor = advised._getInstanceAdvisor();
         if (advisor != null && advisor.hasInterceptors())
         {
            aspects = advisor.getInterceptors(aspects);
         }
      }
      invocation = new FieldReadInvocation(advisedFields[index], index, aspects);
      invocation.setAdvisor(this);
      invocation.setTargetObject(target);
      return invocation.invokeNext();
   }

   /**
    * Invokes interceptor chain.
    * This is the beginning
    */
   public Object invokeWrite(Object target, int index, Object value)
   throws Throwable
   {
      Interceptor[] aspects = fieldWriteInfos[index].getInterceptors();
      if (aspects == null) aspects = new Interceptor[0];
      FieldWriteInvocation invocation;
      if (target != null)
      {
         InstanceAdvised advised = (InstanceAdvised) target;
         InstanceAdvisor advisor = advised._getInstanceAdvisor();
         if (advisor != null && advisor.hasInterceptors())
         {
            aspects = advised._getInstanceAdvisor().getInterceptors(aspects);
         }
      }
      invocation = new FieldWriteInvocation(advisedFields[index], index, value, aspects);
      invocation.setAdvisor(this);
      invocation.setTargetObject(target);
      return invocation.invokeNext();
   }

   /**
    * Invokes interceptor chain.
    * This is the beginning
    */
   public Object invoke(Invocation invocation) throws Throwable
   {
      if (invocation instanceof FieldWriteInvocation)
      {
         FieldWriteInvocation fieldInvocation = (FieldWriteInvocation) invocation;
         Object target = fieldInvocation.getTargetObject();
         Object val = fieldInvocation.getValue();
         Field field = fieldInvocation.getField();
         field.set(target, val);
         return null;
      }
      else if (invocation instanceof FieldReadInvocation)
      {
         FieldReadInvocation fieldInvocation = (FieldReadInvocation) invocation;
         Object target = fieldInvocation.getTargetObject();
         Field field = fieldInvocation.getField();
         return field.get(target);
      }
      else if (invocation instanceof MethodInvocation)
      {
         MethodInvocation methodInvocation = (MethodInvocation) invocation;
         return invokeWithoutAdvisement(methodInvocation.getTargetObject(),
         methodInvocation.getMethodHash(),
         methodInvocation.getArguments());
      }
      else if (invocation instanceof ConstructorInvocation)
      {
         ConstructorInvocation cInvocation = (ConstructorInvocation) invocation;
         Object[] arguments = cInvocation.getArguments();
         Constructor constructor = cInvocation.getConstructor();
         return invokeNewWithoutAdvisement(arguments, constructor);
      }
      throw new IllegalStateException("Unknown Invocation type: " + invocation.getClass().getName());
   }
   
   @Override
   public void cleanup()
   {
      super.cleanup();
      if (methodByMethodData != null)
      {
         methodByMethodData.cleanup();
         methodByMethodData = null;
      }
      if (conByMethodData != null)
      {
         conByMethodData.cleanup();
         conByMethodData = null;
      }
   }
   
   // interceptor chain observer
   private InterceptorChainObserver interceptorChainObserver;

   /**
    * Returns the interceptor chain observer associated with this advisor.
    */
   protected InterceptorChainObserver getInterceptorChainObserver()
   {
      return this.interceptorChainObserver;
   }

   /**
    * Defines the interceptor chain observer associated with this advisor.
    * @param observer the interceptor chain observer.
    */
   protected void setInterceptorChainObserver(InterceptorChainObserver observer)
   {
      if (observer != null)
      {
         observer.initialInterceptorChains(this.clazz, fieldReadInterceptors, fieldWriteInterceptors,
               constructorInterceptors, methodInterceptors);
      }
      this.interceptorChainObserver = observer;
   }

   /** @deprecated We should just be using xxxxInfos */
   protected void populateInterceptorsFromInfos()
   {
      super.populateInterceptorsFromInfos();
      fieldReadInterceptors = new Interceptor[fieldReadInfos.length][];
      for (int i = 0 ; i < fieldReadInfos.length ; i++)
      {
         fieldReadInterceptors[i] = fieldReadInfos[i].getInterceptors();
      }
      fieldWriteInterceptors = new Interceptor[fieldWriteInfos.length][];
      for (int i = 0 ; i < fieldWriteInfos.length ; i++)
      {
         fieldWriteInterceptors[i] = fieldWriteInfos[i].getInterceptors();
      }
      constructionInterceptors = new Interceptor[constructionInfos.length][];
      for (int i = 0 ; i < constructionInfos.length ; i++)
      {
         constructionInterceptors[i] = constructionInfos[i].getInterceptors();
      }
   
   }

   protected MethodByMethodData getMethodByMethodData()
   {
      if (methodByMethodData == null)
      {
         lockWrite();
         try
         {
            if (methodByMethodData == null)
            {
               methodByMethodData = new MethodByMethodData();
            }
         }
         finally
         {
            unlockWrite();
         }
      }
      return methodByMethodData;
   }

   protected ConByMethodData getConByMethodData()
   {
      if (conByMethodData == null)
      {
         lockWrite();
         try
         {
            if (conByMethodData == null)
            {
               conByMethodData = new ConByMethodData();
            }
         }
         finally
         {
            unlockWrite();
         }
      }
      return conByMethodData;
   }

   interface ResolveCallerConstuctorInfoAction
   {
      ConByMethodInfo resolveInfo(ConByMethodData data, long callingMethodHash, String calledClass, long calledConHash);

      ResolveCallerConstuctorInfoAction PRIVILEGED = new ResolveCallerConstuctorInfoAction()
      {
         public ConByMethodInfo resolveInfo(final ConByMethodData data, final long callingMethodHash, final String calledClass, final long calledConHash)
         {
            try
            {
               return (ConByMethodInfo)AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     return data.doResolveCallerConstructorInfo(callingMethodHash, calledClass, calledConHash);
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if (ex instanceof RuntimeException)
               {
                  throw (RuntimeException)ex;
               }
               throw new RuntimeException(ex);
            }
         }
      };

      ResolveCallerConstuctorInfoAction NON_PRIVILEGED = new ResolveCallerConstuctorInfoAction()
      {
         public ConByMethodInfo resolveInfo(ConByMethodData data, long callingMethodHash, String calledClass, long calledConHash)
         {
            return data.doResolveCallerConstructorInfo(callingMethodHash, calledClass, calledConHash);
         }
      };
   }

   interface ResolveCallerMethodInfoAction
   {
      MethodByMethodInfo resolveInfo(MethodByMethodData data, long callingMethodHash, String calledClass, long calledMethodHash);

      ResolveCallerMethodInfoAction PRIVILEGED = new ResolveCallerMethodInfoAction()
      {
         public MethodByMethodInfo resolveInfo(final MethodByMethodData data, final long callingMethodHash, final String calledClass, final long calledMethodHash)
         {
            try
            {
               return (MethodByMethodInfo)AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     return data.doResolveCallerMethodInfo(callingMethodHash, calledClass, calledMethodHash);
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if (ex instanceof RuntimeException)
               {
                  throw (RuntimeException)ex;
               }
               throw new RuntimeException(ex);
            }
         }
      };

      ResolveCallerMethodInfoAction NON_PRIVILEGED = new ResolveCallerMethodInfoAction()
      {
         public MethodByMethodInfo resolveInfo(MethodByMethodData data, long callingMethodHash, String calledClass, long calledMethodHash)
         {
            return data.doResolveCallerMethodInfo(callingMethodHash, calledClass, calledMethodHash);
         }
      };
   }

   interface ResolveConstructorCallerMethodInfoAction
   {
      MethodByConInfo resolveInfo(ClassAdvisor advisor, Class callingClass, int callingIndex, String calledClass, long calledMethodHash);

      ResolveConstructorCallerMethodInfoAction PRIVILEGED = new ResolveConstructorCallerMethodInfoAction()
      {
         public MethodByConInfo resolveInfo(final ClassAdvisor advisor, final Class callingClass, final int callingIndex, final String calledClass, final long calledMethodHash)
         {
            try
            {
               return (MethodByConInfo)AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     return advisor.doResolveConstructorCallerMethodInfo(callingClass, callingIndex, calledClass, calledMethodHash);
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if (ex instanceof RuntimeException)
               {
                  throw (RuntimeException)ex;
               }
               throw new RuntimeException(ex);
            }
         }
      };

      ResolveConstructorCallerMethodInfoAction NON_PRIVILEGED = new ResolveConstructorCallerMethodInfoAction()
      {
         public MethodByConInfo resolveInfo(ClassAdvisor advisor, Class callingClass, int callingIndex, String calledClass, long calledMethodHash)
         {
            return advisor.doResolveConstructorCallerMethodInfo(callingClass, callingIndex, calledClass, calledMethodHash);
         }
      };
   }

   interface ResolveConstructorCallerConstructorInfoAction
   {
      ConByConInfo resolveInfo(ClassAdvisor advisor, Class callingClass, int callingIndex, String calledClass, long calledConHash);

      ResolveConstructorCallerConstructorInfoAction PRIVILEGED = new ResolveConstructorCallerConstructorInfoAction()
      {
         public ConByConInfo resolveInfo(final ClassAdvisor advisor, final Class callingClass, final int callingIndex, final String calledClass, final long calledConHash)
         {
            try
            {
               return (ConByConInfo)AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     return advisor.doResolveConstructorCallerConstructorInfo(callingClass, callingIndex, calledClass, calledConHash);
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if (ex instanceof RuntimeException)
               {
                  throw (RuntimeException)ex;
               }
               throw new RuntimeException(ex);
            }
         }
      };

      ResolveConstructorCallerConstructorInfoAction NON_PRIVILEGED = new ResolveConstructorCallerConstructorInfoAction()
      {
         public ConByConInfo resolveInfo(ClassAdvisor advisor, Class callingClass, int callingIndex, String calledClass, long calledConHash)
         {
            return advisor.doResolveConstructorCallerConstructorInfo(callingClass, callingIndex, calledClass, calledConHash);
         }
      };
   }
   interface RebuildInterceptorsAction
   {
      void rebuildInterceptors(ClassAdvisor advisor);

      RebuildInterceptorsAction PRIVILEGED = new RebuildInterceptorsAction()
      {
         public void rebuildInterceptors(final ClassAdvisor advisor)
         {
            try
            {
               AccessController.doPrivileged(new PrivilegedExceptionAction<Object>()
               {
                  public Object run()
                  {
                     advisor.doRebuildInterceptors();
                     return null;
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if (ex instanceof RuntimeException)
               {
                  throw (RuntimeException) ex;
               }
               throw new RuntimeException(ex);
            }
         }
      };

      RebuildInterceptorsAction NON_PRIVILEGED = new RebuildInterceptorsAction()
      {
         public void rebuildInterceptors(ClassAdvisor advisor)
         {
            advisor.doRebuildInterceptors();
         }
      };
   }
   
   private class MethodByMethodData
   {
      private volatile TLongObjectHashMap methodCalledByMethodBindings = UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP;
      private volatile HashMap backrefMethodCalledByMethodBindings = UnmodifiableEmptyCollections.EMPTY_HASHMAP;
      private volatile TLongObjectHashMap methodCalledByMethodInterceptors = UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP;

      public TLongObjectHashMap getMethodCalledByMethodInterceptors()
      {
         //No need to initialise map here if it is empty
         return methodCalledByMethodInterceptors;
      }

      public TLongObjectHashMap getMethodCalledByMethodBindings()
      {
         //No need to initialise map here if it is empty
         return methodCalledByMethodBindings;
      }

      public void rebuildCallerInterceptors() throws Exception
      {
         //No need to initialise map here if it is empty
         long[] callingKeys = methodCalledByMethodInterceptors.keys();
         for (int i = 0; i < callingKeys.length; i++)
         {
            long callingHash = callingKeys[i];
            HashMap calledClasses = (HashMap) methodCalledByMethodInterceptors.get(callingHash);
            Iterator classesIterator = calledClasses.entrySet().iterator();
            while (classesIterator.hasNext())
            {
               Map.Entry entry = (Map.Entry) classesIterator.next();
               String cname = (String) entry.getKey();
               TLongObjectHashMap calledMethods = (TLongObjectHashMap) entry.getValue();
               long[] calledKeys = calledMethods.keys();
               for (int j = 0; j < calledKeys.length; j++)
               {
                  long calledHash = calledKeys[j];
                  ArrayList bindings = getCallerBindings(callingHash, cname, calledHash);
                  Method calling = MethodHashing.findMethodByHash(clazz, callingHash);
                  bindCallerInterceptorChain(bindings, callingHash, cname, calledHash, calling);
               }
            }
         }
      }

      public void removeCallerPointcut(AdviceBinding binding)
      {
         //No need to initialise map here if it is empty
         ArrayList backrefs = (ArrayList) backrefMethodCalledByMethodBindings.get(binding.getName());
         if (backrefs == null) return;
         for (int i = 0; i < backrefs.size(); i++)
         {
            ArrayList ref = (ArrayList) backrefs.get(i);
            ref.remove(binding);
         }
      }

      public MethodByMethodInfo resolveCallerMethodInfo(long callingMethodHash, String calledClass, long calledMethodHash)
      {
         //The main entry point for adding data to the maps, let us initialise them here.
         if (methodCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP || 
               backrefMethodCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_HASHMAP || 
               methodCalledByMethodInterceptors == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP)
         {
            lockWrite();
            try
            {
               if (methodCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP)
               {
                  methodCalledByMethodBindings = new TLongObjectHashMap();
               }
               if (backrefMethodCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_HASHMAP)
               {
                  backrefMethodCalledByMethodBindings = new HashMap();
               }
               if (methodCalledByMethodInterceptors == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP)
               {
                  methodCalledByMethodInterceptors = new TLongObjectHashMap();
               }
            }
            finally
            {
               unlockWrite();
            }
         }
         
         if (System.getSecurityManager() == null)
         {
            return ResolveCallerMethodInfoAction.NON_PRIVILEGED.resolveInfo(this, callingMethodHash, calledClass, calledMethodHash);
         }
         else
         {
            return ResolveCallerMethodInfoAction.PRIVILEGED.resolveInfo(this, callingMethodHash, calledClass, calledMethodHash);
         }
      }

      private MethodByMethodInfo doResolveCallerMethodInfo(long callingMethodHash, String calledClass, long calledMethodHash)
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         try
         {
            Method callingMethod = MethodHashing.findMethodByHash(clazz, callingMethodHash);
            if (callingMethod == null) throw new RuntimeException("Unable to figure out calling method of a caller pointcut");
            // FIXME ClassLoader - how do we know the class is visible from the context classloader?
            Class called = SecurityActions.getContextClassLoader().loadClass(calledClass);
            Method calledMethod = MethodHashing.findMethodByHash(called, calledMethodHash);
            if (calledMethod == null) throw new RuntimeException("Unable to figure out calledmethod of a caller pointcut");

            Iterator it = manager.getBindings().values().iterator();
            boolean matched = false;
            while (it.hasNext())
            {
               AdviceBinding binding = (AdviceBinding) it.next();
               if (binding.getPointcut().matchesCall(ClassAdvisor.this, callingMethod, called, calledMethod))
               {
                  addMethodCalledByMethodPointcut(callingMethodHash, calledClass, calledMethodHash, binding);
                  matched = true;
               }
            }
            if (!matched) initializeEmptyCallerChain(callingMethodHash, calledClass, calledMethodHash);
            MethodByMethodInfo info = initializeCallerInterceptorsMap(callingMethodHash, calledClass, calledMethodHash, callingMethod, calledMethod);
            ArrayList bindings = getCallerBindings(callingMethodHash, calledClass, calledMethodHash);
            bindCallerInterceptorChain(bindings, callingMethodHash, calledClass, calledMethodHash, callingMethod);
            return info;
         }
         catch (Exception x)
         {
            throw new RuntimeException(x);
         }
      }

      private void addMethodCalledByMethodPointcut(long callingMethodHash, String calledClass, long calledMethodHash, AdviceBinding binding) throws Exception
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         if (AspectManager.verbose) System.err.println("method call matched binding " + binding.getPointcut().getExpr());
         adviceBindings.add(binding);
         binding.addAdvisor(ClassAdvisor.this);
         HashMap callingMethod = (HashMap) methodCalledByMethodBindings.get(callingMethodHash);
         if (callingMethod == null)
         {
            callingMethod = new HashMap();
            methodCalledByMethodBindings.put(callingMethodHash, callingMethod);
         }
         TLongObjectHashMap classMap = (TLongObjectHashMap) callingMethod.get(calledClass);
         if (classMap == null)
         {
            classMap = new TLongObjectHashMap();
            callingMethod.put(calledClass, classMap);
         }
         ArrayList bindings = (ArrayList) classMap.get(calledMethodHash);
         boolean createdBindings = false;
         if (bindings == null)
         {
            bindings = new ArrayList();
            classMap.put(calledMethodHash, bindings);
            createdBindings = true;
         }
         if (!bindings.contains(binding)) bindings.add(binding);
      
         // this is so that we can undeploy a caller
         ArrayList backrefs = (ArrayList) backrefMethodCalledByMethodBindings.get(binding.getName());
         if (backrefs == null)
         {
            backrefs = new ArrayList();
            backrefMethodCalledByMethodBindings.put(binding.getName(), backrefs);
            backrefs.add(bindings);
         }
         else if (createdBindings) backrefs.add(bindings);
      }

      private void initializeEmptyCallerChain(long callingMethodHash, String calledClass, long calledMethodHash) throws Exception
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         HashMap callingMethod = (HashMap) methodCalledByMethodBindings.get(callingMethodHash);
         if (callingMethod == null)
         {
            callingMethod = new HashMap();
            methodCalledByMethodBindings.put(callingMethodHash, callingMethod);
         }
         TLongObjectHashMap classMap = (TLongObjectHashMap) callingMethod.get(calledClass);
         if (classMap == null)
         {
            classMap = new TLongObjectHashMap();
            callingMethod.put(calledClass, classMap);
         }
         ArrayList bindings = (ArrayList) classMap.get(calledMethodHash);
         if (bindings == null)
         {
            bindings = new ArrayList();
            classMap.put(calledMethodHash, bindings);
         }
      }

      private MethodByMethodInfo initializeCallerInterceptorsMap(long callingMethodHash, String calledClass, long calledMethodHash, Method callingMethod, Method calledMethod) throws Exception
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         HashMap calledClassesMap = (HashMap) methodCalledByMethodInterceptors.get(callingMethodHash);
         if (calledClassesMap == null)
         {
            calledClassesMap = new HashMap();
            methodCalledByMethodInterceptors.put(callingMethodHash, calledClassesMap);
         }
         TLongObjectHashMap calledMethodsMap = (TLongObjectHashMap) calledClassesMap.get(calledClass);
         if (calledMethodsMap == null)
         {
            calledMethodsMap = new TLongObjectHashMap();
            calledClassesMap.put(calledClass, calledMethodsMap);
         }
         //The standard MethodCalledByXXXXInvocation class calls by reflection and needs access
         calledMethod.setAccessible(true);

         // FIXME ClassLoader - how do we know the class is visible from the context classloader?
         Class calledClazz = SecurityActions.getContextClassLoader().loadClass(calledClass);
         MethodByMethodInfo info = new MethodByMethodInfo(ClassAdvisor.this, calledClazz, calledMethod, callingMethod, callingMethodHash, calledMethodHash, null);
         calledMethodsMap.put(calledMethodHash, info);
         return info;
      }

      private ArrayList getCallerBindings(long callingHash, String cname, long calledHash)
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         HashMap calledClasses = (HashMap) methodCalledByMethodBindings.get(callingHash);
         TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(cname);
         return (ArrayList) calledMethods.get(calledHash);
      }

      private void bindCallerInterceptorChain(ArrayList bindings, long callingHash, String cname, long calledHash, Method calling)
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         MethodByMethodInfo info = getCallerMethodInfo(callingHash, cname, calledHash);
         info.clear();
         Iterator it = bindings.iterator();
         while (it.hasNext())
         {
            AdviceBinding binding = (AdviceBinding) it.next();
            pointcutResolved(info, binding, new MethodCalledByMethodJoinpoint(info.getCallingMethod(), info.getMethod()));
         }
         finalizeMethodCalledByMethodInterceptorChain(info);
      }

      private MethodByMethodInfo getCallerMethodInfo(long callingMethodHash, String calledClass, long calledMethodHash)
      {
         //Called via resolveCallerMethodInfo, maps are initialised
         HashMap calledClasses = (HashMap) methodCalledByMethodInterceptors.get(callingMethodHash);
         TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(calledClass);
         MethodByMethodInfo info = (MethodByMethodInfo) calledMethods.get(calledMethodHash);
         return info;
      }

      public void cleanup()
      {
         methodCalledByMethodBindings.clear();
         backrefMethodCalledByMethodBindings.clear();
         methodCalledByMethodInterceptors.clear();
      }
   }
   
   private class ConByMethodData
   {
      // constructor caller pointcut support for methods calling constructors only
      private volatile TLongObjectHashMap conCalledByMethodBindings = UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP;
      private volatile HashMap backrefConCalledByMethodBindings = UnmodifiableEmptyCollections.EMPTY_HASHMAP;
      private volatile TLongObjectHashMap conCalledByMethodInterceptors = UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP;
      
      public TLongObjectHashMap getConCalledByMethodInterceptors()
      {
         return conCalledByMethodInterceptors;
      }

      public ArrayList getConCalledByMethodBindings(long callingHash, String cname, long calledHash)
      {
         HashMap calledClasses = (HashMap) conCalledByMethodBindings.get(callingHash);
         TLongObjectHashMap calledCons = (TLongObjectHashMap) calledClasses.get(cname);
         return (ArrayList) calledCons.get(calledHash);
      }

      public void rebuildCallerInterceptors() throws Exception
      {
         long[] callingKeys = conCalledByMethodInterceptors.keys();
         for (int i = 0; i < callingKeys.length; i++)
         {
            long callingHash = callingKeys[i];
            HashMap calledClasses = (HashMap) conCalledByMethodInterceptors.get(callingHash);
            Iterator classesIterator = calledClasses.entrySet().iterator();
            while (classesIterator.hasNext())
            {
               Map.Entry entry = (Map.Entry) classesIterator.next();
               String cname = (String) entry.getKey();
               TLongObjectHashMap calledMethods = (TLongObjectHashMap) entry.getValue();
               long[] calledKeys = calledMethods.keys();
               for (int j = 0; j < calledKeys.length; j++)
               {
                  long calledHash = calledKeys[j];
                  ArrayList bindings = getConCalledByMethodBindings(callingHash, cname, calledHash);
                  bindConCalledByMethodInterceptorChain(bindings, callingHash, cname, calledHash);
               }
            }
         }
      }
      
      public ConByMethodInfo resolveCallerConstructorInfo(long callingMethodHash, String calledClass, long calledConHash)
      {
         //The main entry point for adding data to the maps, let us initialise them here.
         if (conCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP || 
               backrefConCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_HASHMAP || 
               conCalledByMethodInterceptors == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP)
         {
            lockWrite();
            try
            {
               if (conCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP)
               {
                  conCalledByMethodBindings = new TLongObjectHashMap();
               }
               if (backrefConCalledByMethodBindings == UnmodifiableEmptyCollections.EMPTY_HASHMAP)
               {
                  backrefConCalledByMethodBindings = new HashMap();
               }
               if (conCalledByMethodInterceptors == UnmodifiableEmptyCollections.EMPTY_TLONG_OBJECT_HASHMAP)
               {
                  conCalledByMethodInterceptors = new TLongObjectHashMap();
               }
            }
            finally
            {
               unlockWrite();
            }
         }
         
         if (System.getSecurityManager() == null)
         {
            return ResolveCallerConstuctorInfoAction.NON_PRIVILEGED.resolveInfo(this, callingMethodHash, calledClass, calledConHash);
         }
         else
         {
            return ResolveCallerConstuctorInfoAction.PRIVILEGED.resolveInfo(this, callingMethodHash, calledClass, calledConHash);
         }
      }

      private ConByMethodInfo doResolveCallerConstructorInfo(long callingMethodHash, String calledClass, long calledConHash)
      {
         try
         {
            Method callingMethod = MethodHashing.findMethodByHash(clazz, callingMethodHash);
            if (callingMethod == null) throw new RuntimeException("Unable to figure out calling method of a constructor caller pointcut");
            // FIXME ClassLoader - how do we know the class is visible from the context classloader?
            Class called = SecurityActions.getContextClassLoader().loadClass(calledClass);
            Constructor calledCon = MethodHashing.findConstructorByHash(called, calledConHash);
            if (calledCon == null) throw new RuntimeException("Unable to figure out calledcon of a constructor caller pointcut");

            boolean matched = false;
            synchronized (manager.getBindings())
            {
               Iterator it = manager.getBindings().values().iterator();
               while (it.hasNext())
               {
                  AdviceBinding binding = (AdviceBinding) it.next();
                  if (binding.getPointcut().matchesCall(ClassAdvisor.this, callingMethod, called, calledCon))
                  {
                     addConstructorCalledByMethodPointcut(callingMethodHash, calledClass, calledConHash, binding);
                     matched = true;
                  }
               }
            }
            if (!matched) initializeConCalledByMethodEmptyChain(callingMethodHash, calledClass, calledConHash);
            ConByMethodInfo info = initializeConCalledByMethodInterceptorsMap(callingMethod, callingMethodHash, calledClass, calledConHash, calledCon);
            ArrayList bindings = getConCalledByMethodBindings(callingMethodHash, calledClass, calledConHash);
            bindConCalledByMethodInterceptorChain(bindings, callingMethodHash, calledClass, calledConHash);
            return info;
         }
         catch (Exception x)
         {
            throw new RuntimeException(x);
         }
      }

      private void addConstructorCalledByMethodPointcut(long callingMethodHash, String calledClass, long calledMethodHash, AdviceBinding binding) throws Exception
      {
         if (AspectManager.verbose) System.err.println("method call matched binding " + binding.getPointcut().getExpr());
         adviceBindings.add(binding);
         binding.addAdvisor(ClassAdvisor.this);
         HashMap callingMethod = (HashMap) conCalledByMethodBindings.get(callingMethodHash);
         if (callingMethod == null)
         {
            callingMethod = new HashMap();
            conCalledByMethodBindings.put(callingMethodHash, callingMethod);
         }
         TLongObjectHashMap classMap = (TLongObjectHashMap) callingMethod.get(calledClass);
         if (classMap == null)
         {
            classMap = new TLongObjectHashMap();
            callingMethod.put(calledClass, classMap);
         }
         ArrayList bindings = (ArrayList) classMap.get(calledMethodHash);
         boolean createdBindings = false;
         if (bindings == null)
         {
            bindings = new ArrayList();
            classMap.put(calledMethodHash, bindings);
            createdBindings = true;
         }
         if (!bindings.contains(binding)) bindings.add(binding);
      
         // this is so that we can undeploy a caller
         ArrayList backrefs = (ArrayList) backrefConCalledByMethodBindings.get(binding.getName());
         if (backrefs == null)
         {
            backrefs = new ArrayList();
            backrefConCalledByMethodBindings.put(binding.getName(), backrefs);
            backrefs.add(bindings);
         }
         else if (createdBindings) backrefs.add(bindings);
      }

      private ConByMethodInfo initializeConCalledByMethodInterceptorsMap(Method callingMethod, long callingMethodHash, String calledClass, long calledConHash, Constructor calledCon) throws Exception
      {
         HashMap calledClassesMap = (HashMap) conCalledByMethodInterceptors.get(callingMethodHash);
         if (calledClassesMap == null)
         {
            calledClassesMap = new HashMap();
            conCalledByMethodInterceptors.put(callingMethodHash, calledClassesMap);
         }
         TLongObjectHashMap calledMethodsMap = (TLongObjectHashMap) calledClassesMap.get(calledClass);
         if (calledMethodsMap == null)
         {
            calledMethodsMap = new TLongObjectHashMap();
            calledClassesMap.put(calledClass, calledMethodsMap);
         }

         ConByMethodInfo info = createConByMethodInfo(calledClass, callingMethod, callingMethodHash, calledCon, calledConHash);
         calledMethodsMap.put(calledConHash, info);
         return info;
      }

      private void initializeConCalledByMethodEmptyChain(long callingMethodHash, String calledClass, long calledConHash) throws Exception
      {
         HashMap callingMethod = (HashMap) conCalledByMethodBindings.get(callingMethodHash);
         if (callingMethod == null)
         {
            callingMethod = new HashMap();
            conCalledByMethodBindings.put(callingMethodHash, callingMethod);
         }
         TLongObjectHashMap classMap = (TLongObjectHashMap) callingMethod.get(calledClass);
         if (classMap == null)
         {
            classMap = new TLongObjectHashMap();
            callingMethod.put(calledClass, classMap);
         }
         ArrayList bindings = (ArrayList) classMap.get(calledConHash);
         if (bindings == null)
         {
            bindings = new ArrayList();
            classMap.put(calledConHash, bindings);
         }
      }

      private ConByMethodInfo getConCalledByMethod(long callingMethodHash, String calledClass, long calledConHash)
      {
         HashMap calledClasses = (HashMap) conCalledByMethodInterceptors.get(callingMethodHash);
         TLongObjectHashMap calledMethods = (TLongObjectHashMap) calledClasses.get(calledClass);
         ConByMethodInfo info = (ConByMethodInfo) calledMethods.get(calledConHash);
         return info;
      }

      private ConByMethodInfo createConByMethodInfo(String calledClass, Method callingMethod, long callingMethodHash, Constructor calledCon, long calledConHash) throws Exception
      {
         //The standard ConstructorCalledByXXXXInvocation class calls by reflection and needs access
         calledCon.setAccessible(true);

         // FIXME ClassLoader - how do we know the class is visible from the context classloader?
         Class calledClazz = SecurityActions.getContextClassLoader().loadClass(calledClass);
         try
         {
            int index = calledClass.lastIndexOf('.');
            String baseClassName = calledClass.substring(index + 1);
            Method wrapper = calledCon.getDeclaringClass().getDeclaredMethod(ConstructorExecutionTransformer.constructorFactory(baseClassName), calledCon.getParameterTypes());
            return new ConByMethodInfo(ClassAdvisor.this, calledClazz, callingMethod, callingMethodHash, calledCon, calledConHash, wrapper, null);
         }
         catch (NoSuchMethodException e)
         {
            return new ConByMethodInfo(ClassAdvisor.this, calledClazz, callingMethod, callingMethodHash, calledCon, calledConHash, null, null);
         }
      }

      private void bindConCalledByMethodInterceptorChain(ArrayList bindings, long callingHash, String cname, long calledHash) throws Exception
      {
         ConByMethodInfo info = getConCalledByMethod(callingHash, cname, calledHash);
         info.clear();
         Iterator it = bindings.iterator();
         while (it.hasNext())
         {
            AdviceBinding binding = (AdviceBinding) it.next();
            pointcutResolved(info, binding, new ConstructorCalledByMethodJoinpoint(info.getCallingMethod(), info.getConstructor()));
         }
         finalizeConCalledByMethodInterceptorChain(info);
      }

      public void cleanup()
      {
         conCalledByMethodBindings.clear();
         conCalledByMethodInterceptors.clear();
      }
   }
}