package skyveo.foodpouch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.component.type.DyedColorComponent;
import skyveo.foodpouch.item.ModItems;
import skyveo.foodpouch.util.ModModelPredicateProvider;

public class FoodPouchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register(
                (stack, tintIndex) -> tintIndex == 0 ? DyedColorComponent.getColor(stack, -6265536) : -1,
                ModItems.FOOD_POUCH,
                ModItems.IRON_FOOD_POUCH,
                ModItems.GOLD_FOOD_POUCH,
                ModItems.DIAMOND_FOOD_POUCH,
                ModItems.NETHERITE_FOOD_POUCH
        );

        ModModelPredicateProvider.load();
    }
}
