package com.ziver.tab_layouts.api;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.CreativeTabLayouts;
import com.ziver.tab_layouts.client.render.CtlVisualRenderer;
import com.ziver.tab_layouts.client.util.CtlUiLayouts;
import com.ziver.tab_layouts.internal.layout.*;
import com.ziver.tab_layouts.internal.registry.CtlSubtabRegistry;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import com.ziver.tab_layouts.internal.registry.CtlVisualRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Public extension API for querying resolved Creative Tab Layouts data.
 *
 * <p>This API exposes immutable semantic snapshots instead of CTL's internal
 * creative-menu representation. Consumers are responsible for deciding how
 * pages, sections and entries are arranged in their own interface.</p>
 */
public final class CtlApiExtensions {
    private CtlApiExtensions() {}

    /**
     * Returns whether the specified creative tab is currently controlled by CTL.
     *
     * <p>A built-in vanilla layout disabled through CTL's configuration is not
     * considered controlled while that configuration is disabled.</p>
     *
     * @param tabId registry ID of the creative tab
     * @return {@code true} when CTL currently controls the tab
     */
    public static boolean isTabControlled(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlTabRegistry.isControlled(tabId);
    }

    public static boolean areBuiltinVanillaLayoutsEnabled() {
        return Config.ENABLE_BUILTIN_VANILLA_LAYOUTS.get();
    }

    public static boolean areSubtabsEnabled() {
        return Config.ENABLE_SUBTABS.get();
    }

    public static boolean isCreativeConfigButtonEnabled() {
        return Config.SHOW_CREATIVE_CONFIG_BUTTON.get();
    }

