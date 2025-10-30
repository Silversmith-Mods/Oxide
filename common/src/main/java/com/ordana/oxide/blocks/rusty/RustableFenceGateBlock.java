package com.ordana.oxide.blocks.rusty;

import com.mojang.serialization.MapCodec;
import com.ordana.oxide.entities.SprayParticleEntity;
import com.ordana.oxide.reg.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiConsumer;

public class RustableFenceGateBlock extends HorizontalDirectionalBlock implements Rustable {


    public static final BooleanProperty OPEN;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty TOP;
    public static final BooleanProperty BOTTOM;
    public static final BooleanProperty WIDE;
    public static final DirectionProperty SIDE_DIR;
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;

    protected static final VoxelShape Z_SHAPE_LOW;
    protected static final VoxelShape X_SHAPE_LOW;

    protected static final VoxelShape Z_COLLISION_SHAPE;
    protected static final VoxelShape X_COLLISION_SHAPE;

    protected static final VoxelShape Z_SUPPORT_SHAPE;
    protected static final VoxelShape X_SUPPORT_SHAPE;

    protected static final VoxelShape Z_OCCLUSION_SHAPE_LOW;
    protected static final VoxelShape X_OCCLUSION_SHAPE_LOW;

    private final RustLevel rustLevel;

    public RustableFenceGateBlock(RustLevel rustLevel, Properties properties) {
        super(properties);
        this.rustLevel = rustLevel;
        this.registerDefaultState(this.defaultBlockState()
                .setValue(OPEN, false)
                .setValue(POWERED, false)
                .setValue(TOP, true)
                .setValue(BOTTOM, true)
                .setValue(WIDE, false)
                .setValue(SIDE_DIR, Direction.NORTH)
                .setValue(VARNISHED, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }

    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity instanceof LivingEntity liv) entity.causeFallDamage(Math.min(fallDistance, (liv.getHealth() / 2) + 2f), 2.0F, level.damageSources().stalagmite());
    }

