package com.ziver.tab_layouts.internal.plugin;

import com.ziver.tab_layouts.api.plugin.CtlPluginType;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Objects;

public final class CtlPluginValidation {
    private CtlPluginValidation() {}

    private static CtlPluginDescriptor activePlugin;

    static void begin(CtlPluginDescriptor plugin) {
        if (activePlugin != null) throw new IllegalStateException("A CTL plugin is already being registered");
        activePlugin = Objects.requireNonNull(plugin, "plugin");
    }

    static void end() {
        activePlugin = null;
    }

    public static void controlTab(Logger logger, ResourceLocation tabId) {
        CtlPluginDescriptor plugin = activePlugin;
        if (plugin == null) return;

        if (plugin.type() == CtlPluginType.COMPAT)
            logger.warn("[CTL] COMPAT plugin '{}' called controlTab({}); compat plugins should normally contribute to an existing layout", plugin.id(), tabId);
    }

    public static void controlSubtab(Logger logger, ResourceLocation tabId, ResourceLocation parentTabId) {
        CtlPluginDescriptor plugin = activePlugin;
        if (plugin == null) return;

        if (plugin.type() == CtlPluginType.COMPAT)
            logger.warn("[CTL] COMPAT plugin '{}' called controlSubtab({}, {}); compat plugins should normally contribute to existing layouts", plugin.id(), tabId, parentTabId);
    }
}