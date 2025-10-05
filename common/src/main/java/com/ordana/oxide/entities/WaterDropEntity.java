package com.ordana.oxide.entities;

import com.ordana.oxide.blocks.rusty.Rustable;
import com.ordana.oxide.reg.ModEntities;
import net.mehvahdjukaar.moonlight.api.client.util.ParticleUtil;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class WaterDropEntity extends ImprovedProjectileEntity {

    private boolean active = true;
    private int changeTimer = -1;
    private boolean superCharged = false;
    private EntityType type = ModEntities.WATER_DROP.get();


    public WaterDropEntity(Level level, LivingEntity shooter) {
        super(ModEntities.WATER_DROP.get(), shooter, level);
        this.maxAge = (level.dimensionType().ultraWarm() ? 7 : 300);
    }

    public WaterDropEntity(EntityType<? extends WaterDropEntity> type, Level world) {
        super(type, world);
        this.maxAge = (world.dimensionType().ultraWarm() ? 7 : 300);
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
        return Items.GLASS_BOTTLE;
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
        Vec3 newPos = this.position();
        if (this.active && this.tickCount > 1) {

            double dx = newPos.x - xo;
            double dy = newPos.y - yo;
            double dz = newPos.z - zo;
            int s = 1;
            for (int i = 0; i < s; ++i) {
                double j = i / (double) s;
                this.level().addParticle(level().dimensionType().ultraWarm() ? ParticleTypes.POOF : ParticleTypes.SPLASH,
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
        BlockState state = this.level().getBlockState(pos);

        var rusted = Rustable.getIncreasedRustBlock(state.getBlock());
        if (rusted.isPresent()) {
            this.level().setBlockAndUpdate(pos, rusted.get().withPropertiesOf(state));
            this.level().playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (this.level().isClientSide) ParticleUtil.spawnParticlesOnBlockFaces(this.level(), pos, ParticleTypes.SPLASH, UniformInt.of(3, 5), -0.05f, 0.05f, false);
        }

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }

        super.onHitBlock(hit);
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        int i = entity instanceof Blaze ? 3 : 0;
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float)i);
    }

}