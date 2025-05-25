package com.ordana.oxide.reg;

import com.mojang.serialization.Codec;
import com.ordana.oxide.Oxide;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModComponents {
    public static void init() {
    }

    public static final Supplier<DataComponentType<Integer>> CEMENT = register("cement",
            ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT);


    public static final Supplier<DataComponentType<Integer>> VARNISH = register("varnish",
            ExtraCodecs.POSITIVE_INT, ByteBufCodecs.VAR_INT);




    public static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        return register(name, codec, streamCodec, false);
    }


    public static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec,
                                                              @Nullable StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec,
                                                              boolean cache) {
        return RegHelper.registerDataComponent(Oxide.res(name), () -> {
            var builder = DataComponentType.<T>builder()
                    .persistent(codec);
            if (streamCodec != null) builder.networkSynchronized(streamCodec);
            if (cache) builder.cacheEncoding();
            return builder.build();
        });
    }
}
