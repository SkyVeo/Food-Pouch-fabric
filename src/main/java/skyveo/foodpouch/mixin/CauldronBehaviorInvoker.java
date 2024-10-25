package skyveo.foodpouch.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorInvoker {
    @Invoker("cleanArmor")
    static ActionResult cleanArmor(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack) {
        throw new AssertionError();
    }
}
