package com.ordana.oxide.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class ChargeSprayerRecipe extends CustomRecipe {

    private final Ingredient targetItem;
    private final int chargesPerItem;
    private final boolean canOverflow;

    public ChargeSprayerRecipe(CraftingBookCategory category, Ingredient arrow,
                               int chargesPerItem, boolean canOverflow) {
        super(category);
        this.targetItem = arrow;
        this.chargesPerItem = chargesPerItem;
        this.canOverflow = canOverflow;
    }

    @Override
    public boolean matches(CraftingInput inv, Level worldIn) {

        ItemStack arrow = null;
        ItemStack rope = null;
        int newTotalCharges = 0;

        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (targetItem.test(stack)) {
                if (arrow != null) {
                    return false;
                }
                arrow = stack;
                newTotalCharges += stack.getOrDefault(ModComponents.CHARGES.get(), 0);
            } else if (targetCharge.test(stack)) {
                rope = stack;
                newTotalCharges += chargesPerItem;
            } else if (!stack.isEmpty()) return false;
        }
        return arrow != null && rope != null && (canOverflow || newTotalCharges <=
                result.getDefaultInstance().getOrDefault(ModComponents.MAX_CHARGES.get(), 0));
    }

    @Override
    public ItemStack assemble(CraftingInput inv, HolderLookup.Provider access) {
        int newTotalCharges = 0;
        ItemStack arrow = null;
        for (int i = 0; i < inv.size(); ++i) {
            ItemStack stack = inv.getItem(i);
            if (targetItem.test(stack)) {
                arrow = stack;
                newTotalCharges += stack.getOrDefault(ModComponents.CHARGES.get(), 0);
            } else if (targetCharge.test(stack)) {
                newTotalCharges += chargesPerItem;
            }
        }
        ItemStack returnArrow = arrow.transmuteCopy(result, 1);
        returnArrow.set(ModComponents.CHARGES.get(), Math.min(newTotalCharges,
                returnArrow.getOrDefault(ModComponents.MAX_CHARGES.get(), 0)));
        return returnArrow;

    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.ROPE_ARROW_ADD.get();
    }

    public static class Serializer implements RecipeSerializer<ChargeSprayerRecipe> {

        private static final MapCodec<ChargeSprayerRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(CraftingRecipe::category),
                Ingredient.CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.targetItem),
                Codec.INT.optionalFieldOf("charges_per_item", 1).forGetter((recipe) -> recipe.chargesPerItem),
                Codec.BOOL.optionalFieldOf("can_overfill", false).forGetter((recipe) -> recipe.canOverflow)
        ).apply(instance, ChargeSprayerRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, ChargeSprayerRecipe> STREAM_CODEC = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC, CraftingRecipe::category,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.targetItem,
                ByteBufCodecs.VAR_INT, recipe -> recipe.chargesPerItem,
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
