package com.ordana.oxide.neoforge;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.reg.ModSetup;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.neoforged.fml.common.Mod;

@Mod(Oxide.MOD_ID)
public class OxideForge {

    public OxideForge() {
        Oxide.commonInit();
        PlatHelper.addCommonSetup(ModSetup::setup);
    }
}

