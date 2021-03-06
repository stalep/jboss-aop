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
package test.asynchronous;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 */

public class BusinessModelWithStaticCleanupWithParameters
	extends BusinessModel {
	public BusinessModelWithStaticCleanupWithParameters() {}
	public BusinessModelWithStaticCleanupWithParameters(long sleepTime) {
		super(sleepTime);
	}
 	/**
 	 * @@org.jboss.aspects.asynchronous.aspects.jboss.Asynchronous
 	 */
	static public long processBusinessModel4(
		long aSleepTime,
		Parameter parameter) {
		sleep(aSleepTime);
		return aSleepTime;
	}
	static public void cleanup(long sleepTime, Parameter parameter) {
		parameter.bCleanupCalled = true;
	}
}
