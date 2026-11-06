package net.igobyjake.neopianomelettes.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.igobyjake.neopianomelettes.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModels) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModels) {
        // Every stage of every omelette is a flat item using textures/item/<name>.png
        for (Item item : ModItems.ALL) {
            itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
        }
    }
}
