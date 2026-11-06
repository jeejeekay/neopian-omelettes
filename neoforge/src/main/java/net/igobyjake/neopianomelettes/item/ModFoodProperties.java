package net.igobyjake.neopianomelettes.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

// Hunger + saturation (FoodProperties) and what happens when you finish eating (Consumable).
// This file is identical on Fabric and NeoForge - change one, copy it to the other.
// Effect durations are in ticks: 20 ticks = 1 second.
public class ModFoodProperties {

    // ---- Hunger / saturation ----
    public static final FoodProperties PLAIN_OMELETTE = food(5, 0.3F);
    public static final FoodProperties ONE_MEAT_OMELETTE = food(6, 0.4F);
    public static final FoodProperties TWO_MEAT_OMELETTE = food(8, 0.6F);
    public static final FoodProperties THREE_MEAT_OMELETTE = food(10, 0.8F);
    public static final FoodProperties FISHY_OMELETTE = food(6, 0.4F);
    public static final FoodProperties MUSHROOM_OMELETTE = food(6, 0.4F);
    public static final FoodProperties CHORUS_OMELETTE = food(6, 0.4F);
    public static final FoodProperties BEET_OMELETTE = food(7, 0.6F);
    public static final FoodProperties RABBIT_OMELETTE = food(8, 0.9F);
    public static final FoodProperties GLOW_OMELETTE = food(7, 0.6F);
    public static final FoodProperties SWEET_OMELETTE = food(5, 0.7F);
    public static final FoodProperties KELP_OMELETTE = food(6, 0.4F);
    public static final FoodProperties NETHER_SHROOM_OMELETTE = food(6, 0.4F);

    // ---- Effects ----
    // The meat omelettes give a tiny burst of Regeneration XI (4-12 ticks = 0.2-0.6 s).
    public static final Consumable ONE_MEAT_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.REGENERATION, 4, 10)))
            .build();
    public static final Consumable TWO_MEAT_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.REGENERATION, 8, 10)))
            .build();
    public static final Consumable THREE_MEAT_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0)))
            .onConsume(effect(new MobEffectInstance(MobEffects.REGENERATION, 12, 10)))
            .build();
    public static final Consumable FISHY_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.LUCK, 6000, 0)))
            .build();
    public static final Consumable MUSHROOM_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.HASTE, 3000, 0)))
            .build();
    public static final Consumable CHORUS_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.LEVITATION, 100, 2)))
            .onConsume(effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0)))
            .build();
    public static final Consumable BEET_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.STRENGTH, 600, 0)))
            .build();
    public static final Consumable RABBIT_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.JUMP_BOOST, 600, 1)))
            .onConsume(effect(new MobEffectInstance(MobEffects.SPEED, 600, 0)))
            .build();
    public static final Consumable GLOW_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.GLOWING, 3000, 0)))
            .onConsume(effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3000, 0)))
            .build();
    public static final Consumable SWEET_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.RESISTANCE, 600, 0)))
            .build();
    public static final Consumable KELP_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 0)))
            .build();
    public static final Consumable NETHER_SHROOM_OMELETTE_EFFECT = Consumables.defaultFood()
            .onConsume(effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 0)))
            .build();

    private static FoodProperties food(int nutrition, float saturation) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).alwaysEdible().build();
    }

    private static ApplyStatusEffectsConsumeEffect effect(MobEffectInstance instance) {
        return new ApplyStatusEffectsConsumeEffect(instance, 1.0F);
    }
}
