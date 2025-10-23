package com.ordana.oxide;

import com.ordana.oxide.entities.DrippingLiquidParticle;
import com.ordana.oxide.entities.FallingLiquidParticle;
import com.ordana.oxide.entities.RustyNailRenderer;
import com.ordana.oxide.reg.ModBlocks;
import com.ordana.oxide.reg.ModEntities;
import com.ordana.oxide.reg.ModItems;
import com.ordana.oxide.reg.ModParticles;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;

public class OxideClient {

    public static final ModelLayerLocation RUSTY_NAIL =  new ModelLayerLocation(Oxide.res("rusty_nail"), "rusty_nail");

    public static void init() {
        ClientHelper.addClientSetup(OxideClient::setup);
        ClientHelper.addEntityRenderersRegistration(OxideClient::registerEntityRenderers);
        //ClientHelper.registerOptionalTexturePack(Oxide.res("visual_waxed_iron_items"));
        ClientHelper.addParticleRegistration(OxideClient::registerParticles);
    }

    public static void setup() {
        ClientHelper.registerRenderType(ModBlocks.WHITE_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.LIGHT_GRAY_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.GRAY_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.BLACK_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.BROWN_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RED_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.ORANGE_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.YELLOW_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.LIME_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.GREEN_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.CYAN_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.LIGHT_BLUE_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.BLUE_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.PURPLE_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.MAGENTA_PAINT.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.PINK_PAINT.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.IRON_SCAFFOLD.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_SCAFFOLD.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_SCAFFOLD.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.IRON_SCAFFOLD_STAIRS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_SCAFFOLD_STAIRS.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_SCAFFOLD_STAIRS.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.IRON_SCAFFOLD_SLAB.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_IRON_SCAFFOLD_SLAB.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_IRON_SCAFFOLD_SLAB.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.HEAVY_IRON_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_HEAVY_IRON_TRAPDOOR.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_HEAVY_IRON_TRAPDOOR.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.HEAVY_IRON_CHAIN.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_HEAVY_IRON_CHAIN.get(), RenderType.cutout());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_HEAVY_IRON_CHAIN.get(), RenderType.cutout());

        ClientHelper.registerRenderType(ModBlocks.HEAVY_IRON_BARS.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_HEAVY_IRON_BARS.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_HEAVY_IRON_BARS.get(), RenderType.cutoutMipped());

        ClientHelper.registerRenderType(ModBlocks.WROUGHT_IRON_FENCE.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_WROUGHT_IRON_FENCE.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_WROUGHT_IRON_FENCE.get(), RenderType.cutoutMipped());

        ClientHelper.registerRenderType(ModBlocks.WROUGHT_IRON_FENCE_GATE.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.WEATHERED_WROUGHT_IRON_FENCE_GATE.get(), RenderType.cutoutMipped());
        ClientHelper.registerRenderType(ModBlocks.RUSTED_WROUGHT_IRON_FENCE_GATE.get(), RenderType.cutoutMipped());

        ItemProperties.register(ModItems.VARNISH_SPRAYER.get(), Oxide.res("primed"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && (livingEntity.getUseItemRemainingTicks() != 0) && livingEntity.getUseItem() == itemStack && (livingEntity.getUseItemRemainingTicks() % 20 <= 1) ? 1 : 0);

    }

    private static void registerEntityRenderers(ClientHelper.EntityRendererEvent event) {
        event.register(ModEntities.RUSTY_NAIL.get(), RustyNailRenderer::new);
        event.register(ModEntities.SPRAY_ENTITY.get(), NoopRenderer::new);
    }

    private static void registerParticles(ClientHelper.ParticleEvent event) {
        event.register(ModParticles.SCRAPE_RUST.get(), ScrapeRustFactory::new);
        event.register(ModParticles.DRIPPING_LIQUID.get(), DrippingLiquidParticle.Factory::new);
        event.register(ModParticles.FALLING_LIQUID.get(), FallingLiquidParticle.Factory::new);
        event.register(ModParticles.SPLASHING_LIQUID.get(), ColoredSplashingParticle::new);
    }

    public static Level getClientLevel() {
        return Minecraft.getInstance().level;
    }


    public static class ColoredSplashingParticle extends SplashParticle.Provider {
        public ColoredSplashingParticle(SpriteSet sprites) {
            super(sprites);
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double r, double g, double b) {
            var p = super.createParticle(type, level, x, y, z, 0, 0, 0);
            p.setColor((float) r, (float) g, (float) b);
            return p;
        }
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