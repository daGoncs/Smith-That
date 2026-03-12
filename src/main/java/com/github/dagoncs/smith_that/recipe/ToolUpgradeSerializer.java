package com.github.dagoncs.smith_that.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ToolUpgradeSerializer implements RecipeSerializer<ToolUpgradeRecipe> {

    public static final MapCodec<ToolUpgradeRecipe> CODEC = RecordCodecBuilder.mapCodec(
            inst -> inst.group(
                    Ingredient.CODEC.fieldOf("template").forGetter(r -> r.template),
                    Ingredient.CODEC.fieldOf("base").forGetter(r -> r.base),
                    Ingredient.CODEC.fieldOf("addition").forGetter(r -> r.addition)
            ).apply(inst, ToolUpgradeRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ToolUpgradeRecipe> STREAM_CODEC = StreamCodec.of(
            ToolUpgradeSerializer::toNetwork, ToolUpgradeSerializer::fromNetwork
    );

    @Override
    public MapCodec<ToolUpgradeRecipe> codec() { return CODEC; }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, ToolUpgradeRecipe> streamCodec() { return STREAM_CODEC; }

    private static ToolUpgradeRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
        Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient base = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient addition = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        return new ToolUpgradeRecipe(template, base, addition);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, ToolUpgradeRecipe recipe) {
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.template);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.base);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.addition);
    }
}