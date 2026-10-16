package com.ziver.tab_layouts.api.plugin;

/**
 * Semantic role of a CTL plugin.
 *
 * <p>The type participates in plugin resolution and communicates whether an integration
 * owns a target layout, provides a fallback preset, or adds compatibility behavior.</p>
 */
public enum CtlPluginType {
    /** Integration supplied by the mod being represented. */
    NATIVE,
    /** Fallback integration used only when no eligible native integration exists. */
    PRESET,
    /** Additive compatibility integration, typically involving multiple target mods. */
    COMPAT
}