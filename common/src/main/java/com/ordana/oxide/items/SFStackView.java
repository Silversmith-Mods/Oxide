package com.ordana.oxide.items;

import com.mojang.serialization.Codec;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.misc.HolderReference;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

// immutable view of a SoftFluidStack. Because components need to be guaranteed immutable
// technically not needed
public class SFStackView implements TooltipProvider {

    public static final Codec<SFStackView> CODEC = SoftFluidStack.CODEC.xmap(SFStackView::of, SFStackView::toMutable);
    public static final StreamCodec<RegistryFriendlyByteBuf, SFStackView> STREAM_CODEC =
            SoftFluidStack.STREAM_CODEC.map(SFStackView::of, SFStackView::toMutable);

    private final SoftFluidStack fluid;

    private SFStackView(SoftFluidStack stack) {
        this.fluid = stack.copy();
    }

    public static SFStackView of(SoftFluidStack stack) {
        return new SFStackView(stack);
    }

    public SFStackView copyWithCount(int count) {
        return of(this.fluid.copyWithCount(count));
    }

    public static SFStackView load( HolderLookup.Provider provider, Tag nbt) {
        return of(SoftFluidStack.load(provider, nbt));
    }

    public Tag save(HolderLookup.Provider provider) {
        return this.fluid.save(provider);
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

    public boolean sameFluidSameComponents(SoftFluidStack stack){
        return this.fluid.isSameFluidSameComponents(stack);
    }

    public boolean is(HolderReference<SoftFluid> tag) {
        return this.fluid.is(tag);
    }

    public SoftFluidStack toMutable() {
        return this.fluid.copy();
    }

    public int getParticleColor(Level level, @Nullable BlockPos pos) {
        //TODO: try still, particle and flowing color. idk which might work best
        return fluid.getParticleColor(level, pos);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag) {
        if (!this.fluid.isEmpty()) {
            Component fluidName = fluid.getDisplayName();

            //tooltip is broen here, a bug I have in ML
            tooltipAdder.accept(Component.translatable("tooltip.oxide.fluid",
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
        if (!(object instanceof SFStackView that)) return false;
        return Objects.equals(fluid, that.fluid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fluid);
    }
}