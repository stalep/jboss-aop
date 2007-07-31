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
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * A Simple mojo that use the JBoss AOP compiler to weave in AOP code
 * based on a jboss-aop.xml file.
 * NOTE: This mojo is not finished and will probably be rewritten or completly removed
 * when i learn more about maven...
 * 
 * Use it by running: mvn jbossaop:aopc
 * 
 * Supported options:
 * - aoppath (default src/main/resources/jboss-aop.xml)
 *  - verbose (default true)
 *  - suppress (default false)
 *  - noopt (default false)
 *  - report (default false)
 *  - includes 
 *  
 * 
 * @author <a href="mailto:spederse@redhat.com">Stale W. Pedersen</a>
 * @goal compile
 * @phase process-classes
 * @requiresDependencyResolution
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
    * @parameter expression="${supress}" default-value="false"
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
    * 
    * 
    * @parameter expression="${failOnError}" default-value="true"
    */
   private boolean failOnError;

   /** 
    * 
    * @parameter expression="${classPath}" default-value="${project.build.outputDirectory}"
    */
   private String classPath;

   /** 
    * 
    * @parameter expression="${aopPath}" default-value="src/main/resources/jboss-aop.xml"
    */
   private String aoppath;

   /** 
    * 
    * @parameter expression="${aopClassPath}" default-value=""
    */
   private String aopClassPath;

   /** 
    * 
    * @parameter expression="${compilerClassPath}" default-value=""
    */
   private String compilerClasspath;

   /**
    * @parameter default-value="${project.compileDependencies}"
    * @required
    * @readonly
    */
   private List<Dependency> cDependencies;

   /**
    * @parameter default-value="${project.dependency}"
    * @required
    * @readonly
    */
   private Dependency pDependency;
   
   /**
    * @parameter
    */
   private String[] includes;
   
   private boolean test;

   /**
    * @parameter expression="${project}"
    */
   private MavenProject project;

   public void execute() throws MojoExecutionException
   {
      compileOutOfProcess(createCommandLine());
   }

   private String createClassPathList(Set<Artifact> artifacts)
   {
      StringBuffer sb = new StringBuffer();
      for(Artifact a : artifacts)
      {
         sb.append(a.getFile().toString()).append(File.pathSeparator);
      }

      if(isTest())
         sb.append(project.getBuild().getTestOutputDirectory());
      else
         sb.append(project.getBuild().getOutputDirectory());

      return sb.toString();
   }


   @SuppressWarnings("unchecked")
   private Commandline createCommandLine()
   {
      Commandline cl = new Commandline();
      cl.setExecutable("java");
      cl.addArguments(new String[] { "-cp", createClassPathList(project.getArtifacts())});
      cl.addArguments(new String[] { "org.jboss.aop.standalone.Compiler"});
      if(isVerbose())
         cl.addArguments(new String[] { "-verbose"});
      if(isSuppress())
         cl.addArguments(new String[] { "-suppress"});
      if(isNoopt())
         cl.addArguments(new String[] { "-noopt"});
      if(hasReport())
         cl.addArguments(new String[] { "-report"});
      
      cl.addArguments(new String[] { "-aoppath", getAoppath()});
      
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
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private String getAoppath()
   {
      return aoppath;
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

   private String getCompilerClasspath()
   {
      return compilerClasspath;
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

   protected boolean isTest()
   {
      return test;
   }

   protected void setTest(boolean test)
   {
      this.test = test;
   }

}
