package com.ziver.tab_layouts.client.screen;

import com.ziver.tab_layouts.internal.config.CtlPageOrderConfig;
import com.ziver.tab_layouts.internal.layout.CtlPage;
import com.ziver.tab_layouts.internal.layout.CtlPageState;
import com.ziver.tab_layouts.internal.layout.CtlPageType;
import com.ziver.tab_layouts.internal.layout.CtlTabLayout;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * WIP prototype for a future CTL page organizer screen.
 *
 * Not registered or exposed in the current version.
 * Kept only as a reference for a possible future custom page order feature.
 */
public final class CtlPageOrganizerScreen extends Screen {
    private static final int PANEL_WIDTH = 340;
    private static final int CARD_HEIGHT = 22;
    private static final int CARD_GAP = 4;

    private static final int BUTTON_WIDTH = 24;
    private static final int BUTTON_HEIGHT = 20;

    private static final int COLOR_PANEL = 0xAA000000;
    private static final int COLOR_CARD = 0x55202020;
    private static final int COLOR_CARD_LOCKED = 0x55303010;
    private static final int COLOR_TEXT = 0xFFFFFFFF;
    private static final int COLOR_MUTED = 0xFFB8A978;
    private static final int COLOR_TITLE = 0xFFFFD76A;
    private static final int COLOR_UNSAVED = 0xFFFFAA55;

    private final Screen parent;
    private final ResourceLocation tabId;

    private final List<CtlPage> basePages = new ArrayList<>();
    private final List<CtlPage> addonPages = new ArrayList<>();

    private CtlPage overviewPage;
    private boolean dirty = false;

    public CtlPageOrganizerScreen(Screen parent, ResourceLocation tabId) {
        super(Component.literal("CTL Page Organizer"));
        this.parent = parent;
        this.tabId = tabId;
    }

    @Override
    protected void init() {
        reloadPages();
        rebuildWidgets();
    }

    private void reloadPages() {
        basePages.clear();
        addonPages.clear();
        overviewPage = null;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null) {
            return;
        }

