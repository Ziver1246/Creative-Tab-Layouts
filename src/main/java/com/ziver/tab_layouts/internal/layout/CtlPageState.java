package com.ziver.tab_layouts.internal.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CtlPageState {
    private static final Map<ResourceLocation, Integer> PAGES = new HashMap<>();

    private CtlPageState() {}

    public static int page(ResourceLocation tabId) {
        return PAGES.getOrDefault(tabId, 0);
    }

    public static boolean canPrevious(ResourceLocation tabId) {
        return page(tabId) > 0;
    }

    public static boolean canNext(ResourceLocation tabId, int pageCount) {
        return page(tabId) < pageCount - 1;
    }

    public static boolean previous(ResourceLocation tabId) {
        if (!canPrevious(tabId)) return false;
        PAGES.put(tabId, page(tabId) - 1);
        return true;
    }

    public static boolean next(ResourceLocation tabId, int pageCount) {
        if (!canNext(tabId, pageCount)) return false;
        PAGES.put(tabId, page(tabId) + 1);
        return true;
    }

    public static void reset(ResourceLocation tabId) {
        PAGES.put(tabId, 0);
    }
}