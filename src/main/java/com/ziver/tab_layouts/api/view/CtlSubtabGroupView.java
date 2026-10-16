package com.ziver.tab_layouts.api.view;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record CtlSubtabGroupView(ResourceLocation parentId, List<ResourceLocation> subtabs) {

    public CtlSubtabGroupView {
        Objects.requireNonNull(parentId, "parentId");
        subtabs = List.copyOf(subtabs);
    }

    public List<ResourceLocation> tabs() {
        List<ResourceLocation> tabs = new ArrayList<>(subtabs.size() + 1);
        tabs.add(parentId);
        tabs.addAll(subtabs);
        return List.copyOf(tabs);
    }

    public boolean contains(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return parentId.equals(tabId) || subtabs.contains(tabId);
    }
}