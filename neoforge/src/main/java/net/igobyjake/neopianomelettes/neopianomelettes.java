package net.igobyjake.neopianomelettes;

import com.mojang.logging.LogUtils;
import net.igobyjake.neopianomelettes.item.ModCreativeModeTabs;
import net.igobyjake.neopianomelettes.item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(neopianomelettes.MOD_ID)
public class neopianomelettes {
    public static final String MOD_ID = "neopianomelettes";
    public static final Logger LOGGER = LogUtils.getLogger();

    public neopianomelettes(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("You approach the massive omelette...\n... and manage to take a slice!!!");
    }
}
