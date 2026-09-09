package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.items.*;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {


    public static void init() {
    }

    public static <T extends Item> Supplier<T> regItem(String name, Function<Item.Properties, T> factory) {
        return RegHelper.registerItem(Oxide.res(name), factory);
    }

    public static final Supplier<Item> CEMENT_POWDER_BUCKET = regItem("cement_powder_bucket", (properties) ->
            new CementPowderBucketItem(properties.stacksTo(1)));
    public static final Supplier<Item> CEMENT_BUCKET = regItem("cement_bucket", (properties) ->
            new CementBucketItem(properties.component(ModComponents.CEMENT.get(), 128).stacksTo(1)));

    public static final Supplier<Item> VARNISH_SPRAYER = regItem("varnish_sprayer", (properties) ->
            new VarnishSprayer(properties.component(ModComponents.MAX_DROPS.get(), 128)
                    .delayedComponent(ModComponents.FLUID.get(), (level)->SFStackView.of(SoftFluidStack.empty(level)))
                    .stacksTo(1)));

    public static final Supplier<Item> RUSTY_NAIL = regItem("rusty_nail", (properties) ->
            new RustyNailItem(properties.attributes(RustyNailItem.createAttributes()).component(DataComponents.TOOL, RustyNailItem.createToolProperties())));
    public static final Supplier<Item> PURE_NAIL = regItem("pure_nail", (properties) ->
            new PureNailItem(properties.attributes(PureNailItem.createAttributes()).component(DataComponents.TOOL, PureNailItem.createToolProperties())));
}