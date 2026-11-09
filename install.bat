@echo off
rem One-time setup: installs JDK 25 and Git. Double-click this file.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\install.ps1" %*
set CODE=%ERRORLEVEL%
pause
exit /b %CODE%
