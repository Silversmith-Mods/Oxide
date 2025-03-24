package com.ordana.oxide.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PureNailItem extends Item {
    public PureNailItem(Item.Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 8.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ENTITY_INTERACTION_RANGE, new AttributeModifier(BASE_ATTACK_SPEED_ID, 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = (ServerLevel)attacker.level();

            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 1, true, false));
            attacker.setDeltaMovement(attacker.getLookAngle().x * 0.7, 0.8, attacker.getLookAngle().z * 0.7);
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            serverLevel.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, serverPlayer.getSoundSource(), 5.0F, 3F / (serverLevel.random.nextFloat() * 0.4F + 0.8F));
        }

        return true;
    }

    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = (ServerLevel)serverPlayer.level();

            serverPlayer.setDeltaMovement(serverPlayer.isCrouching() ? -serverPlayer.getLookAngle().x : serverPlayer.getLookAngle().x, 0.8, serverPlayer.isCrouching() ? -serverPlayer.getLookAngle().z : serverPlayer.getLookAngle().z);
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            level.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, serverPlayer.getSoundSource(), 5.0F, 3F / (serverLevel.random.nextFloat() * 0.4F + 0.8F));
        }
        return !player.isCreative();
    }
}