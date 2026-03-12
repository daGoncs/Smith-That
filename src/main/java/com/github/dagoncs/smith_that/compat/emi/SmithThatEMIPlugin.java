package com.github.dagoncs.smith_that.compat.emi;

import com.github.dagoncs.smith_that.config.SmithThatConfig;
import com.github.dagoncs.smith_that.recipe.ToolUpgradeRecipe;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;

import java.util.ArrayList;
import java.util.List;

@EmiEntrypoint
public class SmithThatEMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        List<? extends String> blacklist = List.of();
        try {
            blacklist = SmithThatConfig.SERVER.blacklist.get();
        } catch (Exception ignored) {}

        for (RecipeHolder<SmithingRecipe> holder : registry.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING)) {
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

                    EmiIngredient emiTemplate = EmiIngredient.of(recipe.getTemplate());
                    EmiIngredient emiBase = EmiIngredient.of(Ingredient.of(validBases.toArray(new ItemStack[0])));
                    EmiIngredient emiAddition = EmiStack.of(additionStack);
                    EmiStack emiOutput = EmiStack.of(additionStack.copy());

                    registry.addRecipe(new SmithThatEmiRecipe(emiTemplate, emiBase, emiAddition, emiOutput));
                }
            }
        }
    }
}