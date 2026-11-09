#!/usr/bin/env bash
# One-time setup for building Neopian Omelettes on macOS or Linux:
# installs JDK 25 (Eclipse Temurin) and Git if they're missing.
# JDK 25 builds every version; Gradle downloads anything else it needs (incl. JDK 21) by itself.
# Windows: use install.bat instead.
set -u

have_java25() {
  for c in "${JAVA_HOME:-}/bin/java" /Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home/bin/java \
           /usr/lib/jvm/temurin-25-jdk-*/bin/java /usr/lib/jvm/java-25-openjdk*/bin/java "$(command -v java 2>/dev/null)"; do
    if [ -x "$c" ]; then
      v="$("$c" -version 2>&1 | grep -m1 'version "' | sed -E 's/.*version "([0-9]+).*/\1/')"
      [ "$v" -ge 25 ] 2>/dev/null && { echo "JDK $v found: $c"; return 0; }
    fi
  done
  return 1
}

case "$(uname -s)" in
  Darwin)
    if ! command -v brew >/dev/null; then
      echo "Homebrew is needed first. Install it from https://brew.sh, then run this script again."; exit 1
    fi
    have_java25 || brew install --cask temurin@25
    command -v git >/dev/null || brew install git
    ;;
  Linux)
    if ! have_java25; then
      if command -v apt-get >/dev/null; then
        # Adoptium's apt repository (https://adoptium.net/installation/linux/)
        sudo apt-get update && sudo apt-get install -y wget apt-transport-https gpg
        sudo mkdir -p /etc/apt/keyrings
        wget -qO - https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor | sudo tee /etc/apt/keyrings/adoptium.gpg >/dev/null
        echo "deb [signed-by=/etc/apt/keyrings/adoptium.gpg] https://packages.adoptium.net/artifactory/deb $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") main" \
          | sudo tee /etc/apt/sources.list.d/adoptium.list >/dev/null
        sudo apt-get update && sudo apt-get install -y temurin-25-jdk
      elif command -v dnf >/dev/null; then
        sudo dnf install -y java-25-openjdk-devel
      elif command -v pacman >/dev/null; then
        sudo pacman -S --needed jdk-openjdk
      else
        echo "Install JDK 25 yourself from https://adoptium.net, then run this again."; exit 1
      fi
    fi
    if ! command -v git >/dev/null; then
      if command -v apt-get >/dev/null; then sudo apt-get install -y git
      elif command -v dnf >/dev/null; then sudo dnf install -y git
      elif command -v pacman >/dev/null; then sudo pacman -S --needed git; fi
    fi
    ;;
  *) echo "Unknown system. On Windows run install.bat."; exit 1 ;;
esac

echo
if have_java25 && command -v git >/dev/null; then
  echo "All set. Build the mod with:  ./compile.sh"
else
  echo "Something is still missing (see above). Open a NEW terminal and run this script again."
  exit 1
fi
