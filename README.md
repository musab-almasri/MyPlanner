# My Planner (Android)

A personal planner with three tabs:

- **Tasks**: add, edit, delete, tick off, optional due date, filter All / To do / Done
- **Notes**: write, edit, delete and search notes
- **Calendar**: monthly view, add your own events (title, date, time, details, colour), edit and delete them

Everything is stored on the phone (localStorage), so it works offline.

## Get the APK (no installs needed)

1. Create a new repository on GitHub.
2. Upload **all files from this folder** (keep the structure, including the hidden `.github` folder), or push with git:
   ```
   git init
   git add .
   git commit -m "My Planner"
   git branch -M main
   git remote add origin https://github.com/<your-name>/<your-repo>.git
   git push -u origin main
   ```
3. Open the **Actions** tab of the repository. The workflow "Build Android APK" starts by itself (about 5 minutes). If it does not, click it and press **Run workflow**.
4. When it is green, open the run and download **MyPlanner-APK** under *Artifacts*. Unzip it to get `app-debug.apk`.
5. Copy the APK to your phone and install it (allow "install unknown apps" when asked).

## Edit the app

The whole interface lives in `www/index.html` (HTML, CSS and JavaScript in one file).
Change it, push to GitHub, and a new APK is built automatically. Opening `www/index.html` in a browser also works for quick testing.

## Project layout

```
www/index.html                  the app
capacitor.config.json           app name and id (com.myplanner.app)
package.json                    Capacitor dependencies
.github/workflows/build-apk.yml builds the APK on GitHub
```

The `android/` folder is generated during the build, so it is not included.

## Notes

- The APK is a *debug* build: fine for personal use. For Google Play you would need a signed release build.
- To rename the app, change `appName` in `capacitor.config.json`.
