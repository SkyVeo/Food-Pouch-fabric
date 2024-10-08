package skyveo.foodpouch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import skyveo.foodpouch.item.ModItems;

@Deprecated
public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.FOOD_POUCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.IRON_FOOD_POUCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLD_FOOD_POUCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_FOOD_POUCH, Models.GENERATED);
        itemModelGenerator.register(ModItems.NETHERITE_FOOD_POUCH, Models.GENERATED);
    }
}
