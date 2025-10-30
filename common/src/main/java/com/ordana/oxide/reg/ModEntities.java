package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.entities.RustyNailEntity;
import com.ordana.oxide.entities.SprayParticleEntity;
import com.ordana.oxide.items.SFStackView;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntities {

    public static void init() {
    }

    public static final Supplier<EntityDataSerializer<SFStackView>> FLUID_DATA = RegHelper.registerEntityDataSerializer(
            Oxide.res("fluid_data"), () -> EntityDataSerializer.forValueType(SFStackView.STREAM_CODEC));

    public static final Supplier<EntityType<RustyNailEntity>> RUSTY_NAIL = RegHelper.registerEntityType(
            Oxide.res("rusty_nail"),
            RustyNailEntity::new, MobCategory.MISC, 0.3F, 0.3F, 10, 20);

    public static final Supplier<EntityType<SprayParticleEntity>> SPRAY_ENTITY = RegHelper.registerEntityType(
            Oxide.res("spray_entity"),
            SprayParticleEntity::new, MobCategory.MISC, 0.3F, 0.3F, 10, 20);



}
