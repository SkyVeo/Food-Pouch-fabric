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
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import skyveo.foodpouch.item.FoodPouchMaterial;
import skyveo.foodpouch.mixin.BundleItemInvoker;
import skyveo.foodpouch.util.FoodPouchContentsComponentBuilder;
import TypedActionResult;
import UseAction;
import java.util.List;
import java.util.Optional;

public class FoodPouchItem extends BundleItem {
    public static final int BUNDLE_SIZE = 64;

    private final int size;

    public FoodPouchItem(FoodPouchMaterial material, Item.Settings settings) {
        super(settings.maxCount(1).component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT));
        this.size = material.getSize();
    }

    public FoodPouchItem(FoodPouchMaterial material) {
        this(material, new Item.Settings());
    }

    public int getSize() {
        return size;
    }

    public ItemStack getFirstFood(ItemStack foodPouch) {
        BundleContentsComponent bundleContentsComponent = foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS);
        return bundleContentsComponent == null || bundleContentsComponent.isEmpty() ? ItemStack.EMPTY : bundleContentsComponent.get(0);
    }

    protected void updateFoodPouchContents(ItemStack foodPouch) {
        ItemStack stack = getFirstFood(foodPouch);
        if (stack.isEmpty()) {
            foodPouch.remove(DataComponentTypes.FOOD);
        } else {
            foodPouch.set(DataComponentTypes.FOOD, stack.get(DataComponentTypes.FOOD));
        }
    }

    protected boolean insertFood(ItemStack foodPouch, ItemStack food, Slot slot, ClickType clickType, PlayerEntity player, @Nullable StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || (cursorStackReference != null && !slot.canTakePartial(player))) {
            return false;
        }

        Optional<FoodPouchContentsComponentBuilder> optionalBuilder = FoodPouchContentsComponentBuilder.of(foodPouch);

        return optionalBuilder.map(builder -> {
            BundleItemInvoker invoker = (BundleItemInvoker) this;

            if (food.isEmpty()) {
                ItemStack removedItem = builder.removeFirst();
                if (removedItem == null) {
                    return false;
                }

                invoker.invokePlayRemoveOneSound(player);

                if (cursorStackReference != null) {
                    cursorStackReference.set(removedItem);
                } else {
                    ItemStack leftovers = slot.insertStack(removedItem);
                    builder.add(leftovers);
                }
            } else {
                if (builder.add(food) == 0) {
                    return false;
                }
                invoker.invokePlayInsertSound(player);
            }

            foodPouch.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
            updateFoodPouchContents(foodPouch);

            return true;
        }).orElse(false);
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
        ItemStack food = getFirstFood(stack);
        return food.getItem().getMaxUseTime(food, user);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        ItemStack food = getFirstFood(stack);
        return food.getItem().getUseAction(food);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack foodPouch = user.getStackInHand(hand);
        return getFirstFood(foodPouch).getItem().use(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Optional<FoodPouchContentsComponentBuilder> optionalBuilder = FoodPouchContentsComponentBuilder.of(stack);

        return optionalBuilder.map(builder -> {
            ItemStack food = builder.removeFirst();
            if (food == null) {
                return stack;
            }

            ItemStack leftovers = food.getItem().finishUsing(food, world, user);
            int i = builder.add(leftovers);
            if (i == 0 && user instanceof PlayerEntity playerEntity && !playerEntity.isInCreativeMode()) {
                if (!playerEntity.getInventory().insertStack(leftovers)) {
                    playerEntity.dropItem(leftovers, false);
                }
            }

            stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
            updateFoodPouchContents(stack);

            return stack;
        }).orElse(stack);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        BundleContentsComponent bundleContentsComponent = stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        int occupancy = MathHelper.multiplyFraction(bundleContentsComponent.getOccupancy(), BUNDLE_SIZE);
        return Math.min(1 + MathHelper.multiplyFraction(Fraction.getFraction(occupancy, this.size), ITEM_BAR_STEPS - 1), ITEM_BAR_STEPS);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        BundleContentsComponent bundleContentsComponent = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            int occupancy = MathHelper.multiplyFraction(bundleContentsComponent.getOccupancy(), BUNDLE_SIZE);
            tooltip.add(Text.translatable("item.minecraft.bundle.fullness", occupancy, this.size).formatted(Formatting.GRAY));
        }

    }
}
