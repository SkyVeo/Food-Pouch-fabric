package skyveo.foodpouch.util;

import org.spongepowered.asm.mixin.Mixin;

public interface ICustomBundleContentBuilder {
    void setMaxSize(int value);

    int getMaxSize();
}
