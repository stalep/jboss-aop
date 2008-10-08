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
package org.jboss.aophelper.ui.run;

import org.jboss.aophelper.ui.run.classpath.RunClasspathTableModel;
import org.jboss.aophelper.ui.run.classpath.RunClasspathTablePane;
import org.jboss.aophelper.ui.run.options.RunOptionsPane;
import org.jboss.aophelper.ui.run.xml.RunXmlTableModel;
import org.jboss.aophelper.ui.run.xml.RunXmlTablePane;

/**
 * A RunMediator.
 * 
 * @author <a href="stalep@gmail.com">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class RunMediator
{
   private static final RunMediator mediator = new RunMediator();

   private RunClasspathTablePane runClasspathTable;
   private RunClasspathTableModel runClasspathModel;
   
   private RunXmlTablePane runXmlTable;
   private RunXmlTableModel runXmlModel;
   
   private RunOptionsPane runOptionsPane;
   private RunOutputPane runOutputPane;
   
   private RunMediator()
   {
      
   }
   
   public static RunMediator instance()
   {
      return mediator;
   }
   
   /**
    * FIXME Comment this
    * 
    * @param tableModel
    */
   public void setRunClasspathModel(RunClasspathTableModel tableModel)
   {
      runClasspathModel = tableModel;
   }
   
   public RunClasspathTableModel getRunClasspathModel()
   {
      return runClasspathModel;
   }

   /**
    * FIXME Comment this
    * 
    * @param runClasspathTablePane
    */
   public void setRunClasspathTable(RunClasspathTablePane classpathTablePane)
   {
      runClasspathTable = classpathTablePane;
   }
   
   public RunClasspathTablePane getRunClasspathTable()
   {
      return runClasspathTable;
   }

   /**
    * Get the runXmlTable.
    * 
    * @return the runXmlTable.
    */
   public RunXmlTablePane getRunXmlTable()
   {
      return runXmlTable;
   }

   /**
    * Set the runXmlTable.
    * 
    * @param runXmlTable The runXmlTable to set.
    */
   public void setRunXmlTable(RunXmlTablePane runXmlTable)
   {
      this.runXmlTable = runXmlTable;
   }

   /**
    * Get the runXmlModel.
    * 
    * @return the runXmlModel.
    */
   public RunXmlTableModel getRunXmlModel()
   {
      return runXmlModel;
   }

   /**
    * Set the runXmlModel.
    * 
    * @param runXmlModel The runXmlModel to set.
    */
   public void setRunXmlModel(RunXmlTableModel runXmlModel)
   {
      this.runXmlModel = runXmlModel;
   }

   /**
    * Get the runOptionsPane.
    * 
    * @return the runOptionsPane.
    */
   public final RunOptionsPane getRunOptionsPane()
   {
      return runOptionsPane;
   }

   /**
    * Set the runOptionsPane.
    * 
    * @param runOptionsPane The runOptionsPane to set.
    */
   public final void setRunOptionsPane(RunOptionsPane runOptionsPane)
   {
      this.runOptionsPane = runOptionsPane;
   }

   /**
    * Get the runOutputPane.
    * 
    * @return the runOutputPane.
    */
   public final RunOutputPane getRunOutputPane()
   {
      return runOutputPane;
   }

   /**
    * Set the runOutputPane.
    * 
    * @param runOutputPane The runOutputPane to set.
    */
   public final void setRunOutputPane(RunOutputPane runOutputPane)
   {
      this.runOutputPane = runOutputPane;
   }
   
   public void refresh()
   {
      runClasspathTable.refresh();
      runXmlTable.refresh();
      runOptionsPane.refresh();
   }
}
