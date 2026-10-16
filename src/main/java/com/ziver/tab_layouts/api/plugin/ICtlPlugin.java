package com.ziver.tab_layouts.api.plugin;

import net.minecraft.resources.ResourceLocation;

public interface ICtlPlugin {

    /**
     * @deprecated Declare plugin identity through {@link CtlPlugin}.
     * This method will be removed in CTL 2.0.0.
     */
    @Deprecated(forRemoval = true)
    default ResourceLocation getPluginUid() {
        throw new UnsupportedOperationException("Plugin UID is declared through @CtlPlugin");
    }

    void register(CtlPluginContext ctx);
}