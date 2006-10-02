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

import java.beans.PropertyEditorManager;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.jboss.util.propertyeditor.ClassArrayEditor;
import org.jboss.util.propertyeditor.IntArrayEditor;
import org.jboss.util.propertyeditor.StringArrayEditor;

import javassist.CtClass;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class SecurityActions
{
   private interface InitPropertyEditorsAction
   {
      void initEditors();
      
      InitPropertyEditorsAction PRIVILEGED = new InitPropertyEditorsAction()
      {
         public void initEditors() 
         {
            try
            {
               AccessController.doPrivileged(new PrivilegedExceptionAction()
               {
                  public Object run() throws Exception
                  {
                     doInitEditors();
                     return null;
                  }
               });
            }
            catch (PrivilegedActionException e)
            {
               throw new RuntimeException(e.getException());
            }
         }
      };

      InitPropertyEditorsAction NON_PRIVILEGED = new InitPropertyEditorsAction()
      {
         public void initEditors() 
         {
            doInitEditors();
         }
      };
   }

   private static void doInitEditors()
   {
      String[] currentPath = PropertyEditorManager.getEditorSearchPath();
      int length = currentPath != null ? currentPath.length : 0;
      String[] newPath = new String[length+2];
      System.arraycopy(currentPath, 0, newPath, 2, length);
      // Put the JBoss editor path first
      // The default editors are not very flexible
      newPath[0] = "org.jboss.util.propertyeditor";
      newPath[1] = "org.jboss.mx.util.propertyeditor";
      PropertyEditorManager.setEditorSearchPath(newPath);

      /* Register the editor types that will not be found using the standard
      class name to editor name algorithm. For example, the type String[] has
      a name '[Ljava.lang.String;' which does not map to a XXXEditor name.
      */
      Class strArrayType = String[].class;
      PropertyEditorManager.registerEditor(strArrayType, StringArrayEditor.class);
      Class clsArrayType = Class[].class;
      PropertyEditorManager.registerEditor(clsArrayType, ClassArrayEditor.class);
      Class intArrayType = int[].class;
      PropertyEditorManager.registerEditor(intArrayType, IntArrayEditor.class);
   }


   static void initEditors()
   {
      if (System.getSecurityManager() == null)
      {
         InitPropertyEditorsAction.NON_PRIVILEGED.initEditors();
      }
      else
      {
         InitPropertyEditorsAction.PRIVILEGED.initEditors();
      }
   }
   
}
