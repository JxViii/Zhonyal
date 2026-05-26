@echo off
setlocal
pushd "%~dp0"

rem Usage: package-windows.bat [version]
rem Requires:
rem   - JDK 25 (https://adoptium.net)
rem   - Inno Setup 6 (https://jrsoftware.org/isdl.php)

set VERSION=%1
if "%VERSION%"=="" set VERSION=1.0.0

rem ── Find JDK bin dir ────────────────────────────────────────────────────────
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
    echo Set JAVA_HOME to your JDK installation directory.
    popd & exit /b 1
)

rem ── Find Inno Setup compiler ─────────────────────────────────────────────────
set ISCC=
if exist "%ProgramFiles(x86)%\Inno Setup 6\ISCC.exe" set ISCC="%ProgramFiles(x86)%\Inno Setup 6\ISCC.exe"
if exist "%ProgramFiles%\Inno Setup 6\ISCC.exe"      set ISCC="%ProgramFiles%\Inno Setup 6\ISCC.exe"
for /f "tokens=*" %%i in ('where ISCC 2^>nul') do if not defined ISCC set ISCC="%%i"
if not defined ISCC (
    echo ERROR: Inno Setup not found.
    echo Download and install from: https://jrsoftware.org/isdl.php
    popd & exit /b 1
)

rem ── Build fat JAR ────────────────────────────────────────────────────────────
call build.bat
if errorlevel 1 ( popd & exit /b 1 )

rem ── Build app-image (bundles JRE, no WiX needed) ────────────────────────────
echo =^> Creating app-image...
if exist "dist\appimage" rmdir /S /Q "dist\appimage"

%JPACKAGE% ^
  --input dist ^
  --main-jar zhonyal.jar ^
  --main-class Main ^
  --name Zhonyal ^
  --app-version %VERSION% ^
  --type app-image ^
  --dest dist\appimage ^
  --app-content images ^
  --app-content fonts ^
  --java-options "-Dawt.useSystemAAFontSettings=lcd_hrgb" ^
  --java-options "-Dswing.aatext=true"

if errorlevel 1 ( echo jpackage failed. & popd & exit /b 1 )

rem ── Package with Inno Setup ───────────────────────────────────────────────────
echo =^> Building installer with Inno Setup...
%ISCC% /DAppVersion=%VERSION% zhonyal.iss
if errorlevel 1 ( echo Inno Setup failed. & popd & exit /b 1 )

rem ── Clean up temp app-image ───────────────────────────────────────────────────
rmdir /S /Q "dist\appimage"

echo.
echo =^> dist\Zhonyal-%VERSION%-Setup.exe ready
echo    Upload to GitHub Releases. Users just double-click to install.
popd
