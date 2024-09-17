package skyveo.foodpouch.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.Nullable;
import skyveo.foodpouch.FoodPouch;
import skyveo.foodpouch.item.custom.FoodPouchItem;

public class ModItems {
    public static final Item FOOD_POUCH = register("food_pouch", new FoodPouchItem(), ItemGroups.TOOLS);
    public static final Item IRON_FOOD_POUCH = register("iron_food_pouch", new FoodPouchItem(2), ItemGroups.TOOLS);
    public static final Item GOLD_FOOD_POUCH = register("golden_food_pouch", new FoodPouchItem(3), ItemGroups.TOOLS);
    public static final Item DIAMOND_FOOD_POUCH = register("diamond_food_pouch", new FoodPouchItem(4), ItemGroups.TOOLS);
    public static final Item NETHERITE_FOOD_POUCH = register("netherite_food_pouch", new FoodPouchItem(5, new Item.Settings().fireproof()), ItemGroups.TOOLS);

    public static <T extends Item> T register(String name, T item) {
        return register(name, item, null);
    }

    public static <T extends Item> T register(String name, T item, @Nullable RegistryKey<ItemGroup> group) {
        T registeredItem = Registry.register(Registries.ITEM, FoodPouch.id(name), item);
        if (group != null) {
            ItemGroupEvents.modifyEntriesEvent(group).register((entries) -> entries.add(registeredItem));
        }
        return registeredItem;
    }

    public static void load() {
    }
}
