package skyveo.foodpouch.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import skyveo.foodpouch.mixin.BundleItemInvoker;
import skyveo.foodpouch.util.ICustomBundleContentBuilder;

import java.util.List;

public class FoodPouchItem extends BundleItem {
    public static final int BASE_SIZE = 64;

    public final int maxSize;

    public FoodPouchItem(int tier, Item.Settings settings) {
        super(settings.maxCount(1).component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT));
        this.maxSize = getMaxSizeByTier(tier);
    }

    public FoodPouchItem(int tier) {
        this(tier, new Item.Settings());
    }

    public FoodPouchItem() {
        this(1);
    }

    public static int getMaxSizeByTier(int tier) {
        return tier > 0 ? tier * BASE_SIZE : BASE_SIZE;
    }

    public static boolean isFood(ItemStack stack) {
        return !(stack.getItem() instanceof FoodPouchItem) && stack.contains(DataComponentTypes.FOOD);
    }

    public ItemStack getFirstFood(ItemStack foodPouch) {
        BundleContentsComponent bundleContentsComponent = foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS);
        return bundleContentsComponent == null || bundleContentsComponent.isEmpty() ? ItemStack.EMPTY : bundleContentsComponent.get(0);
    }

    protected void onContentUpdate(ItemStack foodPouch) {
        ItemStack stack = getFirstFood(foodPouch);
        if (stack.isEmpty()) {
            foodPouch.remove(DataComponentTypes.FOOD);
        } else {
            foodPouch.set(DataComponentTypes.FOOD, stack.get(DataComponentTypes.FOOD));
        }
    }

    @Nullable
    protected BundleContentsComponent.Builder getBuilder(ItemStack stack) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent == null) {
            return null;
        }

        BundleContentsComponent.Builder builder = new BundleContentsComponent.Builder(bundleContentsComponent);
        ((ICustomBundleContentBuilder) builder).setMaxSize(this.maxSize);

        return builder;
    }

    protected boolean insertFood(ItemStack foodPouch, ItemStack food, Slot slot, ClickType clickType, PlayerEntity player, @Nullable StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || (!food.isEmpty() && !isFood(food)) || (cursorStackReference != null && !slot.canTakePartial(player))) {
            return false;
        }

        BundleContentsComponent.Builder builder = this.getBuilder(foodPouch);
        if (builder == null) {
            return false;
        }

        BundleItemInvoker invoker = (BundleItemInvoker) this;
        if (food.isEmpty()) {
            ItemStack itemStack = builder.removeFirst();
            if (itemStack != null) {
                invoker.invokePlayRemoveOneSound(player);
                if (cursorStackReference != null) {
                    cursorStackReference.set(itemStack);
                } else {
                    ItemStack leftovers = slot.insertStack(itemStack);
                    builder.add(leftovers);
                }
            }
        } else if (food.getItem().canBeNested()) {
            int i = builder.add(food);
            if (i > 0) {
                invoker.invokePlayInsertSound(player);
            }
        }

        foodPouch.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
        this.onContentUpdate(foodPouch);

        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        return insertFood(stack, slot.getStack(), slot, clickType, player, null);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        return insertFood(stack, otherStack, slot, clickType, player, cursorStackReference);
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

    protected ItemStack consumeFirstFood(ItemStack stack, World world, LivingEntity user) {
        BundleContentsComponent.Builder builder = this.getBuilder(stack);
        if (builder == null) {
            return stack;
        }

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
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        return this.consumeFirstFood(stack, world, user);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            int i = MathHelper.multiplyFraction(bundleContentsComponent.getOccupancy(), 64);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", i, this.maxSize).formatted(Formatting.GRAY));
        }

    }
}
