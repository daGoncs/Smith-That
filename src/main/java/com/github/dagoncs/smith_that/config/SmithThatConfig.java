package com.github.dagoncs.smith_that.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class SmithThatConfig {
    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        final Pair<Server, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair.getRight();
        SERVER = specPair.getLeft();
    }

    public static class Server {
        public final ModConfigSpec.BooleanValue consumeTemplate;
        public final ModConfigSpec.ConfigValue<List<? extends String>> blacklist;

        public Server(ModConfigSpec.Builder builder) {
            builder.push("general");

            consumeTemplate = builder
                    .comment("If true, the progression template is consumed when smithing. If false, it remains in the slot.")
                    .define("consume_template_by_default", true);

            blacklist = builder
                    .comment("A list of exact item IDs (e.g., 'minecraft:wooden_pickaxe' or 'botania:terra_blade') that CANNOT be used in Slot 2 or Slot 3 of the Smithing Table.")
                    .defineListAllowEmpty("blacklist", List.of(), obj -> obj instanceof String);

            builder.pop();
        }
    }
}