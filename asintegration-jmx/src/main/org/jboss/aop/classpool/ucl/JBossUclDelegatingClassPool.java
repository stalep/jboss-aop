/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.classpool.ucl;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.scopedpool.ScopedClassPoolRepository;

import org.jboss.aop.classpool.ClassPoolDomain;
import org.jboss.aop.classpool.DelegatingClassPool;
import org.jboss.mx.loading.RepositoryClassLoader;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBossUclDelegatingClassPool extends DelegatingClassPool
{
   File tmpDir;
   URL tmpURL;
   // For loadClass tmpdir creation for UCL
   protected final Object tmplock = new Object();

   public JBossUclDelegatingClassPool(ClassPoolDomain domain, ClassLoader cl, ClassPool parent, 
         ScopedClassPoolRepository repository, File tmpDir, URL tmpURL)
   {
      super(domain, cl, parent, repository);
      this.tmpDir = tmpDir;
      this.tmpURL = tmpURL;
   }

   public Class<?> toClass(CtClass cc, ClassLoader loader, ProtectionDomain domain)
   throws CannotCompileException
   {
      lockInCache(cc);
      if (getClassLoader() == null || tmpDir == null)
      {
         return super.toClass(cc, loader, domain);
      }
      Class<?> dynClass = null;
      try
      {
         File classFile = null;
         String classFileName = getResourceName(cc.getName());
         // Write the clas file to the tmpdir
         synchronized (tmplock)
         {
            classFile = new File(tmpDir, classFileName);
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

}
