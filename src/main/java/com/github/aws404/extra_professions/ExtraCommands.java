package com.github.aws404.extra_professions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Schedule;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.village.VillagerData;

public class ExtraCommands {
    private static final SimpleCommandExceptionType META_NOT_FOUND_EXCEPTION = new SimpleCommandExceptionType(new LiteralText("Could not find the mod's metadata, is it loaded?"));

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal("extra_professions")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("help")
                        .executes(context -> {
                            ModContainer container = FabricLoader.getInstance().getModContainer(ExtraProfessionsMod.MOD_ID).orElseThrow(META_NOT_FOUND_EXCEPTION::create);

                            context.getSource().sendFeedback(new LiteralText(container.getMetadata().getName() + " (ver: " + container.getMetadata().getVersion().getFriendlyString() + ")"), false);
                            context.getSource().sendFeedback(new LiteralText("For help, visit the mod's github page: " + container.getMetadata().getContact().get("sources").orElseThrow(META_NOT_FOUND_EXCEPTION::create)), false);

                            return 1;
                        })
                )
                .then(CommandManager.literal("schedule")
                        .executes(context -> {
                            long time = context.getSource().getWorld().getTimeOfDay();
                            Activity activity = Schedule.VILLAGER_DEFAULT.getActivityForTime((int)(time % 24000L));
                            context.getSource().sendFeedback(new LiteralText("Current villager activity: " + activity.getId()), false);
                            return 1;
                        })
                )
        );
    }
}