        overviewPage = layout.overviewPage();
        basePages.addAll(layout.pagesOfType(CtlPageType.BASE));
        addonPages.addAll(layout.pagesOfType(CtlPageType.ADDON));
    }

    protected void rebuildWidgets() {
        clearWidgets();

        int panelX = panelX();
        int y = 50;

        if (overviewPage != null) {
            y += sectionHeight("Overview", 1);
        }

        if (!basePages.isEmpty()) {
            y = addGroupButtons(panelX, y + 14, basePages);
            y += 10;
        }

        if (!addonPages.isEmpty()) {
            y = addGroupButtons(panelX, y + 14, addonPages);
        }

        int bottomY = height - 28;

        addRenderableWidget(Button.builder(Component.literal("Sort Base A-Z"), button -> {
            sort(basePages);
            dirty = true;
            rebuildWidgets();
        }).pos(panelX, bottomY).size(90, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Sort Addon A-Z"), button -> {
            sort(addonPages);
            dirty = true;
            rebuildWidgets();
        }).pos(panelX + 94, bottomY).size(100, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Reset"), button -> {
            CtlPageOrderConfig.reset(tabId);
            CtlPageState.reset(tabId);
            dirty = false;
            reloadPages();
            rebuildWidgets();
        }).pos(panelX + 198, bottomY).size(50, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Save"), button -> {
            save();
            if (minecraft != null) minecraft.setScreen(parent);
        }).pos(panelX + PANEL_WIDTH - 88, bottomY).size(42, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Cancel"), button -> {
            if (minecraft != null) minecraft.setScreen(parent);
        }).pos(panelX + PANEL_WIDTH - 44, bottomY).size(44, 20).build());
    }

    private int addGroupButtons(int panelX, int startY, List<CtlPage> pages) {
        int y = startY;

        for (int i = 0; i < pages.size(); i++) {
            int index = i;
            int cardY = y;

            Button up = Button.builder(Component.literal("↑"), button -> {
                moveUp(pages, index);
                dirty = true;
                rebuildWidgets();
            }).pos(panelX + PANEL_WIDTH - BUTTON_WIDTH * 2 - 8, cardY + 1).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();

            up.active = index > 0;

            Button down = Button.builder(Component.literal("↓"), button -> {
                moveDown(pages, index);
                dirty = true;
                rebuildWidgets();
            }).pos(panelX + PANEL_WIDTH - BUTTON_WIDTH - 4, cardY + 1).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();

            down.active = index < pages.size() - 1;

            addRenderableWidget(up);
            addRenderableWidget(down);

            y += CARD_HEIGHT + CARD_GAP;
        }

        return y;
    }

    private void save() {
        CtlPageOrderConfig.setOrder(tabId, basePages.stream().map(CtlPage::id).toList(), addonPages.stream().map(CtlPage::id).toList());
        CtlPageState.reset(tabId);
        dirty = false;
    }

    private static void moveUp(List<CtlPage> pages, int index) {
        if (index <= 0 || index >= pages.size()) return;

        CtlPage page = pages.remove(index);
        pages.add(index - 1, page);
    }

    private static void moveDown(List<CtlPage> pages, int index) {
        if (index < 0 || index >= pages.size() - 1) return;
        CtlPage page = pages.remove(index);
        pages.add(index + 1, page);
    }

    private static void sort(List<CtlPage> pages) {
        pages.sort(Comparator.comparing(page -> page.title().getString(), String.CASE_INSENSITIVE_ORDER));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);

        int panelX = panelX();
        int panelY = 22;
        int panelHeight = height - 58;

        graphics.fill(panelX - 8, panelY, panelX + PANEL_WIDTH + 8, panelY + panelHeight, COLOR_PANEL);

        graphics.drawCenteredString(font, title, width / 2, 10, COLOR_TITLE);

        if (dirty) {
            graphics.drawString(font, Component.literal("Unsaved changes"), panelX, 32, COLOR_UNSAVED, false);
        } else {
            graphics.drawString(font, Component.literal("Saved"), panelX, 32, COLOR_MUTED, false);
        }

        int y = 50;

        if (overviewPage != null) {
            y = renderGroupTitle(graphics, "Overview", y);
            renderPageCard(graphics, overviewPage, panelX, y, true);
            y += CARD_HEIGHT + CARD_GAP + 14;
        }

        if (!basePages.isEmpty()) {
            y = renderGroupTitle(graphics, "Base Pages", y);
            y = renderPageCards(graphics, basePages, panelX, y, false);
            y += 10;
        }

        if (!addonPages.isEmpty()) {
            y = renderGroupTitle(graphics, "Addon Pages", y);
            renderPageCards(graphics, addonPages, panelX, y, false);
        }

    }

    private int renderGroupTitle(GuiGraphics graphics, String title, int y) {
        graphics.drawString(font, Component.literal(title), panelX(), y, COLOR_MUTED, false);
        return y + 12;
    }

    private int renderPageCards(GuiGraphics graphics, List<CtlPage> pages, int x, int startY, boolean locked) {
        int y = startY;

        for (CtlPage page : pages) {
            renderPageCard(graphics, page, x, y, locked);
            y += CARD_HEIGHT + CARD_GAP;
        }

        return y;
    }

    private void renderPageCard(GuiGraphics graphics, CtlPage page, int x, int y, boolean locked) {
        int color = locked ? COLOR_CARD_LOCKED : COLOR_CARD;

        graphics.fill(x, y, x + PANEL_WIDTH, y + CARD_HEIGHT, color);

        Component label = page.title();
        graphics.drawString(font, label, x + 6, y + 7, COLOR_TEXT, false);

        if (locked) {
            graphics.drawString(font, Component.literal("Locked"), x + PANEL_WIDTH - 44, y + 7, COLOR_MUTED, false);
        }
    }

    private int sectionHeight(String title, int count) {
        return 12 + count * (CARD_HEIGHT + CARD_GAP) + 14;
    }

    private int panelX() {
        return width / 2 - PANEL_WIDTH / 2;
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}