# One-time setup for building Neopian Omelettes on Windows (run install.bat, not this file).
# Installs JDK 25 (Eclipse Temurin) and Git with winget if they're missing.
# JDK 25 builds every version; Gradle downloads anything else it needs (incl. JDK 21) by itself.

function Find-Java25 {
    $candidates = @()
    if ($env:JAVA_HOME) { $candidates += Join-Path $env:JAVA_HOME 'bin\java.exe' }
    $candidates += Get-ChildItem 'C:\Program Files\Eclipse Adoptium\jdk-2*\bin\java.exe' -ErrorAction SilentlyContinue | ForEach-Object FullName
    $onPath = Get-Command java.exe -ErrorAction SilentlyContinue
    if ($onPath) { $candidates += $onPath.Source }
    foreach ($c in $candidates) {
        if (-not (Test-Path $c)) { continue }
        $line = (& $c -version 2>&1 | ForEach-Object { $_.ToString() } | Where-Object { $_ -match 'version "' } | Select-Object -First 1)
        if ($line -match 'version "(\d+)' -and [int]$Matches[1] -ge 25) { return $c }
    }
    return $null
}

if (-not (Get-Command winget -ErrorAction SilentlyContinue)) {
    Write-Host 'winget is missing. Install "App Installer" from the Microsoft Store, or install JDK 25 from https://adoptium.net and Git from https://git-scm.com, then run this again.'
    exit 1
}

$java = Find-Java25
if ($java) { Write-Host "JDK 25 found: $java" }
else {
    Write-Host 'Installing JDK 25 (Eclipse Temurin)...'
    winget install --id EclipseAdoptium.Temurin.25.JDK --exact --accept-source-agreements --accept-package-agreements
}

if (Get-Command git -ErrorAction SilentlyContinue) { Write-Host 'Git found.' }
else {
    Write-Host 'Installing Git...'
    winget install --id Git.Git --exact --accept-source-agreements --accept-package-agreements
}

Write-Host ''
$java = Find-Java25
if ($java) {
    Write-Host 'All set. Build the mod by double-clicking compile.bat'
    Write-Host '(If Git was just installed, close this window and open a new one first.)'
} else {
    Write-Host 'JDK 25 still not found. Close this window, open a new one, and run install.bat again.'
    exit 1
}
