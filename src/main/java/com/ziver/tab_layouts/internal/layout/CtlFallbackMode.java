package com.ziver.tab_layouts.internal.layout;

import net.minecraft.network.chat.Component;

public enum CtlFallbackMode {
    BY_MOD_SECTION("screen.tab_layouts.config.fallback_mode.by_mod_section"),
    BY_MOD_PAGE("screen.tab_layouts.config.fallback_mode.by_mod_page");

    private final String translationKey;

    CtlFallbackMode(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }

    public String translationKey() {
        return translationKey;
    }
}