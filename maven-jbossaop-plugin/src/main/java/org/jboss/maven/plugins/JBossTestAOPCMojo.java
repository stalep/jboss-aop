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
package org.jboss.maven.plugins;


/**
 * A Simple mojo that use the JBoss AOP compiler to weave in AOP code
 * based on a jboss-aop.xml file.
 * NOTE: This mojo is not finished and will probably be rewritten or completly removed
 * when i learn more about maven...
 * 
 * Use it by running: mvn jbossaop:aopc
 * 
 * Supported options:
 * - aoppath (default src/main/resources/jboss-aop.xml)
 *  - verbose (default true)
 *  - suppress (default false)
 *  - noopt (default false)
 *  - report (default false)
 *  
 * 
 * @author <a href="mailto:spederse@redhat.com">Stale W. Pedersen</a>
 * @goal compile-test
 * @phase process-test-classes
 * @requiresDependencyResolution
 */
public class JBossTestAOPCMojo extends JBossAOPCMojo
{
   public JBossTestAOPCMojo()
   {
      setTest(true);
   }

}
