package com.github.aws404.extra_professions.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ai.brain.task.GiveGiftsToHeroTask;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;

import java.util.Map;

@Mixin(GiveGiftsToHeroTask.class)
public interface GiveGiftsToHeroTaskAccessor {
    @Accessor("GIFTS")
    static Map<VillagerProfession, Identifier> getGIFTS() {
        throw new UnsupportedOperationException();
    }
}
