package com.github.aws404.extra_professions.structure;

import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;

import java.util.Optional;

public class RandomiseCandlePropertiesProcessor extends StructureProcessor {
    private static final RandomiseCandlePropertiesProcessor INSTANCE = new RandomiseCandlePropertiesProcessor();
    public static final Codec<RandomiseCandlePropertiesProcessor> CODEC = Codec.unit(INSTANCE);

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData data) {
        if (!structureBlockInfo2.state.isIn(BlockTags.CANDLES)) {
            return structureBlockInfo2;
        }
        Random random = data.getRandom(structureBlockInfo2.pos);
        Optional<BlockState> state = Registry.BLOCK.getEntryList(BlockTags.CANDLES).flatMap(registryEntries -> registryEntries.getRandom(random)).map(blockRegistryEntry ->
                blockRegistryEntry.value()
                    .getDefaultState()
                    .with(CandleBlock.CANDLES, random.nextBetween(1, 4))
                    .with(CandleBlock.LIT, random.nextBoolean()));
        return new Structure.StructureBlockInfo(structureBlockInfo2.pos, state.orElse(Blocks.AIR.getDefaultState()), structureBlockInfo2.nbt);
    }

    @Override
    protected StructureProcessorType<RandomiseCandlePropertiesProcessor> getType() {
        return ExtraStructureProcessorTypes.RANDOMISE_CANDLES_PROCESSOR;
    }
}
