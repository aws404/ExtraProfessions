package com.github.aws404.extra_professions.structure;

import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.registry.Registry;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.id;

public class ExtraStructureProcessorTypes {
    public static final StructureProcessorType<RandomiseCandlePropertiesProcessor> RANDOMISE_CANDLES_PROCESSOR = Registry.register(Registry.STRUCTURE_PROCESSOR, id("randomise_candles"), () -> RandomiseCandlePropertiesProcessor.CODEC);
    public static final StructureProcessorType<PoolIgnoreBoundaryStructureProcessor> POOL_IGNORE_BOUNDARY_PROCESSOR = Registry.register(Registry.STRUCTURE_PROCESSOR, id("pool_ignore_boundary"), () -> PoolIgnoreBoundaryStructureProcessor.CODEC);
    public static final StructureProcessorType<InheretStructureProcessor> INHERIT_STRUCTURE_PROCESSOR = Registry.register(Registry.STRUCTURE_PROCESSOR, id("inherit"), () -> InheretStructureProcessor.CODEC);

    public static void init() {
    }
}
