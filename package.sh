#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
VERSION="${1:-1.0.0}"
PACKAGE_NAME="zhonyal-$VERSION"
STAGING="$PROJECT_DIR/dist/$PACKAGE_NAME"

bash "$PROJECT_DIR/build.sh"

echo "==> Packaging $PACKAGE_NAME..."
rm -rf "$STAGING"
mkdir -p "$STAGING"

cp "$PROJECT_DIR/dist/zhonyal.jar" "$STAGING/"
cp -r "$PROJECT_DIR/images"        "$STAGING/"
cp -r "$PROJECT_DIR/fonts"         "$STAGING/"

cat > "$STAGING/install.sh" << 'EOF'
#!/bin/bash
set -e
PACKAGE_DIR="$(cd "$(dirname "$(readlink -f "$0")")" && pwd)"
INSTALL_DIR="$HOME/.local/share/zhonyal"
APPS_DIR="$HOME/.local/share/applications"

echo "==> Installing Zhonyal to $INSTALL_DIR..."
mkdir -p "$INSTALL_DIR"
cp "$PACKAGE_DIR/zhonyal.jar" "$INSTALL_DIR/"
cp -r "$PACKAGE_DIR/images"   "$INSTALL_DIR/"
cp -r "$PACKAGE_DIR/fonts"    "$INSTALL_DIR/"

cat > "$INSTALL_DIR/zhonyal" << 'LAUNCHER'
#!/bin/bash
cd "$(dirname "$(readlink -f "$0")")"
exec java -Dawt.useSystemAAFontSettings=lcd_hrgb -Dswing.aatext=true -jar zhonyal.jar "$@"
LAUNCHER
chmod +x "$INSTALL_DIR/zhonyal"

mkdir -p "$APPS_DIR"
cat > "$APPS_DIR/zhonyal.desktop" << DESKTOP
[Desktop Entry]
Name=Zhonyal
Comment=Study session tracker
Exec=$INSTALL_DIR/zhonyal
Icon=$INSTALL_DIR/images/Logo.png
Type=Application
Categories=Education;
Terminal=false
StartupWMClass=zhonyal
DESKTOP

echo "==> Done. Launch Zhonyal from your app drawer or run:"
echo "    $INSTALL_DIR/zhonyal"
EOF
chmod +x "$STAGING/install.sh"

cp "$PROJECT_DIR/install.ps1" "$STAGING/"

cd "$PROJECT_DIR/dist"
zip -r "$PACKAGE_NAME.zip" "$PACKAGE_NAME"
rm -rf "$STAGING"

echo "==> dist/$PACKAGE_NAME.zip ready for GitHub Release"
