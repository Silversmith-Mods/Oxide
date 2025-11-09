package com.ordana.oxide.entities;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.ordana.oxide.blocks.rusty.Rustable;
import com.ordana.oxide.items.SFStackView;
import com.ordana.oxide.reg.*;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.entity.ParticleTrailEmitter;
import net.mehvahdjukaar.moonlight.api.fluids.MLBuiltinSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class SprayParticleEntity extends ImprovedProjectileEntity {

    public static final Supplier<EntityDataAccessor<SFStackView>> DATA_FLUID = Suppliers.memoize(() ->
            SynchedEntityData.defineId(SprayParticleEntity.class, Preconditions.checkNotNull(ModEntities.FLUID_DATA.get())));

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

    public SprayParticleEntity(Level level, SFStackView fluid) {
        super(ModEntities.SPRAY_ENTITY.get(), level);
        this.maxAge = ((level.dimensionType().ultraWarm() && fluid.is(MLBuiltinSoftFluids.WATER)) ? 7 : 300);
        this.setDataFluid(fluid);
    }

    public SprayParticleEntity(Level level, double x, double y, double z, SFStackView fluid) {
        super(ModEntities.SPRAY_ENTITY.get(), x, y, z, level);
        this.maxAge = ((level.dimensionType().ultraWarm() && fluid.is(MLBuiltinSoftFluids.WATER)) ? 7 : 300);
        this.setDataFluid(fluid);
    }

    public SprayParticleEntity(EntityType<SprayParticleEntity> particle, Level level) {
        super(particle, level);
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
        super.tick();
    }


    @Override
    public void spawnTrailParticles() {
        if (this.tickCount > 1) {
            Vec3 newPos = this.position();

            boolean ultraWarm = level().dimensionType().ultraWarm();
            var pt = ultraWarm ? ParticleTypes.POOF : ModParticles.FALLING_LIQUID.get();
            int color = ultraWarm ? 0 : getDataFluid().getParticleColor(level(), this.blockPosition());

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


        if (getDataFluid().is(MLBuiltinSoftFluids.LAVA)) {
            placeFire(this.level(), hit);
            if (random.nextFloat() > 0.75) level().playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
        }

        if (getDataFluid().is(ModTags.PAINT)) {
            placePaint(this.level(), hit);
            if (random.nextFloat() > 0.75) level().playSound(null, pos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
        }

        if (getDataFluid().is(MLBuiltinSoftFluids.WATER)) {
            var relPos = pos.relative(hit.getDirection());
            var relState = level().getBlockState(relPos);
            if (relState.is(ModTags.WATER_DESTROYS)) level().destroyBlock(relPos, false);

            var rusted = Rustable.getIncreasedRustBlock(state.getBlock());
            if (rusted.isPresent()) {
                this.level().setBlockAndUpdate(pos, rusted.get().withPropertiesOf(state));
                if (random.nextFloat() > 0.75) this.level().playSound(null, pos, SoundEvents.AMBIENT_UNDERWATER_ENTER, SoundSource.BLOCKS, 0.5f, 0.8f + random.nextFloat());
            }
            else if (random.nextFloat() > 0.75) this.level().playSound(null, pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER, SoundSource.BLOCKS, 2f, 0.7f + random.nextFloat());
        }

        if (getDataFluid().is(ModTags.VARNISH)) {

            if (state.hasProperty(ModBlockProperties.VARNISHED)) {
                if (!state.getValue(ModBlockProperties.VARNISHED)) {
                    level().setBlockAndUpdate(pos, state.setValue(ModBlockProperties.VARNISHED, true));
                    level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1f, 0.8f + random.nextFloat());
                    if (level().isClientSide()) ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
                }
            }

            var relPos = pos.relative(hit.getDirection());
            var relState = level().getBlockState(relPos);
            if (relState.hasProperty(ModBlockProperties.VARNISHED)) if (!relState.getValue(ModBlockProperties.VARNISHED)) {
                level().setBlockAndUpdate(relPos, relState.setValue(ModBlockProperties.VARNISHED, true));
                level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1f, 0.8f + random.nextFloat());
                if (level().isClientSide()) ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
            }

            var waxed = HoneycombItem.getWaxed(state);
            if (waxed.isPresent()) {
                level().setBlockAndUpdate(pos, waxed.get());
                level().playSound(null, pos, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1f, 0.8f + random.nextFloat());
                if (level().isClientSide()) ParticleUtils.spawnParticlesOnBlockFaces(level(), pos, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
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

        if (getDataFluid().is(ModTags.PAINT)) if (entity instanceof Sheep sheep) sheep.setColor(getDye(getDataFluid().getFluid()));
        if (getDataFluid().is(MLBuiltinSoftFluids.WATER)) if (entity instanceof Blaze) entity.hurt(this.damageSources().thrown(this, this.getOwner()), (float) 3);
        if (getDataFluid().is(MLBuiltinSoftFluids.LAVA)) if (!entity.fireImmune()) entity.lavaHurt();
        if (getDataFluid().is(MLBuiltinSoftFluids.MILK)) if (entity instanceof LivingEntity livingEntity) livingEntity.removeAllEffects();
        if (getDataFluid().is(ModTags.VARNISH)) if (entity instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, true, false, true));
    }

    public void placeFire(Level level, BlockHitResult hitResult) {
        Direction dir = hitResult.getDirection();
        BlockPos pos = hitResult.getBlockPos();
        BlockPos relativePos = pos.relative(dir);

        if (BaseFireBlock.canBePlacedAt(level, relativePos, dir)) {
            level.setBlockAndUpdate(relativePos, BaseFireBlock.getState(level, relativePos));
        }
        this.remove(RemovalReason.DISCARDED);
    }

    public void placePaint(Level level, BlockHitResult hitResult) {

        Direction dir = hitResult.getDirection();
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        BlockPos relativePos = pos.relative(dir);
        BlockState replaceState = level.getBlockState(relativePos);
        var paintBlock = getPaint(getDataFluid().getFluid());
        var colorBlock = BlocksColorAPI.changeColor(state.getBlock(), getDye(getDataFluid().getFluid()));

        if (colorBlock != null) level.setBlockAndUpdate(pos, colorBlock.withPropertiesOf(state));
        else if (state.is(ModTags.PAINTABLE)) level.setBlockAndUpdate(pos, getCorrugatedIron(state, getDataFluid().getFluid()).withPropertiesOf(state));
        else if (replaceState.canBeReplaced() && state.isFaceSturdy(level, pos, dir)) {
            level.setBlockAndUpdate(relativePos, replaceState.is(paintBlock) ? replaceState.setValue(MultifaceBlock.getFaceProperty(dir.getOpposite()), true) : paintBlock.defaultBlockState().setValue(MultifaceBlock.getFaceProperty(dir.getOpposite()), true).setValue(BlockStateProperties.UP, false));
        }
        this.remove(RemovalReason.DISCARDED);
    }

    private Block getPaint(SoftFluid fluid) {
        String color = String.valueOf(fluid.getTintColor());
        switch (color) {
            case "15854828": return ModBlocks.WHITE_PAINT.get();
            case "10921124": return ModBlocks.LIGHT_GRAY_PAINT.get();
            case "5987163": return ModBlocks.GRAY_PAINT.get();
            case "1776411": return ModBlocks.BLACK_PAINT.get();
            case "4338210": return ModBlocks.BROWN_PAINT.get();
            case "13315116": return ModBlocks.RED_PAINT.get();
            case "14900769": return ModBlocks.ORANGE_PAINT.get();
            case "15647526": return ModBlocks.YELLOW_PAINT.get();
            case "2734143": return ModBlocks.LIME_PAINT.get();
            case "2394164": return ModBlocks.GREEN_PAINT.get();
            case "1021870": return ModBlocks.CYAN_PAINT.get();
            case "3786976": return ModBlocks.LIGHT_BLUE_PAINT.get();
            case "2903216": return ModBlocks.BLUE_PAINT.get();
            case "7881893": return ModBlocks.PURPLE_PAINT.get();
            case "10832555": return ModBlocks.MAGENTA_PAINT.get();
            case "12999817": return ModBlocks.PINK_PAINT.get();
        }
        return ModBlocks.WHITE_PAINT.get();
    }

    private DyeColor getDye(SoftFluid fluid) {
        String color = String.valueOf(fluid.getTintColor());
        switch (color) {
            case "15854828": return DyeColor.WHITE;
            case "10921124": return DyeColor.LIGHT_GRAY;
            case "5987163": return DyeColor.GRAY;
            case "1776411": return DyeColor.BLACK;
            case "4338210": return DyeColor.BROWN;
            case "13315116": return DyeColor.RED;
            case "14900769": return DyeColor.ORANGE;
            case "15647526": return DyeColor.YELLOW;
            case "2734143": return DyeColor.LIME;
            case "2394164": return DyeColor.GREEN;
            case "1021870": return DyeColor.CYAN;
            case "3786976": return DyeColor.LIGHT_BLUE;
            case "2903216": return DyeColor.BLUE;
            case "7881893": return DyeColor.PURPLE;
            case "10832555": return DyeColor.MAGENTA;
            case "12999817": return DyeColor.PINK;
        }
        return DyeColor.WHITE;
    }


    private Block getCorrugatedIron(BlockState state, SoftFluid fluid) {
        String color = String.valueOf(fluid.getTintColor());
        switch (color) {
            case "15854828": return state.is(BlockTags.SLABS) ? ModBlocks.WHITE_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.WHITE_CORRUGATED_IRON_STAIRS.get() : ModBlocks.WHITE_CORRUGATED_IRON.get();
            case "10921124": return state.is(BlockTags.SLABS) ? ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_STAIRS.get() : ModBlocks.LIGHT_GRAY_CORRUGATED_IRON.get();
            case "5987163": return state.is(BlockTags.SLABS) ? ModBlocks.GRAY_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.GRAY_CORRUGATED_IRON_STAIRS.get() : ModBlocks.GRAY_CORRUGATED_IRON.get();
            case "1776411": return state.is(BlockTags.SLABS) ? ModBlocks.BLACK_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.BLACK_CORRUGATED_IRON_STAIRS.get() : ModBlocks.BLACK_CORRUGATED_IRON.get();
            case "4338210": return state.is(BlockTags.SLABS) ? ModBlocks.BROWN_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.BROWN_CORRUGATED_IRON_STAIRS.get() : ModBlocks.BROWN_CORRUGATED_IRON.get();
            case "13315116": return state.is(BlockTags.SLABS) ? ModBlocks.RED_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.RED_CORRUGATED_IRON_STAIRS.get() : ModBlocks.RED_CORRUGATED_IRON.get();
            case "14900769": return state.is(BlockTags.SLABS) ? ModBlocks.ORANGE_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.ORANGE_CORRUGATED_IRON_STAIRS.get() : ModBlocks.ORANGE_CORRUGATED_IRON.get();
            case "15647526": return state.is(BlockTags.SLABS) ? ModBlocks.YELLOW_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.YELLOW_CORRUGATED_IRON_STAIRS.get() : ModBlocks.YELLOW_CORRUGATED_IRON.get();
            case "2734143": return state.is(BlockTags.SLABS) ? ModBlocks.LIME_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.LIME_CORRUGATED_IRON_STAIRS.get() : ModBlocks.LIME_CORRUGATED_IRON.get();
            case "2394164": return state.is(BlockTags.SLABS) ? ModBlocks.GREEN_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.GREEN_CORRUGATED_IRON_STAIRS.get() : ModBlocks.GREEN_CORRUGATED_IRON.get();
            case "1021870": return state.is(BlockTags.SLABS) ? ModBlocks.CYAN_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.CYAN_CORRUGATED_IRON_STAIRS.get() : ModBlocks.CYAN_CORRUGATED_IRON.get();
            case "3786976": return state.is(BlockTags.SLABS) ? ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_STAIRS.get() : ModBlocks.LIGHT_BLUE_CORRUGATED_IRON.get();
            case "2903216": return state.is(BlockTags.SLABS) ? ModBlocks.BLUE_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.BLUE_CORRUGATED_IRON_STAIRS.get() : ModBlocks.BLUE_CORRUGATED_IRON.get();
            case "7881893": return state.is(BlockTags.SLABS) ? ModBlocks.PURPLE_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.PURPLE_CORRUGATED_IRON_STAIRS.get() : ModBlocks.PURPLE_CORRUGATED_IRON.get();
            case "10832555": return state.is(BlockTags.SLABS) ? ModBlocks.MAGENTA_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.MAGENTA_CORRUGATED_IRON_STAIRS.get() : ModBlocks.MAGENTA_CORRUGATED_IRON.get();
            case "12999817": return state.is(BlockTags.SLABS) ? ModBlocks.PINK_CORRUGATED_IRON_SLAB.get() : state.is(BlockTags.STAIRS) ? ModBlocks.PINK_CORRUGATED_IRON_STAIRS.get() : ModBlocks.PINK_CORRUGATED_IRON.get();
        }
        return ModBlocks.WHITE_CORRUGATED_IRON.get();
    }
}