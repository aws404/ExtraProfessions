package com.github.aws404.extra_professions.screen;

import com.mojang.datafixers.util.Pair;

import com.github.aws404.extra_professions.ExtraTags;
import com.github.aws404.extra_professions.block.ExtraBlocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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

public class DippingStationScreenHandler extends ScreenHandler {
    public static final Pair<Integer, Integer> HONEYCOMB_SLOT_POS = Pair.of(44, 38);
    public static final Pair<Integer, Integer> STRING_SLOT_POS = Pair.of(31, 16);
    public static final Pair<Integer, Integer> DYE_SLOT_POS = Pair.of(57, 16);

    private static final Map<Item, Integer> WAX_ITEMS = Map.of(Items.HONEYCOMB, 2, Items.HONEYCOMB_BLOCK, 8, Items.HONEY_BOTTLE, 6, Items.HONEY_BLOCK, 24);

    private final ScreenHandlerContext context;
    private long lastTakeTime;
    private final Slot honeycombSlot;
    private final Slot stringSlot;
    private final Slot dyeSlot;
    private final Slot outputSlot;
    private final SimpleInventory input = new DippingStationInputInventory(3);
    private final SimpleInventory output = new SimpleInventory(1);

    public DippingStationScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public DippingStationScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ExtraScreenHandlers.DIPPING_STATION, syncId);
        this.context = context;
        this.honeycombSlot = this.addSlot(new Slot(this.input, 0, HONEYCOMB_SLOT_POS.getFirst(), HONEYCOMB_SLOT_POS.getSecond()) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ExtraTags.WAX_ITEMS);
            }
        });
        this.stringSlot = this.addSlot(new Slot(this.input, 1, STRING_SLOT_POS.getFirst(), STRING_SLOT_POS.getSecond()) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isIn(ExtraTags.WICK_ITEMS);
            }
        });
        this.dyeSlot = this.addSlot(new Slot(this.input, 2, DYE_SLOT_POS.getFirst(), DYE_SLOT_POS.getSecond()) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });
        this.outputSlot = this.addSlot(new Slot(this.output, 0, 116, 26) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());
                int outputCount = getOutputCount();

                Item combRecipeRemainder = honeycombSlot.getStack().getItem().getRecipeRemainder();
                Item stringRecipeRemainder = stringSlot.getStack().getItem().getRecipeRemainder();

                ItemStack remainingCombs = honeycombSlot.takeStack(1);
                ItemStack remainingString = stringSlot.takeStack(outputCount);
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
        return !this.honeycombSlot.getStack().isEmpty() ? WAX_ITEMS.getOrDefault(this.honeycombSlot.getStack().getItem(), 2) : 0;
    }

    private int getOutputCount() {
        return Math.min(this.stringSlot.getStack().getCount(), this.getMaxOutput());
    }

    private void populateResult() {
        if (this.stringSlot.getStack().getCount() >= 1 && this.honeycombSlot.hasStack()) {
            DyeColor color = (this.dyeSlot.hasStack() && this.dyeSlot.getStack().getItem() instanceof DyeItem dyeItem) ? dyeItem.getColor() : null;

            if (color == null) {
                this.outputSlot.setStack(new ItemStack(Items.CANDLE, this.getOutputCount()));
            } else {
                this.outputSlot.setStack(new ItemStack(Registry.ITEM.get(new Identifier(color.asString() + "_candle")), this.getOutputCount()));
            }
        } else if (this.outputSlot.hasStack()) {
            this.outputSlot.setStack(ItemStack.EMPTY);
        }

        this.sendContentUpdates();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.populateResult();
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

    public boolean hasHoneycomb() {
        return this.honeycombSlot.hasStack();
    }

    public boolean hasDye() {
        return this.dyeSlot.hasStack();
    }

    public boolean hasWick() {
        return this.stringSlot.hasStack();
    }

    public Slot getHoneycombSlot() {
        return this.honeycombSlot;
    }

    private class DippingStationInputInventory extends SimpleInventory {

        public DippingStationInputInventory(int size) {
            super(size);
        }

        @Override
        public void markDirty() {
            super.markDirty();
            populateResult();
        }
    }
}
