package com.github.aws404.extra_professions.screen;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class ExtraScreenHandlers {
    public static final ScreenHandlerType<SawmillScreenHandler> SAWMILL_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ExtraProfessionsMod.id("sawmill"), SawmillScreenHandler::new);
    public static final ScreenHandlerType<AnnealerScreenHandler> LEHR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(ExtraProfessionsMod.id("lehr"), AnnealerScreenHandler::new);
}
