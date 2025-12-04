package com.ordana.oxide.configs;

import com.ordana.oxide.Oxide;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.configs.ModConfigHolder;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommonConfigs {

    public static void init() {
    }

    public static final ModConfigHolder CONFIG_HOLDER;
    private static final WeakReference<ConfigBuilder> builderReference;

    static {
        ConfigBuilder builder = ConfigBuilder.create(Oxide.MOD_ID, ConfigType.COMMON_SYNCED);

        builderReference = new WeakReference<>(builder);

        General.init();

        CONFIG_HOLDER = builder.build();
        CONFIG_HOLDER.forceLoad();
    }

    public static class General {
        public static void init() {
        }

        static {
            ConfigBuilder builder = builderReference.get();

            builder.comment("General settings")
                    .push("general");

            CREATIVE_TAB = builder.comment("Enable Creative Tab").define("creative_tab", false);

            RUST_RATE = builder.comment("Enable Creative Tab").define("rusting_rate", 50, 0, 100);
            CEMENT_FLOW_RATE = builder.comment("Rate at which Wet Cement flows").define("cement_flow_rate", 8, 1, 32);
            CEMENT_CURE_DELAY = builder.comment("Amount of age states before cement can harden").define("cement_cure_delay", 10, 1, 25);
            FALLING_CEMENT_CRACK_CHANCE = builder.comment("Odds of cement cracking after falling").define("cement_crack_chance", 0.5f, 0f, 1f);

            builder.pop();
        }

        public static final Supplier<Boolean> CREATIVE_TAB;
        public static final Supplier<Integer> RUST_RATE;
        public static final Supplier<Integer> CEMENT_FLOW_RATE;
        public static final Supplier<Integer> CEMENT_CURE_DELAY;
        public static final Supplier<Float> FALLING_CEMENT_CRACK_CHANCE;
    }
}