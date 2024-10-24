package skyveo.foodpouch.item;

import net.minecraft.item.Item;

public enum FoodPouchMaterials implements FoodPouchMaterial {
    LEATHER(Item.DEFAULT_MAX_COUNT),
    IRON(Item.DEFAULT_MAX_COUNT * 2),
    GOLD(Item.DEFAULT_MAX_COUNT * 3),
    DIAMOND(Item.DEFAULT_MAX_COUNT * 4),
    NETHERITE(Item.DEFAULT_MAX_COUNT * 5);

    private final int size;

    FoodPouchMaterials(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
