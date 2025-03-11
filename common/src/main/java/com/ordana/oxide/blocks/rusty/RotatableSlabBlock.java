package com.ordana.oxide.blocks.rusty;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Objects;

public class RotatableSlabBlock extends RustableSlabBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public RotatableSlabBlock(RustLevel rustLevel, Properties settings) {
        super(rustLevel, settings);
        this.stateDefinition.any().setValue(FACING, Direction.NORTH);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        var bl = Objects.requireNonNull(context.getPlayer()).isSecondaryUseActive();
        var dir = context.getHorizontalDirection();
        var type = (context.getClickLocation().y - (double)blockPos.getY() > 0.5) ? SlabType.TOP : SlabType.BOTTOM;
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false).setValue(FACING, bl ? dir.getOpposite() : dir);
        } return this.defaultBlockState().setValue(FACING, bl ? dir.getOpposite() : dir).setValue(TYPE, type);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
}
