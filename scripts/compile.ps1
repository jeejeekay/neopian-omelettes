# Builds the mod jars for every Minecraft version (both loaders) into dist\.
# Run compile.bat (double-click, or from a terminal):
#   compile.bat                  every mc/* branch
#   compile.bat 26.3 1.21.11     only these versions
#   compile.bat --here           only the folder you're in, including changes you haven't committed
#   compile.bat --test ...       also run the GameTests (slower)
#
# Each branch is built in its own folder under .build\ (a "git worktree"), so your
# current branch and uncommitted work are never touched. Needs JDK 25 (run install.bat).

$Root = Split-Path -Parent $PSScriptRoot
Set-Location $Root

$Here = $false; $Test = $false; $Versions = @()
foreach ($a in $args) {
    switch -regex ($a) {
        '^--here$' { $Here = $true; continue }
        '^--test$' { $Test = $true; continue }
        '^mc/'     { $Versions += $a.Substring(3); continue }
        default    { $Versions += $a }
    }
}

# ---- Java 25 ----
function Get-JavaMajor($exe) {
    $line = (& $exe -version 2>&1 | ForEach-Object { $_.ToString() } | Where-Object { $_ -match 'version "' } | Select-Object -First 1)
    if ($line -match 'version "(\d+)') { return [int]$Matches[1] } else { return 0 }
}
$candidates = @()
if ($env:JAVA_HOME) { $candidates += Join-Path $env:JAVA_HOME 'bin\java.exe' }
$candidates += Get-ChildItem 'C:\Program Files\Eclipse Adoptium\jdk-2*\bin\java.exe' -ErrorAction SilentlyContinue | ForEach-Object FullName
$onPath = Get-Command java.exe -ErrorAction SilentlyContinue
if ($onPath) { $candidates += $onPath.Source }
$java = $candidates | Where-Object { (Test-Path $_) -and ((Get-JavaMajor $_) -ge 25) } | Select-Object -First 1
if (-not $java) { Write-Host 'JDK 25 not found. Run install.bat first (or set JAVA_HOME to a JDK 25).'; exit 1 }
$env:JAVA_HOME = Split-Path -Parent (Split-Path -Parent $java)
Write-Host "Using Java: $env:JAVA_HOME"

function Get-Prop($file, $name) {
    $line = Get-Content $file | Where-Object { $_ -match "^$name=" } | Select-Object -First 1
    return ($line -split '=', 2)[1].Trim()
}

New-Item -ItemType Directory -Force -Path (Join-Path $Root 'dist\logs') | Out-Null
$Results = New-Object System.Collections.Generic.List[string]

function Invoke-Gradle($dir, $task, $log, [switch]$Append) {
    Push-Location $dir
    $redir = if ($Append) { '>>' } else { '>' }
    cmd /c "gradlew.bat $task --console=plain $redir `"$log`" 2>&1"
    $code = $LASTEXITCODE
    Pop-Location
    return $code
}

function Build-One($dir, $label) {
    $props = Join-Path $dir 'fabric\gradle.properties'
    $mc = Get-Prop $props 'minecraft_version'
    $mv = Get-Prop $props 'mod_version'
    foreach ($loader in 'neoforge', 'fabric') {
        $log = Join-Path $Root "dist\logs\$label-$loader.log"
        Write-Host ''
        Write-Host "=== $label  $loader  (Minecraft $mc, mod $mv)   log: dist\logs\$label-$loader.log"
        $ldir = Join-Path $dir $loader
        $out = "neopianomelettes-$loader-$mc-$mv.jar"
        Remove-Item (Join-Path $Root "dist\$out") -ErrorAction SilentlyContinue   # never leave an old jar that looks current
        if ((Invoke-Gradle $ldir 'build' $log) -ne 0) {
            Select-String -Path $log -Pattern 'error:|What went wrong|^> [^T]|FAILED' | Select-Object -First 15 | ForEach-Object { Write-Host $_.Line }
            $Results.Add("FAILED  $label $loader   (see dist\logs\$label-$loader.log)"); continue
        }
        $jar = Get-ChildItem (Join-Path $ldir 'build\libs\*.jar') -ErrorAction SilentlyContinue |
               Where-Object { $_.Name -notmatch '-sources|-dev|-javadoc' } | Select-Object -First 1
        if (-not $jar) { $Results.Add("FAILED  $label $loader   (built, but no jar in build\libs)"); continue }
        Copy-Item $jar.FullName (Join-Path $Root "dist\$out") -Force
        $note = "dist\$out"
        if ($Test) {
            $task = if ($loader -eq 'neoforge') { 'runGameTestServer' } else { 'runGameTest' }
            if ((Invoke-Gradle $ldir $task $log -Append) -eq 0) { $note += '  + tests passed' } else { $note += '  but TESTS FAILED (see log)' }
        }
        $Results.Add("OK      $label $loader   $note")
    }
}

if ($Here) {
    $branch = (git branch --show-current) -replace '/', '-'
    Build-One $Root "here-$branch"
} else {
    git fetch --quiet origin 2>$null
    if ($LASTEXITCODE -ne 0) { Write-Host "(couldn't reach GitHub, using the branches already on this computer)" }
    if ($Versions.Count -eq 0) {
        $Versions = git for-each-ref --format='%(refname:short)' refs/heads/mc/ refs/remotes/origin/mc/ |
            ForEach-Object { $_ -replace '^origin/', '' -replace '^mc/', '' } | Sort-Object -Unique |
            Sort-Object { try { [version]$_ } catch { [version]'0.0' } }
    }
    if (-not $Versions) { Write-Host 'No mc/* branches found.'; exit 1 }
    if (git status --porcelain) {
        Write-Host 'Note: you have uncommitted changes. They are NOT in these builds (only committed code is). Use --here to build them.'
    }
    New-Item -ItemType Directory -Force -Path (Join-Path $Root '.build') | Out-Null
    foreach ($v in $Versions) {
        # your local branch if you have one (it may have commits you haven't pushed), otherwise GitHub's
        git rev-parse -q --verify "refs/heads/mc/$v" *> $null
        if ($LASTEXITCODE -eq 0) { $ref = "mc/$v" }
        else {
            git rev-parse -q --verify "refs/remotes/origin/mc/$v" *> $null
            if ($LASTEXITCODE -eq 0) { $ref = "origin/mc/$v" } else { $Results.Add("FAILED  $v   (no branch mc/$v)"); continue }
        }
        $wt = Join-Path $Root ".build\mc-$v"
        if (Test-Path (Join-Path $wt '.git')) { git -C $wt checkout -q --force --detach $ref }
        else {
            if (Test-Path $wt) { Remove-Item -Recurse -Force $wt }
            git worktree prune
            git worktree add -q --force --detach $wt $ref
        }
        Build-One $wt $v
    }
}

Write-Host ''
Write-Host '================ Summary ================'
$Results | ForEach-Object { Write-Host $_ }
Write-Host "Jars are in: $Root\dist"
if ($Results | Where-Object { $_ -match '^FAILED|TESTS FAILED' }) { exit 1 } else { exit 0 }
