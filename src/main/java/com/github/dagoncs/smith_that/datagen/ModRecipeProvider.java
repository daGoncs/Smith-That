package com.github.dagoncs.smith_that.datagen;

import com.github.dagoncs.smith_that.SmithThat;
import com.github.dagoncs.smith_that.recipe.ToolUpgradeRecipe;
import com.github.dagoncs.smith_that.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModRegistries.UPGRADE_TEMPLATE.get(), 2)
                .pattern(" L ")
                .pattern("LPL")
                .pattern(" L ")
                .define('L', Tags.Items.GEMS_LAPIS)
                .define('P', Items.PAPER)
                .unlockedBy("has_lapis", has(Tags.Items.GEMS_LAPIS))
                .save(output, ResourceLocation.fromNamespaceAndPath(SmithThat.MOD_ID, "upgrade_template_crafting"));

        // Universal Smithing Transfer Recipes
        // We map standard equipment tags to themselves. E.g., Pickaxe -> Pickaxe.
        List<TagKey<Item>> equipmentTags = new java.util.ArrayList<>(List.of(
                ItemTags.SWORDS, ItemTags.PICKAXES, ItemTags.AXES, ItemTags.SHOVELS, ItemTags.HOES,
                ItemTags.HEAD_ARMOR, ItemTags.CHEST_ARMOR, ItemTags.LEG_ARMOR, ItemTags.FOOT_ARMOR
        ));

        // Add standard modded utility/ranged tags (plural))
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/bows")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/crossbows")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/shields")));

        // Adds standard modded utility/ranged tags (singular)
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/bow")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/crossbow")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/shield")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "tools/trident")));

        // Fallbacks for mods that don't use the "tools/" prefix
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "bows")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "bow")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "shields")));
        equipmentTags.add(ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", "shield")));

        for (TagKey<Item> tag : equipmentTags) {
            // Converts names like "tools/bow" to "tools_bow" so the JSON filename is safe
            String safeName = tag.location().getPath().replace("/", "_");

            output.accept(ResourceLocation.fromNamespaceAndPath(SmithThat.MOD_ID, "transfer_" + safeName),
                    new ToolUpgradeRecipe(
                            Ingredient.of(ModRegistries.UPGRADE_TEMPLATE.get()),
                            Ingredient.of(tag),
                            Ingredient.of(tag)
                    ), null);
        }
    }
}