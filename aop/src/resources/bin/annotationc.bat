@echo off

IF %1a == a goto display_usage
IF %2a == a goto display_usage

SET USER_CLASSPATH=%1
shift

set ARGS_AND_FILES=
REM get all the command line args
:setupArgs
if %1a==a goto doneStart
	set ARGS_AND_FILES=%ARGS_AND_FILES% %1
shift
goto setupArgs

:doneStart

REM Setup AOP classpath
SET AOPC_CLASSPATH=..\lib-14\javassist.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-aop.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-backport-concurrent.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-common-core-jdk14.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-reflect-jdk14.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-mdr-jdk14.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-logging-log4j.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-logging-spi.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-retro-rt.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jboss-standalone-aspect-library-jdk14.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\jdk14-pluggable-instrumentor.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\log4j.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\qdox.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib-14\trove.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;%USER_CLASSPATH%


java -classpath %AOPC_CLASSPATH%  org.jboss.aop.annotation.compiler.AnnotationCompiler %ARGS_AND_FILES%

goto end

:display_usage
echo Batch file for running the aop precopiler for JDK 1.4
echo Usage:
echo annotationc.bat classpath [-verbose] [-xml [-o outputfile]] [-bytecode]  dir_or_file+
echo    classpath:    classpath of your sourcefiles and all required libraries
echo    -xml:         if we want to generate an xml file containing the metadata
echo                  bindings
echo    -o:           specify the output file, default is metadata-aop.xml
echo    -bytecode:    if we want to instrument the classes with the annotations
echo    dir_or_file:  directory containing files to be aop precompiled
echo    -verbose:     Specify if you want verbose output
echo    -report:      If specified, classes do not get instrumented. Instead 
echo                  you get an xml file containing the bindings applied.

:end

