package com.github.aws404.extra_professions.tasks;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class BreakNearbyLeavesTask extends Task<VillagerEntity> {

    private long nextResponseTime = 0L;

    public BreakNearbyLeavesTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        return world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        if (this.nextResponseTime > time) {
            return;
        }

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos pos = entity.getBlockPos().add(x, y, z);
                    if (world.getBlockState(pos).isIn(BlockTags.LEAVES)) {
                        world.breakBlock(pos, true, entity);
                        this.nextResponseTime = time + 20L;
                        return;
                    }
                }
            }
        }
    }
}
