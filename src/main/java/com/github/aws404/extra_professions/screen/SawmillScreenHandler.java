package com.github.aws404.extra_professions.screen;

import com.google.common.collect.Lists;

import com.github.aws404.extra_professions.ExtraTags;
import com.github.aws404.extra_professions.block.ExtraBlocks;
import com.github.aws404.extra_professions.recipe.ExtraRecipeTypes;
import com.github.aws404.extra_professions.recipe.SawmillRecipe;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.List;

public class SawmillScreenHandler extends ScreenHandler {

    private final ScreenHandlerContext context;
    private final Property selectedRecipe;
    private final World world;
    private List<SawmillRecipe> availableRecipes;
    private ItemStack inputStack;
    long lastTakeTime;
    final Slot inputSlot;
    final Slot outputSlot;
    Runnable contentsChangedListener;
    public final Inventory input;
    final CraftingResultInventory output;

    public SawmillScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, new SimpleInventory(3), ScreenHandlerContext.EMPTY);
    }

    public SawmillScreenHandler(int syncId, PlayerInventory playerInventory, Inventory entity, ScreenHandlerContext context) {
        super(ExtraScreenHandlers.SAWMILL, syncId);
        this.context = context;
        this.selectedRecipe = Property.create();
        this.inputStack = ItemStack.EMPTY;
        this.availableRecipes = Lists.newArrayList();
        this.contentsChangedListener = () -> {
        };
        this.input = new SimpleInventory(1) {
            public void markDirty() {
                super.markDirty();
                SawmillScreenHandler.this.onContentChanged(this);
                SawmillScreenHandler.this.contentsChangedListener.run();
            }
        };
        this.output = new CraftingResultInventory();
        this.world = playerInventory.player.world;
        this.inputSlot = this.addSlot(new Slot(this.input, 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                stack.onCraft(player.world, player, stack.getCount());
                SawmillScreenHandler.this.output.unlockLastRecipe(player);
                ItemStack itemStack = SawmillScreenHandler.this.inputSlot.takeStack(1);
                if (!itemStack.isEmpty()) {
                    SawmillScreenHandler.this.populateResult();
                }

                context.run((world, pos) -> {
                    long l = world.getTime();
                    if (SawmillScreenHandler.this.lastTakeTime != l) {
                        world.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        SawmillScreenHandler.this.lastTakeTime = l;
                    }

                });
                super.onTakeItem(player, stack);
            }
        });

        for (int i = 0; i < 3; i++) {
            this.addSlot(new Slot(entity, i, 116 + i * 18, 75) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return stack.isIn(ExtraTags.WOODEN_ITEMS);
                }
            });
        }

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 97 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 155));
        }

        this.addProperty(this.selectedRecipe);
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, ExtraBlocks.SAWMILL_BLOCK);
    }

    void populateResult() {
        if (!this.availableRecipes.isEmpty() && this.isInBounds(this.selectedRecipe.get())) {
            SawmillRecipe recipe = this.availableRecipes.get(this.selectedRecipe.get());
            this.output.setLastRecipe(recipe);
            this.outputSlot.setStack(recipe.craft(this.input));
        } else {
            this.outputSlot.setStack(ItemStack.EMPTY);
        }

        this.sendContentUpdates();
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        ItemStack itemStack = this.inputSlot.getStack();
        if (!itemStack.isOf(this.inputStack.getItem())) {
            this.inputStack = itemStack.copy();
            this.updateInput(inventory, itemStack);
        }
    }

    private void updateInput(Inventory input, ItemStack stack) {
        this.availableRecipes.clear();
        this.selectedRecipe.set(-1);
        this.outputSlot.setStack(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.availableRecipes = this.world.getRecipeManager().getAllMatches(ExtraRecipeTypes.SAWMILL_RECIPE_TYPE, input, this.world);
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (this.isInBounds(id)) {
            this.selectedRecipe.set(id);
            this.populateResult();
        }
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            Item item = itemStack2.getItem();
            itemStack = itemStack2.copy();
            if (index == 1) {
                item.onCraft(itemStack2, player.world, player);
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index == 0 ? !this.insertItem(itemStack2, 2, 38, false) : (this.world.getRecipeManager().getFirstMatch(ExtraRecipeTypes.SAWMILL_RECIPE_TYPE, new SimpleInventory(itemStack2), this.world).isPresent() ? !this.insertItem(itemStack2, 0, 1, false) : (index >= 2 && index < 29 ? !this.insertItem(itemStack2, 29, 38, false) : index >= 29 && index < 38 && !this.insertItem(itemStack2, 2, 29, false)))) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            slot.markDirty();
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
            this.sendContentUpdates();
        }
        return itemStack;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }

    public void setContentsChangedListener(Runnable contentsChangedListener) {
        this.contentsChangedListener = contentsChangedListener;
    }

    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    public List<SawmillRecipe> getAvailableRecipes() {
        return this.availableRecipes;
    }

    public int getAvailableRecipeCount() {
        return this.availableRecipes.size();
    }

    public boolean canCraft() {
        return this.inputSlot.hasStack() && !this.availableRecipes.isEmpty();
    }


    private boolean isInBounds(int id) {
        return id >= 0 && id < this.availableRecipes.size();
    }
}
