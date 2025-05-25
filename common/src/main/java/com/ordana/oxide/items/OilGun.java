package com.ordana.oxide.items;

import com.ordana.oxide.reg.ModComponents;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class OilGun extends Item {
    public OilGun(Properties properties) {
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
        tooltip.add(Component.translatable("tooltip.oxide.oil_gun", getAmount(stack), "1000").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (getAmount(itemStack) == 0) return InteractionResultHolder.fail(itemStack);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_SPLASH, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            Snowball eggplantEntity = new Snowball(level, player);
            eggplantEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(eggplantEntity);
            setAmount(itemStack,getAmount(itemStack) - 1);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    public int getBarWidth(ItemStack stack) {
        return Math.round((((1000f + getAmount(stack)) / 1000f * 13f) - 13));
    }

    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(1F, 0F, 1F);
    }
}
