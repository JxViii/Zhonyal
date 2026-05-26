#ifndef AppVersion
  #define AppVersion "1.0.0"
#endif

[Setup]
AppName=Zhonyal
AppVersion={#AppVersion}
AppPublisher=JxViii
DefaultDirName={autopf}\Zhonyal
DefaultGroupName=Zhonyal
OutputDir=dist
OutputBaseFilename=Zhonyal-{#AppVersion}-Setup
Compression=lzma2/ultra64
SolidCompression=yes
UninstallDisplayIcon={app}\Zhonyal.exe
ArchitecturesInstallIn64BitMode=x64compatible
DisableProgramGroupPage=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "dist\appimage\Zhonyal\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Zhonyal"; Filename: "{app}\Zhonyal.exe"; WorkingDir: "{app}"
Name: "{autodesktop}\Zhonyal"; Filename: "{app}\Zhonyal.exe"; WorkingDir: "{app}"; Tasks: desktopicon

[Run]
Filename: "{app}\Zhonyal.exe"; Description: "{cm:LaunchProgram,Zhonyal}"; Flags: nowait postinstall skipifsilent
