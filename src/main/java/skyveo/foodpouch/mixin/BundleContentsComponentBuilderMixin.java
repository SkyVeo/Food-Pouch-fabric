package skyveo.foodpouch.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import skyveo.foodpouch.util.ICustomBundleContentBuilder;

@Mixin(BundleContentsComponent.Builder.class)
public abstract class BundleContentsComponentBuilderMixin implements ICustomBundleContentBuilder {
    @Unique
    private int maxSize = 64;

    @Override
    public void setMaxSize(int maxSize) {
        if (maxSize >= 64) {
            this.maxSize = maxSize;
        }
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Inject(method = "getMaxAllowed", at = @At("RETURN"), cancellable = true)
    protected void getMaxAllowedInject(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        BundleContentsComponent.Builder builder = (BundleContentsComponent.Builder) (Object) this;

        Fraction itemValue = BundleContentsComponentInvoker.getOccupancy(stack).multiplyBy(Fraction.getFraction(64));
        Fraction usedSpace = builder.getOccupancy().multiplyBy(Fraction.getFraction(64));
        Fraction freeSpace = Fraction.getFraction(this.getMaxSize()).subtract(usedSpace);

        cir.setReturnValue(Math.max(freeSpace.divideBy(itemValue).intValue(), 0));
    }
}
