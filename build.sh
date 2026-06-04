#!/bin/bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
DIST_DIR="$PROJECT_DIR/dist"
TMP_DIR="$DIST_DIR/tmp"

echo "==> Compiling..."
mkdir -p "$PROJECT_DIR/out"
javac -cp "$PROJECT_DIR/lib/*" -d "$PROJECT_DIR/out" -sourcepath "$PROJECT_DIR" "$PROJECT_DIR/Main.java"
cp -r "$PROJECT_DIR/i18n" "$PROJECT_DIR/out/"

echo "==> Building fat JAR..."
rm -rf "$TMP_DIR"
mkdir -p "$TMP_DIR"

# Extract all dependency JARs (suppress duplicate warnings)
for jar in "$PROJECT_DIR/lib/"*.jar; do
    unzip -q -o "$jar" -d "$TMP_DIR" 2>/dev/null || true
done

# Remove signature files that would break the fat JAR
rm -rf "$TMP_DIR/META-INF"

# sqlite-jdbc bundles native libs for every OS/arch; keep only the current platform
NATIVE_DIR="$TMP_DIR/org/sqlite/native"
if [ -d "$NATIVE_DIR" ]; then
    OS=$(uname -s)   # Linux, Darwin, Windows, …
    ARCH=$(uname -m) # x86_64, aarch64, …
    find "$NATIVE_DIR" -mindepth 1 -maxdepth 1 -type d ! -name "$OS" -exec rm -rf {} +
    [ -d "$NATIVE_DIR/$OS" ] && find "$NATIVE_DIR/$OS" -mindepth 1 -maxdepth 1 -type d ! -name "$ARCH" -exec rm -rf {} +
fi

# Copy compiled classes and i18n bundles
cp -r "$PROJECT_DIR/out/"* "$TMP_DIR/"

# Write manifest
mkdir -p "$TMP_DIR/META-INF"
printf "Main-Class: Main\n\n" > "$TMP_DIR/META-INF/MANIFEST.MF"

# Pack
mkdir -p "$DIST_DIR"
jar --create --file="$DIST_DIR/zhonyal.jar" --manifest="$TMP_DIR/META-INF/MANIFEST.MF" -C "$TMP_DIR" .

rm -rf "$TMP_DIR"
echo "==> dist/zhonyal.jar ready"
