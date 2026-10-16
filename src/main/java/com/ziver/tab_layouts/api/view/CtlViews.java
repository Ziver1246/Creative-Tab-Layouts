package com.ziver.tab_layouts.api.view;

import com.ziver.tab_layouts.internal.layout.*;
import com.ziver.tab_layouts.internal.layout.entry.CtlEntry;
import com.ziver.tab_layouts.internal.layout.entry.CtlEntryPlacementResolver;
import com.ziver.tab_layouts.internal.layout.fallback.CtlFallbackPageBuilder;
import com.ziver.tab_layouts.internal.registry.CtlSubtabRegistry;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import com.ziver.tab_layouts.internal.registry.CtlVisualRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.*;

/**
 * Read-only entry point for querying resolved CTL state.
 *
 * <p>Returned views are immutable snapshots and do not expose CTL's internal mutable model.</p>
 */
public final class CtlViews {
    private CtlViews() {}

    /** Returns whether CTL currently controls the creative tab. */
    public static boolean isTabControlled(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlTabRegistry.isControlled(tabId);
    }

    /** Returns whether the controlled tab has declared CTL pages. */
    public static boolean hasPages(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlTabRegistry.hasPages(tabId);
    }

    /** Returns whether the tab is registered as a CTL subtab. */
    public static boolean isSubtab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.isSubtab(tabId);
    }

    /** Returns whether the tab owns one or more CTL subtabs. */
    public static boolean hasSubtabs(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.hasSubtabs(tabId);
    }

    /** Returns the immediate parent of a registered subtab, if present. */
    public static Optional<ResourceLocation> getParentTab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.parent(tabId);
    }

    /** Returns the registered child subtabs of the supplied parent tab. */
    public static List<ResourceLocation> getSubtabs(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.subtabs(tabId);
    }

    /** Returns the complete subtab group containing the supplied tab, if one exists. */
    public static Optional<CtlSubtabGroupView> getSubtabGroup(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");

        ResourceLocation parentId = CtlSubtabRegistry.root(tabId);
        List<ResourceLocation> subtabs = CtlSubtabRegistry.subtabs(parentId);

        if (subtabs.isEmpty()) return Optional.empty();

        return Optional.of(new CtlSubtabGroupView(parentId, subtabs));
    }

    /** Resolves declared CTL pages only; generated fallback pages require the overload with original items. */
    public static Optional<CtlTabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries) {
        return getTabView(tabId, registries, List.of());
    }

    /** Resolves a complete tab snapshot, including fallback pages derived from unclaimed original items. */
    public static Optional<CtlTabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries, List<ItemStack> originalItems) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(registries, "registries");
        Objects.requireNonNull(originalItems, "originalItems");

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || layout.isEmpty()) return Optional.empty();

        List<CtlPage> pages = layout.orderedPages();
        List<CtlPageView> pageViews = new ArrayList<>(pages.size());

        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            pageViews.add(createPageView(pages.get(pageIndex), pageIndex, registries));
        }

        pageViews.addAll(createFallbackPageViews(tabId, layout, originalItems, registries, pageViews.size()));

        return Optional.of(new CtlTabView(tabId, pageViews));
    }

    /** Resolves a declared CTL page by current zero-based page index; generated fallback pages are excluded. */
    public static Optional<CtlPageView> getPageView(ResourceLocation tabId, int pageIndex, HolderLookup.Provider registries) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(registries, "registries");

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || pageIndex < 0 || pageIndex >= layout.pageCount()) return Optional.empty();

        CtlPage page = layout.orderedPages().get(pageIndex);
        return Optional.of(createPageView(page, pageIndex, registries));
    }

    private static CtlPageView createPageView(CtlPage page, int pageIndex, HolderLookup.Provider registries) {
        List<ItemStack> entries = page.type() == CtlPageType.OVERVIEW ? List.of() : resolveEntries(page.entries(), registries);
        List<CtlSection> sections = page.type() == CtlPageType.OVERVIEW ? List.of() : orderedSections(page.sections());
        List<CtlSectionView> sectionViews = new ArrayList<>(sections.size());

        for (CtlSection section : sections) {
            sectionViews.add(new CtlSectionView(section.id(), section.title(), toPublicType(section.type()), resolveEntries(section.entries(), registries), CtlVisualRegistry.hasHeader(section.id())));
        }

        return new CtlPageView(page.id(), page.title(), toPublicType(page.type()), pageIndex, entries, sectionViews, CtlVisualRegistry.hasBanner(page.id()));
    }

    private static List<CtlPageView> createFallbackPageViews(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> originalItems, HolderLookup.Provider registries, int firstPageIndex) {
        if (originalItems.isEmpty()) return List.of();

        List<CtlFallbackPageBuilder.CtlFallbackPage> fallbackPages = CtlFallbackPageBuilder.resolve(tabId, layout, originalItems, registries);
        if (fallbackPages.isEmpty()) return List.of();

        List<CtlPageView> result = new ArrayList<>(fallbackPages.size());

        for (int index = 0; index < fallbackPages.size(); index++) {
            CtlFallbackPageBuilder.CtlFallbackPage fallbackPage = fallbackPages.get(index);
            List<CtlSectionView> sections = fallbackPage.sections().stream().map(section -> new CtlSectionView(section.id(), section.title(), CtlSectionView.Type.FALLBACK, section.items(), false)).toList();

            result.add(new CtlPageView(fallbackPage.id(), fallbackPage.title(), CtlPageView.Type.FALLBACK, firstPageIndex + index, List.of(), sections, false));
        }

        return List.copyOf(result);
    }

    private static List<ItemStack> resolveEntries(List<CtlEntry> entries, HolderLookup.Provider registries) {
        if (entries.isEmpty()) return List.of();

        List<CtlEntry> orderedEntries = CtlEntryPlacementResolver.resolve(entries, registries);
        List<ItemStack> stacks = new ArrayList<>();

        for (CtlEntry entry : orderedEntries) {
            for (ItemStack stack : entry.buildStacks(registries)) {
                if (!stack.isEmpty() || entry.keepIfEmpty()) stacks.add(stack.copy());
            }
        }

        return List.copyOf(stacks);
    }

    private static List<CtlSection> orderedSections(List<CtlSection> sections) {
        if (sections.isEmpty()) return List.of();

        Comparator<CtlSection> ordering = Comparator.comparingLong(CtlSection::priority).thenComparingLong(CtlSection::insertionOrder);
        List<CtlSection> ordered = new ArrayList<>(sections.size());

        sections.stream().filter(section -> section.type() == CtlSectionType.BASE).sorted(ordering).forEach(ordered::add);
        sections.stream().filter(section -> section.type() == CtlSectionType.ADDON).sorted(ordering).forEach(ordered::add);

        return List.copyOf(ordered);
    }

    private static CtlPageView.Type toPublicType(CtlPageType type) {
        return switch (type) {
            case OVERVIEW -> CtlPageView.Type.OVERVIEW;
            case BASE -> CtlPageView.Type.BASE;
            case ADDON -> CtlPageView.Type.ADDON;
        };
    }

    private static CtlSectionView.Type toPublicType(CtlSectionType type) {
        return switch (type) {
            case BASE -> CtlSectionView.Type.BASE;
            case ADDON -> CtlSectionView.Type.ADDON;
        };
    }
}