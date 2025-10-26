package com.ordana.oxide.items;

import com.ordana.oxide.OxideClient;
import com.ordana.oxide.entities.SprayParticleEntity;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000; //arbitrary high value, we will stop using manually. same as bow. arm gets tired i guess
    }

    //spray here
    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, livingEntity, stack, remainingUseDuration);

        if (remainingUseDuration > getUseDuration(stack, livingEntity) - 20) return;
        if (remainingUseDuration % 20 != 0) return;

        SFStackView fluid = getFluidComponent(stack, level.registryAccess());
        if (fluid.isEmpty()) {
            livingEntity.stopUsingItem();
            return;
        }
        //shrink fluid stack count
        if (livingEntity instanceof Player player) {
            if (!player.isCreative()) {
                SoftFluidStack mutable = fluid.toMutable();
                if (fluid.getCount() >= 1) mutable.shrink(1);
                setFluidComponent(stack, mutable);
            }
        }

        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.AZALEA_LEAVES_BREAK, SoundSource.NEUTRAL, 1F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        //TODO: shoot
        for (int y = -3; y < 3; ++y) {
            for (int x = -3; x < 3; ++x) {
                SprayParticleEntity fluidDrop = new SprayParticleEntity(level, livingEntity, fluid.copyWithCount(1));
                fluidDrop.shootFromRotation(livingEntity, livingEntity.getXRot() + (y * 5 * level.random.nextFloat()), livingEntity.getYRot() + (x * 5 * level.random.nextFloat()), 1.0F + (5 * level.random.nextFloat()), 1.0F + (level.random.nextFloat() / 2), 1.0F);
                level.addFreshEntity(fluidDrop);
            }
        }
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (PlatHelper.getPhysicalSide().isClient()) {
            SFStackView fluid = getFluidComponent(stack, OxideClient.getClientLevel().registryAccess());
            fluid.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean isBarVisible(ItemStack stack) {
        SFStackView fluid = getFluidComponent(stack, OxideClient.getClientLevel().registryAccess());
        return !fluid.isEmpty();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarWidth(ItemStack stack) {
        SFStackView fluid = getFluidComponent(stack, OxideClient.getClientLevel().registryAccess());
        if (fluid.isEmpty()) return 0;
        int getMaxCharges = getMaxCharges(stack);
        return Math.round(((((float) getMaxCharges + fluid.getCount()) / getMaxCharges * 13f) - 13));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarColor(ItemStack stack) {
        Level clienntLevel = OxideClient.getClientLevel();
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
