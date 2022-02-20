package com.github.aws404.extra_professions.block_entity;

import com.github.aws404.extra_professions.ExtraTags;
import com.github.aws404.extra_professions.block.ExtraBlocks;
import com.github.aws404.extra_professions.screen.SawmillScreenHandler;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class SawmillBlockEntity extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);

    public SawmillBlockEntity(BlockPos pos, BlockState state) {
        super(ExtraBlocks.SAWMILL_BLOCK_ENTITY, pos, state);
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.sawmill");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new SawmillScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(this.world, this.pos));
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return stack.isIn(ExtraTags.WOODEN_ITEMS_TAG);
    }

    public void addStack(ItemStack stack) {
        ItemStack itemStack = stack.copy();
        this.addToExistingSlot(itemStack);
        if (!itemStack.isEmpty()) {
            this.addToNewSlot(itemStack);
        }
    }

    private void addToExistingSlot(ItemStack stack) {
        for(int i = 0; i < this.size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (ItemStack.canCombine(itemStack, stack)) {
                this.transfer(stack, itemStack);
                if (stack.isEmpty()) {
                    return;
                }
            }
        }
    }

    private void transfer(ItemStack source, ItemStack target) {
        int i = Math.min(this.getMaxCountPerStack(), target.getMaxCount());
        int j = Math.min(source.getCount(), i - target.getCount());
        if (j > 0) {
            target.increment(j);
            source.decrement(j);
            this.markDirty();
        }
    }

    private void addToNewSlot(ItemStack stack) {
        for(int i = 0; i < this.size(); ++i) {
            ItemStack itemStack = this.getStack(i);
            if (itemStack.isEmpty()) {
                this.setStack(i, stack.copy());
                stack.setCount(0);
                return;
            }
        }
    }

    public Object2IntMap<Item> getItemCounts() {
        return this.inventory.stream().map(stack -> Pair.of(stack.getItem(), stack.getCount())).collect(Object2IntOpenHashMap::new, (map, pair) -> map.addTo(pair.left(), pair.right()), (map, map2) -> map2.forEach(map::addTo));
    }

}
