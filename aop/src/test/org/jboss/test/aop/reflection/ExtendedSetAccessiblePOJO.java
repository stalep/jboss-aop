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
package org.jboss.test.aop.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.jboss.test.aop.reflection.setaccessible.DifferentPackageSetAccessiblePOJO;

/**
 * @author Jason Hill
 * @version $Revision: 57688 $
 */
public class ExtendedSetAccessiblePOJO extends DifferentPackageSetAccessiblePOJO
{
   /**
    * Test Accessability of visible and non-visible fields
    */
   public void testSetAccesible() throws Exception
   {
      testSetAccessible(this, "advisedPrivateField",   true);
      // Fix this - still broken with AOP
      //testSetAccessable(this, "defaultAccessField",    true);
      //testSetAccessable(this, "advisedProtectedField", false);
      testSetAccessible(this, "advisedPublicField",    false);
   }
   
   private void testSetAccessible(Object object, String fieldName, boolean illegalAccessExceptionTest) throws Exception
   {
      System.out.println("ExtendedSetAccessiblePOJO.testSetAccessible(" +fieldName + ", " + illegalAccessExceptionTest + ")");
      
      //final Field field = object.getClass().getDeclaredField(fieldName);
      final Field field = object.getClass().getSuperclass().getDeclaredField(fieldName);
      final int modifiers = field.getModifiers();
      
      String fieldAccess;
      
      if(Modifier.isPublic(modifiers))         fieldAccess = "public";
      else if(Modifier.isProtected(modifiers)) fieldAccess = "protected";
      else if(Modifier.isPrivate(modifiers))   fieldAccess = "private";
      else                                     fieldAccess = "default-access";
      
      fieldAccess += "/extended-class";
      //fieldAccess += getClass().getPackage().equals(object.getClass().getPackage())?
      //      "/same-package" : "/different-package";
      
      if(illegalAccessExceptionTest)
      {
         // Verify that an IllegalAccessException is correctly generated
         // When accessing non-visible fields
         SetAccessibleAspect.reset();
         
         String errorGet = "IllegalAccessException should have been thrown because setAccessable had not been called before calling Field.get() on a " + fieldAccess + " field.";
         
         try
         {
            field.get(object);
            throw new RuntimeException(errorGet); 
         }
         catch(IllegalAccessException e)
         {
            // IllegalAccessException is expected because setAccessable has not been called
         }
         
         String errorSet = "IllegalAccessException should have been thrown because setAccessable had not been called before calling Field.set() on a " + fieldAccess + " field."; 
         
         try
         {
            field.set(object, "new value");
            throw new RuntimeException(errorSet); 
         }
         catch(IllegalAccessException e)
         {
            // IllegalAccessException is expected because setAccessable has not been called
         }
         
         // Make the field accessable
         field.setAccessible(true);
      }
      
      // Verify that an IllegalAccessException is not generated
      // when accessing visible fields 
      // or when accessing non-visible fields after calling Field.setAccessable()
      SetAccessibleAspect.reset();
      
      String errorGet = "Could not get " + fieldAccess + " field using Field.get()";
      if(illegalAccessExceptionTest) errorGet += " after calling Field.setAccessable()";
      
      try
      {
         field.get(object);
      }
      catch(IllegalAccessException e)
      {
         throw new RuntimeException(errorGet);
      }
      
      String errorSet = "Could not set " + fieldAccess + " field using Field.set()";
      if(illegalAccessExceptionTest) errorSet += " after calling Field.setAccessable()";
      
      try
      {
         field.set(object, "new value");
      }
      catch(IllegalAccessException e)
      {
         throw new RuntimeException(errorSet);
      }
      
      // Make sure interception actually occurred
      SetAccessibleAspect.validateInterception(fieldAccess);
   }
}
