package com.github.aws404.extra_professions.tasks;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import com.github.aws404.extra_professions.block_entity.SawmillBlockEntity;
import com.github.aws404.extra_professions.mixin.SimpleInventoryAccessor;

import net.minecraft.entity.ai.brain.BlockPosLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.GlobalPos;

import java.util.Optional;

public class SwapItemsWithSawmillTask extends Task<VillagerEntity> {

    private static final int RUN_TIME = 1200;
    private static final int KEEP_ITEM_COUNT = 32;

    protected Object2IntMap<Item> takeFromStationCounts = new Object2IntOpenHashMap<>();
    protected Object2IntMap<Item> putInStationCounts = new Object2IntOpenHashMap<>();
    private long lastCheckedTime;

    public SwapItemsWithSawmillTask() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryModuleState.REGISTERED));
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        if (world.getTime() - RUN_TIME < this.lastCheckedTime) {
            return false;
        }
        Optional<GlobalPos> pos = entity.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE);
        if (pos.isEmpty() || pos.get().getDimension() != world.getRegistryKey()) {
            return false;
        }
        this.lastCheckedTime = world.getTime();
        if (world.getBlockEntity(pos.get().getPos()) instanceof SawmillBlockEntity station) {
            Object2IntMap<Item> villagerItemCounts = ((SimpleInventoryAccessor) entity.getInventory()).getStacks().stream().map(stack -> Pair.of(stack.getItem(), stack.getCount())).collect(Object2IntOpenHashMap::new, (map, pair) -> map.addTo(pair.left(), pair.right()), (map, map2) -> map2.forEach(map::addTo));

            villagerItemCounts.forEach((item, integer) -> {
                if (integer > KEEP_ITEM_COUNT) {
                    // We only check if the item is valid, not if it fits. Will be voided if it cannot fit.
                    if (station.isValid(0, new ItemStack(item, 1))) {
                        this.putInStationCounts.put(item, integer - KEEP_ITEM_COUNT);
                    }
                }
            });

            station.getItemCounts().forEach((item, integer) -> {
                if (item.getRegistryEntry().isIn(ItemTags.SAPLINGS) && villagerItemCounts.getInt(item) < KEEP_ITEM_COUNT) {
                    int count = Math.min(KEEP_ITEM_COUNT - villagerItemCounts.getInt(item), integer);
                    ItemStack stack = new ItemStack(item, count);
                    if (entity.getInventory().canInsert(stack)) {
                        this.takeFromStationCounts.put(item, count);
                    }
                }
            });

        }

        return !this.putInStationCounts.isEmpty() || !this.takeFromStationCounts.isEmpty();
    }

    @Override
    protected void run(ServerWorld world, VillagerEntity entity, long time) {
        Optional<GlobalPos> pos = entity.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE);
        if (pos.isPresent()) {
            entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new BlockPosLookTarget(pos.get().getPos()));
            entity.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(pos.get().getPos(), 0.4F, 1));
        }
    }

    @Override
    protected void finishRunning(ServerWorld world, VillagerEntity entity, long time) {
        entity.getBrain().forget(MemoryModuleType.LOOK_TARGET);
        entity.getBrain().forget(MemoryModuleType.WALK_TARGET);
        this.takeFromStationCounts = new Object2IntOpenHashMap<>();
        this.putInStationCounts = new Object2IntOpenHashMap<>();
    }

    @Override
    protected void keepRunning(ServerWorld world, VillagerEntity entity, long time) {
        Optional<GlobalPos> pos = entity.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE);
        if (pos.isEmpty() || pos.get().getDimension() != world.getRegistryKey() || !pos.get().getPos().isWithinDistance(entity.getPos(), 1.5)) {
            return;
        }
        if (world.getBlockEntity(pos.get().getPos()) instanceof SawmillBlockEntity station) {
            this.putInStationCounts.forEach((item, integer) -> {
                int realValue = Inventories.remove(entity.getInventory(), stack -> stack.isOf(item), -1, true);
                ItemStack giveStack = new ItemStack(item, Math.min(integer, realValue));
                // We only check if the item is valid, not if it fits. Will be voided if it cannot fit.
                if (station.isValid(0, giveStack)) {
                    station.addStack(giveStack);
                    Inventories.remove(entity.getInventory(), stack -> stack.isOf(item), giveStack.getCount(), false);
                }
            });
            this.takeFromStationCounts.forEach((item, integer) -> {
                int realValue = Inventories.remove(station, stack -> stack.isOf(item), -1, true);
                ItemStack giveStack = new ItemStack(item, Math.min(integer, realValue));
                if (entity.getInventory().canInsert(giveStack)) {
                    entity.getInventory().addStack(giveStack);
                    Inventories.remove(station, stack -> stack.isOf(item), giveStack.getCount(), false);
                }
            });
            this.stop(world, entity, time);
        }

    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, VillagerEntity entity, long time) {
        Optional<GlobalPos> pos = entity.getBrain().getOptionalMemory(MemoryModuleType.JOB_SITE);
        if (pos.isEmpty() || pos.get().getDimension() != world.getRegistryKey()) {
            return false;
        }
        return !this.putInStationCounts.isEmpty() || !this.takeFromStationCounts.isEmpty();
    }
}
