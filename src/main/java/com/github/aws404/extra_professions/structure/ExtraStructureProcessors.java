package com.github.aws404.extra_professions.structure;

import com.github.aws404.extra_professions.mixin.StructureProcessorListsAccessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.structure.processor.StructureProcessorType;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.sId;

public class ExtraStructureProcessors {
    protected static final StructureProcessorType<RandomiseCandlePropertiesProcessor> RANDOMISE_CANDLES_PROCESSOR = StructureProcessorType.register(sId("randomise_candles"), RandomiseCandlePropertiesProcessor.CODEC);

    public static final StructureProcessorList MOSSIFY_10_PERCENT_AND_RANDOMISE_CANDLES = StructureProcessorListsAccessor.callRegister(sId("mossify_10_percent_and_randomise_candles"), ImmutableList.<StructureProcessor>builder().addAll(StructureProcessorLists.MOSSIFY_10_PERCENT.getList()).add(new RandomiseCandlePropertiesProcessor()).build());
    public static final StructureProcessorList RANDOMISE_CANDLES = StructureProcessorListsAccessor.callRegister(sId("randomise_candles"), ImmutableList.of(new RandomiseCandlePropertiesProcessor()));
}
