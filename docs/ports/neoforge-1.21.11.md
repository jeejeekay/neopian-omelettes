# NeoForge 1.21.1 → 1.21.11  (branch `mc/1.21.11`)

## Done (by Claude) — builds and all GameTests pass on GitHub Actions
- [x] Build files copied from Rose Gold's working `mc/1.21.11` (NeoForge 21.11.48, ModDevGradle 2.0.123, Gradle 8.9 wrapper, Java 21)
- [x] `minecraft_version_range=[1.21.11,1.22)`, `mod_version=1.3.0`
- [x] Item registration: `ITEMS.registerItem(name, properties -> ...)` (1.21.2+ items need their ID set in the properties; `registerItem` does it)
- [x] Food: `FoodProperties.effect(...)` was removed in 1.21.2 → effects moved to a `Consumable` (`ModFoodProperties`)
- [x] Effect renames: `DIG_SPEED`→`HASTE`, `DAMAGE_BOOST`→`STRENGTH`, `JUMP`→`JUMP_BOOST`, `MOVEMENT_SPEED`→`SPEED`, `DAMAGE_RESISTANCE`→`RESISTANCE`
- [x] Item models: `items/*.json` files (1.21.4+) via datagen; pre-generated copies committed in `src/generated/resources`
- [x] Recipes: 1.21.2+ ingredient format (`"#minecraft:eggs"` instead of `{"item": ...}`)
- [x] `EventBusSubscriber.Bus.MOD` removed (gone in 21.6+); the class had only empty handlers anyway

## Still to do
- [x] `./gradlew build` (Actions green)
- [x] datagen: no diff
- [ ] Visual check in `docs/TESTING.md`

## Notes / mistakes found
-
