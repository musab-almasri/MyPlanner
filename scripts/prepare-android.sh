#!/usr/bin/env bash
# Creates the Android project from the web files and patches it.
# Usage: bash scripts/prepare-android.sh widget|nowidget
set -euo pipefail
MODE="${1:-widget}"
rm -rf android
npx cap add android
npx cap sync android
bash scripts/patch-android.sh "$MODE"
