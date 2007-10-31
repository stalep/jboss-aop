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
package org.jboss.aop.deployers;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.vfs.spi.deployer.JAXPDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.mx.loading.LoaderRepositoryFactory;
import org.jboss.mx.loading.LoaderRepositoryFactory.LoaderRepositoryConfig;
import org.jboss.virtual.VirtualFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Deployer to read the loader-repository config from a .aop/META-INF/jboss-aop.xml
 * and populate the LoaderRepositoryConfig attachment. The main use-case is when 
 * deploying a standalone .aop file that we want to attach to a scoped deployment.
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AspectAppParsingDeployer extends JAXPDeployer<AspectDeployment>
{
   public AspectAppParsingDeployer()
   {
      super(AspectDeployment.class);
      setSuffix("-aop.xml");
      setStage(DeploymentStages.DESCRIBE);
   }

   protected AspectDeployment parse(VFSDeploymentUnit unit, VirtualFile file, Document document) throws Exception
   {
      if (unit.getTopLevel() == unit)
      {
         // Check for a custom loader-repository for scoping
         NodeList loaders = document.getElementsByTagName("loader-repository");
         if( loaders.getLength() > 0 )
         {
            Element loader = (Element) loaders.item(0);
            try
            {
               LoaderRepositoryConfig config = LoaderRepositoryFactory.parseRepositoryConfig(loader);
               unit.addAttachment(LoaderRepositoryConfig.class, config);
            }
            catch (Exception e)
            {
               throw DeploymentException.rethrowAsDeploymentException("Unable to parse loader repository config", e);
            }
         }
      }
      return null;
   }

}
