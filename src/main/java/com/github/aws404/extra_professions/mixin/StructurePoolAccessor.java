package com.github.aws404.extra_professions.mixin;

import com.mojang.datafixers.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;

import java.util.List;

@Mixin(StructurePool.class)
public interface StructurePoolAccessor {
    @Accessor
    List<Pair<StructurePoolElement, Integer>> getElementCounts();

    @Mutable
    @Accessor
    void setElementCounts(List<Pair<StructurePoolElement, Integer>> elementCounts);

    @Accessor
    List<StructurePoolElement> getElements();

    @Mutable
    @Accessor
    void setElements(List<StructurePoolElement> elements);
}
