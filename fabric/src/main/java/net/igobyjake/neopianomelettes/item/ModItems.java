package net.igobyjake.neopianomelettes.item;

import net.igobyjake.neopianomelettes.neopianomelettes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModItems {
    // Every registered item (all three stages of every omelette). Used by the model datagen.
    public static final List<Item> ALL = new ArrayList<>();

    // Each omelette() call registers three items:
    //   <name>_omelette  ->  eat  ->  <name>_omelette_2 ("Nibbled")  ->  eat  ->  <name>_omelette_3 ("Partial")  ->  eat  ->  gone
    public static final Item PLAIN_OMELETTE = omelette("plain", ModFoodProperties.PLAIN_OMELETTE, null);

    public static final Item BACON_OMELETTE = omelette("bacon", ModFoodProperties.ONE_MEAT_OMELETTE, ModFoodProperties.ONE_MEAT_OMELETTE_EFFECT);
    public static final Item SAUSAGE_OMELETTE = omelette("sausage", ModFoodProperties.ONE_MEAT_OMELETTE, ModFoodProperties.ONE_MEAT_OMELETTE_EFFECT);
    public static final Item STEAK_OMELETTE = omelette("steak", ModFoodProperties.ONE_MEAT_OMELETTE, ModFoodProperties.ONE_MEAT_OMELETTE_EFFECT);

    public static final Item SAUSAGE_HAM_OMELETTE = omelette("sausage_ham", ModFoodProperties.TWO_MEAT_OMELETTE, ModFoodProperties.TWO_MEAT_OMELETTE_EFFECT);
    public static final Item STEAK_HAM_OMELETTE = omelette("steak_ham", ModFoodProperties.TWO_MEAT_OMELETTE, ModFoodProperties.TWO_MEAT_OMELETTE_EFFECT);
    public static final Item STEAK_SAUSAGE_OMELETTE = omelette("steak_sausage", ModFoodProperties.TWO_MEAT_OMELETTE, ModFoodProperties.TWO_MEAT_OMELETTE_EFFECT);

    public static final Item MEAT_FEAST_OMELETTE = omelette("meat_feast", ModFoodProperties.THREE_MEAT_OMELETTE, ModFoodProperties.THREE_MEAT_OMELETTE_EFFECT);

    public static final Item FISHY_OMELETTE = omelette("fishy", ModFoodProperties.FISHY_OMELETTE, ModFoodProperties.FISHY_OMELETTE_EFFECT);
    public static final Item MUSHROOM_OMELETTE = omelette("mushroom", ModFoodProperties.MUSHROOM_OMELETTE, ModFoodProperties.MUSHROOM_OMELETTE_EFFECT);
    public static final Item CHORUS_OMELETTE = omelette("chorus", ModFoodProperties.CHORUS_OMELETTE, ModFoodProperties.CHORUS_OMELETTE_EFFECT);
    public static final Item RABBIT_OMELETTE = omelette("rabbit", ModFoodProperties.RABBIT_OMELETTE, ModFoodProperties.RABBIT_OMELETTE_EFFECT);
    public static final Item BEET_OMELETTE = omelette("beet", ModFoodProperties.BEET_OMELETTE, ModFoodProperties.BEET_OMELETTE_EFFECT);
    public static final Item GLOW_OMELETTE = omelette("glow", ModFoodProperties.GLOW_OMELETTE, ModFoodProperties.GLOW_OMELETTE_EFFECT);

    public static final Item KELP_OMELETTE = omelette("kelp", ModFoodProperties.KELP_OMELETTE, ModFoodProperties.KELP_OMELETTE_EFFECT);
    public static final Item SWEET_OMELETTE = omelette("sweet", ModFoodProperties.SWEET_OMELETTE, ModFoodProperties.SWEET_OMELETTE_EFFECT);
    public static final Item NETHER_SHROOM_OMELETTE = omelette("nether_shroom", ModFoodProperties.NETHER_SHROOM_OMELETTE, ModFoodProperties.NETHER_SHROOM_OMELETTE_EFFECT);

    /** Registers the three stages of one omelette and returns the full (uneaten) one. */
    private static Item omelette(String name, FoodProperties food, Consumable effect) {
        Item partial = registerItem(name + "_omelette_3", properties -> new Item(stage(properties, food, effect)));
        Item nibbled = registerItem(name + "_omelette_2", properties -> new Item(stage(properties, food, effect).usingConvertsTo(partial)));
        return registerItem(name + "_omelette", properties -> new Item(stage(properties, food, effect).usingConvertsTo(nibbled)));
    }

    // Same for every stage: doesn't stack, same food value, same effect. (null effect = just food, no potion effect)
    private static Item.Properties stage(Item.Properties properties, FoodProperties food, Consumable effect) {
        properties.stacksTo(1);
        return effect == null ? properties.food(food) : properties.food(food, effect);
    }

    private static Item registerItem(String name, Function<Item.Properties, Item> function) {
        Identifier id = Identifier.fromNamespaceAndPath(neopianomelettes.MOD_ID, name);
        Item item = Registry.register(BuiltInRegistries.ITEM, id,
                function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
        ALL.add(item);
        return item;
    }

    public static void registerModItems() {
        neopianomelettes.LOGGER.info("You approach the massive omelette...\n... and manage to take a slice!!!");
    }
}
