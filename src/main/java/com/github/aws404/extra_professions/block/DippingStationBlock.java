package com.github.aws404.extra_professions.block;

import org.jetbrains.annotations.Nullable;

import com.github.aws404.extra_professions.ExtraStats;
import com.github.aws404.extra_professions.screen.DippingStationScreenHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DippingStationBlock extends Block {
    private static final Text TITLE = Text.translatable("container.dipping_station");

    public DippingStationBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            player.incrementStat(ExtraStats.INTERACT_WITH_DIPPING_STATION);
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new DippingStationScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

}
