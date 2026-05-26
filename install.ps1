# Zhonyal Windows Installer
# Run with: powershell -ExecutionPolicy Bypass -File install.ps1

$InstallDir = "$env:LOCALAPPDATA\Zhonyal"
$StartMenu  = "$env:APPDATA\Microsoft\Windows\Start Menu\Programs"
$ScriptDir  = Split-Path -Parent $MyInvocation.MyCommand.Path

# Check Java is installed
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "ERROR: Java not found. Install Java 17+ from https://adoptium.net and try again." -ForegroundColor Red
    exit 1
}

Write-Host "==> Installing Zhonyal to $InstallDir..."
New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null

Copy-Item "$ScriptDir\zhonyal.jar" "$InstallDir\" -Force
Copy-Item "$ScriptDir\images"      "$InstallDir\" -Recurse -Force
Copy-Item "$ScriptDir\fonts"       "$InstallDir\" -Recurse -Force

# Launcher — javaw so no console window opens
@"
@echo off
cd /d "%~dp0"
javaw -Dawt.useSystemAAFontSettings=lcd_hrgb -Dswing.aatext=true -jar zhonyal.jar %*
"@ | Out-File -FilePath "$InstallDir\zhonyal.bat" -Encoding ASCII

# Start Menu shortcut
$Shell    = New-Object -ComObject WScript.Shell
$Shortcut = $Shell.CreateShortcut("$StartMenu\Zhonyal.lnk")
$Shortcut.TargetPath       = "$InstallDir\zhonyal.bat"
$Shortcut.WorkingDirectory = $InstallDir
$Shortcut.Description      = "Study session tracker"
$Shortcut.Save()

Write-Host "==> Done. Launch Zhonyal from the Start Menu."
Write-Host "    Or run: $InstallDir\zhonyal.bat"
