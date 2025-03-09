package com.ordana.oxide.items;

import com.ordana.oxide.entities.RustyNailEntity;
import com.ordana.oxide.reg.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.Level;

public class RustyNailItem extends Item {
    public RustyNailItem(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        ItemStack itemstack = player.getItemInHand(usedHand);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));

        if (!level.isClientSide) {
            RustyNailEntity nail = new RustyNailEntity(level, player, ModEntities.RUSTY_NAIL.get());
            float pitch = -10;//player.isSneaking()?0:-20;
            nail.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    pitch, nail.getDefaultShootVelocity(), 1);
            level.addFreshEntity(nail);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);

        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}