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
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * A Simple mojo that use the JBoss AOP compiler to weave in AOP code
 * based on a jboss-aop.xml file.
 * 
 * Use it by running: mvn jbossaop:compile
 * 
 * Supported options:
 * - aoppaths[] (default src/main/resources/jboss-aop.xml)
 *  - verbose (default true)
 *  - suppress (default false)
 *  - noopt (default false)
 *  - report (default false)
 *  - includeProjectDependency (default false)
 * 
 * @author <a href="mailto:stale.pedersen@jboss.org">Stale W. Pedersen</a>
 * @goal compile
 * @phase process-classes
 * @requiresDependencyResolution compile
 */
public class JBossAOPCMojo extends AbstractMojo
{

   /**
    * Set the verbose level of the compiler
    *
    * @parameter expression="${verbose}" default-value="true"
    */
   private boolean verbose;

   /** 
    * 
    * 
    * @parameter expression="${supress}" default-value="true"
    */
   private boolean suppress;
   
   /** 
    * 
    * 
    * @parameter expression="${noopt}" default-value="false"
    */
   private boolean noopt;

   /** 
    * 
    * 
    * @parameter expression="${report}" default-value="false"
    */
   private boolean report;

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
    * @parameter expression="${aopClassPath}" default-value=""
    */
   private String aopClassPath;
   
   /**
    * Set this to specify certain classes that will be weaved. This prevents the parser to check
    * all the classes in eg target/classes.
    * 
    * @parameter
    */
   private String[] includes;
   
   private boolean test;

   /**
    * @parameter expression="${project}"
    * @readonly
    */
   private MavenProject project;
   
   /**
    * The plugin dependencies.
    *
    * @parameter expression="${plugin.artifacts}"
    * @required
    * @readonly
    */
   private List<Artifact> pluginArtifacts; 

   public void execute() throws MojoExecutionException
   {
      compileOutOfProcess(createCommandLine());
   }

   private String createClassPathList()
   {
      //if classPath is set, use only that
      if(getClassPath() != null && getClassPath().length() > 0)
         return getClassPath();
      
      StringBuffer sb = new StringBuffer();
      for(Artifact a : pluginArtifacts)
      {
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

      if(isTest())
         sb.append(project.getBuild().getTestOutputDirectory()).append(File.pathSeparator);
      
         sb.append(project.getBuild().getOutputDirectory());

      return sb.toString();
   }


   @SuppressWarnings("unchecked")
   private Commandline createCommandLine()
   {
      Commandline cl = new Commandline();
      cl.setExecutable("java");
      cl.addArguments(new String[] { "-cp", createClassPathList()});
      cl.addArguments(new String[] { "org.jboss.aop.standalone.Compiler"});
      if(isVerbose())
         cl.addArguments(new String[] { "-verbose"});
      if(isSuppress())
         cl.addArguments(new String[] { "-suppress"});
      if(isNoopt())
         cl.addArguments(new String[] { "-noopt"});
      if(hasReport())
         cl.addArguments(new String[] { "-report"});
      
      if(getAopClassPath() != null && getAopClassPath().length() > 0)
         cl.addArguments(new String[] { "-aopclasspath", getAopClassPath()});
      
      String aoppath = getAoppath();
      if(aoppath != null && aoppath.length() > 0)
         cl.addArguments(new String[] { "-aoppath", aoppath});
      
      if(includes != null && includes.length > 0)
      {
         for(String include : includes)
         {
            File f = null;
            if(isTest())
               f = new File(project.getBuild().getTestOutputDirectory(), include);
            else
               f = new File(project.getBuild().getOutputDirectory(), include);
            
            cl.addArguments(new String[] {f.getAbsolutePath()});
         }
         
      }
      else
      {
         if(isTest())
            cl.addArguments(new String[] { project.getBuild().getTestOutputDirectory()});
         else
            cl.addArguments(new String[] { project.getBuild().getOutputDirectory()});
      }

      if(getLog().isDebugEnabled()) 
      { 
         getLog().debug("Executing aopc: "+cl.toString()); 
      }
      return cl;
   }

   private void compileOutOfProcess(Commandline cl)
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

   private String getClassPath()
   {
      return classPath;
   }

   private boolean isVerbose()
   {
      return verbose;
   }

   private boolean isSuppress()
   {
      return suppress;
   }

   private boolean isNoopt()
   {
      return noopt;
   }

   private boolean hasReport()
   {
      return report;
   }
   
   private String getAopClassPath()
   {
      return aopClassPath;
   }

   protected boolean isTest()
   {
      return test;
   }

   protected void setTest(boolean test)
   {
      this.test = test;
   }

}
