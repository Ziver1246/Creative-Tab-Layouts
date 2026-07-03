package com.ziver.tab_layouts.internal.visual;

import java.util.Locale;

public enum CtlSpriteLayout {
    VERTICAL,
    HORIZONTAL,
    GRID;

    public static CtlSpriteLayout byName(String name) {
        if (name == null || name.isBlank()) {
            return VERTICAL;
        }

        return switch (name.toLowerCase(Locale.ROOT)) {
            case "horizontal" -> HORIZONTAL;
            case "grid" -> GRID;
            default -> VERTICAL;
        };
    }
}