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
package org.jboss.aop.deployment;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.ProtectionDomain;

import org.jboss.aop.classpool.AOPClassPool;
import org.jboss.mx.loading.RepositoryClassLoader;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * Comment
 *
 * @deprecated TODO JBAOP-107 Need a different version for the JBoss5 classloader 
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author adrian@jboss.org
 * @version $Revision$
 */
@Deprecated
public class JBossClassPool extends AOPClassPool
{
   /**
    * Used for dynamically created classes (see loadClass(String, byte[]), ClassLoader)
    */
   protected File tempdir = null;
   protected URL tempURL = null;
   // For loadClass tmpdir creation for UCL
   protected final Object tmplock = new Object();
   
   protected JBossClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, File tmp, URL tmpURL)
   {
      super(cl, src, repository);
      tempdir = tmp;
      tempURL = tmpURL;
   }

   protected JBossClassPool(ClassPool src, ScopedClassPoolRepository repository)
   {
      super(src, repository);
   }

   public boolean isUnloadedClassLoader()
   {
      if (getClassLoader() instanceof RepositoryClassLoader)
      {
         RepositoryClassLoader rcl = (RepositoryClassLoader) getClassLoader();
         return rcl.getLoaderRepository() == null;
      }
      return false;
   }

   public Class toClass(CtClass cc, ClassLoader loader, ProtectionDomain domain)
   throws CannotCompileException
   {
      lockInCache(cc);
      if (getClassLoader() == null || tempdir == null)
      {
         return super.toClass(cc, loader, domain);
      }
      Class dynClass = null;
      try
      {
         File classFile = null;
         String classFileName = getResourceName(cc.getName());
         // Write the clas file to the tmpdir
         synchronized (tmplock)
         {
            classFile = new File(tempdir, classFileName);
            File pkgDirs = classFile.getParentFile();
            pkgDirs.mkdirs();
            FileOutputStream stream = new FileOutputStream(classFile);
            stream.write(cc.toBytecode());
            stream.flush();
            stream.close();
            classFile.deleteOnExit();
         }
         // We have to clear Blacklist caches or the class will never
         // be found
         //((UnifiedClassLoader)dcl).clearBlacklists();
         // To be backward compatible
         RepositoryClassLoader rcl = (RepositoryClassLoader) getClassLoader();
         rcl.clearClassBlackList();
         rcl.clearResourceBlackList();

         // Now load the class through the cl
         dynClass = getClassLoader().loadClass(cc.getName());
      }
      catch (Exception ex)
      {
         ClassFormatError cfe = new ClassFormatError("Failed to load dyn class: " + cc.getName());
         cfe.initCause(ex);
         throw cfe;
      }

      return dynClass;
   }

   protected boolean isLocalResource(String resourceName)
   {
      if (super.isLocalResource(resourceName))
      {
         return true;
      }
      
      File file = new File(tempdir, resourceName);
      if (file.exists())
      {
         return true;
      }
      
      return false;
   }
}
