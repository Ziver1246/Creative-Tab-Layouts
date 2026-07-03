package com.ziver.tab_layouts.client.render;

import com.ziver.tab_layouts.internal.layout.CtlRenderedSection;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CtlPageRenderState {
    private CtlPageRenderState() {}

    public static int currentRow = 0;

    private static final Map<ResourceLocation, Map<Integer, List<CtlRenderedSection>>> SECTIONS = new HashMap<>();

    public static void set(ResourceLocation tabId, int pageIndex, List<CtlRenderedSection> sections) {
        SECTIONS.computeIfAbsent(tabId, ignored -> new HashMap<>()).put(pageIndex, sections);
    }

    public static List<CtlRenderedSection> get(ResourceLocation tabId, int pageIndex) {
        Map<Integer, List<CtlRenderedSection>> byPage = SECTIONS.get(tabId);
        if (byPage == null) {
            return List.of();
        }

        List<CtlRenderedSection> sections = byPage.get(pageIndex);
        return sections == null ? List.of() : sections;
    }

    public static void clear(ResourceLocation tabId, int pageIndex) {
        Map<Integer, List<CtlRenderedSection>> byPage = SECTIONS.get(tabId);

        if (byPage != null) {
            byPage.remove(pageIndex);
        }
    }

    public static void clearAll() {
        SECTIONS.clear();
        currentRow = 0;
    }
}