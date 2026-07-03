package com.ziver.tab_layouts.internal.registry;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.internal.layout.CtlTabLayout;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class CtlTabRegistry {
    private static final Map<ResourceLocation, CtlTabLayout> CONTROLLED_TABS = new LinkedHashMap<>();
    private static final Set<ResourceLocation> BUILTIN_VANILLA_TABS = new HashSet<>();

    private CtlTabRegistry() {}

    public static CtlTabLayout controlTab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CONTROLLED_TABS.computeIfAbsent(tabId, CtlTabLayout::new);
    }

    public static CtlTabLayout get(ResourceLocation tabId) {
        if (!isEnabled(tabId)) {
            return null;
        }

        return CONTROLLED_TABS.get(tabId);
    }

    public static boolean isControlled(ResourceLocation tabId) {
        return isEnabled(tabId) && CONTROLLED_TABS.containsKey(tabId);
    }

    public static void markBuiltinVanilla(ResourceLocation tabId) {
        BUILTIN_VANILLA_TABS.add(tabId);
    }

    public static boolean isBuiltinVanilla(ResourceLocation tabId) {
        return BUILTIN_VANILLA_TABS.contains(tabId);
    }

    public static boolean hasPages(ResourceLocation tabId) {
        CtlTabLayout layout = get(tabId);
        return layout != null && layout.pageCount() > 0;
    }

    private static boolean isEnabled(ResourceLocation tabId) {
        if (BUILTIN_VANILLA_TABS.contains(tabId) && !Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get()) {
            return false;
        }

        return true;
    }
}