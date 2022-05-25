package com.github.aws404.extra_professions.tasks;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;

import com.github.aws404.extra_professions.util.WorldUtil;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;

import java.util.List;

public class CutDownTreeTask extends Task<VillagerEntity> {

    @Nullable
    private BlockPos currentTreeBase;
    @Nullable
    private BlockPos standingPosition;
    private List<BlockPos> logBlocksToBreak = List.of();
    private long nextResponseTime;

    public CutDownTreeTask() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleState.VALUE_PRESENT), 200, 600);
    }

    protected boolean shouldRun(ServerWorld serverWorld, VillagerEntity villagerEntity) {
        if (!serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return false;
        } else if (villagerDoesNotHaveSapling(villagerEntity)) {
            return false;
        } else {
            BlockPos.Mutable mutable = villagerEntity.getBlockPos().mutableCopy();

            for(int x = -4; x <= 4; ++x) {
                for(int y = -2; y <= 2; ++y) {
                    for(int z = -4; z <= 4; ++z) {
                        mutable.set(villagerEntity.getX() + (double)x, villagerEntity.getY() + (double)y, villagerEntity.getZ() + (double)z);
                        if (isValidTreeBase(mutable, serverWorld)) {
                            this.currentTreeBase = mutable.toImmutable();
                            for (Direction value : Direction.Type.HORIZONTAL) {
                                BlockPos.Mutable pos = new BlockPos.Mutable().set(mutable).move(value);
                                if (serverWorld.getBlockState(pos).isAir()) {
                                    this.standingPosition = pos.toImmutable();
                                    break;
                                }
                            }
                            if (this.standingPosition == null) {
                                this.standingPosition = this.currentTreeBase;
                            }
                            this.logBlocksToBreak = WorldUtil.traceUpwardsBlocks(serverWorld, this.currentTreeBase, state -> state.isIn(BlockTags.LOGS));
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    public static boolean villagerDoesNotHaveSapling(VillagerEntity entity) {
        return Inventories.remove(entity.getInventory(), stack -> stack.isIn(ItemTags.SAPLINGS), -1, true) <= 0;
    }

    private static boolean isValidTreeBase(BlockPos pos, ServerWorld world) {
        BlockState blockState = world.getBlockState(pos);
        BlockState downBlock = world.getBlockState(pos.down());
        BlockState upBlock = world.getBlockState(pos.up());

        return blockState.isIn(BlockTags.LOGS) && blockState.isOf(upBlock.getBlock()) && downBlock.isIn(BlockTags.DIRT);
    }

    protected void run(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
        if (this.currentTreeBase != null && this.standingPosition != null &&  !this.logBlocksToBreak.isEmpty()) {
            villagerEntity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(this.logBlocksToBreak.get(0)));
            villagerEntity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(this.standingPosition, 0.5F, 0));
        }
        villagerEntity.getBrain().remember(MemoryModuleType.INTERACTION_TARGET, villagerEntity); // Stops the HoldTradeOffersTask from changing the held item
        villagerEntity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_AXE));
        villagerEntity.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0F);
    }

    protected void finishRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
        if (this.currentTreeBase != null && serverWorld.getBlockState(this.currentTreeBase).isAir()) {
            BlockItem sapling = takeSapling(villagerEntity);
            if (sapling != null) {
                BlockState state = sapling.getBlock().getDefaultState();
                if (serverWorld.canPlace(state, this.currentTreeBase, null)) {
                    serverWorld.setBlockState(this.currentTreeBase, state);
                    serverWorld.playSound(null, this.currentTreeBase.getX(), this.currentTreeBase.getY(), this.currentTreeBase.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }

        villagerEntity.getBrain().forget(MemoryModuleType.LOOK_TARGET);
        villagerEntity.getBrain().forget(MemoryModuleType.WALK_TARGET);
        this.currentTreeBase = null;
        this.standingPosition = null;
        this.logBlocksToBreak.clear();
        villagerEntity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        villagerEntity.setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.085F);
        villagerEntity.getBrain().forget(MemoryModuleType.INTERACTION_TARGET);
    }

    protected void keepRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
        if (this.currentTreeBase != null && this.standingPosition != null && this.logBlocksToBreak.size() > 0) {
            BlockPos currentTarget = this.logBlocksToBreak.get(0);
            villagerEntity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(currentTarget));
            villagerEntity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(this.standingPosition, 0.5F, 0));

            if (this.currentTreeBase.isWithinDistance(villagerEntity.getPos(), 1.5D) && this.nextResponseTime <= l) {
                if (serverWorld.getBlockState(currentTarget).isIn(BlockTags.LOGS)) {
                    villagerEntity.swingHand(Hand.MAIN_HAND);
                    serverWorld.breakBlock(currentTarget, true, villagerEntity);
                    this.nextResponseTime = l + 20L;
                    this.logBlocksToBreak.remove(0);

                    if (this.logBlocksToBreak.isEmpty()) {
                        this.stop(serverWorld, villagerEntity, l);
                    }
                } else {
                    this.stop(serverWorld, villagerEntity, l);
                }
            }
        }
    }
    
    public static BlockItem takeSapling(VillagerEntity entity) {
        for (int i = 0; i < entity.getInventory().size(); i++) {
            ItemStack stack = entity.getInventory().getStack(i);
            if (stack.isIn(ItemTags.SAPLINGS) && stack.getItem() instanceof BlockItem sapling) {
                stack.decrement(1);
                return sapling;
            }
        }
        return null;
    }

    protected boolean shouldKeepRunning(ServerWorld serverWorld, VillagerEntity villagerEntity, long l) {
        return this.currentTreeBase != null || !this.logBlocksToBreak.isEmpty();
    }
}
