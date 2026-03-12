package com.github.dagoncs.smith_that.registry;

import com.github.dagoncs.smith_that.SmithThat;
import com.github.dagoncs.smith_that.item.UpgradeTemplateItem;
import com.github.dagoncs.smith_that.recipe.ToolUpgradeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModRegistries {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SmithThat.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SmithThat.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SmithThat.MOD_ID);

    // Register our single Universal Template Item
    public static final DeferredItem<Item> UPGRADE_TEMPLATE = ITEMS.register("upgrade_template",
            () -> new UpgradeTemplateItem(new Item.Properties()));

    public static final DeferredHolder<RecipeSerializer<?>, ToolUpgradeSerializer> TOOL_UPGRADE_SERIALIZER =
            RECIPE_SERIALIZERS.register("tool_upgrade", ToolUpgradeSerializer::new);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SMITH_THAT_TAB = CREATIVE_TABS.register("smith_that_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.smith_that"))
            .icon(() -> new ItemStack(UPGRADE_TEMPLATE.get())) // Updated here
            .displayItems((parameters, output) -> {
                output.accept(UPGRADE_TEMPLATE.get()); // Updated here
            })
            .build());

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}