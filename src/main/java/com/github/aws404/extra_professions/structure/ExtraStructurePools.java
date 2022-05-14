package com.github.aws404.extra_professions.structure;

import com.mojang.datafixers.util.Pair;
import org.apache.commons.lang3.tuple.Triple;

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
    private static final int GLAZIER_SHOP_CHANCE = 2;

    //                 vanilla pool id         structure file,    list id,   chance
    public static final Map<Identifier, List<Triple<Identifier, Identifier, Integer>>> STRUCTURES_POOL_ADDITIONS = Map.of(
            new Identifier("minecraft:village/plains/houses"), List.of(
                    Triple.of(id("village/plains/houses/plains_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/plains/houses/plains_chandler_house_1"), id("randomise_candles_mossify_10_percent"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/plains/houses/plains_glazier_shop_1"), new Identifier("mossify_10_percent"), GLAZIER_SHOP_CHANCE)
            ),
            new Identifier("minecraft:village/plains/zombie/houses"), List.of(
                    Triple.of(id("village/plains/houses/plains_lumber_farm_1"), new Identifier("zombie_plains"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/plains/houses/plains_chandler_house_1"), new Identifier("zombie_plains"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/plains/houses/plains_glazier_shop_1"), new Identifier("zombie_plains"), GLAZIER_SHOP_CHANCE)
            ),

            new Identifier("minecraft:village/desert/houses"), List.of(
                    Triple.of(id("village/desert/houses/plains_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/desert/houses/plains_chandler_house_1"), id("randomise_candles"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/desert/houses/plains_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE)
            ),
            new Identifier("minecraft:village/desert/zombie/houses"), List.of(
                    Triple.of(id("village/desert/houses/plains_lumber_farm_1"), new Identifier("zombie_desert"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/desert/houses/plains_chandler_house_1"), new Identifier("zombie_desert"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/desert/houses/plains_glazier_shop_1"), new Identifier("zombie_desert"), GLAZIER_SHOP_CHANCE)
            ),

            new Identifier("minecraft:village/savanna/houses"), List.of(
                    Triple.of(id("village/savanna/houses/plains_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/savanna/houses/plains_chandler_house_1"), id("randomise_candles"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/savanna/houses/plains_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE)
            ),
            new Identifier("minecraft:village/savanna/zombie/houses"), List.of(
                    Triple.of(id("village/savanna/houses/plains_lumber_farm_1"), new Identifier("zombie_savanna"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/savanna/houses/plains_chandler_house_1"), new Identifier("zombie_savanna"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/savanna/houses/plains_glazier_shop_1"), new Identifier("zombie_savanna"), GLAZIER_SHOP_CHANCE)
            ),

            new Identifier("minecraft:village/snowy/houses"), List.of(
                    Triple.of(id("village/snowy/houses/plains_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/snowy/houses/plains_chandler_house_1"), id("randomise_candles"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/snowy/houses/plains_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE)
            ),
            new Identifier("minecraft:village/snowy/zombie/houses"), List.of(
                    Triple.of(id("village/snowy/houses/plains_lumber_farm_1"), new Identifier("zombie_snowy"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/snowy/houses/plains_chandler_house_1"), new Identifier("zombie_snowy"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/snowy/houses/plains_glazier_shop_1"), new Identifier("zombie_snowy"), GLAZIER_SHOP_CHANCE)
            ),

            new Identifier("minecraft:village/taiga/houses"), List.of(
                    Triple.of(id("village/taiga/houses/plains_lumber_farm_1"), new Identifier("empty"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/taiga/houses/plains_chandler_house_1"), id("randomise_candles_mossify_10_percent"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/taiga/houses/plains_glazier_shop_1"), new Identifier("empty"), GLAZIER_SHOP_CHANCE)
            ),
            new Identifier("minecraft:village/taiga/zombie/houses"), List.of(
                    Triple.of(id("village/taiga/houses/plains_lumber_farm_1"), new Identifier("zombie_taiga"), LUMBER_FARM_CHANCE),
                    Triple.of(id("village/taiga/houses/plains_chandler_house_1"), new Identifier("zombie_taiga"), CHANDLER_HOUSE_CHANCE),
                    Triple.of(id("village/taiga/houses/plains_glazier_shop_1"), new Identifier("zombie_taiga"), GLAZIER_SHOP_CHANCE)
            )

    );

    public static void addAllStructures(MinecraftServer server) {
        STRUCTURES_POOL_ADDITIONS.forEach((identifier, triples) -> triples.forEach(triple -> addToStructurePool(server, identifier, triple.getLeft(), triple.getMiddle(), triple.getRight())));
    }

    private static void addToStructurePool(MinecraftServer server, Identifier poolId, Identifier structureId, Identifier processor, int weight) {
        Optional<StructurePool> poolGetter = server.getRegistryManager() .get(Registry.STRUCTURE_POOL_KEY).getOrEmpty(poolId);

        if (poolGetter.isEmpty()) {
            return;
        }
        StructurePool pool = poolGetter.get();

        Optional<RegistryEntry<StructureProcessorList>> processorList = server.getRegistryManager().get(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getEntry(RegistryKey.of(Registry.STRUCTURE_PROCESSOR_LIST_KEY, processor));

        List<StructurePoolElement> pieceList = ((StructurePoolAccessor) pool).getElements();
        SinglePoolElement piece = processorList.isPresent() ? StructurePoolElement.ofProcessedSingle(structureId.toString(), processorList.orElseThrow()).apply(StructurePool.Projection.RIGID) : StructurePoolElement.ofLegacySingle(structureId.toString()).apply(StructurePool.Projection.RIGID);

        ArrayList<Pair<StructurePoolElement, Integer>> list = new ArrayList<>(((StructurePoolAccessor) pool).getElementCounts());
        list.add(Pair.of(piece, weight));
        ((StructurePoolAccessor) pool).setElementCounts(list);

        for (int i = 0; i < weight; ++i) {
            pieceList.add(piece);
        }
    }
}
