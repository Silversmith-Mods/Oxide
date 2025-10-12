package com.ordana.oxide.entities;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.ordana.oxide.blocks.rusty.Rustable;
import com.ordana.oxide.items.SFStackView;
import com.ordana.oxide.reg.ModEntities;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.entity.ParticleTrailEmitter;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class FluidDropEntity extends ImprovedProjectileEntity {

    private static final Supplier<EntityDataAccessor<SFStackView>> DATA_FLUID = Suppliers.memoize(() ->
            SynchedEntityData.defineId(FluidDropEntity.class, Preconditions.checkNotNull(ModEntities.FLUID_DATA.get())));

    //TODO configure to your linking. this makes sure particles are equally spread out no matter the proj speed
    private final ParticleTrailEmitter trailEmitter = new ParticleTrailEmitter.Builder()
            .maxParticlesPerTick(20)
            .minParticlesPerTick(1)
            .spacing(0.3f)
            .build();

    public FluidDropEntity(Level level, LivingEntity shooter, SFStackView fluid) {
        super(ModEntities.FLUID_DROP.get(), shooter, level);
        this.maxAge = (level.dimensionType().ultraWarm() ? 7 : 300);
        this.setDataFluid(fluid);
    }

    public FluidDropEntity(EntityType<? extends FluidDropEntity> type, Level world) {
        super(type, world);
        this.maxAge = (world.dimensionType().ultraWarm() ? 7 : 300);
        this.maxStuckTime = (90000000);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLUID.get(), SFStackView.of(SoftFluidStack.empty(this.level().registryAccess())));
    }

    public void setDataFluid(SFStackView fluid) {
        this.entityData.set(DATA_FLUID.get(), fluid);
    }

    public SFStackView getDataFluid() {
        return this.entityData.get(DATA_FLUID.get());
    }

    //data to be saved when the entity gets unloaded


    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("fluid", this.getDataFluid().save(this.level().registryAccess()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setDataFluid(SFStackView.load(this.level().registryAccess(), compound.getCompound("fluid")));
    }

    @Override
    protected Item getDefaultItem() {
        return Items.BARRIER;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Items.BARRIER);
    }


    @Override
    public void tick() {

        //if (this.active && this.isInWater() && this.type != BombType.BLUE) {
        //    this.turnOff();
        //}
        super.tick();
    }


    @Override
    public void spawnTrailParticles() {
        if (this.tickCount > 1) {
            var pt = level().dimensionType().ultraWarm() ? ParticleTypes.POOF : ParticleTypes.FALLING_WATER;
            trailEmitter.tick(this, (pos, speed) -> {
                this.level().addParticle(
                        pt,
                        pos.x,
                        pos.y,
                        pos.z,
                        0, 0.02, 0);
            });
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {

        BlockPos pos = hit.getBlockPos();
        BlockState state = this.level().getBlockState(pos);

        var rusted = Rustable.getIncreasedRustBlock(state.getBlock());
        if (rusted.isPresent()) {
            this.level().setBlockAndUpdate(pos, rusted.get().withPropertiesOf(state));
            if (random.nextFloat() > 0.75)
                this.level().playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
        }

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }

        super.onHitBlock(hit);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if (entity instanceof Blaze) entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) 3);
    }

}