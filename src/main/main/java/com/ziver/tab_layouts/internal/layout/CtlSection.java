package com.ziver.tab_layouts.internal.layout;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record CtlSection(ResourceLocation id, Component title, CtlSectionType type, long priority, long insertionOrder, List<CtlEntry> entries) {
    public CtlSection withAdditionalEntries(List<CtlEntry> additionalEntries) {
        List<CtlEntry> mergedEntries = new ArrayList<>(entries);
        mergedEntries.addAll(additionalEntries);

        return new CtlSection(id, title, type, priority, insertionOrder, List.copyOf(mergedEntries));
    }

    public long nextEntryInsertionOrder() {
        long max = -1L;

        for (CtlEntry entry : entries) {
            max = Math.max(max, entry.insertionOrder());
        }

        return max + 1L;
    }
}