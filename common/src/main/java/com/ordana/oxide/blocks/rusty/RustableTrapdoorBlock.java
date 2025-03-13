package com.ordana.oxide.blocks.rusty;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

//affected by rust but cannot rust itself (waxed)
public class RustableTrapdoorBlock extends TrapDoorBlock implements Rustable {
    private final Rustable.RustLevel rustLevel;

    public RustableTrapdoorBlock(Rustable.RustLevel rustLevel, Properties properties) {
        super(BlockSetType.IRON, properties);
        this.rustLevel = rustLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        this.tryWeather(state, serverLevel, pos, random);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        state = state.cycle(OPEN);
        level.setBlock(pos, state, 2);
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        playSound(player, level, pos, state.getValue(OPEN));
        return InteractionResult.sidedSuccess(level.isClientSide);

    }

    public Rustable.RustLevel getAge() {
        return this.rustLevel;
    }

    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return this.use(stack, state, level, pos, player, hand, hitResult);
    }
}
