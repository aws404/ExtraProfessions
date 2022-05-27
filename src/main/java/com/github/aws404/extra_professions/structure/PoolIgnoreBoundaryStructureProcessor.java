package com.github.aws404.extra_professions.structure;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Blocks;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;

import java.time.Duration;

public class PoolIgnoreBoundaryStructureProcessor extends StructureProcessor {
    public static final Codec<PoolIgnoreBoundaryStructureProcessor> CODEC = Codec.unit(new PoolIgnoreBoundaryStructureProcessor());

    private final Cache<BlockPos, Boolean> generated = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMinutes(1)).build();

    @Nullable
    @Override
    public Structure.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, Structure.StructureBlockInfo originalInfo, Structure.StructureBlockInfo currentInfo, StructurePlacementData data) {
        if (!currentInfo.state.isOf(Blocks.STRUCTURE_BLOCK) || currentInfo.state.get(StructureBlock.MODE) != StructureBlockMode.DATA) {
            return currentInfo;
        }

        String[] metaData = currentInfo.nbt.getString("metadata").split("\\|");
        RegistryKey<StructurePool> poolKey = RegistryKey.of(Registry.STRUCTURE_POOL_KEY, new Identifier(metaData[0]));
        Identifier name = new Identifier(metaData[1]);

        RegistryEntry<StructurePool> pool = ((StructureWorldAccess) world).getRegistryManager().get(Registry.STRUCTURE_POOL_KEY).getEntry(poolKey).orElseThrow();

        if (this.generated.getIfPresent(currentInfo.pos) == null) {
            ServerWorld serverWorld = ((StructureWorldAccess) world).toServerWorld();
            serverWorld.getServer().execute(() -> StructurePoolBasedGenerator.generate(serverWorld, pool, name, 5, currentInfo.pos, false));

            this.generated.put(currentInfo.pos, true);
        }

        return new Structure.StructureBlockInfo(currentInfo.pos, Blocks.CAVE_AIR.getDefaultState(), null);
    }

    @Override
    protected StructureProcessorType<PoolIgnoreBoundaryStructureProcessor> getType() {
        return ExtraStructureProcessorTypes.POOL_IGNORE_BOUNDARY_PROCESSOR;
    }
}
