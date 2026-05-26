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
