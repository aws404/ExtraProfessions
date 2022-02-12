package com.github.aws404.extra_professions;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExtraStats {
    public static final Identifier OPEN_SAWMILL = register("open_sawmill", StatFormatter.DEFAULT);

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = ExtraProfessionsMod.id(id);
        Registry.register(Registry.CUSTOM_STAT, identifier, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
