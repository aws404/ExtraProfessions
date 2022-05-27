package com.github.aws404.extra_professions.screen;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.id;

public class ExtraScreenHandlers {
    public static final ScreenHandlerType<SawmillScreenHandler> SAWMILL = Registry.register(Registry.SCREEN_HANDLER, id("sawmill"), new ScreenHandlerType<>(SawmillScreenHandler::new));
    public static final ScreenHandlerType<AnnealerScreenHandler> ANNEALER = Registry.register(Registry.SCREEN_HANDLER, id("lehr"), new ScreenHandlerType<>(AnnealerScreenHandler::new));
    public static final ScreenHandlerType<DippingStationScreenHandler> DIPPING_STATION = Registry.register(Registry.SCREEN_HANDLER, id("dipping_station"), new ScreenHandlerType<>(DippingStationScreenHandler::new));

    public static void init() {
    }
}
