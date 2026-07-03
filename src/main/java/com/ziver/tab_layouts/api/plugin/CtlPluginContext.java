package com.ziver.tab_layouts.api.plugin;

import com.ziver.tab_layouts.api.layout.CtlTabBuilder;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.Optional;

public final class CtlPluginContext {
    private final Logger logger;

    public CtlPluginContext(Logger logger) {
        this.logger = Objects.requireNonNull(logger, "logger");
    }

    /**
     * Creates or retrieves a CTL-controlled layout for the given creative tab.
     *
     * <p>Use this when your plugin owns the layout for that tab.</p>
     */
    public CtlTabBuilder controlTab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlTabRegistry.controlTab(tabId);
    }

    /**
     * Retrieves a CTL-controlled layout only if another plugin already controls it.
     *
     * <p>Use this for addons/compat integrations that should only contribute
     * to an existing CTL layout without forcing the tab to become controlled.</p>
     */
    public Optional<CtlTabBuilder> contributeTab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        if (!CtlTabRegistry.isControlled(tabId)) return Optional.empty();
        return Optional.of(CtlTabRegistry.controlTab(tabId));
    }

    public void info(String message) {
        logger.info("[CTL Plugin] {}", message);
    }

    public void warn(String message) {
        logger.warn("[CTL Plugin] {}", message);
    }

    public void error(String message) {
        logger.error("[CTL Plugin] {}", message);
    }
}