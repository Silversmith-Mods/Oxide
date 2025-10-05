package com.ordana.oxide.integrations;

import com.ordana.oxide.blocks.rusty.Rustable;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Map;

@EmiEntrypoint
public class EmiIntegration implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        EmiIngredient axes = EmiIngredient.of(ItemTags.AXES);


        //RUSTING
        Map<Block, Block> rust = Rustable.RUST_LEVEL_INCREASES.get();
        for (Block key : rust.keySet()) {
            ResourceLocation blockId = BuiltInRegistries.ITEM.getKey(key.asItem());
            EmiStack input = EmiStack.of(key);
            EmiStack output = EmiStack.of(rust.get(key));
            registry.addRecipe(EmiWorldInteractionRecipe.builder()
                .id(ResourceLocation.fromNamespaceAndPath("oxide", "/sponge_rusting/" + blockId.getNamespace() + "/" + blockId.getPath()))
                .leftInput(input)
                .rightInput(EmiStack.of(Items.WET_SPONGE), true)
                .output(output)
                .build());
        }

    }
}
