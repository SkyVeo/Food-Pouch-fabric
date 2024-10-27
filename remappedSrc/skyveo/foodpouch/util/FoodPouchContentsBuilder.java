package skyveo.foodpouch.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.Fraction;
import skyveo.foodpouch.item.custom.FoodPouchItem;
import skyveo.foodpouch.mixin.BundleContentsComponentBuilderAccessor;
import skyveo.foodpouch.mixin.BundleContentsComponentInvoker;

import java.util.Optional;

public class FoodPouchContentsBuilder extends BundleContentsComponent.Builder {
    private final int maxSize;

    public FoodPouchContentsBuilder(BundleContentsComponent base, int maxSize) {
        super(base);
        this.maxSize = maxSize;
    }

    public static Optional<FoodPouchContentsBuilder> of(ItemStack foodPouch) {
        Optional<BundleContentsComponent> optionalComponent = getComponent(foodPouch);

        if (optionalComponent.isEmpty() || !(foodPouch.getItem() instanceof FoodPouchItem foodPouchItem)) {
            return Optional.empty();
        }
        return Optional.of(new FoodPouchContentsBuilder(optionalComponent.get(), foodPouchItem.getMaxSize()));
    }

    public static Optional<BundleContentsComponent> getComponent(ItemStack foodPouch) {
        return Optional.ofNullable(foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS));
    }

    public static BundleContentsComponent getOrDefaultComponent(ItemStack foodPouch) {
        return foodPouch.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
    }

    public static BundleContentsComponent getTooltipComponent(BundleContentsComponent component, int maxSize) {
        BundleContentsComponent newComponent = BundleContentsComponentInvoker.invokeConstructor(
                component.stream().toList(),
                bundleToFoodPouchOccupancy(component.getOccupancy(), maxSize),
                component.getSelectedStackIndex()
        );
        ((CustomBundleContentsComponent) (Object) newComponent).setFoodPouchMaxSize(maxSize);

        return newComponent;
    }

    public static Fraction bundleToFoodPouchOccupancy(Fraction bundleOccupancy, int maxSize) {
        int itemOccupancy = MathHelper.multiplyFraction(bundleOccupancy, Item.DEFAULT_MAX_COUNT);
        return Fraction.getFraction(itemOccupancy, maxSize);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public static boolean canBeAdded(ItemStack stack) {
        Item item = stack.getItem();
        return item.canBeNested() && !(item instanceof FoodPouchItem) && stack.contains(DataComponentTypes.CONSUMABLE);
    }

    public int getMaxAllowed(ItemStack stack) {
        Fraction freeSpace = Fraction.ONE.subtract(bundleToFoodPouchOccupancy(getOccupancy(), getMaxSize()));
        Fraction occupancy = bundleToFoodPouchOccupancy(BundleContentsComponentInvoker.getOccupancy(stack), getMaxSize());

        return Math.max(freeSpace.divideBy(occupancy).intValue(), 0);
    }

    protected void addOccupancy(Fraction occupancy) {
        ((BundleContentsComponentBuilderAccessor) this).setOccupancy(getOccupancy().add(occupancy));
    }

    @Override
    public int add(ItemStack stack) {
        if (!canBeAdded(stack)) {
            return 0;
        }

        int toAdd = Math.min(stack.getCount(), getMaxAllowed(stack));
        if (toAdd == 0) {
            return 0;
        }

        addOccupancy(BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(toAdd, 1)));

        BundleContentsComponentBuilderAccessor accessor = (BundleContentsComponentBuilderAccessor) this;

        int existingStackIndex = accessor.invokeGetInsertionIndex(stack);
        if (existingStackIndex != -1) {
            ItemStack existingStack = accessor.getStacks().remove(existingStackIndex);
            int newCount = existingStack.getCount() + toAdd;
            int maxStackSize = existingStack.getMaxCount();

            if (newCount > maxStackSize) {
                accessor.getStacks().add(existingStackIndex, existingStack.copyWithCount(maxStackSize));
                accessor.getStacks().add(0, existingStack.copyWithCount(newCount - maxStackSize));
            } else {
                accessor.getStacks().add(0, existingStack.copyWithCount(newCount));
            }
            stack.decrement(toAdd);
        } else {
            accessor.getStacks().add(0, stack.split(toAdd));
        }

        return toAdd;
    }

    @Override
    public int add(Slot slot, PlayerEntity player) {
        return add(slot.getStack());
    }

    public void build(ItemStack foodPouch) {
        foodPouch.set(DataComponentTypes.BUNDLE_CONTENTS, build());
    }
}
