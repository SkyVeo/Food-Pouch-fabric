package skyveo.foodpouch.mixin;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import skyveo.foodpouch.item.custom.FoodPouchItem;

@Mixin(ConsumableComponent.class)
public abstract class ConsumableComponentMixin {
    @Inject(method = "spawnParticlesAndPlaySound", at = @At("HEAD"), cancellable = true)
    protected void spawnFoodPouchFirstFoodParticles(Random random, LivingEntity user, ItemStack stack, int particleCount, CallbackInfo info) {
        if (stack.getItem() instanceof FoodPouchItem foodPouch) {
            ConsumableComponent consumableComponent = ((ConsumableComponent) (Object) this);
            consumableComponent.spawnParticlesAndPlaySound(random, user, foodPouch.getFirstFood(stack), particleCount);
            info.cancel();
        }
    }
}
