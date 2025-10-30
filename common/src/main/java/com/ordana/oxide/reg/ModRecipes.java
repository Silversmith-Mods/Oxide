package com.ordana.oxide.reg;

import com.ordana.oxide.Oxide;
import com.ordana.oxide.recipe.ChargeSprayerRecipe;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class ModRecipes {

    public static void init() {
    }

    public static final Supplier<RecipeSerializer<ChargeSprayerRecipe>> CHARGE_SPRAYER = RegHelper.registerRecipeSerializer(
            Oxide.res("charge_sprayer"), ChargeSprayerRecipe.Serializer::new);
}
