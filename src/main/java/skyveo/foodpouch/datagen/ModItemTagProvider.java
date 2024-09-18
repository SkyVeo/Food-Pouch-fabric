package skyveo.foodpouch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import skyveo.foodpouch.item.ModItemTags;
import skyveo.foodpouch.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ItemTags.DYEABLE)
                .add(ModItems.FOOD_POUCH)
                .add(ModItems.IRON_FOOD_POUCH)
                .add(ModItems.GOLD_FOOD_POUCH)
                .add(ModItems.DIAMOND_FOOD_POUCH)
                .add(ModItems.NETHERITE_FOOD_POUCH);

        getOrCreateTagBuilder(ModItemTags.FOOD_POUCH_CRAFTING_FOOD_INGREDIENTS)
                .add(Items.APPLE)
                .add(Items.GOLDEN_APPLE)
                .add(Items.ENCHANTED_GOLDEN_APPLE)
                .add(Items.MELON_SLICE)
                .add(Items.SWEET_BERRIES)
                .add(Items.GLOW_BERRIES)
                .add(Items.CHORUS_FRUIT)
                .add(Items.CARROT)
                .add(Items.GOLDEN_CARROT)
                .add(Items.POTATO)
                .add(Items.BAKED_POTATO)
                .add(Items.POISONOUS_POTATO)
                .add(Items.BEETROOT)
                .add(Items.DRIED_KELP)
                .add(Items.BEEF)
                .add(Items.COOKED_BEEF)
                .add(Items.PORKCHOP)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.MUTTON)
                .add(Items.COOKED_MUTTON)
                .add(Items.CHICKEN)
                .add(Items.COOKED_CHICKEN)
                .add(Items.RABBIT)
                .add(Items.COOKED_RABBIT)
                .add(Items.COD)
                .add(Items.COOKED_COD)
                .add(Items.SALMON)
                .add(Items.COOKED_SALMON)
                .add(Items.TROPICAL_FISH)
                .add(Items.PUFFERFISH)
                .add(Items.BREAD)
                .add(Items.COOKIE)
                .add(Items.PUMPKIN_PIE)
                .add(Items.ROTTEN_FLESH)
                .add(Items.SPIDER_EYE)
                .add(Items.MUSHROOM_STEW)
                .add(Items.BEETROOT_SOUP)
                .add(Items.RABBIT_STEW)
                .add(Items.SUSPICIOUS_STEW);
    }
}
