package com.github.aws404.extra_professions.mixin;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.github.aws404.extra_professions.tasks.BreakNearbyLeavesTask;
import com.github.aws404.extra_professions.tasks.CutDownTreeTask;
import com.github.aws404.extra_professions.tasks.PlantSaplingOnPodzolTask;
import com.github.aws404.extra_professions.tasks.SwapItemsWithSawmillTask;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerTaskListProvider.class)
public abstract class VillagerTaskListProviderMixin {

    @Shadow
    private static Pair<Integer, Task<LivingEntity>> createBusyFollowTask() {
        throw new IllegalStateException();
    }

    @Inject(method = "createWorkTasks", at= @At("HEAD"), cancellable = true)
    private static void injectWorkTasks(VillagerProfession profession, float speed, CallbackInfoReturnable<ImmutableList<Pair<Integer, ? extends Task<? super VillagerEntity>>>> cir) {
        if (profession == ExtraProfessionsMod.LUMBERJACK_PROFESSION) {
            cir.setReturnValue(
                    ImmutableList.of(
                            Pair.of(1, new BreakNearbyLeavesTask()),
                            createBusyFollowTask(),
                            Pair.of(5,
                                    new RandomTask<>(
                                            ImmutableList.of(
                                                    Pair.of(new VillagerWorkTask(), 7),
                                                    Pair.of(new SwapItemsWithSawmillTask(), 7),
                                                    Pair.of(new GoToIfNearbyTask(MemoryModuleType.JOB_SITE, 0.4F, 10), 2),
                                                    Pair.of(new GoToNearbyPositionTask(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5),
                                                    Pair.of(new GoToSecondaryPositionTask(MemoryModuleType.SECONDARY_JOB_SITE, speed, 1, 6, MemoryModuleType.JOB_SITE), 5),
                                                    Pair.of(new CutDownTreeTask(), 2),
                                                    Pair.of(new PlantSaplingOnPodzolTask(), 2)
                                            )
                                    )
                            ),
                            Pair.of(10, new HoldTradeOffersTask(400, 1600)),
                            Pair.of(10, new FindInteractionTargetTask(EntityType.PLAYER, 4)),
                            Pair.of(2, new VillagerWalkTowardsTask(MemoryModuleType.JOB_SITE, speed, 9, 100, 1200)),
                            Pair.of(3, new GiveGiftsToHeroTask(100)),
                            Pair.of(99, new ScheduleActivityTask())
                    )
            );
        }
    }
}
