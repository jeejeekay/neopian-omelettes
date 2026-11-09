#!/usr/bin/env bash
# Builds the mod jars for every Minecraft version (both loaders) into dist/.
#
#   ./compile.sh                  every mc/* branch
#   ./compile.sh 26.3 1.21.11     only these versions
#   ./compile.sh --here           only the folder you're in, including changes you haven't committed
#   ./compile.sh --test ...       also run the GameTests (slower)
#
# Each branch is built in its own folder under .build/ (a "git worktree"), so your
# current branch and uncommitted work are never touched. Needs JDK 25 (run ./install.sh).
# Windows: use compile.bat instead.
set -u
cd "$(dirname "$0")"
ROOT="$(pwd)"

HERE=0; TEST=0; VERSIONS=()
for a in "$@"; do
  case "$a" in
    --here) HERE=1 ;;
    --test) TEST=1 ;;
    -h|--help) sed -n '2,11p' "$0"; exit 0 ;;
    mc/*) VERSIONS+=("${a#mc/}") ;;
    *) VERSIONS+=("$a") ;;
  esac
done

# ---- Java 25 ----
java_major() { "$1" -version 2>&1 | grep -m1 'version "' | sed -E 's/.*version "([0-9]+).*/\1/'; }
JAVA=""
for c in "${JAVA_HOME:-}/bin/java" \
         /Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home/bin/java \
         /usr/lib/jvm/temurin-25-jdk-*/bin/java /usr/lib/jvm/java-25-openjdk*/bin/java \
         "$(command -v java 2>/dev/null)"; do
  if [ -x "$c" ] && [ "$(java_major "$c")" -ge 25 ] 2>/dev/null; then JAVA="$c"; break; fi
done
if [ -z "$JAVA" ]; then
  echo "JDK 25 not found. Run ./install.sh first (or set JAVA_HOME to a JDK 25)."; exit 1
fi
export JAVA_HOME="$(cd "$(dirname "$JAVA")/.." && pwd)"
echo "Using Java: $JAVA_HOME"

prop() { grep -E "^$2=" "$1" | head -1 | cut -d= -f2- | tr -d '\r'; }

mkdir -p dist/logs
RESULTS=()

# build_one <folder> <label>
build_one() {
  local dir="$1" label="$2" loader mc mv jar log task
  mc="$(prop "$dir/fabric/gradle.properties" minecraft_version)"
  mv="$(prop "$dir/fabric/gradle.properties" mod_version)"
  for loader in neoforge fabric; do
    log="$ROOT/dist/logs/$label-$loader.log"
    echo; echo "=== $label  $loader  (Minecraft $mc, mod $mv)   log: dist/logs/$label-$loader.log"
    rm -f "$ROOT/dist/neopianomelettes-$loader-$mc-$mv.jar"   # never leave an old jar that looks current
    chmod +x "$dir/$loader/gradlew" 2>/dev/null
    if ! (cd "$dir/$loader" && ./gradlew build --console=plain) >"$log" 2>&1; then
      grep -E "error:|What went wrong|^> [^T]|FAILED" "$log" | head -15
      RESULTS+=("FAILED  $label $loader   (see dist/logs/$label-$loader.log)"); continue
    fi
    jar="$(ls "$dir/$loader/build/libs/"*.jar 2>/dev/null | grep -v -e '-sources' -e '-dev' -e '-javadoc' | head -1)"
    if [ -z "$jar" ]; then RESULTS+=("FAILED  $label $loader   (built, but no jar in build/libs)"); continue; fi
    cp "$jar" "$ROOT/dist/neopianomelettes-$loader-$mc-$mv.jar"
    local note="dist/neopianomelettes-$loader-$mc-$mv.jar"
    if [ "$TEST" = 1 ]; then
      if [ "$loader" = neoforge ]; then task=runGameTestServer; else task=runGameTest; fi
      if (cd "$dir/$loader" && ./gradlew "$task" --console=plain) >>"$log" 2>&1; then note="$note  + tests passed"
      else note="$note  but TESTS FAILED (see log)"; fi
    fi
    RESULTS+=("OK      $label $loader   $note")
  done
}

if [ "$HERE" = 1 ]; then
  build_one "$ROOT" "here-$(git branch --show-current 2>/dev/null | tr '/' '-')"
else
  git fetch --quiet origin 2>/dev/null || echo "(couldn't reach GitHub, using the branches already on this computer)"
  if [ ${#VERSIONS[@]} -eq 0 ]; then
    while read -r v; do VERSIONS+=("$v"); done < <(
      git for-each-ref --format='%(refname:short)' refs/heads/mc/ refs/remotes/origin/mc/ \
        | sed -e 's#^origin/##' -e 's#^mc/##' | sort -u -V)
  fi
  [ ${#VERSIONS[@]} -eq 0 ] && { echo "No mc/* branches found."; exit 1; }
  [ -n "$(git status --porcelain 2>/dev/null)" ] && \
    echo "Note: you have uncommitted changes. They are NOT in these builds (only committed code is). Use --here to build them."
  mkdir -p .build
  for v in "${VERSIONS[@]}"; do
    # your local branch if you have one (it may have commits you haven't pushed), otherwise GitHub's
    if git rev-parse -q --verify "refs/heads/mc/$v" >/dev/null; then ref="mc/$v"
    elif git rev-parse -q --verify "refs/remotes/origin/mc/$v" >/dev/null; then ref="origin/mc/$v"
    else RESULTS+=("FAILED  $v   (no branch mc/$v)"); continue; fi
    wt="$ROOT/.build/mc-$v"
    if [ -e "$wt/.git" ]; then git -C "$wt" checkout -q --force --detach "$ref"
    else rm -rf "$wt"; git worktree prune; git worktree add -q --force --detach "$wt" "$ref"; fi
    build_one "$wt" "$v"
  done
fi

echo; echo "================ Summary ================"
printf '%s\n' "${RESULTS[@]}"
echo "Jars are in: $ROOT/dist"
printf '%s\n' "${RESULTS[@]}" | grep -q '^FAILED\|TESTS FAILED' && exit 1 || exit 0
