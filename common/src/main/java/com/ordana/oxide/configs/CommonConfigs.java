package com.ordana.oxide.configs;

import com.ordana.oxide.Oxide;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;

import java.util.function.Supplier;

public class CommonConfigs {

    //public static final ConfigSpec SERVER_SPEC;

    public static final Supplier<Boolean> CREATIVE_TAB;
    public static final Supplier<Boolean> RUSTING;
    public static final Supplier<Integer> RUSTING_INFLUENCE_RADIUS;
    public static final Supplier<Double> RUSTING_RATE;

    public static final Supplier<Integer> CEMENT_FLOW_RATE;


    public static void init() {
    }

    static{
        ConfigBuilder builder = ConfigBuilder.create(Oxide.res("common"), ConfigType.COMMON);

        //builder.setSynced();

        builder.push("rusting");
        CREATIVE_TAB = builder.define("creative_tab", false);
        RUSTING = builder.define("rusting", true);
        RUSTING_INFLUENCE_RADIUS = builder.define("rusting_influence_radius", 4, 1, 8);
        RUSTING_RATE = builder.define("rusting_rate", 0.06, 0, 1);
        CEMENT_FLOW_RATE = builder.define("cement_flow_rate", 8, 1, 32);
        builder.pop();

        //SERVER_SPEC = builder.buildAndRegister();
        //SERVER_SPEC.loadFromFile();
    }

}
