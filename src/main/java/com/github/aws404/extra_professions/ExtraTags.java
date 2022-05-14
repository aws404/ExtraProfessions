package com.github.aws404.extra_professions;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ExtraTags {
    public static final TagKey<Item> WOODEN_ITEMS = TagKey.of(Registry.ITEM_KEY, ExtraProfessionsMod.id("wooden_items"));
    public static final TagKey<Item> WAX_ITEMS = TagKey.of(Registry.ITEM_KEY, ExtraProfessionsMod.id("wax_items"));
    public static final TagKey<Item> WICK_ITEMS = TagKey.of(Registry.ITEM_KEY, ExtraProfessionsMod.id("wick_items"));
}
