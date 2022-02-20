package com.github.aws404.extra_professions;

import com.github.aws404.extra_professions.util.WorldUtil;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.enums.JigsawOrientation;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.structure.Structure;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.text.LiteralText;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class ExtraCommands {

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

    }

    public static void registerDevCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("jigsaw")
                .then(CommandManager.literal("generate")
                        .then(CommandManager.argument("pool", IdentifierArgumentType.identifier())
                                .suggests((context, builder) -> CommandSource.suggestIdentifiers(((CommandSource)context.getSource()).getRegistryManager().get(Registry.STRUCTURE_POOL_KEY).getIds(), builder))
                                .then(CommandManager.argument("target", IdentifierArgumentType.identifier())
                                        .suggests((context, builder) -> CommandSource.suggestIdentifiers(List.of(new Identifier("minecraft:building_entrance"), new Identifier("minecraft:street"), new Identifier("minecraft:plate_entry")), builder))
                                        .executes(context -> {
                                            Identifier pool = IdentifierArgumentType.getIdentifier(context, "pool");
                                            Identifier target = IdentifierArgumentType.getIdentifier(context, "target");
                                            JigsawOrientation orientation = JigsawOrientation.byDirections(context.getSource().getEntity().getHorizontalFacing(), Direction.UP);
                                            WorldUtil.generatePool(context.getSource().getWorld(), pool, target, new BlockPos(context.getSource().getPosition()), orientation, 32, false);
                                            return 1;
                                        }))
                        )
                )
                .then(CommandManager.literal("list")
                        .then(CommandManager.argument("pool", IdentifierArgumentType.identifier())
                                .suggests((context, builder) -> CommandSource.suggestIdentifiers(((CommandSource)context.getSource()).getRegistryManager().get(Registry.STRUCTURE_POOL_KEY).getIds(), builder))
                                .executes(context -> {
                                    Identifier poolId = IdentifierArgumentType.getIdentifier(context, "pool");
                                    StructurePool pool = context.getSource().getServer().getRegistryManager().get(Registry.STRUCTURE_POOL_KEY).get(poolId);
                                    StructurePoolElement element = pool.getRandomElement(context.getSource().getWorld().random);
                                    List<Structure.StructureBlockInfo> jigsaws = element.getStructureBlockInfos(context.getSource().getServer().getStructureManager(), new BlockPos(context.getSource().getPosition()), BlockRotation.NONE, context.getSource().getWorld().random);
                                    if (jigsaws.isEmpty()) {
                                        context.getSource().sendFeedback(new LiteralText("No jigsaws found in target pool selected."), false);
                                    } else {
                                        context.getSource().sendFeedback(new LiteralText(String.format("Found the following jigsaw targets on pool '%s':", poolId.toString())), false);
                                        jigsaws.forEach(structureBlockInfo -> context.getSource().sendFeedback(new LiteralText(String.format("- %s (%s)", structureBlockInfo.nbt.getString("target"), structureBlockInfo.nbt.getString("name"))), false));
                                    }
                                    return jigsaws.size();
                                })
                        )
                )
        );

    }
}
