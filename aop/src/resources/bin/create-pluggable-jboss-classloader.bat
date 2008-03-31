@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal



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


SET AOP_FILES=%AOPC_CLASSPATH%

if x%JAVA_HOME%==x goto display_java_home

echo ============================================
echo JAVA_HOME: %JAVA_HOME%
echo This tool will only work with JDK 1.4!!!
echo ============================================

SET JAVA_CMD=%JAVA_HOME%\bin\java
echo %JAVA_CMD%

rem Create class loader
%JAVA_CMD% -cp %AOP_FILES% org.jboss.aop.hook.GenerateInstrumentedClassLoader .\gen-classloader


SET AOP_FILES=.\gen-classloader;%AOPC_CLASSPATH%

rem Run instrumented program
%JAVA_CMD%  -Xbootclasspath/p:%AOP_FILES% -classpath %USER_CLASSPATH% %AOPPATH% %AOPCLASSPATH% %MAINCLASS_AND_ARGS%

%JAVA_CMD% -cp %AOP_FILES% org.jboss.aop.hook.GeneratePluggableInstrumentedClassLoader gen-classloader
cd gen-classloader
jar -cvf jboss-classloader-transformer.jar *
mv jboss-classloader-transformer.jar ..
cd ..
