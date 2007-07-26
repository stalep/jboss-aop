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
package org.jboss.aop.pointcut;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.annotation.AnnotationElement;
import org.jboss.aop.annotation.PortableAnnotationElement;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTException;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTParameter;
import org.jboss.aop.pointcut.ast.ClassExpression;
import org.jboss.aop.util.JavassistMethodHashing;
import org.jboss.aop.util.MethodHashing;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 */
public class Util
{
   public static boolean matchesClassExpr(ClassExpression classExpr, CtClass clazz, Advisor advisor)
   {
      try
      {
         if (classExpr.isAnnotation())
         {
            String sub = classExpr.getOriginal().substring(1);
            if (advisor != null)
            {
               if (advisor.getClassMetaData().hasTag(sub)) return true;
               return advisor.hasAnnotation(clazz, sub);
            }
            else
            {
               return AnnotationElement.isAnyAnnotationPresent(clazz, sub);
            }
         }
         else if (classExpr.isInstanceOf())
         {
            return Util.subtypeOf(clazz, classExpr, advisor);
         }
         else if (classExpr.isTypedef())
         {
            return Util.matchesTypedef(clazz, classExpr, advisor);
         }
         else
         {
            return classExpr.matches(clazz.getName());
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }

   }

   public static boolean matchesClassExpr(ClassExpression classExpr, Class clazz)
   {
      return matchesClassExpr(classExpr, clazz, null);
   }
   
   public static boolean matchesClassExpr(ClassExpression classExpr, Class clazz, Advisor advisor)
   {
      try
      {
         if (classExpr.isAnnotation())
         {
            String sub = classExpr.getOriginal().substring(1);
            if (advisor != null)
            {
               if (advisor.getClassMetaData().hasTag(sub)) return true;
               return advisor.hasAnnotation(clazz, sub);
            }
            else
            {
               return AnnotationElement.isAnyAnnotationPresent(clazz, sub);
            }
         }
         else if (classExpr.isInstanceOf())
         {
            return Util.subtypeOf(clazz, classExpr, advisor);
         }
         else if (classExpr.isTypedef())
         {
            return Util.matchesTypedef(clazz, classExpr, advisor);
         }
         else
         {            
            return(classExpr.matches(clazz.getName()));
         }
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }

   }

   /**
    * @param method Method we are looking for
    * @param target ClassExpression with the class/interface we are looking for the method in
    */
   public static boolean methodExistsInSuperClassOrInterface(Method method, ClassExpression target, boolean exactSuper, Advisor advisor) throws Exception
   {
      long hash = MethodHashing.calculateHash(method);
      boolean exists = methodExistsInSuperClassOrInterface(hash, target, method.getDeclaringClass(), exactSuper);
      if (!exists)
      {
         exists = checkMethodExistsInIntroductions(hash, target, exactSuper, advisor);
      }
      return exists;
   }
   
   private static boolean methodExistsInSuperClassOrInterface(long hash, ClassExpression expr, Class clazz, boolean exactSuper) throws Exception
   {
      if (clazz == null) return false;
      
      if (expr.isAnnotation())
      {
         String sub = expr.getOriginal().substring(1);
          if (AnnotationElement.isAnyAnnotationPresent(clazz, sub))
          {
             if (classHasMethod(clazz, hash, exactSuper)) return true;
          }
       }
       else if (expr.matches(clazz.getName()))
       {
          if (classHasMethod(clazz, hash, exactSuper)) return true;
       }

       Class[] interfaces = clazz.getInterfaces();
       for (int i = 0; i < interfaces.length; i++)
       {
          if (methodExistsInSuperClassOrInterface(hash, expr, interfaces[i], exactSuper)) return true;
       }
       
       if (clazz.isInterface()) return false; // we are done
   
       return methodExistsInSuperClassOrInterface(hash, expr, clazz.getSuperclass(), exactSuper);
   }
   
   /**
    * When using the SystemClassLoader, trying to load up classes when using loadtime weaving gives ClassCircularityErrors,
    * so do this with javassist
    */
   private static boolean checkMethodExistsInIntroductions(long hash, ClassExpression target, boolean exactSuper, Advisor advisor) throws Exception
   {
      if (advisor != null)
      {
         ArrayList intros = advisor.getInterfaceIntroductions();
         if (intros.size() > 0)
         {
            ClassLoader tcl = Thread.currentThread().getContextClassLoader();
            ClassPool pool = advisor.getManager().findClassPool(tcl);
            HashSet doneClasses = new HashSet();
            for (Iterator it = intros.iterator() ; it.hasNext() ; )
            {
               InterfaceIntroduction intro = (InterfaceIntroduction)it.next();
               String[] ifs = intro.getInterfaces();
               for (int i = 0 ; ifs != null && i < ifs.length ; i++)
               {
                  if (!doneClasses.contains(ifs[i]))
                  {
                     doneClasses.add(ifs[i]);
                     if (methodExistsInSuperClassOrInterface(pool, hash, target, ifs[i], exactSuper)) return true;
                  }
               }
               
               ArrayList mixins = intro.getMixins();
               if (mixins != null && mixins.size() > 0)
               {
                  for (Iterator mit = mixins.iterator() ; mit.hasNext() ; )
                  {
                     InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin)mit.next();
                     String[] mifs = mixin.getInterfaces();
                     for (int i = 0 ; mifs != null && i < mifs.length ; i++)
                     {
                        if (!doneClasses.contains(mifs[i]))
                        {
                           doneClasses.add(mifs[i]);
                           if (methodExistsInSuperClassOrInterface(pool, hash, target, mifs[i], exactSuper)) return true;
                        }
                     }
                  }
               }
            }
         }
      }      
      return false;
   }
   
