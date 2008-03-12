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
package org.jboss.maven.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * A Simple mojo that uses JBoss AOP to run a class with aop weaving enabled.
 * Supports compiletime and loadtime.
 * 
 * @author <a href="stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @version $Revision: 1.1 $
 * @requiresDependencyResolution package
 * @goal run
 * @phase package
 */
public class JBossAOPMojo  extends AbstractMojo
{

   /** 
    * If it is set to true all project dependencies will also be included to the aop classpath
    * 
    * @parameter expression="${includeProjectDependency}" default-value="false"
    */
   private boolean includeProjectDependency;
   
   /** 
    * 
    * @parameter expression="${classPath}" default-value=""
    */
   private String classPath;
   
   /** 
    * 
    * @parameter expression="${aopPaths}" default-value={src/main/resources/jboss-aop.xml}
    */
   private File[] aoppaths;
   
   /** 
    * 
    * @parameter expression="${aopClassPath}" default-value=
    */
   private String aopClassPath;
   
   /** 
    * 
    * @parameter expression="${executable}" default-value=
    * @required
    */
   private String executable;
   
   /** 
    * 
    * @parameter expression="${loadtime}" default-value="false"
    */
   private boolean loadtime;
   
   /**
    * The plugin dependencies.
    *
    * @parameter expression="${plugin.artifacts}"
    * @required
    * @readonly
    */
   private List<Artifact> pluginArtifacts; 
   
   /**
    * @parameter expression="${project}"
    * @required
    * @readonly
    */
   private MavenProject project;
   
   public void execute() throws MojoExecutionException, MojoFailureException
   {
      executeOutOfProcess(createCommandLine());
      
   }
   
   private void executeOutOfProcess(Commandline cl)
   {

      CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
      CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();     
      try
      {
         CommandLineUtils.executeCommandLine( cl, out, err );

         processStream(new BufferedReader( new StringReader( err.getOutput()) ), true);
         processStream(new BufferedReader( new StringReader( out.getOutput()) ), false);
      }
      catch (CommandLineException e)
      {
         e.printStackTrace();
      }
   }
   
   @SuppressWarnings("unchecked")
   private Commandline createCommandLine()
   {
      Commandline cl = new Commandline();
      cl.setExecutable("java");
      cl.setWorkingDirectory(project.getBuild().getOutputDirectory());
      
      if(loadtime)
      {
         String javaagent = getJavaagentPath();
         if(javaagent != null)
         cl.addArguments(new String[] { "-javaagent:"+javaagent});
      }
      cl.addArguments(new String[] { "-cp", createClassPathList()});
      
      String aoppath = getAoppath();
      if(aoppath != null && aoppath.length() > 0)
         cl.addArguments(new String[] { "-Djboss.aop.path="+ aoppath});
      
      if(aopClassPath != null && aopClassPath.length() > 0)
         cl.addArguments(new String[] { "-Djboss.aop.class.path="+ aopClassPath});
         
      cl.addArguments(new String[] { executable});
      
      if(getLog().isDebugEnabled()) 
      { 
         getLog().debug("Executing aop: "+cl.toString()); 
      }
      return cl;
   }
   
   private String getJavaagentPath()
   {
      for(Artifact a : pluginArtifacts)
      {
         if(a.getArtifactId().equals("jboss-aop"))
         {
            return a.getFile().toString();
         }
      }
      return null;
   }
   
   private String createClassPathList()
   {
    //if classPath is set, use only that
      if(classPath != null && classPath.length() > 0)
         return classPath;
      
      StringBuffer sb = new StringBuffer();
      for(Artifact a : pluginArtifacts)
      {
         if(a.getGroupId().startsWith("org.jboss") 
               || a.getGroupId().startsWith("jboss") 
               || a.getGroupId().startsWith("trove"))
         sb.append(a.getFile().toString()).append(File.pathSeparator);
      }

      if(includeProjectDependency)
      {
         for(Object o : project.getDependencyArtifacts())
         {
            if(((Artifact) o).getFile() != null)
            sb.append(((Artifact) o).getFile().toString()).append(File.pathSeparator);
         }
      }

      sb.append(project.getBuild().getOutputDirectory());

      return sb.toString();
   }

   
   private String getAoppath()
   { 
      StringBuffer sb = new StringBuffer();
      if(aoppaths != null)
      {
         for(File aoppath : aoppaths)
         {
            if(aoppath != null)
            {
               if(sb.length() > 0)
                  sb.append(File.pathSeparator);
               sb.append(aoppath.getAbsolutePath());
            }
         }
         return sb.toString();
      }
      else
         return null;
   }

   private void processStream(BufferedReader input, boolean isError)
   {
      String err = null;
      try
      {
         while( (err = input.readLine()) != null)
         {
            if(isError)
               getLog().error(err);
            else
               getLog().info(err);
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

}
