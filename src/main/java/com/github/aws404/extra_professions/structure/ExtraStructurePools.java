package com.github.aws404.extra_professions.structure;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.VillagePlacedFeatures;

import java.util.List;
import java.util.Map;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.sId;
import static com.github.aws404.extra_professions.ExtraProfessionsMod.id;

@SuppressWarnings("unused")
public class ExtraStructurePools {

    private static final StructurePool TREES_SPRUCE = StructurePools.register(new StructurePool(ExtraProfessionsMod.id("trees/spruce"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofFeature(VillagePlacedFeatures.SPRUCE), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("trees/spruce_sapling"), StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));
    private static final StructurePool TREES_OAK = StructurePools.register(new StructurePool(id("trees/oak"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofFeature(VillagePlacedFeatures.OAK), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("trees/oak_sapling"), StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));
    private static final StructurePool TREES_ACACIA = StructurePools.register(new StructurePool(id("trees/acacia"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofFeature(VillagePlacedFeatures.ACACIA), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("trees/acacia_sapling"), StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));
    private static final StructurePool PLAINS_SAND_PILE = StructurePools.register(new StructurePool(id("village/plains/sand_pile"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/sand_pile/plains_sand_pile_1"), StructureProcessorLists.EMPTY), 1),Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/sand_pile/plains_sand_pile_2"), StructureProcessorLists.EMPTY), 1), Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/sand_pile/plains_sand_pile_3"), StructureProcessorLists.EMPTY), 1)), StructurePool.Projection.RIGID));

    public static final Map<Identifier, List<Pair<StructurePoolElement, Integer>>> EXTRA_STRUCTURES = Map.of(
            new Identifier("minecraft:village/plains/houses"), List.of(
                    Pair.of(StructurePoolElement.ofLegacySingle(sId("village/plains/houses/plains_lumber_farm_1")).apply(StructurePool.Projection.RIGID), 3),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/houses/plains_chandler_house_1"), ExtraStructureProcessors.MOSSIFY_10_PERCENT_AND_RANDOMISE_CANDLES).apply(StructurePool.Projection.RIGID), 2),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/houses/plains_glazier_shop_1"), StructureProcessorLists.MOSSIFY_10_PERCENT).apply(StructurePool.Projection.RIGID), 1)
            ),
            new Identifier("minecraft:village/plains/zombie/houses"), List.of(
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/houses/plains_lumber_farm_1"), StructureProcessorLists.ZOMBIE_PLAINS).apply(StructurePool.Projection.RIGID), 3),
				    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/houses/plains_chandler_house_1"), StructureProcessorLists.ZOMBIE_PLAINS).apply(StructurePool.Projection.RIGID), 2),
				    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/plains/houses/plains_glazier_shop_1"), StructureProcessorLists.ZOMBIE_PLAINS).apply(StructurePool.Projection.RIGID), 1)
            ),
            new Identifier("minecraft:village/desert/houses"), List.of(
                    Pair.of(StructurePoolElement.ofLegacySingle(sId("village/desert/houses/desert_lumber_farm_1")).apply(StructurePool.Projection.RIGID), 3),
				    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/desert/houses/desert_chandler_house_1"), ExtraStructureProcessors.RANDOMISE_CANDLES).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/desert/zombie/houses"), List.of(
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/desert/houses/desert_lumber_farm_1"), StructureProcessorLists.ZOMBIE_DESERT).apply(StructurePool.Projection.RIGID), 3),
			    	Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/desert/houses/desert_chandler_house_1"), StructureProcessorLists.ZOMBIE_DESERT).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/savanna/houses"), List.of(
                    Pair.of(StructurePoolElement.ofLegacySingle(sId("village/savanna/houses/savanna_lumber_farm_1")).apply(StructurePool.Projection.RIGID), 3),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/savanna/houses/savanna_chandler_house_1"), ExtraStructureProcessors.RANDOMISE_CANDLES).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/savanna/zombie/houses"), List.of(
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/savanna/houses/savanna_lumber_farm_1"), StructureProcessorLists.ZOMBIE_SAVANNA).apply(StructurePool.Projection.RIGID), 3),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/savanna/houses/savanna_chandler_house_1"), StructureProcessorLists.ZOMBIE_SAVANNA).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/snowy/houses"), List.of(
                    Pair.of(StructurePoolElement.ofLegacySingle(sId("village/snowy/houses/snowy_lumber_farm_1")).apply(StructurePool.Projection.RIGID), 3),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/snowy/houses/snowy_chandler_house_1"), ExtraStructureProcessors.RANDOMISE_CANDLES).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/snowy/zombie/houses"), List.of(
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/snowy/houses/snowy_lumber_farm_1"), StructureProcessorLists.ZOMBIE_SNOWY).apply(StructurePool.Projection.RIGID), 3),
				    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/snowy/houses/snowy_chandler_house_1"), StructureProcessorLists.ZOMBIE_SNOWY).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/taiga/houses"), List.of(
                    Pair.of(StructurePoolElement.ofLegacySingle(sId("village/taiga/houses/taiga_lumber_farm_1")).apply(StructurePool.Projection.RIGID), 3),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/taiga/houses/taiga_chandler_house_1"), ExtraStructureProcessors.MOSSIFY_10_PERCENT_AND_RANDOMISE_CANDLES).apply(StructurePool.Projection.RIGID), 2)
            ),
            new Identifier("minecraft:village/taiga/zombie/houses"), List.of(
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/taiga/houses/taiga_lumber_farm_1"), StructureProcessorLists.ZOMBIE_TAIGA).apply(StructurePool.Projection.RIGID), 3),
                    Pair.of(StructurePoolElement.ofProcessedLegacySingle(sId("village/taiga/houses/taiga_chandler_house_1"), StructureProcessorLists.ZOMBIE_TAIGA).apply(StructurePool.Projection.RIGID), 2)
            )
    );

    public static void registerDevPools() {
        StructurePools.register(new StructurePool(
                id("test_lumber_farm"),
                new Identifier("empty"),
                ImmutableList.of(
                        Pair.of(StructurePoolElement.ofLegacySingle(sId("village/plains/houses/plains_lumber_farm_1")), 1),
                        Pair.of(StructurePoolElement.ofLegacySingle(sId("village/desert/houses/desert_lumber_farm_1")), 1),
                        Pair.of(StructurePoolElement.ofLegacySingle(sId("village/savanna/houses/savanna_lumber_farm_1")), 1),
                        Pair.of(StructurePoolElement.ofLegacySingle(sId("village/snowy/houses/snowy_lumber_farm_1")), 1),
                        Pair.of(StructurePoolElement.ofLegacySingle(sId("village/taiga/houses/taiga_lumber_farm_1")), 1)
                ),
                StructurePool.Projection.RIGID)
        );
    }
}
