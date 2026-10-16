package com.ziver.tab_layouts.api.plugin;

import net.minecraft.resources.ResourceLocation;

/**
 * Entry point implemented by CTL integrations.
 *
 * <p>Plugins are discovered through {@link CtlPlugin} and receive a
 * {@link CtlPluginContext} during registration.</p>
 */
public interface ICtlPlugin {

    /**
     * @deprecated Declare plugin identity through {@link CtlPlugin}.
     * This method will be removed in CTL 2.0.0.
     */
    @Deprecated(forRemoval = true)
    default ResourceLocation getPluginUid() {
        throw new UnsupportedOperationException("Plugin UID is declared through @CtlPlugin");
    }

    /**
     * Registers this plugin's layouts, contributions, and subtab relationships.
     *
     * @param ctx registration context supplied by CTL
     */
    void register(CtlPluginContext ctx);
}