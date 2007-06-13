package org.jboss.test.aop.reflection;

public class SamePackageSetAccessiblePOJO
{
   public String advisedPublicField = null;

   @SuppressWarnings("unused")
   private String advisedPrivateField     = null;
   protected String advisedProtectedField = null;
   String defaultAccessField              = null;
}
