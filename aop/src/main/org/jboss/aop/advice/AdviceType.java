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
package org.jboss.aop.advice;

import org.jboss.aop.advice.annotation.AdviceMethodFactory;

/**
 * Indicates whether the type of an advice is <i>before</i>, <i>around</i>,
 * <i>after</i>, <i>throwing</i> or <i>finally</i>.
 * 
 * Notice that interceptors are a special type of <i>around</i> advices.
 * 
 * @author  <a href="flavia.rainone@jboss.com">Flavia Rainone</a>
 */
public enum AdviceType
{
   BEFORE("before", AdviceMethodFactory.BEFORE, true),
   AROUND("around", AdviceMethodFactory.AROUND, false),
   AFTER("after", AdviceMethodFactory.AFTER, false),
   THROWING("throwing", AdviceMethodFactory.THROWING, false),
   FINALLY("finally", AdviceMethodFactory.FINALLY, false);
   
   private String description;
   private String accessor;
   private AdviceMethodFactory factory;
   private boolean generatedOnly;
   
   AdviceType(String description, AdviceMethodFactory factory, boolean generatedOnly)
   {
      this.description = description;
      this.accessor = "get" + Character.toUpperCase(description.charAt(0))
         + description.substring(1);
      this.factory = factory;
      this.generatedOnly = generatedOnly;
   }
   
   /**
    * Returns a lower case description of this type.
    */
   public final String getDescription()
   {
      return this.description;
   }
   
   /**
    * Returns an accessor string for this type.
    * 
    * This accessor is built by concatenating <code>"get"</code> with the
    * the {@link #getDescription() description} starting with an upper case.
    * 
    * @return an accessor string for this type.
    */
   public final String getAccessor()
   {
      return this.accessor;
   }
   
   /**
    * An advice method factory for this type. Notice this factory is for use on
    * generated mode only.
    * 
    * @return an advice method factory
    */
   public final AdviceMethodFactory getFactory()
   {
      return this.factory;
   }
   
   /**
    * Indicates if the use of this advice type is restrictive to the generated
    * advisor mode, or if can be used on all instrumentation modes.
    * 
    * @return <code>true</code> only if this advice type must be used on generated
    *         advisor mode.
    */
   public final boolean isGeneratedOnly()
   {
      return this.generatedOnly;
   }
}