    @Override
    public RustLevel getAge() {
        return this.rustLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!state.getValue(VARNISHED)) this.tryWeather(state, serverLevel, pos, random);
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return this.use(stack, state, level, pos, player, hand, hitResult);
    }


    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE_LOW : Z_SHAPE_LOW;
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis axis = direction.getAxis();
        if (neighborState.getBlock() == this && !axis.isVertical()) {
            if (!state.getValue(WIDE) && ((state.getValue(FACING) == neighborState.getValue(FACING)) || (state.getValue(FACING) == neighborState.getValue(FACING).getOpposite()))) {
                if (neighborState.getValue(WIDE))
                    return state.setValue(WIDE, true).setValue(SIDE_DIR, neighborState.getValue(SIDE_DIR).getOpposite());
            }
        }

        if (state.getValue(FACING).getClockWise().getAxis() != axis) {
            boolean open = state.getValue(OPEN);
            boolean above = level.getBlockState(pos.above()).getBlock() instanceof RustableFenceGateBlock;
            boolean below = level.getBlockState(pos.below()).getBlock() instanceof RustableFenceGateBlock;
            var facing = state.getValue(FACING);
            if (axis == Direction.Axis.Y && neighborState.getBlock() instanceof RustableFenceGateBlock) open = neighborState.getValue(OPEN);
            if (axis == Direction.Axis.Y && neighborState.getBlock() instanceof RustableFenceGateBlock) facing = neighborState.getValue(FACING);
            return super.updateShape(state, direction, neighborState, level, pos, neighborPos).setValue(TOP, !above).setValue(BOTTOM, !below).setValue(OPEN, open).setValue(FACING, facing);
        } else {
            boolean open = state.getValue(OPEN);
            boolean above = level.getBlockState(pos.above()).getBlock() instanceof RustableFenceGateBlock;
            boolean below = level.getBlockState(pos.below()).getBlock() instanceof RustableFenceGateBlock;
            if (axis == Direction.Axis.Y && neighborState.getBlock() instanceof RustableFenceGateBlock) open = neighborState.getValue(OPEN);
            return state.setValue(TOP, !above).setValue(BOTTOM, !below).setValue(OPEN, open);
        }

    }

    protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        if (state.getValue(OPEN)) {
            return Shapes.empty();
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.Z ? Z_SUPPORT_SHAPE : X_SUPPORT_SHAPE;
        }
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            if (context instanceof EntityCollisionContext c) {
                var e = c.getEntity();
                if (e instanceof SprayParticleEntity) {
                    return Shapes.block();
                }
            }
            return Shapes.empty();
        } else {
            return state.getValue(FACING).getAxis() == Direction.Axis.Z ? Z_COLLISION_SHAPE : X_COLLISION_SHAPE;
        }
    }

    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_OCCLUSION_SHAPE_LOW : Z_OCCLUSION_SHAPE_LOW;
    }

    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        switch (pathComputationType) {
            case LAND, AIR -> {
                return state.getValue(OPEN);
            }
            default -> {
                return false;
            }
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        boolean bl = level.hasNeighborSignal(blockPos);
        boolean crouch = context.getPlayer().isCrouching();

        Direction direction = context.getHorizontalDirection();
        Direction clockDir = direction.getClockWise();
        Direction cClockDir = direction.getCounterClockWise();

        boolean above = level.getBlockState(blockPos.above()).is(this);
        boolean below = level.getBlockState(blockPos.below()).is(this);
        boolean adjacent = level.getBlockState(blockPos.relative(clockDir)).is(this);
        boolean adjacent2 = level.getBlockState(blockPos.relative(cClockDir)).is(this);
        boolean wide = false;
        boolean sideDirBl = false;

        Direction sideDir = null;
        if (adjacent ^ adjacent2) {
            if (adjacent) sideDir = cClockDir;
            if (adjacent2) sideDir = clockDir;
        }

        if (!crouch && (adjacent || adjacent2)) {
            wide = true;
            if (adjacent ^ adjacent2) {
                sideDirBl = true;
            }
        }

        BlockState placementState = this.stateDefinition.getOwner().defaultBlockState()
                .setValue(FACING, direction)
                .setValue(WIDE, wide)
                .setValue(SIDE_DIR, sideDirBl ? sideDir : direction)
                .setValue(OPEN, bl)
                .setValue(POWERED, bl)
                .setValue(TOP, !above)
                .setValue(BOTTOM, !below);

        return placementState;
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(OPEN)) {
            state = state.setValue(OPEN, false);
            level.setBlock(pos, state, 10);
        } else {
            Direction direction = player.getDirection();
            if (state.getValue(FACING) == direction.getOpposite()) {
                state = state.setValue(FACING, direction);
            }

            state = state.setValue(OPEN, true);
            level.setBlock(pos, state, 10);
        }

        boolean bl = state.getValue(OPEN);
        level.playSound(player, pos, bl ? SoundEvents.COPPER_TRAPDOOR_OPEN : SoundEvents.COPPER_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        level.gameEvent(player, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && !(Boolean)state.getValue(POWERED)) {
            boolean bl = state.getValue(OPEN);
            level.setBlockAndUpdate(pos, state.setValue(OPEN, !bl));
            level.playSound(null, pos, bl ? SoundEvents.COPPER_TRAPDOOR_CLOSE : SoundEvents.COPPER_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            level.gameEvent(bl ? GameEvent.BLOCK_CLOSE : GameEvent.BLOCK_OPEN, pos, GameEvent.Context.of(state));
        }

        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            boolean bl = level.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != bl) {
                level.setBlock(pos, state.setValue(POWERED, bl).setValue(OPEN, bl), 2);
                if (state.getValue(OPEN) != bl) {
                    level.playSound(null, pos, bl ? SoundEvents.COPPER_TRAPDOOR_OPEN : SoundEvents.COPPER_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                    level.gameEvent(null, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                }
            }

        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, POWERED, TOP, BOTTOM, WIDE, SIDE_DIR, VARNISHED);
    }

    static {
        OPEN = BlockStateProperties.OPEN;
        POWERED = BlockStateProperties.POWERED;
        TOP = BooleanProperty.create("top");
        BOTTOM = BlockStateProperties.BOTTOM;
        WIDE = BooleanProperty.create("wide");
        SIDE_DIR = DirectionProperty.create("side_dir", Direction.Plane.HORIZONTAL);
        Z_SHAPE_LOW = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
        X_SHAPE_LOW = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
        Z_COLLISION_SHAPE = Block.box(0.0, 0.0, 6.0, 16.0, 24.0, 10.0);
        X_COLLISION_SHAPE = Block.box(6.0, 0.0, 0.0, 10.0, 24.0, 16.0);
        Z_SUPPORT_SHAPE = Block.box(0.0, 5.0, 6.0, 16.0, 24.0, 10.0);
        X_SUPPORT_SHAPE = Block.box(6.0, 5.0, 0.0, 10.0, 24.0, 16.0);
        Z_OCCLUSION_SHAPE_LOW = Shapes.or(Block.box(0.0, 2.0, 7.0, 2.0, 16.0, 9.0), Block.box(14.0, 2.0, 7.0, 16.0, 16.0, 9.0));
        X_OCCLUSION_SHAPE_LOW = Shapes.or(Block.box(7.0, 2.0, 0.0, 9.0, 16.0, 2.0), Block.box(7.0, 2.0, 14.0, 9.0, 16.0, 16.0));
    }
}
