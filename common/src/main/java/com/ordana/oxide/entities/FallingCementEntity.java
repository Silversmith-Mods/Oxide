package com.ordana.oxide.entities;

import com.ordana.oxide.blocks.cement.CementBlock;
import com.ordana.oxide.blocks.cement.CementSlabBlock;
import com.ordana.oxide.configs.CommonConfigs;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

public class FallingCementEntity extends ImprovedFallingBlockEntity implements Fallable {

    public FallingCementEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
        this.dropItem = false;
    }

    private FallingCementEntity(Level level, double x, double y, double z, BlockState state) {
        this(EntityType.FALLING_BLOCK, level);
        this.dropItem = false;
        this.blockState = state;
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public static FallingBlockEntity fall(Level level, BlockPos pos, BlockState blockState) {
        FallingBlockEntity fallingBlockEntity = new FallingCementEntity(level, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState);
        level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }

    @Override
    public void tick() {
        BlockPos blockPos = this.blockPosition();
        var relativeState = this.level().getBlockState(blockPos);
        boolean bl = this.blockState.getBlock() instanceof CementSlabBlock;
        boolean bl2 = this.blockState.getBlock() instanceof CementBlock;
        boolean bl3 = (bl||bl2) && relativeState.getBlock() instanceof CementSlabBlock;
        if (bl3) {
            boolean weathered = this.blockState.is(ModTags.WEATHERED_CEMENT);
            var state = weathered ?
                    (level().random.nextFloat() < CommonConfigs.General.FALLING_CEMENT_CRACK_CHANCE.get()) ?
                            ModBlocks.CRACKED_WEATHERED_CEMENT_SLAB.get() : ModBlocks.WEATHERED_CEMENT_SLAB.get() : (level().random.nextFloat() < CommonConfigs.General.FALLING_CEMENT_CRACK_CHANCE.get()) ? ModBlocks.CRACKED_CEMENT_SLAB.get() : ModBlocks.CEMENT_SLAB.get();

            level().setBlockAndUpdate(blockPos, state.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE));
            if (bl2) level().setBlockAndUpdate(blockPos.above(), state.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM));
            level().playSound(null, blockPos, SoundEvents.DEEPSLATE_BREAK, SoundSource.BLOCKS, 1.0F, level().getRandom().nextFloat() * 0.1F + 0.9F);
            this.discard();
            return;
        }
        super.tick();
    }



    /*

    public FallingCementEntity(EntityType<? extends FallingBlockEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void setBlockState(BlockState state) {
        if (state.hasProperty(BlockStateProperties.SLAB_TYPE)) {
            state = state.setValue(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM);
        }
        CompoundTag tag = new CompoundTag();
        tag.put("BlockState", NbtUtils.writeBlockState(state));
        tag.putInt("Time", this.time);
        this.readAdditionalSaveData(tag);
    }

    public FallingCementEntity(Level level, BlockPos pos, BlockState blockState) {
        super(ModEntities.FALLING_CEMENT_ENTITY.get(), level, pos, blockState, false);
    }

    public static FallingCementEntity fall(Level level, BlockPos pos, BlockState state) {
        FallingCementEntity entity = new FallingCementEntity(level, pos, state);
        level.addFreshEntity(entity);
        return entity;
    }
     */
}
