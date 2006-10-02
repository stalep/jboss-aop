#!/bin/sh


usage(){
   echo Batch file for running the aop precopiler for JDK 1.4
   echo Usage:
   echo annotationc.sh classpath [-verbose] [-xml [-o outputfile]] [-bytecode]  dir_or_file+
   echo    classpath:    classpath of your sourcefiles and all required libraries
   echo    -xml:         if we want to generate an xml file containing the metadata
   echo                  bindings
   echo    -o:           specify the output file, default is metadata-aop.xml
   echo    -bytecode:    if we want to instrument the classes with the annotations
   echo    dir_or_file:  directory containing files to be aop precompiled
   echo    -verbose:     Specify if you want verbose output
   echo    -report:      If specified, classes do not get instrumented. Instead 
   echo                  you get an xml file containing the bindings applied.
   exit 1
}

if [ "x$1" = "x" ]; then
   usage
fi
if [ "x$2" = "x" ]; then
   usage
fi

USER_CLASSPATH=$1

AOPC_CLASSPATH=../lib/concurrent.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib/javassist.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib/jboss-aop.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib/jboss-aspect-library.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib/jboss-common.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib/qdox.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib/trove.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:$USER_CLASSPATH

CTR=0

for param in $*; do
   
   CTR=`expr $CTR + 1`
   if [ $CTR -gt 1 ]; then
      ARGS_AND_FILES=$ARGS_AND_FILES" "$param
   fi
done

#Check for cygwin and convert path if necessary
if (cygpath --version) >/dev/null 2>/dev/null; then
   AOPC_CLASSPATH=`cygpath --path --windows $AOPC_CLASSPATH`
fi


java -classpath $AOPC_CLASSPATH org.jboss.aop.annotation.compiler.AnnotationCompiler $ARGS_AND_FILES

