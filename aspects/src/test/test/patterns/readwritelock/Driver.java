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
package test.patterns.readwritelock;
/**
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 */
public class Driver
{

   public static void main(String[] args)
   {
	Account accountWithoutReadWriteLock = new Account(1000);
	testReadWriteLockPattern(accountWithoutReadWriteLock);

	Account accountWithReadWriteLock = new AccountWithReadWriteLock(1000);
	testReadWriteLockPattern(accountWithReadWriteLock);

   }

   public static void testReadWriteLockPattern(Account account)
   {

     new Thread(new ProcessTransaction(account,100)).start();
     new Thread(new ProcessTransaction(account,300)).start();
     new Thread(new ProcessTransaction(account,-150)).start();
     new Thread(new ProcessTransaction(account,-250)).start();

     try
     {
		 Thread.sleep(500);
     }
     catch (Exception e){}
     System.out.println("The balance should be $1000 and it's "+account);

   }


}
