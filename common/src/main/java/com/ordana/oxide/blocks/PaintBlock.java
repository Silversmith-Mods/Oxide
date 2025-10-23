package com.ordana.oxide.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.ordana.oxide.reg.ModBlockProperties;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class PaintBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private static final Map SHAPE_BY_DIRECTION;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    protected static final Direction[] DIRECTIONS;
    private final ImmutableMap<BlockState, VoxelShape> shapesCache;
    private final boolean canRotate;
    private final boolean canMirrorX;
    private final boolean canMirrorZ;


    public PaintBlock(Properties properties) {
        super(properties);
        this.shapesCache = this.getShapeForEachState(PaintBlock::calculateMultifaceShape);
        this.canRotate = Direction.Plane.HORIZONTAL.stream().allMatch(this::isFaceSupported);
        this.canMirrorX = Direction.Plane.HORIZONTAL.stream().filter(Direction.Axis.X).filter(this::isFaceSupported).count() % 2L == 0L;
        this.canMirrorZ = Direction.Plane.HORIZONTAL.stream().filter(Direction.Axis.Z).filter(this::isFaceSupported).count() % 2L == 0L;
        this.registerDefaultState(getDefaultMultifaceState(this.stateDefinition).setValue(VARNISHED, false).setValue(WATERLOGGED, false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (serverLevel.isRainingAt(pos) && !state.getValue(VARNISHED)) serverLevel.destroyBlock(pos, false);
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            if (!state.getValue(ModBlockProperties.VARNISHED)) level.destroyBlock(pos, false);
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!hasAnyFace(state)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return hasFace(state, direction) && !canAttachTo(level, direction, neighborPos, neighborState) ? removeFace(state, getFaceProperty(direction)) : state;
        }
    }

    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    private static VoxelShape calculateMultifaceShape(BlockState state) {
        VoxelShape voxelShape = Shapes.empty();
        Direction[] var2 = DIRECTIONS;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Direction direction = var2[var4];
            if (hasFace(state, direction)) {
                voxelShape = Shapes.or(voxelShape, (VoxelShape) SHAPE_BY_DIRECTION.get(direction));
            }
        }

        return voxelShape.isEmpty() ? DOWN_AABB : voxelShape;
    }

    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return DOWN_AABB;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        Direction[] var2 = DIRECTIONS;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Direction direction = var2[var4];
            if (this.isFaceSupported(direction)) {
                builder.add(getFaceProperty(direction));
            }
        }
        builder.add(WATERLOGGED, VARNISHED);
    }

    static {
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
        SHAPE_BY_DIRECTION = Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
            enumMap.put(Direction.NORTH, NORTH_AABB);
            enumMap.put(Direction.EAST, EAST_AABB);
            enumMap.put(Direction.SOUTH, SOUTH_AABB);
            enumMap.put(Direction.WEST, WEST_AABB);
            enumMap.put(Direction.UP, UP_AABB);
            enumMap.put(Direction.DOWN, DOWN_AABB);
        });
        DIRECTIONS = Direction.values();
    }

    protected boolean isFaceSupported(Direction face) {
        return true;
    }

    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean bl = false;
        Direction[] var5 = DIRECTIONS;
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Direction direction = var5[var7];
            if (hasFace(state, direction)) {
                BlockPos blockPos = pos.relative(direction);
                if (!canAttachTo(level, direction, blockPos, level.getBlockState(blockPos))) {
                    return false;
                }

                bl = true;
            }
        }

        return bl;
    }

    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return hasAnyVacantFace(state);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        return Arrays.stream(context.getNearestLookingDirections()).map((direction) -> this.getStateForPlacement(blockState, level, blockPos, direction)).filter(Objects::nonNull).findFirst().orElse(null);
    }

    public boolean isValidStateForPlacement(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        if (this.isFaceSupported(direction) && (!state.is(this) || !hasFace(state, direction))) {
            BlockPos blockPos = pos.relative(direction);
            return canAttachTo(level, direction, blockPos, level.getBlockState(blockPos));
        } else {
            return false;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockState currentState, BlockGetter level, BlockPos pos, Direction lookingDirection) {
        if (!this.isValidStateForPlacement(level, currentState, pos, lookingDirection)) {
            return null;
        } else {
            BlockState blockState;
            if (currentState.is(this)) {
                blockState = currentState;
            } else if (this.isWaterloggable() && currentState.getFluidState().isSourceOfType(Fluids.WATER)) {
                blockState = this.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, true);
            } else {
                blockState = this.defaultBlockState();
            }

            return blockState.setValue(getFaceProperty(lookingDirection), true);
        }
    }

    protected BlockState rotate(BlockState state, Rotation rotation) {
        if (!this.canRotate) {
            return state;
        } else {
            Objects.requireNonNull(rotation);
            return this.mapDirections(state, rotation::rotate);
        }
    }

    protected BlockState mirror(BlockState state, Mirror mirror) {
        if (mirror == Mirror.FRONT_BACK && !this.canMirrorX) {
            return state;
        } else if (mirror == Mirror.LEFT_RIGHT && !this.canMirrorZ) {
            return state;
        } else {
            Objects.requireNonNull(mirror);
            return this.mapDirections(state, mirror::mirror);
        }
    }

    private BlockState mapDirections(BlockState state, Function<Direction, Direction> directionalFunction) {
        BlockState blockState = state;
        Direction[] var4 = DIRECTIONS;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Direction direction = var4[var6];
            if (this.isFaceSupported(direction)) {
                blockState = blockState.setValue(getFaceProperty((Direction)directionalFunction.apply(direction)), (Boolean)state.getValue(getFaceProperty(direction)));
            }
        }

        return blockState;
    }

    public static boolean hasFace(BlockState state, Direction direction) {
        BooleanProperty booleanProperty = getFaceProperty(direction);
        return state.hasProperty(booleanProperty) && (Boolean)state.getValue(booleanProperty);
    }

    public static boolean canAttachTo(BlockGetter level, Direction direction, BlockPos pos, BlockState state) {
        return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction.getOpposite()) || Block.isFaceFull(state.getCollisionShape(level, pos), direction.getOpposite());
    }

    private boolean isWaterloggable() {
        return this.stateDefinition.getProperties().contains(BlockStateProperties.WATERLOGGED);
    }

    private static BlockState removeFace(BlockState state, BooleanProperty faceProp) {
        BlockState blockState = (BlockState)state.setValue(faceProp, false);
        return hasAnyFace(blockState) ? blockState : Blocks.AIR.defaultBlockState();
    }

    public static BooleanProperty getFaceProperty(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }

    private static BlockState getDefaultMultifaceState(StateDefinition<Block, BlockState> stateDefinition) {
        BlockState blockState = stateDefinition.any();
        Iterator var2 = PROPERTY_BY_DIRECTION.values().iterator();

        while(var2.hasNext()) {
            BooleanProperty booleanProperty = (BooleanProperty)var2.next();
            if (blockState.hasProperty(booleanProperty)) {
                blockState = blockState.setValue(booleanProperty, false);
            }
        }

        return blockState;
    }

    protected static boolean hasAnyFace(BlockState state) {
        return Arrays.stream(DIRECTIONS).anyMatch((direction) -> {
            return hasFace(state, direction);
        });
    }

    private static boolean hasAnyVacantFace(BlockState state) {
        return Arrays.stream(DIRECTIONS).anyMatch((direction) -> {
            return !hasFace(state, direction);
        });
    }


}
