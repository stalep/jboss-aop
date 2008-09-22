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
package org.jboss.aophelper.util;

import java.io.File;
import java.io.IOException;

import org.jboss.aophelper.core.AopHandler;
import org.jboss.aophelper.core.AopRun;

/**
 * A AopRunCommand.
 * 
 * @author <a href="stalep@gmail.com">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 */
public class AopRunCommand
{


   public String execute()
   {
      String execute = setup();
      try
      {
         String[] emptyArray = new String[0];
         Process process = Runtime.getRuntime().exec( execute, emptyArray,  new File(AopHandler.instance().getRun().getWorkingdir()));

         return CommandUtil.analyzeProcess(process);
      }
      catch ( IOException ie )
      {
         System.out.println("Error while aopcompiling: "+ie.getMessage());
         ie.printStackTrace();
       
         return null;
      }
   }
   

   private String setup()
   {
      AopRun runOptions = AopHandler.instance().getRun();
      
      StringBuilder execute = new StringBuilder();
      execute.append("java ");
      if(runOptions.isLoadtime())
         execute.append(getLoadtimePath());
      execute.append(" -cp ").append(CommandUtil.getAopPaths());
//      execute.append(" org.jboss.aop.standalone.Compiler ");
      execute.append(" ");
      if(runOptions.isVerbose())
         execute.append("-Djboss.aop.verbose=true ");
      if(runOptions.isNoopt())
         execute.append("-Djboss.aop.noopt=true ");
      if(runOptions.isSuppress())
         execute.append("-Djboss.aop.suppress=true ");
      
      if(runOptions.getAopXml().size() > 0)
      {
         execute.append("-Djboss.aop.path=");
         String pathSep = System.getProperty("path.separator");
         StringBuffer xmlPaths = new StringBuffer();
         for(String xml : runOptions.getAopXml())
         {
            if(xmlPaths.length() > 0)
               xmlPaths.append(pathSep);
            System.out.println("appending xml: "+xml);
            xmlPaths.append(xml);
         }
         execute.append(xmlPaths.toString()).append(" ");
      }
      
      execute.append(calculateCorrectExecutionClass(runOptions));
      
//      execute.append("\"");
      System.out.println("EXECUTING: "+execute.toString());
      
      
      return execute.toString();
      
   }
   
   private String calculateCorrectExecutionClass(AopRun runOptions)
   {
      String workingdir = runOptions.getWorkingdir();
      String exeClass = runOptions.getExecutionClass();
      
      if(exeClass.startsWith(workingdir))
      {
        String rest = exeClass.split(workingdir)[1];
        if(rest.startsWith("/"))
          rest = rest.substring(1);
        rest = rest.replaceAll("/",".");
        int index = rest.lastIndexOf(".class");
        rest = rest.substring(0,index);
        System.out.println("rest="+rest);

        return rest;
      }
      else
         return null;
      
   }
   
   private String getLoadtimePath()
   {
      String pathSeparator = System.getProperty("path.separator");
      String[] paths = System.getProperty("java.class.path").split(pathSeparator);
      for(String p : paths)
      {
         if(p.contains("jboss-aop-"))
            return "-javaagent:"+p;
      }
      return null;
   }
}
