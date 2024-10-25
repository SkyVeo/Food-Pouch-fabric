package skyveo.foodpouch.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.Nullable;
import skyveo.foodpouch.FoodPouch;
import skyveo.foodpouch.item.custom.FoodPouchItem;

public class ModItems {
    public static final Item FOOD_POUCH = registerFoodPouch("food_pouch", FoodPouchMaterials.LEATHER);
    public static final Item IRON_FOOD_POUCH = registerFoodPouch("iron_food_pouch", FoodPouchMaterials.IRON);
    public static final Item GOLD_FOOD_POUCH = registerFoodPouch("golden_food_pouch", FoodPouchMaterials.GOLD);
    public static final Item DIAMOND_FOOD_POUCH = registerFoodPouch("diamond_food_pouch", FoodPouchMaterials.DIAMOND);
    public static final Item NETHERITE_FOOD_POUCH = registerFoodPouch("netherite_food_pouch", FoodPouchMaterials.NETHERITE);

    public static <T extends Item> T register(String name, T item) {
        return register(name, item, null);
    }

    public static <T extends Item> T register(String name, T item, @Nullable RegistryKey<ItemGroup> group) {
        T registeredItem = Registry.register(Registries.ITEM, RegistryKey.of(RegistryKeys.ITEM, FoodPouch.id(name)), item);
        if (group != null) {
            ItemGroupEvents.modifyEntriesEvent(group).register((entries) -> entries.add(registeredItem));
        }
        return registeredItem;
    }

    public static FoodPouchItem registerFoodPouch(String name, FoodPouchMaterial material) {
        Item.Settings settings = new Item.Settings()
                .maxCount(1)
                .component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, FoodPouch.id(name)));

        if (material == FoodPouchMaterials.NETHERITE) {
            settings = settings.fireproof();
        }

        return register(name, new FoodPouchItem(material, settings), ItemGroups.TOOLS);
    }

    public static void load() {
        // TODO

//        Map<Item, CauldronBehavior> waterCauldronBehavior = CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map();
//        waterCauldronBehavior.put(FOOD_POUCH, CauldronBehavior::cleanArmor);
//        waterCauldronBehavior.put(IRON_FOOD_POUCH, CauldronBehavior.CLEAN_DYEABLE_ITEM);
//        waterCauldronBehavior.put(GOLD_FOOD_POUCH, CauldronBehavior.CLEAN_DYEABLE_ITEM);
//        waterCauldronBehavior.put(DIAMOND_FOOD_POUCH, CauldronBehavior.CLEAN_DYEABLE_ITEM);
//        waterCauldronBehavior.put(NETHERITE_FOOD_POUCH, CauldronBehavior.CLEAN_DYEABLE_ITEM);
    }
}
