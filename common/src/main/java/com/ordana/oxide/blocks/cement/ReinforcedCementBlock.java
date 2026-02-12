package com.ordana.oxide.blocks.cement;

import com.mojang.serialization.MapCodec;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ReinforcedCementBlock extends Block implements WeatherableCement {
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
    public static final EnumProperty<SlabType> TYPE;

    protected static final VoxelShape SHAPE;
    protected static final VoxelShape BIG_SHAPE;


    public @NotNull MapCodec<WallBlock> codec() {
        return CODEC;
    }

    public ReinforcedCementBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(NORTH, true).setValue(EAST, true).setValue(SOUTH, true).setValue(WEST, true).setValue(UP, true).setValue(DOWN, true).setValue(TYPE, SlabType.BOTTOM));

    }

    public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModBlocks.REBAR.get());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        tryWeather(state, serverLevel, pos, random);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(TYPE) == SlabType.DOUBLE ? BIG_SHAPE : SHAPE);
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.NORTH)), state.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.SOUTH)), state.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.EAST)), state.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.WEST)), state.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.UP)), state.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(Direction.DOWN)), state.getValue(DOWN));
    }

    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.NORTH)), state.getValue(NORTH)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.SOUTH)), state.getValue(SOUTH)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.EAST)), state.getValue(EAST)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.WEST)), state.getValue(WEST)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.UP)), state.getValue(UP)).setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(Direction.DOWN)), state.getValue(DOWN));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, NORTH_UPPER, EAST_UPPER, SOUTH_UPPER, WEST_UPPER, TYPE);
    }

    static {
        SHAPE = Block.box(0, 0, 0, 16.0, 8.0, 16.0);
        BIG_SHAPE = Block.box(0, 0, 0, 16.0, 16.0, 16.0);

        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        UP = PipeBlock.UP;
        DOWN = PipeBlock.DOWN;
        NORTH_UPPER = ModBlockProperties.NORTH_UPPER;
        EAST_UPPER = ModBlockProperties.EAST_UPPER;
        SOUTH_UPPER = ModBlockProperties.SOUTH_UPPER;
        WEST_UPPER = ModBlockProperties.WEST_UPPER;
        TYPE = BlockStateProperties.SLAB_TYPE;

        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    }
}
