package com.ziver.tab_layouts.internal.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class CtlSubtabRegistry {
    private static final Map<ResourceLocation, ResourceLocation> PARENT_BY_SUBTAB = new LinkedHashMap<>();
    private static final Map<ResourceLocation, List<ResourceLocation>> SUBTABS_BY_PARENT = new LinkedHashMap<>();

    private CtlSubtabRegistry() {}

    public static void register(ResourceLocation subtabId, ResourceLocation parentTabId) {
        Objects.requireNonNull(subtabId, "subtabId");
        Objects.requireNonNull(parentTabId, "parentTabId");

        if (subtabId.equals(parentTabId))
            throw new IllegalArgumentException("Creative tab '" + subtabId + "' cannot be its own parent");

        if (PARENT_BY_SUBTAB.containsKey(parentTabId))
            throw new IllegalStateException("Creative tab '" + parentTabId + "' is already a subtab and cannot contain subtabs");

        if (hasSubtabs(subtabId))
            throw new IllegalStateException("Creative tab '" + subtabId + "' already contains subtabs and cannot become a subtab");

        ResourceLocation currentParent = PARENT_BY_SUBTAB.putIfAbsent(subtabId, parentTabId);

        if (currentParent != null && !currentParent.equals(parentTabId))
            throw new IllegalStateException("Creative tab '" + subtabId + "' is already a subtab of '" + currentParent + "'");

        List<ResourceLocation> subtabs = SUBTABS_BY_PARENT.computeIfAbsent(parentTabId, ignored -> new ArrayList<>());

        if (!subtabs.contains(subtabId)) subtabs.add(subtabId);
    }

    public static boolean isSubtab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return PARENT_BY_SUBTAB.containsKey(tabId);
    }

    public static boolean hasSubtabs(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");

        List<ResourceLocation> subtabs = SUBTABS_BY_PARENT.get(tabId);
        return subtabs != null && !subtabs.isEmpty();
    }

    public static Optional<ResourceLocation> parent(ResourceLocation subtabId) {
        Objects.requireNonNull(subtabId, "subtabId");
        return Optional.ofNullable(PARENT_BY_SUBTAB.get(subtabId));
    }

    public static List<ResourceLocation> subtabs(ResourceLocation parentTabId) {
        Objects.requireNonNull(parentTabId, "parentTabId");

        List<ResourceLocation> subtabs = SUBTABS_BY_PARENT.get(parentTabId);
        return subtabs == null ? List.of() : List.copyOf(subtabs);
    }

    public static ResourceLocation root(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return PARENT_BY_SUBTAB.getOrDefault(tabId, tabId);
    }

    public static List<ResourceLocation> group(ResourceLocation tabId) {
        ResourceLocation rootId = root(tabId);
        List<ResourceLocation> group = new ArrayList<>();

        group.add(rootId);
        group.addAll(subtabs(rootId));

        return List.copyOf(group);
    }
}