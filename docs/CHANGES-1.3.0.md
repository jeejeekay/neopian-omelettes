# Neopian Omelettes 1.3.0 — what changed

Starting points: Fabric **1.2.5** (Minecraft 1.21.6–1.21.11) and NeoForge **1.2.1** (Minecraft 1.21.1),
from the two `src.zip` uploads. Item IDs and the mod ID are unchanged, so existing worlds keep their omelettes.

## The two loaders now behave the same
They had drifted apart. **The Fabric 1.2.5 values were kept everywhere** (newest release); NeoForge
players will notice these differences:

| | NeoForge 1.2.1 (old) | 1.3.0 (both loaders) |
|---|---|---|
| Meat Feast | Regeneration only | + Absorption I for 30 s |
| Beet | 8 hunger, Strength 10 s | 7 hunger, Strength 30 s |
| Rabbit | sat. 0.6, Jump II + Speed 10 s | sat. 0.9, 30 s |
| Glow | 8 hunger | 7 hunger |
| Sweet | sat. 0.5 | sat. 0.7 |
| Kelp / Nether Shroom | 120 s | 60 s |
| Recipes | 3 plain eggs; 1 berry / glow berry / mushroom | any egg (white, brown, blue); 3 sweet berries / glow berries / mushrooms |
| Partial (`_3`) omelettes | stacked to 64 | don't stack (bug) |
| Plain omelette textures | older version | Fabric's newer textures |

If you'd rather keep a NeoForge value, change it in `ModFoodProperties.java` and copy that file to the other loader.

## Code clean-up
- **NeoForge:** 34 `foodstuffs/*Omelette(Two).java` classes replaced by `usingConvertsTo(...)` (what
  Fabric already did). One `omelette("name", food, effect)` line registers all three stages.
- **NeoForge:** split into `ModItems` / `ModFoodProperties` / `ModCreativeModeTabs` like Fabric; package
  renamed `igobyjake.NeopianOmelettes` → `net.igobyjake.neopianomelettes` (same as Fabric and Rose Gold).
  Removed template leftovers (config-screen imports, empty server/client events, `RAW_ROSE_GOLD_BLOCK_ITEM` comment).
- **NeoForge:** item models now come from datagen (`ModModelProvider`) in the 1.21.4+ format
  (`assets/neopianomelettes/items/*.json`). The old hand-written `models/item` files had no `items/` file,
  so they would show as missing textures on 1.21.4+.
- **Fabric:** Yarn → Mojang names (needed for 26.1). Deleted `ExampleMixin` (it would crash the game on
  versions where `loadWorld` is renamed) and the mixins file; deleted the empty client entrypoint.
- **Fabric:** `fabric.mod.json` links pointed at Fabric's example mod; now Modrinth + GitHub.
  `"minecraft": "~1.21"` → exact version per branch.
- **Both:** one creative-tab translation key (`itemgroup.neopianomelettes.neopian_omelettes_tab`).
- **Both:** automated GameTests and a `/function neopianomelettes:showcase` for the visual check.

## Left as they were (tell me if any are unintended)
- Meat omelettes give **Regeneration XI for 4–12 ticks** (0.2–0.6 s) — a quick heal burst. Same trick as Rose Gold.
- Every recipe uses recipe-book group `neopianomelettes:plain_omelette`, so the recipe book shows all
  17 under one icon.
- The plain omelette is in `#minecraft:meat`, so tamed wolves eat it.
- The last bite leaves nothing behind (no bowl), matching the recipes, which use no bowl.
- Recipe names: "Sausage" uses mutton, "Bacon/Ham" uses porkchop, "Steak" uses beef.
