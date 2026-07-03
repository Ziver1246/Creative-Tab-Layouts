package com.ziver.tab_layouts.internal.plugin;

import com.ziver.tab_layouts.api.plugin.CtlPluginContext;
import com.ziver.tab_layouts.internal.builtin.vanilla.MinecraftCtlPlugin;

public final class CtlBuiltinPlugins {
    private CtlBuiltinPlugins() {}

    public static void register(CtlPluginContext ctx) {
        new MinecraftCtlPlugin().register(ctx);
    }
}