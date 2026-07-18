package com.ziver.tab_layouts.api;

import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

/**
 * Public query API for Creative Tab Layouts.
 */
public final class CtlApiExtensions {
    private CtlApiExtensions() {}

    /**
     * Returns whether the specified creative tab is currently controlled by CTL.
     *
     * <p>A built-in vanilla layout disabled through CTL's configuration is not
     * considered controlled while that configuration is disabled.</p>
     *
     * @param tabId registry ID of the creative tab
     * @return {@code true} when CTL currently controls the tab
     */
    public static boolean isTabControlled(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlTabRegistry.isControlled(tabId);
    }
}