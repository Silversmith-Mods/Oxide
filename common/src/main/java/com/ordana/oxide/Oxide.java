package com.ordana.oxide;

import com.ordana.oxide.blocks.rusty.Rustable;
import com.ordana.oxide.configs.ClientConfigs;
import com.ordana.oxide.configs.CommonConfigs;
import com.ordana.oxide.reg.*;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Oxide {

    public static final String MOD_ID = "oxide";
    public static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation res(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    public static void commonInit() {

        CommonConfigs.init();

        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientConfigs.init();
            OxideClient.init();
        }

        PlatHelper.addCommonSetup(Oxide::setup);

        ModParticles.init();
        ModCreativeTab.init();
        ModBlocks.init();
        ModItems.init();
        ModEntities.init();
        ModComponents.init();
        ModRecipes.init();
        ModDispenserBehaviors.init();
    }

    public static void setup() {
        //ModCompostable.register();
    }

    public Optional<BlockState> getNext(BlockState state) {
        return Rustable.getIncreasedRustBlock(state.getBlock()).map(block -> block.withPropertiesOf(state));
    }
}
