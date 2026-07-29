package com.ziver.tab_layouts.client.render;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.internal.layout.CtlSubtabState;
import com.ziver.tab_layouts.internal.registry.CtlSubtabRegistry;
import com.ziver.tab_layouts.mixins.CreativeModeInventoryScreenAccessor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CtlSubtabPanel {
    private static final int PANEL_GAP = 4;
    private static final int PANEL_PADDING = 3;
    private static final int ENTRY_SIZE = 22;
    private static final int ICON_SIZE = 16;
    private static final int MAX_VISIBLE_ENTRIES = 4;

    private static final int SCROLLBAR_WIDTH = 3;
    private static final int SCROLLBAR_GAP_LEFT = 2;
    private static final int SCROLLBAR_GAP_RIGHT = 0;
    private static final int SCROLLBAR_MIN_THUMB_HEIGHT = 10;
    private static final int SCROLLBAR_SPACE = SCROLLBAR_GAP_LEFT + SCROLLBAR_WIDTH + SCROLLBAR_GAP_RIGHT;

    private static final int PANEL_BASE_WIDTH = PANEL_PADDING * 2 + ENTRY_SIZE;

    private static final int SCROLLBAR_TRACK = 0xFF39414B;
    private static final int SCROLLBAR_THUMB = 0xFF788391;

    private static final int PANEL_BACKGROUND = 0xCC101010;
    private static final int ENTRY_BACKGROUND = 0x66202020;
    private static final int ENTRY_HOVERED = 0x99404040;
    private static final int ENTRY_SELECTED = 0xCC707070;
    private static final int BORDER_COLOR = 0xFF8B8B8B;

    private ResourceLocation activeRootId;
    private boolean draggingScrollbar;
    private int scrollbarDragOffsetY;

    public void init(CreativeModeTab selectedTab) {
        if (!Config.ENABLE_SUBTABS.get()) {
            this.activeRootId = null;
            return;
        }
        updateActiveGroup(selectedTab);
        clampScroll(selectedTab);
    }

    public void render(CreativeModeInventoryScreen screen, GuiGraphics graphics, CreativeModeTab selectedTab, int mouseX, int mouseY) {
        if (!Config.ENABLE_SUBTABS.get()) return;
        SubtabGroup group = groupFor(selectedTab);
        if (group == null) return;

        updateActiveGroup(selectedTab);
        clampScroll(selectedTab);

        int panelX = panelX(screen, group);
        int panelY = panelY(screen);
        int panelWidth = panelWidth(group);
        int panelHeight = panelHeight(group);
        int visibleEntries = visibleEntries(group);

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 300.0F);

        graphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, PANEL_BACKGROUND);
        renderBorder(graphics, panelX, panelY, panelWidth, panelHeight);

        CreativeModeTab hoveredTab = null;

        for (int visibleIndex = 0; visibleIndex < visibleEntries; visibleIndex++) {
            int tabIndex = scrollOffset() + visibleIndex;
            if (tabIndex >= group.tabs().size()) break;

            CreativeModeTab tab = group.tabs().get(tabIndex);

            int entryX = panelX + PANEL_PADDING;
            int entryY = panelY + PANEL_PADDING + visibleIndex * ENTRY_SIZE;

            boolean hovered = isInside(mouseX, mouseY, entryX, entryY, ENTRY_SIZE, ENTRY_SIZE);
            boolean selected = tab == selectedTab;

            int background = selected ? ENTRY_SELECTED : hovered ? ENTRY_HOVERED : ENTRY_BACKGROUND;
            graphics.fill(entryX, entryY, entryX + ENTRY_SIZE, entryY + ENTRY_SIZE, background);

            if (selected) renderBorder(graphics, entryX, entryY, ENTRY_SIZE, ENTRY_SIZE);

            ItemStack icon = tab.getIconItem();
            int iconX = entryX + (ENTRY_SIZE - ICON_SIZE) / 2;
            int iconY = entryY + (ENTRY_SIZE - ICON_SIZE) / 2;

            graphics.renderItem(icon, iconX, iconY);

            if (hovered) hoveredTab = tab;
        }

        if (maxScroll(group) > 0) renderScrollbar(graphics, group, panelX, panelY, panelHeight);

        graphics.pose().popPose();

        if (hoveredTab != null)
            graphics.renderTooltip(screen.getMinecraft().font, hoveredTab.getDisplayName(), mouseX, mouseY);
    }

    private void renderScrollbar(GuiGraphics graphics, SubtabGroup group, int panelX, int panelY, int panelHeight) {
        int trackX = scrollbarX(panelX);
        int trackY = panelY + PANEL_PADDING;
        int trackHeight = panelHeight - PANEL_PADDING * 2;
        int thumbHeight = scrollbarThumbHeight(group, trackHeight);
        int thumbY = scrollbarThumbY(group, trackY, trackHeight, thumbHeight);

        graphics.fill(trackX, trackY, trackX + SCROLLBAR_WIDTH, trackY + trackHeight, SCROLLBAR_TRACK);
        graphics.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH, thumbY + thumbHeight, SCROLLBAR_THUMB);
    }

    private static int scrollbarX(int panelX) {
        return panelX + PANEL_PADDING + ENTRY_SIZE + SCROLLBAR_GAP_LEFT;
    }

    private static int scrollbarThumbHeight(SubtabGroup group, int trackHeight) {
        int visibleEntries = visibleEntries(group);
        int thumbHeight = Math.max(SCROLLBAR_MIN_THUMB_HEIGHT, trackHeight * visibleEntries / Math.max(1, group.tabs().size()));
        return Math.min(trackHeight, thumbHeight);
    }

    private int scrollbarThumbY(SubtabGroup group, int trackY, int trackHeight, int thumbHeight) {
        int maximumScroll = maxScroll(group);
        int travel = trackHeight - thumbHeight;

        if (maximumScroll <= 0 || travel <= 0) return trackY;
        return trackY + scrollOffset() * travel / maximumScroll;
    }

    public boolean mouseClicked(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab, double mouseX, double mouseY, int button) {
        if (!Config.ENABLE_SUBTABS.get()) return false;
        if (button != 0) return false;

        SubtabGroup group = groupFor(selectedTab);
        if (group == null) return false;

        updateActiveGroup(selectedTab);

        int panelX = panelX(screen, group);
        int panelY = panelY(screen);
        int panelWidth = panelWidth(group);
        int panelHeight = panelHeight(group);
        int visibleEntries = visibleEntries(group);

        if (!isInside(mouseX, mouseY, panelX, panelY, panelWidth, panelHeight)) return false;

        if (maxScroll(group) > 0 && isInsideScrollbar(mouseX, mouseY, group, panelX, panelY, panelHeight)) {
            int trackY = panelY + PANEL_PADDING;
            int trackHeight = panelHeight - PANEL_PADDING * 2;
            int thumbHeight = scrollbarThumbHeight(group, trackHeight);
            int thumbY = scrollbarThumbY(group, trackY, trackHeight, thumbHeight);

            this.draggingScrollbar = true;

            if (mouseY >= thumbY && mouseY < thumbY + thumbHeight) {
                this.scrollbarDragOffsetY = Mth.floor(mouseY) - thumbY;
            } else {
                this.scrollbarDragOffsetY = thumbHeight / 2;
                updateScrollbarFromMouse(group, mouseY, trackY, trackHeight, thumbHeight);
            }

            return true;
        }

        int localY = Mth.floor(mouseY) - panelY - PANEL_PADDING;
        if (localY < 0) return true;

        int visibleIndex = localY / ENTRY_SIZE;
        if (visibleIndex >= visibleEntries) return true;

        int tabIndex = scrollOffset() + visibleIndex;
        if (tabIndex < 0 || tabIndex >= group.tabs().size()) return true;

        CreativeModeTab targetTab = group.tabs().get(tabIndex);
        if (targetTab == selectedTab) return true;

        ResourceLocation targetId = tabId(targetTab);
        if (targetId != null) CtlSubtabState.select(group.rootId(), targetId);


        ((CreativeModeInventoryScreenAccessor) screen).ctl$selectTab(targetTab);
        return true;
    }

    private static boolean isInsideScrollbar(double mouseX, double mouseY, SubtabGroup group, int panelX, int panelY, int panelHeight) {
        if (maxScroll(group) <= 0) return false;

        int x = scrollbarX(panelX);
        int y = panelY + PANEL_PADDING;
        int height = panelHeight - PANEL_PADDING * 2;

        return isInside(mouseX, mouseY, x, y, SCROLLBAR_WIDTH, height);
    }

    public boolean mouseDragged(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab, double mouseX, double mouseY, int button) {
        if (!this.draggingScrollbar || button != 0) return false;
        if (!Config.ENABLE_SUBTABS.get()) return false;

        SubtabGroup group = groupFor(selectedTab);
        if (group == null || maxScroll(group) <= 0) {
            this.draggingScrollbar = false;
            return false;
        }

        updateActiveGroup(selectedTab);

        int panelY = panelY(screen);
        int panelHeight = panelHeight(group);
        int trackY = panelY + PANEL_PADDING;
        int trackHeight = panelHeight - PANEL_PADDING * 2;
        int thumbHeight = scrollbarThumbHeight(group, trackHeight);

        updateScrollbarFromMouse(group, mouseY, trackY, trackHeight, thumbHeight);
        return true;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != 0 || !this.draggingScrollbar) return false;

        this.draggingScrollbar = false;
        return true;
    }

    private void updateScrollbarFromMouse(SubtabGroup group, double mouseY, int trackY, int trackHeight, int thumbHeight) {
        int maximumScroll = maxScroll(group);
        int travel = trackHeight - thumbHeight;

        if (maximumScroll <= 0 || travel <= 0) {
            setScrollOffset(0);
            return;
        }

        int thumbY = Mth.clamp(Mth.floor(mouseY) - this.scrollbarDragOffsetY, trackY, trackY + travel);
        int nextOffset = Math.round((thumbY - trackY) * (float) maximumScroll / travel);

        setScrollOffset(nextOffset);
    }

    public boolean mouseScrolled(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab, double mouseX, double mouseY, double scrollY) {
        if (!Config.ENABLE_SUBTABS.get()) return false;
        SubtabGroup group = groupFor(selectedTab);
        if (group == null) return false;

        updateActiveGroup(selectedTab);

        int panelX = panelX(screen, group);
        int panelY = panelY(screen);
        int panelHeight = panelHeight(group);

        if (!isInside(mouseX, mouseY, panelX, panelY, panelWidth(group), panelHeight)) return false;

        int maxScroll = maxScroll(group);
        if (maxScroll == 0) return true;

        int direction = Double.compare(0.0D, scrollY);
        if (direction == 0) return true;

        int previousOffset = scrollOffset();
        int nextOffset = Mth.clamp(previousOffset + direction, 0, maxScroll);

        setScrollOffset(nextOffset);
        return true;
    }

    private static int panelWidth(SubtabGroup group) {
        return PANEL_BASE_WIDTH + (maxScroll(group) > 0 ? SCROLLBAR_SPACE : 0);
    }

    public static boolean isSubtab(CreativeModeTab tab) {
        if (!Config.ENABLE_SUBTABS.get()) return false;
        ResourceLocation tabId = tabId(tab);
        return tabId != null && CtlSubtabRegistry.isSubtab(tabId);
    }

    public static boolean belongsToGroup(CreativeModeTab tab) {
        if (!Config.ENABLE_SUBTABS.get()) return false;
        return groupFor(tab) != null;
    }

    private int scrollOffset() {
        return activeRootId == null ? 0 : CtlSubtabState.scroll(activeRootId);
    }

    private void setScrollOffset(int scrollOffset) {
        if (activeRootId == null) return;

        SubtabGroup group = groupFor(activeRootId);
        int maximum = group == null ? 0 : maxScroll(group);

        CtlSubtabState.scroll(activeRootId, scrollOffset, maximum);
    }

    private void updateActiveGroup(CreativeModeTab selectedTab) {
        SubtabGroup group = groupFor(selectedTab);
        activeRootId = group == null ? null : group.rootId();
    }

    private void clampScroll(CreativeModeTab selectedTab) {
        SubtabGroup group = groupFor(selectedTab);

        if (group == null) {
            activeRootId = null;
            return;
        }

        CtlSubtabState.scroll(group.rootId(), scrollOffset(), maxScroll(group));
    }

    private static SubtabGroup groupFor(CreativeModeTab selectedTab) {
        ResourceLocation selectedTabId = tabId(selectedTab);
        return selectedTabId == null ? null : groupFor(selectedTabId);
    }

    private static SubtabGroup groupFor(ResourceLocation tabId) {
        ResourceLocation rootId = CtlSubtabRegistry.root(tabId);
        if (!CtlSubtabRegistry.hasSubtabs(rootId)) return null;

        CreativeModeTab rootTab = resolve(rootId);
        if (rootTab == null || !rootTab.shouldDisplay()) return null;

        List<CreativeModeTab> subtabs = new ArrayList<>();

        for (ResourceLocation subtabId : CtlSubtabRegistry.subtabs(rootId)) {
            CreativeModeTab subtab = resolve(subtabId);
            if (subtab != null && subtab.shouldDisplay()) subtabs.add(subtab);
        }

        subtabs.sort(Comparator.comparing((CreativeModeTab tab) -> tab.getDisplayName().getString(), String.CASE_INSENSITIVE_ORDER).thenComparing(tab -> {
            ResourceLocation id = tabId(tab);
            return id == null ? "" : id.toString();
        }));

        List<CreativeModeTab> tabs = new ArrayList<>(subtabs.size() + 1);
        tabs.add(rootTab);
        tabs.addAll(subtabs);

        return new SubtabGroup(rootId, List.copyOf(tabs));
    }

    private static int visibleEntries(SubtabGroup group) {
        return Math.min(group.tabs().size(), MAX_VISIBLE_ENTRIES);
    }

    private static int panelHeight(SubtabGroup group) {
        return PANEL_PADDING * 2 + ENTRY_SIZE * visibleEntries(group);
    }

    private static int maxScroll(SubtabGroup group) {
        return Math.max(0, group.tabs().size() - visibleEntries(group));
    }

    private static int panelX(CreativeModeInventoryScreen screen, SubtabGroup group) {
        return screen.getGuiLeft() - PANEL_GAP - panelWidth(group);
    }

    private static int panelY(CreativeModeInventoryScreen screen) {
        return screen.getGuiTop() + 17;
    }

    private static ResourceLocation tabId(CreativeModeTab tab) {
        return tab == null ? null : BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
    }

    private static CreativeModeTab resolve(ResourceLocation tabId) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.get(tabId);
    }

    private static boolean isInside(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private static void renderBorder(GuiGraphics graphics, int x, int y, int width, int height) {
        graphics.fill(x, y, x + width, y + 1, BORDER_COLOR);
        graphics.fill(x, y + height - 1, x + width, y + height, BORDER_COLOR);
        graphics.fill(x, y, x + 1, y + height, BORDER_COLOR);
        graphics.fill(x + width - 1, y, x + width, y + height, BORDER_COLOR);
    }

    private record SubtabGroup(ResourceLocation rootId, List<CreativeModeTab> tabs) {}
}