    public static boolean isSubtab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.isSubtab(tabId);
    }

    public static boolean hasSubtabs(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.hasSubtabs(tabId);
    }

    public static Optional<ResourceLocation> getParentTab(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.parent(tabId);
    }

    public static List<ResourceLocation> getSubtabs(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlSubtabRegistry.subtabs(tabId);
    }

    public static Optional<SubtabGroupView> getSubtabGroup(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");

        ResourceLocation parentId = CtlSubtabRegistry.root(tabId);
        List<ResourceLocation> subtabs = CtlSubtabRegistry.subtabs(parentId);

        if (subtabs.isEmpty()) return Optional.empty();

        return Optional.of(new SubtabGroupView(parentId, subtabs));
    }

    /**
     * Returns whether the specified tab has at least one active CTL page.
     *
     * @param tabId registry ID of the creative tab
     * @return {@code true} when the tab is controlled and has pages
     */
    public static boolean hasPages(ResourceLocation tabId) {
        Objects.requireNonNull(tabId, "tabId");
        return CtlTabRegistry.hasPages(tabId);
    }

    /**
     * Creates a resolved immutable snapshot of the specified CTL tab.
     *
     * <p>Entry placement, dynamic entries, priorities, insertion order and
     * section ordering are resolved before the snapshot is returned.</p>
     *
     *<p>The overload receiving original items also includes CTL fallback pages.
     * The overload without original items only exposes declared CTL pages.</p>
     *
     * @param tabId registry ID of the creative tab
     * @param registries registry provider used to resolve dynamic entries
     * @return resolved tab snapshot, or an empty optional when CTL does not
     * control the tab or the tab has no pages
     */
    public static Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries) {
        return getTabView(tabId, registries, List.of());
    }

    public static Optional<TabView> getTabView(ResourceLocation tabId, HolderLookup.Provider registries, List<ItemStack> originalItems) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(registries, "registries");
        Objects.requireNonNull(originalItems, "originalItems");

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || layout.isEmpty()) return Optional.empty();

        List<CtlPage> pages = layout.orderedPages();
        List<PageView> pageViews = new ArrayList<>(pages.size());

        for (int pageIndex = 0; pageIndex < pages.size(); pageIndex++) {
            pageViews.add(createPageView(pages.get(pageIndex), pageIndex, registries));
        }

        pageViews.addAll(createFallbackPageViews(tabId, layout, originalItems, registries, pageViews.size()));

        return Optional.of(new TabView(tabId, pageViews));
    }

    /**
     * Returns a resolved page snapshot by its index in CTL's current page order.
     *
     * @param tabId registry ID of the creative tab
     * @param pageIndex zero-based page index
     * @param registries registry provider used to resolve dynamic entries
     * @return resolved page snapshot, or an empty optional when unavailable
     */
    public static Optional<PageView> getPageView(ResourceLocation tabId, int pageIndex, HolderLookup.Provider registries) {
        Objects.requireNonNull(tabId, "tabId");
        Objects.requireNonNull(registries, "registries");

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || pageIndex < 0 || pageIndex >= layout.pageCount()) return Optional.empty();

        CtlPage page = layout.orderedPages().get(pageIndex);
        return Optional.of(createPageView(page, pageIndex, registries));
    }

    private static PageView createPageView(CtlPage page, int pageIndex, HolderLookup.Provider registries) {
        List<ItemStack> entries = page.type() == CtlPageType.OVERVIEW ? List.of() : resolveEntries(page.entries(), registries);
        List<CtlSection> sections = page.type() == CtlPageType.OVERVIEW ? List.of() : orderedSections(page.sections());
        List<SectionView> sectionViews = new ArrayList<>(sections.size());

        for (CtlSection section : sections) {
            sectionViews.add(new SectionView(section.id(), section.title(), toPublicType(section.type()), resolveEntries(section.entries(), registries), CtlVisualRegistry.hasHeader(section.id())));
        }

        return new PageView(page.id(), page.title(), toPublicType(page.type()), pageIndex, entries, sectionViews, CtlVisualRegistry.hasBanner(page.id()));
    }

    private static List<PageView> createFallbackPageViews(ResourceLocation tabId, CtlTabLayout layout, List<ItemStack> originalItems, HolderLookup.Provider registries, int firstPageIndex) {
        if (originalItems.isEmpty()) return List.of();

        List<CtlFallbackPageBuilder.CtlFallbackPage> fallbackPages = CtlFallbackPageBuilder.resolve(tabId, layout, originalItems, registries);
        if (fallbackPages.isEmpty()) return List.of();

        List<PageView> result = new ArrayList<>(fallbackPages.size());

        for (int index = 0; index < fallbackPages.size(); index++) {
            CtlFallbackPageBuilder.CtlFallbackPage fallbackPage = fallbackPages.get(index);
            List<SectionView> sections = fallbackPage.sections().stream()
                    .map(section -> new SectionView(section.id(), section.title(), SectionType.FALLBACK, section.items(), false))
                    .toList();

            result.add(new PageView(fallbackPage.id(), fallbackPage.title(), PageType.FALLBACK, firstPageIndex + index, List.of(), sections, false));
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

    private static PageType toPublicType(CtlPageType type) {
        return switch (type) {
            case OVERVIEW -> PageType.OVERVIEW;
            case BASE -> PageType.BASE;
            case ADDON -> PageType.ADDON;
        };
    }

    private static SectionType toPublicType(CtlSectionType type) {
        return switch (type) {
            case BASE -> SectionType.BASE;
            case ADDON -> SectionType.ADDON;
        };
    }

    private static List<ItemStack> copyStacks(List<ItemStack> stacks) {
        return stacks.stream().map(ItemStack::copy).toList();
    }

    /**
     * Public client-only CTL rendering functions.
     *
     * <p>Consumers should only access this class from client code after
     * confirming that CTL is installed.</p>
     */
    public static final class Client {
        private Client() {}

        private static final ResourceLocation DEFAULT_ANIMATION_CONTEXT = CreativeTabLayouts.id("external");

        /**
         * Returns CTL's preferred section-header dimensions.
         *
         * <p>The width may be adapted to the consumer's available area. The
         * preferred height should normally be preserved because header textures
         * and animations are authored for that height.</p>
         */
        public static Size getPreferredHeaderSize() {
            return new Size(CtlUiLayouts.HEADER_ROW_WIDTH, CtlUiLayouts.HEADER_ROW_HEIGHT);
        }

        /**
         * Returns CTL's preferred overview-banner dimensions.
         *
         * <p>Banner visuals are authored for this aspect ratio.</p>
         */
        public static Size getPreferredBannerSize() {
            return new Size(CtlUiLayouts.BANNER_WIDTH, CtlUiLayouts.BANNER_HEIGHT);
        }

        /**
         * Returns whether a custom CTL header visual exists for the section.
         */
        public static boolean hasHeaderVisual(ResourceLocation sectionId) {
            Objects.requireNonNull(sectionId, "sectionId");
            return CtlVisualRegistry.hasHeader(sectionId);
        }

        /**
         * Returns whether a custom CTL banner visual exists for the page.
         */
        public static boolean hasBannerVisual(ResourceLocation pageId) {
            Objects.requireNonNull(pageId, "pageId");
            return CtlVisualRegistry.hasBanner(pageId);
        }

        /**
         * Renders a CTL section header inside arbitrary bounds.
         *
         * <p>This method only renders CTL's custom visual. It returns
         * {@code false} when the tab, page, section or visual does not exist so
         * the consumer can render its own fallback frame.</p>
         *
         * <p>The default external animation context is used. Developer visual
         * debug information is enabled when permitted by CTL's configuration.</p>
         *
         * @param graphics GUI graphics
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param sectionId section ID
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the section header is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @return {@code true} when CTL rendered a custom header visual
         */
        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return renderSectionHeader(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, true);
        }

        /**
         * Renders a CTL section header inside arbitrary bounds.
         *
         * <p>The default external animation context is used. This overload allows
         * integrations to suppress CTL's developer visual debug information without
         * changing CTL's global configuration.</p>
         *
         * @param graphics GUI graphics
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param sectionId section ID
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the section header is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @param allowDebug whether CTL may render developer visual debug information
         * @return {@code true} when CTL rendered a custom header visual
         */
        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            return renderSectionHeader(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        /**
         * Renders a CTL section header inside arbitrary bounds using an independent
         * animation context.
         *
         * <p>Different contexts maintain independent animation states. Developer
         * visual debug information is enabled when permitted by CTL's configuration.</p>
         *
         * @param graphics GUI graphics
         * @param animationContext animation-state context ID
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param sectionId section ID
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the section header is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @return {@code true} when CTL rendered a custom header visual
         */
        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return renderSectionHeader(graphics, animationContext, tabId, pageIndex, sectionId, x, y, width, height, hovered, mouseX, mouseY, true);
        }

        /**
         * Renders a CTL section header inside arbitrary bounds using an independent
         * animation context.
         *
         * <p>This is the complete overload. Different contexts maintain independent
         * animation states, allowing the same visual to be rendered simultaneously
         * in multiple interfaces without sharing hover or pause state.</p>
         *
         * @param graphics GUI graphics
         * @param animationContext animation-state context ID
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param sectionId section ID
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the section header is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @param allowDebug whether CTL may render developer visual debug information
         * @return {@code true} when CTL rendered a custom header visual
         */
        public static boolean renderSectionHeader(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, ResourceLocation sectionId, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            Objects.requireNonNull(graphics, "graphics");
            Objects.requireNonNull(animationContext, "animationContext");
            Objects.requireNonNull(tabId, "tabId");
            Objects.requireNonNull(sectionId, "sectionId");

            CtlPage page = findPage(tabId, pageIndex);
            if (page == null || page.type() == CtlPageType.OVERVIEW) return false;

            CtlSection section = page.section(sectionId);
            if (section == null) return false;

            return CtlVisualRenderer.renderHeader(graphics, Minecraft.getInstance().font, animationContext, tabId, pageIndex, section, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        /**
         * Renders a CTL overview banner inside arbitrary bounds.
         *
         * <p>This method only renders CTL's custom visual. It returns
         * {@code false} when the tab, page or visual does not exist so the
         * consumer can render its own fallback frame.</p>
         *
         * <p>The default external animation context is used. Developer visual
         * debug information is enabled when permitted by CTL's configuration.</p>
         *
         * @param graphics GUI graphics
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the banner is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @return {@code true} when CTL rendered a custom banner visual
         */
        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return renderPageBanner(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, true);
        }

        /**
         * Renders a CTL overview banner inside arbitrary bounds.
         *
         * <p>The default external animation context is used. This overload allows
         * integrations to suppress CTL's developer visual debug information without
         * changing CTL's global configuration.</p>
         *
         * @param graphics GUI graphics
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the banner is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @param allowDebug whether CTL may render developer visual debug information
         * @return {@code true} when CTL rendered a custom banner visual
         */
        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            return renderPageBanner(graphics, DEFAULT_ANIMATION_CONTEXT, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        /**
         * Renders a CTL overview banner inside arbitrary bounds using an independent
         * animation context.
         *
         * <p>Different contexts maintain independent animation states. Developer
         * visual debug information is enabled when permitted by CTL's configuration.</p>
         *
         * @param graphics GUI graphics
         * @param animationContext animation-state context ID
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the banner is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @return {@code true} when CTL rendered a custom banner visual
         */
        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY) {
            return renderPageBanner(graphics, animationContext, tabId, pageIndex, x, y, width, height, hovered, mouseX, mouseY, true);
        }

        /**
         * Renders a CTL overview banner inside arbitrary bounds using an independent
         * animation context.
         *
         * <p>This is the complete overload. Different contexts maintain independent
         * animation states, allowing the same visual to be rendered simultaneously
         * in multiple interfaces without sharing hover or pause state.</p>
         *
         * @param graphics GUI graphics
         * @param animationContext animation-state context ID
         * @param tabId controlled creative-tab ID
         * @param pageIndex page index from {@link PageView#index()}
         * @param x destination X
         * @param y destination Y
         * @param width destination width
         * @param height destination height
         * @param hovered whether the banner is hovered
         * @param mouseX mouse X
         * @param mouseY mouse Y
         * @param allowDebug whether CTL may render developer visual debug information
         * @return {@code true} when CTL rendered a custom banner visual
         */
        public static boolean renderPageBanner(GuiGraphics graphics, ResourceLocation animationContext, ResourceLocation tabId, int pageIndex, int x, int y, int width, int height, boolean hovered, int mouseX, int mouseY, boolean allowDebug) {
            Objects.requireNonNull(graphics, "graphics");
            Objects.requireNonNull(animationContext, "animationContext");
            Objects.requireNonNull(tabId, "tabId");

            CtlPage page = findPage(tabId, pageIndex);
            if (page == null || page.type() != CtlPageType.OVERVIEW) return false;

            return CtlVisualRenderer.renderBanner(graphics, animationContext, tabId, pageIndex, page, x, y, width, height, hovered, mouseX, mouseY, allowDebug);
        }

        @Nullable
        private static CtlPage findPage(ResourceLocation tabId, int pageIndex) {
            CtlTabLayout layout = CtlTabRegistry.get(tabId);
            if (layout == null || pageIndex < 0 || pageIndex >= layout.pageCount()) return null;

            return layout.orderedPages().get(pageIndex);
        }
    }

    /**
     * Immutable resolved view of a controlled CTL tab.
     *
     * @param id creative-tab registry ID
     * @param pages ordered declared CTL pages
     */
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

    /**
     * Immutable resolved view of a CTL page.
     *
     * @param id page ID
     * @param title translated page title component
     * @param type page type
     * @param index index in CTL's current page order
     * @param entries direct page entries rendered before sections
     * @param sections ordered page sections
     * @param customBanner whether the page has a custom CTL banner visual
     */
    public record PageView(ResourceLocation id, Component title, PageType type, int index, List<ItemStack> entries, List<SectionView> sections, boolean customBanner) {
        public PageView {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(type, "type");

            entries = copyStacks(entries);
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

    /**
     * Immutable resolved view of a CTL section.
     *
     * @param id section ID
     * @param title translated section title component
     * @param type section type
     * @param items resolved section entries
     * @param customHeader whether the section has a custom CTL header visual
     */
    public record SectionView(ResourceLocation id, Component title, SectionType type, List<ItemStack> items, boolean customHeader) {
        public SectionView {
            Objects.requireNonNull(id, "id");
            Objects.requireNonNull(title, "title");
            Objects.requireNonNull(type, "type");

            items = copyStacks(items);
        }

        public boolean isEmpty() {
            return items.isEmpty();
        }
    }

    /**
     * Preferred visual dimensions supplied by CTL.
     */
    public record Size(int width, int height) {
        public Size {
            if (width <= 0) throw new IllegalArgumentException("Width must be greater than zero: " + width);
            if (height <= 0) throw new IllegalArgumentException("Height must be greater than zero: " + height);
        }
    }

    public enum PageType {
        OVERVIEW,
        BASE,
        ADDON,
        FALLBACK
    }

    public enum SectionType {
        BASE,
        ADDON,
        FALLBACK
    }
}