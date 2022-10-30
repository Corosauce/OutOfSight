package com.corosus.out_of_sight.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import static net.minecraftforge.common.ForgeConfigSpec.*;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

@EventBusSubscriber
public class Config {

    private static final Builder CLIENT_BUILDER = new Builder();

    public static final CategoryGeneral GENERAL = new CategoryGeneral();

    public static final class CategoryGeneral {

        public final BooleanValue entityRenderLimit;
        public final DoubleValue entityRenderHor;
        public final DoubleValue entityRenderVer;

        public final BooleanValue tileEntityRenderLimit;
        public final DoubleValue tileEntityRenderHor;
        public final DoubleValue tileEntityRenderVer;

        public final BooleanValue chunkRenderLimit;
        public final DoubleValue chunkRenderVer;

        private CategoryGeneral() {
            CLIENT_BUILDER.comment("General mod settings").push("general");

            entityRenderLimit = CLIENT_BUILDER
                    .define("entityRenderLimit", true);
            entityRenderHor = CLIENT_BUILDER
                    .defineInRange("entityRenderHor", 128, 1D, 30000);
            entityRenderVer = CLIENT_BUILDER
                    .defineInRange("entityRenderVer", 24, 1D, 30000);

            tileEntityRenderLimit = CLIENT_BUILDER
                    .define("tileEntityRenderLimit", true);
            tileEntityRenderHor = CLIENT_BUILDER
                    .defineInRange("tileEntityRenderHor", 128, 1D, 30000);
            tileEntityRenderVer = CLIENT_BUILDER
                    .defineInRange("tileEntityRenderVer", 24, 1D, 30000);

            chunkRenderLimit = CLIENT_BUILDER
                    .define("chunkRenderLimit", true);
            chunkRenderVer = CLIENT_BUILDER
                    .defineInRange("chunkRenderVer", 64, 1D, 30000);

            CLIENT_BUILDER.pop();
        }
    }
    public static final ForgeConfigSpec CLIENT_CONFIG = CLIENT_BUILDER.build();
}
