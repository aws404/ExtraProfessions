package com.github.aws404.extra_professions.screen;

import com.mojang.datafixers.util.Pair;

import com.github.aws404.extra_professions.ExtraTags;
import com.github.aws404.extra_professions.block.ExtraBlocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.function.Predicate;

public class DippingStationScreenHandler extends ScreenHandler {
    public static final Pair<Integer, Integer> WAX_SLOT_POS = Pair.of(44, 38);
    public static final Pair<Integer, Integer> WICK_SLOT_POS = Pair.of(31, 16);
    public static final Pair<Integer, Integer> DYE_SLOT_POS = Pair.of(57, 16);
    public static final int WAX_SLOT = 0;
    public static final int WICK_SLOT = 1;
    public static final int DYE_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;

    private static final Map<Item, Integer> WAX_ITEMS = Map.of(Items.HONEYCOMB, 2, Items.HONEYCOMB_BLOCK, 8, Items.HONEY_BOTTLE, 6, Items.HONEY_BLOCK, 24);

    private final ScreenHandlerContext context;
    private long lastTakeTime;
    private final SimpleInventory input = new SimpleInventory(3);
    private final CraftingResultInventory output = new CraftingResultInventory();

    public DippingStationScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public DippingStationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ExtraScreenHandlers.DIPPING_STATION, syncId);
        this.context = context;

        this.addSlot(new InputSlot(WAX_SLOT, WAX_SLOT_POS, stack -> stack.isIn(ExtraTags.WAX_ITEMS)));
        this.addSlot(new InputSlot(WICK_SLOT, WICK_SLOT_POS, stack -> stack.isIn(ExtraTags.WICK_ITEMS)));
        this.addSlot(new InputSlot(DYE_SLOT, DYE_SLOT_POS, stack -> stack.getItem() instanceof DyeItem));
        this.addSlot(new Slot(this.output, 0, 116, 26) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());
                int outputCount = getOutputCount();

                Slot waxSlot = getSlot(WAX_SLOT);
                Slot wickSlot = getSlot(WICK_SLOT);
                Slot dyeSlot = getSlot(DYE_SLOT);

                Item combRecipeRemainder = waxSlot.getStack().getItem().getRecipeRemainder();
                Item stringRecipeRemainder = wickSlot.getStack().getItem().getRecipeRemainder();

                ItemStack remainingCombs = waxSlot.takeStack(1);
                ItemStack remainingString = wickSlot.takeStack(outputCount);
                dyeSlot.takeStack(1);

                if (combRecipeRemainder != null) {
                    player.giveItemStack(new ItemStack(combRecipeRemainder));
                }
                if (stringRecipeRemainder != null) {
                    player.giveItemStack(new ItemStack(stringRecipeRemainder, outputCount));
                }

                if (!remainingCombs.isEmpty() && !remainingString.isEmpty()) {
                    DippingStationScreenHandler.this.populateResult();
                }

                context.run((world, pos) -> {
                    long curTime = world.getTime();
                    if (lastTakeTime != curTime) {
                        world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        lastTakeTime = curTime;
                    }

                });
                super.onTakeItem(player, stack);
            }
        });

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 69 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 127));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ExtraBlocks.DIPPING_STATION_BLOCK);
    }

    public int getMaxOutput() {
        return !this.getSlot(WAX_SLOT).getStack().isEmpty() ? WAX_ITEMS.getOrDefault(this.getSlot(WAX_SLOT).getStack().getItem(), 2) : 0;
    }

    private int getOutputCount() {
        return Math.min(this.getSlot(WICK_SLOT).getStack().getCount(), this.getMaxOutput());
    }

    private void populateResult() {
        Slot outputSlot = this.getSlot(OUTPUT_SLOT);

        if (this.getSlot(WICK_SLOT).getStack().getCount() >= 1 && this.getSlot(WAX_SLOT).hasStack()) {
            DyeColor color = (this.getSlot(DYE_SLOT).hasStack() && this.getSlot(DYE_SLOT).getStack().getItem() instanceof DyeItem dyeItem) ? dyeItem.getColor() : null;

            if (color == null) {
                outputSlot.setStack(new ItemStack(Items.CANDLE, this.getOutputCount()));
            } else {
                outputSlot.setStack(new ItemStack(Registry.ITEM.get(new Identifier(color.asString() + "_candle")), this.getOutputCount()));
            }
        } else if (outputSlot.hasStack()) {
            outputSlot.setStack(ItemStack.EMPTY);
        }

        this.sendContentUpdates();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        DippingStationScreenHandler.this.populateResult();
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && slot.canInsert(stack);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot fromSlot = this.slots.get(index);
        if (fromSlot.hasStack()) {
            ItemStack currentStack = fromSlot.getStack();
            itemStack = currentStack.copy();
            if (index <= 4) {
                if (!this.insertItem(currentStack, 5, 40, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(currentStack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (currentStack.isEmpty()) {
                fromSlot.setStack(ItemStack.EMPTY);
            }
            fromSlot.markDirty();
            if (currentStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            fromSlot.onTakeItem(player, currentStack);
            this.sendContentUpdates();
        }
        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.output.clear();
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    private class InputSlot extends Slot {
        private final Predicate<ItemStack> insertPredicate;

        public InputSlot(int index, Pair<Integer, Integer> pos, Predicate<ItemStack> insertPredicate) {
            super(DippingStationScreenHandler.this.input, index, pos.getFirst(), pos.getSecond());
            this.insertPredicate = insertPredicate;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return super.canInsert(stack) && this.insertPredicate.test(stack);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            DippingStationScreenHandler.this.onContentChanged(DippingStationScreenHandler.this.input);
        }
    }
}
