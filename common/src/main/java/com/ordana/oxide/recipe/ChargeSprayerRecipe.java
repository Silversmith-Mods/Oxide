package com.ordana.oxide.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ordana.oxide.items.SFStackView;
import com.ordana.oxide.items.VarnishSprayer;
import com.ordana.oxide.reg.ModComponents;
import com.ordana.oxide.reg.ModRecipes;
import com.ordana.oxide.reg.ModTags;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ChargeSprayerRecipe extends CustomRecipe {

    private final Ingredient sprayerIngredient;
    private final int chargesPerBottle;
    private final boolean canOverflow;

    public ChargeSprayerRecipe(CraftingBookCategory category, Ingredient arrow,
                               int chargesPerItem, boolean canOverflow) {
        super(category);
        this.sprayerIngredient = arrow;
        this.chargesPerBottle = chargesPerItem;
        this.canOverflow = canOverflow;
    }

    private int getBottlesToAdd(ItemStack sprayer, ItemStack charge, Level level) {
        RegistryAccess ra = level.registryAccess();
        SFStackView sprayerContent = VarnishSprayer.getFluidComponent(sprayer, ra);
        var bottleContent = SoftFluidStack.fromItem(charge, ra);
        if (bottleContent == null) return 0;
        SoftFluidStack bottleFluid = bottleContent.getFirst();
        if (bottleFluid.isEmpty()) return 0;
        if (!bottleFluid.is(ModTags.CAN_GO_IN_SPRAY)) return 0;
        if (sprayerContent.isEmpty() || sprayerContent.sameFluidSameComponents(bottleFluid)) {
            return bottleFluid.getCount();
        }
        return 0;
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {

        ItemStack sprayer = null;
        ItemStack fluidBottleItem = null;
        int newTotalCharges = 0;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (sprayerIngredient.test(stack)) {
                if (sprayer != null) {
                    return false;
                }
                sprayer = stack;
            } else if (!stack.isEmpty()) {
                if (fluidBottleItem != null) return false;
                fluidBottleItem = stack;
            }
        }
        int bottlesToAdd = getBottlesToAdd(sprayer, fluidBottleItem, worldIn);
        if (bottlesToAdd == 0) return false;
        newTotalCharges += chargesPerBottle * bottlesToAdd;

        return sprayer != null && fluidBottleItem != null && (canOverflow || newTotalCharges <=
                sprayer.getOrDefault(ModComponents.MAX_DROPS.get(), 0));
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider access) {
        int newTotalCharges = 0;
        ItemStack arrow = null;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (sprayerIngredient.test(stack)) {
                arrow = stack;
            }
        }

        int maxCharges = arrow.getOrDefault(ModComponents.MAX_DROPS.get(), 0);
        ItemStack returnSpray = arrow.copy();
        SoftFluidStack sf = VarnishSprayer.getFluidComponent(returnSpray, access)
                .toMutable();
        sf.setCount(Math.min(maxCharges, sf.getCount() + newTotalCharges));
        VarnishSprayer.setFluidComponent(returnSpray, sf);

        return returnSpray;

    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CHARGE_SPRAYER.get();
    }

    public static class Serializer implements RecipeSerializer<ChargeSprayerRecipe> {

        private static final MapCodec<ChargeSprayerRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CraftingRecipe::category),
                Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.sprayerIngredient),
                Codec.INT.optionalFieldOf("charges_per_item", 1).forGetter((recipe) -> recipe.chargesPerBottle),
                Codec.BOOL.optionalFieldOf("can_overfill", false).forGetter((recipe) -> recipe.canOverflow)
        ).apply(instance, ChargeSprayerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ChargeSprayerRecipe> STREAM_CODEC = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, CraftingRecipe::category,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.sprayerIngredient,
                ByteBufCodecs.VAR_INT, recipe -> recipe.chargesPerBottle,
                ByteBufCodecs.BOOL, recipe -> recipe.canOverflow,
                ChargeSprayerRecipe::new);

        @Override
        public MapCodec<ChargeSprayerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChargeSprayerRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
