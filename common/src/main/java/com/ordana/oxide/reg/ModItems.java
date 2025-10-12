package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.items.CementBucketItem;
import com.ordana.oxide.items.PureNailItem;
import com.ordana.oxide.items.RustyNailItem;
import com.ordana.oxide.items.VarnishSprayer;
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
    public static final Supplier<Item> CEMENT_BUCKET = regItem("cement_bucket", () ->
            new CementBucketItem(new Item.Properties().component(ModComponents.CEMENT.get(), 128).stacksTo(1)));

    public static final Supplier<Item> VARNISH_SPRAYER = regItem("varnish_sprayer", () ->
            new VarnishSprayer(new Item.Properties().component(ModComponents.PRIMED.get(), false)
                    //dont initialize other component as it needs a level
                    .stacksTo(1)));

    public static final Supplier<Item> RUSTY_NAIL = regItem("rusty_nail", () ->
            new RustyNailItem(new Item.Properties().attributes(RustyNailItem.createAttributes()).component(DataComponents.TOOL, RustyNailItem.createToolProperties())));
    public static final Supplier<Item> PURE_NAIL = regItem("pure_nail", () ->
            new PureNailItem(new Item.Properties().attributes(PureNailItem.createAttributes()).component(DataComponents.TOOL, PureNailItem.createToolProperties())));
}