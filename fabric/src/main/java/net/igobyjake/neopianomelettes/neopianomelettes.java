package net.igobyjake.neopianomelettes;

import net.fabricmc.api.ModInitializer;
import net.igobyjake.neopianomelettes.item.ModItemGroups;
import net.igobyjake.neopianomelettes.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Very important comment
public class neopianomelettes implements ModInitializer {
	public static final String MOD_ID = "neopianomelettes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
	}
}
