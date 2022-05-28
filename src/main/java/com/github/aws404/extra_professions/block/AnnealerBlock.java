package com.github.aws404.extra_professions.block;

import org.jetbrains.annotations.Nullable;

import com.github.aws404.extra_professions.ExtraStats;
import com.github.aws404.extra_professions.block_entity.AnnealerBlockEntity;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AnnealerBlock extends AbstractFurnaceBlock {

    public AnnealerBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, ExtraBlocks.ANNEALER_BLOCK_ENTITY);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof AnnealerBlockEntity annealer) {
            player.openHandledScreen(annealer);
            player.incrementStat(ExtraStats.INTERACT_WITH_ANNEALER);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ExtraBlocks.ANNEALER_BLOCK_ENTITY.instantiate(pos, state);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY();
            double z = pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.1D) {
                world.playSound(x, y, z, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double offset = random.nextDouble() * 0.6D - 0.3D;
            double dX = axis == Direction.Axis.X ? (double)direction.getOffsetX() * 0.52D : offset;
            double dZ = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * 0.52D : offset;
            double dY = random.nextDouble() * 9.0D / 16.0D;
            world.addParticle(ParticleTypes.SMOKE, x + dX, y + dY, z + dZ, 0.0D, 0.0D, 0.0D);
        }
    }
}
