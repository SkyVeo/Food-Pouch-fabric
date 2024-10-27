package skyveo.foodpouch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import skyveo.foodpouch.item.ModItemTags;
import skyveo.foodpouch.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            private void offerFoodPouchUpgradeRecipe(Item addition, Item foodPouch, Item result) {
                SmithingTransformRecipeJsonBuilder.create(
                        Ingredient.ofItems(Items.LEATHER),
                        Ingredient.ofItems(foodPouch),
                        Ingredient.ofItems(addition),
                        RecipeCategory.TOOLS, result
                )
                        .criterion(RecipeGenerator.hasItem(addition), conditionsFromItem(addition))
                        .offerTo(exporter, RecipeGenerator.getItemPath(result) + "_smithing");
            }

            @Override
            public void generate() {
                ShapelessRecipeJsonBuilder.create(registryLookup.getOrThrow(RegistryKeys.ITEM), RecipeCategory.TOOLS, ModItems.FOOD_POUCH)
                        .input(Items.STRING)
                        .input(ModItemTags.FOOD_POUCH_CRAFTING_FOOD_INGREDIENTS)
                        .input(Items.LEATHER)
                        .criterion(RecipeGenerator.hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                        .offerTo(exporter);

                offerFoodPouchUpgradeRecipe(Items.IRON_INGOT, ModItems.FOOD_POUCH, ModItems.IRON_FOOD_POUCH);
                offerFoodPouchUpgradeRecipe(Items.GOLD_INGOT, ModItems.IRON_FOOD_POUCH, ModItems.GOLD_FOOD_POUCH);
                offerFoodPouchUpgradeRecipe(Items.DIAMOND, ModItems.GOLD_FOOD_POUCH, ModItems.DIAMOND_FOOD_POUCH);
                offerNetheriteUpgradeRecipe(ModItems.DIAMOND_FOOD_POUCH, RecipeCategory.TOOLS, ModItems.NETHERITE_FOOD_POUCH);
            }
        };
    }

    @Override
    public String getName() {
        return "Food Pouch Mod - Recipes";
    }
}
