package com.ziver.tab_layouts.internal.layout;

import net.minecraft.network.chat.Component;

public enum CtlFallbackMode {
    BY_MOD_SECTION("screen.tab_layouts.config.fallback_mode.by_mod_section"),
    BY_MOD_PAGE("screen.tab_layouts.config.fallback_mode.by_mod_page");

    private final Component displayName;

    CtlFallbackMode(String translationKey) {
        this.displayName = Component.translatable(translationKey);
    }

    public Component displayName() {
        return this.displayName;
    }
}