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
import org.jboss.aspects.asynchronous.aspects.AsynchronousFacade;
import org.jboss.aspects.asynchronous.AsynchronousTask;
/**
 *
 * @version <tt>$Revision$</tt>
 * @author  <a href="mailto:chussenet@yahoo.com">{Claude Hussenet Independent Consultant}</a>.
 */
public class BusinessThread implements Runnable {
	private BusinessModel businessModel;
	private long sleep;
	private long parameter;
	private String txt;
	public Object result;
	public BusinessThread(
		BusinessModel aBusinessModel,
		long parameter,
		long sleep,
		String txt) {
		this.businessModel = aBusinessModel;
		this.sleep = sleep;
		this.parameter = parameter;
		this.txt = txt;
		
	}
	public void run() {
		AsynchronousFacade asynchronousFacade =
			(AsynchronousFacade)businessModel;
		long res1 = businessModel.processBusinessModel2(parameter);
		BusinessModel.sleep(sleep);
		AsynchronousTask asynchronousTask1 =
			asynchronousFacade.getAsynchronousTask();
		if (!asynchronousFacade.isDone(asynchronousTask1))
			asynchronousFacade.waitForResponse(asynchronousTask1);
			result =asynchronousFacade.getReturnValue(asynchronousTask1);
 
	}
}
