package com.github.aws404.extra_professions.screen;

import com.github.aws404.extra_professions.ExtraProfessionsMod;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.screen.ScreenHandlerType;

public class ExtraScreenHandlers {
    public static final ScreenHandlerType<SawmillScreenHandler> SAWMILL = ScreenHandlerRegistry.registerSimple(ExtraProfessionsMod.id("sawmill"), SawmillScreenHandler::new);
    public static final ScreenHandlerType<AnnealerScreenHandler> ANNEALER = ScreenHandlerRegistry.registerSimple(ExtraProfessionsMod.id("lehr"), AnnealerScreenHandler::new);
    public static final ScreenHandlerType<DippingStationScreenHandler> DIPPING_STATION = ScreenHandlerRegistry.registerSimple(ExtraProfessionsMod.id("dipping_station"), DippingStationScreenHandler::new);
}
