package com.ziver.tab_layouts.client.render.animation;

import com.ziver.tab_layouts.CreativeTabLayouts;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class CtlAnimationStateRegistry {
    public static final ResourceLocation CREATIVE_CONTEXT = CreativeTabLayouts.id("creative");

    public static final ResourceLocation DEFAULT_EXTERNAL_CONTEXT = CreativeTabLayouts.id("external");

    private static final Map<Key, CtlAnimationState> STATES = new HashMap<>();

    private CtlAnimationStateRegistry() {}

    public static CtlAnimationState header(ResourceLocation contextId, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId) {
        Objects.requireNonNull(contextId, "contextId");

        return STATES.computeIfAbsent(new Key(contextId, CtlAnimationTarget.HEADER, tabId, pageIndex, sectionId), ignored -> new CtlAnimationState());
    }

    public static CtlAnimationState banner(ResourceLocation contextId, ResourceLocation tabId, int pageIndex, ResourceLocation pageId) {
        Objects.requireNonNull(contextId, "contextId");

        return STATES.computeIfAbsent(new Key(contextId, CtlAnimationTarget.BANNER, tabId, pageIndex, pageId), ignored -> new CtlAnimationState());
    }

    public static void clear() {
        STATES.clear();
    }

    private enum CtlAnimationTarget {
        HEADER,
        BANNER
    }

    private record Key(ResourceLocation contextId, CtlAnimationTarget target, ResourceLocation tabId, int pageIndex, ResourceLocation visualId) {}
}