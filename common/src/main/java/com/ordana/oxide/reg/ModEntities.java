package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.entities.RustyNailEntity;
import com.ordana.oxide.entities.SprayParticleEntity;
import com.ordana.oxide.entities.WaterDropEntity;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {

    public static void init() {
    }

    public static <T extends Entity> Supplier<EntityType<T>> regEntity(String name, Supplier<EntityType.Builder<T>> builder) {
        return RegHelper.registerEntityType(Oxide.res(name), () -> builder.get().build(name));
    }

    public static Supplier<EntityType<RustyNailEntity>> RUSTY_NAIL = RegHelper.registerEntityType(
            Oxide.res("rusty_nail"),
            RustyNailEntity::new, MobCategory.MISC, 0.3F, 0.3F, 10, 20);

    public static Supplier<EntityType<SprayParticleEntity>> SPRAY_ENTITY = RegHelper.registerEntityType(
            Oxide.res("spray_entity"),
            SprayParticleEntity::new, MobCategory.MISC, 0.3F, 0.3F, 10, 20);

    public static Supplier<EntityType<WaterDropEntity>> WATER_DROP = RegHelper.registerEntityType(
            Oxide.res("water_drop"),
            WaterDropEntity::new, MobCategory.MISC, 0.3F, 0.3F, 10, 20);
}
