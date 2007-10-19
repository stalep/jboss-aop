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

import java.util.Map;
import java.util.WeakHashMap;

import org.jboss.aop.AspectManager;
import org.jboss.aop.Domain;
import org.jboss.aop.classpool.AOPClassLoaderScopingPolicy;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.stack.MetaDataStack;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class MetaDataClassLoaderScopingPolicy implements AOPClassLoaderScopingPolicy
{
   static Logger log = Logger.getLogger(MetaDataClassLoaderScopingPolicy.class);
   
   //Several loaders may share the same domain
   private Map<ClassLoader, Domain> domainsByLoader = new WeakHashMap<ClassLoader, Domain>();
   
   public Domain getDomain(ClassLoader classLoader, AspectManager parent)
   {
      //Check metadata stack for domain to use
      MetaData metaData = MetaDataStack.peek();
      if (metaData != null)
      {
         //There is metadata on the stack, so check there. No domain in this case means use the main AM
         Domain domain = metaData.getMetaData(Domain.class);
         if (domain != null)
         {
            registerDomain(classLoader, domain);
            return domain;
         }
      }      
      
      //Check the stored domains
      Domain domain = getRegisteredDomain(classLoader);
      if (domain != null)
      {
         return domain;
      }
      
      return null;
   }

   public Domain getTopLevelDomain(AspectManager parent)
   {
      Thread.currentThread().getContextClassLoader();
      return null;
   }

   public boolean isScoped(ClassLoader classLoader)
   {
      //TODO come up with something here?
      return false;
   }

   private synchronized void registerDomain(ClassLoader cl, Domain domain)
   {
      Domain found = domainsByLoader.get(cl);
      if (found == null)
      {
         domainsByLoader.put(cl, domain);
      }
      
      if (found != domain)
      {
         log.warn("Several domains being used for a particular classloader");
      }
   }
   
   private synchronized Domain getRegisteredDomain(ClassLoader cl)
   {
      return domainsByLoader.get(cl);
   }
}
