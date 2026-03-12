package com.github.dagoncs.smith_that.logic;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ToolTransformationEngine {

    /**
     * Transforms data from the old item (Slot 2) to the new item type (Slot 3).
     *
     * @param slot2Old The old, enchanted/damaged item being upgraded.
     * @param slot3New The target item providing the new material tier.
     * @return The final transformed ItemStack.
     */
    public static ItemStack transform(ItemStack slot2Old, ItemStack slot3New) {
        if (slot2Old.isEmpty() || slot3New.isEmpty()) return ItemStack.EMPTY;

        // Output Construction: Creates a fresh ItemStack using the exact Item type from Slot 3;
        // This inherently clears any weird data the player might have had on the Slot 3 item;
        ItemStack output = new ItemStack(slot3New.getItem(), 1);

        // Component Wipe & Copy: Apply the patch from Slot 2 onto the fresh Slot 3 item;
        // This transfers Custom Names, Lore, Trim, and Enchantments;
        output.applyComponents(slot2Old.getComponentsPatch());

        // Additive Durability (Damage)
        applyAdditiveDamage(slot2Old, slot3New, output);

        // Handles Strict Enchantment Filtering
        filterEnchantments(output);

        return output;
    }

    private static void applyAdditiveDamage(ItemStack slot2Old, ItemStack slot3New, ItemStack output) {
        // If either item doesn't use durability, we skip the math;
        if (!slot2Old.isDamageableItem() || !output.isDamageableItem()) return;

        int max2 = slot2Old.getMaxDamage();
        int damage2 = slot2Old.getDamageValue();
        int remaining2 = max2 - damage2;

        int max3 = output.getMaxDamage(); // Output shares max damage with Slot 3;
        int damage3 = slot3New.getDamageValue(); // Checks if Slot 3 was already damaged;
        int remaining3 = max3 - damage3;

        // Adds remaining durabilities together;
        int totalRemaining = remaining2 + remaining3;

        // Hard-caps at the new tool's maximum durability;
        int cappedRemaining = Math.min(totalRemaining, max3);

        // Applies the inverse as the new damage component;
        int newDamage = max3 - cappedRemaining;

        // Clamp to max3 - 1 to prevent breaking the item instantly in edge cases;
        output.setDamageValue(Math.min(newDamage, max3 - 1));
    }

    private static void filterEnchantments(ItemStack output) {
        ItemEnchantments currentEnchants = EnchantmentHelper.getEnchantmentsForCrafting(output);
        if (currentEnchants.isEmpty()) return;

        ItemEnchantments.Mutable mutableEnchants = new ItemEnchantments.Mutable(currentEnchants);

        // Removes enchantments that are not compatible with the new item type;
        mutableEnchants.keySet().removeIf(enchantment ->
                !enchantment.value().canEnchant(output)
        );

        EnchantmentHelper.setEnchantments(output, mutableEnchants.toImmutable());
    }
}