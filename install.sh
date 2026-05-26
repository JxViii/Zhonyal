#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
INSTALL_DIR="$HOME/.local/share/zhonyal"
APPS_DIR="$HOME/.local/share/applications"

# Build first
bash "$PROJECT_DIR/build.sh"

echo "==> Installing to $INSTALL_DIR..."
mkdir -p "$INSTALL_DIR"

cp "$PROJECT_DIR/dist/zhonyal.jar" "$INSTALL_DIR/"
cp -r "$PROJECT_DIR/images"        "$INSTALL_DIR/"
cp -r "$PROJECT_DIR/fonts"         "$INSTALL_DIR/"

# Migrate existing DB if one exists in the project dir and none in install dir yet
if [ -f "$PROJECT_DIR/zhonyal.db" ] && [ ! -f "$INSTALL_DIR/zhonyal.db" ]; then
    cp "$PROJECT_DIR/zhonyal.db" "$INSTALL_DIR/"
    echo "==> Migrated existing database"
fi

# Launcher script
cat > "$INSTALL_DIR/zhonyal" << 'EOF'
#!/bin/bash
cd "$(dirname "$(readlink -f "$0")")"
exec java \
  -Dawt.useSystemAAFontSettings=lcd_hrgb \
  -Dswing.aatext=true \
  -jar zhonyal.jar "$@"
EOF
chmod +x "$INSTALL_DIR/zhonyal"

# .desktop entry
mkdir -p "$APPS_DIR"
cat > "$APPS_DIR/zhonyal.desktop" << EOF
[Desktop Entry]
Name=Zhonyal
Comment=Study session tracker
Exec=$INSTALL_DIR/zhonyal
Icon=$INSTALL_DIR/images/Logo.png
Type=Application
Categories=Education;
Terminal=false
StartupWMClass=zhonyal
EOF

echo "==> Done. Launch Zhonyal from your app drawer or run:"
echo "    $INSTALL_DIR/zhonyal"
