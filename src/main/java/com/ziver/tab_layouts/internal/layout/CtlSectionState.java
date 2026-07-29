package com.ziver.tab_layouts.internal.layout;

import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class CtlSectionState {
    private CtlSectionState() {}

    private static final Set<SectionKey> COLLAPSED = new HashSet<>();

    public static boolean isCollapsed(ResourceLocation tabId, ResourceLocation pageId, ResourceLocation sectionId) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(pageId, "pageId");
        Objects.requireNonNull(sectionId, "sectionId");

        return COLLAPSED.contains(new SectionKey(tabId, pageId, sectionId));
    }

    public static boolean toggle(ResourceLocation tabId, ResourceLocation pageId, ResourceLocation sectionId) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(pageId, "pageId");
        Objects.requireNonNull(sectionId, "sectionId");

        SectionKey key = new SectionKey(tabId, pageId, sectionId);

        if (COLLAPSED.remove(key)) return false;

        COLLAPSED.add(key);
        return true;
    }

    public static void expand(ResourceLocation tabId, ResourceLocation pageId, ResourceLocation sectionId) {
        COLLAPSED.remove(new SectionKey(tabId, pageId, sectionId));
    }

    public static void collapse(ResourceLocation tabId, ResourceLocation pageId, ResourceLocation sectionId) {
        COLLAPSED.add(new SectionKey(tabId, pageId, sectionId));
    }

    public static void clear() {
        COLLAPSED.clear();
    }

    private record SectionKey(ResourceLocation tabId, ResourceLocation pageId, ResourceLocation sectionId) {}
}