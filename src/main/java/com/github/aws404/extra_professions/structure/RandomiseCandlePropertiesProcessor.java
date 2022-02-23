package com.github.aws404.extra_professions.structure;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RandomiseCandlePropertiesProcessor extends StructureProcessor {
    public static final RandomiseCandlePropertiesProcessor INSTANCE = new RandomiseCandlePropertiesProcessor();
    public static final Codec<RandomiseCandlePropertiesProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData data) {
        if (!structureBlockInfo2.state.isIn(BlockTags.CANDLES)) {
            return structureBlockInfo2;
        }
        Random random = data.getRandom(structureBlockInfo2.pos);
        BlockState newState = BlockTags.CANDLES.getRandom(random).getDefaultState()
                .with(CandleBlock.CANDLES, random.nextInt(1, 5))
                .with(CandleBlock.LIT, random.nextBoolean());

        return new Structure.StructureBlockInfo(structureBlockInfo2.pos, newState, structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<RandomiseCandlePropertiesProcessor> getType() {
        return ExtraStructureProcessors.RANDOMISE_CANDLES_PROCESSOR;
    }
}
