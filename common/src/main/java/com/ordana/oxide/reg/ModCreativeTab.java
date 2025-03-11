package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.configs.CommonConfigs;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModCreativeTab {

    public static final RegSupplier<CreativeModeTab> MOD_TAB =
            RegHelper.registerCreativeModeTab(Oxide.res("oxide"),
                    (c) -> c.title(Component.translatable("itemGroup.oxide.oxide"))
                            .icon(() -> ModBlocks.RUSTED_CUT_IRON.get().asItem().getDefaultInstance()));

    private static final Set<Item> HIDDEN_ITEMS = new HashSet<>();
    private static final List<ItemStack> NON_HIDDEN_ITEMS = new ArrayList<>();


    public static void init() {
        RegHelper.addItemsToTabsRegistration(ModCreativeTab::registerItemsToTabs);
    }

    private static boolean isRunningSetup = false;

    public static void setup() {
        isRunningSetup = true;
        List<Item> all = new ArrayList<>(BuiltInRegistries.ITEM.entrySet().stream().filter(e -> e.getKey().location().getNamespace()
                .equals(Oxide.MOD_ID)).map(Map.Entry::getValue).toList());
        Map<ResourceKey<CreativeModeTab>, List<ItemStack>> map = new HashMap<>();
        CreativeModeTabs.tabs().forEach(t -> map.putIfAbsent(BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(t).get(), new ArrayList<>()));
        var dummy = new RegHelper.ItemToTabEvent() {

            @Override
            public void addItems(ResourceKey<CreativeModeTab> resourceKey, @Nullable Predicate<ItemStack> predicate,
                                 boolean reverse, List<ItemStack> list) {
                var l = map.computeIfAbsent(resourceKey, t -> new ArrayList<>());
                if (reverse) {
                    var v = new ArrayList<>(list);
                    Collections.reverse(v);
                    l.addAll(v);
                } else l.addAll(list);
            }
        };
        registerItemsToTabs(dummy);
        for (var e : map.values()) {
            NON_HIDDEN_ITEMS.addAll(e);
        }

        for (var v : NON_HIDDEN_ITEMS) {
            all.remove(v.getItem());
        }
        HIDDEN_ITEMS.addAll(all);
        isRunningSetup = false;
    }

    public static void registerItemsToTabs(RegHelper.ItemToTabEvent e) {
        if (MOD_TAB != null && !isRunningSetup) {
            List<ItemStack> toAdd = new ArrayList<>();
            for (var i : NON_HIDDEN_ITEMS) {
                if (toAdd.stream().noneMatch(a -> ItemStack.isSameItemSameComponents(a, i))) {
                    toAdd.add(i);
                }
            }

            e.add(MOD_TAB.getHolder().unwrapKey().get(), toAdd.toArray(ItemStack[]::new));
            return;
        }

        after(e, Items.IRON_BLOCK, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.WET_CEMENT, ModBlocks.CEMENT, ModBlocks.CEMENT_STAIRS, ModBlocks.CEMENT_SLAB,

                ModItems.RUSTY_NAIL,
                ModBlocks.PLATE_IRON, ModBlocks.PLATE_IRON_STAIRS, ModBlocks.PLATE_IRON_SLAB,
                ModBlocks.WEATHERED_PLATE_IRON, ModBlocks.WEATHERED_PLATE_IRON_STAIRS, ModBlocks.WEATHERED_PLATE_IRON_SLAB,
                ModBlocks.RUSTED_PLATE_IRON, ModBlocks.RUSTED_PLATE_IRON_STAIRS, ModBlocks.RUSTED_PLATE_IRON_SLAB,

                ModBlocks.CUT_IRON, ModBlocks.CUT_IRON_STAIRS, ModBlocks.CUT_IRON_SLAB,
                ModBlocks.WEATHERED_CUT_IRON, ModBlocks.WEATHERED_CUT_IRON_STAIRS, ModBlocks.WEATHERED_CUT_IRON_SLAB,
                ModBlocks.RUSTED_CUT_IRON, ModBlocks.RUSTED_CUT_IRON_STAIRS, ModBlocks.RUSTED_CUT_IRON_SLAB,

                ModBlocks.WHITE_CORRUGATED_IRON, ModBlocks.LIGHT_GRAY_CORRUGATED_IRON, ModBlocks.GRAY_CORRUGATED_IRON, ModBlocks.BLACK_CORRUGATED_IRON,ModBlocks.BROWN_CORRUGATED_IRON,ModBlocks.RED_CORRUGATED_IRON,ModBlocks.ORANGE_CORRUGATED_IRON,ModBlocks.YELLOW_CORRUGATED_IRON,ModBlocks.LIME_CORRUGATED_IRON,ModBlocks.GREEN_CORRUGATED_IRON,ModBlocks.CYAN_CORRUGATED_IRON,ModBlocks.LIGHT_BLUE_CORRUGATED_IRON,ModBlocks.BLUE_CORRUGATED_IRON,ModBlocks.PURPLE_CORRUGATED_IRON,ModBlocks.MAGENTA_CORRUGATED_IRON,ModBlocks.PINK_CORRUGATED_IRON,
                ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON, ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON, ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON, ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON,ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON,ModBlocks.WEATHERED_RED_CORRUGATED_IRON,ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON,ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON,ModBlocks.WEATHERED_LIME_CORRUGATED_IRON,ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON,ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON,ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON,ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON,ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON,ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON,ModBlocks.WEATHERED_PINK_CORRUGATED_IRON,
                ModBlocks.RUSTED_CORRUGATED_IRON,

                ModBlocks.WHITE_CORRUGATED_IRON_STAIRS, ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_STAIRS, ModBlocks.GRAY_CORRUGATED_IRON_STAIRS, ModBlocks.BLACK_CORRUGATED_IRON_STAIRS,ModBlocks.BROWN_CORRUGATED_IRON_STAIRS,ModBlocks.RED_CORRUGATED_IRON_STAIRS,ModBlocks.ORANGE_CORRUGATED_IRON_STAIRS,ModBlocks.YELLOW_CORRUGATED_IRON_STAIRS,ModBlocks.LIME_CORRUGATED_IRON_STAIRS,ModBlocks.GREEN_CORRUGATED_IRON_STAIRS,ModBlocks.CYAN_CORRUGATED_IRON_STAIRS,ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_STAIRS,ModBlocks.BLUE_CORRUGATED_IRON_STAIRS,ModBlocks.PURPLE_CORRUGATED_IRON_STAIRS,ModBlocks.MAGENTA_CORRUGATED_IRON_STAIRS,ModBlocks.PINK_CORRUGATED_IRON_STAIRS,
                ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_STAIRS, ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_STAIRS, ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_STAIRS, ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_RED_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_STAIRS,ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_STAIRS,
                ModBlocks.RUSTED_CORRUGATED_IRON_STAIRS,

                ModBlocks.WHITE_CORRUGATED_IRON_SLAB, ModBlocks.LIGHT_GRAY_CORRUGATED_IRON_SLAB, ModBlocks.GRAY_CORRUGATED_IRON_SLAB, ModBlocks.BLACK_CORRUGATED_IRON_SLAB,ModBlocks.BROWN_CORRUGATED_IRON_SLAB,ModBlocks.RED_CORRUGATED_IRON_SLAB,ModBlocks.ORANGE_CORRUGATED_IRON_SLAB,ModBlocks.YELLOW_CORRUGATED_IRON_SLAB,ModBlocks.LIME_CORRUGATED_IRON_SLAB,ModBlocks.GREEN_CORRUGATED_IRON_SLAB,ModBlocks.CYAN_CORRUGATED_IRON_SLAB,ModBlocks.LIGHT_BLUE_CORRUGATED_IRON_SLAB,ModBlocks.BLUE_CORRUGATED_IRON_SLAB,ModBlocks.PURPLE_CORRUGATED_IRON_SLAB,ModBlocks.MAGENTA_CORRUGATED_IRON_SLAB,ModBlocks.PINK_CORRUGATED_IRON_SLAB,
                ModBlocks.WEATHERED_WHITE_CORRUGATED_IRON_SLAB, ModBlocks.WEATHERED_LIGHT_GRAY_CORRUGATED_IRON_SLAB, ModBlocks.WEATHERED_GRAY_CORRUGATED_IRON_SLAB, ModBlocks.WEATHERED_BLACK_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_BROWN_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_RED_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_ORANGE_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_YELLOW_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_LIME_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_GREEN_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_CYAN_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_LIGHT_BLUE_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_BLUE_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_PURPLE_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_MAGENTA_CORRUGATED_IRON_SLAB,ModBlocks.WEATHERED_PINK_CORRUGATED_IRON_SLAB,
                ModBlocks.RUSTED_CORRUGATED_IRON_SLAB,

                ModBlocks.IRON_SCAFFOLD, ModBlocks.IRON_SCAFFOLD_STAIRS, ModBlocks.IRON_SCAFFOLD_SLAB,
                ModBlocks.WEATHERED_IRON_SCAFFOLD, ModBlocks.WEATHERED_IRON_SCAFFOLD_STAIRS, ModBlocks.WEATHERED_IRON_SCAFFOLD_SLAB,
                ModBlocks.RUSTED_IRON_SCAFFOLD, ModBlocks.RUSTED_IRON_SCAFFOLD_STAIRS, ModBlocks.RUSTED_IRON_SCAFFOLD_SLAB
        );

        after(e, Items.IRON_BARS, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.WEATHERED_IRON_BARS, ModBlocks.RUSTED_IRON_BARS
        );

        after(e, Items.IRON_DOOR, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.HEAVY_IRON_DOOR, ModBlocks.WEATHERED_HEAVY_IRON_DOOR, ModBlocks.RUSTED_HEAVY_IRON_DOOR
        );

        after(e, Items.IRON_TRAPDOOR, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.HEAVY_IRON_TRAPDOOR, ModBlocks.WEATHERED_HEAVY_IRON_TRAPDOOR, ModBlocks.RUSTED_HEAVY_IRON_TRAPDOOR
        );

        after(e, Items.IRON_TRAPDOOR, CreativeModeTabs.BUILDING_BLOCKS,
                ModBlocks.HEAVY_IRON_CHAIN, ModBlocks.WEATHERED_HEAVY_IRON_CHAIN, ModBlocks.RUSTED_HEAVY_IRON_CHAIN
        );
    }

    private static void after(RegHelper.ItemToTabEvent event, Item target,
                              ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        after(event, i -> i.is(target), tab, items);
    }

    private static void after(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred,
                              ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        if (items[0].get() instanceof ItemStack) {
            ItemStack[] entries = Arrays.stream(items).map(s -> (ItemStack) s.get()).toArray(ItemStack[]::new);
            event.addAfter(tab, targetPred, entries);
        } else {
            ItemLike[] entries = Arrays.stream(items).map((s -> (ItemLike) (s.get()))).toArray(ItemLike[]::new);
            event.addAfter(tab, targetPred, entries);
        }
    }

    private static void before(RegHelper.ItemToTabEvent event, Item target,
                               ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        before(event, i -> i.is(target), tab, items);
    }

    private static void before(RegHelper.ItemToTabEvent event, Predicate<ItemStack> targetPred,
                               ResourceKey<CreativeModeTab> tab, Supplier<?>... items) {
        if (items[0].get() instanceof ItemStack) {
            ItemStack[] entries = Arrays.stream(items).map(s -> (ItemStack) s.get()).toArray(ItemStack[]::new);
            event.addBefore(tab, targetPred, entries);
        } else {
            ItemLike[] entries = Arrays.stream(items).map(s -> (ItemLike) s.get()).toArray(ItemLike[]::new);
            event.addBefore(tab, targetPred, entries);
        }
    }
}
