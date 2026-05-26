@echo off
setlocal
pushd "%~dp0"

set VERSION=%1
if "%VERSION%"=="" set VERSION=1.0.0

rem Find JDK bin dir from javac
for /f "tokens=*" %%i in ('where javac 2^>nul') do set JAVAC_PATH=%%i
if not defined JAVAC_PATH (
    echo ERROR: javac not found. Install JDK 25 from https://adoptium.net
    popd & exit /b 1
)
for %%i in ("%JAVAC_PATH%") do set JDK_BIN=%%~dpi
set JPACKAGE="%JDK_BIN%jpackage.exe"

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
