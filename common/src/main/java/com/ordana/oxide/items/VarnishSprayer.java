package com.ordana.oxide.items;

import com.ordana.oxide.OxideClient;
import com.ordana.oxide.reg.ModComponents;
import com.ordana.oxide.reg.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.mehvahdjukaar.moonlight.api.fluids.MLBuiltinSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VarnishSprayer extends Item
        //implements IThirdPersonAnimationProvider //TODO: add this for fancy animation
{

    public static final int MAX_CHARGES = 128;

    public VarnishSprayer(Properties properties) {
        super(properties);
    }

    //initialize if null
    @NotNull
    public static SFStackView getFluid(ItemStack stack, Level level) {
        SFStackView f = stack.get(ModComponents.FLUID.get());
        if (f == null) {
            SFStackView view = SFStackView.of(SoftFluidStack.empty(level.registryAccess()));
            stack.set(ModComponents.FLUID.get(), view);
            f = view;
        }
        return f;
    }

    //fill water
    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        FluidState state = context.getLevel().getFluidState(pos);
        if(!state.isEmpty()){
            SoftFluidStack sf = SoftFluidStack.fromFluid(state, level.registryAccess());
            if(!sf.isEmpty() && sf.is(ModTags.CAN_GO_IN_SPRAY)){
                //TODO: fill
            }

        }
        return super.useOn(context);
    }

    //start using
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        SFStackView fluid = getFluid(itemstack, level);

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
            SFStackView fluid = getFluid(stack, OxideClient.getClienntLevel());
            fluid.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean isBarVisible(ItemStack stack) {
        SFStackView fluid = getFluid(stack, OxideClient.getClienntLevel());
        return !fluid.isEmpty();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarWidth(ItemStack stack) {
        SFStackView fluid = getFluid(stack, OxideClient.getClienntLevel());
        if (fluid.isEmpty()) return 0;
        if (true) return 13;
        //TODO: this might need to be adjusted
        return Math.round(((((float) MAX_CHARGES + fluid.getCount()) / MAX_CHARGES * 13f) - 13));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarColor(ItemStack stack) {
        Level clienntLevel = OxideClient.getClienntLevel();
        SFStackView fluid = getFluid(stack, clienntLevel);
        if (fluid.isEmpty()) return -1;
        return fluid.getParticleColor(clienntLevel, BlockPos.ZERO);
    }
}
