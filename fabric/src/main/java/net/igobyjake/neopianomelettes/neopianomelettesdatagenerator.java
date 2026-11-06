package net.igobyjake.neopianomelettes;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.igobyjake.neopianomelettes.datagen.ModModelProvider;

// Runs when you use ./gradlew runDatagen (writes into src/main/generated)
public class neopianomelettesdatagenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModModelProvider::new);
    }
}
