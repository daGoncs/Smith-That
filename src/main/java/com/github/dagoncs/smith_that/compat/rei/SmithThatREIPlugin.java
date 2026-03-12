package com.github.dagoncs.smith_that.compat.rei;

import com.github.dagoncs.smith_that.config.SmithThatConfig;
import com.github.dagoncs.smith_that.recipe.ToolUpgradeRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class SmithThatREIPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        List<? extends String> blacklist = List.of();
        try {
            blacklist = SmithThatConfig.SERVER.blacklist.get();
        } catch (Exception ignored) {
            // Failsafe for early REI initialization
        }

        for (RecipeHolder<SmithingRecipe> holder : registry.getRecipeManager().getAllRecipesFor(RecipeType.SMITHING)) {
            if (holder.value() instanceof ToolUpgradeRecipe recipe) {

                ItemStack[] templates = recipe.getTemplate().getItems();
                ItemStack[] bases = recipe.getBase().getItems();
                ItemStack[] additions = recipe.getAddition().getItems();

                List<ItemStack> validBases = new ArrayList<>();
                for (ItemStack baseStack : bases) {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(baseStack.getItem());
                    if (!blacklist.contains(id.toString())) {
                        validBases.add(baseStack);
                    }
                }

                if (validBases.isEmpty()) continue;

                EntryIngredient templateIngredient = EntryIngredients.ofItemStacks(List.of(templates));
                EntryIngredient baseIngredient = EntryIngredients.ofItemStacks(validBases);

                for (ItemStack additionStack : additions) {
                    ResourceLocation additionId = BuiltInRegistries.ITEM.getKey(additionStack.getItem());

                    if (blacklist.contains(additionId.toString())) {
                        continue;
                    }

                    EntryIngredient additionIngredient = EntryIngredients.of(additionStack);
                    EntryIngredient outputIngredient = EntryIngredients.of(additionStack);

                    registry.add(new SmithThatSmithingDisplay(
                            templateIngredient,
                            baseIngredient,
                            additionIngredient,
                            outputIngredient,
                            holder.id()
                    ));
                }
            }
        }
    }
}