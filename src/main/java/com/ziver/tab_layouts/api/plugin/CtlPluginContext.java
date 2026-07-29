package com.ziver.tab_layouts.api.plugin;

import com.ziver.tab_layouts.api.layout.CtlContributionBuilder;
import com.ziver.tab_layouts.api.layout.CtlTabBuilder;
import com.ziver.tab_layouts.internal.registry.CtlSubtabRegistry;
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
     * Retrieves the full builder for an already controlled creative tab.
     *
     * @deprecated Use {@link #contribute(ResourceLocation)}. This method exposes
     * owner-only layout operations and will be removed in 2.0.0.
     */
    @Deprecated(since = "1.2.0", forRemoval = true)
    public Optional<CtlTabBuilder> contributeTab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        if (!CtlTabRegistry.isControlled(tabId)) return Optional.empty();
        return Optional.of(CtlTabRegistry.controlTab(tabId));
    }

    /**
     * Retrieves a contribution-only builder if the creative tab is already
     * controlled by CTL.
     *
     * <p>This does not force an uncontrolled tab to become controlled.</p>
     */
    public Optional<CtlContributionBuilder> contribute(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        if (!CtlTabRegistry.isControlled(tabId)) return Optional.empty();
        return Optional.of(CtlTabRegistry.controlTab(tabId));
    }

    /**
     * Creates or retrieves a CTL-controlled layout and registers its creative tab
     * as a subtab of another creative tab.
     *
     * <p>The parent tab does not need to be controlled by CTL.</p>
     */
    public CtlTabBuilder controlSubtab(ResourceLocation tabId, ResourceLocation parentTabId) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(parentTabId, "parentTabId");

        CtlSubtabRegistry.register(tabId, parentTabId);
        return CtlTabRegistry.controlTab(tabId);
    }

    /**
     * Registers an existing creative tab as a subtab while preserving its original layout.
     *
     * <p>The parent tab does not need to be controlled by CTL.</p>
     */
    public void subtab(ResourceLocation tabId, ResourceLocation parentTabId) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(parentTabId, "parentTabId");
        CtlSubtabRegistry.register(tabId, parentTabId);
    }

    /**
     * Registers multiple existing creative tabs as subtabs of the same parent while preserving their original layouts.
     *
     * <p>The parent tab does not need to be controlled by CTL.</p>
     */
    public void subtabs(ResourceLocation parentTabId, ResourceLocation... tabIds) {
        Objects.requireNonNull(parentTabId, "parentTabId");
        Objects.requireNonNull(tabIds, "tabIds");

        for (ResourceLocation tabId : tabIds)
            subtab(tabId, parentTabId);
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