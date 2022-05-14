package com.github.aws404.extra_professions;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExtraStats {
    public static final Identifier OPEN_SAWMILL = register("open_sawmill", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_ANNEALER = register("interact_with_annealer", StatFormatter.DEFAULT);
    public static final Identifier INTERACT_WITH_DIPPING_STATION = register("interact_with_dipping_station", StatFormatter.DEFAULT);

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = ExtraProfessionsMod.id(id);
        Registry.register(Registry.CUSTOM_STAT, identifier, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    public static void init() {
    }
}
