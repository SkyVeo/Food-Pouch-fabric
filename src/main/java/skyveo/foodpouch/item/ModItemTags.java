package skyveo.foodpouch.item;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import skyveo.foodpouch.FoodPouch;

public class ModItemTags {
    public static final TagKey<Item> FOOD_POUCHES = register("food_pouches");
    public static final TagKey<Item> FOOD_POUCH_CRAFTING_FOOD_INGREDIENTS = register("food_pouch_crafting_food_ingredients");

    private static TagKey<Item> register(String name) {
        return TagKey.of(RegistryKeys.ITEM, FoodPouch.id(name));
    }
}
