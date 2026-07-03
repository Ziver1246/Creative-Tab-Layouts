package com.ziver.tab_layouts.internal.layout;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record CtlPage(ResourceLocation id, Component title, CtlPageType type, long priority, long insertionOrder, List<CtlEntry> entries, List<CtlSection> sections) {

    public CtlBuiltPage build(HolderLookup.Provider registries) {
        if (type == CtlPageType.OVERVIEW) {
            return buildOverviewPage();
        }

        return buildSectionedPage(registries);
    }

    private CtlBuiltPage buildOverviewPage() {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < 45; i++) {
            items.add(ItemStack.EMPTY);
        }

        return new CtlBuiltPage(items, List.of(), true);
    }

    private CtlBuiltPage buildSectionedPage(HolderLookup.Provider registries) {
        List<ItemStack> items = new ArrayList<>();
        List<CtlRenderedSection> renderedSections = new ArrayList<>();

        if (!entries.isEmpty()) {
            List<CtlEntry> orderedEntries = CtlEntryPlacementResolver.resolve(entries, registries);
            addEntries(items, orderedEntries, registries);
            padToFullRow(items);
        }

        for (CtlSection section : orderedSections()) {
            int headerRow = items.size() / 9;
            renderedSections.add(new CtlRenderedSection(section, headerRow));

            for (int i = 0; i < 9; i++) {
                items.add(ItemStack.EMPTY);
            }

            List<CtlEntry> orderedEntries = CtlEntryPlacementResolver.resolve(section.entries(), registries);
            addEntries(items, orderedEntries, registries);
            padToFullRow(items);
        }

        if (items.isEmpty()) {
            for (int i = 0; i < 45; i++) {
                items.add(ItemStack.EMPTY);
            }
        }

        return new CtlBuiltPage(items, renderedSections, false);
    }

    private static void addEntries(List<ItemStack> items, List<CtlEntry> entries, HolderLookup.Provider registries) {
        for (CtlEntry entry : entries) {
            List<ItemStack> stacks = entry.buildStacks(registries);

            for (ItemStack stack : stacks) {
                if (!stack.isEmpty() || entry.keepIfEmpty()) {
                    items.add(stack);
                }
            }
        }
    }

    private static void padToFullRow(List<ItemStack> items) {
        int padding = 9 - items.size() % 9;

        if (padding < 9) {
            for (int i = 0; i < padding; i++) {
                items.add(ItemStack.EMPTY);
            }
        }
    }

    private List<CtlSection> orderedSections() {
        List<CtlSection> base = sections.stream()
                .filter(section -> section.type() == CtlSectionType.BASE)
                .sorted(Comparator.comparingLong(CtlSection::priority).thenComparingLong(CtlSection::insertionOrder))
                .toList();

        List<CtlSection> addon = sections.stream()
                .filter(section -> section.type() == CtlSectionType.ADDON)
                .sorted(Comparator.comparingLong(CtlSection::priority).thenComparingLong(CtlSection::insertionOrder))
                .toList();

        List<CtlSection> ordered = new ArrayList<>(sections.size());
        ordered.addAll(base);
        ordered.addAll(addon);

        return ordered;
    }

    public CtlPage withAdditionalContent(CtlPage contribution) {
        List<CtlEntry> mergedEntries = new ArrayList<>(entries);
        mergedEntries.addAll(contribution.entries());

        List<CtlSection> mergedSections = new ArrayList<>(sections);

        for (CtlSection contributionSection : contribution.sections()) {
            int index = findSectionIndex(mergedSections, contributionSection.id());

            if (index >= 0) {
                throw new IllegalArgumentException("Duplicate CTL section id '" + contributionSection.id() + "' while contributing to page '" + id + "'. " + "Use contributeSection(...) to add entries to an existing section.");
            }

            mergedSections.add(contributionSection);
        }

        return new CtlPage(id, title, type, priority, insertionOrder, List.copyOf(mergedEntries), List.copyOf(mergedSections));
    }

    public CtlPage withAdditionalSectionContent(CtlSection contribution) {
        List<CtlSection> mergedSections = new ArrayList<>(sections);
        int index = findSectionIndex(mergedSections, contribution.id());

        if (index < 0) throw new IllegalArgumentException("Cannot contribute to missing CTL section '" + contribution.id() + "' in page '" + id + "'");

        CtlSection existing = mergedSections.get(index);
        mergedSections.set(index, existing.withAdditionalEntries(contribution.entries()));

        return new CtlPage(id, title, type, priority, insertionOrder, entries, List.copyOf(mergedSections));
    }

    public boolean hasSection(ResourceLocation sectionId) {
        return findSectionIndex(sections, sectionId) >= 0;
    }

    private static int findSectionIndex(List<CtlSection> sections, ResourceLocation sectionId) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).id().equals(sectionId)) return i;
        }

        return -1;
    }

    public CtlSection section(ResourceLocation sectionId) {
        for (CtlSection section : sections) {
            if (section.id().equals(sectionId)) {
                return section;
            }
        }

        return null;
    }

    public long nextEntryInsertionOrder() {
        long max = -1L;

        for (CtlEntry entry : entries) {
            max = Math.max(max, entry.insertionOrder());
        }

        return max + 1L;
    }

    public long nextSectionInsertionOrder() {
        long max = -1L;

        for (CtlSection section : sections) {
            max = Math.max(max, section.insertionOrder());
        }

        return max + 1L;
    }
}