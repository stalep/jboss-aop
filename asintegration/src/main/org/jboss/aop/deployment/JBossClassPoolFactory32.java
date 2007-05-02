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
import java.io.IOException;
import java.net.URL;
import org.jboss.mx.loading.UnifiedClassLoader;
import javassist.ClassPool;
import javassist.scopedpool.ScopedClassPool;
import javassist.scopedpool.ScopedClassPoolFactory;
import javassist.scopedpool.ScopedClassPoolRepository;

/**
 * Comment
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision$
 *
 **/
public class JBossClassPoolFactory32 implements ScopedClassPoolFactory
{
   protected File tmpClassesDir;

   public JBossClassPoolFactory32(File tmpClassesDir) throws IOException
   {
      this.tmpClassesDir = tmpClassesDir;

   }
   public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository)
   {
      try
      {
         File tempdir = createTempDir(cl);
         return new JBossClassPool32(cl, src, repository, tempdir);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository)
   {
      return new JBossClassPool32(src, repository);
   }

   public File createTempDir(ClassLoader cl) throws IOException
   {
      if (!(cl instanceof UnifiedClassLoader)) return null;
      File tempdir = File.createTempFile("ucl", "", tmpClassesDir);
      tempdir.delete();
      tempdir.mkdir();
      tempdir.deleteOnExit();
      UnifiedClassLoader ucl = (UnifiedClassLoader) cl;
      URL tmpURL = tempdir.toURL();
      URL tmpCP = new URL(tmpURL, "?dynamic=true");
      // we may be shutting down.
      if (ucl.getLoaderRepository() != null) ucl.addURL(tmpCP);
      return tempdir;
   }
}
