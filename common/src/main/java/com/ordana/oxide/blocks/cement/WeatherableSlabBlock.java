package com.ordana.oxide.blocks.cement;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class WeatherableSlabBlock extends SlabBlock implements WeatherableCement {
    public WeatherableSlabBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (state.getValue(WATERLOGGED) && state.getValue(TYPE) == SlabType.BOTTOM) return;
        tryWeather(state, serverLevel, pos, random);
    }
}
