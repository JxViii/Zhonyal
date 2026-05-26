@echo off
setlocal

set PROJECT_DIR=%~dp0
set DIST_DIR=%PROJECT_DIR%dist
set TMP_DIR=%DIST_DIR%\tmp

echo =^> Compiling...
if not exist "%PROJECT_DIR%out" mkdir "%PROJECT_DIR%out"
javac -cp "%PROJECT_DIR%lib\*" -d "%PROJECT_DIR%out" -sourcepath "%PROJECT_DIR%" "%PROJECT_DIR%Main.java"
if errorlevel 1 ( echo Compilation failed. & exit /b 1 )
xcopy /E /I /Y "%PROJECT_DIR%i18n" "%PROJECT_DIR%out\i18n\" >nul

echo =^> Building fat JAR...
if exist "%TMP_DIR%" rmdir /S /Q "%TMP_DIR%"
mkdir "%TMP_DIR%"

rem Extract all dependency JARs into tmp
pushd "%TMP_DIR%"
for %%f in ("%PROJECT_DIR%lib\*.jar") do (
    jar xf "%%f"
)
popd

rem Remove signature files that break the fat JAR
if exist "%TMP_DIR%\META-INF" rmdir /S /Q "%TMP_DIR%\META-INF"

rem Copy compiled classes and i18n
xcopy /E /I /Y "%PROJECT_DIR%out\*" "%TMP_DIR%\" >nul

rem Write manifest
mkdir "%TMP_DIR%\META-INF"
(echo Main-Class: Main & echo.) > "%TMP_DIR%\META-INF\MANIFEST.MF"

rem Pack
if not exist "%DIST_DIR%" mkdir "%DIST_DIR%"
jar --create --file="%DIST_DIR%\zhonyal.jar" --manifest="%TMP_DIR%\META-INF\MANIFEST.MF" -C "%TMP_DIR%" .

rmdir /S /Q "%TMP_DIR%"
echo =^> dist\zhonyal.jar ready
