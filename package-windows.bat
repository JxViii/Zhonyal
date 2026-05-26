@echo off
setlocal
pushd "%~dp0"

set VERSION=%1
if "%VERSION%"=="" set VERSION=1.0.0

rem Find JDK bin dir: JAVA_HOME > where jar > where javac
if defined JAVA_HOME (
    set JDK_BIN=%JAVA_HOME%\bin\
    goto :found_jdk
)
for /f "tokens=*" %%i in ('where jar 2^>nul') do ( set _TMP=%%i & goto :jar_found )
:jar_found
if defined _TMP ( for %%i in ("%_TMP%") do set JDK_BIN=%%~dpi & goto :found_jdk )
for /f "tokens=*" %%i in ('where javac 2^>nul') do ( set _TMP=%%i & goto :javac_found )
:javac_found
if defined _TMP ( for %%i in ("%_TMP%") do set JDK_BIN=%%~dpi & goto :found_jdk )
echo ERROR: JDK not found. Install JDK 25 and set JAVA_HOME.
popd & exit /b 1
:found_jdk
set JPACKAGE="%JDK_BIN%jpackage.exe"
if not exist "%JDK_BIN%jpackage.exe" (
    echo ERROR: jpackage.exe not found in %JDK_BIN%
    echo Set JAVA_HOME to your JDK installation directory, e.g.:
    echo   set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.1.9-hotspot
    popd & exit /b 1
)

rem Build fat JAR
call build.bat
if errorlevel 1 ( popd & exit /b 1 )

echo =^> Packaging with jpackage...
if exist "dist\package" rmdir /S /Q "dist\package"

%JPACKAGE% ^
  --input dist ^
  --main-jar zhonyal.jar ^
  --main-class Main ^
  --name Zhonyal ^
  --app-version %VERSION% ^
  --type app-image ^
  --dest dist\package ^
  --app-content images ^
  --app-content fonts ^
  --java-options "-Dawt.useSystemAAFontSettings=lcd_hrgb" ^
  --java-options "-Dswing.aatext=true"

if errorlevel 1 ( echo jpackage failed. & popd & exit /b 1 )

echo =^> Creating zip...
powershell -Command "Compress-Archive -Path 'dist\package\Zhonyal' -DestinationPath 'dist\Zhonyal-%VERSION%-windows.zip' -Force"

rmdir /S /Q "dist\package"
echo =^> dist\Zhonyal-%VERSION%-windows.zip ready for GitHub Release
popd
