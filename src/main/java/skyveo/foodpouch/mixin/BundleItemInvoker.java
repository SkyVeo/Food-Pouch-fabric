package skyveo.foodpouch.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemInvoker {
    @Accessor("FULL_ITEM_BAR_COLOR")
    static int getFullItemBarColor() {
        throw new AssertionError();
    }

    @Accessor("ITEM_BAR_COLOR")
    static int getItemBarColor() {
        throw new AssertionError();
    }

    @Invoker("onContentChanged")
    void invokeOnContentChanged(PlayerEntity user);

    @Invoker("playInsertSound")
    static void playInsertSound(Entity entity) {
        throw new AssertionError();
    }

    @Invoker("playRemoveOneSound")
    static void playRemoveOneSound(Entity entity) {
        throw new AssertionError();
    }

    @Invoker("playInsertFailSound")
    static void playInsertFailSound(Entity entity) {
        throw new AssertionError();
    }
}
