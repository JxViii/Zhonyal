@echo off
setlocal
pushd "%~dp0"

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
set JAVAC="%JDK_BIN%javac.exe"
set JAR="%JDK_BIN%jar.exe"
if not exist "%JDK_BIN%jar.exe" (
    echo ERROR: jar.exe not found in %JDK_BIN%
    echo Set JAVA_HOME to your JDK installation directory, e.g.:
    echo   set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.1.9-hotspot
    popd & exit /b 1
)

echo =^> Compiling...
if not exist "out" mkdir "out"
%JAVAC% -cp "lib\*" -d "out" -sourcepath "." Main.java
if errorlevel 1 ( echo Compilation failed. & popd & exit /b 1 )
xcopy /E /I /Y "i18n" "out\i18n\" >nul

echo =^> Building fat JAR...
if exist "dist\tmp" rmdir /S /Q "dist\tmp"
mkdir "dist\tmp"

pushd "dist\tmp"
for %%f in ("..\..\lib\*.jar") do %JAR% xf "%%f"
popd

if exist "dist\tmp\META-INF" rmdir /S /Q "dist\tmp\META-INF"
xcopy /E /I /Y "out\*" "dist\tmp\" >nul

mkdir "dist\tmp\META-INF"
(echo Main-Class: Main & echo.) > "dist\tmp\META-INF\MANIFEST.MF"

if not exist "dist" mkdir "dist"
%JAR% --create --file="dist\zhonyal.jar" --manifest="dist\tmp\META-INF\MANIFEST.MF" -C "dist\tmp" .

rmdir /S /Q "dist\tmp"
echo =^> dist\zhonyal.jar ready
popd
