package skyveo.foodpouch.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import skyveo.foodpouch.item.ModItemTags;
import skyveo.foodpouch.item.ModItems;

import java.util.concurrent.CompletableFuture;

@Deprecated
public class ModRecipeProvider {
//    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
//        super(output, registriesFuture);
//    }
//
//    protected static void offerFoodPouchUpgradeRecipe(RecipeExporter exporter, Item addition, Item foodPouch, Item result) {
//        SmithingTransformRecipeJsonBuilder.create(
//                Ingredient.ofItems(Items.LEATHER),
//                Ingredient.ofItems(foodPouch),
//                Ingredient.ofItems(addition),
//                RecipeCategory.TOOLS,
//                result
//        )
//                .criterion(hasItem(addition), conditionsFromItem(addition))
//                .offerTo(exporter, getItemPath(result));
//    }
//
//    @Override
//    public void generate(RecipeExporter exporter) {
//        ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.FOOD_POUCH)
//                .input(Items.STRING)
//                .input(ModItemTags.FOOD_POUCH_CRAFTING_FOOD_INGREDIENTS)
//                .input(Items.LEATHER)
//                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
//                .offerTo(exporter);
//
//        offerFoodPouchUpgradeRecipe(exporter, Items.IRON_INGOT, ModItems.FOOD_POUCH, ModItems.IRON_FOOD_POUCH);
//        offerFoodPouchUpgradeRecipe(exporter, Items.GOLD_INGOT, ModItems.IRON_FOOD_POUCH, ModItems.GOLD_FOOD_POUCH);
//        offerFoodPouchUpgradeRecipe(exporter, Items.DIAMOND, ModItems.GOLD_FOOD_POUCH, ModItems.DIAMOND_FOOD_POUCH);
//        offerNetheriteUpgradeRecipe(exporter, ModItems.DIAMOND_FOOD_POUCH, RecipeCategory.TOOLS, ModItems.NETHERITE_FOOD_POUCH);
//    }
}
