package com.ordana.oxide.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;

import java.util.List;

public class PureNailItem extends Item {
    public PureNailItem(Item.Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    //public UseAnim getUseAnimation(ItemStack stack) {
        //return UseAnim.BOW;
    //}

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (!(livingEntity instanceof Player player)) return;
        player.setDeltaMovement(player.getDeltaMovement().x, 0.6, player.getDeltaMovement().z);
        player.knockback((double) timeCharged /150000, -player.getLookAngle().x, -player.getLookAngle().z);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PHANTOM_FLAP, SoundSource.NEUTRAL, 3F, 2F / (level.random.nextFloat() * 0.4F + 0.8F));
    }
    public void AttackEntity(Player player, Level level) {
        player.setDeltaMovement(player.getDeltaMovement().x, 0.6, player.getDeltaMovement().z);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.THORNS_HIT, SoundSource.NEUTRAL, 3F, 3.4F / (level.random.nextFloat() * 0.4F + 0.8F));
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }
}