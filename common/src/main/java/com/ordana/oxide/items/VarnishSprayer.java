package com.ordana.oxide.items;

import com.ordana.oxide.entities.VarnishDropEntity;
import com.ordana.oxide.entities.WaterDropEntity;
import com.ordana.oxide.reg.ModComponents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.mehvahdjukaar.moonlight.api.entity.ImprovedProjectileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;
import java.util.function.Supplier;

public class VarnishSprayer extends Item {
    public VarnishSprayer(Properties properties) {
        super(properties);
    }




    public void setPrimed(ItemStack stack, int amount) {
        stack.set(ModComponents.PRIMED.get(), amount);
    }
    public static int isPrimed(ItemStack stack) {
        return stack.getOrDefault(ModComponents.PRIMED.get(), 0);
    }


    public Supplier<DataComponentType<Integer>> getFluid(ItemStack stack) {
        var water = stack.getOrDefault(ModComponents.WATER.get(), 0);
        var varnish = stack.getOrDefault(ModComponents.VARNISH.get(), 0);
        return (water) > 0 ? ModComponents.WATER : (varnish) > 0 ? ModComponents.VARNISH : null;
    }
    public int getAmount(ItemStack stack) {
        if (getFluid(stack) == null) return 0;
        return (stack.getOrDefault(getFluid(stack).get(), 0));
    }
    public void setAmount(ItemStack stack, int amount) {
        stack.set(getFluid(stack).get(), amount);
    }
    public boolean isWater (ItemStack stack) {
        return getFluid(stack) == ModComponents.WATER;
    }
    public boolean isVarnish (ItemStack stack) {
        return getFluid(stack) == ModComponents.VARNISH;
    }


    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (isVarnish(stack)) tooltip.add(Component.translatable( "tooltip.oxide.varnish_sprayer_varnish", getAmount(stack), "1000").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GOLD)));
        if (isWater(stack)) tooltip.add(Component.translatable( "tooltip.oxide.varnish_sprayer_water", getAmount(stack), "1000").setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        boolean state = isPrimed(stack) == 1;
        int fluid = getAmount(stack);


        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            if (!level.mayInteract(player, blockPos)) {
                return InteractionResultHolder.pass(stack);
            }

            if (level.getFluidState(blockPos).is(FluidTags.WATER) && (!isVarnish(stack))) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);
                stack.set(ModComponents.WATER.get(), 1000);
                setPrimed(stack, 1);
                return InteractionResultHolder.success(stack);
            }
        }


        if (fluid > 0) {
            if (!state) {
                setPrimed(stack, 1);
                return InteractionResultHolder.success(stack);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                for (int y = -4; y < 4; ++y) {
                    for (int x = -4; x < 4; ++x) {
                        ImprovedProjectileEntity entity = null;
                        if (isVarnish(stack)) entity = new VarnishDropEntity(level, player);
                        else if (isWater(stack)) entity = new WaterDropEntity(level, player);
                        assert entity != null;

                        entity.shootFromRotation(player, player.getXRot() + (y * 5 * level.random.nextFloat()), player.getYRot() + (x * 5 * level.random.nextFloat()), 1.0F + (5 * level.random.nextFloat()), 1.5F + (2 * level.random.nextFloat()), 1.0F);
                        level.addFreshEntity(entity);
                    }
                }
            }
            if (!player.getAbilities().instabuild) {
                setAmount(stack, fluid == 1 ? stack.remove(getFluid(stack).get()) : fluid - 1);
                setPrimed(stack, 0);
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
        return Math.round((((1000f + getAmount(stack)) / 1000f * 13f) - 13));
    }

    public int getBarColor(ItemStack stack) {
        if (isVarnish(stack)) return 0xea8e16;
        if (isWater(stack)) return 0x2e58d3;
        else return 0xffffff;
    }
}
