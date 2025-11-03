package com.ordana.oxide.reg;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockProperties {
    public static final BooleanProperty CURABLE = BooleanProperty.create("curable");
    public static final BooleanProperty VARNISHED = BooleanProperty.create("varnished");
    public static final IntegerProperty OVERHANG = IntegerProperty.create("overhang", 0, 8);
    public static final BooleanProperty NORTH_UPPER = BooleanProperty.create("north_upper");
    public static final BooleanProperty SOUTH_UPPER = BooleanProperty.create("south_upper");
    public static final BooleanProperty EAST_UPPER = BooleanProperty.create("east_upper");
    public static final BooleanProperty WEST_UPPER = BooleanProperty.create("west_upper");
}
