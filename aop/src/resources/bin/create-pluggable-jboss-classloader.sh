#!/bin/sh


usage(){
   echo Script file for creating a custom instrumented classloader that can be used for loadtime AOP with JDK 1.4 and JBoss App server
   echo Usage:
   echo create-pluggable-jboss-classloader.sh classpath
   exit 1
}

AOPC_CLASSPATH=../lib-14/javassist.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-aop.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-backport-concurrent.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-common-core-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-container-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-logging-log4j.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-logging-spi.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-retro-rt.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jboss-standalone-aspect-library-jdk14.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/jdk14-pluggable-instrumentor.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/log4j.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/qdox.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:../lib-14/trove.jar
AOPC_CLASSPATH=$AOPC_CLASSPATH:$USER_CLASSPATH

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
$JAVA_CMD -cp $AOP_FILES org.jboss.aop.hook.GeneratePluggableInstrumentedClassLoader gen-classloader
cd gen-classloader 
jar -cvf jboss-classloader-transformer.jar *
mv jboss-classloader-transformer.jar ..
cd ..
rm -rf gen-classloader



