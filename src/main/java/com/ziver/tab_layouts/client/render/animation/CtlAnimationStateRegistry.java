package com.ziver.tab_layouts.client.render.animation;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class CtlAnimationStateRegistry {
    private static final Map<Key, CtlAnimationState> STATES = new HashMap<>();

    private CtlAnimationStateRegistry() {}

    public static CtlAnimationState header(ResourceLocation tabId, int pageIndex, ResourceLocation sectionId) {
        return STATES.computeIfAbsent(new Key(CtlAnimationTarget.HEADER, tabId, pageIndex, sectionId), ignored -> new CtlAnimationState());
    }

    public static CtlAnimationState banner(ResourceLocation tabId, int pageIndex, ResourceLocation pageId) {
        return STATES.computeIfAbsent(new Key(CtlAnimationTarget.BANNER, tabId, pageIndex, pageId), ignored -> new CtlAnimationState());
    }

    public static void clear() {
        STATES.clear();
    }

    private enum CtlAnimationTarget {
        HEADER,
        BANNER
    }

    private record Key(CtlAnimationTarget target, ResourceLocation tabId, int pageIndex, ResourceLocation visualId) {}
}