# Zhonyal Windows Installer
# Run with: powershell -ExecutionPolicy Bypass -File install.ps1

$InstallDir = "$env:LOCALAPPDATA\Zhonyal"
$StartMenu  = "$env:APPDATA\Microsoft\Windows\Start Menu\Programs"
$ScriptDir  = $PSScriptRoot

# Check Java is installed
if (-not (Get-Command javaw -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: Java not found. Install Java 17+ from https://adoptium.net and try again." -ForegroundColor Red
    exit 1
}

# Build from source if no JAR present yet
$Jar = "$ScriptDir\zhonyal.jar"
if (-not (Test-Path $Jar)) {
    if (Test-Path "$ScriptDir\build.bat") {
        Write-Host "==> No JAR found, building from source..."
        cmd /c "`"$ScriptDir\build.bat`""
        if ($LASTEXITCODE -ne 0) { Write-Host "Build failed." -ForegroundColor Red; exit 1 }
        $Jar = "$ScriptDir\dist\zhonyal.jar"
    } else {
        Write-Host "ERROR: zhonyal.jar not found." -ForegroundColor Red
        exit 1
    }
}

Write-Host "==> Installing Zhonyal to $InstallDir..."
New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null

Copy-Item $Jar                    "$InstallDir\zhonyal.jar" -Force
Copy-Item "$ScriptDir\images"     $InstallDir -Recurse -Force
Copy-Item "$ScriptDir\fonts"      $InstallDir -Recurse -Force

# Launcher batch (for command-line use)
@"
@echo off
cd /d "%~dp0"
javaw -Dawt.useSystemAAFontSettings=lcd_hrgb -Dswing.aatext=true -jar zhonyal.jar %*
"@ | Out-File -FilePath "$InstallDir\zhonyal.bat" -Encoding ASCII

# Start Menu shortcut — points to javaw directly so no console window
$JavawPath = (Get-Command javaw).Source
$Shell     = New-Object -ComObject WScript.Shell
$Shortcut  = $Shell.CreateShortcut("$StartMenu\Zhonyal.lnk")
$Shortcut.TargetPath       = $JavawPath
$Shortcut.Arguments        = "-Dawt.useSystemAAFontSettings=lcd_hrgb -Dswing.aatext=true -jar `"$InstallDir\zhonyal.jar`""
$Shortcut.WorkingDirectory = $InstallDir
$Shortcut.Description      = "Study session tracker"
$Shortcut.IconLocation     = "$InstallDir\images\Logo.png"
$Shortcut.Save()

Write-Host "==> Done. Launch Zhonyal from the Start Menu."
Write-Host "    Or run: $InstallDir\zhonyal.bat"
