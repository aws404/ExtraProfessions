package com.github.aws404.extra_professions.structure;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import com.github.aws404.extra_professions.mixin.StructurePoolAccessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.id;

public class ExtraStructurePools {
    private static final int LUMBER_FARM_CHANCE = 3;
    private static final int CHANDLER_HOUSE_CHANCE = 2;
    private static final int GLAZIER_SHOP_CHANCE =  1;
    private static final int MINESHAFT_CHANCE = 200;

    public static final Map<Identifier, List<StructureAdditionData>> STRUCTURES_POOL_ADDITIONS = Map.of(
            new Identifier("minecraft:village/plains/houses"), List.of(
                    StructureAdditionData.of(id("village/plains/houses/plains_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/plains/houses/plains_chandler_house_1"), id("randomise_candles_mossify_10_percent"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/plains/houses/plains_glazier_shop_1"), new Identifier("mossify_10_percent"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/plains/houses/plains_mineshaft/top"), id("mineshaft_top"), MINESHAFT_CHANCE)
            ),
            new Identifier("minecraft:village/plains/zombie/houses"), List.of(
                    StructureAdditionData.of(id("village/plains/houses/plains_lumber_farm_1"), new Identifier("zombie_plains"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/plains/houses/plains_chandler_house_1"), new Identifier("zombie_plains"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/plains/houses/plains_glazier_shop_1"), new Identifier("zombie_plains"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/plains/houses/plains_mineshaft/top"), id("mineshaft_top_zombie_plains"), MINESHAFT_CHANCE)
            ),

            new Identifier("minecraft:village/desert/houses"), List.of(
                    StructureAdditionData.of(id("village/desert/houses/desert_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/desert/houses/desert_chandler_house_1"), id("randomise_candles"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/desert/houses/desert_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/desert/houses/desert_mineshaft/top"), id("mineshaft_top"), MINESHAFT_CHANCE)
            ),
            new Identifier("minecraft:village/desert/zombie/houses"), List.of(
                    StructureAdditionData.of(id("village/desert/houses/desert_lumber_farm_1"), new Identifier("zombie_desert"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/desert/houses/desert_chandler_house_1"), new Identifier("zombie_desert"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/desert/houses/desert_glazier_shop_1"), new Identifier("zombie_desert"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/desert/houses/desert_mineshaft/top"), id("mineshaft_top_zombie_desert"), MINESHAFT_CHANCE)
            ),

            new Identifier("minecraft:village/savanna/houses"), List.of(
                    StructureAdditionData.of(id("village/savanna/houses/savanna_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/savanna/houses/savanna_chandler_house_1"), id("randomise_candles"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/savanna/houses/savanna_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/savanna/houses/savanna_mineshaft/top"), id("mineshaft_top"), MINESHAFT_CHANCE)
            ),
            new Identifier("minecraft:village/savanna/zombie/houses"), List.of(
                    StructureAdditionData.of(id("village/savanna/houses/savanna_lumber_farm_1"), new Identifier("zombie_savanna"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/savanna/houses/savanna_chandler_house_1"), new Identifier("zombie_savanna"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/savanna/houses/savanna_glazier_shop_1"), new Identifier("zombie_savanna"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/savanna/houses/savanna_mineshaft/top"), id("mineshaft_top_zombie_savanna"), MINESHAFT_CHANCE)
            ),

            new Identifier("minecraft:village/snowy/houses"), List.of(
                    StructureAdditionData.of(id("village/snowy/houses/snowy_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/snowy/houses/snowy_chandler_house_1"), id("randomise_candles"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/snowy/houses/snowy_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/snowy/houses/snowy_mineshaft/top"), id("mineshaft_top"), MINESHAFT_CHANCE)
            ),
            new Identifier("minecraft:village/snowy/zombie/houses"), List.of(
                    StructureAdditionData.of(id("village/snowy/houses/snowy_lumber_farm_1"), new Identifier("zombie_snowy"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/snowy/houses/snowy_chandler_house_1"), new Identifier("zombie_snowy"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/snowy/houses/snowy_glazier_shop_1"), new Identifier("zombie_snowy"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/snowy/houses/snowy_mineshaft/top"), id("mineshaft_top_zombie_snowy"), MINESHAFT_CHANCE)
            ),

            new Identifier("minecraft:village/taiga/houses"), List.of(
                    StructureAdditionData.of(id("village/taiga/houses/taiga_lumber_farm_1"), new Identifier("mossify_10_percent"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/taiga/houses/taiga_chandler_house_1"), id("randomise_candles_mossify_10_percent"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/taiga/houses/taiga_glazier_shop_1"), new Identifier("mossify_10_percent"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/taiga/houses/taiga_mineshaft/top"), id("mineshaft_top_mossify_10_percent"), MINESHAFT_CHANCE)
            ),
            new Identifier("minecraft:village/taiga/zombie/houses"), List.of(
                    StructureAdditionData.of(id("village/taiga/houses/taiga_lumber_farm_1"), new Identifier("zombie_taiga"), LUMBER_FARM_CHANCE),
                    StructureAdditionData.of(id("village/taiga/houses/taiga_chandler_house_1"), new Identifier("zombie_taiga"), CHANDLER_HOUSE_CHANCE),
                    StructureAdditionData.of(id("village/taiga/houses/taiga_glazier_shop_1"), new Identifier("zombie_taiga"), GLAZIER_SHOP_CHANCE),
                    StructureAdditionData.of(id("village/taiga/houses/taiga_mineshaft/top"), id("mineshaft_top_zombie_taiga"), MINESHAFT_CHANCE)
            )
    );

    public static void addAllStructures(MinecraftServer server) {
        STRUCTURES_POOL_ADDITIONS.forEach((vanillaPool, additions) -> addToStructurePool(server, vanillaPool, additions));
    }

    private static void addToStructurePool(MinecraftServer server, Identifier vanillaPool, List<StructureAdditionData> additions) {
        StructurePool pool = server.getRegistryManager().get(Registry.STRUCTURE_POOL_KEY).getOrEmpty(vanillaPool).orElseThrow();
        List<Pair<StructurePoolElement, Integer>> list = ((StructurePoolAccessor) pool).getElementCounts();
        if (!(list instanceof ArrayList)) {
            ((StructurePoolAccessor) pool).setElementCounts(list = new ArrayList<>(list));
        }

        for (StructureAdditionData addition : additions) {
            Optional<RegistryEntry<StructureProcessorList>> processorList = server.getRegistryManager().get(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getEntry(RegistryKey.of(Registry.STRUCTURE_PROCESSOR_LIST_KEY, addition.processor));
            if (processorList.isEmpty()) {
                continue;
            }

            SinglePoolElement piece = StructurePoolElement.ofProcessedLegacySingle(addition.structureId.toString(), processorList.get()).apply(addition.projection);

            list.add(Pair.of(piece, addition.chance));

            ObjectArrayList<StructurePoolElement> pieceList = ((StructurePoolAccessor) pool).getElements();
            for (int i = 0; i < addition.chance; ++i) {
                pieceList.add(piece);
            }
        }
    }

    private static record StructureAdditionData(Identifier structureId, Identifier processor, int chance, StructurePool.Projection projection) {
        static StructureAdditionData of(Identifier structureId, Identifier processor, int chance) {
            return new StructureAdditionData(structureId, processor, chance, StructurePool.Projection.RIGID);
        }
    }
}
