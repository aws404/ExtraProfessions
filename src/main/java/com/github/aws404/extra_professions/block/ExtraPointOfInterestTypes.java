package com.github.aws404.extra_professions.block;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.id;

public class ExtraPointOfInterestTypes {
    public static final RegistryKey<PointOfInterestType> LUMBERJACK = register(id("lumberjack"), ExtraBlocks.SAWMILL_BLOCK);
    public static final RegistryKey<PointOfInterestType> CHANDLER = register(id("chandler"),  ExtraBlocks.DIPPING_STATION_BLOCK);
    public static final RegistryKey<PointOfInterestType> GLAZIER = register(id("glazier"),  ExtraBlocks.ANNEALER_BLOCK);
    public static final RegistryKey<PointOfInterestType> MINER = register(id("miner"),  ExtraBlocks.MINING_DRILL_BLOCK);
    public static final RegistryKey<PointOfInterestType> BREEDER = register(id("breeder"),  ExtraBlocks.FEED_TROUGH_BLOCK);

    private static RegistryKey<PointOfInterestType> register(Identifier id, Block block) {
        PointOfInterestHelper.register(id, 1, 1, block);
        return RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, id);
    }
}
