package com.ordana.oxide.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;

public class RustyNailEntity extends Arrow {
    public RustyNailEntity(EntityType<? extends Arrow> entityType, Level level) {
        super(entityType, level);
    }
}
