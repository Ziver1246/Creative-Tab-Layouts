package com.ziver.tab_layouts.internal.util;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class CtlLangKeys {
    private CtlLangKeys() {}

    public static String page(ResourceLocation pageId) {
        Objects.requireNonNull(pageId, "pageId");
        return "tabpage." + pageId.getNamespace() + "." + normalizePath(pageId.getPath());
    }

    public static String section(ResourceLocation sectionId) {
        Objects.requireNonNull(sectionId, "sectionId");
        return "tabsection." + sectionId.getNamespace() + "." + normalizePath(sectionId.getPath());
    }

    private static String normalizePath(String path) {
        return path.replace('/', '.');
    }
}