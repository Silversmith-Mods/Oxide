package com.ordana.oxide.neoforge;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.reg.ModSetup;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;

@Mod(Oxide.MOD_ID)
public class OxideNeoForge {

    public OxideNeoForge() {
        Oxide.commonInit();
        PlatHelper.addCommonSetup(ModSetup::setup);
    }

}

