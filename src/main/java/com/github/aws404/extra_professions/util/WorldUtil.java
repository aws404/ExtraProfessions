package com.github.aws404.extra_professions.util;

import com.google.common.collect.Lists;

import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WorldUtil {

    public static List<BlockPos> traceUpwardsBlocks(ServerWorld world, BlockPos baseBlock, Predicate<BlockState> predicate) {
        if (!predicate.test(world.getBlockState(baseBlock))) {
            return List.of();
        }

        List<BlockPos> positions = new ArrayList<>();
        positions.add(baseBlock);
        recurseTraceUpwardsBlocks(world, baseBlock, predicate, positions);
        return positions;
    }

    private static void recurseTraceUpwardsBlocks(ServerWorld world, BlockPos startPos, Predicate<BlockState> predicate, List<BlockPos> positions) {
        for (int x = -1; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = startPos.add(x, y, z);
                    if (!positions.contains(pos) && predicate.test(world.getBlockState(pos))) {
                        positions.add(pos);
                        recurseTraceUpwardsBlocks(world, pos, predicate, positions);
                    }
                }
            }
        }
    }

    public static void generatePool(ServerWorld world, Identifier poolId, Identifier target, BlockPos pos, JigsawOrientation orientation, int maxDepth, boolean keepJigsaws) {
        NbtCompound root = new NbtCompound();
        root.putIntArray("size", new int[]{1, 1, 1});
        root.put("entities", new NbtList());
        root.put("blocks", Util.make(new NbtList(), nbtElements -> nbtElements.add(Util.make(new NbtCompound(), blockInfo -> {
            blockInfo.putInt("state", 0);
            blockInfo.putIntArray("pos", new int[]{0, 0, 0});
            blockInfo.put("nbt", Util.make(new NbtCompound(), blockNbt -> {
                blockNbt.putString("joint", "rollable");
                blockNbt.putString("name", "minecraft:empty");
                blockNbt.putString("pool", poolId.toString());
                blockNbt.putString("final_state", "minecraft:air");
                blockNbt.putString("id", "minecraft:jigsaw");
                blockNbt.putString("target", target.toString());
            }));
        }))));
        root.put("palette", Util.make(new NbtList(), nbtElements -> nbtElements.add(Util.make(new NbtCompound(), entry -> {
            entry.putString("Name", "minecraft:jigsaw");
            entry.put("Properties", Util.make(new NbtCompound(), properties -> properties.putString("orientation", orientation.asString())));
        }))));
        root.putInt(SharedConstants.DATA_VERSION_KEY, SharedConstants.WORLD_VERSION);

        List<PoolStructurePiece> list = Lists.newArrayList();
        StructurePoolElement structurePoolElement = new SinglePoolElement(Util.make(new Structure(), structure1 -> structure1.readNbt(root)));
        PoolStructurePiece poolStructurePiece = new PoolStructurePiece(world.getStructureManager(), structurePoolElement, pos, 1, BlockRotation.NONE, new BlockBox(pos));
        StructurePoolBasedGenerator.generate(world.getRegistryManager(), poolStructurePiece, maxDepth, PoolStructurePiece::new, world.getChunkManager().getChunkGenerator(), world.getStructureManager(), list, world.getRandom(), world);

        for (PoolStructurePiece poolStructurePiece2 : list) {
            poolStructurePiece2.generate(world, world.getStructureAccessor(), world.getChunkManager().getChunkGenerator(), world.getRandom(), BlockBox.infinite(), pos, keepJigsaws);
        }

    }
}
