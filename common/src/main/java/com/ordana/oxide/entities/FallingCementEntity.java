package com.ordana.oxide.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class FallingCementEntity extends FallingBlockEntity {

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
