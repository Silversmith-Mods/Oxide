package com.ordana.oxide.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.oxide.reg.ModItems;
import com.ordana.oxide.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RebarCementBlock extends Block {
    public static final MapCodec<WallBlock> CODEC = simpleCodec(WallBlock::new);

    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final BooleanProperty EAST;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    public static final EnumProperty<SlabType> TYPE;

    protected static final VoxelShape SHAPE;
    protected static final VoxelShape BIG_SHAPE;


    public @NotNull MapCodec<WallBlock> codec() {
        return CODEC;
    }

    public RebarCementBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, true).setValue(EAST, true).setValue(SOUTH, true).setValue(WEST, true).setValue(UP, true).setValue(DOWN, true).setValue(TYPE, SlabType.BOTTOM));

    }

    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(UP) ? SHAPE : BIG_SHAPE);
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter blockGetter = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(DOWN, !blockGetter.getBlockState(blockPos.below()).is(ModTags.REBAR))
                .setValue(UP, !blockGetter.getBlockState(blockPos.above()).is(ModTags.REBAR))
                .setValue(NORTH, !blockGetter.getBlockState(blockPos.north()).is(ModTags.REBAR))
                .setValue(EAST, !blockGetter.getBlockState(blockPos.east()).is(ModTags.REBAR))
                .setValue(SOUTH, !blockGetter.getBlockState(blockPos.south()).is(ModTags.REBAR))
                .setValue(WEST, !blockGetter.getBlockState(blockPos.west()).is(ModTags.REBAR));
    }

    protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(direction), !neighborState.is(ModTags.REBAR));
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.NORTH)), state.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.SOUTH)), state.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.EAST)), state.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.WEST)), state.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.UP)), state.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.DOWN)), state.getValue(DOWN));
    }

    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.NORTH)), state.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.SOUTH)), state.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.EAST)), state.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.WEST)), state.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.UP)), state.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.DOWN)), state.getValue(DOWN));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, TYPE);
    }

    static {
        SHAPE = Block.box(2.0, 2.0, 2.0, 14.0, 6.0, 14.0);
        BIG_SHAPE = Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0);
        
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        UP = PipeBlock.UP;
        DOWN = PipeBlock.DOWN;
        TYPE = BlockStateProperties.SLAB_TYPE;
        
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    }
}
