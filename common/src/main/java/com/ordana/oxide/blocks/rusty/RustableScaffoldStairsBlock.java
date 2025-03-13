package com.ordana.oxide.blocks.rusty;

import com.ordana.oxide.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class RustableScaffoldStairsBlock extends ModStairBlock implements Rustable {

    private final RustLevel rustLevel;

    public RustableScaffoldStairsBlock(RustLevel rustLevel, Supplier<Block> baseBlockState, Properties settings) {
        super(baseBlockState, Rustable.setRandomTicking(settings, rustLevel));
        this.rustLevel = rustLevel;
    }

    @Override
    public RustLevel getAge() {
        return this.rustLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        this.tryWeather(state, serverLevel, pos, random);
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
            if (adjacentState.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.TOP) bl = false;
        }
        return (bl) || super.skipRendering(state, adjacentState, direction);
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return this.use(stack, state, level, pos, player, hand, hitResult);
    }
}
