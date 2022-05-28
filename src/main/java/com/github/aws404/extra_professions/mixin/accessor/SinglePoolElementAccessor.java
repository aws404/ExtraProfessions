package com.github.aws404.extra_professions.mixin.accessor;

import com.mojang.datafixers.util.Either;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.Identifier;

@Mixin(SinglePoolElement.class)
public interface SinglePoolElementAccessor {
    @Accessor
    Either<Identifier, Structure> getLocation();
}
