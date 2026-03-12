package com.github.dagoncs.smith_that.datagen;

import com.github.dagoncs.smith_that.SmithThat;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SmithThat.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("upgrade_template", "item/generated")
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(SmithThat.MOD_ID, "item/upgrade_template"));    }
}