package com.ordana.oxide.blocks.cement;

import com.ordana.oxide.blocks.rusty.RustableLadderBlock;
import com.ordana.oxide.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.stream.IntStream;

public class CinderBlockBlock extends Block implements SimpleWaterloggedBlock {


    public static final DirectionProperty FACING;
    public static final EnumProperty<Half> HALF;
    public static final EnumProperty<StairsShape> SHAPE;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape OCTET_NNN;
    protected static final VoxelShape OCTET_NNP;
    protected static final VoxelShape OCTET_NPN;
    protected static final VoxelShape OCTET_NPP;
    protected static final VoxelShape OCTET_PNN;
    protected static final VoxelShape OCTET_PNP;
    protected static final VoxelShape OCTET_PPN;
    protected static final VoxelShape OCTET_PPP;
    protected static final VoxelShape[] TOP_SHAPES;
    protected static final VoxelShape[] BOTTOM_SHAPES;
    private static final int[] SHAPE_BY_STATE;
    //private final Block base;
    //protected final BlockState baseState;


    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        ItemStack itemStack = useContext.getItemInHand();
        var shape = state.getValue(SHAPE);
        var dir = state.getValue(FACING).getOpposite();

        if (shape == StairsShape.STRAIGHT && itemStack.is(this.asItem())) {
            if (useContext.replacingClickedOnBlock()) {
                Direction face = useContext.getClickedFace();
                return face == dir;
            }
        }
        return false;
    }

    private static VoxelShape[] makeShapes(VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
        return IntStream.range(0, 16).mapToObj((i) -> makeStairShape(i, nwCorner, neCorner, swCorner, seCorner)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeStairShape(int bitfield, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner) {
        VoxelShape voxelShape = Shapes.empty();
        if ((bitfield & 1) != 0) {
            voxelShape = Shapes.or(voxelShape, nwCorner);
        }

        if ((bitfield & 2) != 0) {
            voxelShape = Shapes.or(voxelShape, neCorner);
        }

        if ((bitfield & 4) != 0) {
            voxelShape = Shapes.or(voxelShape, swCorner);
        }

        if ((bitfield & 8) != 0) {
            voxelShape = Shapes.or(voxelShape, seCorner);
        }

        return voxelShape;
    }

    public CinderBlockBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, false));
        //this.base = baseState.getBlock();
        //this.baseState = baseState;
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(HALF) == Half.TOP ? TOP_SHAPES : BOTTOM_SHAPES)[SHAPE_BY_STATE[this.getShapeIndex(state)]];
    }

    private int getShapeIndex(BlockState state) {
        return state.getValue(SHAPE).ordinal() * 4 + state.getValue(FACING).get2DDataValue();
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(blockPos);
        var state = context.getLevel().getBlockState(blockPos);
        if (state.is(this)) return ModBlocks.CINDER_BLOCK_SLAB.get().withPropertiesOf(state).setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y);
        BlockState blockState = this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(HALF, Half.BOTTOM)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
        return blockState.setValue(SHAPE, getStairsShape(blockState, context.getLevel(), blockPos));
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : direction.getAxis().isHorizontal() ? state.setValue(SHAPE, getStairsShape(state, level, pos)) : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private static StairsShape getStairsShape(BlockState state, BlockGetter level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockState = level.getBlockState(pos.relative(direction));
        if (isStairs(blockState) && state.getValue(HALF) == blockState.getValue(HALF)) {
            Direction direction2 = blockState.getValue(FACING);
            if (direction2.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, level, pos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState blockState2 = level.getBlockState(pos.relative(direction.getOpposite()));
        if (isStairs(blockState2) && state.getValue(HALF) == blockState2.getValue(HALF)) {
            Direction direction3 = blockState2.getValue(FACING);
            if (direction3.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, level, pos, direction3)) {
                if (direction3 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        BlockState blockState = level.getBlockState(pos.relative(face));
        return !isStairs(blockState) || blockState.getValue(FACING) != state.getValue(FACING) || blockState.getValue(HALF) != state.getValue(HALF);
    }

    public static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof CinderBlockBlock;
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        StairsShape stairsShape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    switch (stairsShape) {
                        case INNER_LEFT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        }
                        case INNER_RIGHT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        }
                        case OUTER_LEFT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        }
                        case OUTER_RIGHT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        }
                        default -> {
                            return state.rotate(Rotation.CLOCKWISE_180);
                        }
                    }
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    switch (stairsShape) {
                        case INNER_LEFT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        }
                        case INNER_RIGHT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        }
                        case OUTER_LEFT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        }
                        case OUTER_RIGHT -> {
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        }
                        case STRAIGHT -> {
                            return state.rotate(Rotation.CLOCKWISE_180);
                        }
                    }
                }
        }

        return super.mirror(state, mirror);
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        HALF = BlockStateProperties.HALF;
        SHAPE = BlockStateProperties.STAIRS_SHAPE;

        WATERLOGGED = BlockStateProperties.WATERLOGGED;

        OCTET_NNN = Block.box(0.0, 8.0, 0.0, 16.0, 8.0, 8.0);
        OCTET_NNP = Block.box(0.0, 8.0, 8.0, 16.0, 8.0, 16.0);
        OCTET_NPN = Block.box(0.0, 0.0, 0.0, 8.0, 8.0, 8.0);
        OCTET_NPP = Block.box(0.0, 0.0, 8.0, 8.0, 8.0, 16.0);
        OCTET_PNN = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 8.0);
        OCTET_PNP = Block.box(8.0, 8.0, 8.0, 16.0, 16.0, 16.0);
        OCTET_PPN = Block.box(8.0, 0.0, 0.0, 16.0, 8.0, 8.0);
        OCTET_PPP = Block.box(8.0, 0.0, 8.0, 16.0, 8.0, 16.0);
        TOP_SHAPES = makeShapes(OCTET_NNN, OCTET_PNN, OCTET_NNP, OCTET_PNP);
        BOTTOM_SHAPES = makeShapes(OCTET_NPN, OCTET_PPN, OCTET_NPP, OCTET_PPP);
        SHAPE_BY_STATE = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};
    }
}
