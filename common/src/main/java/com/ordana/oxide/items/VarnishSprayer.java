package com.ordana.oxide.items;

import com.ordana.oxide.OxideClient;
import com.ordana.oxide.entities.FluidDropEntity;
import com.ordana.oxide.reg.ModComponents;
import com.ordana.oxide.reg.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.misc.FabricOverride;
import net.mehvahdjukaar.moonlight.api.misc.ForgeOverride;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VarnishSprayer extends Item
        //implements IThirdPersonAnimationProvider //TODO: add this for fancy animation
{

    public VarnishSprayer(Properties properties) {
        super(properties);
    }

    public static int getMaxCharges(ItemStack stack) {
        return stack.getOrDefault(ModComponents.MAX_DROPS.get(), 0);
    }

    //initialize if null
    @NotNull
    public static SFStackView getFluidComponent(ItemStack stack, HolderLookup.Provider reg) {
        SFStackView f = stack.get(ModComponents.FLUID.get());
        if (f == null) {
            SFStackView view = SFStackView.of(SoftFluidStack.empty(reg));
            stack.set(ModComponents.FLUID.get(), view);
            f = view;
        }
        return f;
    }

    public static void setFluidComponent(ItemStack stack, SoftFluidStack fluid) {
        stack.set(ModComponents.FLUID.get(), SFStackView.of(fluid));
    }

    //fill water
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();

        FluidState state = context.getLevel().getFluidState(pos);
        if (!state.isEmpty()) {
            SoftFluidStack fluidThatBlockContains = SoftFluidStack.fromFluid(state, level.registryAccess());
            if (!fluidThatBlockContains.isEmpty() && fluidThatBlockContains.is(ModTags.CAN_GO_IN_SPRAY)) {
                var myFluid = getFluidComponent(stack, level.registryAccess());
                boolean full = getMaxCharges(stack) <= myFluid.getCount();
                if (!full) {
                    int bottles = fluidThatBlockContains.getCount();
                    //TODO: fill

                }
            }

        }
        return super.useOn(context);
    }

    //start using
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        SFStackView fluid = getFluidComponent(itemstack, level.registryAccess());

        if (!fluid.isEmpty()) {
            //TODO: play sound here
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
    }


    //spray here
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);

        SFStackView fluid = getFluidComponent(stack, level.registryAccess());
        if (fluid.isEmpty()) {
            livingEntity.stopUsingItem();
            return;
        }
        //shrink fluid stack count
        SoftFluidStack mutable = fluid.toMutable();
        mutable.shrink(1);
        setFluidComponent(stack, mutable);
        FluidDropEntity fluidDrop = new FluidDropEntity(level, livingEntity, fluid.copyWithCount(1));
        //TODO: shoot
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        //TODO: use anim. leave if using special animation stuff
        return super.getUseAnimation(stack);
    }

    /*
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        stack.set(ModComponents.FLUID.get(), SFStackView.of(SoftFluidStack.of(MLBuiltinSoftFluids.WATER.getHolder(level))));

        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!level.mayInteract(player, blockPos)) {
                return InteractionResultHolder.pass(stack);
            }

            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 0.6F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                stack.set(ModComponents.FLUID.get(), SFStackView.of(SoftFluidStack.of(MLBuiltinSoftFluids.WATER.getHolder(level), 128)));
                //    setFluid(level, stack, MLBuiltinSoftFluids.WATER, 128);
                //   setPrimed(stack, true);
                return InteractionResultHolder.success(stack);
            }
        }

        SFStackView containedFluid = stack.get(ModComponents.FLUID.get());
        if (containedFluid == null || containedFluid.isEmpty()) return InteractionResultHolder.pass(stack);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.AZALEA_LEAVES_BREAK, SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!level.isClientSide) {
            for (int y = -4; y < 4; ++y) {
                for (int x = -4; x < 4; ++x) {
                    ImprovedProjectileEntity entity = null;
                    //     if (getFluid(stack).is(MLBuiltinSoftFluids.WATER)) entity = new WaterDropEntity(level, player);
                    //  assert entity != null;

                    //      entity.shootFromRotation(player, player.getXRot() + (y * 5 * level.random.nextFloat()), player.getYRot() + (x * 5 * level.random.nextFloat()), 1.0F + (5 * level.random.nextFloat()), 1.5F + (2 * level.random.nextFloat()), 1.0F);
                    //      level.addFreshEntity(entity);
                }
            }
        }
        if (!player.getAbilities().instabuild) {
            // var bl = getFluid(stack).getCount() == 1;
            // if (bl) stack.remove(ModComponents.FLUID.get());
            // else setAmount(stack, getFluid(stack).getCount() - 1);
            // setPrimed(stack, false);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }*/

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (PlatHelper.getPhysicalSide().isClient()) {
            SFStackView fluid = getFluidComponent(stack, OxideClient.getClienntLevel().registryAccess());
            fluid.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean isBarVisible(ItemStack stack) {
        SFStackView fluid = getFluidComponent(stack, OxideClient.getClienntLevel().registryAccess());
        return !fluid.isEmpty();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarWidth(ItemStack stack) {
        SFStackView fluid = getFluidComponent(stack, OxideClient.getClienntLevel().registryAccess());
        if (fluid.isEmpty()) return 0;
        int getMaxCharges = getMaxCharges(stack);
        return Math.round(((((float) getMaxCharges + fluid.getCount()) / getMaxCharges * 13f) - 13));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarColor(ItemStack stack) {
        Level clienntLevel = OxideClient.getClienntLevel();
        SFStackView fluid = getFluidComponent(stack, clienntLevel.registryAccess());
        if (fluid.isEmpty()) return -1;
        return fluid.getParticleColor(clienntLevel, BlockPos.ZERO);
    }


    @FabricOverride
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        SFStackView sf = oldStack.get(ModComponents.FLUID.get());
        SFStackView sf2 = newStack.get(ModComponents.FLUID.get());
        if (sf != null && sf2 != null) {
            if (sf.getFluid() == sf2.getFluid()) {
                return sf.getCount() == sf2.getCount();
            }
        }
        return true;
    }

    @ForgeOverride
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (slotChanged) return true;
        SFStackView sf = oldStack.get(ModComponents.FLUID.get());
        SFStackView sf2 = newStack.get(ModComponents.FLUID.get());
        if (sf != null && sf2 != null) {
            if (sf.getFluid() == sf2.getFluid()) {
                return sf.getCount() == sf2.getCount();
            }
        }
        return true;
    }
}
