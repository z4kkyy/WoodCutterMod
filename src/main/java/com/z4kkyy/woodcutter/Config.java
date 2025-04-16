package com.z4kkyy.woodcutter;

import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CommonConfig COMMON;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class CommonConfig{

        public final ForgeConfigSpec.BooleanValue enableMod;
        public final ForgeConfigSpec.BooleanValue damageAxe;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("WoodCutterMod");

            enableMod = builder
                    .comment("MOD有効/無効")
                    .define("enableMod", true);

            damageAxe = builder
                    .comment("斧の耐久性の損失の有/無")
                    .define("damageAxe", true);

            builder.pop();
        }
    }
}
