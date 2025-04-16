package com.z4kkyy.woodcutter;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(WoodCutter.MODID)
public class WoodCutter {

    public static final String MODID = "woodcutter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WoodCutter(FMLJavaModLoadingContext context) {
        context.getModEventBus().addListener(this::setup);
        ModLoadingContext modLoadingContext = new ModLoadingContext();
        modLoadingContext.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        MinecraftForge.EVENT_BUS.register(new BlockBreakEventHandler());
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("WoodCutterMod is setting up!");
    }
}
