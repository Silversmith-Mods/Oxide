package com.ordana.oxide.entities;

import com.ordana.oxide.reg.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RustyNailEntity extends ThrowableItemProjectile {

    public RustyNailEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public void playerTouch(Player player) {
        if (!this.level().isClientSide) {
            if (player.takeXpDelay == 0) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 4, 1, true, false, true));

                //this.discard();
            }

        }
    }

    protected Item getDefaultItem() {
        return ModItems.RUSTY_NAIL.get();
    }
}
