package com.ordana.oxide.blocks;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WetCementBlock extends Block {
    public static final EnumProperty<SlabType> TYPE;
    protected static final VoxelShape BOTTOM_AABB;
    protected static final VoxelShape BOTTOM_COLLISION_AABB;
    protected static final VoxelShape DOUBLE_COLLISION_AABB;


    public WetCementBlock(Properties properties) {
        super(properties);
        this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM);
    }

    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) != SlabType.DOUBLE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        var belowState = serverLevel.getBlockState(pos.below());
        if (!belowState.is(this) && belowState.isFaceSturdy(serverLevel, pos.below(), Direction.UP)) serverLevel.setBlockAndUpdate(pos, state.getValue(TYPE) == SlabType.DOUBLE ? ModBlocks.CEMENT.get().defaultBlockState() : ModBlocks.CEMENT_SLAB.get().defaultBlockState());
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(ItemTags.SHOVELS)) {
            var dir = player.getDirection();
            if (player.isCrouching()) dir = dir.getOpposite();
            var relativePos = pos.relative(dir);
            var relativeState = level.getBlockState(relativePos);

            if (state.getValue(TYPE) == SlabType.BOTTOM) {
                if (relativeState.canBeReplaced()) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    level.setBlockAndUpdate(relativePos, state);
                    level.scheduleTick(relativePos, this, 8);
                    level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                if (relativeState.is(ModTags.CEMENT)) {
                    if (relativeState.getValue(TYPE) == SlabType.BOTTOM) {
                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        level.setBlockAndUpdate(relativePos, relativeState.setValue(TYPE, SlabType.DOUBLE));
                        level.scheduleTick(relativePos, this, 8);
                        level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
            if (state.getValue(TYPE) == SlabType.DOUBLE) {
                if (relativeState.canBeReplaced()) {
                    level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
                    level.setBlockAndUpdate(relativePos, state.setValue(TYPE, SlabType.BOTTOM));
                    level.scheduleTick(relativePos, this, 8);
                    level.playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                }
                if (relativeState.is(ModTags.CEMENT)) {
                    if (relativeState.getValue(TYPE) == SlabType.BOTTOM) {
                        level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
                        level.setBlockAndUpdate(relativePos, relativeState.setValue(TYPE, SlabType.DOUBLE));
                        level.scheduleTick(relativePos, this, 8);
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
        return BOTTOM_AABB;
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
        context.getLevel().scheduleTick(context.getClickedPos(), this, 8);
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.is(ModTags.CEMENT)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE);
        } else {
            return this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM);
        }
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        ItemStack itemStack = useContext.getItemInHand();
        SlabType slabType = state.getValue(TYPE);
        if (slabType != SlabType.DOUBLE && (itemStack.is(this.asItem()) || itemStack.is(ModItems.CEMENT_BUCKET.get()))) {
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

        level.scheduleTick(pos, this, 8);

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        var belowState = level.getBlockState(pos.below());
        if (belowState.canBeReplaced() || belowState.is(ModBlocks.REBAR.get())) {
            if (state.getValue(TYPE) == SlabType.DOUBLE) {
                level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
                level.setBlockAndUpdate(pos.below(), belowState.is(ModBlocks.REBAR.get()) ?
                        ModBlocks.CEMENTED_REBAR.get().withPropertiesOf(belowState).setValue(TYPE, SlabType.BOTTOM) :
                        ModBlocks.WET_CEMENT.get().withPropertiesOf(belowState).setValue(TYPE, SlabType.BOTTOM));
                return;
            }
            else {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(pos.below(), belowState.is(ModBlocks.REBAR.get()) ?
                        ModBlocks.CEMENTED_REBAR.get().withPropertiesOf(belowState).setValue(TYPE, state.getValue(TYPE)) :
                        ModBlocks.WET_CEMENT.get().withPropertiesOf(belowState).setValue(TYPE, state.getValue(TYPE)));
            }
            level.scheduleTick(pos.below(), this, 8);
            return;
        }

        if (belowState.is(ModTags.CEMENT)) {
            if (belowState.getValue(TYPE) == SlabType.BOTTOM) {
                level.setBlockAndUpdate(pos, state.getValue(TYPE) == SlabType.DOUBLE ?
                        state.setValue(TYPE, SlabType.BOTTOM) :
                        Blocks.AIR.defaultBlockState());
                level.setBlockAndUpdate(pos.below(), belowState.is(ModBlocks.CEMENTED_REBAR.get()) ?
                        ModBlocks.CEMENTED_REBAR.get().withPropertiesOf(belowState).setValue(TYPE, SlabType.DOUBLE) :
                        ModBlocks.WET_CEMENT.get().defaultBlockState().setValue(TYPE, SlabType.DOUBLE));
                level.scheduleTick(pos.below(), this, 8);
                return;
            }
        }

        if (state.getValue(TYPE) == SlabType.DOUBLE) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
                var dirState = level.getBlockState(pos.relative(dir));
                if (dirState.canBeReplaced() || dirState.is(ModBlocks.REBAR.get())) {
                    level.setBlockAndUpdate(pos.relative(dir), dirState.is(ModBlocks.REBAR.get()) ?
                            ModBlocks.CEMENTED_REBAR.get().withPropertiesOf(dirState).setValue(TYPE, SlabType.BOTTOM) :
                            ModBlocks.WET_CEMENT.get().defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
                    level.setBlockAndUpdate(pos, state.setValue(TYPE, SlabType.BOTTOM));
                    level.scheduleTick(pos.relative(dir), this, 8);

                    break;
                }
            }
        }

        if (state.getValue(TYPE) == SlabType.BOTTOM) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
                var dirPos = pos.relative(dir).below();
                var dirState = level.getBlockState(dirPos);

                if (dirState.canBeReplaced() || dirState.is(ModBlocks.REBAR.get())) {
                    if (dirState.canBeReplaced()) {
                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        level.setBlockAndUpdate(dirPos, state.setValue(TYPE, SlabType.BOTTOM));
                        level.setBlockAndUpdate(dirPos, dirState.is(ModBlocks.REBAR.get()) ?
                                ModBlocks.CEMENTED_REBAR.get().withPropertiesOf(dirState).setValue(TYPE, SlabType.BOTTOM) :
                                ModBlocks.WET_CEMENT.get().defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
                        level.scheduleTick(dirPos, this, 8);
                        break;
                    }
                }
                if (dirState.is(ModTags.CEMENT)) {
                    if (dirState.getValue(TYPE) == SlabType.BOTTOM) {

                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        level.setBlockAndUpdate(dirPos, dirState.is(ModBlocks.REBAR.get()) ?
                                ModBlocks.CEMENTED_REBAR.get().withPropertiesOf(dirState).setValue(TYPE, SlabType.DOUBLE) :
                                ModBlocks.WET_CEMENT.get().defaultBlockState().setValue(TYPE, SlabType.DOUBLE));
                        level.scheduleTick(dirPos, this, 8);
                        break;
                    }
                }
            }
        }
    }

    static {
        TYPE = BlockStateProperties.SLAB_TYPE;
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        BOTTOM_COLLISION_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);
        DOUBLE_COLLISION_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    }
}