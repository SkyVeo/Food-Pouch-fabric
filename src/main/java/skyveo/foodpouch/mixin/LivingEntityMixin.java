package skyveo.foodpouch.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyveo.foodpouch.item.custom.FoodPouchItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    protected abstract void spawnConsumptionEffects(ItemStack stack, int particleCount);

    @Inject(method = "spawnConsumptionEffects", at = @At("HEAD"), cancellable = true)
    protected void spawnConsumptionEffectsInject(ItemStack stack, int amount, CallbackInfo info) {
        if (stack.getItem() instanceof FoodPouchItem foodPouch) {
            this.spawnConsumptionEffects(foodPouch.getFirstFood(stack), amount);
            info.cancel();
        }
    }
}
