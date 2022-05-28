package com.github.aws404.extra_professions.util;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WorldUtil {

    public static Stream<BlockPos> getRelativePositionsRandomly(BlockPos pos, int radX, int radY, int radZ) {
        List<BlockPos> positions = IntStream.range(-radX, radX).boxed().flatMap(x -> IntStream.range(-radY, radY).boxed().flatMap(y -> IntStream.range(-radZ, radZ).boxed().map(z -> new BlockPos(x, y, z)))).collect(Collectors.toList());
        Collections.shuffle(positions);
        return positions.stream().map(pos::add);
    }

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
}
