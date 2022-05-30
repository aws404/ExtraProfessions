package com.github.aws404.extra_professions.tasks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.github.aws404.extra_professions.ExtraTags;
import com.github.aws404.extra_professions.mixin.accessor.VillagerEntityAccessor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BreedAnimalsTask extends Task<VillagerEntity> {
    private static final Predicate<LivingEntity> VALID_ENTITY_PREDICATE = entity -> entity instanceof AnimalEntity && entity.getType().isIn(ExtraTags.VILLAGER_BREEDABLE_ENTITIES);
    private static final Predicate<LivingEntity> BREEDABLE_ENTITY_PREDICATE = entity -> ((AnimalEntity) entity).canEat() && ((AnimalEntity) entity).getBreedingAge() == 0;
    private static final int FOOD_LEVEL_TO_BREED = 5;

    private AnimalEntity animal1;
    private AnimalEntity animal2;

    public BreedAnimalsTask() {
        super(ImmutableMap.of(MemoryModuleType.VISIBLE_MOBS, MemoryModuleState.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryModuleState.VALUE_ABSENT), 60, 200);
    }

    @Override
    protected boolean shouldRun(ServerWorld world, VillagerEntity entity) {
        if (!hasFoodToBreedAnimals(entity)) {
            return false;
        }

        Optional<List<AnimalEntity>> animals = entity.getBrain().getOptionalMemory(MemoryModuleType.VISIBLE_MOBS).flatMap(livingTargetCache ->
                livingTargetCache.stream(VALID_ENTITY_PREDICATE)
                        .collect((Supplier<HashMap<EntityType<?>, List<AnimalEntity>>>) HashMap::new, (map, animal) -> map.merge(animal.getType(), Lists.newArrayList((AnimalEntity) animal), (m1, m2) -> {
                            m1.addAll(m2);
                            return m1;
                        }), (map1, map2) -> map1.forEach((type, list1) -> map2.merge(type, list1, (m1, m2) -> {
                            m1.addAll(m2);
                            return m1;
                        })))
                        .values()
                        .stream()
                        .filter(animalEntities -> animalEntities.size() <= world.getGameRules().getInt(ExtraProfessionsMod.MAX_VILLAGER_ANIMAL_BREEDING_GAME_RULE))
                        .map(animalEntities -> animalEntities.stream().filter(BREEDABLE_ENTITY_PREDICATE).collect(Collectors.toList()))
                        .filter(animalEntities -> animalEntities.size() >= 2)
                        .findFirst());

        if (animals.isPresent()) {
            this.animal1 = animals.get().get(0);
            this.animal2 = animals.get().get(1);
            return true;
        }

        return false;
    }

    @Override
    protected void finishRunning(ServerWorld world, VillagerEntity entity, long time) {
        this.animal1 = null;
        this.animal2 = null;
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, VillagerEntity entity, long time) {
        return this.animal1 != null && this.animal2 != null && this.animal1.isAlive() && this.animal2.isAlive() && (this.animal1.canEat() || this.animal2.canEat());
    }

    @Override
    protected void keepRunning(ServerWorld world, VillagerEntity entity, long time) {
        if (BREEDABLE_ENTITY_PREDICATE.test(this.animal1)) {
            moveToAndBreed(entity, this.animal1);
        } else if (BREEDABLE_ENTITY_PREDICATE.test(this.animal2)) {
            moveToAndBreed(entity, this.animal2);
        } else {
            this.finishRunning(world, entity, time);
        }
    }

    private static void moveToAndBreed(VillagerEntity villager, AnimalEntity animal) {
        if (villager.isInRange(animal, 1.5)) {
            animal.lovePlayer(null);

            if (((VillagerEntityAccessor) villager).getFoodLevel() < FOOD_LEVEL_TO_BREED) {
                ((VillagerEntityAccessor) villager).callConsumeAvailableFood();
            }

            ((VillagerEntityAccessor) villager).callDepleteFood(FOOD_LEVEL_TO_BREED);

            villager.getBrain().forget(MemoryModuleType.LOOK_TARGET);
            villager.getBrain().forget(MemoryModuleType.WALK_TARGET);
        } else if (villager.getBrain().getOptionalMemory(MemoryModuleType.WALK_TARGET).isEmpty()) {
            LookTarget target = new EntityLookTarget(animal, true);
            villager.getBrain().remember(MemoryModuleType.LOOK_TARGET, target);
            villager.getBrain().remember(MemoryModuleType.WALK_TARGET, new WalkTarget(target, (float) villager.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED), 1));
        }
    }

    private static boolean hasFoodToBreedAnimals(VillagerEntity villager) {
        return FOOD_LEVEL_TO_BREED * 2 <= ((VillagerEntityAccessor) villager).getFoodLevel() + ((VillagerEntityAccessor) villager).callGetAvailableFood();
    }
}
