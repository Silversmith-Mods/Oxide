package com.ordana.oxide.items;

import com.ordana.oxide.entities.VarnishDropEntity;
import com.ordana.oxide.reg.ModComponents;
import com.ordana.oxide.reg.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class VarnishSprayer extends Item {
    public VarnishSprayer(Properties properties) {
        super(properties);
    }

    public void setAmount(ItemStack stack, int amount) {
        stack.set(ModComponents.VARNISH.get(), amount);
    }

    public int getAmount(ItemStack stack) {
        return stack.getOrDefault(ModComponents.VARNISH.get(), 1000);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.oxide.varnish_sprayer", getAmount(stack), "1000").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }


    @Override
    public int getUseDuration(ItemStack itemStack, LivingEntity livingEntity) {
        return 72000;
    }


    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round((((1000f + getAmount(stack)) / 1000f * 13f) - 13));
    }

    public int getBarColor(ItemStack stack) {
        return 0xffffff;
    }
}
