package skyveo.foodpouch.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import skyveo.foodpouch.item.custom.FoodPouchItem;
import skyveo.foodpouch.mixin.BundleContentsComponentBuilderAccessor;
import skyveo.foodpouch.mixin.BundleContentsComponentInvoker;

import java.util.Optional;

public class FoodPouchContentsComponentBuilder extends BundleContentsComponent.Builder {
    private final int size;

    public FoodPouchContentsComponentBuilder(BundleContentsComponent base, int size) {
        super(base);
        this.size = size;
    }

    public static Optional<FoodPouchContentsComponentBuilder> of(ItemStack foodPouch) {
        BundleContentsComponent bundleContentsComponent = foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null || !(foodPouch.getItem() instanceof FoodPouchItem foodPouchItem)) {
            return Optional.empty();
        }
        return Optional.of(new FoodPouchContentsComponentBuilder(bundleContentsComponent, foodPouchItem.getSize()));
    }

    public int getSize() {
        return size;
    }

    public static boolean canStoreItem(ItemStack stack) {
        Item item = stack.getItem();
        return item.canBeNested() && !(item instanceof FoodPouchItem) && stack.contains(DataComponentTypes.CONSUMABLE);
    }

    public int getMaxAllowed(ItemStack stack) {
        Fraction occupancy = BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(FoodPouchItem.BUNDLE_SIZE));
        Fraction usedSpace = getOccupancy().multiplyBy(Fraction.getFraction(FoodPouchItem.BUNDLE_SIZE));
        Fraction freeSpace = Fraction.getFraction(getSize()).subtract(usedSpace);

        return Math.max(freeSpace.divideBy(occupancy).intValue(), 0);
    }

    protected void addOccupancy(Fraction occupancy) {
        ((BundleContentsComponentBuilderAccessor) this).setOccupancy(getOccupancy().add(occupancy));
    }

    @Override
    public int add(ItemStack stack) {
        if (!canStoreItem(stack)) {
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
}
