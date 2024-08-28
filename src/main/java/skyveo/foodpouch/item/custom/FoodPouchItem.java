package skyveo.foodpouch.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class FoodPouchItem extends BundleItem {
    public FoodPouchItem(Settings settings) {
        super(settings);
    }

    public static boolean isFood(ItemStack stack) {
        return !(stack.getItem() instanceof FoodPouchItem) && stack.contains(DataComponentTypes.FOOD);
    }

    public ItemStack getFirstFood(ItemStack foodPouch) {
        BundleContentsComponent bundleContentsComponent = foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS);
        return bundleContentsComponent == null || bundleContentsComponent.isEmpty() ? ItemStack.EMPTY : bundleContentsComponent.get(0);
    }

    public boolean isEmpty(ItemStack foodPouch) {
        return this.getFirstFood(foodPouch).isEmpty();
    }

    protected void onContentUpdate(ItemStack foodPouch) {
        if (!this.isEmpty(foodPouch)) {
            foodPouch.set(DataComponentTypes.FOOD, getFirstFood(foodPouch).get(DataComponentTypes.FOOD));
        } else {
            foodPouch.remove(DataComponentTypes.FOOD);
        }
    }

    protected ItemStack consumeFirstFood(ItemStack stack, World world, LivingEntity user) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null) {
            return stack;
        }

        BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContentsComponent);
        ItemStack food = builder.removeFirst();
        if (food == null) {
            return stack;
        }

        ItemStack leftovers = food.getItem().finishUsing(food, world, user);
        if (isFood(leftovers)) {
            builder.add(leftovers);
        } else if (user instanceof PlayerEntity playerEntity && !playerEntity.isInCreativeMode()) {
            if (!playerEntity.getInventory().insertStack(leftovers)) {
                playerEntity.dropItem(leftovers, false);
            }
        }

        stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
        this.onContentUpdate(stack);

        return stack;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        ItemStack itemStack = slot.getStack();
        if (!itemStack.isEmpty() && !isFood(itemStack)) {
            return false;
        }
        boolean result = super.onStackClicked(stack, slot, clickType, player);
        if (result) {
            this.onContentUpdate(stack);
        }
        return result;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (!otherStack.isEmpty() && !isFood(otherStack)) {
            return false;
        }
        boolean result = super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
        if (result) {
            this.onContentUpdate(stack);
        }
        return result;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        ItemStack food = this.getFirstFood(stack);
        return food.getItem().getMaxUseTime(food, user);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        ItemStack food = this.getFirstFood(stack);
        return food.getItem().getUseAction(food);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack foodPouch = user.getStackInHand(hand);
        return this.getFirstFood(foodPouch).getItem().use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return this.consumeFirstFood(stack, world, user);
    }
}
