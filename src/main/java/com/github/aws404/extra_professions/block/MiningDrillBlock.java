package com.github.aws404.extra_professions.block;

import com.github.aws404.extra_professions.ExtraTags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;

public class MiningDrillBlock extends WallMountedBlock {
    private static final VoxelShape FLOOR_Z_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 2.0D, 16.0D, 12.0D, 14.0D);
    private static final VoxelShape FLOOR_X_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 12.0D, 16.0D);
    private static final VoxelShape CEILING_Z_SHAPE = Block.createCuboidShape(0.0D, 4.0D, 2.0D, 16.0D, 16.0D, 14.0D);
    private static final VoxelShape CEILING_X_SHAPE = Block.createCuboidShape(2.0D, 4.0D, 0.0D, 14.0D, 16.0D, 16.0D);
    private static final VoxelShape WALL_NORTH_SHAPE = Block.createCuboidShape(0.0D, 2.0D, 4.0D, 16.0D, 14.0D, 16.0D);
    private static final VoxelShape WALL_EAST_SHAPE = Block.createCuboidShape(0.0D, 2.0D, 0.0D, 12.0D, 14.0D, 16.0D);
    private static final VoxelShape WALL_SOUTH_SHAPE = Block.createCuboidShape(0.0D, 2.0D, 0.0D, 16.0D, 14.0D, 12.0D);
    private static final VoxelShape WALL_WEST_SHAPE = Block.createCuboidShape(4.0D, 2.0D, 0.0D, 16.0D, 14.0D, 16.0D);

    public MiningDrillBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, FACE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACE)) {
            case FLOOR -> state.get(FACING).getAxis() == Direction.Axis.Z ? FLOOR_Z_SHAPE : FLOOR_X_SHAPE;
            case CEILING -> state.get(FACING).getAxis() == Direction.Axis.Z ? CEILING_Z_SHAPE : CEILING_X_SHAPE;
            case WALL -> switch (state.get(FACING)) {
                case EAST -> WALL_EAST_SHAPE;
                case SOUTH -> WALL_SOUTH_SHAPE;
                case WEST -> WALL_WEST_SHAPE;
                default -> WALL_NORTH_SHAPE;
            };
        };
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Direction oppositeFace = getDirection(state);
        Direction.Axis destructionAxis = switch (state.get(FACE)) {
            case FLOOR, CEILING -> state.get(FACING).getAxis();
            case WALL -> Direction.Axis.Y;
        };

        Set<Direction> checkDirs = Direction.shuffle(random).stream().filter(direction -> destructionAxis.test(direction) || direction == oppositeFace).collect(Collectors.toSet());

        for (Direction checkDir : checkDirs) {
            if (world.getBlockState(pos.offset(checkDir)).isIn(ExtraTags.MINING_DRILLABLE_BLOCKS)) {
                world.breakBlock(pos.offset(checkDir), true);
                return;
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        Direction baseFace = getDirection(state).getOpposite();

        BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(pos.offset(baseFace)));
        for(int i = 0; i < 3; ++i) {
            double x = random.nextTriangular(pos.getX() + 0.5D, 0.2D);
            double y = random.nextTriangular(pos.getY() + 0.5D, 0.2D);
            double z = random.nextTriangular(pos.getZ() + 0.5D, 0.2D);
            world.addParticle(particle, x, y, z, 0.2D, 0.2D, 0.2D);
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}
