package com.github.aws404.extra_professions.mixin.pathfinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.github.aws404.extra_professions.ExtraTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.BlockView;

import java.util.Optional;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker {
    @Shadow public static PathNodeType getLandNodeType(BlockView world, BlockPos.Mutable pos) { return null; }

    @Redirect(method = "getDefaultNodeType", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/pathing/LandPathNodeMaker;getLandNodeType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos$Mutable;)Lnet/minecraft/entity/ai/pathing/PathNodeType;"))
    private PathNodeType extra_professions_allowFenceGatePathfind(BlockView world, BlockPos.Mutable pos) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof FenceGateBlock) {
            if (blockState.get(FenceGateBlock.OPEN)) {
                return PathNodeType.DOOR_OPEN;
            }

            if (this.entity instanceof VillagerEntity villager) {
                Optional<RegistryEntry<VillagerProfession>> entry = Registry.VILLAGER_PROFESSION.getKey(villager.getVillagerData().getProfession()).flatMap(Registry.VILLAGER_PROFESSION::getEntry);

                if (entry.isPresent() && entry.get().isIn(ExtraTags.USES_GATES_PROFESSIONS)) {
                    return PathNodeType.DOOR_WOOD_CLOSED;
                }
            }
        }

        return getLandNodeType(world, pos);
    }
}
