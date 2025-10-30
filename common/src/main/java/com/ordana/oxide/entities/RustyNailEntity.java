package com.ordana.oxide.entities;

import com.ordana.oxide.reg.ModEntities;
import com.ordana.oxide.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class RustyNailEntity extends ImprovedProjectileEntity {

    private boolean active = true;
    private int changeTimer = -1;
    private boolean superCharged = false;
    private EntityType type = ModEntities.RUSTY_NAIL.get();

    public RustyNailEntity(EntityType<? extends RustyNailEntity> type, Level world) {
        super(type, world);
        this.maxAge = (300);
        this.maxStuckTime = (90000000);
    }

    public RustyNailEntity(Level worldIn, LivingEntity throwerIn, EntityType type) {
        super(ModEntities.RUSTY_NAIL.get(), throwerIn, worldIn);
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
        return ModItems.RUSTY_NAIL.get();
    }

    /*
    @Override
    public ItemStack getItem() {
        return type.getDisplayStack(this.active);
    }
     */


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
            int s = 4;
            for (int i = 0; i < s; ++i) {
                double j = i / (double) s;
                this.level().addParticle(ParticleTypes.SMOKE,
                        xo - dx * j,
                        0.25 + yo - dy * j,
                        zo - dz * j,
                        0, 0.02, 0);
            }
        }
    }

    @Override
    public void playerTouch(Player entityIn) {
        if (!this.level().isClientSide) {
            if (this.tickCount < 10) return;
            playSound(SoundEvents.THORNS_HIT);
            if (!entityIn.isCreative()) {
                entityIn.hurt(level().damageSources().thrown(this, this.getOwner()), 1);
                entityIn.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, true, false, true));
            }
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        playSound(SoundEvents.THORNS_HIT);
        if (hit.getEntity() instanceof Player player) {
            if (!player.isCreative()) {
                hit.getEntity().hurt(level().damageSources().thrown(this, this.getOwner()), 1);
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1, true, false, true));
            }
        }
        this.discard();

    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {
        super.onHitBlock(hit);

        BlockState state = level().getBlockState(hit.getBlockPos());
        if (!state.is(Blocks.SLIME_BLOCK) || hit.getDirection() != Direction.UP) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setOnGround(true);
        }
    }
}