#!/usr/bin/env bash
# Adjusts the generated android/ project: permissions, signing key and (optionally) the home screen widget.
# Usage: bash scripts/patch-android.sh widget|nowidget
set -euo pipefail
MODE="${1:-widget}"
M=android/app/src/main/AndroidManifest.xml

for p in POST_NOTIFICATIONS SCHEDULE_EXACT_ALARM USE_EXACT_ALARM RECEIVE_BOOT_COMPLETED VIBRATE RECORD_AUDIO MODIFY_AUDIO_SETTINGS; do
  grep -q "android.permission.$p" "$M" || sed -i "s#<application#<uses-permission android:name=\"android.permission.$p\" />\n    <application#" "$M"
done

mkdir -p "$HOME/.android"
cp debug.keystore "$HOME/.android/debug.keystore"

if [ "$MODE" = "widget" ]; then
  J=android/app/src/main/java/com/myplanner/app
  R=android/app/src/main/res
  mkdir -p "$J" "$R/layout" "$R/xml" "$R/drawable"
  cp native/PlannerWidgetProvider.java native/PlannerWidgetPlugin.java native/MainActivity.java "$J/"
  cp native/planner_widget.xml "$R/layout/planner_widget.xml"
  cp native/planner_widget_info.xml "$R/xml/planner_widget_info.xml"
  cp native/widget_bg.xml "$R/drawable/widget_bg.xml"
  python3 - <<'PY'
m = "android/app/src/main/AndroidManifest.xml"
s = open(m, encoding="utf-8").read()
r = open("native/receiver.xml", encoding="utf-8").read()
if "PlannerWidgetProvider" not in s:
    assert "</application>" in s
    s = s.replace("</application>", r + "    </application>", 1)
    open(m, "w", encoding="utf-8").write(s)
PY
fi
echo "Android project ready (mode: $MODE)"
grep "uses-permission" "$M" || true
