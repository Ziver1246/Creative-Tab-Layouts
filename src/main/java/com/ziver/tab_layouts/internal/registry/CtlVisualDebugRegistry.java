package com.ziver.tab_layouts.internal.registry;

import com.ziver.tab_layouts.internal.visual.debug.CtlVisualDebugInfo;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CtlVisualDebugRegistry {
    private static final Map<ResourceLocation, CtlVisualDebugInfo> HEADERS = new HashMap<>();
    private static final Map<ResourceLocation, CtlVisualDebugInfo> BANNERS = new HashMap<>();

    private CtlVisualDebugRegistry() {}

    public static void replaceHeaders(Map<ResourceLocation, CtlVisualDebugInfo> headers) {
        HEADERS.clear();
        HEADERS.putAll(headers);
    }

    public static void replaceBanners(Map<ResourceLocation, CtlVisualDebugInfo> banners) {
        BANNERS.clear();
        BANNERS.putAll(banners);
    }

    public static CtlVisualDebugInfo header(ResourceLocation sectionId) {
        return HEADERS.get(sectionId);
    }

    public static CtlVisualDebugInfo banner(ResourceLocation pageId) {
        return BANNERS.get(pageId);
    }

    public static void clear() {
        HEADERS.clear();
        BANNERS.clear();
    }
}