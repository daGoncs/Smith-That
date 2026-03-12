package com.github.dagoncs.smith_that.mixin;

import com.github.dagoncs.smith_that.config.SmithThatConfig;
import com.github.dagoncs.smith_that.recipe.ToolUpgradeRecipe;
import com.github.dagoncs.smith_that.registry.ModRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {

    // Unique flag to pass data from HEAD to TAIL
    @Unique
    private boolean smith_that$shouldRestoreTemplate = false;

    public SmithingMenuMixin(MenuType<?> type, int containerId, net.minecraft.world.entity.player.Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    // 1. Check BEFORE anything is consumed
    @Inject(method = "onTake", at = @At("HEAD"))
    private void smith_that$onTakeHead(Player player, ItemStack stack, CallbackInfo ci) {
        Level level = player.level();
        Optional<RecipeHolder<SmithingRecipe>> recipe = level.getRecipeManager().getRecipeFor(
                net.minecraft.world.item.crafting.RecipeType.SMITHING,
                this.createRecipeInput(),
                level
        );

        // Save our decision
        smith_that$shouldRestoreTemplate = recipe.isPresent()
                && recipe.get().value() instanceof ToolUpgradeRecipe
                && !SmithThatConfig.SERVER.consumeTemplate.get();
    }

    // 2. Restore AFTER vanilla/owo-lib finishes their operations
    @Inject(method = "onTake", at = @At("TAIL"))
    private void smith_that$onTakeTail(Player player, ItemStack stack, CallbackInfo ci) {
        if (smith_that$shouldRestoreTemplate) {
            ItemStack templateStack = this.inputSlots.getItem(0);

            if (templateStack.isEmpty()) {
                // If the slot was emptied, recreate the template
                this.inputSlots.setItem(0, new ItemStack(ModRegistries.UPGRADE_TEMPLATE.get()));
            } else {
                // If they had a stack (e.g., 64 templates), just give the 1 back
                templateStack.grow(1);
            }

            // Reset the flag for the next craft
            smith_that$shouldRestoreTemplate = false;
        }
    }

    @Shadow protected abstract SmithingRecipeInput createRecipeInput();
}