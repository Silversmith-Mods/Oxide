package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.items.PureNailItem;
import com.ordana.oxide.items.RustyNailItem;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ModItems {


    public static void init() {
    }

    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> itemSup) {
        return RegHelper.registerItem(Oxide.res(name), itemSup);
    }

    public static final Supplier<Item> RUSTY_NAIL = regItem("rusty_nail", () ->
            new RustyNailItem(new Item.Properties().attributes(RustyNailItem.createAttributes()).component(DataComponents.TOOL, RustyNailItem.createToolProperties())));
    public static final Supplier<Item> PURE_NAIL = regItem("pure_nail", () ->
            new PureNailItem(new Item.Properties().attributes(RustyNailItem.createAttributes()).component(DataComponents.TOOL, RustyNailItem.createToolProperties())));
}
