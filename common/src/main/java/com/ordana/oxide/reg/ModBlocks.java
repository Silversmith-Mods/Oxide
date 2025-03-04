package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.blocks.WetCementBlock;
import com.ordana.oxide.blocks.rusty.*;
import net.mehvahdjukaar.moonlight.api.block.ModStairBlock;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

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


    public static final Supplier<Block> WET_CEMENT = regWithItem("wet_cement", () ->
            new WetCementBlock(BlockBehaviour.Properties.of().sound(SoundType.MUD).randomTicks().isSuffocating(ModBlocks::always)));

    public static final Supplier<Block> CEMENT = regWithItem("cement", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> CEMENT_STAIRS = regWithItem("cement_stairs", () ->
            new ModStairBlock(ModBlocks.CEMENT, BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));
    public static final Supplier<Block> CEMENT_SLAB = regWithItem("cement_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)));



    //plate iron
    public static final Supplier<Block> PLATE_IRON = regWithItem("plate_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PLATE_IRON = regWithItem("weathered_plate_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_PLATE_IRON = regWithItem("rusted_plate_iron", () ->
            new RustableBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> PLATE_IRON_STAIRS = regWithItem("plate_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, PLATE_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PLATE_IRON_STAIRS = regWithItem("weathered_plate_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, PLATE_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_PLATE_IRON_STAIRS = regWithItem("rusted_plate_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.RUSTED, PLATE_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> PLATE_IRON_SLAB = regWithItem("plate_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PLATE_IRON_SLAB = regWithItem("weathered_plate_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_PLATE_IRON_SLAB = regWithItem("rusted_plate_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    //waxed
    public static final Supplier<Block> WAXED_PLATE_IRON = regWithItem("waxed_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_WEATHERED_PLATE_IRON = regWithItem("waxed_weathered_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_RUSTED_PLATE_IRON = regWithItem("waxed_rusted_plate_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));

    public static final Supplier<Block> WAXED_PLATE_IRON_STAIRS = regWithItem("waxed_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_WEATHERED_PLATE_IRON_STAIRS = regWithItem("waxed_weathered_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_WEATHERED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_RUSTED_PLATE_IRON_STAIRS = regWithItem("waxed_rusted_plate_iron_stairs", () ->
            new ModStairBlock(WAXED_RUSTED_PLATE_IRON, noTick(Blocks.IRON_BLOCK)));

    public static final Supplier<Block> WAXED_PLATE_IRON_SLAB = regWithItem("waxed_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_WEATHERED_PLATE_IRON_SLAB = regWithItem("waxed_weathered_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_RUSTED_PLATE_IRON_SLAB = regWithItem("waxed_rusted_plate_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));


    //cut iron
    public static final Supplier<Block> CUT_IRON = regWithItem("cut_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_CUT_IRON = regWithItem("weathered_cut_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_CUT_IRON = regWithItem("rusted_cut_iron", () ->
            new RustableBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> CUT_IRON_STAIRS = regWithItem("cut_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, CUT_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_CUT_IRON_STAIRS = regWithItem("weathered_cut_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, CUT_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_CUT_IRON_STAIRS = regWithItem("rusted_cut_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.RUSTED, CUT_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> CUT_IRON_SLAB = regWithItem("cut_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_CUT_IRON_SLAB = regWithItem("weathered_cut_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_CUT_IRON_SLAB = regWithItem("rusted_cut_iron_slab", () ->
            new RustableSlabBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    //waxed
    public static final Supplier<Block> WAXED_CUT_IRON = regWithItem("waxed_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_WEATHERED_CUT_IRON = regWithItem("waxed_weathered_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_RUSTED_CUT_IRON = regWithItem("waxed_rusted_cut_iron", () ->
            new Block(noTick(Blocks.IRON_BLOCK)));

    public static final Supplier<Block> WAXED_CUT_IRON_STAIRS = regWithItem("waxed_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_WEATHERED_CUT_IRON_STAIRS = regWithItem("waxed_weathered_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_WEATHERED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_RUSTED_CUT_IRON_STAIRS = regWithItem("waxed_rusted_cut_iron_stairs", () ->
            new ModStairBlock(WAXED_RUSTED_CUT_IRON, noTick(Blocks.IRON_BLOCK)));

    public static final Supplier<Block> WAXED_CUT_IRON_SLAB = regWithItem("waxed_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_WEATHERED_CUT_IRON_SLAB = regWithItem("waxed_weathered_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));
    public static final Supplier<Block> WAXED_RUSTED_CUT_IRON_SLAB = regWithItem("waxed_rusted_cut_iron_slab", () ->
            new SlabBlock(noTick(Blocks.IRON_BLOCK)));


    public static final Supplier<Block> IRON_SCAFFOLD = regWithItem("iron_scaffold", () ->
            new RustableScaffoldBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_IRON_SCAFFOLD = regWithItem("weathered_iron_scaffold", () ->
            new RustableScaffoldBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_IRON_SCAFFOLD = regWithItem("rusted_iron_scaffold", () ->
            new RustableScaffoldBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> IRON_SCAFFOLD_STAIRS = regWithItem("iron_scaffold_stairs", () ->
            new RustableScaffoldStairsBlock(Rustable.RustLevel.UNAFFECTED, IRON_SCAFFOLD, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_IRON_SCAFFOLD_STAIRS = regWithItem("weathered_iron_scaffold_stairs", () ->
            new RustableScaffoldStairsBlock(Rustable.RustLevel.WEATHERED, IRON_SCAFFOLD, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_IRON_SCAFFOLD_STAIRS = regWithItem("rusted_iron_scaffold_stairs", () ->
            new RustableScaffoldStairsBlock(Rustable.RustLevel.RUSTED, IRON_SCAFFOLD, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> IRON_SCAFFOLD_SLAB = regWithItem("iron_scaffold_slab", () ->
            new RustableScaffoldSlabBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_IRON_SCAFFOLD_SLAB = regWithItem("weathered_iron_scaffold_slab", () ->
            new RustableScaffoldSlabBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RUSTED_IRON_SCAFFOLD_SLAB = regWithItem("rusted_iron_scaffold_slab", () ->
            new RustableScaffoldSlabBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.GLASS).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));

    //corrugated iron
    public static final Supplier<Block> WHITE_CORRUGATED_IRON = regWithItem("white_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIGHT_GRAY_CORRUGATED_IRON = regWithItem("light_gray_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> GRAY_CORRUGATED_IRON = regWithItem("gray_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BLACK_CORRUGATED_IRON = regWithItem("black_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BROWN_CORRUGATED_IRON = regWithItem("brown_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RED_CORRUGATED_IRON = regWithItem("red_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> ORANGE_CORRUGATED_IRON = regWithItem("orange_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> YELLOW_CORRUGATED_IRON = regWithItem("yellow_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIME_CORRUGATED_IRON = regWithItem("lime_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> GREEN_CORRUGATED_IRON = regWithItem("green_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BLUE_CORRUGATED_IRON = regWithItem("blue_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> PURPLE_CORRUGATED_IRON = regWithItem("purple_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> MAGENTA_CORRUGATED_IRON = regWithItem("magenta_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> PINK_CORRUGATED_IRON = regWithItem("pink_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIGHT_BLUE_CORRUGATED_IRON = regWithItem("light_blue_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> CYAN_CORRUGATED_IRON = regWithItem("cyan_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> WEATHERED_WHITE_CORRUGATED_IRON = regWithItem("weathered_white_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIGHT_GRAY_CORRUGATED_IRON = regWithItem("weathered_light_gray_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_GRAY_CORRUGATED_IRON = regWithItem("weathered_gray_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BLACK_CORRUGATED_IRON = regWithItem("weathered_black_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BROWN_CORRUGATED_IRON = regWithItem("weathered_brown_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_RED_CORRUGATED_IRON = regWithItem("weathered_red_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_ORANGE_CORRUGATED_IRON = regWithItem("weathered_orange_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_YELLOW_CORRUGATED_IRON = regWithItem("weathered_yellow_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIME_CORRUGATED_IRON = regWithItem("weathered_lime_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_GREEN_CORRUGATED_IRON = regWithItem("weathered_green_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BLUE_CORRUGATED_IRON = regWithItem("weathered_blue_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PURPLE_CORRUGATED_IRON = regWithItem("weathered_purple_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_MAGENTA_CORRUGATED_IRON = regWithItem("weathered_magenta_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PINK_CORRUGATED_IRON = regWithItem("weathered_pink_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIGHT_BLUE_CORRUGATED_IRON = regWithItem("weathered_light_blue_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_CYAN_CORRUGATED_IRON = regWithItem("weathered_cyan_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> RUSTED_CORRUGATED_IRON = regWithItem("rusted_corrugated_iron", () ->
            new RustableBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));



    public static final Supplier<Block> WHITE_CORRUGATED_IRON_STAIRS = regWithItem("white_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIGHT_GRAY_CORRUGATED_IRON_STAIRS = regWithItem("light_gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> GRAY_CORRUGATED_IRON_STAIRS = regWithItem("gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BLACK_CORRUGATED_IRON_STAIRS = regWithItem("black_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BROWN_CORRUGATED_IRON_STAIRS = regWithItem("brown_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RED_CORRUGATED_IRON_STAIRS = regWithItem("red_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> ORANGE_CORRUGATED_IRON_STAIRS = regWithItem("orange_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> YELLOW_CORRUGATED_IRON_STAIRS = regWithItem("yellow_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIME_CORRUGATED_IRON_STAIRS = regWithItem("lime_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> GREEN_CORRUGATED_IRON_STAIRS = regWithItem("green_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BLUE_CORRUGATED_IRON_STAIRS = regWithItem("blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> PURPLE_CORRUGATED_IRON_STAIRS = regWithItem("purple_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> MAGENTA_CORRUGATED_IRON_STAIRS = regWithItem("magenta_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> PINK_CORRUGATED_IRON_STAIRS = regWithItem("pink_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIGHT_BLUE_CORRUGATED_IRON_STAIRS = regWithItem("light_blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> CYAN_CORRUGATED_IRON_STAIRS = regWithItem("cyan_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.UNAFFECTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> WEATHERED_WHITE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_white_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS = regWithItem("weathered_light_gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_GRAY_CORRUGATED_IRON_STAIRS = regWithItem("weathered_gray_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BLACK_CORRUGATED_IRON_STAIRS = regWithItem("weathered_black_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BROWN_CORRUGATED_IRON_STAIRS = regWithItem("weathered_brown_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_RED_CORRUGATED_IRON_STAIRS = regWithItem("weathered_red_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_orange_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS = regWithItem("weathered_yellow_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIME_CORRUGATED_IRON_STAIRS = regWithItem("weathered_lime_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_GREEN_CORRUGATED_IRON_STAIRS = regWithItem("weathered_green_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BLUE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_purple_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS = regWithItem("weathered_magenta_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PINK_CORRUGATED_IRON_STAIRS = regWithItem("weathered_pink_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS = regWithItem("weathered_light_blue_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_CYAN_CORRUGATED_IRON_STAIRS = regWithItem("weathered_cyan_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.WEATHERED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> RUSTED_CORRUGATED_IRON_STAIRS = regWithItem("rusted_corrugated_iron_stairs", () ->
            new RustableStairsBlock(Rustable.RustLevel.RUSTED, WHITE_CORRUGATED_IRON, BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));



    public static final Supplier<Block> WHITE_CORRUGATED_IRON_SLAB = regWithItem("white_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIGHT_GRAY_CORRUGATED_IRON_SLAB = regWithItem("light_gray_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> GRAY_CORRUGATED_IRON_SLAB = regWithItem("gray_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BLACK_CORRUGATED_IRON_SLAB = regWithItem("black_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BROWN_CORRUGATED_IRON_SLAB = regWithItem("brown_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> RED_CORRUGATED_IRON_SLAB = regWithItem("red_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> ORANGE_CORRUGATED_IRON_SLAB = regWithItem("orange_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> YELLOW_CORRUGATED_IRON_SLAB = regWithItem("yellow_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIME_CORRUGATED_IRON_SLAB = regWithItem("lime_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> GREEN_CORRUGATED_IRON_SLAB = regWithItem("green_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> BLUE_CORRUGATED_IRON_SLAB = regWithItem("blue_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> PURPLE_CORRUGATED_IRON_SLAB = regWithItem("purple_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> MAGENTA_CORRUGATED_IRON_SLAB = regWithItem("magenta_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> PINK_CORRUGATED_IRON_SLAB = regWithItem("pink_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> LIGHT_BLUE_CORRUGATED_IRON_SLAB = regWithItem("light_blue_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> CYAN_CORRUGATED_IRON_SLAB = regWithItem("cyan_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.UNAFFECTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> WEATHERED_WHITE_CORRUGATED_IRON_SLAB = regWithItem("weathered_white_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB = regWithItem("weathered_light_gray_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_GRAY_CORRUGATED_IRON_SLAB = regWithItem("weathered_gray_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BLACK_CORRUGATED_IRON_SLAB = regWithItem("weathered_black_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BROWN_CORRUGATED_IRON_SLAB = regWithItem("weathered_brown_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_RED_CORRUGATED_IRON_SLAB = regWithItem("weathered_red_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_ORANGE_CORRUGATED_IRON_SLAB = regWithItem("weathered_orange_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_YELLOW_CORRUGATED_IRON_SLAB = regWithItem("weathered_yellow_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIME_CORRUGATED_IRON_SLAB = regWithItem("weathered_lime_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_GREEN_CORRUGATED_IRON_SLAB = regWithItem("weathered_green_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_BLUE_CORRUGATED_IRON_SLAB = regWithItem("weathered_blue_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PURPLE_CORRUGATED_IRON_SLAB = regWithItem("weathered_purple_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB = regWithItem("weathered_magenta_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_PINK_CORRUGATED_IRON_SLAB = regWithItem("weathered_pink_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB = regWithItem("weathered_light_blue_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    public static final Supplier<Block> WEATHERED_CYAN_CORRUGATED_IRON_SLAB = regWithItem("weathered_cyan_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.WEATHERED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));

    public static final Supplier<Block> RUSTED_CORRUGATED_IRON_SLAB = regWithItem("rusted_corrugated_iron_slab", () ->
            new RotatableSlabBlock(Rustable.RustLevel.RUSTED,BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.NETHERITE_BLOCK)));
    
    

    //iron decor

    public static final Supplier<Block> HEAVY_IRON_DOOR = regWithItem("heavy_iron_door", () ->
            new RustableDoorBlock(Rustable.RustLevel.UNAFFECTED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)));

    public static final Supplier<Block> WEATHERED_HEAVY_IRON_DOOR = regWithItem("weathered_heavy_iron_door", () ->
            new RustableDoorBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)));
    public static final Supplier<Block> RUSTED_HEAVY_IRON_DOOR = regWithItem("rusted_heavy_iron_door", () ->
            new RustableDoorBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_DOOR)));

    public static final Supplier<Block> WEATHERED_IRON_TRAPDOOR = regWithItem("weathered_iron_trapdoor", () ->
            new RustableTrapdoorBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_TRAPDOOR)));
    public static final Supplier<Block> RUSTED_IRON_TRAPDOOR = regWithItem("rusted_iron_trapdoor", () ->
            new RustableTrapdoorBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_TRAPDOOR)));

    public static final Supplier<Block> WEATHERED_IRON_BARS = regWithItem("weathered_iron_bars", () ->
            new RustableBarsBlock(Rustable.RustLevel.WEATHERED, BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
    public static final Supplier<Block> RUSTED_IRON_BARS = regWithItem("rusted_iron_bars", () ->
            new RustableBarsBlock(Rustable.RustLevel.RUSTED, BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));

    //waxed
    public static final Supplier<Block> WAXED_HEAVY_IRON_DOOR = regWithItem("waxed_heavy_iron_door", () ->
            new RustAffectedDoorBlock(Rustable.RustLevel.UNAFFECTED, noTick(Blocks.IRON_DOOR)));
    public static final Supplier<Block> WAXED_WEATHERED_HEAVY_IRON_DOOR = regWithItem("waxed_weathered_heavy_iron_door", () ->
            new RustAffectedDoorBlock(Rustable.RustLevel.WEATHERED, noTick(Blocks.IRON_DOOR)));
    public static final Supplier<Block> WAXED_RUSTED_HEAVY_IRON_DOOR = regWithItem("waxed_rusted_heavy_iron_door", () ->
            new RustAffectedDoorBlock(Rustable.RustLevel.RUSTED, noTick(Blocks.IRON_DOOR)));

    public static final Supplier<Block> WAXED_IRON_TRAPDOOR = regWithItem("waxed_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(Rustable.RustLevel.UNAFFECTED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));
    public static final Supplier<Block> WAXED_WEATHERED_IRON_TRAPDOOR = regWithItem("waxed_weathered_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(Rustable.RustLevel.WEATHERED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));
    public static final Supplier<Block> WAXED_RUSTED_IRON_TRAPDOOR = regWithItem("waxed_rusted_iron_trapdoor", () ->
            new RustAffectedTrapdoorBlock(Rustable.RustLevel.RUSTED, noTick(Blocks.IRON_TRAPDOOR), BlockSetType.IRON));

    public static final Supplier<Block> WAXED_IRON_BARS = regWithItem("waxed_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});
    public static final Supplier<Block> WAXED_WEATHERED_IRON_BARS = regWithItem("waxed_weathered_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});
    public static final Supplier<Block> WAXED_RUSTED_IRON_BARS = regWithItem("waxed_rusted_iron_bars", () ->
            new IronBarsBlock(noTick(Blocks.IRON_BARS)) {});


    private static BlockBehaviour.Properties noTick(Block copyFrom){
        var p = BlockBehaviour.Properties.copy(copyFrom);
        p.isRandomlyTicking = false;
        return p;
    }
}
