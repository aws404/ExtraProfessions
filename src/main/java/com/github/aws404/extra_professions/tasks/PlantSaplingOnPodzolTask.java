package com.github.aws404.extra_professions.tasks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.Nullable;

public class PlantSaplingOnPodzolTask extends Task<VillagerEntity> {

    @Nullable
    private BlockPos plantPos;

    public PlantSaplingOnPodzolTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return false;
        } if (CutDownTreeTask.villagerDoesNotHaveSapling(entity)) {
            return false;
        }
        BlockPos.Mutable mutable = entity.getBlockPos().mutableCopy();

        for(int x = -4; x <= 4; ++x) {
            for(int y = -2; y <= 0; ++y) {
                for(int z = -4; z <= 4; ++z) {
                    mutable.set(entity.getX() + (double)x, entity.getY() + (double)y, entity.getZ() + (double)z);
                    if (world.getBlockState(mutable).getBlock() == Blocks.PODZOL && world.getBlockState(mutable.up()).isAir()) {
                        this.plantPos = mutable.up().toImmutable();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    protected void finishRunning(ServerWorld world, VillagerEntity entity, long time) {
        entity.getBrain().forget(MemoryModuleType.LOOK_TARGET);
        entity.getBrain().forget(MemoryModuleType.WALK_TARGET);
        this.plantPos = null;
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, VillagerEntity entity, long time) {
        return this.plantPos != null;
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        if (this.plantPos != null) {
            entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(this.plantPos));
            entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(this.plantPos, 0.5F, 0));
        }
    }

    @Override
    protected void keepRunning(ServerWorld world, VillagerEntity entity, long time) {
        if (this.plantPos != null && this.plantPos.isWithinDistance(entity.getBlockPos(), 1.0)) {
            BlockItem sapling = CutDownTreeTask.takeSapling(entity);
            if (sapling != null) {
                BlockState state = sapling.getBlock().getDefaultState();
                if (world.canPlace(state, this.plantPos, null)) {
                    world.setBlockState(this.plantPos, state);
                    world.playSound(null, this.plantPos.getX(), this.plantPos.getY(), this.plantPos.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                this.stop(world, entity, time);
            }
        }
    }
}
