package com.ordana.oxide.blocks.rusty;

import com.ordana.oxide.entities.RustyNailEntity;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModEntities;
import com.ordana.oxide.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RustableScaffoldSlabBlock extends SlabBlock implements Rustable {
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;

    private final RustLevel rustLevel;

    public RustableScaffoldSlabBlock(RustLevel rustLevel, Properties settings) {
        super(Rustable.setRandomTicking(settings, rustLevel));
        this.rustLevel = rustLevel;

        this.registerDefaultState(this.defaultBlockState().setValue(VARNISHED, false));
    }

    @Override
    public RustLevel getAge() {
        return this.rustLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!state.getValue(VARNISHED)) this.tryWeather(state, serverLevel, pos, random);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (rustLevel == RustLevel.RUSTED && !level.isClientSide && level.random.nextFloat() <= 0.1f && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            RustyNailEntity nail = new RustyNailEntity(level, player, ModEntities.RUSTY_NAIL.get());
            nail.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            level.addFreshEntity(nail);
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        boolean bl = adjacentState.is(ModTags.SCAFFOLDS) && direction.getAxis() == Direction.Axis.Y;
        if (adjacentState.is(BlockTags.STAIRS)) {
            if (adjacentState.getValue(BlockStateProperties.HALF) == Half.TOP) bl = false;
        }
        if (adjacentState.is(BlockTags.SLABS)) {
            if (direction == Direction.DOWN && adjacentState.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.BOTTOM) bl = false;
            if (direction == Direction.UP && adjacentState.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.TOP) bl = false;
            if (adjacentState.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) bl = true;
        }
        return (bl) || super.skipRendering(state, adjacentState, direction);
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return this.use(stack, state, level, pos, player, hand, hitResult);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, VARNISHED);
    }
}
