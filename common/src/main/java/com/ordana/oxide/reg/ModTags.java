package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final TagKey<Block> CLEAN_IRON = registerBlockTag("clean_iron");
    public static final TagKey<Block> EXPOSED_IRON = registerBlockTag("exposed_iron");
    public static final TagKey<Block> WEATHERED_IRON = registerBlockTag("weathered_iron");
    public static final TagKey<Block> RUSTED_IRON = registerBlockTag("rusted_iron");
    public static final TagKey<Block> RUSTABLE = registerBlockTag("rustable");
    public static final TagKey<Block> WAXED_BLOCKS = registerBlockTag("waxed_blocks");
    public static final TagKey<Block> SCAFFOLDS = registerBlockTag("scaffolds");
    public static final TagKey<Block> WEATHERED = registerBlockTag("weathered");
    public static final TagKey<Block> WET_CEMENT = registerBlockTag("wet_cement");
    public static final TagKey<Block> REBAR = registerBlockTag("rebar");
    public static final TagKey<Block> BARS = registerBlockTag("bars");
    public static final TagKey<Block> PAINTABLE = registerBlockTag("paintable");
    public static final TagKey<Block> WATER_DESTROYS = registerBlockTag("water_destroys");
    public static final TagKey<Block> PAINT_BLOCK = registerBlockTag("paint");
    public static final TagKey<Block> WEATHERED_CEMENT = registerBlockTag("weathered_cement");
    public static final TagKey<Block> REINFORCED_CEMENT = registerBlockTag("reinforced_cement");

    public static final TagKey<SoftFluid> CAN_GO_IN_SPRAY = registerSFTag("can_go_in_spray");
    public static final TagKey<SoftFluid> VARNISH = registerSFTag("varnish");
    public static final TagKey<SoftFluid> PAINT = registerSFTag("paint");

    public static final TagKey<Item> LEATHER_ARMOR = registerItemTag("leather_armor");


    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, Oxide.res(id));
    }
    private static TagKey<Item> registerItemTag(String id) {
        return TagKey.create(Registries.ITEM, Oxide.res(id));
    }

    private static TagKey<SoftFluid> registerSFTag(String id) {
        return TagKey.create(SoftFluidRegistry.KEY, Oxide.res(id));
    }
}
