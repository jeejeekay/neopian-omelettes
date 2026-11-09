#!/usr/bin/env bash
# Runs a Gradle command; if it fails, copies the useful part of the log into a
# GitHub "error annotation" so it shows on the commit's checks without opening logs.
# usage: .github/scripts/gradle-run.sh <label> <gradle args...>
label="$1"; shift
log="$(mktemp)"
# Up to 3 tries, but only when the failure is downloading Gradle itself (its servers sometimes time out / 504).
for try in 1 2 3; do
  ./gradlew "$@" --console=plain 2>&1 | tee "$log"
  code=${PIPESTATUS[0]}
  [ "$code" -eq 0 ] && break
  grep -qE "IOException.*(gradle-distributions|services\.gradle\.org|gradle-[0-9.]+-bin\.zip)|Downloading from https://services\.gradle\.org" "$log" || break
  echo "::warning title=${label}::Gradle download failed (try $try of 3), retrying in 20 s"
  sleep 20
done
if [ "$code" -ne 0 ]; then
  {
    grep -E "^\S+\.java:[0-9]+: error:|^e: |error: |^> |What went wrong|Could not|Caused by|Exception|FAILED|[Ff]ailed|GameTest|neopianomelettes:" "$log" \
      | grep -vE "^> (Task|Configure)" | head -60
  } > "$log.err"
  msg=$(sed 's/%/%25/g' "$log.err" | awk '{printf "%s%%0A", $0}')
  echo "::error title=${label} failed::${msg}"
fi
exit "$code"
