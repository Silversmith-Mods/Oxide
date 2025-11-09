package com.ordana.oxide.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class PaintBlock extends MultifaceBlock {
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final MapCodec<PaintBlock> CODEC = simpleCodec(PaintBlock::new);

    public PaintBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(defaultBlockState().setValue(VARNISHED, false));
    }

    public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.VARNISH_SPRAYER.get());
    }

    @Override
    protected MapCodec<? extends MultifaceBlock> codec()  {
        return CODEC;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (serverLevel.isRainingAt(pos) && !state.getValue(VARNISHED)) serverLevel.destroyBlock(pos, false);
    }

    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return null;
    }


    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(VARNISHED);
    }
}
