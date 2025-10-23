package com.ordana.oxide.reg;

import com.ordana.oxide.blocks.Dispenser.SprayerDispenserBehavior;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;

public class ModDispenserBehaviors {

    public static void init() {
        RegHelper.addDynamicDispenserBehaviorRegistration(ModDispenserBehaviors::registerBehaviors);
    }

    public static void registerBehaviors(DispenserHelper.Event event) {
        event.register(new SprayerDispenserBehavior(ModItems.VARNISH_SPRAYER.get()));
    }

}