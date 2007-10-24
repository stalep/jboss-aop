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
package org.jboss.aop.asintegration.jboss5;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.scopedpool.ScopedClassPoolRepository;

import org.jboss.aop.classpool.AOPClassPool;
import org.jboss.classloader.spi.base.BaseClassLoader;
import org.jboss.mx.loading.RepositoryClassLoader;
import org.jboss.virtual.plugins.context.memory.MemoryContextFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JBoss5ClassPool extends AOPClassPool
{
   protected URL tempURL = null;
   // For loadClass tmpdir creation for UCL
   protected final Object tmplock = new Object();
   
   protected JBoss5ClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, URL tmpURL)
   {
      super(cl, src, repository);
      tempURL = tmpURL;
   }

   protected JBoss5ClassPool(ClassPool src, ScopedClassPoolRepository repository)
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

   
   public Class toClass(CtClass cc, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException
   {
      lockInCache(cc);
      final ClassLoader myloader = getClassLoader();
      if (myloader == null || tempURL == null)
      {
         return super.toClass(cc, loader, domain);
      }
      
      try
      {
         String classFileName = getResourceName(cc.getName());
         URL outputURL = new URL(tempURL.toString() + "/" + classFileName);
         //Write the classfile to the temporary url
         synchronized (tmplock)
         {
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(byteout);
            out.write(cc.toBytecode());
            out.flush();
            out.close();
            
            byte[] classBytes = byteout.toByteArray();
            MemoryContextFactory factory = MemoryContextFactory.getInstance();
            factory.putFile(outputURL, classBytes);

            if (myloader instanceof BaseClassLoader)
            {
               //Update check to RealClassLoader once integration project catches up
               ((BaseClassLoader)myloader).clearBlackList(classFileName);
               
            }
            
            Class clazz = myloader.loadClass(cc.getName());

            return clazz;
         }
      }
      catch(Exception e)
      {
       ClassFormatError cfe = new ClassFormatError("Failed to load dyn class: " + cc.getName());
       cfe.initCause(e);
       throw cfe;
      }
   }
   
//   protected boolean isLocalResource(String resourceName)
//   {
//      if (super.isLocalResource(resourceName))
//      {
//         return true;
//      }
//      
//      File file = new File(tempdir, resourceName);
//      if (file.exists())
//      {
//         return true;
//      }
//      
//      return false;
//   }

}
