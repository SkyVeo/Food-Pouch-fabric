package skyveo.foodpouch.mixin;

import net.minecraft.client.gui.tooltip.BundleTooltipSubmenuHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import skyveo.foodpouch.item.custom.FoodPouchItem;

@Mixin(BundleTooltipSubmenuHandler.class)
public abstract class BundleTooltipSubmenuHandlerMixin {
    @Inject(method = "isApplicableTo", at = @At("HEAD"), cancellable = true)
    protected void isApplicableToFoodPouch(Slot slot, CallbackInfoReturnable<Boolean> info) {
        if (slot.getStack().getItem() instanceof FoodPouchItem) {
            info.setReturnValue(true);
        }
    }
}
