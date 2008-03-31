#!/bin/sh


usage(){
   echo Script file for running loadtime instrumented aop applications for JDK 1.4 using the custom instrumented classloaeder
   echo Usage:
   echo run-boot.sh classpath [-aoppath path_to_aop.xml [-aopclasspath path_to_annotated] Main.class
   echo 
   echo    classpath:        Classpath of your sourcefiles and all required libraries
   echo 
   echo    path_to_.aop.xml: Path to your *-aop.xml files. Use colon as separator  if you have more than one
   echo 
   echo    Main.class:       Your main class
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




AOPC_CLASSPATH=../lib-14/javassist.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-aop.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-backport-concurrent.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-common-core-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-reflect-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-mdr-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-logging-log4j.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-logging-spi.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-retro-rt.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-standalone-aspect-library-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jdk14-pluggable-instrumentor.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/log4j.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/qdox.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/trove.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:$USER_CLASSPATH


CTR=0

for param in $*; do
   
   CTR=`expr $CTR + 1`
   if [ $CTR -gt $FILESTART ]; then
      MAINCLASS_AND_ARGS=$MAINCLASS_AND_ARGS" "$param
   fi
done

#Check for cygwin and convert path if necessary
# if (cygpath --version) >/dev/null 2>/dev/null; then
#   AOPC_CLASSPATH=`cygpath --path --windows $AOPC_CLASSPATH`
#fi

AOP_FILES=$AOPC_CLASSPATH

#Check for cygwin and convert path if necessary
if (cygpath --version) >/dev/null 2>/dev/null; then
   AOP_FILES=`cygpath --path --windows $AOP_FILES`
fi



if [ "x$JAVA_HOME" = "x" ]; then
   echo "Please set the JAVA_HOME environment variable to the root of your JDK 1.4 distribution"
   exit 1
fi

echo ============================================
echo JAVA_HOME: $JAVA_HOME
echo This tool will only work with JDK 1.4!!!
echo ============================================

JAVA_CMD="$JAVA_HOME/bin/java"
echo $JAVA_CMD

#Create class loader
$JAVA_CMD -cp $AOP_FILES org.jboss.aop.hook.GenerateInstrumentedClassLoader gen-classloader


AOP_FILES=`pwd`/gen-classloader:$AOPC_CLASSPATH
#Check for cygwin and convert path if necessary
if (cygpath --version) >/dev/null 2>/dev/null; then
   AOP_FILES=`cygpath --path --windows $AOP_FILES`
fi


#Run instrumented program
$JAVA_CMD  -Xbootclasspath/p:$AOP_FILES -classpath $1 $AOPPATH $AOPCLASSPATH $MAINCLASS_AND_ARGS


