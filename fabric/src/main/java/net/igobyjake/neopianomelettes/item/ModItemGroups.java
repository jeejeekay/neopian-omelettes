package net.igobyjake.neopianomelettes.item;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.igobyjake.neopianomelettes.neopianomelettes;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {
    // Only the full omelettes are listed; the nibbled/partial stages appear when you eat them.
    public static final CreativeModeTab NEOPIAN_OMELETTES_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(neopianomelettes.MOD_ID, "neopian_omelettes_tab"),
            FabricCreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MEAT_FEAST_OMELETTE))
                    .title(Component.translatable("itemgroup.neopianomelettes.neopian_omelettes_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.PLAIN_OMELETTE);

                        output.accept(ModItems.BACON_OMELETTE);
                        output.accept(ModItems.SAUSAGE_OMELETTE);
                        output.accept(ModItems.STEAK_OMELETTE);

                        output.accept(ModItems.SAUSAGE_HAM_OMELETTE);
                        output.accept(ModItems.STEAK_HAM_OMELETTE);
                        output.accept(ModItems.STEAK_SAUSAGE_OMELETTE);

                        output.accept(ModItems.MEAT_FEAST_OMELETTE);

                        output.accept(ModItems.FISHY_OMELETTE);
                        output.accept(ModItems.MUSHROOM_OMELETTE);
                        output.accept(ModItems.CHORUS_OMELETTE);
                        output.accept(ModItems.RABBIT_OMELETTE);
                        output.accept(ModItems.BEET_OMELETTE);
                        output.accept(ModItems.GLOW_OMELETTE);

                        output.accept(ModItems.KELP_OMELETTE);
                        output.accept(ModItems.SWEET_OMELETTE);
                        output.accept(ModItems.NETHER_SHROOM_OMELETTE);
                    }).build());

    public static void registerItemGroups() {
    }
}
