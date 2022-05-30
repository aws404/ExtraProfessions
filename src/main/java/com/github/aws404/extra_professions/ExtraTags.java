package com.github.aws404.extra_professions;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;

public class ExtraTags {
    public static final TagKey<Item> WOODEN_ITEMS = TagKey.of(Registry.ITEM_KEY, ExtraProfessionsMod.id("wooden_items"));
    public static final TagKey<Item> WAX_ITEMS = TagKey.of(Registry.ITEM_KEY, ExtraProfessionsMod.id("wax_items"));
    public static final TagKey<Item> WICK_ITEMS = TagKey.of(Registry.ITEM_KEY, ExtraProfessionsMod.id("wick_items"));

    public static final TagKey<Block> MINING_DRILLABLE_BLOCKS = TagKey.of(Registry.BLOCK_KEY, ExtraProfessionsMod.id("mining_drillable"));

    public static final TagKey<EntityType<?>> VILLAGER_BREEDABLE_ENTITIES = TagKey.of(Registry.ENTITY_TYPE_KEY, ExtraProfessionsMod.id("villager_breedable"));

    public static final TagKey<VillagerProfession> USES_GATES_PROFESSIONS = TagKey.of(Registry.VILLAGER_PROFESSION_KEY, ExtraProfessionsMod.id("uses_gates"));
}
