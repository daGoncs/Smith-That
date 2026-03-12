package com.github.dagoncs.smith_that.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmithThatEmiRecipe implements EmiRecipe {
    private final EmiIngredient template;
    private final EmiIngredient base;
    private final EmiIngredient addition;
    private final EmiStack output;

    public SmithThatEmiRecipe(EmiIngredient template, EmiIngredient base, EmiIngredient addition, EmiStack output) {
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.output = output;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return VanillaEmiRecipeCategories.SMITHING;
    }

    // Hardcoded this to return null so EMI knows it's a dynamic recipe
    @Override
    public @Nullable ResourceLocation getId() {
        return null;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(template, base, addition);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 112;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 58, 1);
        widgets.addSlot(template, 0, 0);
        widgets.addSlot(base, 18, 0);
        widgets.addSlot(addition, 36, 0);
        widgets.addSlot(output, 94, 0).recipeContext(this);
    }
}