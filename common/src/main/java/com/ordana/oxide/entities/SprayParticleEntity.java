package com.ordana.oxide.entities;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.ordana.oxide.blocks.rusty.Rustable;
import com.ordana.oxide.items.SFStackView;
import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModEntities;
import com.ordana.oxide.reg.ModParticles;
import com.ordana.oxide.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.entity.ParticleTrailEmitter;
import net.mehvahdjukaar.moonlight.api.fluids.MLBuiltinSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.HoneycombItem;
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

public class SprayParticleEntity extends ImprovedProjectileEntity {

    public static final Supplier<EntityDataAccessor<SFStackView>> DATA_FLUID = Suppliers.memoize(() ->
            SynchedEntityData.defineId(SprayParticleEntity.class, Preconditions.checkNotNull(ModEntities.FLUID_DATA.get())));

    //TODO configure to your linking. this makes sure particles are equally spread out no matter the proj speed
    private final ParticleTrailEmitter trailEmitter = new ParticleTrailEmitter.Builder()
            .maxParticlesPerTick(20)
            .minParticlesPerTick(1)
            .spacing(0.3f)
            .build();

    public SprayParticleEntity(Level level, LivingEntity shooter, SFStackView fluid) {
        super(ModEntities.SPRAY_ENTITY.get(), shooter, level);
        this.maxAge = ((level.dimensionType().ultraWarm() && fluid.is(MLBuiltinSoftFluids.WATER)) ? 7 : 300);
        this.setDataFluid(fluid);
    }

    public SprayParticleEntity(EntityType<? extends SprayParticleEntity> type, Level level) {
        super(type, level);
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
            Vec3 newPos = this.position();

            boolean ultraWarm = level().dimensionType().ultraWarm();
            var pt = ultraWarm ? ParticleTypes.POOF : ModParticles.FALLING_LIQUID.get();
            int color = ultraWarm ? 0 : getDataFluid().getParticleColor(level(), this.blockPosition());
            //TODO:copy supplementaries faucet drop particles (how it does its color). then pass this color arg to the particle or similar


            float r = FastColor.ARGB32.red(color) / 255f;
            float g = FastColor.ARGB32.green(color) / 255f;
            float b = FastColor.ARGB32.blue(color) / 255f;

            double dx = newPos.x - xo;
            double dy = newPos.y - yo;
            double dz = newPos.z - zo;
            int s = 1;
            var particle = ModParticles.FALLING_LIQUID.get();

            for (int i = 0; i < s; ++i) {
                double j = i / (double) s;
                if (this.level().dimensionType().ultraWarm()) particle = ParticleTypes.POOF;
                this.level().addParticle(particle, xo - dx * j, 0.25 + yo - dy * j, zo - dz * j, r, g, b);

            }
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

        if (getDataFluid().is(MLBuiltinSoftFluids.WATER)) {
            var rusted = Rustable.getIncreasedRustBlock(state.getBlock());
            if (rusted.isPresent()) {
                this.level().setBlockAndUpdate(pos, rusted.get().withPropertiesOf(state));
                if (random.nextFloat() > 0.75)
                    this.level().playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
            }
        }

        if (getDataFluid().is(ModTags.VARNISH)) {
            if (state.hasProperty(ModBlockProperties.VARNISHED)) {
                if (!state.getValue(ModBlockProperties.VARNISHED)) level().setBlockAndUpdate(pos, state.setValue(ModBlockProperties.VARNISHED, true));
                if (level().isClientSide) {
                    if (random.nextFloat() > 0.75) level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                    ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
                }
            }

            var waxed = HoneycombItem.getWaxed(state);
            if (waxed.isPresent()) {
                level().setBlockAndUpdate(pos, waxed.get());
                if (level().isClientSide) {
                    if (random.nextFloat() > 0.75) level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
                    ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
                }
            }
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
        if (entity.is(this)) return;

        if (getDataFluid().is(MLBuiltinSoftFluids.WATER)) if (entity instanceof Blaze) entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) 3);
        if (getDataFluid().is(MLBuiltinSoftFluids.LAVA)) if (!entity.fireImmune()) entity.lavaHurt();
        if (getDataFluid().is(ModTags.VARNISH)) if (entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, true, false, true));
    }

}