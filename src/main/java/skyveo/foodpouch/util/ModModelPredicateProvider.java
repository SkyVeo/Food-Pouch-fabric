package skyveo.foodpouch.util;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import skyveo.foodpouch.FoodPouch;
import skyveo.foodpouch.item.ModItems;

public class ModModelPredicateProvider {
    public static void registerFoodPouchEating(Item foodPouch) {
        ModelPredicateProviderRegistry.register(
                foodPouch,
                FoodPouch.id("eating"),
                (stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );
    }

    public static void load() {
        registerFoodPouchEating(ModItems.FOOD_POUCH);
        registerFoodPouchEating(ModItems.IRON_FOOD_POUCH);
        registerFoodPouchEating(ModItems.GOLD_FOOD_POUCH);
        registerFoodPouchEating(ModItems.DIAMOND_FOOD_POUCH);
        registerFoodPouchEating(ModItems.NETHERITE_FOOD_POUCH);
    }
}
