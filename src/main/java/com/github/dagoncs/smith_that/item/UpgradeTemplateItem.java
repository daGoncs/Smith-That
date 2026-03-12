package com.github.dagoncs.smith_that.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UpgradeTemplateItem extends Item {

    public UpgradeTemplateItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.smith_that.upgrade_template.applies_to").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.smith_that.upgrade_template.applies_to_value").withStyle(ChatFormatting.BLUE));

        tooltipComponents.add(Component.translatable("item.smith_that.upgrade_template.ingredients").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.translatable("item.smith_that.upgrade_template.ingredients_value").withStyle(ChatFormatting.BLUE));    }
}