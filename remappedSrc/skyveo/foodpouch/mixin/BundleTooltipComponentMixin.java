package skyveo.foodpouch.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyveo.foodpouch.util.CustomBundleContentsComponent;

import java.util.Objects;

@Mixin(BundleTooltipComponent.class)
public abstract class BundleTooltipComponentMixin {
    @Shadow
    protected BundleContentsComponent bundleContents;

    @Shadow
    protected abstract int getXMargin(int width);

    @Shadow
    protected abstract void drawProgressBar(int x, int y, TextRenderer textRenderer, DrawContext context);

    @Inject(method = "drawEmptyTooltip", at = @At("HEAD"), cancellable = true)
    protected void drawEmptyFoodPouchTooltip(TextRenderer textRenderer, int x, int y, int width, int height, DrawContext context, CallbackInfo info) {
        Integer maxSize = ((CustomBundleContentsComponent) (Object) bundleContents).getFoodPouchMaxSize();

        if (maxSize != null) {
            Text text = Text.translatable("item.food_pouch.empty.description", maxSize);

            context.drawTextWrapped(textRenderer, text, x + getXMargin(width), y, 96, 11184810);

            int descriptionHeight = textRenderer.wrapLines(text, 96).size();
            Objects.requireNonNull(textRenderer);
            descriptionHeight *= 9;

            drawProgressBar(x + getXMargin(width), y + descriptionHeight + 4, textRenderer, context);
            info.cancel();
        }
    }
}
