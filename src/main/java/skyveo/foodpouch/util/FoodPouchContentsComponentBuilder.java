package skyveo.foodpouch.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.item.PotionItem;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import skyveo.foodpouch.item.custom.FoodPouchItem;
import skyveo.foodpouch.mixin.BundleContentsComponentBuilderAccessor;
import skyveo.foodpouch.mixin.BundleContentsComponentInvoker;

import java.util.List;

public class FoodPouchContentsComponentBuilder extends BundleContentsComponent.Builder {
    public static final List<Class<? extends Item>> ADDITIONAL_ALLOWED_ITEMS = List.of(PotionItem.class, MilkBucketItem.class);
    public final int maxSize;

    public FoodPouchContentsComponentBuilder(BundleContentsComponent base, int maxSize) {
        super(base);
        this.maxSize = maxSize;
    }

    @Nullable
    public static FoodPouchContentsComponentBuilder of(ItemStack foodPouch) {
        BundleContentsComponent bundleContentsComponent = foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null || !(foodPouch.getItem() instanceof FoodPouchItem foodPouchItem)) {
            return null;
        }
        return new FoodPouchContentsComponentBuilder(bundleContentsComponent, foodPouchItem.maxSize);
    }

    public static boolean canStoreItem(ItemStack stack) {
        Item item = stack.getItem();
        if (!item.canBeNested() || item instanceof FoodPouchItem) {
            return false;
        }
        return stack.contains(DataComponentTypes.FOOD) || ADDITIONAL_ALLOWED_ITEMS.contains(item.getClass());
    }

    public int getMaxAllowed(ItemStack stack) {
        Fraction itemValue = BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(64));
        Fraction usedSpace = this.getOccupancy().multiplyBy(Fraction.getFraction(64));
        Fraction freeSpace = Fraction.getFraction(this.maxSize).subtract(usedSpace);

        return Math.max(freeSpace.divideBy(itemValue).intValue(), 0);
    }

    @Override
    public int add(ItemStack stack) {
        if (!FoodPouchContentsComponentBuilder.canStoreItem(stack)) {
            return 0;
        }

        int toAdd = Math.min(stack.getCount(), this.getMaxAllowed(stack));
        if (toAdd == 0) {
            return 0;
        }

        BundleContentsComponentBuilderAccessor accessor = (BundleContentsComponentBuilderAccessor) this;
        accessor.setOccupancy(this.getOccupancy().add(BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(toAdd, 1))));

        int existingStackIndex = accessor.invokeAddInternal(stack);
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
        ItemStack itemStack = slot.getStack();
        int i = this.getMaxAllowed(itemStack);
        return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
    }
}
