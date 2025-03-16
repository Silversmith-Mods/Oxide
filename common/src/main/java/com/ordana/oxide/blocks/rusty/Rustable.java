package com.ordana.oxide.blocks.rusty;

import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.ordana.oxide.reg.ModBlocks;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface Rustable extends ChangeOverTimeBlock<Rustable.RustLevel> {
    Supplier<Map<Block, Block>> RUST_LEVEL_INCREASES = Suppliers.memoize(() -> ImmutableMap.<Block, Block>builder()
            .put(ModBlocks.PLATE_IRON.get(), ModBlocks.WEATHERED_PLATE_IRON.get())
            .put(ModBlocks.WEATHERED_PLATE_IRON.get(), ModBlocks.RUSTED_PLATE_IRON.get())
            .put(ModBlocks.PLATE_IRON_SLAB.get(), ModBlocks.WEATHERED_PLATE_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_PLATE_IRON_SLAB.get(), ModBlocks.RUSTED_PLATE_IRON_SLAB.get())
            .put(ModBlocks.PLATE_IRON_STAIRS.get(), ModBlocks.WEATHERED_PLATE_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_PLATE_IRON_STAIRS.get(), ModBlocks.RUSTED_PLATE_IRON_STAIRS.get())
            .put(ModBlocks.CUT_IRON.get(),ModBlocks.WEATHERED_CUT_IRON.get())
            .put(ModBlocks.WEATHERED_CUT_IRON.get(), ModBlocks.RUSTED_CUT_IRON.get())
            .put(ModBlocks.CUT_IRON_SLAB.get(), ModBlocks.WEATHERED_CUT_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_CUT_IRON_SLAB.get(), ModBlocks.RUSTED_CUT_IRON_SLAB.get())
            .put(ModBlocks.CUT_IRON_STAIRS.get(), ModBlocks.WEATHERED_CUT_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_CUT_IRON_STAIRS.get(), ModBlocks.RUSTED_CUT_IRON_STAIRS.get())

            .put(ModBlocks.IRON_SCAFFOLD.get(),ModBlocks.WEATHERED_IRON_SCAFFOLD.get())
            .put(ModBlocks.WEATHERED_IRON_SCAFFOLD.get(), ModBlocks.RUSTED_IRON_SCAFFOLD.get())
            .put(ModBlocks.IRON_SCAFFOLD_SLAB.get(), ModBlocks.WEATHERED_IRON_SCAFFOLD_SLAB.get())
            .put(ModBlocks.WEATHERED_IRON_SCAFFOLD_SLAB.get(), ModBlocks.RUSTED_IRON_SCAFFOLD_SLAB.get())
            .put(ModBlocks.IRON_SCAFFOLD_STAIRS.get(), ModBlocks.WEATHERED_IRON_SCAFFOLD_STAIRS.get())
            .put(ModBlocks.WEATHERED_IRON_SCAFFOLD_STAIRS.get(), ModBlocks.RUSTED_IRON_SCAFFOLD_STAIRS.get())

            .put(ModBlocks.RED_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_RED_CORRUGATED_IRON.get())
            .put(ModBlocks.ORANGE_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON.get())
            .put(ModBlocks.YELLOW_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON.get())
            .put(ModBlocks.LIME_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_LIME_CORRUGATED_IRON.get())
            .put(ModBlocks.GREEN_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON.get())
            .put(ModBlocks.CYAN_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON.get())
            .put(ModBlocks.BLUE_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON.get())
            .put(ModBlocks.LIGHT_BLUE_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON.get())
            .put(ModBlocks.PURPLE_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON.get())
            .put(ModBlocks.MAGENTA_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON.get())
            .put(ModBlocks.PINK_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_PINK_CORRUGATED_IRON.get())
            .put(ModBlocks.WHITE_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON.get())
            .put(ModBlocks.LIGHT_GRAY_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON.get())
            .put(ModBlocks.GRAY_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON.get())
            .put(ModBlocks.BLACK_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON.get())
            .put(ModBlocks.BROWN_CORRUGATED_IRON.get(), ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON.get())
            
            .put(ModBlocks.WEATHERED_RED_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_LIME_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_PINK_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON.get(), ModBlocks.RUSTED_CORRUGATED_IRON.get())

            .put(ModBlocks.RED_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_RED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.ORANGE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.YELLOW_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.LIME_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.GREEN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.CYAN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.PURPLE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.MAGENTA_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.PINK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WHITE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.BLACK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.BROWN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_STAIRS.get())

            .put(ModBlocks.WEATHERED_RED_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get())

            .put(ModBlocks.RED_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_RED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.ORANGE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.YELLOW_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.LIME_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.GREEN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.CYAN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.PURPLE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.MAGENTA_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.PINK_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WHITE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.BLACK_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.BROWN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_SLAB.get())

            .put(ModBlocks.WEATHERED_RED_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_SLAB.get(), ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get())

            .put(ModBlocks.WROUGHT_IRON_FENCE.get(), ModBlocks.WEATHERED_WROUGHT_IRON_FENCE.get())
            .put(ModBlocks.WEATHERED_WROUGHT_IRON_FENCE.get(), ModBlocks.RUSTED_WROUGHT_IRON_FENCE.get())

            .put(ModBlocks.WROUGHT_IRON_FENCE_GATE.get(), ModBlocks.WEATHERED_WROUGHT_IRON_FENCE_GATE.get())
            .put(ModBlocks.WEATHERED_WROUGHT_IRON_FENCE_GATE.get(), ModBlocks.RUSTED_WROUGHT_IRON_FENCE_GATE.get())

            .put(ModBlocks.IRON_PILLAR.get(), ModBlocks.WEATHERED_IRON_PILLAR.get())
            .put(ModBlocks.WEATHERED_IRON_PILLAR.get(), ModBlocks.RUSTED_IRON_PILLAR.get())

            .put(ModBlocks.HEAVY_IRON_TRAPDOOR.get(), ModBlocks.WEATHERED_HEAVY_IRON_TRAPDOOR.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_TRAPDOOR.get(), ModBlocks.RUSTED_HEAVY_IRON_TRAPDOOR.get())
            
            .put(ModBlocks.HEAVY_IRON_CHAIN.get(), ModBlocks.WEATHERED_HEAVY_IRON_CHAIN.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_CHAIN.get(), ModBlocks.RUSTED_HEAVY_IRON_CHAIN.get())

            .put(ModBlocks.HEAVY_IRON_DOOR.get(), ModBlocks.WEATHERED_HEAVY_IRON_DOOR.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_DOOR.get(), ModBlocks.RUSTED_HEAVY_IRON_DOOR.get())

            .put(ModBlocks.HEAVY_IRON_BARS.get(), ModBlocks.WEATHERED_HEAVY_IRON_BARS.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_BARS.get(), ModBlocks.RUSTED_HEAVY_IRON_BARS.get())

            .build());


    static Optional<Block> getIncreasedRustBlock(Block block) {
        return Optional.ofNullable(RUST_LEVEL_INCREASES.get().get(block));
    }

    @Override
    default Optional<BlockState> getNext(BlockState state) {
        return Rustable.getIncreasedRustBlock(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }


    Supplier<BiMap<Block, Block>> WAXING = Suppliers.memoize(() -> ImmutableBiMap.<Block, Block>builder()
            .put(ModBlocks.PLATE_IRON.get(), ModBlocks.WAXED_PLATE_IRON.get())
            .put(ModBlocks.WEATHERED_PLATE_IRON.get(), ModBlocks.WAXED_WEATHERED_PLATE_IRON.get())
            .put(ModBlocks.RUSTED_PLATE_IRON.get(), ModBlocks.WAXED_RUSTED_PLATE_IRON.get())
            .put(ModBlocks.PLATE_IRON_STAIRS.get(), ModBlocks.WAXED_PLATE_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_PLATE_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_PLATE_IRON_STAIRS.get())
            .put(ModBlocks.RUSTED_PLATE_IRON_STAIRS.get(), ModBlocks.WAXED_RUSTED_PLATE_IRON_STAIRS.get())
            .put(ModBlocks.PLATE_IRON_SLAB.get(), ModBlocks.WAXED_PLATE_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_PLATE_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_PLATE_IRON_SLAB.get())
            .put(ModBlocks.RUSTED_PLATE_IRON_SLAB.get(), ModBlocks.WAXED_RUSTED_PLATE_IRON_SLAB.get())

            .put(ModBlocks.CUT_IRON.get(), ModBlocks.WAXED_CUT_IRON.get())
            .put(ModBlocks.WEATHERED_CUT_IRON.get(), ModBlocks.WAXED_WEATHERED_CUT_IRON.get())
            .put(ModBlocks.RUSTED_CUT_IRON.get(), ModBlocks.WAXED_RUSTED_CUT_IRON.get())
            .put(ModBlocks.CUT_IRON_STAIRS.get(), ModBlocks.WAXED_CUT_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_CUT_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_CUT_IRON_STAIRS.get())
            .put(ModBlocks.RUSTED_CUT_IRON_STAIRS.get(), ModBlocks.WAXED_RUSTED_CUT_IRON_STAIRS.get())
            .put(ModBlocks.CUT_IRON_SLAB.get(), ModBlocks.WAXED_CUT_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_CUT_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_CUT_IRON_SLAB.get())
            .put(ModBlocks.RUSTED_CUT_IRON_SLAB.get(), ModBlocks.WAXED_RUSTED_CUT_IRON_SLAB.get())

            .put(ModBlocks.IRON_SCAFFOLD.get(), ModBlocks.WAXED_IRON_SCAFFOLD.get())
            .put(ModBlocks.WEATHERED_IRON_SCAFFOLD.get(), ModBlocks.WAXED_WEATHERED_IRON_SCAFFOLD.get())
            .put(ModBlocks.RUSTED_IRON_SCAFFOLD.get(), ModBlocks.WAXED_RUSTED_IRON_SCAFFOLD.get())

            .put(ModBlocks.IRON_SCAFFOLD_SLAB.get(), ModBlocks.WAXED_IRON_SCAFFOLD_SLAB.get())
            .put(ModBlocks.WEATHERED_IRON_SCAFFOLD_SLAB.get(), ModBlocks.WAXED_WEATHERED_IRON_SCAFFOLD_SLAB.get())
            .put(ModBlocks.RUSTED_IRON_SCAFFOLD_SLAB.get(), ModBlocks.WAXED_RUSTED_IRON_SCAFFOLD_SLAB.get())

            .put(ModBlocks.IRON_SCAFFOLD_STAIRS.get(), ModBlocks.WAXED_IRON_SCAFFOLD_STAIRS.get())
            .put(ModBlocks.WEATHERED_IRON_SCAFFOLD_STAIRS.get(), ModBlocks.WAXED_WEATHERED_IRON_SCAFFOLD_STAIRS.get())
            .put(ModBlocks.RUSTED_IRON_SCAFFOLD_STAIRS.get(), ModBlocks.WAXED_RUSTED_IRON_SCAFFOLD_STAIRS.get())

            .put(ModBlocks.RED_CORRUGATED_IRON.get(), ModBlocks.WAXED_RED_CORRUGATED_IRON.get())
            .put(ModBlocks.ORANGE_CORRUGATED_IRON.get(), ModBlocks.WAXED_ORANGE_CORRUGATED_IRON.get())
            .put(ModBlocks.YELLOW_CORRUGATED_IRON.get(), ModBlocks.WAXED_YELLOW_CORRUGATED_IRON.get())
            .put(ModBlocks.LIME_CORRUGATED_IRON.get(), ModBlocks.WAXED_LIME_CORRUGATED_IRON.get())
            .put(ModBlocks.GREEN_CORRUGATED_IRON.get(), ModBlocks.WAXED_GREEN_CORRUGATED_IRON.get())
            .put(ModBlocks.CYAN_CORRUGATED_IRON.get(), ModBlocks.WAXED_CYAN_CORRUGATED_IRON.get())
            .put(ModBlocks.BLUE_CORRUGATED_IRON.get(), ModBlocks.WAXED_BLUE_CORRUGATED_IRON.get())
            .put(ModBlocks.LIGHT_BLUE_CORRUGATED_IRON.get(), ModBlocks.WAXED_LIGHT_BLUE_CORRUGATED_IRON.get())
            .put(ModBlocks.PURPLE_CORRUGATED_IRON.get(), ModBlocks.WAXED_PURPLE_CORRUGATED_IRON.get())
            .put(ModBlocks.MAGENTA_CORRUGATED_IRON.get(), ModBlocks.WAXED_MAGENTA_CORRUGATED_IRON.get())
            .put(ModBlocks.PINK_CORRUGATED_IRON.get(), ModBlocks.WAXED_PINK_CORRUGATED_IRON.get())
            .put(ModBlocks.WHITE_CORRUGATED_IRON.get(), ModBlocks.WAXED_WHITE_CORRUGATED_IRON.get())
            .put(ModBlocks.LIGHT_GRAY_CORRUGATED_IRON.get(), ModBlocks.WAXED_LIGHT_GRAY_CORRUGATED_IRON.get())
            .put(ModBlocks.GRAY_CORRUGATED_IRON.get(), ModBlocks.WAXED_GRAY_CORRUGATED_IRON.get())
            .put(ModBlocks.BLACK_CORRUGATED_IRON.get(), ModBlocks.WAXED_BLACK_CORRUGATED_IRON.get())
            .put(ModBlocks.BROWN_CORRUGATED_IRON.get(), ModBlocks.WAXED_BROWN_CORRUGATED_IRON.get())

            .put(ModBlocks.WEATHERED_RED_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_RED_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_ORANGE_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_YELLOW_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_LIME_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_LIME_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_GREEN_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_CYAN_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_BLUE_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_LIGHT_BLUE_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_PURPLE_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_MAGENTA_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_PINK_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_PINK_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_WHITE_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_LIGHT_GRAY_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_GRAY_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_BLACK_CORRUGATED_IRON.get())
            .put(ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON.get(), ModBlocks.WAXED_WEATHERED_BROWN_CORRUGATED_IRON.get())
            
            .put(ModBlocks.RUSTED_CORRUGATED_IRON.get(), ModBlocks.WAXED_RUSTED_CORRUGATED_IRON.get())

            .put(ModBlocks.RED_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_RED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.ORANGE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_ORANGE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.YELLOW_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_YELLOW_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.LIME_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_LIME_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.GREEN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_GREEN_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.CYAN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_CYAN_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_BLUE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.PURPLE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_PURPLE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.MAGENTA_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_MAGENTA_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.PINK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_PINK_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WHITE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WHITE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_GRAY_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.BLACK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_BLACK_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.BROWN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_BROWN_CORRUGATED_IRON_STAIRS.get())

            .put(ModBlocks.WEATHERED_RED_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_RED_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_LIME_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_GREEN_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_CYAN_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_BLUE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_PINK_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_WHITE_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_GRAY_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_BLACK_CORRUGATED_IRON_STAIRS.get())
            .put(ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_WEATHERED_BROWN_CORRUGATED_IRON_STAIRS.get())

            .put(ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS.get(), ModBlocks.WAXED_RUSTED_CORRUGATED_IRON_STAIRS.get())

            .put(ModBlocks.RED_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_RED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.ORANGE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_ORANGE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.YELLOW_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_YELLOW_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.LIME_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_LIME_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.GREEN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_GREEN_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.CYAN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_CYAN_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_BLUE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_LIGHT_BLUE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.PURPLE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_PURPLE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.MAGENTA_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_MAGENTA_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.PINK_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_PINK_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WHITE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WHITE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_LIGHT_GRAY_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_GRAY_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.BLACK_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_BLACK_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.BROWN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_BROWN_CORRUGATED_IRON_SLAB.get())

            .put(ModBlocks.WEATHERED_RED_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_RED_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_ORANGE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_YELLOW_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_LIME_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_GREEN_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_CYAN_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_BLUE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_PURPLE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_PINK_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_WHITE_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_GRAY_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_BLACK_CORRUGATED_IRON_SLAB.get())
            .put(ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_WEATHERED_BROWN_CORRUGATED_IRON_SLAB.get())

            .put(ModBlocks.RUSTED_CORRUGATED_IRON_SLAB.get(), ModBlocks.WAXED_RUSTED_CORRUGATED_IRON_SLAB.get())

            .put(ModBlocks.WROUGHT_IRON_FENCE.get(), ModBlocks.WAXED_WROUGHT_IRON_FENCE.get())
            .put(ModBlocks.WEATHERED_WROUGHT_IRON_FENCE.get(), ModBlocks.WAXED_WEATHERED_WROUGHT_IRON_FENCE.get())
            .put(ModBlocks.RUSTED_WROUGHT_IRON_FENCE.get(), ModBlocks.WAXED_RUSTED_WROUGHT_IRON_FENCE.get())

            .put(ModBlocks.WROUGHT_IRON_FENCE_GATE.get(), ModBlocks.WAXED_WROUGHT_IRON_FENCE_GATE.get())
            .put(ModBlocks.WEATHERED_WROUGHT_IRON_FENCE_GATE.get(), ModBlocks.WAXED_WEATHERED_WROUGHT_IRON_FENCE_GATE.get())
            .put(ModBlocks.RUSTED_WROUGHT_IRON_FENCE_GATE.get(), ModBlocks.WAXED_RUSTED_WROUGHT_IRON_FENCE_GATE.get())

            .put(ModBlocks.IRON_PILLAR.get(), ModBlocks.WAXED_IRON_PILLAR.get())
            .put(ModBlocks.WEATHERED_IRON_PILLAR.get(), ModBlocks.WAXED_WEATHERED_IRON_PILLAR.get())
            .put(ModBlocks.RUSTED_IRON_PILLAR.get(), ModBlocks.WAXED_RUSTED_IRON_PILLAR.get())

            .put(ModBlocks.HEAVY_IRON_TRAPDOOR.get(), ModBlocks.WAXED_HEAVY_IRON_TRAPDOOR.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_TRAPDOOR.get(), ModBlocks.WAXED_WEATHERED_HEAVY_IRON_TRAPDOOR.get())
            .put(ModBlocks.RUSTED_HEAVY_IRON_TRAPDOOR.get(), ModBlocks.WAXED_RUSTED_HEAVY_IRON_TRAPDOOR.get())

            .put(ModBlocks.HEAVY_IRON_CHAIN.get(), ModBlocks.WAXED_HEAVY_IRON_CHAIN.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_CHAIN.get(), ModBlocks.WAXED_WEATHERED_HEAVY_IRON_CHAIN.get())
            .put(ModBlocks.RUSTED_HEAVY_IRON_CHAIN.get(), ModBlocks.WAXED_RUSTED_HEAVY_IRON_CHAIN.get())

            .put(ModBlocks.HEAVY_IRON_DOOR.get(), ModBlocks.WAXED_HEAVY_IRON_DOOR.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_DOOR.get(), ModBlocks.WAXED_WEATHERED_HEAVY_IRON_DOOR.get())
            .put(ModBlocks.RUSTED_HEAVY_IRON_DOOR.get(), ModBlocks.WAXED_RUSTED_HEAVY_IRON_DOOR.get())

            .put(ModBlocks.HEAVY_IRON_BARS.get(), ModBlocks.WAXED_HEAVY_IRON_BARS.get())
            .put(ModBlocks.WEATHERED_HEAVY_IRON_BARS.get(), ModBlocks.WAXED_WEATHERED_HEAVY_IRON_BARS.get())
            .put(ModBlocks.RUSTED_HEAVY_IRON_BARS.get(), ModBlocks.WAXED_RUSTED_HEAVY_IRON_BARS.get())

            .build());

    Supplier<BiMap<Block, Block>> UNWAXED = Suppliers.memoize(() -> WAXING.get().inverse());

    static Optional<Block> getWaxed(Block block) {
        return Optional.ofNullable(WAXING.get().get(block));
    }

    default Optional<BlockState> getWaxed(BlockState state) {
        return Rustable.getWaxed(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }
    static Optional<Block> getUnWaxed(Block block) {
        return Optional.ofNullable(UNWAXED.get().get(block));
    }

    default Optional<BlockState> getUnWaxed(BlockState state) {
        return Rustable.getUnWaxed(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }

    default ItemInteractionResult use(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        var item = stack.getItem();
        var age = getAge();

        if (item instanceof AxeItem && (age == RustLevel.WAXED)) {
            var waxed = this.getUnWaxed(state);

            if (waxed.isPresent()) {
                level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.WAX_OFF, UniformInt.of(3, 5));
                if (player instanceof ServerPlayer serverPlayer) {
                    stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
                    player.awardStat(Stats.ITEM_USED.get(item));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                    level.setBlockAndUpdate(pos, waxed.get());
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (item == Items.HONEYCOMB && (age != RustLevel.WAXED)) {
            var waxed = this.getWaxed(state);

            if (waxed.isPresent()) {
                level.playSound(player, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtils.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
                if (player instanceof ServerPlayer serverPlayer) {
                    player.awardStat(Stats.ITEM_USED.get(item));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                    level.setBlockAndUpdate(pos, waxed.get());
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        if (item == Items.WET_SPONGE && (age == RustLevel.CLEAN || age == RustLevel.WEATHERED)) {
            var rusted = this.getNext(state);

            if (rusted.isPresent()) {
                level.playSound(player, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
                ParticleUtil.spawnParticlesOnBlockFaces(level, pos, ParticleTypes.SPLASH, UniformInt.of(3, 5), -0.05f, 0.05f, false);
                if (player instanceof ServerPlayer serverPlayer) {
                    player.awardStat(Stats.ITEM_USED.get(item));
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, stack);
                    level.setBlockAndUpdate(pos, rusted.get());
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    default float getChanceModifier() {
        if (this.getAge() == RustLevel.CLEAN) {
            return 0.75f;
        }
        return 1.0f;
    }

    enum RustLevel {
        WAXED,
        CLEAN,
        WEATHERED,
        RUSTED;
    }

    default void applyChangeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
        int airCheck = 0;
        int wetness = 0;
        if (getAge() == RustLevel.WAXED || getAge() == RustLevel.RUSTED) return;
        for (Direction dir : Direction.values()) {
            var dirPos = pos.relative(dir);
            var dirState = level.getBlockState(dirPos);
            if (!dirState.isSuffocating(level, dirPos)) airCheck += 1;

            if (level.isRainingAt(dirPos)) {
                if (dir == Direction.UP) wetness += 30;
                else if (dir != Direction.DOWN) wetness += 1;
                //wetness += 1;
            }
            //if (level.getFluidState(dirPos).is(FluidTags.WATER)) wetness += 20;
            if (dirState.is(Blocks.BUBBLE_COLUMN)) wetness += 50;
            if (dirState.getBlock() instanceof Rustable rusty) {
                if (rusty.getAge() == RustLevel.RUSTED) wetness += 10;
                if (rusty.getAge() == RustLevel.WEATHERED) wetness += 10;
            }
        }
        if (airCheck == 0) return;

        for (int i = 0; i < wetness; i++) {
            if (randomSource.nextInt(50) == 1) {
                this.getNext(state).ifPresent(s -> level.setBlockAndUpdate(pos, s));
                return;
            }
        }
    }

    default void tryWeather(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        applyChangeOverTime(state, level, pos, random);
    }

    static BlockBehaviour.Properties setRandomTicking(BlockBehaviour.Properties properties, RustLevel rustLevel) {
        properties.isRandomlyTicking = rustLevel == RustLevel.WEATHERED || rustLevel == RustLevel.CLEAN;
        return properties;
    }
}
