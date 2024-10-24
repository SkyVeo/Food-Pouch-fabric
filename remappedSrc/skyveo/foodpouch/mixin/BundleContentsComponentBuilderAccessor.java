package skyveo.foodpouch.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BundleContentsComponent.Builder.class)
public interface BundleContentsComponentBuilderAccessor {
    @Accessor("occupancy")
    void setOccupancy(Fraction occupancy);

    @Accessor
    List<ItemStack> getStacks();

    @Invoker("addInternal")
    int invokeAddInternal(ItemStack stack);
}
