# Fabric 1.21.11 clean-up + Yarn → Mojang names  (branch `mc/1.21.11`)

## Done (by Claude) — builds and all GameTests pass on GitHub Actions
- [x] Build files copied from Rose Gold's working `mc/1.21.11` (Loom 1.19 `fabric-loom-remap`, Mojang mappings, Fabric API 0.141.6, Loader 0.19.5)
- [x] Renames: `Item.Settings`→`Item.Properties`, `maxCount`→`stacksTo`, `useRemainder`→`usingConvertsTo`,
      `FoodComponent`→`FoodProperties`, `ConsumableComponent(s)`→`Consumable(s)`, `StatusEffectInstance`→`MobEffectInstance`,
      `StatusEffects`→`MobEffects`, `ApplyEffectsConsumeEffect`→`ApplyStatusEffectsConsumeEffect`, `ItemGroup`→`CreativeModeTab`,
      `Text`→`Component`, `Registries`→`BuiltInRegistries`, `Models.GENERATED`→`ModelTemplates.FLAT_ITEM`
- [x] Deleted `ExampleMixin` + mixins file, empty client entrypoint
- [x] `fabric.mod.json`: real links, `"minecraft": "~1.21.11"`; `mod_version=1.3.0`

## Still to do
- [x] `./gradlew build` (Actions green)
- [x] datagen: no diff
- [ ] Visual check in `docs/TESTING.md`

## Notes / mistakes found
-
