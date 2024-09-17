package skyveo.foodpouch.util;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.math.Fraction;
import skyveo.foodpouch.mixin.BundleContentsComponentBuilderAccessor;
import skyveo.foodpouch.mixin.BundleContentsComponentInvoker;

public class FoodPouchContentsComponentBuilder extends BundleContentsComponent.Builder {
    public final int maxSize;

    public FoodPouchContentsComponentBuilder(BundleContentsComponent base, int maxSize) {
        super(base);
        this.maxSize = maxSize;
    }

    public int getMaxAllowed(ItemStack stack) {
        Fraction itemValue = BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(64));
        Fraction usedSpace = this.getOccupancy().multiplyBy(Fraction.getFraction(64));
        Fraction freeSpace = Fraction.getFraction(this.maxSize).subtract(usedSpace);

        return Math.max(freeSpace.divideBy(itemValue).intValue(), 0);
    }

    public int add(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canBeNested()) {
            int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
            if (i == 0) {
                return 0;
            } else {
                BundleContentsComponentBuilderAccessor accessor = (BundleContentsComponentBuilderAccessor) this;

                accessor.setOccupancy(this.getOccupancy().add(BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1))));
                int j = accessor.invokeAddInternal(stack);
                if (j != -1) {
                    ItemStack itemStack = accessor.getStacks().remove(j);
                    int newCount = itemStack.getCount() + i;
                    int stackSize = itemStack.getMaxCount();
                    if (newCount > stackSize) {
                        ItemStack fullStack = itemStack.copyWithCount(stackSize);
                        ItemStack remainingStack = itemStack.copyWithCount(newCount - stackSize);
                        accessor.getStacks().add(0, fullStack);
                        accessor.getStacks().add(0, remainingStack);
                    } else {
                        ItemStack combinedStack = itemStack.copyWithCount(newCount);
                        accessor.getStacks().add(0, combinedStack);
                    }
                    stack.decrement(i);
                } else {
                    accessor.getStacks().add(0, stack.split(i));
                }

                return i;
            }
        } else {
            return 0;
        }
    }

    public int add(Slot slot, PlayerEntity player) {
        ItemStack itemStack = slot.getStack();
        int i = this.getMaxAllowed(itemStack);
        return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
    }
}
