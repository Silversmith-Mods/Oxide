package com.ordana.oxide.items;

import com.ordana.oxide.entities.WaterDropEntity;
import com.ordana.oxide.reg.ModComponents;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.fluids.MLBuiltinSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.misc.HolderReference;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class VarnishSprayer extends Item {
    public VarnishSprayer(Properties properties) {
        super(properties);
    }

    public void setPrimed(ItemStack stack, boolean bl) {
        stack.set(ModComponents.PRIMED.get(), bl);
    }
    public static boolean isPrimed(ItemStack stack) {
        return stack.getOrDefault(ModComponents.PRIMED.get(), false);
    }




    public SoftFluidStackView getFluid(ItemStack stack) {
        return stack.get(ModComponents.FLUID.get());
    }

    public void setAmount(ItemStack stack, int amount) {
        if (stack.get(ModComponents.FLUID.get()) != null) {
            var mutableStack = stack.get(ModComponents.FLUID.get()).toMutable();
            mutableStack.setCount(amount);
            stack.set(ModComponents.FLUID.get(), SoftFluidStackView.of(mutableStack));
        }
    }

    public void setFluid(Level level, ItemStack stack, HolderReference<SoftFluid> fluid, int amount) {
        if (stack.get(ModComponents.FLUID.get()) != null) {
            var mutableStack = stack.get(ModComponents.FLUID.get()).toMutable();
            mutableStack = SoftFluidStack.of(fluid.getHolder(level), amount);
            stack.set(ModComponents.FLUID.get(), SoftFluidStackView.of(mutableStack));
        }
    }


    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!level.mayInteract(player, blockPos)) {
                return InteractionResultHolder.pass(stack);
            }

            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 0.6F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                stack.set(ModComponents.FLUID.get(), SoftFluidStackView.of(SoftFluidStack.of(MLBuiltinSoftFluids.WATER.getHolder(level), 128)));
                setFluid(level, stack, MLBuiltinSoftFluids.WATER, 128);
                setPrimed(stack, true);
                return InteractionResultHolder.success(stack);
            }
        }

        boolean state = isPrimed(stack);
        int fluid = getFluid(stack).getCount();

        if (fluid > 0) {
            if (!state) {
                setPrimed(stack, true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1F, 1.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                return InteractionResultHolder.success(stack);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AZALEA_LEAVES_BREAK, SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                for (int y = -4; y < 4; ++y) {
                    for (int x = -4; x < 4; ++x) {
                        ImprovedProjectileEntity entity = null;
                        if (getFluid(stack).is(MLBuiltinSoftFluids.WATER)) entity = new WaterDropEntity(level, player);
                        assert entity != null;

                        entity.shootFromRotation(player, player.getXRot() + (y * 5 * level.random.nextFloat()), player.getYRot() + (x * 5 * level.random.nextFloat()), 1.0F + (5 * level.random.nextFloat()), 1.5F + (2 * level.random.nextFloat()), 1.0F);
                        level.addFreshEntity(entity);
                    }
                }
            }
            if (!player.getAbilities().instabuild) {
                var bl = getFluid(stack).getCount() == 1;
                if (bl) stack.remove(ModComponents.FLUID.get());
                else setAmount(stack, getFluid(stack).getCount() - 1);
                setPrimed(stack, false);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }

        return InteractionResultHolder.pass(stack);
    }


    public boolean isBarVisible(ItemStack stack) {
        return getFluid(stack) != null;
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round((((128f + getFluid(stack).getCount()) / 128f * 13f) - 13));
    }

    public int getBarColor(ItemStack stack) {
        return getFluid(stack).toMutable().getStillColor(Minecraft.getInstance().level, null);
    }
}
