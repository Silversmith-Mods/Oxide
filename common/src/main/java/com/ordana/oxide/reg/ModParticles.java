package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModParticles {

    public static void init() {
    }

    public static Supplier<SimpleParticleType> registerParticle(String name) {
        return RegHelper.registerParticle(Oxide.res(name));
    }

    public static final Supplier<SimpleParticleType> SCRAPE_RUST = registerParticle("scrape_rust");
    public static final Supplier<SimpleParticleType> VARNISH = registerParticle("varnish");
    public static final Supplier<SimpleParticleType> WATER = registerParticle("water");
}
