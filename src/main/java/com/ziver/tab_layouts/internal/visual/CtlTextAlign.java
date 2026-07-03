package com.ziver.tab_layouts.internal.visual;

import java.util.Locale;

public enum CtlTextAlign {
    LEFT,
    CENTER,
    RIGHT;

    public static CtlTextAlign byName(String name) {
        if (name == null || name.isBlank()) {
            return LEFT;
        }

        return switch (name.toLowerCase(Locale.ROOT)) {
            case "center" -> CENTER;
            case "right" -> RIGHT;
            default -> LEFT;
        };
    }
}