package com.ordana.oxide.blocks;

import com.ordana.oxide.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
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
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM));
    }

    public boolean useShapeForLightOcclusion(BlockState state) {
        return state.getValue(TYPE) != SlabType.DOUBLE;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!serverLevel.getBlockState(pos.below()).is(this)) serverLevel.setBlockAndUpdate(pos, state.getValue(TYPE) == SlabType.DOUBLE ? ModBlocks.CEMENT.get().defaultBlockState() : ModBlocks.CEMENT_SLAB.get().defaultBlockState());
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
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE);
        } else {
            return this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM);
        }
    }

    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        ItemStack itemStack = useContext.getItemInHand();
        SlabType slabType = state.getValue(TYPE);
        if (slabType != SlabType.DOUBLE && itemStack.is(this.asItem())) {
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
        if (level.isClientSide()) return Blocks.AIR.defaultBlockState();
        if (state.getValue(TYPE) == SlabType.DOUBLE) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.getRandom())) {
                var dirState = level.getBlockState(pos.relative(dir));
                if (dirState.canBeReplaced()) {
                    level.setBlock(pos.relative(dir), state.setValue(TYPE, SlabType.BOTTOM), 3);
                    level.setBlock(pos, state.setValue(TYPE, SlabType.BOTTOM), 3);
                    break;
                }
            }
        }
        var belowPos = (level.getBlockState(pos.below()));

        if (belowPos.canBeReplaced()) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            level.setBlock(pos.below(), state, 3);
        }
        if (belowPos.is(ModBlocks.WET_CEMENT.get())) {
            if (belowPos.getValue(TYPE) == SlabType.BOTTOM) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                level.setBlock(pos.below(), state.setValue(TYPE, SlabType.DOUBLE), 3);
            }
        }


        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    static {
        TYPE = BlockStateProperties.SLAB_TYPE;
        BOTTOM_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
        BOTTOM_COLLISION_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 4.0, 12.0);
        DOUBLE_COLLISION_AABB = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    }
}