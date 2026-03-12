package com.github.dagoncs.smith_that.compat.rei;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.plugin.common.displays.DefaultSmithingDisplay;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

// Extends REI's specific DefaultSmithingDisplay so the category accepts it;
public class SmithThatSmithingDisplay extends DefaultSmithingDisplay {

    public SmithThatSmithingDisplay(EntryIngredient template, EntryIngredient base, EntryIngredient addition, EntryIngredient output, ResourceLocation location) {
        // Pass the slots in the exact order REI expects (Template, Base, Addition)
        super(List.of(template, base, addition), List.of(output), Optional.ofNullable(location));
    }
}