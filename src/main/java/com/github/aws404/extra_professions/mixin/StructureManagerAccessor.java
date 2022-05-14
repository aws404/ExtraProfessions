package com.github.aws404.extra_professions.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

@Mixin(StructureManager.class)
public interface StructureManagerAccessor {
    @Accessor
    Map<Identifier, Optional<Structure>> getStructures();
}