   private static boolean classHasMethod(Class clazz, long hash, boolean exactSuper)throws Exception
   {
      Method m = MethodHashing.findMethodByHash(clazz, hash);
      if (m != null)
      {
         if (exactSuper)
         {
            //MethodHashing will check all super classes so make sure it is on the class itself
            return clazz == m.getDeclaringClass();
         }
         else
         {
            return true;
         }
      }
      
      return false;
   }

   /**
    * @param method CtMethod we are looking for
    * @param target ClassExpression with the class/interface we are looking for the method in
    */
   public static boolean methodExistsInSuperClassOrInterface(CtMethod method, ClassExpression target, boolean exactSuper) throws Exception
   {
      long hash = JavassistMethodHashing.methodHash(method);
      return methodExistsInSuperClassOrInterface(hash, target, method.getDeclaringClass(), exactSuper);
   }
   
   private static boolean methodExistsInSuperClassOrInterface(ClassPool pool, long hash, ClassExpression expr, String className, boolean exactSuper) throws Exception
   {
      CtClass clazz = pool.get(className);
      HashMap map = JavassistMethodHashing.getMethodMap(clazz);
      return methodExistsInSuperClassOrInterface(hash, expr, clazz, exactSuper);
   }
   
   private static boolean methodExistsInSuperClassOrInterface(long hash, ClassExpression expr, CtClass clazz, boolean exactSuper) throws Exception
   {
      if (clazz == null) return false;
      
      if (expr.isAnnotation())
      {
         String sub = expr.getOriginal().substring(1);
         if (AnnotationElement.isAnyAnnotationPresent(clazz, sub))
         {
            if (classHasMethod(clazz, hash, exactSuper)) return true;
         }
      }
      else if (expr.matches(clazz.getName()))
      {
         if (classHasMethod(clazz, hash, exactSuper)) return true;
      }
      
      CtClass[] interfaces = clazz.getInterfaces();
      for (int i = 0; i < interfaces.length; i++)
      {
         if (methodExistsInSuperClassOrInterface(hash, expr, interfaces[i], exactSuper)) return true;
      }
      if (clazz.isInterface()) return false; // we are done
      
      return methodExistsInSuperClassOrInterface(hash, expr, clazz.getSuperclass(), exactSuper);
   }
   
   private static boolean classHasMethod(CtClass clazz, long hash, boolean exactSuper)throws Exception
   {
      HashMap methods = JavassistMethodHashing.getMethodMap(clazz);
      CtMethod m = (CtMethod)methods.get(new Long(hash));
      if (m != null)
      {
         if (exactSuper)
         {
            //If a class, JavassistMethodHashing will check all super classes so make sure it is on the class itself
            return clazz == m.getDeclaringClass();
         }
         else
         {
            return true;
         }
      }
      
      if (clazz.isInterface() && !exactSuper)
      {
         CtClass[] interfaces = clazz.getInterfaces();
         for (int i = 0 ; i < interfaces.length ; i++)
         {
            if (classHasMethod(interfaces[i], hash, exactSuper)) return true;
         }
      }
      return false;
   }
   
