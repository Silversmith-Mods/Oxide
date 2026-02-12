package com.ordana.oxide.blocks.rusty;

import com.ordana.oxide.entities.RustyNailEntity;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class RustableDoorBlock extends DoorBlock implements Rustable {
    public static final BooleanProperty VARNISHED = ModBlockProperties.VARNISHED;

    protected final Rustable.RustLevel rustLevel;

    public RustableDoorBlock(Rustable.RustLevel rustLevel, Properties properties) {
        super(BlockSetType.IRON, Rustable.setRandomTicking(properties, rustLevel));
        this.rustLevel = rustLevel;

        this.registerDefaultState(this.defaultBlockState().setValue(VARNISHED, false));
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        if (rustLevel == RustLevel.RUSTED && !level.isClientSide && level.random.nextFloat() <= 0.1f && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            RustyNailEntity nail = new RustyNailEntity(level, player, ModEntities.RUSTY_NAIL.get());
            nail.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            level.addFreshEntity(nail);
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    protected void playSound(@Nullable Player player, Level level, BlockPos pos, boolean isOpened) {
        level.playSound(player, pos, isOpened ? this.type.trapdoorOpen() : this.type.trapdoorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
        level.gameEvent(player, isOpened ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf doubleBlockHalf = state.getValue(HALF);
        if ((direction == Direction.UP && doubleBlockHalf == DoubleBlockHalf.LOWER) ||
                (direction == Direction.DOWN && doubleBlockHalf == DoubleBlockHalf.UPPER)) {
            if (neighborState.getBlock() instanceof RustableDoorBlock) {
                state = neighborState.getBlock().withPropertiesOf(state);
            }
        }
        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return neighborState.is(state.getBlock()) && neighborState.getValue(HALF) != doubleBlockHalf ? state.setValue(FACING, neighborState.getValue(FACING)).setValue(OPEN, neighborState.getValue(OPEN)).setValue(HINGE, neighborState.getValue(HINGE)).setValue(POWERED, neighborState.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
        } else {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!state.getValue(VARNISHED)) this.tryWeather(state, serverLevel, pos, random);
    }

    public Rustable.RustLevel getAge() {
        return rustLevel;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING, OPEN, HINGE, POWERED, VARNISHED);
    }
}