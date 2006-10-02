@echo off

IF %1a==a goto display_usage
IF %2a==a goto display_usage
IF %3a==a goto display_usage
IF %4a==a goto display_usage
IF %2==%4 goto display_usage

SET USER_CLASSPATH=%1
SET AOPPATH=
SET AOPCLASSPATH=

IF %2==-aoppath SET AOPPATH=-Djboss.aop.path=%3
IF %2==-aopclasspath SET AOPCLASSPATH=-Djboss.aop.class.path=%3

IF %4%5==-aoppath goto display_usage
IF %4%5==-aopclasspath goto display_usage
IF %4==-aoppath SET AOPPATH=-Djboss.aop.path=%5 
IF %4==-aopclasspath SET AOPCLASSPATH=-Djboss.aop.class.path=%5



IF %4==-aoppath shift 
IF %3==-aoppath shift 
IF %4==-aopclasspath shift 
IF %3==-aopclasspath shift 


shift
shift
shift

SET MAINCLASS_AND_ARGS=
REM get all the command line args
:setupArgs
if %1a==a goto doneStart
	set MAINCLASS_AND_ARGS=%MAINCLASS_AND_ARGS% %1
shift
goto setupArgs

:doneStart

REM Setup AOP classpath
SET AOPC_CLASSPATH=..\lib\concurrent.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\javassist.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jboss-aop.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jboss-aspect-library.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\jboss-common.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\qdox.jar
SET AOPC_CLASSPATH=%AOPC_CLASSPATH%;..\lib\trove.jar


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

goto end

:display_usage
echo Batch file for running the aop precopiler for JDK 1.4
echo Usage:
echo Batch file for running loadtime instrumented aop applications for JDK 1.4 using the custom instrumented classloaeder
echo Usage:
echo run-boot.sh classpath [-aoppath path_to_aop.xml [-aopclasspath path_to_annotated] Main.class
echo    classpath:        Classpath of your sourcefiles and all required libraries
echo    path_to_.aop.xml: Path to your *-aop.xml files. Use colon as separator  if you have more than one
echo    Main.class:       Your main class
goto end

:display_java_home
echo "Please set the JAVA_HOME environment variable to the root of your JDK 1.4 distribution"

:end

