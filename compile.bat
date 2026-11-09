@echo off
rem Builds the mod jars for every Minecraft version into dist\. Double-click this file,
rem or run from a terminal:  compile.bat 26.3   /   compile.bat --here   /   compile.bat --test
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\compile.ps1" %*
set CODE=%ERRORLEVEL%
pause
exit /b %CODE%
