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
package org.jboss.aspects.security;

import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.security.Principal;
import java.security.AccessController;
import java.security.PrivilegedActionException; 
import java.lang.reflect.UndeclaredThrowableException;

import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
 
import org.jboss.logging.Logger;
import org.jboss.security.SecurityAssociation;
import org.jboss.security.RunAsIdentity; 
import org.jboss.security.SecurityContext;  
import org.jboss.security.plugins.SecurityContextAssociation;
import org.jboss.security.plugins.SecurityContextFactory;

/** A collection of privileged actions for this package
 * @author Scott.Stark@jboss.org
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version $Revison: $
 */
class SecurityActions
{
   private static final Logger log = Logger.getLogger(SecurityActions.class);
   
   interface PrincipalInfoAction
   {
      PrincipalInfoAction PRIVILEGED = new PrincipalInfoAction()
      {
         public void push(final Principal principal, final Object credential,
            final Subject subject)
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     SecurityAssociation.pushSubjectContext(subject, principal, credential);
                     return null;
                  }
               }
            );
         }
         public void pop()
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     SecurityAssociation.popSubjectContext();
                     return null;
                  }
               }
            );
         }

         public Principal getPrincipal()
         {
            return (Principal)AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     return SecurityAssociation.getPrincipal();
                  }
               }
            );
         }

         public void setPrincipal(final Principal principal)
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     SecurityAssociation.setPrincipal(principal);
                     return null;
                  }
               }
            );
         }

         public Principal getCallerPrincipal()
         {
            return (Principal)AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     return SecurityAssociation.getCallerPrincipal();
                  }
               }
            );
         }

         public Object getCredential()
         {
            return AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     return SecurityAssociation.getCredential();
                  }
               }
            );
         }

         public void setCredential(final Object credential)
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     SecurityAssociation.setCredential(credential);
                     return null;
                  }
               }
            );
         }
      };

      PrincipalInfoAction NON_PRIVILEGED = new PrincipalInfoAction()
      {
         public void push(Principal principal, Object credential, Subject subject)
         {
            SecurityAssociation.pushSubjectContext(subject, principal, credential);
         }
         public void pop()
         {
            SecurityAssociation.popSubjectContext();
         }
         public Principal getPrincipal()
         {
            return SecurityAssociation.getPrincipal();
         }
         public void setPrincipal(Principal principal)
         {
            SecurityAssociation.setPrincipal(principal);
         }
         public Principal getCallerPrincipal()
         {
            return SecurityAssociation.getPrincipal();
         }
         public Object getCredential()
         {
            return SecurityAssociation.getCredential();
         }
         public void setCredential(Object credential)
         {
            SecurityAssociation.setCredential(credential);
         }
      };

      void push(Principal principal, Object credential, Subject subject);
      void pop();
      Principal getPrincipal();
      void setPrincipal(Principal principal);
      Principal getCallerPrincipal();
      Object getCredential();
      void setCredential(Object credential);
   }


   interface RunAsIdentityActions
   {
      RunAsIdentityActions PRIVILEGED = new RunAsIdentityActions()
      {
         private final PrivilegedAction peekAction = new PrivilegedAction()
         {
            public Object run()
            {
               return SecurityAssociation.peekRunAsIdentity();
            }
         };

         private final PrivilegedAction popAction = new PrivilegedAction()
         {
            public Object run()
            {
               return SecurityAssociation.popRunAsIdentity();
            }
         };

         public RunAsIdentity peek()
         {
            return (RunAsIdentity)AccessController.doPrivileged(peekAction);
         }

         public void push(final RunAsIdentity id)
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     SecurityAssociation.pushRunAsIdentity(id);
                     return null;
                  }
               }
            );
         }

         public RunAsIdentity pop()
         {
            return (RunAsIdentity)AccessController.doPrivileged(popAction);
         }
      };

      RunAsIdentityActions NON_PRIVILEGED = new RunAsIdentityActions()
      {
         public RunAsIdentity peek()
         {
            return SecurityAssociation.peekRunAsIdentity();
         }

         public void push(RunAsIdentity id)
         {
            SecurityAssociation.pushRunAsIdentity(id);
         }

         public RunAsIdentity pop()
         {
            return SecurityAssociation.popRunAsIdentity();
         }
      };

      RunAsIdentity peek();

      void push(RunAsIdentity id);

      RunAsIdentity pop();
   }

   interface ContextInfoActions
   {
      static final String EX_KEY = "org.jboss.security.exception";
      ContextInfoActions PRIVILEGED = new ContextInfoActions()
      {
         private final PrivilegedAction exAction = new PrivilegedAction()
         {
            public Object run()
            {
               return SecurityAssociation.getContextInfo(EX_KEY);
            }
         };
         public Exception getContextException()
         {
            return (Exception)AccessController.doPrivileged(exAction);
         }
      };

      ContextInfoActions NON_PRIVILEGED = new ContextInfoActions()
      {
         public Exception getContextException()
         {
            return (Exception)SecurityAssociation.getContextInfo(EX_KEY);
         }
      };

      Exception getContextException();
   }

   interface PolicyContextActions
   {
      /** The JACC PolicyContext key for the current Subject */
      static final String SUBJECT_CONTEXT_KEY = "javax.security.auth.Subject.container";
      PolicyContextActions PRIVILEGED = new PolicyContextActions()
      {
         private final PrivilegedExceptionAction exAction = new PrivilegedExceptionAction()
         {
            public Object run() throws Exception
            {
               return (Subject) PolicyContext.getContext(SUBJECT_CONTEXT_KEY);
            }
         };
         public Subject getContextSubject()
            throws PolicyContextException
         {
            try
            {
               return (Subject) AccessController.doPrivileged(exAction);
            }
            catch(PrivilegedActionException e)
            {
               Exception ex = e.getException();
               if( ex instanceof PolicyContextException )
                  throw (PolicyContextException) ex;
               else
                  throw new UndeclaredThrowableException(ex);
            }
         }
      };

      PolicyContextActions NON_PRIVILEGED = new PolicyContextActions()
      {
         public Subject getContextSubject()
            throws PolicyContextException
         {
            return (Subject) PolicyContext.getContext(SUBJECT_CONTEXT_KEY);
         }
      };

      Subject getContextSubject()
         throws PolicyContextException;
   }
   
   static ClassLoader getContextClassLoader()
   {
      return TCLAction.UTIL.getContextClassLoader();
   }

   static void setContextClassLoader(ClassLoader loader)
   {
      TCLAction.UTIL.setContextClassLoader(loader);
   }

   static Principal getCallerPrincipal()
   {
      if (System.getSecurityManager() == null)
      {
         return PrincipalInfoAction.NON_PRIVILEGED.getCallerPrincipal();
      }
      else
      {
         return PrincipalInfoAction.PRIVILEGED.getCallerPrincipal();
      }
   }

   static Principal getPrincipal()
   {
      if (System.getSecurityManager() == null)
      {
         return PrincipalInfoAction.NON_PRIVILEGED.getPrincipal();
      }
      else
      {
         return PrincipalInfoAction.PRIVILEGED.getPrincipal();
      }
   }

   static void setPrincipal(Principal principal)
   {
      if (System.getSecurityManager() == null)
      {
         PrincipalInfoAction.NON_PRIVILEGED.setPrincipal(principal);
      }
      else
      {
         PrincipalInfoAction.PRIVILEGED.setPrincipal(principal);
      }
   }

   static Object getCredential()
   {
      if (System.getSecurityManager() == null)
      {
         return PrincipalInfoAction.NON_PRIVILEGED.getCredential();
      }
      else
      {
         return PrincipalInfoAction.PRIVILEGED.getCredential();
      }
   }
   
   static void setCredential(Object credential)
   {
      if (System.getSecurityManager() == null)
      {
         PrincipalInfoAction.NON_PRIVILEGED.setCredential(credential);
      }
      else
      {
         PrincipalInfoAction.PRIVILEGED.setCredential(credential);
      }
   }

   static void pushSubjectContext(Principal principal, Object credential,
      Subject subject)
   {
      if(System.getSecurityManager() == null)
      {
         PrincipalInfoAction.NON_PRIVILEGED.push(principal, credential, subject);
      }
      else
      {
         PrincipalInfoAction.PRIVILEGED.push(principal, credential, subject);
      }
   }
   static void popSubjectContext()
   {
      if(System.getSecurityManager() == null)
      {
         PrincipalInfoAction.NON_PRIVILEGED.pop();
      }
      else
      {
         PrincipalInfoAction.PRIVILEGED.pop();
      }
   }

   static RunAsIdentity peekRunAsIdentity()
   {
      if(System.getSecurityManager() == null)
      {
         return RunAsIdentityActions.NON_PRIVILEGED.peek();
      }
      else
      {
         return RunAsIdentityActions.PRIVILEGED.peek();
      }
   }

   static void pushRunAsIdentity(RunAsIdentity principal)
   {     
      if(System.getSecurityManager() == null)
      {
         RunAsIdentityActions.NON_PRIVILEGED.push(principal);
      }
      else
      {
         RunAsIdentityActions.PRIVILEGED.push(principal);
      }
   }

   static RunAsIdentity popRunAsIdentity()
   {
      if(System.getSecurityManager() == null)
      {
         return RunAsIdentityActions.NON_PRIVILEGED.pop();
      }
      else
      {
         return RunAsIdentityActions.PRIVILEGED.pop();
      }
   }

   static Exception getContextException()
   {
      if(System.getSecurityManager() == null)
      {
         return ContextInfoActions.NON_PRIVILEGED.getContextException();
      }
      else
      {
         return ContextInfoActions.PRIVILEGED.getContextException();
      }
   }

   static Subject getContextSubject()
      throws PolicyContextException
   {
      if(System.getSecurityManager() == null)
      {
         return PolicyContextActions.NON_PRIVILEGED.getContextSubject();
      }
      else
      {
         return PolicyContextActions.PRIVILEGED.getContextSubject();
      }      
   }

   
   
   
   
   
   interface TCLAction
   {
      class UTIL
      {
         static TCLAction getTCLAction()
         {
            return System.getSecurityManager() == null ? NON_PRIVILEGED : PRIVILEGED;
         }

         static ClassLoader getContextClassLoader()
         {
            return getTCLAction().getContextClassLoader();
         }

         static ClassLoader getContextClassLoader(Thread thread)
         {
            return getTCLAction().getContextClassLoader(thread);
         }

         static void setContextClassLoader(ClassLoader cl)
         {
            getTCLAction().setContextClassLoader(cl);
         }

         static void setContextClassLoader(Thread thread, ClassLoader cl)
         {
            getTCLAction().setContextClassLoader(thread, cl);
         }
      }

      TCLAction NON_PRIVILEGED = new TCLAction()
      {
         public ClassLoader getContextClassLoader()
         {
            return Thread.currentThread().getContextClassLoader();
         }

         public ClassLoader getContextClassLoader(Thread thread)
         {
            return thread.getContextClassLoader();
         }

         public void setContextClassLoader(ClassLoader cl)
         {
            Thread.currentThread().setContextClassLoader(cl);
         }

         public void setContextClassLoader(Thread thread, ClassLoader cl)
         {
            thread.setContextClassLoader(cl);
         }
      };

      TCLAction PRIVILEGED = new TCLAction()
      {
         private final PrivilegedAction getTCLPrivilegedAction = new PrivilegedAction()
         {
            public Object run()
            {
               return Thread.currentThread().getContextClassLoader();
            }
         };

         public ClassLoader getContextClassLoader()
         {
            return (ClassLoader)AccessController.doPrivileged(getTCLPrivilegedAction);
         }

         public ClassLoader getContextClassLoader(final Thread thread)
         {
            return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
            {
               public Object run()
               {
                  return thread.getContextClassLoader();
               }
            });
         }

         public void setContextClassLoader(final ClassLoader cl)
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     Thread.currentThread().setContextClassLoader(cl);
                     return null;
                  }
               }
            );
         }

         public void setContextClassLoader(final Thread thread, final ClassLoader cl)
         {
            AccessController.doPrivileged(
               new PrivilegedAction()
               {
                  public Object run()
                  {
                     thread.setContextClassLoader(cl);
                     return null;
                  }
               }
            );
         }
      };

      ClassLoader getContextClassLoader();

      ClassLoader getContextClassLoader(Thread thread);

      void setContextClassLoader(ClassLoader cl);

      void setContextClassLoader(Thread thread, ClassLoader cl);
   }
   
   
   private static class GetSecurityContextAction implements PrivilegedAction
   {  
      GetSecurityContextAction()
      { 
      }
      public Object run()
      { 
         return SecurityContextAssociation.getSecurityContext(); 
      }
   }
   
   private static class SetSecurityContextAction implements PrivilegedAction
   { 
      private SecurityContext securityContext; 
      SetSecurityContextAction(SecurityContext sc)
      {
         this.securityContext = sc; 
      }
      
      public Object run()
      {
         SecurityContextAssociation.setSecurityContext(securityContext);
         return null;
      }
   }
   
   private static class ClearSecurityContextAction implements PrivilegedAction
   {  
      ClearSecurityContextAction()
      { 
      }
      public Object run()
      {
         SecurityContextAssociation.clearSecurityContext();
         return null;
      }
   }

   static void clearSecurityContext()
   {
      ClearSecurityContextAction action = new ClearSecurityContextAction();
      AccessController.doPrivileged(action);
   }
   
   static SecurityContext getSecurityContext()
   {
      GetSecurityContextAction action = new GetSecurityContextAction();
      return (SecurityContext)AccessController.doPrivileged(action);
   }
   
   static void setSecurityContext(SecurityContext sc)
   {
      SetSecurityContextAction action = new SetSecurityContextAction(sc);
      AccessController.doPrivileged(action);
   }
   
   static void establishSecurityContext(String domain, Principal p, Object cred,
         Subject subject)
   { 
      SecurityContext sc = SecurityContextFactory.createSecurityContext(p, 
            cred, subject, domain); 
      SecurityActions.setSecurityContext(sc);
   }
   
}