   public static boolean subtypeOf(CtClass clazz, ClassExpression instanceOf, Advisor advisor)
   {
      try
      {
	      if (clazz == null) return false;
	      if (instanceOf.isInstanceOfAnnotated())
	      {
            if (clazz.isPrimitive()) return false;
            String sub = instanceOf.getInstanceOfAnnotation().substring(1);
            if (PortableAnnotationElement.isAnyAnnotationPresent(clazz, sub)) return true;
	      }
	      else if (instanceOf.matches(clazz.getName()))
	      {
	         return true;
	      }
	      CtClass[] interfaces = clazz.getInterfaces();
	      for (int i = 0; i < interfaces.length; i++)
	      {
	         if (subtypeOf(interfaces[i], instanceOf, advisor)) return true;
	      }
	      if (clazz.isInterface()) return false; // we are done
	
         if (checkIntroductions(clazz, instanceOf, advisor))
         {
            return true;
         }
         
	      return subtypeOf(clazz.getSuperclass(), instanceOf, advisor);
      } 
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   private static boolean checkIntroductions(CtClass clazz, ClassExpression instanceOf, Advisor advisor)
   {
      try
      {
         if (advisor != null)
         {
            // FIXME ClassLoader - why should the class be visible from the context classloader?
            ClassLoader cl = SecurityActions.getContextClassLoader();
            ArrayList intros = advisor.getInterfaceIntroductions();
            if (intros.size() > 0)
            {
               for (Iterator itIntro = intros.iterator() ; itIntro.hasNext() ; )
               {
                  InterfaceIntroduction intro = (InterfaceIntroduction)itIntro.next();
                  String[] introductions = intro.getInterfaces();
                  if (introductions != null)
                  {
                     for (int i = 0 ; i < introductions.length ; i++)
                     {
                        Class iface = cl.loadClass(introductions[i]);
                        if (subtypeOf(iface, instanceOf, advisor)) return true;
                     }
                  }
                  ArrayList mixins = intro.getMixins();
                  if (mixins.size() > 0)
                  {
                     for (Iterator itMixin = mixins.iterator() ; itMixin.hasNext() ; )
                     {
                        InterfaceIntroduction.Mixin mixin = (InterfaceIntroduction.Mixin)itMixin.next();
                        String[] mixinInterfaces = mixin.getInterfaces();
                        if (mixinInterfaces != null)
                        {
                           for (int i = 0 ; i < mixinInterfaces.length ; i++)
                           {
                              Class iface = cl.loadClass(mixinInterfaces[i]);
                              if (subtypeOf(iface, instanceOf, advisor)) return true;                              
                           }
                        }
                     }
                  }
               }
            }
         }
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
      
      return false;
   }
   
   public static boolean subtypeOf(Class clazz, ClassExpression instanceOf, Advisor advisor)
   {
      return MatcherStrategy.getMatcher(advisor).subtypeOf(clazz, instanceOf, advisor);
   }
   
   public static boolean has(CtClass target, ASTMethod method, Advisor advisor)
   {
      return has(target, method, advisor, true);
   }
   
   public static boolean has(CtClass target, ASTMethod method, Advisor advisor, boolean checkSuper)
   {
      try
      {
         CtMethod[] methods = target.getDeclaredMethods();
         for (int i = 0; i < methods.length; i++)
         {
            MethodMatcher matcher = new MethodMatcher(advisor, methods[i], null);
            if (matcher.matches(method).booleanValue()) return true;
         }
         
         if (checkSuper)
         {
            CtClass superClass = target.getSuperclass();
            if (superClass != null) return has(superClass, method, advisor, checkSuper);
         }
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      return false;
   }

   public static boolean has(CtClass target, ASTField field, Advisor advisor)
   {
      return has(target, field, advisor, true);
   }
   
   public static boolean has(CtClass target, ASTField field, Advisor advisor, boolean checkSuper)
   {
      try
      {
         CtField[] fields = target.getDeclaredFields();
         for (int i = 0; i < fields.length; i++)
         {
            FieldGetMatcher matcher = new FieldGetMatcher(advisor, fields[i], null);
            if (((Boolean) field.jjtAccept(matcher, null)).booleanValue()) return true;
         }
         
         if (checkSuper)
         {
            CtClass superClass = target.getSuperclass();
            if (superClass != null) return has(superClass, field, advisor, checkSuper);
         }
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      return false;
   }

   public static boolean has(CtClass target, ASTConstructor con, Advisor advisor)
   {
      try
      {
         CtConstructor[] cons = target.getDeclaredConstructors();
         for (int i = 0; i < cons.length; i++)
         {
            ConstructorMatcher matcher = new ConstructorMatcher(advisor, cons[i], null);
            if (matcher.matches(con).booleanValue()) return true;
         }
      }
      catch (NotFoundException e)
      {
         throw new RuntimeException(e);  //To change body of catch statement use Options | File Templates.
      }
      return false;
   }

   public static boolean has(Class target, ASTMethod method, Advisor advisor)
   {
      return has(target, method, advisor, true);
   }
   
   public static boolean has(Class target, ASTMethod method, Advisor advisor, boolean checkSuper)
   {
      Method[] methods = advisor.getAllMethods();
      if (methods == null)
         methods = target.getDeclaredMethods();
      for (int i = 0; i < methods.length; i++)
      {
         MethodMatcher matcher = new MethodMatcher(advisor, methods[i], null);
         if (matcher.matches(method).booleanValue()) return true;
      }
      
      if (checkSuper)
      {
         Class superClass = target.getSuperclass();
         if (superClass != null) return has(superClass, method, advisor, checkSuper);
      }
      return false;
   }

   public static boolean has(Class target, ASTField field, Advisor advisor)
   {
      return has(target, field, advisor, true);
   }
   
   public static boolean has(Class target, ASTField field, Advisor advisor, boolean checkSuper)
   {
      Field[] fields = target.getDeclaredFields();
      for (int i = 0; i < fields.length; i++)
      {
         FieldGetMatcher matcher = new FieldGetMatcher(advisor, fields[i], null);
         if (((Boolean) field.jjtAccept(matcher, null)).booleanValue()) return true;
      }
      
      if (checkSuper)
      {
         Class superClass = target.getSuperclass();
         if (superClass != null) return has(superClass, field, advisor, checkSuper);
      }
      return false;
   }

   public static boolean has(Class target, ASTConstructor con, Advisor advisor)
   {
      Constructor[] cons = target.getDeclaredConstructors();
      for (int i = 0; i < cons.length; i++)
      {
         ConstructorMatcher matcher = new ConstructorMatcher(advisor, cons[i], null);
         if (matcher.matches(con).booleanValue()) return true;
      }
      return false;
   }

   public static boolean matchesTypedef(CtClass clazz, ClassExpression classExpr, Advisor advisor)
   {
      String original = classExpr.getOriginal();
      String typedefName = original.substring("$typedef{".length(), original.lastIndexOf("}"));
      AspectManager manager = (advisor != null) ? advisor.getManager() : AspectManager.instance(); 
      Typedef typedef = manager.getTypedef(typedefName);
      if (typedef == null) return false;
      return typedef.matches(advisor, clazz);
   }

   public static boolean matchesTypedef(Class clazz, ClassExpression classExpr, Advisor advisor)
   {
      String original = classExpr.getOriginal();
      String typedefName = original.substring("$typedef{".length(), original.lastIndexOf("}"));
      AspectManager manager = (advisor != null) ? advisor.getManager() : AspectManager.instance(); 
      Typedef typedef = manager.getTypedef(typedefName);
      if (typedef == null) return false;
      return typedef.matches(advisor, clazz);
   }

   public static boolean matchModifiers(ASTAttribute need, int have)
   {
      if (Modifier.isAbstract(need.attribute) && need.not) return !Modifier.isAbstract(have);
      if (Modifier.isAbstract(need.attribute) && !need.not) return Modifier.isAbstract(have);
      if (Modifier.isFinal(need.attribute) && need.not) return !Modifier.isFinal(have);
      if (Modifier.isFinal(need.attribute) && !need.not) return Modifier.isFinal(have);
      if (Modifier.isInterface(need.attribute) && need.not) return !Modifier.isInterface(have);
      if (Modifier.isInterface(need.attribute) && !need.not) return Modifier.isInterface(have);
      if (Modifier.isNative(need.attribute) && need.not) return !Modifier.isNative(have);
      if (Modifier.isNative(need.attribute) && !need.not) return Modifier.isNative(have);
      if (Modifier.isPrivate(need.attribute) && need.not) return !Modifier.isPrivate(have);
      if (Modifier.isPrivate(need.attribute) && !need.not) return Modifier.isPrivate(have);
      if (Modifier.isProtected(need.attribute) && need.not) return !Modifier.isProtected(have);
      if (Modifier.isProtected(need.attribute) && !need.not) return Modifier.isProtected(have);
      if (Modifier.isPublic(need.attribute) && need.not) return !Modifier.isPublic(have);
      if (Modifier.isPublic(need.attribute) && !need.not) return Modifier.isPublic(have);
      if (Modifier.isStatic(need.attribute) && need.not) return !Modifier.isStatic(have);
      if (Modifier.isStatic(need.attribute) && !need.not) return Modifier.isStatic(have);
      if (Modifier.isStrict(need.attribute) && need.not) return !Modifier.isStrict(have);
      if (Modifier.isStrict(need.attribute) && !need.not) return Modifier.isStrict(have);
      if (Modifier.isSynchronized(need.attribute) && need.not) return !Modifier.isSynchronized(have);
      if (Modifier.isSynchronized(need.attribute) && !need.not) return Modifier.isSynchronized(have);
      if (Modifier.isTransient(need.attribute) && need.not) return !Modifier.isTransient(have);
      if (Modifier.isTransient(need.attribute) && !need.not) return Modifier.isTransient(have);
      if (Modifier.isVolatile(need.attribute) && need.not) return !Modifier.isVolatile(have);
      if (Modifier.isVolatile(need.attribute) && !need.not) return Modifier.isVolatile(have);

      return true;
   }

   /**
    * @param nodeExceptions  ArrayList of ASTException entries for a given ASTMethod or ASTConstructor
    * @param foundExceptions Array of Exceptions found for a method/constructor
    */
   public static boolean matchExceptions(ArrayList nodeExceptions, CtClass[] foundExceptions)
   {
      if (nodeExceptions.size() > foundExceptions.length) return false;
      for (Iterator it = nodeExceptions.iterator(); it.hasNext();)
      {
         boolean found = false;
         ASTException ex = (ASTException) it.next();
         for (int i = 0; i < foundExceptions.length; i++)
         {
            if (ex.getType().matches(foundExceptions[i].getName()))
            {
               found = true;
               break;
            }
         }

         if (!found) return false;
      }

      return true;
   }

   /**
    * @param nodeExceptions  ArrayList of ASTException entries for a given ASTMethod or ASTConstructor
    * @param foundExceptions Array of Exceptions found for a method/constructor
    */
   public static boolean matchExceptions(ArrayList nodeExceptions, Class[] foundExceptions)
   {
      if (nodeExceptions.size() > foundExceptions.length) return false;
      for (Iterator it = nodeExceptions.iterator(); it.hasNext();)
      {
         boolean found = false;
         ASTException ex = (ASTException) it.next();
         for (int i = 0; i < foundExceptions.length; i++)
         {
            if (ex.getType().matches(foundExceptions[i].getName()))
            {
               found = true;
               break;
            }
         }

         if (!found) return false;
      }

      return true;
   }

   public static boolean matchesParameters(Advisor advisor, ASTMethod node, CtMethod ctMethod)
   {
      if (node.isAnyParameters()) return true;
      try
      {
         return Util.matchesParameters(advisor, node.hasAnyZeroOrMoreParameters(), node.getParameters(), ctMethod.getParameterTypes());
      }
      catch (NotFoundException e)
      {
         // AutoGenerated
         throw new RuntimeException(e);
      }
   }

   public static boolean matchesParameters(Advisor advisor, ASTConstructor node, CtConstructor ctConstructor)
   {
      int i = 0;
      if (node.isAnyParameters()) return true;
      try
      {
         CtClass[] params = ctConstructor.getParameterTypes();
         return Util.matchesParameters(advisor, node.hasAnyZeroOrMoreParameters(), node.getParameters(), params);
      }
      catch (NotFoundException e)
      {
         // AutoGenerated
         throw new RuntimeException(e);
      }
   }

   public static boolean matchesParameters(Advisor advisor, ASTMethod node, Method method) 
   {
      if (node.isAnyParameters()) return true;
      return matchesParameters(advisor, node.hasAnyZeroOrMoreParameters(), node.getParameters(), method.getParameterTypes());
   }

   public static boolean matchesParameters(Advisor advisor, ASTConstructor node, Constructor con) 
   {
      if (node.isAnyParameters()) return true;
      
      Class[] params = con.getParameterTypes();
      
      return matchesParameters(advisor, node.hasAnyZeroOrMoreParameters(), node.getParameters(), con.getParameterTypes());
   }

   private static boolean matchesParameters(Advisor advisor, boolean hasAnyZeroOrMoreParameters, ArrayList parameters, Class[] params)
   {
      RefParameterMatcher matcher = new RefParameterMatcher(advisor, parameters, params);
      return matcher.matches();
   }

   private static boolean matchesParameters(Advisor advisor, boolean hasAnyZeroOrMoreParameters, ArrayList parameters, CtClass[] params)
   {
      CtParameterMatcher matcher = new CtParameterMatcher(advisor, parameters, params);
      return matcher.matches();
   }
   
   private static class ParameterMatcher
   {
      Advisor advisor;
      ArrayList astParameters;
      final long paramsLength;
      int asti;
      int actuali;
      
      ParameterMatcher(Advisor advisor, ArrayList parameters, Object[] params)
      {
         this.advisor = advisor;
         this.astParameters = parameters;
         paramsLength = params.length;
      }

      boolean matches()
      {
         return matches(0, 0);
      }
      
      private boolean matches(int ast, int actual)
      {
         boolean match = true;
         for ( ; match && ast < astParameters.size() && actual < paramsLength  ; ast++)
         {
            if (isAnyZeroOrMoreParameters(ast))
            {
               asti = ast;
               actuali = actual;
               match = wildcard();//ast, cls);
               ast = asti;
               actual = actuali;
               ast--;
            }
            else
            {
               match = doMatch(ast, actual);
               actual++;
            }
         }
         
         while (match && ast < astParameters.size() && isAnyZeroOrMoreParameters(ast))
         {
            ast++;
         }
         return (match && ast == astParameters.size() && paramsLength == actual);
      }
      
      private boolean isAnyZeroOrMoreParameters(int index)
      {
         return ((ASTParameter)astParameters.get(index)).isAnyZeroOrMoreParameters();
      }
      
      boolean doMatch(int astIndex, int actualIndex)
      {
         //Overridden by sub classes
         return false;
      }
      
      private boolean wildcard()
      {
         boolean match = true;
         asti++;
         
         while (actuali < paramsLength && asti < astParameters.size() && isAnyZeroOrMoreParameters(asti))
         {
            asti++;
         }

         while (asti < astParameters.size() && isAnyZeroOrMoreParameters(asti))
         {
            asti++;
         }

         if (actuali == paramsLength && asti < astParameters.size())
            return false;
         if (actuali == paramsLength && asti == astParameters.size())
            return true; 
         else
         {
            if (!matches(asti, actuali))
            {
               do
               {
                  actuali++;
                  while (actuali < paramsLength && asti < astParameters.size() && !doMatch(asti, actuali))
                  {
                     actuali++;
                  }
                  
               }while ( (actuali < paramsLength) ? !(match = matches(asti, actuali)) : false);
               
            }
            if (actuali == paramsLength && asti == astParameters.size())
            {
               match = true;
            }
            return match;
         }
      }
   }
   
   private static class RefParameterMatcher extends ParameterMatcher
   {
      Class[] params;
      public RefParameterMatcher(Advisor advisor, ArrayList parameters, Class[] params)
      {
         super(advisor, parameters, params);
         this.params = params;
      }
      
      boolean doMatch(int astIndex, int actualIndex)
      {
         ASTParameter ast = (ASTParameter) astParameters.get(astIndex);
         ClassExpression exp = ast.getType();

         if (exp.isSimple())
         {
            String asString = ClassExpression.simpleType(params[actualIndex]);
            if (!exp.matches(asString)) return false;
         }
         else
         {
            if (!Util.matchesClassExpr(exp, params[actualIndex], advisor)) return false;
         }
         
         return true;
      }
   }

   private static class CtParameterMatcher extends ParameterMatcher
   {
      CtClass[] params;
      public CtParameterMatcher(Advisor advisor, ArrayList parameters, CtClass[] params)
      {
         super(advisor, parameters, params);
         this.params = params;
      }
      
      boolean doMatch(int astIndex, int actualIndex)
      {
         ASTParameter ast = (ASTParameter) astParameters.get(astIndex);
         ClassExpression exp = ast.getType();
         if (!matchesClassExpr(exp, params[actualIndex], advisor)) return false;
         return true;
      }
   }
}
