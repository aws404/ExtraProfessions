package com.github.aws404.extra_professions.mixin.pathfinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.github.aws404.extra_professions.ExtraTags;

import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.task.OpenDoorsTask;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.village.VillagerProfession;

import java.util.Iterator;
import java.util.Optional;

@Mixin(OpenDoorsTask.class)
public abstract class OpenDoorsTaskMixin {
    @Shadow protected abstract void rememberToCloseDoor(ServerWorld world, LivingEntity entity, BlockPos pos);
    @Shadow private static boolean hasOtherMobReachedDoor(ServerWorld world, LivingEntity entity, BlockPos pos) { return false; }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/task/OpenDoorsTask;pathToDoor(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/pathing/PathNode;Lnet/minecraft/entity/ai/pathing/PathNode;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void extra_professions_openFenceGates(ServerWorld world, LivingEntity entity, long time, CallbackInfo ci, Path path, PathNode lastNode, PathNode currentNode) {
        if (entity instanceof VillagerEntity villager) {
            Optional<RegistryEntry<VillagerProfession>> entry = Registry.VILLAGER_PROFESSION.getKey(villager.getVillagerData().getProfession()).flatMap(Registry.VILLAGER_PROFESSION::getEntry);
            boolean professionCanOpenGates = entry.isPresent() && entry.get().isIn(ExtraTags.USES_GATES_PROFESSIONS);

            BlockPos lastPos = lastNode.getBlockPos();
            BlockState lastState = world.getBlockState(lastPos);

            if (lastState.isIn(BlockTags.FENCE_GATES, state -> state.getBlock() instanceof FenceGateBlock)) {
                if (!lastState.get(FenceGateBlock.OPEN) && professionCanOpenGates) {
                    world.setBlockState(lastPos, lastState.with(FenceGateBlock.OPEN, true), 10);
                }

                this.rememberToCloseDoor(world, entity, lastPos);
            }

            BlockPos currentPos = currentNode.getBlockPos();
            BlockState currentState = world.getBlockState(currentPos);

            if (currentState.isIn(BlockTags.FENCE_GATES, state -> state.getBlock() instanceof FenceGateBlock)) {
                if (!currentState.get(FenceGateBlock.OPEN) && professionCanOpenGates) {
                    world.setBlockState(currentPos, currentState.with(FenceGateBlock.OPEN, true), 10);
                    this.rememberToCloseDoor(world, entity, currentPos);
                }
            }
        }
    }

    @Inject(method = "pathToDoor", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void extra_professions_closeFenceGates(ServerWorld world, LivingEntity entity, PathNode lastNode, PathNode currentNode, CallbackInfo ci, Brain<?> brain, Iterator<GlobalPos> iterator, GlobalPos globalPos, BlockPos blockPos, BlockState state) {
        if (state.isIn(BlockTags.FENCE_GATES, checkState -> checkState.getBlock() instanceof FenceGateBlock)) {
            if (state.get(FenceGateBlock.OPEN) && !hasOtherMobReachedDoor(world, entity, blockPos)) {
                world.setBlockState(blockPos, state.with(FenceGateBlock.OPEN, false), 10);
            }
        }
    }
}
