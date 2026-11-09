# Neopian Omelettes

A Minecraft mod that adds 17 powerful omelettes, each eaten in three bites. By IGoByJake.
Modrinth: https://modrinth.com/mod/neopian-omelettes

## Layout

Each loader is its own separate Gradle project — open each folder on its own in IntelliJ.

| Folder      | Loader   |
|-------------|----------|
| `neoforge/` | NeoForge |
| `fabric/`   | Fabric   |

## Branches = Minecraft versions

| Branch       | Minecraft | NeoForge   | Fabric API | Java | Status |
|--------------|-----------|------------|------------|------|--------|
| `mc/1.21.11` | 1.21.11   | 21.11.48   | 0.141.6    | 21   | written, not built yet |
| `mc/26.1`    | 26.1.2    | 26.1.2.125 | 0.155.3    | 25   | written, not built yet |
| `mc/26.2`    | 26.2      | 26.2.0.66  | 0.161.0    | 25   | written, not built yet |
| `mc/26.3`    | 26.3      | 26.3.0.100 | 0.165.2    | 25   | written, not built yet |

Build settings (Gradle, Loom, ModDevGradle, versions) are copied from Rose Gold Equipment's branches,
which build and pass their tests on GitHub Actions. Each branch was made from the one above it.

To work on a version: `git switch mc/<version>` (IntelliJ: bottom-right branch menu).

## How the omelettes work
`ModItems.omelette("bacon", food, effect)` registers three items: `bacon_omelette` →
`bacon_omelette_2` (Nibbled) → `bacon_omelette_3` (Partial). Eating one leaves the next
(`usingConvertsTo`); the last bite is used up. Food values/effects: `item/ModFoodProperties.java`,
which is the same file on both loaders.

To add a new omelette: one `omelette(...)` line in `ModItems` (both loaders), food + effect in
`ModFoodProperties` (both), 3 textures, 3 lang lines, a recipe, then run datagen.

## Building the jars yourself

| | Windows | macOS / Linux |
|---|---|---|
| 1. One-time setup (installs JDK 25 + Git) | double-click `install.bat` | `./install.sh` |
| 2. Build every version, both loaders | double-click `compile.bat` | `./compile.sh` |

The jars land in `dist/`, named like `neopianomelettes-fabric-26.3-1.3.0.jar`; build logs are in `dist/logs/`.
Options (both scripts): a version to build only that one (`compile.bat 26.3`), `--here` to build just the
folder you have open *including uncommitted changes*, `--test` to also run the GameTests.

Each version is built from its branch in a separate `.build/` folder, so the branch you have open is never switched.
Only committed code is built (except with `--here`). JDK 25 builds every version; Gradle fetches the rest itself.

## Testing
Every push is built and tested automatically: GitHub → **Actions** tab. See `docs/TESTING.md`.

## Docs
- `docs/TESTING.md` — what the automated tests check, and the 2-minute visual check
- `docs/CHANGES-1.3.0.md` — what changed from 1.2.5 (Fabric) / 1.2.1 (NeoForge)
- `docs/ports/` — one checklist per loader per version, with what changed
- `docs/port-checklist.md` — blank template for the next Minecraft version
