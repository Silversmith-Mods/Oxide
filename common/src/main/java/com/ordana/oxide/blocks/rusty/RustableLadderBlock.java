package com.ordana.oxide.blocks.rusty;

import com.ordana.oxide.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class RustableLadderBlock extends LadderBlock implements Rustable{
    public static final BooleanProperty UP;
    public static final BooleanProperty HANGING;
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;
    private final RustLevel rustLevel;


    public RustableLadderBlock(RustLevel rustLevel, Properties properties) {
        super(Rustable.setRandomTicking(properties, rustLevel));
        this.rustLevel = rustLevel;

        this.registerDefaultState(this.defaultBlockState().setValue(VARNISHED, false).setValue(UP, true).setValue(HANGING, true));
    }

    private boolean canAttachTo(BlockGetter blockReader, BlockPos pos, Direction direction) {
        BlockState blockState = blockReader.getBlockState(pos);
        return blockState.isFaceSturdy(blockReader, pos, direction);
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        return (this.canAttachTo(level, pos.relative(direction.getOpposite()), direction) || level.getBlockState(pos.above()).getBlock() instanceof RustableLadderBlock);
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (direction.getOpposite() == state.getValue(FACING)) {
            return state.setValue(HANGING, false).setValue(UP, isTop(level, pos)).setValue(HANGING, !isHanging(level, pos, neighborPos, direction));
        } else {
            if (state.getValue(WATERLOGGED)) {
                level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
            return super.updateShape(state.setValue(UP, isTop(level, pos)), direction, neighborState, level, pos, neighborPos);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState;
        if (!context.replacingClickedOnBlock()) {
            blockState = context.getLevel().getBlockState(context.getClickedPos().relative(context.getClickedFace().getOpposite()));
            if (blockState.getBlock() instanceof RustableLadderBlock && blockState.getValue(FACING) == context.getClickedFace()) {
                return null;
            }
        }

        LevelReader levelReader = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        blockState = this.defaultBlockState();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());

        if (context.getClickedFace() != Direction.UP && context.getClickedFace() != Direction.DOWN) {
            blockState = blockState.setValue(FACING, context.getClickedFace());
        }
        else if (levelReader.getBlockState(blockPos.above()).getBlock() instanceof RustableLadderBlock) {
            blockState = blockState.setValue(FACING, levelReader.getBlockState(blockPos.above()).getValue(FACING));
        }
        blockState = blockState.setValue(UP, isTop(levelReader, blockPos)).setValue(HANGING, !isHanging(levelReader, blockPos, blockPos.relative(blockState.getValue(FACING).getOpposite()), blockState.getValue(FACING)));

        if (blockState.canSurvive(levelReader, blockPos)) {
            return blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        }

        return null;
    }

    private boolean isTop(BlockGetter level, BlockPos pos) {
        return !(level.getBlockState(pos.above()).getBlock() instanceof RustableLadderBlock);
    }

    private boolean isHanging(BlockGetter level, BlockPos pos, BlockPos relativeState, Direction dir) {
        return !level.getBlockState(relativeState).isFaceSturdy(level, pos, dir);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, FACING, HANGING, WATERLOGGED, VARNISHED);
    }


    static {
        HANGING = BlockStateProperties.HANGING;
        UP = BlockStateProperties.UP;
    }

    @Override
    public RustLevel getAge() {
        return this.rustLevel;
    }
}
