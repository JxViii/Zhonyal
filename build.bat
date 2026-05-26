@echo off
setlocal
pushd "%~dp0"

rem Find JDK bin dir from wherever javac lives
for /f "tokens=*" %%i in ('where javac 2^>nul') do set JAVAC_PATH=%%i
if not defined JAVAC_PATH (
    echo ERROR: javac not found. Install JDK 25 from https://adoptium.net
    popd & exit /b 1
)
for %%i in ("%JAVAC_PATH%") do set JDK_BIN=%%~dpi
set JAVAC="%JDK_BIN%javac.exe"
set JAR="%JDK_BIN%jar.exe"

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
