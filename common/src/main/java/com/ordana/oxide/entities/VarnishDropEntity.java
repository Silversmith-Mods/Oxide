package com.ordana.oxide.entities;

import com.ordana.oxide.reg.ModBlockProperties;
import com.ordana.oxide.reg.ModEntities;
import com.ordana.oxide.reg.ModItems;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class VarnishDropEntity extends ImprovedProjectileEntity {

    private boolean active = true;
    private int changeTimer = -1;
    private boolean superCharged = false;
    private EntityType type = ModEntities.VARNISH_DROP.get();

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

    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult hit) {

        BlockState state = level().getBlockState(hit.getBlockPos());
        if (state.hasProperty(ModBlockProperties.VARNISHED)) {
            if (!state.getValue(ModBlockProperties.VARNISHED)) level().setBlockAndUpdate(hit.getBlockPos(), state.setValue(ModBlockProperties.VARNISHED, true));
        }

        var waxed = HoneycombItem.getWaxed(state);
        waxed.ifPresent(blockState -> level().setBlockAndUpdate(hit.getBlockPos(), blockState));

        super.onHitBlock(hit);
    }
}