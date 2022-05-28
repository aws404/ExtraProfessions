package com.github.aws404.extra_professions.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(SimpleInventory.class)
public interface SimpleInventoryAccessor {
    @Accessor
    DefaultedList<ItemStack> getStacks();
}
