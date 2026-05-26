#!/usr/bin/env bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOGIN="${1:-i32casaj}"
ZIP_NAME="${LOGIN}_interfaz.zip"
BUNDLE_DIR="${PROJECT_DIR}/dist/${LOGIN}_interfaz"

echo "=> Building JAR..."
bash "$PROJECT_DIR/build.sh"

echo "=> Creating bundle..."
rm -rf "$BUNDLE_DIR"
mkdir -p "$BUNDLE_DIR/codigo"

# Java source (preserving package dirs)
cp "$PROJECT_DIR/Main.java"  "$BUNDLE_DIR/codigo/"
cp "$PROJECT_DIR/App.java"   "$BUNDLE_DIR/codigo/"
for dir in helpers ui utils services models; do
  [ -d "$PROJECT_DIR/$dir" ] && cp -r "$PROJECT_DIR/$dir" "$BUNDLE_DIR/codigo/"
done
cp -r "$PROJECT_DIR/i18n" "$BUNDLE_DIR/codigo/"

# Runtime assets & deps
cp -r "$PROJECT_DIR/lib"    "$BUNDLE_DIR/"
cp -r "$PROJECT_DIR/images" "$BUNDLE_DIR/"
cp -r "$PROJECT_DIR/fonts"  "$BUNDLE_DIR/"

# Executable JAR
cp "$PROJECT_DIR/dist/zhonyal.jar" "$BUNDLE_DIR/"

echo ""
echo "  ┌─────────────────────────────────────────────────┐"
echo "  │  IMPORTANT: add your documentacion.pdf to:      │"
echo "  │  dist/${LOGIN}_interfaz/                         │"
echo "  │  before zipping!                                 │"
echo "  └─────────────────────────────────────────────────┘"
echo ""
read -rp "  Press ENTER when you've added the PDF (or skip to zip without it)..."

echo "=> Zipping..."
cd "$PROJECT_DIR/dist"
rm -f "$ZIP_NAME"
zip -r "$ZIP_NAME" "${LOGIN}_interfaz/"
rm -rf "$BUNDLE_DIR"

echo "=> dist/${ZIP_NAME} ready"
