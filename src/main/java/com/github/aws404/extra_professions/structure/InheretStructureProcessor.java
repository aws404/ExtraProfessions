package com.github.aws404.extra_professions.structure;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;

import java.util.function.Function;

public class InheretStructureProcessor extends StructureProcessor {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final Codec<InheretStructureProcessor> CODEC = StructureProcessorType.REGISTRY_CODEC.fieldOf("parent").xmap(InheretStructureProcessor::new, o -> o.parentList).codec();

    private final RegistryEntry<StructureProcessorList> parentList;

    public InheretStructureProcessor(RegistryEntry<StructureProcessorList> parentList) {
        this.parentList = parentList;
    }

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo originalInfo, Structure.StructureBlockInfo currentInfo, StructurePlacementData data) {
        StructureProcessorList list = this.parentList.getKeyOrValue().map(key -> ((StructureWorldAccess) world).getRegistryManager().get(Registry.STRUCTURE_PROCESSOR_LIST_KEY).get(key), Function.identity());
        if (list == null) {
            LOGGER.error("Tried to inherit from a non-existent processor list {}, ignoring.", this.parentList.getKey().orElseThrow().getValue());
            return currentInfo;
        }
        for (StructureProcessor processor : list.getList()) {
            currentInfo = processor.process(world, pos, pivot, originalInfo, currentInfo, data);
        }
        return currentInfo;
    }

    @Override
    protected StructureProcessorType<InheretStructureProcessor> getType() {
        return ExtraStructureProcessorTypes.INHERIT_STRUCTURE_PROCESSOR;
    }
}
