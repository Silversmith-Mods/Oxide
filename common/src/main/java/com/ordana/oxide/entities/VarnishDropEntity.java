package com.ordana.oxide.entities;

import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModEntities;
import com.ordana.oxide.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class VarnishDropEntity extends ImprovedProjectileEntity {

    private boolean active = true;
    private int changeTimer = -1;
    private boolean superCharged = false;
    private EntityType type = ModEntities.VARNISH_DROP.get();


    public VarnishDropEntity(Level level, LivingEntity shooter) {
        super(ModEntities.VARNISH_DROP.get(), shooter, level);
    }

    public VarnishDropEntity(EntityType<? extends VarnishDropEntity> type, Level world) {
        super(type, world);
        this.maxAge = (300);
        this.maxStuckTime = (90000000);
    }

    public VarnishDropEntity(Level worldIn, LivingEntity throwerIn, EntityType type) {
        super(ModEntities.VARNISH_DROP.get(), throwerIn, worldIn);
        this.type = type;
        this.maxAge = (300);
        this.maxStuckTime = (90000000);
    }

    //data to be saved when the entity gets unloaded
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.HONEYCOMB;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Blocks.AIR);
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
        Vec3 newPos = this.position();
        if (this.active && this.tickCount > 1) {

            double dx = newPos.x - xo;
            double dy = newPos.y - yo;
            double dz = newPos.z - zo;
            int s = 1;
            for (int i = 0; i < s; ++i) {
                double j = i / (double) s;
                this.level().addParticle(ModParticles.VARNISH.get(),
                        xo - dx * j,
                        0.25 + yo - dy * j,
                        zo - dz * j,
                        0, 0.02, 0);
            }
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {

        BlockPos pos = hit.getBlockPos();
        BlockState state = level().getBlockState(pos);

        if (state.hasProperty(ModBlockProperties.VARNISHED)) {
            if (!state.getValue(ModBlockProperties.VARNISHED)) level().setBlockAndUpdate(pos, state.setValue(ModBlockProperties.VARNISHED, true));
            level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
        }

        var waxed = HoneycombItem.getWaxed(state);
        if (waxed.isPresent()) {
            level().setBlockAndUpdate(pos, waxed.get());
            level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0f, 1.0f);
            ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
        }


        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }

        super.onHitBlock(hit);
    }
}