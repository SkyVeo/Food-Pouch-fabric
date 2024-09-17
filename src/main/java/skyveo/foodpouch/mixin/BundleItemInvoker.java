package skyveo.foodpouch.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemInvoker {
    @Invoker("playInsertSound")
    void invokePlayInsertSound(Entity entity);

    @Invoker("playRemoveOneSound")
    void invokePlayRemoveOneSound(Entity entity);
}
