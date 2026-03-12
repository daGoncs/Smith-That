package com.github.dagoncs.smith_that.compat.jei;

import com.github.dagoncs.smith_that.SmithThat;
import com.github.dagoncs.smith_that.config.SmithThatConfig;
import com.github.dagoncs.smith_that.recipe.ToolUpgradeRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class SmithThatJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(SmithThat.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;

        List<? extends String> blacklist = List.of();
        try {
            blacklist = SmithThatConfig.SERVER.blacklist.get();
        } catch (Exception ignored) {}

        List<RecipeHolder<SmithingRecipe>> unrolledRecipes = new ArrayList<>();

        for (RecipeHolder<SmithingRecipe> holder : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING)) {
            if (holder.value() instanceof ToolUpgradeRecipe recipe) {

                ItemStack[] bases = recipe.getBase().getItems();
                ItemStack[] additions = recipe.getAddition().getItems();

                for (ItemStack additionStack : additions) {
                    ResourceLocation additionId = BuiltInRegistries.ITEM.getKey(additionStack.getItem());

                    if (blacklist.contains(additionId.toString())) continue;

                    List<ItemStack> validBases = new ArrayList<>();
                    for (ItemStack baseStack : bases) {
                        ResourceLocation baseId = BuiltInRegistries.ITEM.getKey(baseStack.getItem());
                        if (!blacklist.contains(baseId.toString())) {
                            validBases.add(baseStack);
                        }
                    }

                    if (validBases.isEmpty()) continue;

                    // Provides exactly what the constructor demands
                    SmithingTransformRecipe dummyRecipe = new SmithingTransformRecipe(
                            recipe.getTemplate(),
                            Ingredient.of(validBases.toArray(new ItemStack[0])),
                            Ingredient.of(additionStack),
                            additionStack.copy()
                    );

                    // Generates a fake, unique ID for this unrolled recipe so the RecipeHolder is happy
                    ResourceLocation dummyId = ResourceLocation.fromNamespaceAndPath(SmithThat.MOD_ID, "dummy_jei_" + additionId.getPath() + "_" + unrolledRecipes.size());

                    // Wrap it in the Holder;
                    unrolledRecipes.add(new RecipeHolder<>(dummyId, dummyRecipe));
                }
            }
        }

        // Recipe registration for JEI
        registration.addRecipes(RecipeTypes.SMITHING, unrolledRecipes);
    }
}