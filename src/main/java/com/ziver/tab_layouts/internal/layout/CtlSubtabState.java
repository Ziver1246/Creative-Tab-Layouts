package com.ziver.tab_layouts.internal.layout;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CtlSubtabState {
    private CtlSubtabState() {}

    private static final Map<ResourceLocation, ResourceLocation> SELECTED_BY_PARENT = new HashMap<>();
    private static final Map<ResourceLocation, Integer> SCROLL_BY_PARENT = new HashMap<>();
    private static boolean collapsed;

    public static ResourceLocation selected(ResourceLocation parentTabId) {
        Objects.requireNonNull(parentTabId, "parentTabId");
        return SELECTED_BY_PARENT.getOrDefault(parentTabId, parentTabId);
    }

    public static void select(ResourceLocation parentTabId, ResourceLocation tabId) {
        Objects.requireNonNull(parentTabId, "parentTabId");
        Objects.requireNonNull(tabId, "tabId");
        SELECTED_BY_PARENT.put(parentTabId, tabId);
    }

    public static int scroll(ResourceLocation parentTabId) {
        Objects.requireNonNull(parentTabId, "parentTabId");
        return SCROLL_BY_PARENT.getOrDefault(parentTabId, 0);
    }

    public static void scroll(ResourceLocation parentTabId, int value, int maximum) {
        Objects.requireNonNull(parentTabId, "parentTabId");
        SCROLL_BY_PARENT.put(parentTabId, Mth.clamp(value, 0, Math.max(0, maximum)));
    }

    public static boolean collapsed() {
        return collapsed;
    }

    public static void toggleCollapsed() {
        collapsed = !collapsed;
    }
}