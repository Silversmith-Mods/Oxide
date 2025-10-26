package com.ordana.oxide.reg;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockProperties {
    public static final BooleanProperty VARNISHED = BooleanProperty.create("varnished");
    public static final IntegerProperty OVERHANG = IntegerProperty.create("overhang", 0, 8);
}
