package com.corosus.out_of_sight;

import com.corosus.out_of_sight.command.CommandReloadConfig;
import com.corosus.out_of_sight.config.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


@Mod(OutOfSight.MODID)
public class OutOfSight
{
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MODID = "out_of_sight";

    public static HashMap<Class, String> cacheClassToCanonicalName = new HashMap<>();

    public OutOfSight() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(new EventHandlerForge());

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void doClientStuff(final FMLClientSetupEvent event) {

    }

    private void onServerStarting(final FMLServerStartingEvent event) {
        CommandReloadConfig.register(event.getServer().getCommandManager().getDispatcher());
    }

    public static String getCanonicalNameCached(Class clazz) {
        if (!cacheClassToCanonicalName.containsKey(clazz)) {
            cacheClassToCanonicalName.put(clazz, clazz.getCanonicalName());
        }
        return cacheClassToCanonicalName.get(clazz);
    }
}
