package skyveo.foodpouch.mixin;

import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.component.type.BundleContentsComponent;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import skyveo.foodpouch.FoodPouch;
import skyveo.foodpouch.util.CustomBundleContentComponent;

@Mixin(BundleContentsComponent.class)
public abstract class BundleContentsComponentMixin implements CustomBundleContentComponent {
    @Unique
    private Integer maxSize;

    @Override
    public Integer getMaxSize() {
        return maxSize;
    }

    @Override
    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    @Inject(method = "getOccupancy()Lorg/apache/commons/lang3/math/Fraction;", at = @At("HEAD"))
    protected void getOccupancy(CallbackInfoReturnable<Fraction> cir) {
        FoodPouch.LOGGER.info("getOccupancy " + getMaxSize());
    }
}
