package com.github.dagoncs.smith_that.recipe;

import com.github.dagoncs.smith_that.config.SmithThatConfig;
import com.github.dagoncs.smith_that.logic.ToolTransformationEngine;
import com.github.dagoncs.smith_that.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;

public class ToolUpgradeRecipe implements SmithingRecipe {
    final Ingredient template;
    final Ingredient base;
    final Ingredient addition;

    public ToolUpgradeRecipe(Ingredient template, Ingredient base, Ingredient addition) {
        this.template = template;
        this.base = base;
        this.addition = addition;
    }

    @Override
    public boolean matches(SmithingRecipeInput input, Level level) {
        if (!this.template.test(input.template()) || !this.base.test(input.base()) || !this.addition.test(input.addition())) {
            return false;
        }

        // STRICT BLACKLIST CHECK: Ensure neither Slot 2 nor Slot 3 are blacklisted
        ResourceLocation baseId = BuiltInRegistries.ITEM.getKey(input.base().getItem());
        ResourceLocation additionId = BuiltInRegistries.ITEM.getKey(input.addition().getItem());
        List<? extends String> blacklist = SmithThatConfig.SERVER.blacklist.get();

        if (blacklist.contains(baseId.toString()) || blacklist.contains(additionId.toString())) {
            return false;
        }

        return true;
    }

    @Override
    public ItemStack assemble(SmithingRecipeInput input, HolderLookup.Provider registries) {
        // Hand the logic over to our Transformation Engine
        return ToolTransformationEngine.transform(input.base(), input.addition());
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY; // Dynamic output based on Slot 3
    }

    @Override
    public boolean isTemplateIngredient(ItemStack stack) { return this.template.test(stack); }

    @Override
    public boolean isBaseIngredient(ItemStack stack) { return this.base.test(stack); }

    @Override
    public boolean isAdditionIngredient(ItemStack stack) { return this.addition.test(stack); }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistries.TOOL_UPGRADE_SERIALIZER.get();
    }

    // REI/JEI/EMI compat accessors
    public net.minecraft.world.item.crafting.Ingredient getTemplate() { return this.template; }
    public net.minecraft.world.item.crafting.Ingredient getBase() { return this.base; }
    public net.minecraft.world.item.crafting.Ingredient getAddition() { return this.addition; }
}