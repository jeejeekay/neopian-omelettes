package net.igobyjake.neopianomelettes.datagen;

import net.igobyjake.neopianomelettes.neopianomelettes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

// Runs when you use the "data" run configuration (./gradlew runData), writes into src/generated/resources.
// GatherDataEvent.Client generates BOTH client assets and server data.
@EventBusSubscriber(modid = neopianomelettes.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        generator.addProvider(true, new ModModelProvider(packOutput));
    }
}
