package com.ziver.tab_layouts.api;

import com.ziver.tab_layouts.CtlConfig;
import com.ziver.tab_layouts.api.client.CtlVisualSize;
import com.ziver.tab_layouts.api.client.CtlVisuals;
import com.ziver.tab_layouts.api.view.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @deprecated Use {@link CtlViews} and {@link CtlVisuals}.
 * This compatibility facade will be removed in CTL 2.0.0.
 */
@Deprecated(forRemoval = true)
public final class CtlApiExtensions {
    private CtlApiExtensions() {}

    public static boolean isTabControlled(ResourceLocation tabId) {
        return CtlViews.isTabControlled(tabId);
    }

    /**
     * @deprecated CTL configuration is no longer part of the public extension API.
     */
    @Deprecated(forRemoval = true)
    public static boolean areBuiltinVanillaLayoutsEnabled() {
        return CtlConfig.ENABLE_BUILTIN_VANILLA_LAYOUTS.get();
    }

    /**
     * @deprecated CTL configuration is no longer part of the public extension API.
     */
    @Deprecated(forRemoval = true)
    public static boolean areSubtabsEnabled() {
        return CtlConfig.ENABLE_SUBTABS.get();
    }

    /**
     * @deprecated CTL configuration is no longer part of the public extension API.
     */
    @Deprecated(forRemoval = true)
    public static boolean isCreativeConfigButtonEnabled() {
        return CtlConfig.SHOW_CREATIVE_CONFIG_BUTTON.get();
    }

    public static boolean isSubtab(ResourceLocation tabId) {
        return CtlViews.isSubtab(tabId);
    }

    public static boolean hasSubtabs(ResourceLocation tabId) {
        return CtlViews.hasSubtabs(tabId);
    }

    public static Optional<ResourceLocation> getParentTab(ResourceLocation tabId) {
        return CtlViews.getParentTab(tabId);
    }

    public static List<ResourceLocation> getSubtabs(ResourceLocation tabId) {
        return CtlViews.getSubtabs(tabId);
    }

    public static Optional<SubtabGroupView> getSubtabGroup(ResourceLocation tabId) {
        return CtlViews.getSubtabGroup(tabId).map(CtlApiExtensions::legacy);
    }

    public static boolean hasPages(ResourceLocation tabId) {
        return CtlViews.hasPages(tabId);
    }

