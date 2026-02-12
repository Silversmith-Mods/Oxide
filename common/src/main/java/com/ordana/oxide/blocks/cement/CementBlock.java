package com.ordana.oxide.blocks.cement;

import com.ordana.oxide.configs.CommonConfigs;
import com.ordana.oxide.entities.FallingCementEntity;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

public class CementBlock extends WeatherableBlock implements Fallable, WeatherableCement {
    public static final IntegerProperty OVERHANG;
    private final int maxOverhang;

    public CementBlock(int maxOverhang, Properties properties) {
        super(properties);
        this.maxOverhang = maxOverhang;
        this.registerDefaultState(this.defaultBlockState().setValue(OVERHANG, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OVERHANG);
    }

    public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.CEMENT_BUCKET.get());
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        level.playSound(null, pos, SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        ParticleUtil.spawnParticlesOnBlockFaces(level, pos, new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.CRACKED_CEMENT.get().defaultBlockState()), UniformInt.of(3, 5), -0.05f, 0.05f, false);
        boolean bl = state.is(ModBlocks.CEMENT.get()) || state.is(ModBlocks.CRACKED_CEMENT.get());
        if (level.random.nextFloat() < CommonConfigs.General.FALLING_CEMENT_CRACK_CHANCE.get()) level.setBlockAndUpdate(pos, bl ? ModBlocks.CRACKED_CEMENT.get().defaultBlockState() : ModBlocks.CRACKED_WEATHERED_CEMENT.get().defaultBlockState());
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
        Level level = context.getLevel();
        return this.defaultBlockState().setValue(OVERHANG, this.getOverhang(level, blockPos));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OVERHANG) == maxOverhang) {
            FallingCementEntity.fall(level, pos, state.setValue(OVERHANG, 0));
        }
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

    static {
        OVERHANG = ModBlockProperties.OVERHANG;
    }
}
