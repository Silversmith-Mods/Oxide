package com.ordana.oxide.fabric;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.reg.ModSetup;
import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;


public class OxideFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        Oxide.commonInit();

        PlatHelper.addCommonSetup(ModSetup::setup);
    }
}
