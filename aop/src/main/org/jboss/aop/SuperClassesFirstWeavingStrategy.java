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


import org.jboss.aop.classpool.AOPClassPool;
import org.jboss.aop.instrument.Instrumentor;
import org.jboss.aop.instrument.InstrumentorFactory;
import org.jboss.aop.util.logging.AOPLogger;
import org.jboss.logging.Logger;

import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Generated advisors need to load all the superclasses
 * before we load the actual class.
 *
 * @author <a href="stalep@conduct.no">Stale W. Pedersen</a>
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @version $Revision:
 */
public class SuperClassesFirstWeavingStrategy extends WeavingStrategySupport {

   private static final Logger logger = AOPLogger.getLogger(SuperClassesFirstWeavingStrategy.class);
   
   private boolean verbose = AspectManager.verbose;
	public static final String AOP_PACKAGE = Advised.class.getPackage().getName();

   public byte[] translate(AspectManager manager, String className, ClassLoader loader, byte[] classfileBuffer) throws Exception
	{
		if (isReEntry())
		{
			return null;
		}
		setReEntry();
		manager.transformationStarted = true;
		try
		{
			if (manager.isNonAdvisableClassName(className))
			{
				return null;
			}

			AOPClassPool pool = (AOPClassPool) manager.registerClassLoader(loader);

			CtClassTransformationInfo info = obtainCtClassInfo(pool, className, classfileBuffer);
			CtClass clazz = instrumentClass(manager, pool, info, true);
			if (clazz != null)
			{
				pool.lockInCache(info.getClazz());
            if (AspectManager.debugClasses)
            {
               SecurityActions.debugWriteFile(info.getClazz());
            }
				byte[] rtn = info.getClazz().toBytecode();
				if (AspectManager.getPrune()) info.getClazz().prune();
				return rtn;
			}
			else
			{
				pool.soften(info.getClazz());
			}
			return null;
		}
		catch (Exception ex)
		{
			if (!(ex instanceof NotFoundException))
			{
				if (verbose)
					ex.printStackTrace();
				else
					System.err.println("[error] " + ex.getMessage() +
                                       ".. Do verbose mode if you want full stack trace.");
			}
			throw ex;
		}
		finally
		{
			clearReEntry();
		}
	}

	private CtClassTransformationInfo obtainCtClassInfo(AOPClassPool pool, String className, byte[] classfileBuffer) throws NotFoundException
	{
		try
		{
			return new CtClassTransformationInfo(pool.getLocally(className), className);
		}
		catch (NotFoundException e)
		{
			// todo Bill Burke: this scares the shit out of me, but it must be done
			// I think it will screw up hotdeployment at some time.  Then again, maybe not ;)
			ByteArrayClassPath cp = new ByteArrayClassPath(className, classfileBuffer);
			pool.insertClassPath(cp);
			return new CtClassTransformationInfo(pool.getLocally(className), className);
		}
      catch(Error e)
      {
         return null;
      }
	}

	private CtClass instrumentClass(AspectManager manager, AOPClassPool pool, CtClassTransformationInfo info, boolean isLoadedClass) throws NotFoundException, Exception
	{
		try
		{
			CtClass superClass = info.getClazz().getSuperclass();
			if (superClass != null && !Instrumentor.implementsAdvised(info.getClazz()))
			{
				CtClassTransformationInfo superInfo = new CtClassTransformationInfo(superClass, superClass.getName());

            ClassPool superPool = superClass.getClassPool();
            if (superPool instanceof AOPClassPool)
            {
               AspectManager aspectManager = manager;
               if (manager instanceof Domain && superPool != pool)
               {
                  //We are in a scoped classloader and the superclass is not
                  aspectManager = AspectManager.instance(superPool.getClassLoader());
               }
               instrumentClass(aspectManager, (AOPClassPool)superPool, superInfo, false);
            }
			}

			if (manager.isNonAdvisableClassName(info.getClassName()))
			{
				return null;
			}

			if (info.getClass().isArray())
			{
				if (verbose && logger.isDebugEnabled()) logger.debug("cannot compile, isArray: " + info.getClassName());
				pool.flushClass(info.getClassName());
				return null;
			}
			if (info.getClazz().isInterface())
			{
				if (verbose && logger.isDebugEnabled()) logger.debug("cannot compile, isInterface: " + info.getClassName());
				//pool.flushClass(info.getClassName());
				info.getClazz().prune();
				return null;
			}
			if (info.getClazz().isFrozen())
			{
				if(isAdvised(pool, info.getClazz()))
					return null;
				if (verbose && logger.isDebugEnabled()) logger.debug("warning, isFrozen: " + info.getClassName() + " " + info.getClazz().getClassPool());
				if (!isLoadedClass)
				{
					info = obtainCtClassInfo(pool, info.getClassName(), null);
				}
				else
					return null;
				//info.getClazz().defrost();
			}

         boolean transformed = info.getClazz().isModified();
         if (!transformed)
         {
            ClassAdvisor advisor =
                   AdvisorFactory.getClassAdvisor(info.getClazz(), manager);
				Instrumentor instrumentor = InstrumentorFactory.getInstrumentor(
				      pool,
                  manager,
                  manager.dynamicStrategy.getJoinpointClassifier(),
                  manager.dynamicStrategy.getDynamicTransformationObserver(info.getClazz()));

				if (!Instrumentor.isTransformable(info.getClazz()))
				{
					if (verbose && logger.isDebugEnabled()) logger.debug("cannot compile, implements Untransformable: " + info.getClassName());
					//Flushing the generated invocation classes breaks things further down the line
					//pool.flushClass(info.getClassName());
					return null;
				}

            manager.attachMetaData(advisor, info.getClazz(), true);
            manager.applyInterfaceIntroductions(advisor, info.getClazz());
				transformed = instrumentor.transform(info.getClazz(), advisor);
         }
			if (transformed)
			{
				if (!isLoadedClass )
				{
					info.setTransformed(transformed);
				}
				return info.getClazz();
			}
			return null;
		}
		catch(Exception e)
		{
			throw new RuntimeException("Error converting class ", e);
		}
		finally
		{
		}
	}

	public boolean isAdvised(ClassPool pool, CtClass clazz) throws NotFoundException
	{
	   CtClass[] interfaces = clazz.getInterfaces();
	   CtClass advised = pool.get(AOP_PACKAGE + ".Advised");
	   for (int i = 0; i < interfaces.length; i++)
	   {
	      if (interfaces[i].equals(advised)) return true;
	      if (interfaces[i].getName().equals(AOP_PACKAGE + ".Advised")) return true;
	   }
	   return false;
	}


	private class CtClassTransformationInfo
	{
		boolean transformed;
		CtClass clazz;
		String className;
		private CtClassTransformationInfo(CtClass clazz, String className)
		{
			this.clazz = clazz;
			this.className = className;
		}

		private CtClassTransformationInfo(CtClass clazz, String className, boolean transformed)
		{
			this.clazz = clazz;
			this.className = className;
			this.transformed = transformed;
		}

		private CtClass getClazz()
		{
			return clazz;
		}

		private boolean isTransformed()
		{
			return transformed;
		}

		private void setTransformed(boolean transformed)
		{
			this.transformed = transformed;
		}

		private String getClassName()
		{
			return className;
		}
	}


}
