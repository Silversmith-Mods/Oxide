package com.ordana.oxide.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModItems;
import com.ordana.oxide.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RebarBlock extends Block implements SimpleWaterloggedBlock {
    public static final MapCodec<WallBlock> CODEC = simpleCodec(WallBlock::new);

    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final BooleanProperty EAST;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty EAST_UPPER;
    public static final BooleanProperty NORTH_UPPER;
    public static final BooleanProperty SOUTH_UPPER;
    public static final BooleanProperty WEST_UPPER;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;

    public static final BooleanProperty WATERLOGGED;

    protected static final VoxelShape SHAPE;
    protected static final VoxelShape BIG_SHAPE;


    public @NotNull MapCodec<WallBlock> codec() {
        return CODEC;
    }

    public RebarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(NORTH, true)
                .setValue(EAST, true)
                .setValue(SOUTH, true)
                .setValue(WEST, true)
                .setValue(NORTH_UPPER, true)
                .setValue(EAST_UPPER, true)
                .setValue(SOUTH_UPPER, true)
                .setValue(WEST_UPPER, true)
                .setValue(UP, true)
                .setValue(DOWN, true)
                .setValue(WATERLOGGED, false));

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

    protected boolean upperCheck(BlockState state, Direction dir) {
        if (state.hasProperty(RebarBlock.UP)) {
            if (state.is(ModBlocks.REINFORCED_CEMENT.get())) {
                return state.getValue(PROPERTY_BY_DIRECTION.get(dir.getOpposite()));
            }
            if (state.is(ModTags.REBAR)) {
                return state.getValue(RebarBlock.UP);
            }

        }
        return true;
    }

    protected boolean rebarChecker(BlockGetter level, BlockPos pos, Direction dir) {
        var dirState = level.getBlockState(pos.relative(dir));
        if (dirState.is(ModTags.REBAR)) return false;
        if (dirState.is(ModBlocks.REINFORCED_CEMENT.get())) {
            return dirState.getValue(PROPERTY_BY_DIRECTION.get(dir.getOpposite()));
        }
        return true;
    }


    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockGetter level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(DOWN, rebarChecker(level, pos, Direction.DOWN))
                .setValue(UP, rebarChecker(level, pos, Direction.UP))
                .setValue(NORTH, rebarChecker(level, pos, Direction.NORTH))
                .setValue(EAST, rebarChecker(level, pos, Direction.EAST))
                .setValue(SOUTH, rebarChecker(level, pos, Direction.SOUTH))
                .setValue(WEST, rebarChecker(level, pos, Direction.WEST))
                .setValue(NORTH_UPPER, upperCheck(level.getBlockState(pos.north()), Direction.NORTH))
                .setValue(EAST_UPPER, upperCheck(level.getBlockState(pos.east()), Direction.EAST))
                .setValue(SOUTH_UPPER, upperCheck(level.getBlockState(pos.south()), Direction.SOUTH))
                .setValue(WEST_UPPER, upperCheck(level.getBlockState(pos.west()), Direction.WEST))
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return state
                .setValue(DOWN, rebarChecker(level, pos, Direction.DOWN))
                .setValue(UP, rebarChecker(level, pos, Direction.UP))
                .setValue(NORTH, rebarChecker(level, pos, Direction.NORTH))
                .setValue(EAST, rebarChecker(level, pos, Direction.EAST))
                .setValue(SOUTH, rebarChecker(level, pos, Direction.SOUTH))
                .setValue(WEST, rebarChecker(level, pos, Direction.WEST))
                .setValue(NORTH_UPPER, upperCheck(level.getBlockState(pos.north()), Direction.NORTH))
                .setValue(EAST_UPPER, upperCheck(level.getBlockState(pos.east()), Direction.EAST))
                .setValue(SOUTH_UPPER, upperCheck(level.getBlockState(pos.south()), Direction.SOUTH))
                .setValue(WEST_UPPER, upperCheck(level.getBlockState(pos.west()), Direction.WEST));
    }


    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.NORTH)), state.getValue(NORTH))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.SOUTH)), state.getValue(SOUTH))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.EAST)), state.getValue(EAST))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.WEST)), state.getValue(WEST))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.UP)), state.getValue(UP))
                .setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.DOWN)), state.getValue(DOWN));
    }

    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.NORTH)), state.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.SOUTH)), state.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.EAST)), state.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.WEST)), state.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.UP)), state.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.DOWN)), state.getValue(DOWN));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, NORTH_UPPER, EAST_UPPER, SOUTH_UPPER, WEST_UPPER, WATERLOGGED);
    }


    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        ItemStack itemStack = useContext.getItemInHand();
        return itemStack.is(ModItems.CEMENT_BUCKET.get());
    }

    static {
        SHAPE = Block.box(2.0, 2.0, 2.0, 14.0, 6.0, 14.0);
        BIG_SHAPE = Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0);
        
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        NORTH_UPPER = ModBlockProperties.NORTH_UPPER;
        EAST_UPPER = ModBlockProperties.EAST_UPPER;
        SOUTH_UPPER = ModBlockProperties.SOUTH_UPPER;
        WEST_UPPER = ModBlockProperties.WEST_UPPER;
        UP = PipeBlock.UP;
        DOWN = PipeBlock.DOWN;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    }
}
