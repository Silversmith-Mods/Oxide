package com.ordana.oxide.items;

import com.mojang.serialization.Codec;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.moonlight.api.misc.HolderReference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.function.Consumer;

// immutable view of a SoftFluidStack. Because components need to be guaranteed immutable
public class SoftFluidStackView implements TooltipProvider {

    public static final Codec<SoftFluidStackView> CODEC = SoftFluidStack.CODEC.xmap(SoftFluidStackView::new, SoftFluidStackView::toMutable);
    public static final StreamCodec<RegistryFriendlyByteBuf, SoftFluidStackView> STREAM_CODEC =
            SoftFluidStack.STREAM_CODEC.map(SoftFluidStackView::new, SoftFluidStackView::toMutable);

    private final SoftFluidStack fluid;

    private SoftFluidStackView(SoftFluidStack stack) {
        this.fluid = stack.copy();
    }

    public static SoftFluidStackView of(SoftFluidStack stack) {
        return new SoftFluidStackView(stack);
    }


    public int getCount() {
        return this.fluid.getCount();
    }

    public SoftFluid getFluid() {
        return this.fluid.fluid();
    }

    public boolean isEmpty() {
        return this.fluid.isEmpty();
    }

    public boolean is(TagKey<SoftFluid> tag) {
        return this.fluid.is(tag);
    }

    public boolean is(HolderReference<SoftFluid> tag) {
        return this.fluid.is(tag);
    }

    public SoftFluidStack toMutable() {
        return this.fluid.copy();
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (!this.fluid.isEmpty()) {
            Component fluidName = fluid.getDisplayName();

            tooltipAdder.accept(Component.translatable("message.supplementaries.fluid_tooltip",
                    fluidName, fluid.getCount()).withStyle(ChatFormatting.GRAY));

            PotionContents contents = fluid.get(DataComponents.POTION_CONTENTS);
            if (contents != null) {
                contents.addPotionTooltip(tooltipAdder, 1.0F, context.tickRate());
            }
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SoftFluidStackView that)) return false;
        return Objects.equals(fluid, that.fluid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fluid);
    }
}