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
package org.jboss.injbossaop.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.jboss.injbossaop.lib.ExampleValue;

/** 
 *  
 * @author <a href="mailto:kabirkhan@bigfoot.com">Kabir Khan</a>
 *
 */
public class ExampleSessionBean implements SessionBean {

   public void ejbCreate() throws CreateException{}
   
   public ExampleValue getValue(String s)
   {
      System.out.println("*** ExampleSessionBean.getValue()");
      return new ExampleValue(s);
   }
   

   public void setSessionContext(SessionContext ctx) throws EJBException{}
   public void ejbRemove() throws EJBException, RemoteException {}
   public void ejbActivate() throws EJBException, RemoteException {}
   public void ejbPassivate() throws EJBException, RemoteException {}
    
}
