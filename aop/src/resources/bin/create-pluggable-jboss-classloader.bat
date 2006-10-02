@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal



REM Setup AOP classpath
SET AOPC_CLASSPATH=..\lib\concurrent.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\javassist.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jboss-aop.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jboss-aspect-library.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jboss-common.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\qdox.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\trove.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jdk14-pluggable-instrumentor.jar


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
