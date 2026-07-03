package com.ziver.tab_layouts.internal.registry;

import com.ziver.tab_layouts.internal.visual.CtlBannerVisual;
import com.ziver.tab_layouts.internal.visual.CtlHeaderVisual;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CtlVisualRegistry {
    private static final Map<ResourceLocation, CtlHeaderVisual> HEADERS = new HashMap<>();
    private static final Map<ResourceLocation, CtlBannerVisual> BANNERS = new HashMap<>();

    private CtlVisualRegistry() {}

    public static void replaceHeaders(Map<ResourceLocation, CtlHeaderVisual> headers) {
        HEADERS.clear();
        HEADERS.putAll(headers);
    }

    public static void replaceBanners(Map<ResourceLocation, CtlBannerVisual> banners) {
        BANNERS.clear();
        BANNERS.putAll(banners);
    }

    public static CtlHeaderVisual header(ResourceLocation sectionId) {
        return HEADERS.get(sectionId);
    }

    public static CtlBannerVisual banner(ResourceLocation pageId) {
        return BANNERS.get(pageId);
    }

    public static boolean hasHeader(ResourceLocation sectionId) {
        return HEADERS.containsKey(sectionId);
    }

    public static boolean hasBanner(ResourceLocation pageId) {
        return BANNERS.containsKey(pageId);
    }
}