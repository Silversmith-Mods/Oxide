package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
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

    private static TagKey<Block> registerBlockTag(String id) {
        return TagKey.create(Registries.BLOCK, Oxide.res(id));
    }
}
