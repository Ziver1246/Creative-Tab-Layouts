package com.ziver.tab_layouts.api.view;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Immutable resolved snapshot of a CTL page, including copied entry stacks and ordered sections. */
public record CtlPageView(ResourceLocation id, Component title, Type type, int index, List<ItemStack> entries, List<CtlSectionView> sections, boolean customBanner) {

    public CtlPageView {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(type, "type");
        entries = entries.stream().map(ItemStack::copy).toList();
        sections = List.copyOf(sections);
    }

    public boolean isOverview() {
        return type == Type.OVERVIEW;
    }

    public boolean hasDirectEntries() {
        return !entries.isEmpty();
    }

    public boolean hasSections() {
        return !sections.isEmpty();
    }

    public Optional<CtlSectionView> section(ResourceLocation sectionId) {
        Objects.requireNonNull(sectionId, "sectionId");

        for (CtlSectionView section : sections) {
            if (section.id().equals(sectionId)) return Optional.of(section);
        }

        return Optional.empty();
    }

    public enum Type {
        OVERVIEW,
        BASE,
        ADDON,
        FALLBACK
    }
}