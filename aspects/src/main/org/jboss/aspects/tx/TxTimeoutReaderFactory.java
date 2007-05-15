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
package org.jboss.aspects.tx;

/**
 * Obtains the correct mechanism to get hold of a transaction timeout.
 * Newer versions of JBoss should use the TransactionTimeoutConfiguration interfsce.
 * For older versions we need to access the TxManager directly
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class TxTimeoutReaderFactory
{
   public static TxTimeoutReader getTxTimeoutReader()
   {
      try
      {
         Class clazz = Class.forName("org.jboss.tm.TransactionTimeoutConfiguration");
         return new TransactionTimeoutConfigurationReader();
      }
      catch (ClassNotFoundException e)
      {
      }
      
      try
      {
         Class clazz = Class.forName("org.jboss.tm.TxManager");
         return new TxManagerTimeOutReader();
      }
      catch (ClassNotFoundException e)
      {
      }
      
      return new NullTransactionTimeoutConfigurationReader();
   }
}
