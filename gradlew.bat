@rem Gradle wrapper launcher for Windows
@rem
@rem This is a minimal, self-contained launcher. Android Studio does not need it
@rem (the IDE runs Gradle itself); it is here only so the project can be built
@rem from a terminal.

@if "%DEBUG%"=="" @echo off
setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set CLASSPATH=%APP_HOME%\gradle\wrapper\gradle-wrapper.jar
if defined GRADLE_USER_HOME (
  set GRADLE_USER_HOME=%GRADLE_USER_HOME%
) else (
  set GRADLE_USER_HOME=%USERPROFILE%\.gradle
)

if defined JAVA_HOME (
  set JAVACMD=%JAVA_HOME%\bin\java.exe
) else (
  set JAVACMD=java.exe
)

"%JAVACMD%" -Dorg.gradle.appname=%APP_BASE_NAME% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
