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
package org.jboss.aop.proxy.container;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.SerialVersionUID;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;

import org.jboss.aop.Advised;
import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ClassContainer;
import org.jboss.aop.InstanceAdvised;
import org.jboss.aop.MethodInfo;
import org.jboss.aop.instrument.TransformerCommon;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.util.JavassistMethodHashing;


/**
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class ContainerProxyFactory
{
   private static final String ADVISED = Advised.class.getName();
   private static final String INSTANCE_ADVISED = InstanceAdvised.class.getName();
   private static final CtClass[] EMPTY_CTCLASS_ARRAY = new CtClass[0];
   public static final String PROXY_NAME_PREFIX = "AOPContainerProxy$";
   
   private static Object maplock = new Object();
   private static WeakHashMap<Class, Map<ContainerProxyCacheKey, Class>> proxyCache = new WeakHashMap<Class, Map<ContainerProxyCacheKey, Class>>();
   private static volatile int counter = 0;
   
   private static CtMethod setDelegateMethod;


   /** True if java.lang.Object should be used as the super class for this proxy */
   private boolean objectAsSuper;

   /** The Advisor for this proxy */
   private Advisor advisor;

   /** The class we are generating this proxy for */
   private Class clazz;

   /** The generated proxy */
   private CtClass proxy;
   
   /** The class pool for the proxy (i.e for the class we are proxying */
   private ClassPool pool;
   
   /** True if we are proxying a class already woven by jboss aop, false otherwise */
   private boolean isAdvised;
   
   ProxyStrategy proxyStrategy;
   
   private CtConstructor defaultCtor;
   
   public static Class getProxyClass(Class clazz, AspectManager manager) throws Exception
   {
      ContainerProxyCacheKey key = new ContainerProxyCacheKey(clazz);
      ClassContainer container = getTempClassContainer(clazz, manager);
      return getProxyClass(false, key, container);
   }
   
   public static Class getProxyClass(boolean objectAsSuper, ContainerProxyCacheKey key, Advisor advisor)
           throws Exception
   {
      return getProxyClass(objectAsSuper, key, advisor, null);
   }
   
   public static Class getProxyClass(boolean objectAsSuper, ContainerProxyCacheKey key, Advisor advisor, MarshalledContainerProxy outOfVmProxy)
   throws Exception
   {   
      Class clazz = key.getClazz();
      // Don't make a proxy of a proxy !
      if (Delegate.class.isAssignableFrom(clazz)) clazz = clazz.getSuperclass();

      Class proxyClass = null;
      synchronized (maplock)
      {
         Map<ContainerProxyCacheKey, Class> map = proxyCache.get(clazz);
         if (map == null)
         {
            map = new HashMap<ContainerProxyCacheKey, Class>();
            proxyCache.put(clazz, map);
         }
         else
         {
            proxyClass = map.get(key);
         }
         
         if (proxyClass == null)
         {
            proxyClass = generateProxy(objectAsSuper, clazz, advisor, outOfVmProxy);
            map.put(key, proxyClass);
         }
      }
      return proxyClass;
   }

   private static Class generateProxy(boolean objectAsSuper, Class clazz, Advisor advisor, MarshalledContainerProxy outOfVmProxy) throws Exception
   {
      ArrayList introductions = advisor.getInterfaceIntroductions();
      CtClass proxy = createProxyCtClass(objectAsSuper, introductions, clazz, advisor, outOfVmProxy);
      ProtectionDomain pd = clazz.getProtectionDomain();
      Class proxyClass = TransformerCommon.toClass(proxy, pd);
      return proxyClass;
   }

   private static ClassProxyContainer getTempClassContainer(Class clazz, AspectManager manager)
   {
      ClassProxyContainer container = new ClassProxyContainer("temp", manager);
      container.setClass(clazz);

      Iterator it = container.getManager().getInterfaceIntroductions().values().iterator();
      while (it.hasNext())
      {
         InterfaceIntroduction intro = (InterfaceIntroduction) it.next();
         if (intro.matches(container, container.getClazz()))
         {
            container.addInterfaceIntroduction(intro);
         }
      }

      return container;
   }
   
   private static CtClass createProxyCtClass(boolean objectAsSuper, ArrayList mixins, Class clazz, Advisor advisor)
   throws Exception
   {
      return createProxyCtClass(objectAsSuper, mixins, clazz, advisor, null);
   }
   
   private static CtClass createProxyCtClass(boolean objectAsSuper, ArrayList mixins, Class clazz, Advisor advisor, MarshalledContainerProxy outOfVmProxy)
           throws Exception
   {
      ContainerProxyFactory factory = new ContainerProxyFactory(objectAsSuper, mixins, clazz, advisor, outOfVmProxy);
      return factory.createProxyCtClass();
   }

   
   private ContainerProxyFactory(boolean objectAsSuper, ArrayList mixins, Class clazz, Advisor advisor, MarshalledContainerProxy outOfVmProxy)
   {
      this.objectAsSuper = objectAsSuper;
      this.clazz = clazz;
      this.advisor = advisor;
      isAdvised = Advised.class.isAssignableFrom(clazz);
      
      if (outOfVmProxy == null)
      {
         proxyStrategy = new OriginalProxyStrategy(mixins);
      }
      else
      {
         proxyStrategy = new UnmarshalledInRemoteJVMProxyStrategy(outOfVmProxy);
      }
   }
  
   
   private CtClass createProxyCtClass() throws Exception
   {
      this.pool = AspectManager.instance().findClassPool(clazz.getClassLoader());
      if (pool == null) throw new NullPointerException("Could not find ClassPool");

      createBasics();
      addMethodsAndMixins();
      overrideSpecialMethods(clazz, proxy);
      
      SerialVersionUID.setSerialVersionUID(proxy);
      
      return proxy;
   }
   

   private CtClass createBasics() throws Exception
   {
      Class proxySuper  = (objectAsSuper) ? Object.class : this.clazz; 
      String classname = getClassName();

      CtClass template = pool.get("org.jboss.aop.proxy.container.ProxyTemplate");
      CtClass superclass = pool.get(proxySuper.getName());

      this.proxy = TransformerCommon.makeClass(pool, classname, superclass);
      proxy.addInterface(pool.get("org.jboss.aop.instrument.Untransformable"));
      
      //Add all the interfaces of the class
      Class[] interfaces = proxySuper.getInterfaces();
      for (int i = 0 ; i < interfaces.length ; i++)
      {
         CtClass interfaze = pool.get(interfaces[i].getName());
         proxy.addInterface(interfaze);
      }
      
      copyConstructors(superclass, proxy);
      addFieldFromTemplate(template, "mixins");

      //Add methods/fields needed for Delegate interface
      proxy.addInterface(pool.get("org.jboss.aop.proxy.container.Delegate"));
      
      addFieldFromTemplate(template, "delegate", superclass);
      addMethodFromTemplate(template, "getDelegate", "{ return delegate; }");
      setDelegateMethod = addMethodFromTemplate(template, "setDelegate", "{ this.delegate = (" + proxySuper.getName() + ")$1; }");

      addFieldFromTemplate(template, "key");
      addMethodFromTemplate(template, "setContainerProxyCacheKey", "{this.key=$1;}");
      
      
      //Add methods/fields needed for AspectManaged interface 
      proxy.addInterface(pool.get("org.jboss.aop.proxy.container.AspectManaged"));

      addFieldFromTemplate(template, "currentAdvisor");
      addFieldFromTemplate(template, "classAdvisor");
      addMethodFromTemplate(template, "getAdvisor", "{return classAdvisor;}");
      addMethodFromTemplate(template, "setAdvisor", "{this.classAdvisor = $1;currentAdvisor = classAdvisor;}");
      
      addFieldFromTemplate(template, "metadata");
      addMethodFromTemplate(template, "setMetadata", "{this.metadata = $1;}");
      
      addFieldFromTemplate(template, "instanceAdvisor");     
      addMethodFromTemplate(template, "setInstanceAdvisor", instanceAdvisorSetterBody());
      addMethodFromTemplate(template, "getInstanceAdvisor", instanceAdvisorGetterBody());
      
      addMethodFromTemplate(template, "writeReplace", writeReplaceObjectBody());
      addMethodFromTemplate(template, "localUnmarshal", localUnmarshalObjectBody(superclass));
      addMethodFromTemplate(template, "remoteUnmarshal", remoteUnmarshalObjectBody(superclass));
      
      if (objectAsSuper)
      {
         addMethodFromTemplate(template, "equals", equalsBody());
         addMethodFromTemplate(template, "hashCode", hashCodeBody());
      }

      addMethodFromTemplate(template, "toString", toStringBody());

      copyAnnotations(superclass, proxy);
      copySignature(superclass, proxy);
      
      return proxy;
   }
   
   private String instanceAdvisorSetterBody()
   {
      return 
         "{" +
         "   synchronized (this)" +
         "   {" +
         "   if (this.instanceAdvisor != null)" +
         "   {" +
         "      throw new RuntimeException(\"InstanceAdvisor already set\");" +
         "   }" +
         "   if (!($1 instanceof org.jboss.aop.proxy.container.InstanceProxyContainer))" +
         "   {" +
         "      throw new RuntimeException(\"Wrong type for instance advisor: \" + $1);" +
         "   }" +
         "   this.instanceAdvisor = $1;" +
         "   currentAdvisor = (org.jboss.aop.proxy.container.InstanceProxyContainer)$1;" +
         "   }" +
         "}";
   }

   private String instanceAdvisorGetterBody()
   {
      return 
         "{" +
         "   synchronized(this)" +
         "   {" +
         "      if (instanceAdvisor == null)" +
         "      {" +
         "         org.jboss.aop.proxy.container.InstanceProxyContainer ipc = ((org.jboss.aop.proxy.container.ClassProxyContainer)currentAdvisor).createInstanceProxyContainer();" +
         "         setInstanceAdvisor(ipc);" +
         "      }" +
         "   }" +
         "   return instanceAdvisor;" +
         "}";
   }

   private String writeReplaceObjectBody()
   {
      return 
         "{" +
         "   return new " + MarshalledContainerProxy.class.getName() + "(" +
         "      this.getClass()," +
         "      this.key," +
         "      this.mixins," +
         "      this.delegate," +
         "      this.currentAdvisor," +
         "      this.metadata);" +
         "}";
   }
   
   private String localUnmarshalObjectBody(CtClass superclass)
   {
      return 
         "{" +
         "   try{" +
         "      this.delegate = (" + superclass.getName() + ")$1.getDelegate();" +
         "      this.mixins = $1.getMixins();" +
         "      this.metadata = $1.getMetadata();" +
         "      this.key = $1.getKey();" +
         "      java.lang.Class clazz = $1.getClazz();" +
         "      this.classAdvisor = org.jboss.aop.proxy.container.ContainerCache.getCachedContainer(this.key);" +
         "      this.currentAdvisor = classAdvisor;" +
         "      if ($1.getInstanceAdvisorDomainName() != null)" +
         "      {" +
         "         org.jboss.aop.proxy.container.ProxyAdvisorDomain domain = (org.jboss.aop.proxy.container.ProxyAdvisorDomain)org.jboss.aop.AspectManager.getTopLevelAspectManager().findManagerByName($1.getInstanceAdvisorDomainName());" +
         "         this.currentAdvisor = domain.getAdvisor();" +
         "         this.instanceAdvisor = this.currentAdvisor;" +
         "      }" +
         "  }catch(java.lang.Exception e){throw e;}" +
         "}";
   }
   
   private String remoteUnmarshalObjectBody(CtClass superclass)
   {
      return 
         "{" +
         "   this.delegate = (" + superclass.getName() + ")$1.getDelegate();" +
         "   this.mixins = $1.getMixins();" +
         "   this.metadata = $1.getMetadata();" +
         "   this.key = $1.getKey();" +
         "   java.lang.Class clazz = $1.getClazz();" +
         "   this.classAdvisor = $2;" +
         "   this.currentAdvisor = $2;" +
         "   this.instanceAdvisor = $2;" +
         "}";
   }
   
   private String equalsBody()
   {
      return 
         "{" +
         "   if (delegate != null)" +
         "   {" +
         "      if ($1 != null && $1 instanceof org.jboss.aop.proxy.container.Delegate)" +
         "         $1 = ((org.jboss.aop.proxy.container.Delegate) $1).getDelegate();" +
         "      return delegate.equals($1);" +
         "   }" +
         "   else" +
         "      return super.equals($1);" +
         "}";
   }
   
   private String hashCodeBody()
   {
      return 
         "{" +
         "   if (delegate != null)" +
         "      return delegate.hashCode();" +
         "   else" +
         "      return super.hashCode();" +
         "}";
   }
   
   private String toStringBody()
   {
      return 
         "{" +
         "   if (delegate != null)" +
         "      return delegate.toString() + \" (proxied by \" + this.getClass().getName() + \"@\" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + \")\";" +
         "   else" +
         "      return super.toString() + \" (empty proxy of \" + this.getClass().getSuperclass().getName() + \")\";" +
         "}";
   }

   private CtField addFieldFromTemplate(CtClass template, String name) throws Exception
   {
      return addFieldFromTemplate(template, name, null);
   }
   
   private CtField addFieldFromTemplate(CtClass template, String name, CtClass type) throws Exception
   {
      CtField templateField = template.getField(name);
      CtClass fieldType = (type == null) ? templateField.getType() : type;
      CtField field = new CtField(fieldType, name, proxy);
      field.setModifiers(templateField.getModifiers());
      proxy.addField(field);
      return field;
   }
   
   private CtMethod addMethodFromTemplate(CtClass template, String name, String body) throws Exception
   {
      CtMethod templateMethod = template.getDeclaredMethod(name);
      CtMethod method = CtNewMethod.make(templateMethod.getReturnType(), name, templateMethod.getParameterTypes(), templateMethod.getExceptionTypes(), body, proxy);
      method.setModifiers(templateMethod.getModifiers());
      proxy.addMethod(method);
      return method;
   }
   
   private void copyConstructors(CtClass superclass, CtClass proxy) throws Exception
   {
      CtConstructor[] ctors = superclass.getConstructors();
      int minParameters = Integer.MAX_VALUE;
      CtConstructor bestCtor = null;
      for (int i = 0 ; i < ctors.length ; i++)
      {
         CtClass[] params = ctors[i].getParameterTypes(); 
         
         CtConstructor ctor = CtNewConstructor.make(
            ctors[i].getParameterTypes(),
            ctors[i].getExceptionTypes(),
            CtNewConstructor.PASS_PARAMS,
            null,
            null,
            proxy);
         ctor.setModifiers(ctors[i].getModifiers());
         proxy.addConstructor(ctor);

         if (params.length < minParameters)
         {
            bestCtor = ctor;
            minParameters = params.length;
         }
         if (params.length == 0)
         {
            defaultCtor = ctor;
         }
      }
      
      if (minParameters > 0)
      {
         //We don't have a default constructor, we need to create one passing in null to the super ctor
         createDefaultConstructor(bestCtor);
      }
   }
   
   private void createDefaultConstructor(CtConstructor bestCtor) throws NotFoundException, CannotCompileException
   {
      CtClass params[] = bestCtor.getParameterTypes();
      
      StringBuffer superCall = new StringBuffer("super(");
      
      for (int i = 0 ; i < params.length ; i++)
      {
         if (i > 0)
         {
            superCall.append(", ");
         }
         
         superCall.append(getNullType(params[i]));
      }
      
      superCall.append(");");
      
      defaultCtor = CtNewConstructor.make(EMPTY_CTCLASS_ARRAY, EMPTY_CTCLASS_ARRAY, "{" + superCall.toString() + "}", proxy);
      proxy.addConstructor(defaultCtor);
   }

   private String getNullType(CtClass clazz)
   {
      if (!clazz.isPrimitive())
      {
         return "null";
      }
      else
      {
         if (clazz.equals(CtClass.booleanType)) return "false";
         else if (clazz.equals(CtClass.charType)) return "'0'";
         else if (clazz.equals(CtClass.byteType)) return "0";
         else if (clazz.equals(CtClass.shortType)) return "0";
         else if (clazz.equals(CtClass.intType)) return "0";
         else if (clazz.equals(CtClass.longType)) return "0L";
         else if (clazz.equals(CtClass.floatType)) return "0f";
         else if (clazz.equals(CtClass.doubleType)) return "0d";
         else return "";//void
      }
      
   }
   
   private void addMethodsAndMixins()throws Exception
   {
      HashSet<Long> addedMethods = new HashSet<Long>();
      createMixinsAndIntroductions(addedMethods);
      createProxyMethods(addedMethods);
   }

   private void createMixinsAndIntroductions(HashSet<Long> addedMethods) throws Exception
   {
      HashSet<String> addedInterfaces = new HashSet<String>();
      Set<String> implementedInterfaces = interfacesAsSet();
      
      if (proxyStrategy.hasIntroductions())
      {
         HashMap<String, Integer> intfs = new HashMap<String, Integer>();
         HashMap<String, Integer> mixinIntfs = new HashMap<String, Integer>();
         ArrayList<MixinInfo> mixes = new ArrayList<MixinInfo>();
         proxyStrategy.getMixins(intfs, mixinIntfs, mixes);
         
         //Now that we have added the mixins, add all the proxies methods to the added methods set
         HashMap allMethods = JavassistMethodHashing.getDeclaredMethodMap(proxy);
         addedMethods.addAll(allMethods.keySet());

         createMixins(addedMethods, mixes, addedInterfaces, implementedInterfaces);
         createIntroductions(addedMethods, intfs, addedInterfaces, implementedInterfaces);
      }
   }
   
   
   
   private void createMixins(HashSet<Long> addedMethods, ArrayList<MixinInfo> mixes, HashSet<String> addedInterfaces, Set<String> implementedInterfaces) throws Exception
   {
      for (int mixinId = 0 ; mixinId < mixes.size() ; mixinId++)
      {
         MixinInfo mixin = mixes.get(mixinId);
         
         String[] intfs = mixin.getInterfaces();
         
         for (int ifId = 0 ; ifId < intfs.length ; ifId++)
         {
            String intf = intfs[ifId];
            if (addedInterfaces.contains(intf)) throw new Exception("2 mixins are implementing the same interfaces " + intf);
            if (implementedInterfaces.contains(intf))  throw new Exception("Attempting to mixin interface already used by class " + intf);

            CtClass intfClass = pool.get(intf);
            CtMethod[] methods = intfClass.getMethods();
            HashSet mixinMethods = new HashSet();
            for (int m = 0; m < methods.length; m++)
            {
               if (methods[m].getDeclaringClass().getName().equals("java.lang.Object")) continue;
               Long hash = new Long(JavassistMethodHashing.methodHash(methods[m]));
               if (mixinMethods.contains(hash)) continue;
               if (addedMethods.contains(hash)) throw new Exception("More than one mixin has same method");
               mixinMethods.add(hash);
               addedMethods.add(hash);
               String aopReturnStr = (methods[m].getReturnType().equals(CtClass.voidType)) ? "" : "return ($r)";
               String returnStr = (methods[m].getReturnType().equals(CtClass.voidType)) ? "" : "return ";
               String args = "null";
               if (methods[m].getParameterTypes().length > 0) args = "$args";
               String code = "{   " +
                             "   try{" +
                             "      " + intf + " mixin = (" + intf + ")mixins[" + mixinId + "];" +
                             "       org.jboss.aop.MethodInfo mi = currentAdvisor.getMethodInfo(" + hash.longValue() + "L); " +
                             "       org.jboss.aop.advice.Interceptor[] interceptors = mi.getInterceptors();" +
                             "       if (mi != null && interceptors != (Object[])null && interceptors.length > 0) { " +
                             "          org.jboss.aop.proxy.container.ContainerProxyMethodInvocation invocation = new org.jboss.aop.proxy.container.ContainerProxyMethodInvocation(mi, interceptors, this); " +
                             "          invocation.setArguments(" + args + "); " +
                             "          invocation.setTargetObject(mixin); " +
                             "          invocation.setMetaData(metadata);" +
                             "          " + aopReturnStr + " invocation.invokeNext(); " +
                             "       } else { " +
                             "       " + returnStr + " mixin." + methods[m].getName() + "($$);" +
                             "       } " +
                             "    }finally{" +
                             "    }" +
                             "}";
               CtMethod newMethod = CtNewMethod.make(methods[m].getReturnType(), methods[m].getName(), methods[m].getParameterTypes(), methods[m].getExceptionTypes(), code, proxy);
               newMethod.setModifiers(Modifier.PUBLIC);
               proxy.addMethod(newMethod);

               copySignature(methods[m], newMethod);
            }

            proxy.addInterface(intfClass);
            addedInterfaces.add(intfClass.getName());
         }
      }
   }

   private void createProxyMethods(HashSet<Long> addedMethods) throws Exception
   {
      HashMap allMethods = JavassistMethodHashing.getMethodMap(proxy.getSuperclass());

      Iterator it = allMethods.entrySet().iterator();
      while (it.hasNext())
      {
         Map.Entry entry = (Map.Entry) it.next();
         CtMethod m = (CtMethod) entry.getValue();
         if (!Modifier.isPublic(m.getModifiers()) || Modifier.isStatic(m.getModifiers()) || Modifier.isFinal(m.getModifiers())) continue;

         Long hash = (Long) entry.getKey();
         if (addedMethods.contains(hash)) continue;
         addedMethods.add(hash);
         String aopReturnStr = (m.getReturnType().equals(CtClass.voidType)) ? "" : "return ($r)";
         String returnStr = (m.getReturnType().equals(CtClass.voidType)) ? "" : "return ";
         String args = "null";
         
         String name = null;
         if (isAdvised)
         {
            MethodInfo info = advisor.getMethodInfo(hash.longValue());
            Method originalMethod = info.getUnadvisedMethod();
            name = originalMethod.getName();
         }
         else
         {
            name = m.getName();
         }
         
         if (m.getParameterTypes().length > 0) args = "$args";
         String code = "{   " +
                       "    boolean handled = false;" +
                       "    try{" +
                       "       if (currentAdvisor != null) {" +
                       "          org.jboss.aop.MethodInfo mi = currentAdvisor.getMethodInfo(" + hash.longValue() + "L); " +
                       "          if (mi == null) " +
                       "             throw new java.lang.NoSuchMethodError(\"" + m.getName() + m.getSignature() + "\");" +
                       "          org.jboss.aop.advice.Interceptor[] interceptors = mi.getInterceptors(); " +
                       "          if (interceptors != (Object[])null && interceptors.length > 0) { " +
                       "             handled = true;" + 
                       "             org.jboss.aop.proxy.container.ContainerProxyMethodInvocation invocation = new org.jboss.aop.proxy.container.ContainerProxyMethodInvocation(mi, interceptors, this); " +
                       "             invocation.setArguments(" + args + "); " +
                       "             invocation.setTargetObject(delegate); " +
                       "             invocation.setMetaData(metadata);" +
                       "             " + aopReturnStr + " invocation.invokeNext(); " +
                       "          }" +
                       "       }" +
                       "       if (!handled && delegate != null){ " +
                       "          " + returnStr + " delegate." + name + "($$); " +
                       "       }" +
                       "       return " + getNullType(m.getReturnType()) + ";" +
                       "    }finally{" +
                       "    }" +
                       "}";
         CtMethod newMethod = CtNewMethod.make(m.getReturnType(), m.getName(), m.getParameterTypes(), m.getExceptionTypes(), code, proxy);
         newMethod.setModifiers(Modifier.PUBLIC);
         proxy.addMethod(newMethod);

         copyAnnotations(m, newMethod);
         copySignature(m, newMethod);
      }
   }
   
   private void createIntroductions(HashSet<Long> addedMethods, HashMap<String, Integer> intfs, HashSet<String> addedInterfaces, Set<String> implementedInterfaces) throws Exception
   {
      Iterator it = intfs.keySet().iterator();
      while (it.hasNext())
      {
         String intf = (String) it.next();
         if (addedInterfaces.contains(intf)) throw new Exception("2 mixins are implementing the same interfaces");
         if (implementedInterfaces.contains(intf))  
         {
            continue;
         }

         CtClass intfClass = pool.get(intf);
         CtMethod[] methods = intfClass.getMethods();
         HashSet mixinMethods = new HashSet();
         for (int m = 0; m < methods.length; m++)
         {
            if (methods[m].getDeclaringClass().getName().equals("java.lang.Object")) continue;
            
            Long hash = new Long(JavassistMethodHashing.methodHash(methods[m]));
            if (mixinMethods.contains(hash)) continue;
            if (addedMethods.contains(hash)) continue;

            mixinMethods.add(hash);
            addedMethods.add(hash);
            String aopReturnStr = (methods[m].getReturnType().equals(CtClass.voidType)) ? "" : "return ($r)";
            String args = "null";
            if (methods[m].getParameterTypes().length > 0) args = "$args";
            String code = "{   " +
                          "    try{" +
                          "       org.jboss.aop.MethodInfo mi = currentAdvisor.getMethodInfo(" + hash.longValue() + "L); " +
                          "       if (mi == null) " +
                          "          throw new java.lang.NoSuchMethodError(\"" + methods[m].getName() + methods[m].getSignature() + "\");" +
                          "       org.jboss.aop.advice.Interceptor[] interceptors = mi.getInterceptors();" +
                          "       org.jboss.aop.proxy.container.ContainerProxyMethodInvocation invocation = new org.jboss.aop.proxy.container.ContainerProxyMethodInvocation(mi, interceptors, this); " +
                          "       invocation.setArguments(" + args + "); " +
                          "       invocation.setTargetObject(delegate); " +
                          "       invocation.setMetaData(metadata);" +
                          "       " + aopReturnStr + " invocation.invokeNext(); " +
                          "    }finally{" +
                          "    }" +
                          "}";
            
            CtMethod newMethod = CtNewMethod.make(methods[m].getReturnType(), methods[m].getName(), methods[m].getParameterTypes(), methods[m].getExceptionTypes(), code, proxy);
            newMethod.setModifiers(Modifier.PUBLIC);
            proxy.addMethod(newMethod);
            
            copySignature(methods[m], newMethod);
         }

         proxy.addInterface(intfClass);
         addedInterfaces.add(intfClass.getName());
      }
   }

   private Set<String> interfacesAsSet() throws NotFoundException
   {
      HashSet<String> set = new HashSet<String>();
      CtClass[] interfaces = proxy.getInterfaces();
      
      for (int i = 0 ; i < interfaces.length ; i++)
      {
         set.add(interfaces[i].getName());
      }
      
      return set;
   }
   
   private String getClassName()
   {
      String packageName = clazz.getPackage().getName();
      if (!packageName.startsWith("java.") && !packageName.startsWith("sun."))
      {
         packageName += ".";
      }
      else
      {
         packageName = "";
      }
      
      return packageName + PROXY_NAME_PREFIX + counter++;
   }

   private void overrideSpecialMethods(Class clazz, CtClass proxy) throws Exception
   {
      addInstanceAdvisedMethods(clazz, proxy);
   }

   /**
    * If the class is Advised, the _getInstanceAdvisor() and _setInstanceAdvisor() methods will
    * not have been overridden. Make sure that these methods work with the instance proxy container.
    */
   private void addInstanceAdvisedMethods(Class clazz, CtClass proxy) throws Exception
   {
      CtClass advisedInterface = null;
      CtClass interfaces[] = proxy.getInterfaces();
      
      for (int i = 0 ; i < interfaces.length ; i++)
      {
         if (interfaces[i].getName().equals(ADVISED))
         {
            advisedInterface = interfaces[i];
            break;
         }
      }
      
      if (advisedInterface != null)
      {
         CtMethod[] methods = advisedInterface.getMethods();
         for (int i = 0 ; i < methods.length ; i++)
         {
            if (methods[i].getDeclaringClass().getName().equals(INSTANCE_ADVISED))
            {
               String name = methods[i].getName();
               String body = null;
               if (name.equals("_getInstanceAdvisor"))
               {
                  body = "{ return getInstanceAdvisor(); }";
               }
               else if (name.equals("_setInstanceAdvisor"))
               {
                  body = "{ setInstanceAdvisor($1); }";
               }
               
               if (body != null)
               {
                  CtMethod m = CtNewMethod.make(methods[i].getReturnType(), methods[i].getName(), methods[i].getParameterTypes(), methods[i].getExceptionTypes(), body, proxy);
                  m.setModifiers(Modifier.PUBLIC);
                  proxy.addMethod(m);
               }
            }
         }
      }
   }
   
   private void copyAnnotations(CtMethod src, CtMethod dest) throws NotFoundException
   {
      javassist.bytecode.MethodInfo srcInfo = src.getMethodInfo2();
      javassist.bytecode.MethodInfo destInfo = dest.getMethodInfo2();
      copyAnnotations(srcInfo, destInfo, AnnotationsAttribute.invisibleTag);
      copyAnnotations(srcInfo, destInfo, AnnotationsAttribute.visibleTag);

      int numParams = src.getParameterTypes().length;
      copyParameterAnnotations(numParams, srcInfo, destInfo, ParameterAnnotationsAttribute.visibleTag);
      copyParameterAnnotations(numParams, srcInfo, destInfo, ParameterAnnotationsAttribute.invisibleTag);

   }
   
   private void copyAnnotations(javassist.bytecode.MethodInfo src, javassist.bytecode.MethodInfo dest, String annotationTag)
   {
      AnnotationsAttribute attribute = (AnnotationsAttribute) src.getAttribute(annotationTag);
      if (attribute != null)
      {
         dest.addAttribute(attribute.copy(dest.getConstPool(), new HashMap()));
      }
   }
   
   private void copyParameterAnnotations(int numParams, javassist.bytecode.MethodInfo src, javassist.bytecode.MethodInfo dest, String paramsTag)
   {
      ParameterAnnotationsAttribute params = (ParameterAnnotationsAttribute)src.getAttribute(paramsTag);
      if (params != null)
      {
         dest.addAttribute(params.copy(dest.getConstPool(), new HashMap()));
         ParameterAnnotationsAttribute srcParams = new ParameterAnnotationsAttribute(src.getConstPool(), paramsTag);
         Annotation[][] emptyParamAnnotations = new Annotation[numParams][];
         for (int i = 0 ; i < numParams ; i++)
         {
            emptyParamAnnotations[i] = new Annotation[0];
         }
         srcParams.setAnnotations(emptyParamAnnotations);
         src.addAttribute(srcParams);
      }
   }

   private void copyAnnotations(CtClass src, CtClass dest) throws NotFoundException
   {
      ClassFile srcFile = src.getClassFile2();
      ClassFile destFile = dest.getClassFile2();
      copyAnnotations(srcFile, destFile, AnnotationsAttribute.invisibleTag);
      copyAnnotations(srcFile, destFile, AnnotationsAttribute.visibleTag);
   }
   
   private void copyAnnotations(ClassFile src, ClassFile dest, String annotationTag)
   {
      AnnotationsAttribute attribute = (AnnotationsAttribute) src.getAttribute(annotationTag);
      if (attribute != null)
      {
         dest.addAttribute(attribute.copy(dest.getConstPool(), new HashMap()));
      }
   }
   
   
   private void copySignature(CtMethod src, CtMethod dest)
   {
      javassist.bytecode.MethodInfo srcInfo = src.getMethodInfo2();
      javassist.bytecode.MethodInfo destInfo = dest.getMethodInfo2();
      
      SignatureAttribute sig = (SignatureAttribute)srcInfo.getAttribute(SignatureAttribute.tag);
      if (sig != null)
      {
         destInfo.addAttribute(sig.copy(destInfo.getConstPool(), new HashMap()));
      }
   }
   
   private void copySignature(CtClass src, CtClass dest)
   {
      ClassFile srcFile = src.getClassFile2();
      ClassFile destFile = dest.getClassFile2();
      
      SignatureAttribute sig = (SignatureAttribute)srcFile.getAttribute(SignatureAttribute.tag);
      if (sig != null)
      {
         destFile.addAttribute(sig.copy(destFile.getConstPool(), new HashMap()));
      }
   }
   
   private interface ProxyStrategy
   {
      /**
       * Whether the proxy has introductions and/or mixins
       * @return true if we have introductions and/or mixins
       */
      boolean hasIntroductions();
      
      /**
       * @param intfs receives the interfaces from plain interface introductions
       * @param mixinInterfaces receives the interfaces from mixins
       * @param mixes receives the actual InterfaceIntroduction.Mixin objects
       */
      void getMixins(HashMap<String, Integer>  intfs, HashMap<String, Integer>  mixins, ArrayList<MixinInfo> mixes) throws Exception;
   }
   
   private class OriginalProxyStrategy implements ProxyStrategy
   {
      ArrayList<InterfaceIntroduction> mixins;
      
      OriginalProxyStrategy(ArrayList<InterfaceIntroduction> mixins)
      {
         this.mixins = mixins;
      }
      
      public boolean hasIntroductions()
      {
         if (mixins == null)
         {
            return false;
         }
         return mixins.size() > 0;
      }

      public void getMixins(HashMap<String, Integer>  intfs, HashMap<String, Integer>  mixinInterfaces, ArrayList<MixinInfo> mixes) throws Exception
      {
         if (mixins != null)
         {
            HashMap mixinIntfs = new HashMap();
            for (int i = 0; i < mixins.size(); i++)
            {
               InterfaceIntroduction introduction = mixins.get(i);
               getIntroductionInterfaces(introduction, intfs, mixinIntfs, mixes, i);
            }
            if (mixes.size() > 0)
            {
               defaultCtor.insertAfter("mixins = new Object[" + mixes.size() + "];");
               for (int i = 0; i < mixes.size(); i++)
               {
                  //If using a constructor and passing "this" as the parameters, the proxy gets used. The delegate (instance wrapped by proxy) is not 
                  //set in the proxy until later, and if the mixin implements Delegate it will get set with the "real" instance at this point.
                  MixinInfo mixin = mixes.get(i);
                  String initializer = (mixin.getConstruction() == null) ? ("new " + mixin.getClassName() + "()") : mixin.getConstruction();
                  String code = "mixins[" + i + "] = " + initializer + ";";
                  defaultCtor.insertAfter(code);
                  setDelegateMethod.insertAfter("{if (org.jboss.aop.proxy.container.Delegate.class.isAssignableFrom(mixins[" + i + "].getClass())) " +
                        "((org.jboss.aop.proxy.container.Delegate)mixins[" + i + "]).setDelegate($1);}");
               }
            }
         }
      }
      
      /**
       * Split the interface introduction into something we can work with
       * 
       * @param intro The InterfaceIntroduction
       * @param intfs receives the interfaces from plain interface introductions
       * @param mixinInterfaces receives the interfaces from mixins
       * @param mixes receives the actual InterfaceIntroduction.Mixin objects
       * @@param the index interface introduction this data comes from 
       */
      private void getIntroductionInterfaces(InterfaceIntroduction intro, 
            HashMap<String, Integer> intfs, 
            HashMap<String, Integer> mixinInterfaces, 
            ArrayList<MixinInfo> mixes, 
            int idx)
      {
         Iterator it = intro.getMixins().iterator();
         while (it.hasNext())
         {
            InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin) it.next();
            mixes.add(new MixinInfo(mixin));
            for (int i = 0; i < mixin.getInterfaces().length; i++)
            {
               if (intfs.containsKey(mixin.getInterfaces()[i]))
               {
                  intfs.remove(mixin.getInterfaces()[i]);
                  
               }
               if (mixinInterfaces.containsKey(mixin.getInterfaces()[i]))
               {
                  throw new RuntimeException("cannot have an IntroductionInterface that introduces several mixins with the same interfaces " + mixin.getInterfaces()[i]);
               }
               mixinInterfaces.put(mixin.getInterfaces()[i], new Integer(idx));
            }
         }
         if (intro.getInterfaces() != null)
         {
            for (int i = 0; i < intro.getInterfaces().length; i++)
            {
               if (intfs.containsKey(intro.getInterfaces()[i]) || mixinInterfaces.containsKey(intro.getInterfaces()[i])) 
               {
                  //Do nothing
               }
               else
               {
                  intfs.put(intro.getInterfaces()[i], new Integer(idx));
               }
            }
         }
      }
   }
   
   private class UnmarshalledInRemoteJVMProxyStrategy implements ProxyStrategy
   {
      MarshalledContainerProxy outOfVmProxy;
      
      UnmarshalledInRemoteJVMProxyStrategy(MarshalledContainerProxy outOfVmProxy)
      {
         this.outOfVmProxy = outOfVmProxy;
      }
      
      public boolean hasIntroductions()
      {
         if (outOfVmProxy == null)
         {
            return false;
         }
         return outOfVmProxy.getIntroducedInterfaces().length > 0;
      }
      
      public void getMixins(HashMap<String, Integer>  intfs, HashMap<String, Integer>  mixinInterfaces, ArrayList<MixinInfo> mixes) throws Exception
      {
         HashSet<String> allInterfaces = new HashSet<String>();
         String[] introducedInterfaces = outOfVmProxy.getIntroducedInterfaces();
         allInterfaces.addAll(Arrays.asList(introducedInterfaces));
         
         Set<String> targetInterfaces = outOfVmProxy.getTargetInterfaces();
         Object[] mixins = outOfVmProxy.getMixins();
         int i = 0;
         for ( ; i < mixins.length ; i++)
         {
            Class clazz = mixins[i].getClass();
            Class[] ifs = clazz.getInterfaces(); 
            ArrayList<String> interfaces = new ArrayList<String>(ifs.length);
            for (Class iface : ifs)
            {
               String name = iface.getName();
               if (name.equals(Serializable.class.getName()) || 
                     name.equals(Externalizable.class.getName()) || 
                     targetInterfaces.contains(name))
               {
                  continue;
               }
               interfaces.add(name);
               allInterfaces.remove(name);
               mixinInterfaces.put(name, i);
            }
            MixinInfo info = new MixinInfo(clazz.getName(), interfaces);
            mixes.add(info);
         }
         
         for (String iface : allInterfaces)
         {
            intfs.put(iface, ++i);
         }
      }
   }
   
   private static class MixinInfo
   {
      String[] interfaces;
      String className;
      String construction;
      
      MixinInfo(InterfaceIntroduction.Mixin mixin)
      {
         this.interfaces = mixin.getInterfaces();
         this.className = mixin.getClassName();
         this.construction = mixin.getConstruction();
      }
      
      MixinInfo(String className, ArrayList<String> interfaces)
      {
         this.className = className;
         this.interfaces = interfaces.toArray(new String[interfaces.size()]);
      }
      
      protected String[] getInterfaces()
      {
         return interfaces;
      }

      protected String getClassName()
      {
         return className;
      }

      protected String getConstruction()
      {
         return construction;
      }
   }
}   
