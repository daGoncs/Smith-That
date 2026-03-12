package com.github.dagoncs.smith_that;

import com.github.dagoncs.smith_that.config.SmithThatConfig;
import com.github.dagoncs.smith_that.datagen.DataGenerators;
import com.github.dagoncs.smith_that.registry.ModRegistries;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(SmithThat.MOD_ID)
public class SmithThat {
    public static final String MOD_ID = "smith_that";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SmithThat(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.SERVER, SmithThatConfig.SERVER_SPEC);
        ModRegistries.register(modEventBus);

        // We'll keep this hook here for when we rebuild DataGen in Step 6
        modEventBus.addListener(DataGenerators::gatherData);

        LOGGER.info("Smith That! Universal Engine Initialized.");
    }
}