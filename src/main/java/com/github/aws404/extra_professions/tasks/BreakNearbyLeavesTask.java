package com.github.aws404.extra_professions.tasks;

import com.google.common.collect.ImmutableMap;

import com.github.aws404.extra_professions.util.WorldUtil;

import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
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

        WorldUtil.getRelativePositionsRandomly(entity.getBlockPos(), 1, 1, 1)
                .filter(pos -> world.getBlockState(pos).isIn(BlockTags.LEAVES))
                .findFirst()
                .ifPresent(pos -> {
                    world.breakBlock(pos, true, entity);
                    this.nextResponseTime = time + 20L;
                });
    }
}
