package com.github.aws404.extra_professions.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureProcessorLists.class)
public interface StructureProcessorListsAccessor {
    @Invoker
    static StructureProcessorList callRegister(String id, ImmutableList<StructureProcessor> processorList) {
        throw new UnsupportedOperationException();
    }
}
