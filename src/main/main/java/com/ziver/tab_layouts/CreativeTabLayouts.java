package com.ziver.tab_layouts;

import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.client.screen.CtlConfigScreen;
import com.ziver.tab_layouts.internal.plugin.CtlBuiltinPlugins;
import com.ziver.tab_layouts.internal.plugin.CtlPluginLoader;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CreativeTabLayouts.MOD_ID)
public final class CreativeTabLayouts {

    public static final String MOD_ID = "tab_layouts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public CreativeTabLayouts(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("[CTL] Initializing Creative Tab Layouts");
        modEventBus.addListener(this::commonSetup);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> new CtlConfigScreen(parent));
        }

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CtlPluginContext ctx = new CtlPluginContext(LOGGER);
            CtlBuiltinPlugins.register(ctx);
            CtlPluginLoader.loadPlugins(LOGGER, ctx);
        });
    }
}
