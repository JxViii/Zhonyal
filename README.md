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

| | Version |
|---|---|
| Java (to run) | 17 or later |
| Java JDK (to build from source) | **25** — required, lower versions will not compile |

Download Java: [adoptium.net](https://adoptium.net)

---

## Install from release

Download the latest `zhonyal-x.x.x.zip` from [Releases](https://github.com/JxViii/Zhonyal/releases) and extract it.

**Linux**
```bash
bash zhonyal-x.x.x/install.sh
```
Zhonyal will appear in your application launcher. Data lives at `~/.local/share/zhonyal/`.

**Windows**
```powershell
powershell -ExecutionPolicy Bypass -File zhonyal-x.x.x\install.ps1
```
Zhonyal will appear in the Start Menu. Data lives at `%LOCALAPPDATA%\Zhonyal\`.

---

## Build from source

```bash
git clone https://github.com/JxViii/Zhonyal.git
cd Zhonyal
bash install.sh
```

This compiles the project, builds a fat JAR, copies assets to `~/.local/share/zhonyal/`, and installs a `.desktop` entry for your launcher.

To just build the JAR without installing:

```bash
bash build.sh
# output: dist/zhonyal.jar
```

To create a release zip (for both Linux and Windows):

```bash
bash package.sh 1.0.0
# output: dist/zhonyal-1.0.0.zip
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
├── build.sh                # Compile + build fat JAR (Linux/Mac)
├── install.sh              # Build + install on Linux
├── install.ps1             # Install on Windows
└── package.sh              # Build + package release zip
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
| Sessions database | `~/.local/share/zhonyal/zhonyal.db` | `%LOCALAPPDATA%\Zhonyal\zhonyal.db` |
| User profile | `~/.zhonyal/user.properties` | `%USERPROFILE%\.zhonyal\user.properties` |

Uninstalling on Linux:
```bash
rm -rf ~/.local/share/zhonyal
rm ~/.local/share/applications/zhonyal.desktop
rm -rf ~/.zhonyal
```

Uninstalling on Windows: delete `%LOCALAPPDATA%\Zhonyal` and the Start Menu shortcut.
