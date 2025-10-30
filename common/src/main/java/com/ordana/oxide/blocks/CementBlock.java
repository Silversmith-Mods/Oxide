package com.ordana.oxide.blocks;

import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CementBlock extends Block implements Fallable {
    public static final IntegerProperty OVERHANG;
    public static final int MAX_OVERHANG = 4;

    public CementBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(OVERHANG, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OVERHANG);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Level level = context.getLevel();
        return this.defaultBlockState().setValue(OVERHANG, this.getOverhang(level, blockPos));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OVERHANG) == MAX_OVERHANG) {
            FallingBlockEntity.fall(level, pos, state.setValue(OVERHANG, 0));
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        updateOverhang(state, level, pos);
    }

    private void updateOverhang(BlockState state, Level level, BlockPos pos) {
        int supported = getOverhang(level, pos);
        if (supported != state.getValue(OVERHANG)) {
            level.setBlockAndUpdate(pos, state.setValue(OVERHANG, supported));
        }
        if (supported == MAX_OVERHANG) {
            level.scheduleTick(pos, state.getBlock(), 1);
        }
    }

    private int getOverhang(Level level, BlockPos pos) {
        int overInt = MAX_OVERHANG;
        for (var dir : Direction.values()) {
            if (dir != Direction.UP && dir != Direction.DOWN) {
                BlockState state = level.getBlockState(pos);
                BlockPos neighborPos = pos.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);

                if (neighborState.hasProperty(OVERHANG)) {
                    if (state.hasProperty(OVERHANG)) if (neighborState.getValue(OVERHANG) > state.getValue(OVERHANG)) return state.getValue(OVERHANG);
                    overInt = Math.min(neighborState.getValue(OVERHANG) + 1, overInt);
                    break;
                }
            }
            else if (dir == Direction.DOWN) {
                var free = FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight();
                if (!free) {
                    return 0;
                }
            }
        }
        return overInt;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(16) == 0 && FallingBlock.isFree(level.getBlockState(pos.below()))) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() - 0.05;
            double f = (double) pos.getZ() + random.nextDouble();
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0, 0.0, 0.0);
        }
    }

    static {
        OVERHANG = ModBlockProperties.OVERHANG;
    }
}
