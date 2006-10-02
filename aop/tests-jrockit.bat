@echo off

echo Make sure you are using JDK 1.5 to compile this


IF A%JROCKIT_HOME%==A SET JROCKIT_HOME=

SET SUN_JAVA_HOME=%JAVA_HOME%
SET ORIGINAL_PATH=%PATH%

IF A%JROCKIT_HOME%==A goto noJRockit
SET JROCKIT_PATH=%JROCKIT_HOME%\bin;%PATH%

REM ###############################################################
REM Compile for jdk 1.4 with normal sun compiler

call ant clean main compile-test-classes 

REM ###############################################################
REM Switch to JRockit and run jdk 1.4 tests

SET PATH=%JROCKIT_PATH%
SET JAVA_HOME=%JROCKIT_HOME%

call ant bootclasspath-tests
call ant precompiled-tests

REM ###############################################################
REM Compile for jdk 5.0 with normal sun compiler

SET PATH=%ORIGINAL_PATH%
SET JAVA_HOME=%SUN_JAVA_HOME%
call ant clean main compile-test-classes compile-jdk15-tests

REM ###############################################################
REM Switch to JRockit and run jdk 5.0 tests

SET PATH=%JROCKIT_PATH%
SET JAVA_HOME=%JROCKIT_HOME%
call ant jdk15-loadtime-tests 
call ant jdk15-precompiled-tests


goto end

:noJrockit
echo You must set JROCKIT_HOME to the root of your JROCKIT distribution

:end








:

