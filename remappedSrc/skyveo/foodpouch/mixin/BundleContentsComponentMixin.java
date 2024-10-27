package skyveo.foodpouch.mixin;

import net.minecraft.component.type.BundleContentsComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import skyveo.foodpouch.util.CustomBundleContentsComponent;

@Mixin(BundleContentsComponent.class)
public abstract class BundleContentsComponentMixin implements CustomBundleContentsComponent {
    @Unique
    private Integer foodPouchMaxSize;

    @Override
    public Integer getFoodPouchMaxSize() {
        return foodPouchMaxSize;
    }

    @Override
    public void setFoodPouchMaxSize(Integer foodPouchMaxSize) {
        this.foodPouchMaxSize = foodPouchMaxSize;
    }
}
