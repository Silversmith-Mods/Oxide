package com.ordana.oxide;

import com.ordana.oxide.configs.ClientConfigs;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.SimpleParticleType;

public class OxideClient {
    
    public static void init() {
        ClientHelper.addClientSetup(OxideClient::setup);
        ClientHelper.registerOptionalTexturePack(Oxide.res("visual_waxed_iron_items"));
        ClientHelper.addParticleRegistration(OxideClient::registerParticles);
    }


    public static void setup() {
        ClientHelper.registerRenderType(ModBlocks.IRON_SCAFFOLD.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_SCAFFOLD.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_SCAFFOLD.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.IRON_SCAFFOLD_STAIRS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_SCAFFOLD_STAIRS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_SCAFFOLD_STAIRS.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.IRON_SCAFFOLD_SLAB.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_SCAFFOLD_SLAB.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_SCAFFOLD_SLAB.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.HEAVY_IRON_DOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_HEAVY_IRON_DOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_HEAVY_IRON_DOOR.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_TRAPDOOR.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_BARS.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_BARS.get(), RenderType.cutoutMipped());

        ClientHelper.registerRenderType(ModBlocks.WAXED_HEAVY_IRON_DOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WAXED_WEATHERED_HEAVY_IRON_DOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WAXED_RUSTED_HEAVY_IRON_DOOR.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.WAXED_IRON_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WAXED_WEATHERED_IRON_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WAXED_RUSTED_IRON_TRAPDOOR.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.WAXED_IRON_BARS.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.WAXED_WEATHERED_IRON_BARS.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.WAXED_RUSTED_IRON_BARS.get(), RenderType.cutoutMipped());
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(ModParticles.SCRAPE_RUST.get(), ScrapeRustFactory::new);
    }

    private static class ScrapeRustFactory extends GlowParticle.ScrapeProvider {

        public ScrapeRustFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double p_172207_, double p_172208_, double p_172209_, double p_172210_, double p_172211_, double p_172212_) {
            Particle p = super.createParticle(particleType, level, p_172207_, p_172208_, p_172209_, p_172210_, p_172211_, p_172212_);
            if (p != null) {
                if (level.random.nextBoolean()) {
                    p.setColor(196 / 255f, 118 / 255f, 73 / 255f);
                } else {
                    p.setColor(176 / 255f, 63 / 255f, 40 / 255f);
                }
            }
            return p;
        }
    }
}