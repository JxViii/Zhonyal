# Zhonyal

A desktop study tracker built with Java Swing. Track your sessions, manage your notes, and keep yourself accountable — with no excuses.

---

## Features

**Sessions**
- Start a named study session with a live timer
- Switch between Study and Pause splits during a session
- Collapsible floating chrono window that stays on top while you work
- View full session history with date filtering
- Session detail view with focus rate, total time, study/pause breakdown

**Notes**
- Upload PDF files to your library
- Browse and open them in the built-in PDF viewer
- Delete mode for removing files from your library

**General**
- EN / ES language toggle
- Dark themed UI with custom fonts
- Profile setup on first launch, editable anytime from the sidebar
- Data stored locally — nothing leaves your machine

---

## Screenshots

> Coming soon

---

## Requirements

| | Linux | Windows |
|---|---|---|
| To run | Java 17+ | Nothing — JRE is bundled in the release |
| To build from source | JDK **25** | JDK **25** |

Download JDK 25: [adoptium.net](https://adoptium.net)

---

## Install on Windows

1. Download `Zhonyal-x.x.x-windows.zip` from [Releases](https://github.com/JxViii/Zhonyal/releases)
2. Extract the zip anywhere you want
3. Open the `Zhonyal` folder and double-click `Zhonyal.exe`

No Java installation needed — the JRE is bundled inside the zip.

---

## Install on Linux

Download `zhonyal-x.x.x.zip` from [Releases](https://github.com/JxViii/Zhonyal/releases), extract it, and run:

```bash
bash zhonyal-x.x.x/install.sh
```

Zhonyal will appear in your application launcher. Data lives at `~/.local/share/zhonyal/`.

---

## Build from source

**Linux**
```bash
git clone https://github.com/JxViii/Zhonyal.git
cd Zhonyal
bash install.sh
```

**Windows** — requires JDK 25, run in Command Prompt:
```bat
git clone https://github.com/JxViii/Zhonyal.git
cd Zhonyal
package-windows.bat 1.0.0
```
This produces `dist\Zhonyal-1.0.0-windows.zip` — extract it and run `Zhonyal.exe`.

**Create a Linux release zip:**
```bash
bash package.sh 1.0.0
# output: dist/zhonyal-1.0.0.zip
```

**Create a Windows release zip** (run on Windows):
```bat
package-windows.bat 1.0.0
# output: dist\Zhonyal-1.0.0-windows.zip
```

---

## Project structure

```
Zhonyal/
├── Main.java               # Entry point
├── App.java                # Root frame, routing, lang change wiring
│
├── helpers/                # Data models and utilities
│   ├── Session.java
│   ├── Split.java
│   ├── UserProfile.java
│   ├── LangManager.java    # i18n manager (EN/ES)
│   └── FilterState.java
│
├── ui/
│   ├── pages/              # Full pages (one per route)
│   │   ├── HomePage.java
│   │   ├── SessionsHomePage.java
│   │   ├── SessionsManager.java
│   │   ├── SessionDetails.java
│   │   ├── Notes.java
│   │   ├── NotesViewer.java
│   │   ├── OnboardingPage.java
│   │   └── Chrono.java     # Floating session timer (JWindow)
│   │
│   ├── components/         # Reusable UI components per feature
│   │   ├── AppPopup.java
│   │   ├── ChronoComp/
│   │   ├── HomeComp/
│   │   ├── NotesComp/
│   │   ├── NotesViewerComp/
│   │   ├── SessionDetailsComp/
│   │   ├── SessionsHomeComp/
│   │   └── SessionsManagerComp/
│   │
│   └── templates/          # App shell, sidebar, header
│       ├── AppShell.java
│       ├── Sidebar.java
│       └── Header.java
│
├── utils/                  # DB, fonts, theme constants
│   ├── DB.java
│   ├── Fonts.java
│   └── Theme.java
│
├── i18n/                   # Language bundles
│   ├── Bundle_en.properties
│   └── Bundle_es.properties
│
├── images/                 # App assets
├── fonts/                  # Bundled typefaces
├── lib/                    # Dependencies (fat-jar'd on build)
│
├── build.sh                # Compile + build fat JAR (Linux)
├── build.bat               # Compile + build fat JAR (Windows)
├── install.sh              # Build + install on Linux
├── install.ps1             # Install on Windows (from source)
├── package.sh              # Build + package Linux release zip
└── package-windows.bat     # Build + package Windows release zip (via jpackage)
```

---

## Dependencies

| Library | Version | Purpose |
|---|---|---|
| [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) | 3.46.0 | Local database |
| [Apache PDFBox](https://pdfbox.apache.org/) | 3.0.7 | PDF rendering |
| [SLF4J](https://www.slf4j.org/) | 2.0.16 | Logging (no-op) |

---

## Data & privacy

All data is stored locally on your machine:

| What | Where (Linux) | Where (Windows) |
|---|---|---|
| Sessions database | `~/.local/share/zhonyal/zhonyal.db` | next to `Zhonyal.exe` |
| User profile | `~/.zhonyal/user.properties` | `%USERPROFILE%\.zhonyal\user.properties` |

Uninstalling on Linux:
```bash
rm -rf ~/.local/share/zhonyal
rm ~/.local/share/applications/zhonyal.desktop
rm -rf ~/.zhonyal
```

Uninstalling on Windows: delete the `Zhonyal` folder you extracted.
