package com.ordana.oxide.blocks.cement;

import com.mojang.serialization.MapCodec;
import com.ordana.oxide.configs.CommonConfigs;
import com.ordana.oxide.entities.FallingCementEntity;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

public class CementSlabBlock extends WeatherableSlabBlock implements Fallable, SimpleWaterloggedBlock, WeatherableCement {
    public static final MapCodec<SlabBlock> CODEC = simpleCodec(SlabBlock::new);
    public static final IntegerProperty OVERHANG = ModBlockProperties.OVERHANG;
    private final int maxOverhang;

    public MapCodec<? extends SlabBlock> codec() {
        return CODEC;
    }

    public CementSlabBlock(int maxOverhang, Properties settings) {
        super(settings);
        this.maxOverhang = maxOverhang;
        this.registerDefaultState(this.defaultBlockState().setValue(OVERHANG, 0).setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, false));
    }

    public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.CEMENT_BUCKET.get());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OVERHANG, TYPE, WATERLOGGED);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        level.playSound(null, pos, SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        boolean bl = state.is(this);
        ParticleUtils.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.CRACKED_CEMENT.get().defaultBlockState()), UniformInt.of(3, 5));
        boolean bl2 = state.is(ModBlocks.CEMENT_SLAB.get());
        var state2 = bl2 ? ModBlocks.CRACKED_CEMENT_SLAB.get() : ModBlocks.CRACKED_WEATHERED_CEMENT_SLAB.get();
        if (level.random.nextFloat() < CommonConfigs.General.FALLING_CEMENT_CRACK_CHANCE.get()) level.setBlockAndUpdate(pos, state2.defaultBlockState().setValue(TYPE, bl ? SlabType.BOTTOM : SlabType.DOUBLE));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = context.getLevel().getBlockState(blockPos);
        Level level = context.getLevel();
        if (blockState.is(this)) {
            return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false).setValue(OVERHANG, this.getOverhang(level, blockPos));
        } else {
            FluidState fluidState = context.getLevel().getFluidState(blockPos);
            BlockState blockState2 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER).setValue(OVERHANG, this.getOverhang(level, blockPos));
            Direction direction = context.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(context.getClickLocation().y - (double) blockPos.getY() > 0.5)) ? blockState2 : (BlockState) blockState2.setValue(TYPE, SlabType.TOP);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OVERHANG) == maxOverhang) {
            FallingCementEntity.fall(level, pos, state.setValue(OVERHANG, 0));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {

        return this.getShape(state, level, pos, context);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        updateOverhang(state, level, pos);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    private void updateOverhang(BlockState state, Level level, BlockPos pos) {
        int supported = getOverhang(level, pos);
        if (supported != state.getValue(OVERHANG)) {
            level.setBlockAndUpdate(pos, state.setValue(OVERHANG, supported));
        }
        if (supported == maxOverhang) {
            level.scheduleTick(pos, state.getBlock(), 1);
        }
    }

    private int getOverhang(Level level, BlockPos pos) {
        var free = FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinBuildHeight();
        if (!free) return 0;

        int overInt = maxOverhang;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (var dir : Direction.Plane.HORIZONTAL.shuffledCopy(level.random)) {

            mutableBlockPos.setWithOffset(pos, dir);
            overInt = Math.min(overInt, getDistanceAt(level.getBlockState(mutableBlockPos)) + 1);
            if (overInt == 0) {
                break;
            }
        }
        return overInt;
    }

    private int getDistanceAt(BlockState neighbor) {
        var i = neighbor.hasProperty(OVERHANG) ? OptionalInt.of(neighbor.getValue(OVERHANG)) : OptionalInt.empty();
        return i.orElse(maxOverhang);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(OVERHANG) == maxOverhang - 1 && random.nextInt(16) == 0 && FallingBlock.isFree(level.getBlockState(pos.below()))) {
            double d = (double) pos.getX() + random.nextDouble();
            double e = (double) pos.getY() - 0.05;
            double f = (double) pos.getZ() + random.nextDouble();
            level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, state), d, e, f, 0.0, 0.0, 0.0);
        }
    }

}