package com.ordana.oxide.blocks.cement;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.ordana.oxide.blocks.rusty.Rustable;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface WeatherableCement {
    Supplier<Map<Block, Block>> WEATHER_INCREASES = Suppliers.memoize(() -> ImmutableMap.<Block, Block>builder()
            .put(ModBlocks.CEMENT.get(), ModBlocks.WEATHERED_CEMENT.get())
            .put(ModBlocks.CEMENT_SLAB.get(), ModBlocks.WEATHERED_CEMENT_SLAB.get())
            .put(ModBlocks.REINFORCED_CEMENT.get(), ModBlocks.WEATHERED_REINFORCED_CEMENT.get())
            .put(ModBlocks.CEMENT_BLOCK.get(), ModBlocks.WEATHERED_CEMENT_BLOCK.get())
            .put(ModBlocks.CEMENT_BLOCK_SLAB.get(), ModBlocks.WEATHERED_CEMENT_BLOCK_SLAB.get())
            .put(ModBlocks.CRACKED_CEMENT.get(), ModBlocks.CRACKED_WEATHERED_CEMENT.get())
            .put(ModBlocks.CRACKED_CEMENT_SLAB.get(),ModBlocks.CRACKED_WEATHERED_CEMENT_SLAB.get())
            .put(ModBlocks.CRACKED_REINFORCED_CEMENT.get(), ModBlocks.CRACKED_WEATHERED_REINFORCED_CEMENT.get())
            .build());


    static Optional<Block> getWeatheredBlock(Block block) {
        return Optional.ofNullable(WEATHER_INCREASES.get().get(block));
    }

    default Optional<BlockState> getNext(BlockState state) {
        return WeatherableCement.getWeatheredBlock(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    default void tryWeather(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.is(ModTags.WEATHERED_CEMENT)) return;
        if (level.getBlockState(pos.above()).is(ModTags.WEATHERED_CEMENT) && level.isRaining()) this.getNext(state).ifPresent(s -> level.setBlockAndUpdate(pos, s));
        if (random.nextFloat() <= 0.2) {
            for (Direction dir : Direction.values()) {
                if (level.isRainingAt(pos.relative(dir))) {
                    if (dir == Direction.DOWN) break;
                    else if (dir == Direction.UP) this.getNext(state).ifPresent(s -> level.setBlockAndUpdate(pos, s));
                    else if (random.nextFloat() <= 0.2) this.getNext(state).ifPresent(s -> level.setBlockAndUpdate(pos, s));
                    return;
                }
            }
        }
    }
}
