package skyveo.foodpouch.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BundleContentsComponent.class)
public interface BundleContentsComponentInvoker {
    @Invoker("<init>")
    static BundleContentsComponent invokeConstructor(List<ItemStack> stacks, Fraction occupancy, int selectedStackIndex) {
        throw new AssertionError();
    }

    @Invoker("getOccupancy")
    static Fraction getOccupancy(ItemStack stack) {
        throw new AssertionError();
    }
}
