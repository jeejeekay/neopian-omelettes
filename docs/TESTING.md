# Testing

## 1. Automatic (every push)
GitHub → **Actions**. Each push builds both loaders, runs datagen and the GameTests in
`src/gametest/.../OmeletteChecks.java` (the same file on both loaders):

| Test | Checks |
|---|---|
| `eating` | all 17 omelettes × 3 stages exist, don't stack, are always edible, give the right hunger and effects, and full → nibbled → partial → used up |
| `recipes` | every recipe loads and crafts from 3 eggs (white, brown and blue mixed) + its ingredients |
| `meat_tag` | plain omelette is in `#minecraft:meat` |
| `showcase_loads` | the `/function neopianomelettes:showcase` file still parses |

Green = good. Red = click it; the error is shown at the top.
Locally: `./gradlew build` (Fabric, includes the tests) or `./gradlew runGameTestServer` (NeoForge).

## 2. Visual check (about 2 minutes, only you can do this)
`./gradlew runClient`, Creative world with cheats, then:
- [ ] Open the **Neopian Omelettes** creative tab: 17 omelettes, all with textures (no purple/black)
- [ ] `/function neopianomelettes:showcase` gives all 51 items: every icon has a texture and a real name
      (not `item.neopianomelettes.…`)
- [ ] Switch to Survival, eat one omelette all the way down: icon changes each bite, effect icons appear
- [ ] No `ERROR` lines mentioning `neopianomelettes` in the log