    public static Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries) {
        return CtlViews.getTabView(tabId, registries).map(CtlApiExtensions::legacy);
    }

    public static Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries, List<ItemStack> originalItems) {
        return CtlViews.getTabView(tabId, registries, originalItems).map(CtlApiExtensions::legacy);
    }

    public static Optional<PageView> getPageView(ResourceLocation tabId, int pageIndex, HolderLookup.Provider registries) {
        return CtlViews.getPageView(tabId, pageIndex, registries).map(CtlApiExtensions::legacy);
    }

    private static TabView legacy(CtlTabView view) {
        return new TabView(view.id(), view.pages().stream().map(CtlApiExtensions::legacy).toList());
    }

    private static PageView legacy(CtlPageView view) {
        return new PageView(view.id(), view.title(), legacy(view.type()), view.index(), view.entries(), view.sections().stream().map(CtlApiExtensions::legacy).toList(), view.customBanner());
    }

    private static SectionView legacy(CtlSectionView view) {
        return new SectionView(view.id(), view.title(), legacy(view.type()), view.items(), view.customHeader());
    }

    private static SubtabGroupView legacy(CtlSubtabGroupView view) {
        return new SubtabGroupView(view.parentId(), view.subtabs());
    }

    private static PageType legacy(CtlPageView.Type type) {
        return switch (type) {
            case OVERVIEW -> PageType.OVERVIEW;
            case BASE -> PageType.BASE;
            case ADDON -> PageType.ADDON;
            case FALLBACK -> PageType.FALLBACK;
        };
    }

    private static SectionType legacy(CtlSectionView.Type type) {
        return switch (type) {
            case BASE -> SectionType.BASE;
            case ADDON -> SectionType.ADDON;
            case FALLBACK -> SectionType.FALLBACK;
        };
    }

    @Deprecated(forRemoval = true)
    public static final class Client {
        private Client() {}

        public static Size getPreferredHeaderSize() {
            return legacy(CtlVisuals.getPreferredHeaderSize());
        }

        public static Size getPreferredBannerSize() {
            return legacy(CtlVisuals.getPreferredBannerSize());
        }

        public static boolean hasHeaderVisual(ResourceLocation sectionId) {
            return CtlVisuals.hasHeaderVisual(sectionId);
        }

        public static boolean hasBannerVisual(ResourceLocation pageId) {
            return CtlVisuals.hasBannerVisual(pageId);
        }

        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return CtlVisuals.renderSectionHeader(graphics, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY);
        }

        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            return CtlVisuals.renderSectionHeader(graphics, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return CtlVisuals.renderSectionHeader(graphics, animationContext, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY);
        }

        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            return CtlVisuals.renderSectionHeader(graphics, animationContext, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return CtlVisuals.renderPageBanner(graphics, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY);
        }

        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            return CtlVisuals.renderPageBanner(graphics, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return CtlVisuals.renderPageBanner(graphics, animationContext, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY);
        }

        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            return CtlVisuals.renderPageBanner(graphics, animationContext, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        private static Size legacy(CtlVisualSize size) {
            return new Size(size.width(), size.height());
        }
    }

    @Deprecated(forRemoval = true)
    public record TabView(ResourceLocation id, List<PageView> pages) {
        public TabView {
            Objects.requireNonNull(id, "id");
            pages = List.copyOf(pages);
        }

        public int pageCount() {
            return pages.size();
        }

        public boolean isEmpty() {
            return pages.isEmpty();
        }

        public Optional<PageView> page(int index) {
            if (index < 0 || index >= pages.size()) return Optional.empty();
            return Optional.of(pages.get(index));
        }

        public Optional<PageView> page(ResourceLocation pageId) {
            Objects.requireNonNull(pageId, "pageId");

            for (PageView page : pages) {
                if (page.id().equals(pageId)) return Optional.of(page);
            }

            return Optional.empty();
        }
    }

    @Deprecated(forRemoval = true)
    public record PageView(ResourceLocation id, Component title, PageType type, int index, List<ItemStack> entries, List<SectionView> sections, boolean customBanner) {
        public PageView {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(type, "type");
            entries = entries.stream().map(ItemStack::copy).toList();
            sections = List.copyOf(sections);
        }

        public boolean isOverview() {
            return type == PageType.OVERVIEW;
        }

        public boolean hasDirectEntries() {
            return !entries.isEmpty();
        }

        public boolean hasSections() {
            return !sections.isEmpty();
        }

        public Optional<SectionView> section(ResourceLocation sectionId) {
            Objects.requireNonNull(sectionId, "sectionId");

            for (SectionView section : sections) {
                if (section.id().equals(sectionId)) return Optional.of(section);
            }

            return Optional.empty();
        }
    }

    @Deprecated(forRemoval = true)
    public record SubtabGroupView(ResourceLocation parentId, List<ResourceLocation> subtabs) {
        public SubtabGroupView {
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

    @Deprecated(forRemoval = true)
    public record SectionView(ResourceLocation id, Component title, SectionType type, List<ItemStack> items, boolean customHeader) {
        public SectionView {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(type, "type");
            items = items.stream().map(ItemStack::copy).toList();
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }

    @Deprecated(forRemoval = true)
    public record Size(int width, int height) {
        public Size {
            if (width <= 0) throw new IllegalArgumentException("Width must be greater than zero: " + width);
            if (height <= 0) throw new IllegalArgumentException("Height must be greater than zero: " + height);
        }
    }

    @Deprecated(forRemoval = true)
    public enum PageType {
        OVERVIEW,
        BASE,
        ADDON,
        FALLBACK
    }

    @Deprecated(forRemoval = true)
    public enum SectionType {
        BASE,
        ADDON,
        FALLBACK
    }
}