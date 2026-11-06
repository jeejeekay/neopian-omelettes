package net.igobyjake.neopianomelettes.datagen;

import net.igobyjake.neopianomelettes.neopianomelettes;
import net.igobyjake.neopianomelettes.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, neopianomelettes.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Every stage of every omelette is a flat item using textures/item/<name>.png
        for (Holder<Item> item : ModItems.ITEMS.getEntries()) {
            itemModels.generateFlatItem(item.value(), ModelTemplates.FLAT_ITEM);
        }
    }
}
