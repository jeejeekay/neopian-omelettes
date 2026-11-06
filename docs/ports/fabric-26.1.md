# Fabric 1.21.11 → 26.1.2  (branch `mc/26.1`)

## Done (by Claude, not built yet) — same changes Rose Gold needed, which build there
- [x] Fabric API 0.155.3+26.1.2, Loader 0.19.5, Loom 1.16 (`net.fabricmc.fabric-loom`), Gradle 9.4.1, Java 25
- [x] No `mappings` line; `modImplementation` → `implementation`
- [x] Renames: `FabricDataOutput`→`FabricPackOutput`, `FabricItemGroup`→`FabricCreativeModeTab`
- [x] `fabric.mod.json`: `"minecraft": "~26.1.2"`, Java 25, Loader 0.19.2+

## Still to do
- [ ] Push → Actions green (build, datagen, GameTests)
- [ ] Visual check in `docs/TESTING.md`

## Notes / mistakes found
-
