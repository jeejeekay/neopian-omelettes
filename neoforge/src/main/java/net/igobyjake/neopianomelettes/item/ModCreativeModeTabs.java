package net.igobyjake.neopianomelettes.item;

import net.igobyjake.neopianomelettes.neopianomelettes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, neopianomelettes.MOD_ID);

    // Only the full omelettes are listed; the nibbled/partial stages appear when you eat them.
    public static final Supplier<CreativeModeTab> NEOPIAN_OMELETTES_TAB = CREATIVE_MODE_TABS.register("neopian_omelettes_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemgroup.neopianomelettes.neopian_omelettes_tab"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> new ItemStack(ModItems.MEAT_FEAST_OMELETTE.get()))
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

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
