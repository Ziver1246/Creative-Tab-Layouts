package com.ziver.tab_layouts.client;

import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.internal.visual.CtlBannerVisualReloadListener;
import com.ziver.tab_layouts.internal.visual.CtlHeaderVisualReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

@EventBusSubscriber(modid = CreativeTabLayouts.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {}

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new CtlHeaderVisualReloadListener());
        event.registerReloadListener(new CtlBannerVisualReloadListener());
    }
}