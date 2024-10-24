package skyveo.foodpouch.item.custom;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.UseRemainderComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import skyveo.foodpouch.FoodPouch;
import skyveo.foodpouch.item.FoodPouchMaterial;
import skyveo.foodpouch.mixin.BundleItemInvoker;
import skyveo.foodpouch.util.FoodPouchContentsComponentBuilder;

import java.util.List;
import java.util.Optional;

public class FoodPouchItem extends BundleItem {
    public static final int BUNDLE_SIZE = 64;

    private final int size;

    public FoodPouchItem(FoodPouchMaterial material, Item.Settings settings) {
        super(
                Identifier.ofVanilla("bundle_open_front"),
                Identifier.ofVanilla("bundle_open_back"),
                settings.maxCount(1).component(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT)
        );
        this.size = material.getSize();
    }

    public int getSize() {
        return size;
    }

    public ItemStack getFirstFood(ItemStack foodPouch) {
        BundleContentsComponent bundleContentsComponent = foodPouch.get(DataComponentTypes.BUNDLE_CONTENTS);
        return bundleContentsComponent == null || bundleContentsComponent.isEmpty() ? ItemStack.EMPTY : bundleContentsComponent.get(0);
    }

    protected void updateFoodPouchContents(ItemStack foodPouch, PlayerEntity player) {
        ((BundleItemInvoker) this).invokeOnContentChanged(player);
        updateFoodPouchContents(foodPouch);
    }

    protected void updateFoodPouchContents(ItemStack foodPouch) {
        ItemStack food = getFirstFood(foodPouch);
        if (food.isEmpty()) {
            foodPouch.remove(DataComponentTypes.CONSUMABLE);
            foodPouch.remove(DataComponentTypes.FOOD);
        } else {
            foodPouch.set(DataComponentTypes.CONSUMABLE, food.get(DataComponentTypes.CONSUMABLE));
            foodPouch.set(DataComponentTypes.FOOD, food.get(DataComponentTypes.FOOD));
        }
    }

    protected boolean insertFood(ItemStack foodPouch, ItemStack food, Slot slot, ClickType clickType, PlayerEntity player, @Nullable StackReference cursorStackReference) {
        if (clickType != ClickType.LEFT || (cursorStackReference != null && !slot.canTakePartial(player))) {
            return false;
        }

        Optional<FoodPouchContentsComponentBuilder> optionalBuilder = FoodPouchContentsComponentBuilder.of(foodPouch);
        FoodPouch.LOGGER.info("Inserting food: " + optionalBuilder);

        optionalBuilder.map(builder -> {
            if (food.isEmpty()) {
                ItemStack removedItem = builder.removeSelected();
                if (removedItem == null) {
                    return false;
                }

                BundleItemInvoker.playRemoveOneSound(player);

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
                BundleItemInvoker.playInsertSound(player);
            }

            foodPouch.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
            updateFoodPouchContents(foodPouch, player);

            return true;
        });
        return false;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
//        return insertFood(stack, slot.getStack(), slot, clickType, player, null);

        Optional<FoodPouchContentsComponentBuilder> optionalBuilder = FoodPouchContentsComponentBuilder.of(stack);

        return optionalBuilder.map(builder -> {
            ItemStack food = slot.getStack();
            if (clickType == ClickType.LEFT && !food.isEmpty()) {
                if (builder.add(food) > 0) {
                    BundleItemInvoker.playInsertSound(player);
                } else {
                    BundleItemInvoker.playInsertFailSound(player);
                }

                stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                updateFoodPouchContents(stack, player);
                return true;
            }
            if (clickType == ClickType.RIGHT && food.isEmpty()) {
                ItemStack removedItem = builder.removeSelected();
                if (removedItem != null) {
                    ItemStack itemStack3 = slot.insertStack(removedItem);
                    if (itemStack3.getCount() > 0) {
                        builder.add(itemStack3);
                    } else {
                        BundleItemInvoker.playRemoveOneSound(player);
                    }
                }

                stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                updateFoodPouchContents(stack, player);
                return true;
            }
            return false;
        }).orElse(false);

    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
//        return insertFood(stack, otherStack, slot, clickType, player, cursorStackReference);

        Optional<FoodPouchContentsComponentBuilder> optionalBuilder = FoodPouchContentsComponentBuilder.of(stack);

        return optionalBuilder.map(builder -> {
            if (clickType == ClickType.LEFT && otherStack.isEmpty()) {
               setSelectedStackIndex(stack, -1);
               return false;
            }
            if (clickType == ClickType.LEFT) {
                if (slot.canTakePartial(player) && builder.add(otherStack) > 0) {
                    BundleItemInvoker.playInsertSound(player);
                } else {
                    BundleItemInvoker.playInsertFailSound(player);
                }

                stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                updateFoodPouchContents(stack, player);
                return true;
            }
            if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
                if (slot.canTakePartial(player)) {
                    ItemStack itemStack = builder.removeSelected();
                    if (itemStack != null) {
                        BundleItemInvoker.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                }

                stack.set(DataComponentTypes.BUNDLE_CONTENTS, builder.build());
                updateFoodPouchContents(stack, player);
                return true;
            }
            setSelectedStackIndex(stack, -1);
            return false;
        }).orElse(false);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {

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
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack foodPouch = user.getStackInHand(hand);
        return getFirstFood(foodPouch).getItem().use(world, user, hand);
    }

    protected void insertStackInInventory(ItemStack stack, LivingEntity player) {
        if (!(player instanceof PlayerEntity playerEntity) || playerEntity.isInCreativeMode()) {
            return;
        }
        if (!playerEntity.getInventory().insertStack(stack)) {
            playerEntity.dropItem(stack, false);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        Optional<FoodPouchContentsComponentBuilder> optionalBuilder = FoodPouchContentsComponentBuilder.of(stack);

        return optionalBuilder.map(builder -> {
            ItemStack food = builder.removeSelected();
            if (food == null) {
                return stack;
            }

            UseRemainderComponent useRemainderComponent = food.get(DataComponentTypes.USE_REMAINDER);
            if (useRemainderComponent != null) {
                insertStackInInventory(useRemainderComponent.convertInto().copy(), user);
            }
            if (builder.add(food.getItem().finishUsing(food, world, user)) == 0) {
                insertStackInInventory(food, user);
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

    public int getItemBarColor(ItemStack stack) {
        return getItemBarStep(stack) == ITEM_BAR_STEPS ? BundleItemInvoker.getFullItemBarColor() : BundleItemInvoker.getItemBarColor();
    }
}
