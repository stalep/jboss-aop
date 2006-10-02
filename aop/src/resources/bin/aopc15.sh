#!/bin/sh


usage(){
   echo Batch file for running the aop precopiler for JDK 1.4
   echo Usage:
   echo aopc15.sh [-aoppath path_to_aop.xml [-aopclasspath path_to_annotated] [-report] [-verbose]  dir_or_file+
   echo 
   echo    classpath:        Classpath of your sourcefiles and all required libraries
   echo 
   echo    path_to_.aop.xml: Path to your *-aop.xml files. Use colon as separator  if you have more than one
   echo 
   echo    path_to_annotated Path to jars/directories that have annotated aspects. Use colon as separator if you have more than one.
   echo 
   echo    dir_or_file:      Directory containing files to be aop precompiled
   echo 
   echo    -verbose:         Specify if you want verbose output
   echo 
   echo    -report:          If specified, classes do not get instrumented. Instead you get an xml file containing the bindings applied.
   
   exit 1
}

#Make sure have $1, $2 and $3
if [ "x$1" = "x" ]; then
   usage
fi
if [ "x$2" = "x" ]; then
   usage
fi
if [ "x$3" = "x" ]; then
   usage
fi
if [ "x$4" = "x" ]; then
   usage
fi
if [ "$2" = "$4" ]; then
   usage
fi

USER_CLASSPATH=$1

AOPPATH=
AOPCLASSPATH=

if [ "$2" = "-aoppath" ]; then
   AOPPATH=-Djboss.aop.path=$3
   FILESTART=3
fi

if [ "$4" = "-aoppath" ]; then
   if [ "x$5" = "x" ]; then
      usage
   fi 
   AOPPATH=-Djboss.aop.path=$5 
   FILESTART=5
fi

if [ "$2" = "-aopclasspath" ]; then
   AOPCLASSPATH=-Djboss.aop.class.path=$3
   FILESTART=3
fi



if [ "$4" = "-aopclasspath" ]; then
   if [ "x$5" = "x" ]; then
      usage
   fi
   AOPCLASSPATH=-Djboss.aop.class.path=$5
   FILESTART=5
fi



AOPC_CLASSPATH=../lib-50/concurrent.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-50/javassist.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-50/jboss-aop-jdk50.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-50/jboss-aspect-library.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-50/jboss-common.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-50/qdox.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-50/trove.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:$USER_CLASSPATH

CTR=0

for param in $*; do
   
   CTR=`expr $CTR + 1`
   if [ $CTR -gt $FILESTART ]; then
      ARGS_AND_FILES=$ARGS_AND_FILES" "$param
   fi
done

#Check for cygwin and convert path if necessary
if (cygpath --version) >/dev/null 2>/dev/null; then
   AOPC_CLASSPATH=`cygpath --path --windows $AOPC_CLASSPATH`
fi


java -classpath $AOPC_CLASSPATH $AOPPATH $AOPCLASSPATH org.jboss.aop.standalone.Compiler $ARGS_AND_FILES

