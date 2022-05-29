package com.github.aws404.extra_professions;

import com.github.aws404.extra_professions.mixin.accessor.GiveGiftsToHeroTaskAccessor;

import net.minecraft.util.Identifier;

public class ExtraLootTables {
    public static final Identifier HERO_OF_THE_VILLAGE_LUMBERJACK_GIFT_GAMEPLAY = ExtraProfessionsMod.id("gameplay/hero_of_the_village/lumberjack_gift");
    public static final Identifier HERO_OF_THE_VILLAGE_CHANDLER_GIFT_GAMEPLAY = ExtraProfessionsMod.id("gameplay/hero_of_the_village/chandler_gift");
    public static final Identifier HERO_OF_THE_VILLAGE_GLAZIER_GIFT_GAMEPLAY = ExtraProfessionsMod.id("gameplay/hero_of_the_village/glazier_gift");
    public static final Identifier HERO_OF_THE_VILLAGE_MINER_GIFT_GAMEPLAY = ExtraProfessionsMod.id("gameplay/hero_of_the_village/miner_gift");

    public static void registerHeroOfTheVillageGifts() {
        GiveGiftsToHeroTaskAccessor.getGIFTS().put(ExtraProfessionsMod.LUMBERJACK_PROFESSION, HERO_OF_THE_VILLAGE_LUMBERJACK_GIFT_GAMEPLAY);
        GiveGiftsToHeroTaskAccessor.getGIFTS().put(ExtraProfessionsMod.CHANDLER_PROFESSION, HERO_OF_THE_VILLAGE_CHANDLER_GIFT_GAMEPLAY);
        GiveGiftsToHeroTaskAccessor.getGIFTS().put(ExtraProfessionsMod.GLAZIER_PROFESSION, HERO_OF_THE_VILLAGE_GLAZIER_GIFT_GAMEPLAY);
        GiveGiftsToHeroTaskAccessor.getGIFTS().put(ExtraProfessionsMod.MINER_PROFESSION, HERO_OF_THE_VILLAGE_MINER_GIFT_GAMEPLAY);
    }
}
