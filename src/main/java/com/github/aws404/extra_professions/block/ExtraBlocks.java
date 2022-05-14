package com.github.aws404.extra_professions.block;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.github.aws404.extra_professions.block_entity.AnnealerBlockEntity;
import com.github.aws404.extra_professions.block_entity.SawmillBlockEntity;
import com.github.aws404.extra_professions.mixin.BlocksAccessor;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class ExtraBlocks {
    public static final Block SAWMILL_BLOCK = Registry.register(Registry.BLOCK, ExtraProfessionsMod.id("sawmill"), new SawmillBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)));
    public static final Block DIPPING_STATION_BLOCK = Registry.register(Registry.BLOCK, ExtraProfessionsMod.id("dipping_station"), new DippingStationBlock(FabricBlockSettings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD)));
    public static final Block ANNEALER_BLOCK = Registry.register(Registry.BLOCK, ExtraProfessionsMod.id("annealer"), new AnnealerBlock(FabricBlockSettings.of(Material.STONE).strength(3.5F).requiresTool().luminance(BlocksAccessor.callCreateLightLevelFromLitBlockState(6)).sounds(BlockSoundGroup.STONE)));

    public static final Item SAWMILL_ITEM = Registry.register(Registry.ITEM, ExtraProfessionsMod.id("sawmill"), new BlockItem(ExtraBlocks.SAWMILL_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
    public static final Item DIPPING_STATION_ITEM = Registry.register(Registry.ITEM, ExtraProfessionsMod.id("dipping_station"), new BlockItem(ExtraBlocks.DIPPING_STATION_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
    public static final Item ANNEALER_ITEM = Registry.register(Registry.ITEM, ExtraProfessionsMod.id("annealer"), new BlockItem(ExtraBlocks.ANNEALER_BLOCK, new FabricItemSettings().group(ItemGroup.DECORATIONS)));

    public static final BlockEntityType<SawmillBlockEntity> SAWMILL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, ExtraProfessionsMod.id("sawmill"), FabricBlockEntityTypeBuilder.create(SawmillBlockEntity::new, ExtraBlocks.SAWMILL_BLOCK).build());
    public static final BlockEntityType<AnnealerBlockEntity> ANNEALER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, ExtraProfessionsMod.id("annealer"), FabricBlockEntityTypeBuilder.create(AnnealerBlockEntity::new, ExtraBlocks.ANNEALER_BLOCK).build());
}
