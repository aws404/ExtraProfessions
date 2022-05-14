package com.github.aws404.extra_professions.block;

import com.github.aws404.extra_professions.ExtraProfessionsMod;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;

import net.minecraft.world.poi.PointOfInterestType;

public class ExtraPointOfInterestTypes {
    public static final PointOfInterestType LUMBERJACK = PointOfInterestHelper.register(ExtraProfessionsMod.id("lumberjack"), 1, 1, ExtraBlocks.SAWMILL_BLOCK);
    public static final PointOfInterestType CHANDLER = PointOfInterestHelper.register(ExtraProfessionsMod.id("chandler"), 1, 1, ExtraBlocks.DIPPING_STATION_BLOCK);
    public static final PointOfInterestType GLAZIER = PointOfInterestHelper.register(ExtraProfessionsMod.id("glazier"), 1, 1, ExtraBlocks.ANNEALER_BLOCK);
}
