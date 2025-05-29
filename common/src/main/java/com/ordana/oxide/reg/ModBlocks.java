package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.blocks.*;
import com.ordana.oxide.blocks.rusty.*;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class ModBlocks {

    public static void init() {
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }

    public static <T extends Block> Supplier<T> regBlock(String name, Supplier<T> block) {
        return RegHelper.registerBlock(Oxide.res(name), block);
    }

    public static Supplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties) {
        return RegHelper.registerItem(Oxide.res(name), () -> new BlockItem(blockSup.get(), properties));
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> blockFactory) {
        Supplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, new Item.Properties());
        return block;
    }


    public static final Supplier<Block> REBAR = regWithItem("rebar", () ->
            new RebarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER_GRATE).noOcclusion().mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> CEMENTED_REBAR = regWithItem("cemented_rebar", () ->
            new CementedRebarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER_GRATE).noOcclusion().mapColor(MapColor.DEEPSLATE)));

    public static final Supplier<Block> WET_CEMENT = regWithItem("wet_cement", () ->
            new WetCementBlock(BlockBehaviour.Properties.of().sound(SoundType.MUD).randomTicks().strength(3.0F, 6.0F).requiresCorrectToolForDrops().isSuffocating(ModBlocks::always).mapColor(MapColor.DEEPSLATE)));

    public static final Supplier<Block> CEMENT = regWithItem("cement", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));
    public static final Supplier<Block> CEMENT_STAIRS = regWithItem("cement_stairs", () ->
            new ModStairBlock(ModBlocks.CEMENT, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));
    public static final Supplier<Block> CEMENT_SLAB = regWithItem("cement_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));


    public static final Supplier<Block> CINDER_BRICKS = regWithItem("cinder_bricks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> CINDER_BRICK_STAIRS = regWithItem("cinder_brick_stairs", () ->
            new ModStairBlock(ModBlocks.CEMENT, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> CINDER_BRICK_SLAB = regWithItem("cinder_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> CINDER_BRICK_WALL = regWithItem("cinder_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));

    public static final Supplier<Block> WEATHERED_CINDER_BRICKS = regWithItem("weathered_cinder_bricks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> WEATHERED_CINDER_BRICK_STAIRS = regWithItem("weathered_cinder_brick_stairs", () ->
            new ModStairBlock(ModBlocks.CINDER_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> WEATHERED_CINDER_BRICK_SLAB = regWithItem("weathered_cinder_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> WEATHERED_CINDER_BRICK_WALL = regWithItem("weathered_cinder_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.COLOR_GRAY)));

    public static final Supplier<Block> CEMENT_BLOCK = regWithItem("cement_block", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));
    public static final Supplier<Block> CEMENT_BLOCK_SLAB = regWithItem("cement_block_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));
    public static final Supplier<Block> CINDER_BLOCKS = regWithItem("cinder_blocks", () ->
            new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));
    public static final Supplier<Block> CINDER_BLOCK = regWithItem("cinder_block", () ->
            new CinderBlockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));
    public static final Supplier<Block> CINDER_BLOCK_SLAB = regWithItem("cinder_block_slab", () ->
            new RotatableSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.STONE)));

    public static final Supplier<Block> CLINKER_BRICKS = regWithItem("clinker_bricks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_RED)));
    public static final Supplier<Block> CLINKER_BRICK_STAIRS = regWithItem("clinker_brick_stairs", () ->
            new ModStairBlock(ModBlocks.CLINKER_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_RED)));
    public static final Supplier<Block> CLINKER_BRICK_SLAB = regWithItem("clinker_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_RED)));
    public static final Supplier<Block> CLINKER_BRICK_WALL = regWithItem("clinker_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_RED)));

    public static final Supplier<Block> STOCK_BRICKS = regWithItem("stock_bricks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> STOCK_BRICK_STAIRS = regWithItem("stock_brick_stairs", () ->
            new ModStairBlock(ModBlocks.STOCK_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> STOCK_BRICK_SLAB = regWithItem("stock_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> STOCK_BRICK_WALL = regWithItem("stock_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.TERRACOTTA_YELLOW)));

    public static final Supplier<Block> CREAM_BRICKS = regWithItem("cream_bricks", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.SAND)));
    public static final Supplier<Block> CREAM_BRICK_STAIRS = regWithItem("cream_brick_stairs", () ->
            new ModStairBlock(ModBlocks.CREAM_BRICKS, BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.SAND)));
    public static final Supplier<Block> CREAM_BRICK_SLAB = regWithItem("cream_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.SAND)));
    public static final Supplier<Block> CREAM_BRICK_WALL = regWithItem("cream_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.SAND)));


    //plate iron
    public static final Supplier<Block> PLATE_IRON = regWithItem("plate_iron", () ->
            new RustableBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_PLATE_IRON = regWithItem("weathered_plate_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_PLATE_IRON = regWithItem("rusted_plate_iron", () ->
            new RustableBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> PLATE_IRON_STAIRS = regWithItem("plate_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, PLATE_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_PLATE_IRON_STAIRS = regWithItem("weathered_plate_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, PLATE_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_PLATE_IRON_STAIRS = regWithItem("rusted_plate_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.RUSTED, PLATE_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> PLATE_IRON_SLAB = regWithItem("plate_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_PLATE_IRON_SLAB = regWithItem("weathered_plate_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_PLATE_IRON_SLAB = regWithItem("rusted_plate_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));


    //cut iron
    public static final Supplier<Block> CUT_IRON = regWithItem("cut_iron", () ->
            new RustableBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_CUT_IRON = regWithItem("weathered_cut_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_CUT_IRON = regWithItem("rusted_cut_iron", () ->
            new RustableBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> CUT_IRON_STAIRS = regWithItem("cut_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_CUT_IRON_STAIRS = regWithItem("weathered_cut_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_CUT_IRON_STAIRS = regWithItem("rusted_cut_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.RUSTED, CUT_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> CUT_IRON_SLAB = regWithItem("cut_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_CUT_IRON_SLAB = regWithItem("weathered_cut_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_CUT_IRON_SLAB = regWithItem("rusted_cut_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));


    //scaffolds
    public static final Supplier<Block> IRON_SCAFFOLD = regWithItem("iron_scaffold", () ->
            new RustableScaffoldBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_IRON_SCAFFOLD = regWithItem("weathered_iron_scaffold", () ->
            new RustableScaffoldBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_IRON_SCAFFOLD = regWithItem("rusted_iron_scaffold", () ->
            new RustableScaffoldBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_RED)));

    public static final Supplier<Block> IRON_SCAFFOLD_STAIRS = regWithItem("iron_scaffold_stairs", () ->
            new RustableScaffoldStairsBlock(Rustable.RustLevel.CLEAN, IRON_SCAFFOLD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_IRON_SCAFFOLD_STAIRS = regWithItem("weathered_iron_scaffold_stairs", () ->
            new RustableScaffoldStairsBlock(Rustable.RustLevel.WEATHERED, IRON_SCAFFOLD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_IRON_SCAFFOLD_STAIRS = regWithItem("rusted_iron_scaffold_stairs", () ->
            new RustableScaffoldStairsBlock(Rustable.RustLevel.RUSTED, IRON_SCAFFOLD, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_RED)));

    public static final Supplier<Block> IRON_SCAFFOLD_SLAB = regWithItem("iron_scaffold_slab", () ->
            new RustableScaffoldSlabBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_IRON_SCAFFOLD_SLAB = regWithItem("weathered_iron_scaffold_slab", () ->
            new RustableScaffoldSlabBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_IRON_SCAFFOLD_SLAB = regWithItem("rusted_iron_scaffold_slab", () ->
            new RustableScaffoldSlabBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.COPPER_GRATE).mapColor(MapColor.TERRACOTTA_RED)));


    //iron pillar
    public static final Supplier<Block> IRON_PILLAR = regWithItem("iron_pillar", () ->
            new RustablePillarBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_IRON_PILLAR = regWithItem("weathered_iron_pillar", () ->
            new RustablePillarBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_IRON_PILLAR = regWithItem("rusted_iron_pillar", () ->
            new RustablePillarBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_RED)));


    //corrugated iron
    public static final Supplier<Block> WHITE_CORRUGATED_IRON = regWithItem("white_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.WOOL)));
    public static final Supplier<Block> LIGHT_GRAY_CORRUGATED_IRON = regWithItem("light_gray_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final Supplier<Block> GRAY_CORRUGATED_IRON = regWithItem("gray_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> BLACK_CORRUGATED_IRON = regWithItem("black_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> BROWN_CORRUGATED_IRON = regWithItem("brown_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> RED_CORRUGATED_IRON = regWithItem("red_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_RED)));
    public static final Supplier<Block> ORANGE_CORRUGATED_IRON = regWithItem("orange_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_ORANGE)));
    public static final Supplier<Block> YELLOW_CORRUGATED_IRON = regWithItem("yellow_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final Supplier<Block> LIME_CORRUGATED_IRON = regWithItem("lime_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final Supplier<Block> GREEN_CORRUGATED_IRON = regWithItem("green_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)));
    public static final Supplier<Block> BLUE_CORRUGATED_IRON = regWithItem("blue_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLUE)));
    public static final Supplier<Block> PURPLE_CORRUGATED_IRON = regWithItem("purple_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_PURPLE)));
    public static final Supplier<Block> MAGENTA_CORRUGATED_IRON = regWithItem("magenta_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_MAGENTA)));
    public static final Supplier<Block> PINK_CORRUGATED_IRON = regWithItem("pink_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_PINK)));
    public static final Supplier<Block> LIGHT_BLUE_CORRUGATED_IRON = regWithItem("light_blue_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_BLUE)));
    public static final Supplier<Block> CYAN_CORRUGATED_IRON = regWithItem("cyan_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_CYAN)));

    public static final Supplier<Block> WEATHERED_WHITE_CORRUGATED_IRON = regWithItem("weathered_white_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final Supplier<Block> WEATHERED_LIGHT_GRAY_CORRUGATED_IRON = regWithItem("weathered_light_gray_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WEATHERED_GRAY_CORRUGATED_IRON = regWithItem("weathered_gray_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_BLACK_CORRUGATED_IRON = regWithItem("weathered_black_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BLACK)));
    public static final Supplier<Block> WEATHERED_BROWN_CORRUGATED_IRON = regWithItem("weathered_brown_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> WEATHERED_RED_CORRUGATED_IRON = regWithItem("weathered_red_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_RED)));
    public static final Supplier<Block> WEATHERED_ORANGE_CORRUGATED_IRON = regWithItem("weathered_orange_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));
    public static final Supplier<Block> WEATHERED_YELLOW_CORRUGATED_IRON = regWithItem("weathered_yellow_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> WEATHERED_LIME_CORRUGATED_IRON = regWithItem("weathered_lime_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));
    public static final Supplier<Block> WEATHERED_GREEN_CORRUGATED_IRON = regWithItem("weathered_green_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GREEN)));
    public static final Supplier<Block> WEATHERED_BLUE_CORRUGATED_IRON = regWithItem("weathered_blue_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)));
    public static final Supplier<Block> WEATHERED_PURPLE_CORRUGATED_IRON = regWithItem("weathered_purple_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_PURPLE)));
    public static final Supplier<Block> WEATHERED_MAGENTA_CORRUGATED_IRON = regWithItem("weathered_magenta_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_MAGENTA)));
    public static final Supplier<Block> WEATHERED_PINK_CORRUGATED_IRON = regWithItem("weathered_pink_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_PINK)));
    public static final Supplier<Block> WEATHERED_LIGHT_BLUE_CORRUGATED_IRON = regWithItem("weathered_light_blue_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)));
    public static final Supplier<Block> WEATHERED_CYAN_CORRUGATED_IRON = regWithItem("weathered_cyan_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN)));

    public static final Supplier<Block> RUSTED_CORRUGATED_IRON = regWithItem("rusted_corrugated_iron", () ->
            new RustableCorrugatedIronBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));


    public static final Supplier<Block> WHITE_CORRUGATED_IRON_STAIRS = regWithItem("white_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.WOOL)));
    public static final Supplier<Block> LIGHT_GRAY_CORRUGATED_IRON_STAIRS = regWithItem("light_gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final Supplier<Block> GRAY_CORRUGATED_IRON_STAIRS = regWithItem("gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> BLACK_CORRUGATED_IRON_STAIRS = regWithItem("black_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> BROWN_CORRUGATED_IRON_STAIRS = regWithItem("brown_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> RED_CORRUGATED_IRON_STAIRS = regWithItem("red_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_RED)));
    public static final Supplier<Block> ORANGE_CORRUGATED_IRON_STAIRS = regWithItem("orange_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_ORANGE)));
    public static final Supplier<Block> YELLOW_CORRUGATED_IRON_STAIRS = regWithItem("yellow_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final Supplier<Block> LIME_CORRUGATED_IRON_STAIRS = regWithItem("lime_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final Supplier<Block> GREEN_CORRUGATED_IRON_STAIRS = regWithItem("green_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)));
    public static final Supplier<Block> BLUE_CORRUGATED_IRON_STAIRS = regWithItem("blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLUE)));
    public static final Supplier<Block> PURPLE_CORRUGATED_IRON_STAIRS = regWithItem("purple_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_PURPLE)));
    public static final Supplier<Block> MAGENTA_CORRUGATED_IRON_STAIRS = regWithItem("magenta_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_MAGENTA)));
    public static final Supplier<Block> PINK_CORRUGATED_IRON_STAIRS = regWithItem("pink_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_PINK)));
    public static final Supplier<Block> LIGHT_BLUE_CORRUGATED_IRON_STAIRS = regWithItem("light_blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_BLUE)));
    public static final Supplier<Block> CYAN_CORRUGATED_IRON_STAIRS = regWithItem("cyan_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.CLEAN, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_CYAN)));

    public static final Supplier<Block> WEATHERED_WHITE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_white_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final Supplier<Block> WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS = regWithItem("weathered_light_gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WEATHERED_GRAY_CORRUGATED_IRON_STAIRS = regWithItem("weathered_gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_BLACK_CORRUGATED_IRON_STAIRS = regWithItem("weathered_black_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BLACK)));
    public static final Supplier<Block> WEATHERED_BROWN_CORRUGATED_IRON_STAIRS = regWithItem("weathered_brown_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> WEATHERED_RED_CORRUGATED_IRON_STAIRS = regWithItem("weathered_red_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_RED)));
    public static final Supplier<Block> WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_orange_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));
    public static final Supplier<Block> WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS = regWithItem("weathered_yellow_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> WEATHERED_LIME_CORRUGATED_IRON_STAIRS = regWithItem("weathered_lime_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));
    public static final Supplier<Block> WEATHERED_GREEN_CORRUGATED_IRON_STAIRS = regWithItem("weathered_green_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GREEN)));
    public static final Supplier<Block> WEATHERED_BLUE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)));
    public static final Supplier<Block> WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_purple_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_PURPLE)));
    public static final Supplier<Block> WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS = regWithItem("weathered_magenta_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_MAGENTA)));
    public static final Supplier<Block> WEATHERED_PINK_CORRUGATED_IRON_STAIRS = regWithItem("weathered_pink_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_PINK)));
    public static final Supplier<Block> WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_light_blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)));
    public static final Supplier<Block> WEATHERED_CYAN_CORRUGATED_IRON_STAIRS = regWithItem("weathered_cyan_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN)));

    public static final Supplier<Block> RUSTED_CORRUGATED_IRON_STAIRS = regWithItem("rusted_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.RUSTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> WHITE_CORRUGATED_IRON_SLAB = regWithItem("white_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.WOOL)));
    public static final Supplier<Block> LIGHT_GRAY_CORRUGATED_IRON_SLAB = regWithItem("light_gray_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final Supplier<Block> GRAY_CORRUGATED_IRON_SLAB = regWithItem("gray_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GRAY)));
    public static final Supplier<Block> BLACK_CORRUGATED_IRON_SLAB = regWithItem("black_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLACK)));
    public static final Supplier<Block> BROWN_CORRUGATED_IRON_SLAB = regWithItem("brown_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BROWN)));
    public static final Supplier<Block> RED_CORRUGATED_IRON_SLAB = regWithItem("red_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_RED)));
    public static final Supplier<Block> ORANGE_CORRUGATED_IRON_SLAB = regWithItem("orange_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_ORANGE)));
    public static final Supplier<Block> YELLOW_CORRUGATED_IRON_SLAB = regWithItem("yellow_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_YELLOW)));
    public static final Supplier<Block> LIME_CORRUGATED_IRON_SLAB = regWithItem("lime_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final Supplier<Block> GREEN_CORRUGATED_IRON_SLAB = regWithItem("green_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_GREEN)));
    public static final Supplier<Block> BLUE_CORRUGATED_IRON_SLAB = regWithItem("blue_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_BLUE)));
    public static final Supplier<Block> PURPLE_CORRUGATED_IRON_SLAB = regWithItem("purple_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_PURPLE)));
    public static final Supplier<Block> MAGENTA_CORRUGATED_IRON_SLAB = regWithItem("magenta_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_MAGENTA)));
    public static final Supplier<Block> PINK_CORRUGATED_IRON_SLAB = regWithItem("pink_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_PINK)));
    public static final Supplier<Block> LIGHT_BLUE_CORRUGATED_IRON_SLAB = regWithItem("light_blue_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_LIGHT_BLUE)));
    public static final Supplier<Block> CYAN_CORRUGATED_IRON_SLAB = regWithItem("cyan_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.CLEAN,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.COLOR_CYAN)));

    public static final Supplier<Block> WEATHERED_WHITE_CORRUGATED_IRON_SLAB = regWithItem("weathered_white_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final Supplier<Block> WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB = regWithItem("weathered_light_gray_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)));
    public static final Supplier<Block> WEATHERED_GRAY_CORRUGATED_IRON_SLAB = regWithItem("weathered_gray_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_BLACK_CORRUGATED_IRON_SLAB = regWithItem("weathered_black_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BLACK)));
    public static final Supplier<Block> WEATHERED_BROWN_CORRUGATED_IRON_SLAB = regWithItem("weathered_brown_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> WEATHERED_RED_CORRUGATED_IRON_SLAB = regWithItem("weathered_red_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_RED)));
    public static final Supplier<Block> WEATHERED_ORANGE_CORRUGATED_IRON_SLAB = regWithItem("weathered_orange_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));
    public static final Supplier<Block> WEATHERED_YELLOW_CORRUGATED_IRON_SLAB = regWithItem("weathered_yellow_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final Supplier<Block> WEATHERED_LIME_CORRUGATED_IRON_SLAB = regWithItem("weathered_lime_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)));
    public static final Supplier<Block> WEATHERED_GREEN_CORRUGATED_IRON_SLAB = regWithItem("weathered_green_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GREEN)));
    public static final Supplier<Block> WEATHERED_BLUE_CORRUGATED_IRON_SLAB = regWithItem("weathered_blue_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)));
    public static final Supplier<Block> WEATHERED_PURPLE_CORRUGATED_IRON_SLAB = regWithItem("weathered_purple_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_PURPLE)));
    public static final Supplier<Block> WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB = regWithItem("weathered_magenta_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_MAGENTA)));
    public static final Supplier<Block> WEATHERED_PINK_CORRUGATED_IRON_SLAB = regWithItem("weathered_pink_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_PINK)));
    public static final Supplier<Block> WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB = regWithItem("weathered_light_blue_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_LIGHT_BLUE)));
    public static final Supplier<Block> WEATHERED_CYAN_CORRUGATED_IRON_SLAB = regWithItem("weathered_cyan_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN)));

    public static final Supplier<Block> RUSTED_CORRUGATED_IRON_SLAB = regWithItem("rusted_corrugated_iron_slab", () ->
            new RustableRotatableSlabBlock(Rustable.RustLevel.RUSTED,BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_ORANGE)));

    //iron decor
    public static final Supplier<Block> HEAVY_IRON_DOOR = regWithItem("heavy_iron_door", () ->
            new RustableDoorBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_HEAVY_IRON_DOOR = regWithItem("weathered_heavy_iron_door", () ->
            new RustableDoorBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_HEAVY_IRON_DOOR = regWithItem("rusted_heavy_iron_door", () ->
            new RustableDoorBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_RED)));


    public static final Supplier<Block> HEAVY_IRON_TRAPDOOR = regWithItem("heavy_iron_trapdoor", () ->
            new RustableTrapdoorBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_HEAVY_IRON_TRAPDOOR = regWithItem("weathered_heavy_iron_trapdoor", () ->
            new RustableTrapdoorBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_HEAVY_IRON_TRAPDOOR = regWithItem("rusted_heavy_iron_trapdoor", () ->
            new RustableTrapdoorBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR).sound(SoundType.NETHERITE_BLOCK).mapColor(MapColor.TERRACOTTA_RED)));


    public static final Supplier<Block> HEAVY_IRON_CHAIN = regWithItem("heavy_iron_chain", () ->
            new RustableChainBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_HEAVY_IRON_CHAIN = regWithItem("weathered_heavy_iron_chain", () ->
            new RustableChainBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_HEAVY_IRON_CHAIN = regWithItem("rusted_heavy_iron_chain", () ->
            new RustableChainBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN).mapColor(MapColor.TERRACOTTA_RED)));

    public static final Supplier<Block> HEAVY_IRON_BARS = regWithItem("heavy_iron_bars", () ->
            new RustableBarsBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final Supplier<Block> WEATHERED_HEAVY_IRON_BARS = regWithItem("weathered_heavy_iron_bars", () ->
            new RustableBarsBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_HEAVY_IRON_BARS = regWithItem("rusted_heavy_iron_bars", () ->
            new RustableBarsBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_RED)));

    public static final Supplier<Block> WROUGHT_IRON_FENCE = regWithItem("wrought_iron_fence", () ->
            new RustableFenceBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_BLACK)));
    public static final Supplier<Block> WEATHERED_WROUGHT_IRON_FENCE = regWithItem("weathered_wrought_iron_fence", () ->
            new RustableFenceBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_WROUGHT_IRON_FENCE = regWithItem("rusted_wrought_iron_fence", () ->
            new RustableFenceBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_ORANGE)));

    public static final Supplier<Block> WROUGHT_IRON_FENCE_GATE = regWithItem("wrought_iron_fence_gate", () ->
            new RustableFenceGateBlock(Rustable.RustLevel.CLEAN, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_BLACK)));
    public static final Supplier<Block> WEATHERED_WROUGHT_IRON_FENCE_GATE = regWithItem("weathered_wrought_iron_fence_gate", () ->
            new RustableFenceGateBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final Supplier<Block> RUSTED_WROUGHT_IRON_FENCE_GATE = regWithItem("rusted_wrought_iron_fence_gate", () ->
            new RustableFenceGateBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BARS).sound(SoundType.COPPER).mapColor(MapColor.TERRACOTTA_ORANGE)));


    public static final Supplier<Block> CEMENT_EATER = regWithItem("cement_eater", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).mapColor(DyeColor.PURPLE)));
    public static final Supplier<Block> CIRNONAIL = regWithItem("cirnonail", () ->
            new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SLIME_BLOCK).mapColor(MapColor.DIAMOND)));


    private static BlockBehaviour.Properties noTick(Block ofFullCopyFrom){
        var p = BlockBehaviour.Properties.ofFullCopy(ofFullCopyFrom);
        p.isRandomlyTicking = false;
        return p;
    }
}
