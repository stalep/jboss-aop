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
package org.jboss.aophelper.ui.compile;

import org.jboss.aophelper.ui.compile.classpath.ClasspathTableModel;
import org.jboss.aophelper.ui.compile.classpath.ClasspathTablePane;
import org.jboss.aophelper.ui.compile.options.CompileOptionsPane;
import org.jboss.aophelper.ui.compile.xml.XmlTableModel;
import org.jboss.aophelper.ui.compile.xml.XmlTablePane;

/**
 * A CompileMediator.
 * 
 * @author <a href="stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class CompileMediator
{
   private static final CompileMediator mediator = new CompileMediator();
   
   private ClasspathTablePane classpathTable;
   private ClasspathTableModel classpathModel;
  
   private XmlTablePane xmlTable;
   private XmlTableModel xmlModel;
   
   private CompileOptionsPane compileOptionsPane;
   private OutputPane outputPane;
   
   private CompileMediator()
   {
      
   }
   
   public static CompileMediator instance()
   {
      return mediator;
   }
   
   public void setClasspathTable(ClasspathTablePane table)
   {
      this.classpathTable = table;
   }
   
   public ClasspathTablePane getClasspathTable()
   {
      return classpathTable;
   }

   /**
    * Get the classpathModel.
    * 
    * @return the classpathModel.
    */
   public ClasspathTableModel getClasspathModel()
   {
      return classpathModel;
   }

   /**
    * Set the classpathModel.
    * 
    * @param classpathModel The classpathModel to set.
    */
   public void setClasspathModel(ClasspathTableModel classpathModel)
   {
      this.classpathModel = classpathModel;
   }

   /**
    * 
    * @param tableModel
    */
   public void setXmlModel(XmlTableModel tableModel)
   {
      xmlModel = tableModel;
   }
   
   public XmlTableModel getXmlModel()
   {
      return xmlModel;
   }
   
   public void setXmlTable(XmlTablePane tableP)
   {
      xmlTable = tableP;
   }
   
   public XmlTablePane getXmlTable()
   {
      return xmlTable;
   }

   /**
    * Get the compileOptionsPane.
    * 
    * @return the compileOptionsPane.
    */
   public CompileOptionsPane getCompileOptionsPane()
   {
      return compileOptionsPane;
   }

   /**
    * Set the compileOptionsPane.
    * 
    * @param compileOptionsPane The compileOptionsPane to set.
    */
   public void setCompileOptionsPane(CompileOptionsPane compileOptionsPane)
   {
      this.compileOptionsPane = compileOptionsPane;
   }

   /**
    * Get the outputPane.
    * 
    * @return the outputPane.
    */
   public OutputPane getOutputPane()
   {
      return outputPane;
   }

   /**
    * Set the outputPane.
    * 
    * @param outputPane The outputPane to set.
    */
   public void setOutputPane(OutputPane outputPane)
   {
      this.outputPane = outputPane;
   }



}
