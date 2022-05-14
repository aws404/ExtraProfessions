package com.github.aws404.extra_professions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;

import java.util.Map;
import java.util.Set;

@Mixin(VillagerEntity.class)
public interface VillagerEntityAccessor {
    @Mutable
    @Accessor
    static void setITEM_FOOD_VALUES(Map<Item, Integer> ITEM_FOOD_VALUES) {
        throw new UnsupportedOperationException();
    }

    @Mutable
    @Accessor
    static void setGATHERABLE_ITEMS(Set<Item> GATHERABLE_ITEMS) {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static Set<Item> getGATHERABLE_ITEMS() {
        throw new UnsupportedOperationException();
    }
}
