package com.github.aws404.extra_professions.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.ToIntFunction;

@Mixin(Blocks.class)
public interface BlocksAccessor {
    @Invoker
    static ToIntFunction<BlockState> callCreateLightLevelFromLitBlockState(int litLevel) {
        throw new UnsupportedOperationException();
    }
}
