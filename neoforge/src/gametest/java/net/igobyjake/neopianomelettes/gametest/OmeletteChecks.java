package net.igobyjake.neopianomelettes.gametest;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;

import java.util.ArrayList;
import java.util.List;

/**
 * The automated version of docs/TESTING.md.
 *
 * Only uses vanilla classes and looks our items up by name, so this exact file
 * works on both Fabric and NeoForge. Each public method is one GameTest; the
 * loader-specific files (OmeletteGameTests) just hook them up.
 */
public final class OmeletteChecks {
    private static final String MOD = "neopianomelettes";

    /** name, hunger, effects it must give (effect + amplifier), extra crafting ingredients besides 3 eggs */
    private record Omelette(String name, int nutrition, List<Expected> effects, List<Item> ingredients) {}
    private record Expected(Holder<MobEffect> effect, int amplifier) {}

    private static Expected fx(Holder<MobEffect> effect, int amplifier) { return new Expected(effect, amplifier); }

    private static final List<Omelette> OMELETTES = List.of(
            new Omelette("plain", 5, List.of(), List.of()),
            new Omelette("bacon", 6, List.of(fx(MobEffects.REGENERATION, 10)), List.of(Items.PORKCHOP)),
            new Omelette("sausage", 6, List.of(fx(MobEffects.REGENERATION, 10)), List.of(Items.MUTTON)),
            new Omelette("steak", 6, List.of(fx(MobEffects.REGENERATION, 10)), List.of(Items.BEEF)),
            new Omelette("sausage_ham", 8, List.of(fx(MobEffects.REGENERATION, 10)), List.of(Items.MUTTON, Items.PORKCHOP)),
            new Omelette("steak_ham", 8, List.of(fx(MobEffects.REGENERATION, 10)), List.of(Items.PORKCHOP, Items.BEEF)),
            new Omelette("steak_sausage", 8, List.of(fx(MobEffects.REGENERATION, 10)), List.of(Items.MUTTON, Items.BEEF)),
            new Omelette("meat_feast", 10, List.of(fx(MobEffects.ABSORPTION, 0), fx(MobEffects.REGENERATION, 10)),
                    List.of(Items.PORKCHOP, Items.BEEF, Items.MUTTON)),
            new Omelette("fishy", 6, List.of(fx(MobEffects.LUCK, 0)), List.of(Items.COD)),
            new Omelette("mushroom", 6, List.of(fx(MobEffects.HASTE, 0)), List.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM)),
            new Omelette("chorus", 6, List.of(fx(MobEffects.LEVITATION, 2), fx(MobEffects.SLOW_FALLING, 0)), List.of(Items.CHORUS_FRUIT)),
            new Omelette("rabbit", 8, List.of(fx(MobEffects.JUMP_BOOST, 1), fx(MobEffects.SPEED, 0)), List.of(Items.RABBIT, Items.CARROT, Items.CARROT)),
            new Omelette("beet", 7, List.of(fx(MobEffects.STRENGTH, 0)), List.of(Items.BEETROOT, Items.BEETROOT, Items.BEETROOT)),
            new Omelette("glow", 7, List.of(fx(MobEffects.GLOWING, 0), fx(MobEffects.NIGHT_VISION, 0)),
                    List.of(Items.GLOW_BERRIES, Items.GLOW_BERRIES, Items.GLOW_BERRIES)),
            new Omelette("kelp", 6, List.of(fx(MobEffects.WATER_BREATHING, 0)), List.of(Items.DRIED_KELP, Items.DRIED_KELP, Items.DRIED_KELP)),
            new Omelette("sweet", 5, List.of(fx(MobEffects.RESISTANCE, 0)), List.of(Items.SWEET_BERRIES, Items.SWEET_BERRIES, Items.SWEET_BERRIES)),
            new Omelette("nether_shroom", 6, List.of(fx(MobEffects.FIRE_RESISTANCE, 0)), List.of(Items.CRIMSON_FUNGUS)));

    private OmeletteChecks() {}

    static Item item(String name) {
        Item item = BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(MOD, name));
        if (item == null || item == Items.AIR) throw new IllegalStateException("missing item " + MOD + ":" + name);
        return item;
    }

    private static ItemStack eat(GameTestHelper h, Player player, String name, int nutrition) {
        ItemStack stack = new ItemStack(item(name));
        h.assertTrue(stack.getMaxStackSize() == 1, name + " should not stack, max stack size is " + stack.getMaxStackSize());
        FoodProperties props = stack.get(DataComponents.FOOD);
        h.assertTrue(props != null && props.canAlwaysEat(), name + " should be edible even when full");
        h.assertTrue(props.nutrition() == nutrition, name + " should fill " + nutrition + " hunger, fills " + props.nutrition());
        return stack.finishUsingItem(h.getLevel(), player);
    }

    /** Every omelette: full -> nibbled (_2) -> partial (_3) -> nothing, right hunger and effects on every bite. */
    public static void eating(GameTestHelper h) {
        Player p = h.makeMockPlayer(GameType.SURVIVAL);
        for (Omelette o : OMELETTES) {
            String full = o.name() + "_omelette";
            String[] stages = {full, full + "_2", full + "_3"};
            for (int i = 0; i < 3; i++) {
                p.removeAllEffects();
                ItemStack left = eat(h, p, stages[i], o.nutrition());
                if (i < 2) {
                    h.assertTrue(left.is(item(stages[i + 1])), stages[i] + " should leave " + stages[i + 1] + ", left " + left);
                } else {
                    h.assertTrue(left.isEmpty(), stages[i] + " should be used up, left " + left);
                }
                for (Expected e : o.effects()) {
                    MobEffectInstance inst = p.getEffect(e.effect());
                    h.assertTrue(inst != null && inst.getAmplifier() == e.amplifier(),
                            stages[i] + " should give " + e.effect().getRegisteredName() + " level " + (e.amplifier() + 1) + ", got " + inst);
                }
            }
        }
        h.succeed();
    }

    /** Every recipe loads and crafts: 3 eggs (any colour) + the ingredients, in a crafting table. */
    public static void recipes(GameTestHelper h) {
        Item[] eggs = {Items.EGG, Items.BROWN_EGG, Items.BLUE_EGG};
        for (Omelette o : OMELETTES) {
            List<ItemStack> grid = new ArrayList<>();
            for (Item egg : eggs) grid.add(new ItemStack(egg));
            for (Item extra : o.ingredients()) grid.add(new ItemStack(extra));
            while (grid.size() < 9) grid.add(ItemStack.EMPTY);

            ResourceKey<Recipe<?>> expected = ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath(MOD, o.name() + "_omelette"));
            var found = h.getLevel().recipeAccess().getRecipeFor(RecipeType.CRAFTING,
                    CraftingInput.of(3, 3, grid), h.getLevel(), (ResourceKey<Recipe<?>>) null);
            h.assertTrue(found.isPresent(), "no crafting recipe matches 3 eggs + " + o.ingredients() + " (expected " + expected + ")");
            h.assertTrue(found.get().id().equals(expected), "3 eggs + " + o.ingredients() + " crafts " + found.get().id() + ", expected " + expected);
        }
        h.succeed();
    }

    /** Plain omelettes count as meat (so tamed wolves eat them) - that's what data/minecraft/tags/item/meat.json does. */
    public static void meatTag(GameTestHelper h) {
        h.assertTrue(new ItemStack(item("plain_omelette")).is(ItemTags.MEAT), "plain_omelette is not in #minecraft:meat");
        h.succeed();
    }

    /** The /function neopianomelettes:showcase file parsed on this Minecraft version (one bad line drops the whole file). */
    public static void showcaseLoads(GameTestHelper h) {
        boolean loaded = h.getLevel().getServer().getFunctions()
                .get(Identifier.fromNamespaceAndPath(MOD, "showcase")).isPresent();
        h.assertTrue(loaded, "function neopianomelettes:showcase failed to load - check the log for the bad command");
        h.succeed();
    }
}
