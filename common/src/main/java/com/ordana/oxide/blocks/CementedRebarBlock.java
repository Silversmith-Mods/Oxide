package com.ordana.oxide.blocks;

import com.ordana.oxide.configs.CommonConfigs;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModItems;
import com.ordana.oxide.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CementedRebarBlock extends RebarBlock {

    private static final int FLOW_RATE = 8;
    public static final EnumProperty<SlabType> TYPE;
    public static final BooleanProperty CURABLE = ModBlockProperties.CURABLE;
    protected static final VoxelShape TALL_REBAR_SHAPE;
    protected static final VoxelShape SLAB_SHAPE;
    protected static final VoxelShape REBAR_SHAPE;
    protected static final VoxelShape BOTTOM_COLLISION_AABB;
    protected static final VoxelShape DOUBLE_COLLISION_AABB;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    private static final int REGULAR_HATCH_TIME_TICKS = 24000;
    private static final int BOOSTED_HATCH_TIME_TICKS = 12000;
    private static final int OFFSET_TICKS = 300;


    public CementedRebarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false);

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, NORTH_UPPER, EAST_UPPER, SOUTH_UPPER, WEST_UPPER, WATERLOGGED, TYPE, CURABLE);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        var belowState = serverLevel.getBlockState(pos.below());
        if (!belowState.is(ModTags.WET_CEMENT) && !belowState.isAir() && serverLevel.isDay())
            serverLevel.setBlockAndUpdate(pos, ModBlocks.REINFORCED_CEMENT.get().withPropertiesOf(state));
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(ItemTags.SHOVELS)) {
            var dir = player.getDirection();
            if (player.isCrouching()) dir = dir.getOpposite();
            var relativePos = pos.relative(dir);
            var relativeState = level.getBlockState(relativePos);
            var cementState = ModBlocks.WET_CEMENT.get().defaultBlockState();

            if (state.getValue(TYPE) == SlabType.BOTTOM) {
                if (relativeState.canBeReplaced()) {
                    level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
                    level.setBlockAndUpdate(relativePos, cementState);
                    level.scheduleTick(relativePos, relativeState.getBlock(), FLOW_RATE);
                    level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                if (relativeState.is(ModTags.WET_CEMENT)) {
                    if (relativeState.getValue(TYPE) == SlabType.BOTTOM) {
                        level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
                        level.setBlockAndUpdate(relativePos, state.setValue(TYPE, SlabType.DOUBLE));
                        level.scheduleTick(relativePos, relativeState.getBlock(), FLOW_RATE);
                        level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
            if (state.getValue(TYPE) == SlabType.DOUBLE) {
                if (relativeState.canBeReplaced()) {
                    level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
                    level.setBlockAndUpdate(relativePos, state.setValue(TYPE, SlabType.BOTTOM));
                    level.scheduleTick(relativePos, relativeState.getBlock(), FLOW_RATE);
                    level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                if (relativeState.is(ModTags.WET_CEMENT)) {
                    if (relativeState.getValue(TYPE) == SlabType.BOTTOM) {
                        level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
                        level.setBlockAndUpdate(relativePos, state.setValue(TYPE, SlabType.DOUBLE));
                        level.scheduleTick(relativePos, relativeState.getBlock(), FLOW_RATE);
                        level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabType = state.getValue(TYPE);
        if (slabType == SlabType.DOUBLE) {
            return Shapes.block();
        }
        return SLAB_SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabType = state.getValue(TYPE);
        if (slabType == SlabType.DOUBLE) {
            return DOUBLE_COLLISION_AABB;
        }
        return BOTTOM_COLLISION_AABB;
    }


    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        entity.makeStuckInBlock(state, new Vec3(0.25, 0.25, 0.25));
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        context.getLevel().scheduleTick(context.getClickedPos(), this, FLOW_RATE);
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);

        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE);
        } else {
            return this.withPropertiesOf(context.getLevel().getBlockState(blockPos)).setValue(TYPE, SlabType.BOTTOM);
        }
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        ItemStack itemStack = useContext.getItemInHand();
        SlabType slabType = state.getValue(TYPE);
        if (itemStack.is(ModItems.CEMENT_BUCKET.get())) {
            if (useContext.replacingClickedOnBlock()) {
                boolean bl = useContext.getClickLocation().y - (double)useContext.getClickedPos().getY() > 0.5;
                Direction direction = useContext.getClickedFace();
                if (slabType == SlabType.BOTTOM) {
                    return direction == Direction.UP || bl && direction.getAxis().isHorizontal();
                } else {
                    return direction == Direction.DOWN || !bl && direction.getAxis().isHorizontal();
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {

        level.scheduleTick(pos, this, FLOW_RATE);

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        var belowState = level.getBlockState(pos.below());
        if (belowState.canBeReplaced() || belowState.is(ModBlocks.REBAR.get())) {
            boolean bl = belowState.is(ModBlocks.REBAR.get());
            var block = bl ? ModBlocks.CEMENTED_REBAR.get() : ModBlocks.WET_CEMENT.get();

            if (state.getValue(TYPE) == SlabType.DOUBLE) {
                level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false));
                level.setBlockAndUpdate(pos.below(), block.withPropertiesOf(belowState).setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false));
                level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                return;
            }
            else {
                level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
                level.setBlockAndUpdate(pos.below(), block.withPropertiesOf(belowState).setValue(TYPE, state.getValue(TYPE)).setValue(CURABLE, false));
                level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
            }
            level.scheduleTick(pos.below(), block, FLOW_RATE);
            return;
        }

        if (belowState.is(ModTags.WET_CEMENT)) {
            if (belowState.getValue(TYPE) == SlabType.BOTTOM) {
                boolean bl = belowState.is(ModBlocks.CEMENTED_REBAR.get());
                var block = bl ? ModBlocks.CEMENTED_REBAR.get() : ModBlocks.WET_CEMENT.get();

                level.setBlockAndUpdate(pos, state.getValue(TYPE) == SlabType.DOUBLE ?
                        state.setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false) :
                        ModBlocks.REBAR.get().withPropertiesOf(state));

                level.setBlockAndUpdate(pos.below(), block.withPropertiesOf(belowState).setValue(TYPE, SlabType.DOUBLE).setValue(CURABLE, false));
                level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                level.scheduleTick(pos.below(), block, FLOW_RATE);
                return;
            }
        }

        if (state.getValue(TYPE) == SlabType.DOUBLE) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
                var dirState = level.getBlockState(pos.relative(dir));
                if (dirState.canBeReplaced() || dirState.is(ModBlocks.REBAR.get())) {
                    boolean bl = dirState.is(ModBlocks.REBAR.get());
                    var block = bl ? ModBlocks.CEMENTED_REBAR.get() : ModBlocks.WET_CEMENT.get();

                    level.setBlockAndUpdate(pos.relative(dir), block.withPropertiesOf(dirState).setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false));
                    level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false));

                    level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                    level.scheduleTick(pos.relative(dir), block, FLOW_RATE);

                    break;
                }
            }
        }

        if (state.getValue(TYPE) == SlabType.BOTTOM) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
                var dirPos = pos.relative(dir).below();
                var dirState = level.getBlockState(dirPos);
                var adjState = level.getBlockState(pos.relative(dir));

                if (adjState.isCollisionShapeFullBlock(level, pos.relative(dir))) break;

                if (dirState.canBeReplaced() || dirState.is(ModBlocks.REBAR.get())) {
                    boolean bl = dirState.is(ModBlocks.REBAR.get());
                    var block = bl ? ModBlocks.CEMENTED_REBAR.get() : ModBlocks.WET_CEMENT.get();

                        level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
                        level.setBlockAndUpdate(dirPos, block.withPropertiesOf(state).setValue(TYPE, SlabType.BOTTOM).setValue(CURABLE, false));

                        level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                        level.scheduleTick(dirPos, block, FLOW_RATE);
                        break;

                }
                if (dirState.is(ModTags.WET_CEMENT)) {
                    if (dirState.getValue(TYPE) == SlabType.BOTTOM) {
                        boolean bl = dirState.is(ModBlocks.CEMENTED_REBAR.get());
                        var block = bl ? ModBlocks.CEMENTED_REBAR.get() : ModBlocks.WET_CEMENT.get();

                        level.setBlockAndUpdate(pos, ModBlocks.REBAR.get().withPropertiesOf(state));
                        level.setBlockAndUpdate(dirPos, block.withPropertiesOf(dirState).setValue(TYPE, SlabType.DOUBLE).setValue(CURABLE, false));

                        level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                        level.scheduleTick(dirPos, block, FLOW_RATE);
                        break;
                    }
                }
            }
        }

        else if (!state.getValue(CURABLE)) {
            level.setBlockAndUpdate(pos, state.setValue(CURABLE, true));
            level.scheduleTick(pos, this, REGULAR_HATCH_TIME_TICKS + level.random.nextInt(300));
        }

        else if (!belowState.is(ModTags.WET_CEMENT) && !belowState.isAir() && level.isDay())
            level.setBlockAndUpdate(pos, ModBlocks.REINFORCED_CEMENT.get().withPropertiesOf(state));
    }


    static {
        SLAB_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        REBAR_SHAPE = Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0);

        TALL_REBAR_SHAPE = Shapes.or(SLAB_SHAPE, REBAR_SHAPE);


        TYPE = BlockStateProperties.SLAB_TYPE;


        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
        BOTTOM_COLLISION_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);
        DOUBLE_COLLISION_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    }

}
