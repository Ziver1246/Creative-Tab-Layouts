package com.ziver.tab_layouts.client.screen;

import com.ziver.tab_layouts.Config;
import com.ziver.tab_layouts.client.render.CtlPageRenderState;
import com.ziver.tab_layouts.client.render.CtlSimpleFrameRenderer;
import com.ziver.tab_layouts.client.render.CtlVisualRenderer;
import com.ziver.tab_layouts.client.render.animation.CtlAnimationStateRegistry;
import com.ziver.tab_layouts.client.util.CtlUiLayouts;
import com.ziver.tab_layouts.internal.layout.*;
import com.ziver.tab_layouts.internal.registry.CtlTabRegistry;
import com.ziver.tab_layouts.mixins.CreativeModeInventoryScreenAccessor;
import com.ziver.tab_layouts.mixins.ItemPickerMenuAccessor;
import com.ziver.tab_layouts.mixins.ScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CtlPageScreenController {

    private static final int GROUP_OFFSET_X = 0;
    private static final int GROUP_OFFSET_Y = 170;

    private static final int BUTTON_WIDTH = 20;
    private static final int BUTTON_HEIGHT = 20;

    private static final int TEXT_BOX_WIDTH = 92;
    private static final int TEXT_BOX_HEIGHT = BUTTON_HEIGHT;

    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int BOX_COLOR = 0x33000000;

    private Button previousButton;
    private Button nextButton;

    private int boxX;
    private int boxY;
    private int boxWidth;
    private int boxHeight;

    private final Map<ResourceLocation, List<ItemStack>> vanillaItemsByTab = new HashMap<>();

    public void init(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab) {
        previousButton = Button.builder(Component.literal("<"), button -> {}).pos(0, 0).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();
        nextButton = Button.builder(Component.literal(">"), button -> {}).pos(0, 0).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();

        ((ScreenAccessor) screen).ctl$addRenderableWidget(previousButton);
        ((ScreenAccessor) screen).ctl$addRenderableWidget(nextButton);

        applyCurrentPage(screen, selectedTab);
        updateButtons(screen, selectedTab);
    }

    public boolean previous(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return false;

        if (!CtlTabRegistry.isControlled(tabId)) return false;
        if (!CtlPageState.previous(tabId)) return false;

        playSoundClick();
        applyCurrentPage(screen, selectedTab);
        updateButtons(screen, selectedTab);
        return true;
    }

    public boolean next(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return false;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null) return false;

        if (!CtlPageState.next(tabId, effectivePageCount(tabId, layout))) return false;

        playSoundClick();
        applyCurrentPage(screen, selectedTab);
        updateButtons(screen, selectedTab);
        return true;
    }

    public void applyCurrentPage(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab) {
        applyCurrentPage(screen, selectedTab, 0.0F);
    }

    private void applyCurrentPage(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab, float scrollOffs) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || layout.pageCount() == 0) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return;

        ItemPickerMenuAccessor menu = (ItemPickerMenuAccessor) screen.getMenu();

        if (!vanillaItemsByTab.containsKey(tabId))
            vanillaItemsByTab.put(tabId, copyCurrentMenuItems(menu));

        int effectivePageCount = effectivePageCount(tabId, layout);
        int pageIndex = CtlPageState.page(tabId);

        if (pageIndex >= effectivePageCount) {
            CtlPageState.reset(tabId);
            pageIndex = 0;
        }

        CtlBuiltPage builtPage;

        if (isFallbackPage(tabId, layout, pageIndex)) {
            List<ItemStack> vanillaItems = vanillaItemsByTab.getOrDefault(tabId, List.of());
            int fallbackIndex = fallbackPageIndex(layout, pageIndex);

            ResourceLocation fallbackPageId = CtlFallbackPageBuilder.pageId(tabId, layout, vanillaItems, minecraft.level.registryAccess(), fallbackIndex);
            builtPage = CtlFallbackPageBuilder.build(tabId, layout, vanillaItems, minecraft.level.registryAccess(), fallbackIndex, sectionId -> CtlSectionState.isCollapsed(tabId, fallbackPageId, sectionId));
        } else {
            CtlPage page = layout.page(pageIndex);
            if (page == null) return;

            builtPage = page.build(minecraft.level.registryAccess(), sectionId -> CtlSectionState.isCollapsed(tabId, page.id(), sectionId));
        }

        float restoredScroll = Mth.clamp(scrollOffs, 0.0F, 1.0F);

        CtlPageRenderState.set(tabId, pageIndex, builtPage.sections());

        menu.ctl$getItems().clear();
        menu.ctl$getItems().addAll(builtPage.items());
        menu.ctl$scrollTo(restoredScroll);

        ((CreativeModeInventoryScreenAccessor) screen).ctl$setScrollOffs(restoredScroll);
    }

    public void renderOverlay(CreativeModeInventoryScreen screen, GuiGraphics graphics, CreativeModeTab selectedTab, int mouseX, int mouseY) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || layout.pageCount() == 0) return;

        int pageIndex = CtlPageState.page(tabId);

        if (isFallbackPage(tabId, layout, pageIndex)) {
            Minecraft minecraft = screen.getMinecraft();
            if (minecraft.level == null) return;

            List<ItemStack> vanillaItems = vanillaItemsByTab.getOrDefault(tabId, List.of());
            int fallbackIndex = fallbackPageIndex(layout, pageIndex);

            ResourceLocation fallbackPageId = CtlFallbackPageBuilder.pageId(tabId, layout, vanillaItems, minecraft.level.registryAccess(), fallbackIndex);

            boolean collapsible = CtlPageRenderState.get(tabId, pageIndex).size() > 1;
            CtlSimpleFrameRenderer.renderHeaders(graphics, screen.getGuiLeft(), screen.getGuiTop(), selectedTab, fallbackPageId, collapsible, mouseX, mouseY);

            return;
        }

        CtlPage page = layout.page(pageIndex);
        if (page == null) return;

        switch (page.type()) {
            case OVERVIEW -> renderOverview(screen, graphics, tabId, pageIndex, page, mouseX, mouseY);
            case BASE, ADDON -> CtlSimpleFrameRenderer.renderHeaders(graphics, screen.getGuiLeft(), screen.getGuiTop(), selectedTab, page.id(), page.sections().size() > 1, mouseX, mouseY);
        }
    }

    private static void renderOverview(CreativeModeInventoryScreen screen, GuiGraphics graphics, ResourceLocation tabId, int pageIndex, CtlPage page, int mouseX, int mouseY) {
        int x = screen.getGuiLeft() + CtlUiLayouts.BANNER_X_OFFSET;
        int y = screen.getGuiTop() + CtlUiLayouts.BANNER_Y_OFFSET;
        int width = CtlUiLayouts.BANNER_WIDTH;
        int height = CtlUiLayouts.BANNER_HEIGHT;

        boolean hovering = mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
        boolean rendered = CtlVisualRenderer.renderBanner(graphics, CtlAnimationStateRegistry.CREATIVE_CONTEXT, tabId, pageIndex, page, x, y, width, height, hovering, mouseX, mouseY, true);

        if (!rendered) CtlSimpleFrameRenderer.renderBannerFrame(graphics, screen.getGuiLeft(), screen.getGuiTop());
    }

    public boolean renderPageTitle(CreativeModeInventoryScreen screen, GuiGraphics graphics, CreativeModeTab selectedTab) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return false;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || layout.pageCount() == 0) return false;

        int pageIndex = CtlPageState.page(tabId);

        Component title;

        if (isFallbackPage(tabId, layout, pageIndex)) {
            Minecraft minecraft = screen.getMinecraft();

            if (minecraft.level == null) {
                title = Component.literal("Mods");
            } else {
                List<ItemStack> vanillaItems = vanillaItemsByTab.getOrDefault(tabId, List.of());
                title = CtlFallbackPageBuilder.title(tabId, layout, vanillaItems, minecraft.level.registryAccess(), fallbackPageIndex(layout, pageIndex));
            }
        } else {
            CtlPage page = layout.page(pageIndex);
            if (page == null) return false;

            title = page.title();
        }

        graphics.drawString(screen.getMinecraft().font, title, 8, 6, selectedTab.getLabelColor(), false);

        return true;
    }

    public void renderPageCounter(CreativeModeInventoryScreen screen, GuiGraphics graphics, CreativeModeTab selectedTab) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null) return;

        int count = effectivePageCount(tabId, layout);
        if (count <= 1) return;

        updateLayout(screen);

        int page = CtlPageState.page(tabId) + 1;

        Component label = Component.translatable("screen.tab_layouts.tab_pages");
        Component number = Component.literal(page + " / " + count);

        int labelWidth = screen.getMinecraft().font.width(label);
        int numberWidth = screen.getMinecraft().font.width(number);

        int textWidth = labelWidth + 6 + numberWidth;
        int textX = boxX + (boxWidth - textWidth) / 2;
        int textY = boxY + (boxHeight - 8) / 2;

        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 250.0F);

        graphics.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, BOX_COLOR);
        graphics.drawString(screen.getMinecraft().font, label, textX, textY, TEXT_COLOR, false);
        graphics.drawString(screen.getMinecraft().font, number, textX + labelWidth + 6, textY, TEXT_COLOR, false);

        graphics.pose().popPose();
    }

    public void updateButtons(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab) {
        ResourceLocation tabId = tabId(selectedTab);
        CtlTabLayout layout = tabId == null ? null : CtlTabRegistry.get(tabId);

        int pageCount = layout == null ? 0 : effectivePageCount(tabId, layout);
        boolean visible = layout != null && pageCount > 1;

        updateLayout(screen);

        if (previousButton != null) {
            previousButton.setPosition(boxX - BUTTON_WIDTH, boxY);
            previousButton.visible = visible;
            previousButton.active = visible && CtlPageState.canPrevious(tabId);
        }

        if (nextButton != null) {
            nextButton.setPosition(boxX + boxWidth, boxY);
            nextButton.visible = visible;
            nextButton.active = visible && CtlPageState.canNext(tabId, pageCount);
        }
    }

    public boolean mouseClicked(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab, double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        if (toggleSection(screen, selectedTab, mouseX, mouseY)) return true;
        if (previousButton != null && previousButton.visible && previousButton.active && previousButton.isMouseOver(mouseX, mouseY)) return previous(screen, selectedTab);
        if (nextButton != null && nextButton.visible && nextButton.active && nextButton.isMouseOver(mouseX, mouseY)) return next(screen, selectedTab);

        return false;
    }

    private boolean toggleSection(CreativeModeInventoryScreen screen, CreativeModeTab selectedTab, double mouseX, double mouseY) {
        ResourceLocation tabId = tabId(selectedTab);
        if (tabId == null) return false;

        CtlTabLayout layout = CtlTabRegistry.get(tabId);
        if (layout == null || layout.pageCount() == 0) return false;

        Minecraft minecraft = screen.getMinecraft();
        if (minecraft.level == null) return false;

        int pageIndex = CtlPageState.page(tabId);
        ResourceLocation pageId;
        boolean collapsible;

        if (isFallbackPage(tabId, layout, pageIndex)) {
            List<ItemStack> vanillaItems = vanillaItemsByTab.getOrDefault(tabId, List.of());
            int fallbackIndex = fallbackPageIndex(layout, pageIndex);

            pageId = CtlFallbackPageBuilder.pageId(tabId, layout, vanillaItems, minecraft.level.registryAccess(), fallbackIndex);

            collapsible = CtlPageRenderState.get(tabId, pageIndex).size() > 1;
        } else {
            CtlPage page = layout.page(pageIndex);

            if (page == null || page.type() == CtlPageType.OVERVIEW) return false;

            pageId = page.id();
            collapsible = page.sections().size() > 1;
        }

        if (!collapsible) return false;

        int baseX = screen.getGuiLeft() + CtlUiLayouts.HEADER_BASE_X_OFFSET;
        int baseY = screen.getGuiTop() + CtlUiLayouts.HEADER_BASE_Y_OFFSET;

        for (CtlRenderedSection rendered : CtlPageRenderState.get(tabId, pageIndex)) {
            int visibleRow = rendered.row() - CtlPageRenderState.currentRow;
            if (visibleRow < 0 || visibleRow >= CtlUiLayouts.GRID_VISIBLE_ROWS) continue;

            int headerY = baseY + visibleRow * CtlUiLayouts.HEADER_ROW_HEIGHT;

            boolean hovering = mouseX >= baseX && mouseX < baseX + CtlUiLayouts.HEADER_ROW_WIDTH && mouseY >= headerY && mouseY < headerY + CtlUiLayouts.HEADER_ROW_HEIGHT;

            if (!hovering) continue;

            float scrollOffs = ((CreativeModeInventoryScreenAccessor) screen).ctl$getScrollOffs();

            CtlSectionState.toggle(tabId, pageId, rendered.section().id());

            playSoundClick();
            applyCurrentPage(screen, selectedTab, scrollOffs);
            updateButtons(screen, selectedTab);
            return true;
        }

        return false;
    }

    private void updateLayout(CreativeModeInventoryScreen screen) {
        int centerX = screen.getGuiLeft() + screen.getXSize() / 2 + GROUP_OFFSET_X;

        boxWidth = TEXT_BOX_WIDTH;
        boxHeight = TEXT_BOX_HEIGHT;
        boxX = centerX - boxWidth / 2;
        boxY = screen.getGuiTop() + GROUP_OFFSET_Y;
    }

    private int effectivePageCount(ResourceLocation tabId, CtlTabLayout layout) {
        Minecraft minecraft = Minecraft.getInstance();

        if (!Config.ENABLE_FALLBACK_PAGES.get()) return layout.pageCount();
        if (minecraft.level == null) return layout.pageCount();

        List<ItemStack> vanillaItems = vanillaItemsByTab.getOrDefault(tabId, List.of());
        int fallbackPageCount = CtlFallbackPageBuilder.pageCount(tabId, layout, vanillaItems, minecraft.level.registryAccess());

        return layout.pageCount() + fallbackPageCount;
    }

    private boolean isFallbackPage(ResourceLocation tabId, CtlTabLayout layout, int pageIndex) {
        return Config.ENABLE_FALLBACK_PAGES.get() && pageIndex >= layout.pageCount() && pageIndex < effectivePageCount(tabId, layout);
    }

    private int fallbackPageIndex(CtlTabLayout layout, int pageIndex) {
        return pageIndex - layout.pageCount();
    }

    private static List<ItemStack> copyCurrentMenuItems(ItemPickerMenuAccessor menu) {
        List<ItemStack> items = new ArrayList<>();

        for (ItemStack stack : menu.ctl$getItems()) {
            if (!stack.isEmpty()) items.add(stack.copy());
        }

        return items;
    }

    private static ResourceLocation tabId(CreativeModeTab tab) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
    }

    private static void playSoundClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}