package com.ziver.tab_layouts;

import com.ziver.tab_layouts.internal.plugin.CtlPluginLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CreativeTabLayouts.MOD_ID)
public final class CreativeTabLayouts {
    public static final String MOD_ID = "tab_layouts";
    public static final String CTO_MOD_ID = "tab_organizer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public CreativeTabLayouts(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("[CTL] Initializing Creative Tab Layouts");
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CtlConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CtlPluginLoader.loadPlugins(LOGGER));
    }
}
