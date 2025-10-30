package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModParticles {

    public static void init() {
    }

    public static Supplier<SimpleParticleType> reg(String name) {
        return RegHelper.registerParticle(Oxide.res(name));
    }

    public static final Supplier<SimpleParticleType> SCRAPE_RUST = reg("scrape_rust");
    public static final Supplier<SimpleParticleType> DRIPPING_LIQUID = reg("dripping_liquid");
    public static final Supplier<SimpleParticleType> FALLING_LIQUID = reg("falling_liquid");
    public static final Supplier<SimpleParticleType> SPLASHING_LIQUID = reg("splashing_liquid");
}
