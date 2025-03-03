package com.ordana.oxide.blocks.rusty;

import com.ordana.oxide.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RustableScaffoldBlock extends Block implements Rustable {

    private final Rustable.RustLevel rustLevel;

    public RustableScaffoldBlock(Rustable.RustLevel rustLevel, BlockBehaviour.Properties settings) {
        super(Rustable.setRandomTicking(settings, rustLevel));
        this.rustLevel = rustLevel;
    }

    @Override
    public Rustable.RustLevel getAge() {
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
}
