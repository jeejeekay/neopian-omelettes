package net.igobyjake.neopianomelettes.item;

import net.igobyjake.neopianomelettes.neopianomelettes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(neopianomelettes.MOD_ID);

    // Each omelette() call registers three items:
    //   <name>_omelette  ->  eat  ->  <name>_omelette_2 ("Nibbled")  ->  eat  ->  <name>_omelette_3 ("Partial")  ->  eat  ->  gone
    // (Before 1.3.0 each stage had its own class in "foodstuffs"; usingConvertsTo does the same job now.)
    public static final DeferredItem<Item> PLAIN_OMELETTE = omelette("plain", ModFoodProperties.PLAIN_OMELETTE, null);

    public static final DeferredItem<Item> BACON_OMELETTE = omelette("bacon", ModFoodProperties.ONE_MEAT_OMELETTE, ModFoodProperties.ONE_MEAT_OMELETTE_EFFECT);
    public static final DeferredItem<Item> SAUSAGE_OMELETTE = omelette("sausage", ModFoodProperties.ONE_MEAT_OMELETTE, ModFoodProperties.ONE_MEAT_OMELETTE_EFFECT);
    public static final DeferredItem<Item> STEAK_OMELETTE = omelette("steak", ModFoodProperties.ONE_MEAT_OMELETTE, ModFoodProperties.ONE_MEAT_OMELETTE_EFFECT);

    public static final DeferredItem<Item> SAUSAGE_HAM_OMELETTE = omelette("sausage_ham", ModFoodProperties.TWO_MEAT_OMELETTE, ModFoodProperties.TWO_MEAT_OMELETTE_EFFECT);
    public static final DeferredItem<Item> STEAK_HAM_OMELETTE = omelette("steak_ham", ModFoodProperties.TWO_MEAT_OMELETTE, ModFoodProperties.TWO_MEAT_OMELETTE_EFFECT);
    public static final DeferredItem<Item> STEAK_SAUSAGE_OMELETTE = omelette("steak_sausage", ModFoodProperties.TWO_MEAT_OMELETTE, ModFoodProperties.TWO_MEAT_OMELETTE_EFFECT);

    public static final DeferredItem<Item> MEAT_FEAST_OMELETTE = omelette("meat_feast", ModFoodProperties.THREE_MEAT_OMELETTE, ModFoodProperties.THREE_MEAT_OMELETTE_EFFECT);

    public static final DeferredItem<Item> FISHY_OMELETTE = omelette("fishy", ModFoodProperties.FISHY_OMELETTE, ModFoodProperties.FISHY_OMELETTE_EFFECT);
    public static final DeferredItem<Item> MUSHROOM_OMELETTE = omelette("mushroom", ModFoodProperties.MUSHROOM_OMELETTE, ModFoodProperties.MUSHROOM_OMELETTE_EFFECT);
    public static final DeferredItem<Item> CHORUS_OMELETTE = omelette("chorus", ModFoodProperties.CHORUS_OMELETTE, ModFoodProperties.CHORUS_OMELETTE_EFFECT);
    public static final DeferredItem<Item> RABBIT_OMELETTE = omelette("rabbit", ModFoodProperties.RABBIT_OMELETTE, ModFoodProperties.RABBIT_OMELETTE_EFFECT);
    public static final DeferredItem<Item> BEET_OMELETTE = omelette("beet", ModFoodProperties.BEET_OMELETTE, ModFoodProperties.BEET_OMELETTE_EFFECT);
    public static final DeferredItem<Item> GLOW_OMELETTE = omelette("glow", ModFoodProperties.GLOW_OMELETTE, ModFoodProperties.GLOW_OMELETTE_EFFECT);

    public static final DeferredItem<Item> KELP_OMELETTE = omelette("kelp", ModFoodProperties.KELP_OMELETTE, ModFoodProperties.KELP_OMELETTE_EFFECT);
    public static final DeferredItem<Item> SWEET_OMELETTE = omelette("sweet", ModFoodProperties.SWEET_OMELETTE, ModFoodProperties.SWEET_OMELETTE_EFFECT);
    public static final DeferredItem<Item> NETHER_SHROOM_OMELETTE = omelette("nether_shroom", ModFoodProperties.NETHER_SHROOM_OMELETTE, ModFoodProperties.NETHER_SHROOM_OMELETTE_EFFECT);

    /** Registers the three stages of one omelette and returns the full (uneaten) one. */
    private static DeferredItem<Item> omelette(String name, FoodProperties food, Consumable effect) {
        DeferredItem<Item> partial = ITEMS.registerItem(name + "_omelette_3",
                properties -> new Item(stage(properties, food, effect)));
        DeferredItem<Item> nibbled = ITEMS.registerItem(name + "_omelette_2",
                properties -> new Item(stage(properties, food, effect).usingConvertsTo(partial.get())));
        return ITEMS.registerItem(name + "_omelette",
                properties -> new Item(stage(properties, food, effect).usingConvertsTo(nibbled.get())));
    }

    // Same for every stage: doesn't stack, same food value, same effect. (null effect = just food, no potion effect)
    private static Item.Properties stage(Item.Properties properties, FoodProperties food, Consumable effect) {
        properties.stacksTo(1);
        return effect == null ? properties.food(food) : properties.food(food, effect);
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
