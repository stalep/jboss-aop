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

/**
 * Interface implemented by all classes or interfaces
 * that are AOP enabled in the generated-advisor mode.
 * <br>
 * In other words, every class that is generated-advisor weaved by JBoss AOP
 * automatically implements  this interface, which allows the domain retrieval.
 * 
 * @author  <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public interface GeneratedAdvised
{
   /**
    * Returns the domain that is associated with this advised object.
    * This domain contains all the bindings and other AOP configuration that has
    * been used to weave this instance. Changes performed dynamically on the domain
    * will automatically become efective on this advised object.
    * 
    * @return the domain where this advised object belongs.
    */
   AspectManager getDomain();
}